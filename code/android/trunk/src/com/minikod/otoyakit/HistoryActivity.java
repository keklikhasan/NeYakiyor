package com.minikod.otoyakit;

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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.minikod.bean.SessionBean;
import com.minikod.dao.ProfileDao;
import com.minikod.dao.RecordDao;
import com.minikod.exception.DaoServiceException;
import com.minikod.modals.Profile;
import com.minikod.utils.MessageUtil;
import com.minikod.utils.ResourceUtil;

public class HistoryActivity extends Activity {
	public static Context context;
	public  List<Profile> list = new ArrayList<Profile>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pro_listview);
		context = this;
		init();
	}

	public void init() {
		Button newPro = (Button) findViewById(R.id.add_new_pro);
		newPro.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// dialog textbox ac plaka al
				// kaydet recordu ekle
				final AlertDialog.Builder alert = new AlertDialog.Builder(
						context);
				alert.setTitle(ResourceUtil
						.getText(R.string.result_activity_new_pro_add));
				final EditText input = new EditText(context);
				input.setHint(ResourceUtil
						.getText(R.string.result_activity_new_pro_hint));
				alert.setView(input);
				alert.setPositiveButton(
						ResourceUtil.getText(R.string.result_activity_save_b),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// kaydet
								// profili make
								// toast mesage
								Profile pro = new Profile();
								pro.setCode(input.getText().toString());
								pro.setActive(true);
								pro.setFuel_type(0);
								ProfileDao dao = new ProfileDao(context);
								try {
									dao.save(pro);
									MessageUtil.makeToast(
											context,
											ResourceUtil
													.getText(R.string.history_activity_pro_suc),
											false);
									init();
								} catch (DaoServiceException e) {
									MessageUtil.makeToast(context,
											e.toString(), false);
								} finally {
									dao.close();
								}
							}
						});
				alert.show();

			}
		});

		ListView listView = (ListView) findViewById(R.id.mylist);
		ProfileDao dao = new ProfileDao(this);
		String[] values = new String[0];
		try {
			list = dao.getProfileList();
			values = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				Profile pro = list.get(i);
				values[i] = pro.getCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);
		listView.setAdapter(adapter);

		listView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				SessionBean.setObject("selectedProfile", list.get(arg2));
				toProfileHistory();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				SessionBean.setObject("selectedProfile", list.get(arg2));
				toProfileHistory();
			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				final int indx = arg2;
				new AlertDialog.Builder(context)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle(R.string.history_activity_warn_head)
						.setMessage(R.string.history_activity_warn_del)
						.setPositiveButton(R.string.app_yes,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										ProfileDao dao = new ProfileDao(context);
										RecordDao dao2 = new RecordDao(context);
										try {
											dao.deleteProfile(list.get(indx)
													.getId());
											dao2.clearProfileRecords(list.get(
													indx).getId());
											MessageUtil
													.makeToast(
															context,
															ResourceUtil
																	.getText(R.string.history_activity_del_suc),
															false);
											init();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(ResourceUtil.getText(R.string.help_activity_label)).setIcon(
				android.R.drawable.ic_menu_help);
		menu.add(ResourceUtil.getText(R.string.aboutus_activity_label))
				.setIcon(android.R.drawable.ic_menu_gallery);
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
		}
		else if (item.getTitle().equals(
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

	public void toHelp() {
		Intent intent = new Intent();
		intent.setClass(HistoryActivity.this, HelpActivity.class);
		startActivity(intent);
		finish();
	}

	public void toAbout() {
		Intent intent = new Intent();
		intent.setClass(HistoryActivity.this, AboutUsActivity.class);
		startActivity(intent);
		finish();
	}

	public void toProfileHistory() {
		Intent intent = new Intent();
		intent.setClass(HistoryActivity.this, ProfileHistoryActivity.class);
		startActivity(intent);
		// finish();
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}
}
