package com.phicdy.mycuration.task;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.phicdy.mycuration.db.DatabaseAdapter;
import com.phicdy.mycuration.rss.Article;
import com.phicdy.mycuration.rss.Feed;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GetHatenaBookmarkTest {

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getTargetContext();
        DatabaseAdapter adapter = DatabaseAdapter.getInstance(context);
        adapter.deleteAll();
    }

    @After
    public void tearDown() {
        Context context = InstrumentationRegistry.getTargetContext();
        DatabaseAdapter adapter = DatabaseAdapter.getInstance(context);
        adapter.deleteAll();
    }

    @Test
    public void MyQiitaArticleReturns0() {
        Context context = InstrumentationRegistry.getTargetContext();
        DatabaseAdapter adapter = DatabaseAdapter.getInstance(context);
        Feed testFeed = adapter.saveNewFeed("test", "http://hoge.com", "hoge", "");
        ArrayList<Article> articles = new ArrayList<>();
        String testUrl = "http://qiita.com/phicdy/items/1bcce3d6f040fc48f7bf";
        articles.add(new Article(1, "hoge", testUrl, Article.UNREAD, "", 1, testFeed.getId(), "", ""));
        adapter.saveNewArticles(articles, testFeed.getId());

        GetHatenaBookmark getHatenaBookmark = new GetHatenaBookmark(adapter);
        getHatenaBookmark.request(testUrl, 0);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThat(adapter.getAllUnreadArticles(true).get(0).getPoint(), is("0"));
    }

    @Test
    public void MyBlogArticleReturns1() {
        Context context = InstrumentationRegistry.getTargetContext();
        DatabaseAdapter adapter = DatabaseAdapter.getInstance(context);

        // Save test feed and article
        Feed testFeed = adapter.saveNewFeed("test", "http://hoge.com", "hoge", "");
        ArrayList<Article> articles = new ArrayList<>();
        String testUrl = "http://phicdy.hatenablog.com/entry/2014/09/01/214055";
        articles.add(new Article(1, "hoge", testUrl, Article.UNREAD, "", 1, testFeed.getId(), "", ""));
        adapter.saveNewArticles(articles, testFeed.getId());

        // Start request
        GetHatenaBookmark getHatenaBookmark = new GetHatenaBookmark(adapter);
        getHatenaBookmark.request(testUrl, 0);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThat(adapter.getAllUnreadArticles(true).get(0).getPoint(), is("1"));
    }
}
