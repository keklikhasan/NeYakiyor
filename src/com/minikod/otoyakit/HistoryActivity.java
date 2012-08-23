package com.minikod.otoyakit;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.minikod.dao.RecordDao;
import com.minikod.modals.Record;
import com.minikod.utils.ResourceUtil;

public class HistoryActivity extends Activity {
	public static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.past);
		context = this;
		ListView listView = (ListView) findViewById(R.id.mylist);
		RecordDao dao = new RecordDao(this);
		String[] values = new String[0];
		DecimalFormat f1 = new DecimalFormat("##.000");
		SimpleDateFormat f2 = new SimpleDateFormat("yyyy.MM.dd");
		try {
			List<Record> list = dao.getRecordList();
			values = new String[list.size()];
			values[0] = "'Tarih' " + " - " + " 'Yakıt Tipi' " + " - "
					+ " 'Birim Fiyat (TL)' " + " - "
					+ " 'Ne Kadarlık Yakıt (TL)' " + " - "
					+ " 'Yakıt Miktarı (Lt)' " + " - " + " 'Yol (KM)' ";

			for (int i = 1; i <= list.size(); i++) {
				Record rec = list.get(i);
				values[i] = f2.format(rec.getDate()) + " - " + rec.getType()
						+ " - " + f1.format(rec.getUnitPrice()) + " - "
						+ rec.getFuelByPrice() + " - " + rec.getFuel() + " - "
						+ rec.getKm();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);

		// Assign adapter to ListView
		listView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(ResourceUtil.getText(R.string.help_activity_label)).setIcon(
				android.R.drawable.ic_menu_help);
		menu.add(ResourceUtil.getText(R.string.aboutus_activity_label)).setIcon(
				android.R.drawable.ic_menu_gallery);
		menu.add("Temizle").setIcon(android.R.drawable.ic_delete);
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
		} else if (item.getTitle().equals("Temizle")) {
			clear();
		}
		return super.onOptionsItemSelected(item);
	}

	public void clear() {

		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Uyarı")
				.setMessage("Temizlensin mi?")
				.setPositiveButton("Evet",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									RecordDao dao = new RecordDao(context);
									dao.clearTable();
									String[] values = new String[0];
									ListView listView = (ListView) findViewById(R.id.mylist);
									ArrayAdapter<String> adapter = new ArrayAdapter<String>(
											context,
											android.R.layout.simple_list_item_1,
											android.R.id.text1, values);

									// Assign adapter to ListView
									listView.setAdapter(adapter);
									Toast.makeText(context, "Temizlendi..",
											Toast.LENGTH_SHORT).show();
								} catch (Exception e) {
									Toast.makeText(context,
											"Temizlenirken Hata : " + e.toString(),
											Toast.LENGTH_SHORT).show();
								}
							}

						}).setNegativeButton("Hayır", null).show();

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

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.setClass(HistoryActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
