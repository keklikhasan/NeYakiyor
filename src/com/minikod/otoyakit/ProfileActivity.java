package com.minikod.otoyakit;

import com.minikod.utils.ResourceUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ProfileActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(ResourceUtil.getText(R.string.help_activity_label)).setIcon(
				android.R.drawable.ic_menu_help);
		menu.add(ResourceUtil.getText(R.string.history_activity_label)).setIcon(
				android.R.drawable.ic_menu_agenda);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals(
				ResourceUtil.getText(R.string.help_activity_label))) {
			toHelp();
		} else if (item.getTitle().equals(
				ResourceUtil.getText(R.string.history_activity_label))) {
			toPast();
		}
		return super.onOptionsItemSelected(item);
	}

	public void toHelp() {
		Intent intent = new Intent();
		intent.setClass(ProfileActivity.this, HelpActivity.class);
		startActivity(intent);
		finish();
	}

	public void toPast() {
		Intent intent = new Intent();
		intent.setClass(ProfileActivity.this, HistoryActivity.class);
		startActivity(intent);
		finish();
	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.setClass(ProfileActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}	

}
