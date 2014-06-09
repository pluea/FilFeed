package com.pluea.rssfilterreader.task;
  
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.xmlpull.v1.XmlPullParser;

import com.example.rssfilterreader.R;
import com.pleua.rssfilterreader.rss.Feed;
import com.pleua.rssfilterreader.rss.RssParser;
import com.pluea.rssfilterreader.db.DatabaseAdapter;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Xml;
import android.widget.Toast;
  
/**
 * 
 * @author kyamaguchi
 * @param  String : data type for task(ex.download URL)
 * @param  String : data type for displaying the progress
 * @param  Feed : submit data type when task will finish 
 */
public class InsertNewFeedTask extends AsyncTask<String, String, Feed>{
  
    private ListActivity   activity_;
    private ProgressDialog progress_;
    private DatabaseAdapter dbAdapter;
    private RssParser rssParser;
      
    public InsertNewFeedTask(ListActivity activity) {
        activity_ = activity;
        dbAdapter  = new DatabaseAdapter(activity_);
        rssParser  = new RssParser(activity_);
    }
  
    /**
     * Execute on main thread
     */
    @Override
    protected void onPostExecute(Feed result) {
        progress_.dismiss();
        if(result == null) {
        	Toast.makeText(activity_, R.string.add_feed_error, Toast.LENGTH_SHORT).show();
        }else {
        	Toast.makeText(activity_, R.string.add_feed_success, Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Execute before doing task
     */
    @Override
    protected void onPreExecute() {
        //Display a dialog
        progress_ = new ProgressDialog(activity_);
        progress_.setMessage("Now loading");
        progress_.show();
    }
  
    @Override
    protected void onProgressUpdate(String... values) {
        // TODO Auto-generated method stub
        super.onProgressUpdate(values);
    }
      
    /**
     * Get articles from RSS Feed
     */
    @Override
    protected Feed doInBackground(String... setting ) {
        Feed newFeed = null;
        try {
            //Set URLConnection
            String urlString = setting[0]; 
            URL url = new URL(urlString);
            URLConnection con = url.openConnection();
            //Set Timeout 1min
            con.setConnectTimeout(60000);
            con.setReadTimeout(60000);
              
            //Get InputStream
            InputStream is = con.getInputStream();
              
            //Judge XML
            newFeed = judgeXml(is, urlString);
            
            //Update new feed
            if(newFeed != null) {
	            //Get Feed id from feed URL
	            Feed feed = dbAdapter.getFeedByUrl(urlString);
	            int feedId = feed.getId();
	            
	        	//Parse XML and get new Articles
	            rssParser.parseXml(urlString, feedId);
	            
	            //Set num of unread articles
	            newFeed.setUnreadArticlesCount(dbAdapter.calcNumOfUnreadArticles(newFeed.getId()));
            }
            
            return newFeed;
        }catch(Exception e) {
            e.printStackTrace();
        }
        return newFeed;
    }
      
    private Feed judgeXml(InputStream is,String feedUrl) throws IOException{
        try {
            //Initialize XmlPullParser
            XmlPullParser parser = Xml.newPullParser();
              
            //Whether FileType is RSS
            boolean rssFlag = false;
            //RSS Format
            String format = new String();
              
            //RSS Format is 
            //<feed><title>�E�E�E or <rdf(or rss)><channel><title>�E�E�E , 
            //so check start tag <feed> or <rdf> or <rss> and then <title>
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tag = parser.getName();
                if(eventType == XmlPullParser.START_TAG) {
                      
                    //Atom
                    /*if(tag.toLowerCase().equals("feed")) {
                        rssFlag = true;
                        format  = "Atom";
                    }else */if(tag.toLowerCase().equals("rdf")){
                        //RSS 1.0,2.0
                        rssFlag = true;
                        format  = "RSS1.0";
                    }else if(tag.toLowerCase().equals("rss")) {
                        rssFlag = true;
                        format  = "RSS2.0";
                    }
                      
                    if(rssFlag && tag.equals("title")) {
                        //Feed title
                        String feedTitle = parser.nextText();
                        return dbAdapter.saveNewFeed(feedTitle,feedUrl,format);
                    }
                      
                }
                eventType = parser.next();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
      
 
}