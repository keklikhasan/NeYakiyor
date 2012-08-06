package com.minikod.otoyakit;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.minikod.bean.SessionBean;
import com.minikod.dao.ProfileDao;
import com.minikod.dao.RecordDao;
import com.minikod.exception.DaoServiceException;
import com.minikod.modals.Profile;
import com.minikod.modals.Record;
import com.minikod.utils.MessageUtil;
import com.minikod.utils.ResourceUtil;

public class ResultActivity extends Activity {

	public static TextView text100km;
	public static TextView text1km;
	public static Button save;
	public static Record record;
	public Double val100km;
	public Double val1km;
	private int mYear;
	private int mMonth;
	private int mDay;
	static final int DATE_DIALOG_ID = 0;
	public static Context context;
	public int saved = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		context = this;
		record = (Record) SessionBean.getObject("record");

		val100km = (100 / record.getKm()) * record.getFuel();
		val1km = (record.getFuel() / record.getKm()) * record.getUnitPrice();

		text100km = (TextView) findViewById(R.id.textField_100_km_i);
		text1km = (TextView) findViewById(R.id.textField_1_km_i);
		save = (Button) findViewById(R.id.save);
		DecimalFormat f = new DecimalFormat("#0.000");

		text100km.setText(f.format(val100km));
		text1km.setText(f.format(val1km));

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (saved == 0 && record != null) {
					Profile pro = (Profile) SessionBean.getObject("profile");
					if (pro == null) {
						ProfileDao dao = new ProfileDao(context);
						pro = dao.getActiveProfile();
						dao.close();
					}
					if (pro != null) {
						// pick profile
						final AlertDialog.Builder alert = new AlertDialog.Builder(
								context);
						alert.setTitle(ResourceUtil
								.getText(R.string.result_activity_which_pro));
						final Spinner inputs = new Spinner(context);

						ProfileDao dao = new ProfileDao(context);
						try {
							List<Profile> list = dao.getProfileList();
							List<String> adp = new ArrayList<String>();
							int indx = 0;
							for (int i = 0; i < list.size(); i++) {
								adp.add(list.get(i).getCode());
								if (pro.getCode().equals(list.get(i).getCode())) {
									indx = i;
								}
							}
							ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
									context,
									android.R.layout.simple_spinner_item, adp);
							dataAdapter
									.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							inputs.setAdapter(dataAdapter);
							inputs.setSelection(indx);
						} catch (Exception e) {
							MessageUtil.makeToast(context, e.toString(), false);
						} finally {
							dao.close();
						}

						alert.setView(inputs);
						alert.setPositiveButton(ResourceUtil
								.getText(R.string.result_activity_choose_b),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										ProfileDao dao = new ProfileDao(context);
										try {
											Profile pro = dao.getProfile(inputs
													.getSelectedItem()
													.toString());
											pro.setFuel_type(record.getType());
											dao.update(pro);
											pro = dao.getProfile(pro.getCode());
											SessionBean.setObject("profile",
													pro);
											showDialog(DATE_DIALOG_ID);
										} catch (Exception e) {
											MessageUtil.makeToast(context,
													e.toString(), false);
										} finally {
											dao.close();
										}
									}
								});
						alert.show();
					} else {
						new AlertDialog.Builder(context)
								.setIcon(android.R.drawable.ic_dialog_alert)
								.setMessage(R.string.result_activity_save_warn)
								.setPositiveButton(R.string.app_yes,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// dialog textbox ac plaka al
												// kaydet recordu ekle
												final AlertDialog.Builder alert = new AlertDialog.Builder(
														context);
												alert.setTitle(ResourceUtil
														.getText(R.string.result_activity_new_pro_add));
												final EditText input = new EditText(
														context);
												input.setHint(ResourceUtil
														.getText(R.string.result_activity_new_pro_hint));
												alert.setView(input);
												alert.setPositiveButton(
														ResourceUtil
																.getText(R.string.result_activity_save_b),
														new DialogInterface.OnClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int whichButton) {
																// kaydet
																// profili ve
																// recordu make
																// toast mesage
																Profile pro = new Profile();
																pro.setCode(input
																		.getText()
																		.toString());
																pro.setActive(true);
																pro.setFuel_type(record
																		.getType());
																ProfileDao dao = new ProfileDao(
																		context);
																try {
																	dao.save(pro);
																	pro = dao
																			.getProfile(pro
																					.getCode());
																	SessionBean
																			.setObject(
																					"profile",
																					pro);
																	showDialog(DATE_DIALOG_ID);
																} catch (DaoServiceException e) {
																	MessageUtil
																			.makeToast(
																					context,
																					e.toString(),
																					false);
																} finally {
																	dao.close();
																}
															}
														});
												alert.show();

											}
										})
								.setNegativeButton(R.string.app_no, null)
								.show();
					}
				}
			}
		});

		// get the current date
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			// save data
			record.setDate(new Date(mYear - 1900, mMonth, mDay));
			RecordDao dao = new RecordDao(context);
			try {
				Profile pro = (Profile) SessionBean.getObject("profile");
				record.setProfile(pro.getId());
				dao.save(record);
				Toast.makeText(
						context,
						ResourceUtil.getText(R.string.result_activity_save_suc),
						Toast.LENGTH_SHORT).show();
				saved = 1;
			} catch (DaoServiceException e) {
				e.printStackTrace();
				Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT)
						.show();
			} finally {
				dao.close();
			}

		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(ResourceUtil.getText(R.string.help_activity_label)).setIcon(
				android.R.drawable.ic_menu_help);
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
				ResourceUtil.getText(R.string.help_activity_label))) {
			toHelp();
		} else if (item.getTitle().equals(
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

	public void toHelp() {
		Intent intent = new Intent();
		intent.setClass(ResultActivity.this, HelpActivity.class);
		startActivity(intent);
		saved = 0;
	}

	public void toPast() {
		Intent intent = new Intent();
		intent.setClass(ResultActivity.this, HistoryActivity.class);
		startActivity(intent);
		saved = 0;
	}

	public void toAbout() {
		Intent intent = new Intent();
		intent.setClass(ResultActivity.this, AboutUsActivity.class);
		startActivity(intent);
		saved = 0;
	}

	@Override
	public void onBackPressed() {
		saved = 0;
		finish();
		super.onBackPressed();
	}
}
