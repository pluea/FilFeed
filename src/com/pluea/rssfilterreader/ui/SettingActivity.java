package com.pluea.rssfilterreader.ui;
  
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rssfilterreader.R;
import com.pluea.rssfilterreader.alarm.AlarmManagerTaskManager;
import com.pluea.rssfilterreader.util.PreferenceManager;
  
public class SettingActivity extends Activity {
  
	private CheckBox cbSortNewArticleTop;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        
        initView();
        setLitener();
	}
    
    private void initView() {
    	PreferenceManager mgr = PreferenceManager.getInstance(getApplicationContext());
    	
    	EditText input = (EditText)findViewById(R.id.inputInterval);
    	int savedInterval = mgr.getAutoUpdateInterval();
    	input.setText(String.valueOf(savedInterval));
    	
    	cbSortNewArticleTop = (CheckBox)findViewById(R.id.cb_article_sort);
    	cbSortNewArticleTop.setChecked(mgr.getSortNewArticleTop());
    }
    
    private void setLitener() {
    	Button saveButton = (Button)findViewById(R.id.saveButton);
    	saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText intervalText = (EditText)findViewById(R.id.inputInterval);
				String input = intervalText.getText().toString();
				PreferenceManager mgr = PreferenceManager.getInstance(getApplicationContext());
				mgr.setAutoUpdateInterval(Integer.valueOf(input));
				
				mgr.setSortNewArticleTop(cbSortNewArticleTop.isChecked());
				
				AlarmManagerTaskManager.setNewAlarm(getApplicationContext());
				
				Toast.makeText(getApplicationContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
			}
		});
    }

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
    
    
}