package com.minikod.otoyakit;

import com.minikod.utils.ResourceUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(ResourceUtil.getText(R.string.history_activity_label)).setIcon(
				android.R.drawable.ic_menu_agenda);
		menu.add(ResourceUtil.getText(R.string.aboutus_activity_label)).setIcon(
				android.R.drawable.ic_menu_gallery);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals(
				ResourceUtil.getText(R.string.history_activity_label))) {
			toPast();
		} else if (item.getTitle().equals(
				ResourceUtil.getText(R.string.aboutus_activity_label))) {
			toAbout();
		}
		return super.onOptionsItemSelected(item);
	}


	public void toPast() {
		Intent intent = new Intent();
		intent.setClass(HelpActivity.this, HistoryActivity.class);
		startActivity(intent);
		finish();
	}
	
	public void toAbout() {
		Intent intent = new Intent();
		intent.setClass(HelpActivity.this, AboutUsActivity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.setClass(HelpActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
