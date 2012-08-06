package com.minikod.otoyakit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.minikod.bean.SessionBean;
import com.minikod.dao.RecordDao;
import com.minikod.modals.Profile;
import com.minikod.modals.Record;
import com.minikod.utils.MessageUtil;
import com.minikod.utils.ResourceUtil;

public class ProfileHistoryActivity extends Activity {
	public static Context context;
	public List<Record> list = new ArrayList<Record>();
	public Profile profile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		context = this;
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		init();
	}

	public void init() {
		profile = (Profile) SessionBean.getObject("selectedProfile");
		if (profile != null) {
			SimpleDateFormat f2 = new SimpleDateFormat("yyyy.MM.dd");
			ListView listView = (ListView) findViewById(R.id.mylist);
			RecordDao dao = new RecordDao(this);
			String[] values = new String[0];
			try {
				list = dao.getRecordListbyProfile(profile.getId());
				values = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					Record rec = list.get(i);
					values[i] = f2.format(rec.getDate());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1,
					values);
			listView.setAdapter(adapter);

			listView.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					SessionBean.setObject("selectedRecord", list.get(arg2));
					toRecordHistory();
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}

			});

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					SessionBean.setObject("selectedRecord", list.get(arg2));
					toRecordHistory();
				}
			});

			listView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					final int indx = arg2;
					new AlertDialog.Builder(context)
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setTitle(R.string.pro_history_activity_warn_head)
							.setMessage(R.string.pro_history_activity_warn_del)
							.setPositiveButton(R.string.app_yes,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											RecordDao dao = new RecordDao(
													context);
											try {
												dao.deleteRecord(list.get(indx)
														.getId());
												MessageUtil
														.makeToast(
																context,
																ResourceUtil
																		.getText(R.string.pro_history_activity_del_suc),
																false);
											} catch (Exception e) {
												MessageUtil.makeToast(context,
														e.toString(), false);
											} finally {
												dao.close();
											}
										}

									}).setNegativeButton(R.string.app_no, null)
							.show();
					return false;
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(ResourceUtil.getText(R.string.help_activity_label)).setIcon(
				android.R.drawable.ic_menu_help);
		menu.add(ResourceUtil.getText(R.string.aboutus_activity_label))
				.setIcon(android.R.drawable.ic_menu_gallery);
		menu.add(ResourceUtil.getText(R.string.pro_history_activity_clean))
				.setIcon(android.R.drawable.ic_delete);
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
				ResourceUtil.getText(R.string.pro_history_activity_clean))) {
			clear();
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

	public void clear() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(
						ResourceUtil
								.getText(R.string.pro_history_activity_warn_head))
				.setMessage(
						ResourceUtil
								.getText(R.string.pro_history_activity_clean_q))
				.setPositiveButton(ResourceUtil.getText(R.string.app_yes),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								RecordDao dao = new RecordDao(context);
								try {
									dao.clearProfileRecords(profile.getId());
									String[] values = new String[0];
									ListView listView = (ListView) findViewById(R.id.mylist);
									ArrayAdapter<String> adapter = new ArrayAdapter<String>(
											context,
											android.R.layout.simple_list_item_1,
											android.R.id.text1, values);
									listView.setAdapter(adapter);
									Toast.makeText(
											context,
											ResourceUtil
													.getText(R.string.pro_history_activity_clean_suc),
											Toast.LENGTH_SHORT).show();
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
		intent.setClass(ProfileHistoryActivity.this, HelpActivity.class);
		startActivity(intent);
	}

	public void toAbout() {
		Intent intent = new Intent();
		intent.setClass(ProfileHistoryActivity.this, AboutUsActivity.class);
		startActivity(intent);
	}

	public void toRecordHistory() {
		Intent intent = new Intent();
		intent.setClass(ProfileHistoryActivity.this,
				RecordHistoryActivity.class);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}
}
