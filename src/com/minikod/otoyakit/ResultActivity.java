package com.minikod.otoyakit;

import java.text.DecimalFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.minikod.bean.SessionBean;
import com.minikod.dao.RecordDao;
import com.minikod.exception.DaoServiceException;
import com.minikod.modals.Record;
import com.minikod.utils.ResourceUtil;

public class ResultActivity extends Activity {

	public static TextView text1;
	public static TextView text2;
	public static Button save;
	public static Double totYakit;
	public static Double totKm;
	public static Double totBirim;
	public Double temp1;
	public Double temp2;
	private int mYear;
	private int mMonth;
	private int mDay;
	static final int DATE_DIALOG_ID = 0;
	public static Context context;
	public static int saved = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		context = this;
		totYakit = (Double) SessionBean.getObject("totYakit");
		totKm = (Double) SessionBean.getObject("totKm");
		totBirim = (Double) SessionBean.getObject("totBirim");

		temp1 = (100 / totKm) * totYakit;
		temp2 = (totYakit / totKm) * totBirim;

		text1 = (TextView) findViewById(R.id.textField1);
		text2 = (TextView) findViewById(R.id.textField2);
		save = (Button) findViewById(R.id.save);
		DecimalFormat f = new DecimalFormat("##.000");

		text1.setText(f.format(temp1));
		text2.setText(f.format(temp2));

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (saved == 0) {
					showDialog(DATE_DIALOG_ID);
				} else {
					Toast.makeText(context, "Bu kaydı daha once kaydettiniz",
							Toast.LENGTH_SHORT).show();
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
			Record rec = new Record();
//			rec.setTotBirim(totBirim);
//			rec.setTotKm(totKm);
//			rec.setTotYakit(totYakit);
//			rec.setDate(new Date(mYear, mMonth, mDay));

			RecordDao dao = new RecordDao(context);
			try {
				dao.save(rec);
				Toast.makeText(context, "kaydetme basarili", Toast.LENGTH_SHORT)
						.show();
				saved = 1;
			} catch (DaoServiceException e) {
				e.printStackTrace();
				Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT)
						.show();
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
		menu.add(ResourceUtil.getText(R.string.history_activity_label)).setIcon(
				android.R.drawable.ic_menu_agenda);
		menu.add(ResourceUtil.getText(R.string.aboutus_activity_label)).setIcon(
				android.R.drawable.ic_menu_gallery);
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
		}
		return super.onOptionsItemSelected(item);
	}

	public void toHelp() {
		Intent intent = new Intent();
		intent.setClass(ResultActivity.this, HelpActivity.class);
		startActivity(intent);
		saved = 0;
		finish();
	}

	public void toPast() {
		Intent intent = new Intent();
		intent.setClass(ResultActivity.this, HistoryActivity.class);
		startActivity(intent);
		saved = 0;
		finish();
	}

	public void toAbout() {
		Intent intent = new Intent();
		intent.setClass(ResultActivity.this, AboutUsActivity.class);
		startActivity(intent);
		saved = 0;
		finish();
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.setClass(ResultActivity.this, MainActivity.class);
		startActivity(intent);
		saved = 0;
		finish();
	}
}
