package com.minikod.otoyakit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.minikod.utils.ResourceUtil;

public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(ResourceUtil.getText(R.string.history_activity_label))
				.setIcon(android.R.drawable.ic_menu_agenda);
		menu.add(ResourceUtil.getText(R.string.aboutus_activity_label))
				.setIcon(android.R.drawable.ic_menu_gallery);
		menu.add(ResourceUtil.getText(R.string.app_exit)).setIcon(
				android.R.drawable.ic_lock_power_off);
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
		} else if (item.getTitle().equals(
				ResourceUtil.getText(R.string.app_exit))) {
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.pro_history_activity_warn_head)
					.setMessage(R.string.app_exit_q)
					.setPositiveButton(R.string.app_yes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									System.exit(1);
								}

							}).setNegativeButton(R.string.app_no, null).show();
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
		finish();
		super.onBackPressed();
	}
}
