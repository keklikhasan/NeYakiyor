package com.minikod.otoyakit;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.minikod.bean.SessionBean;
import com.minikod.dao.RecordDao;
import com.minikod.modals.Record;
import com.minikod.utils.ResourceUtil;

public class RecordHistoryActivity extends Activity {
	public static Context context;
	public static Record record;
	public static TextView text100km;
	public static TextView text1km;
	public Double val100km;
	public Double val1km;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_result);
		context = this;
		record = (Record) SessionBean.getObject("selectedRecord");
		SessionBean.setObject("selectedRecord", null);
		if (record != null) {
			val100km = (100 / record.getKm()) * record.getFuel();
			val1km = (record.getFuel() / record.getKm())
					* record.getUnitPrice();

			text100km = (TextView) findViewById(R.id.textField_100_km_i);
			text1km = (TextView) findViewById(R.id.textField_1_km_i);
			DecimalFormat f = new DecimalFormat("#0.000");

			text100km.setText(f.format(val100km));
			text1km.setText(f.format(val1km));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(ResourceUtil.getText(R.string.help_activity_label)).setIcon(
				android.R.drawable.ic_menu_help);
		menu.add(ResourceUtil.getText(R.string.aboutus_activity_label))
				.setIcon(android.R.drawable.ic_menu_gallery);
		menu.add(ResourceUtil.getText(R.string.rec_history_menu_del)).setIcon(
				android.R.drawable.ic_delete);
		menu.add(ResourceUtil.getText(R.string.app_exit)).setIcon(
				android.R.drawable.ic_lock_power_off);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals(
				ResourceUtil.getText(R.string.help_activity_label))) {
			toHelp();
		} else if (item.getTitle().equals(
				ResourceUtil.getText(R.string.aboutus_activity_label))) {
			toAbout();
		} else if (item.getTitle().equals(
				ResourceUtil.getText(R.string.rec_history_menu_del))) {
			delete();
		}else if (item.getTitle().equals(
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

	public void delete() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(ResourceUtil.getText(R.string.rec_history_warn))
				.setMessage(ResourceUtil.getText(R.string.rec_history_del_q))
				.setPositiveButton(ResourceUtil.getText(R.string.app_yes),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								RecordDao dao = new RecordDao(context);
								try {
									dao.deleteRecord(record.getId());
									Toast.makeText(
											context,
											ResourceUtil
													.getText(R.string.rec_history_del_suc),
											Toast.LENGTH_SHORT).show();
									finish();
								} catch (Exception e) {
									Toast.makeText(context, e.toString(),
											Toast.LENGTH_SHORT).show();
								} finally {
									dao.close();
								}
							}

						})
				.setNegativeButton(ResourceUtil.getText(R.string.app_no), null)
				.show();

	}

	public void toHelp() {
		Intent intent = new Intent();
		intent.setClass(RecordHistoryActivity.this, HelpActivity.class);
		startActivity(intent);
	}

	public void toAbout() {
		Intent intent = new Intent();
		intent.setClass(RecordHistoryActivity.this, AboutUsActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}
}
