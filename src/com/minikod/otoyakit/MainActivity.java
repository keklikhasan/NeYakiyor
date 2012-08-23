package com.minikod.otoyakit;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.minikod.bean.SessionBean;
import com.minikod.dao.ProfileDao;
import com.minikod.exception.DaoServiceException;
import com.minikod.modals.Profile;
import com.minikod.modals.Record;
import com.minikod.utils.ResourceUtil;

public class MainActivity extends Activity {

	public static Spinner fuel_type;
	public static EditText fuel_unit_price;
	public static EditText fuel_by_price;
	public static EditText fuel;
	public static EditText km;
	public static Button calculate;

	public static boolean stat = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ResourceUtil.res = getResources();

		fuel_type = (Spinner) findViewById(R.id.fuel_type_i);
		fuel_unit_price = (EditText) findViewById(R.id.fuel_unit_price_i);
		fuel_by_price = (EditText) findViewById(R.id.main_activity_fuel_by_price_i);
		fuel = (EditText) findViewById(R.id.main_activity_fuel_i);
		km = (EditText) findViewById(R.id.main_activity_km_i);
		calculate = (Button) findViewById(R.id.main_activity_calculate_b);

		calculate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toResult();
			}
		});
		fuel_by_price.addTextChangedListener(new ChangeFuelByPrice());
		fuel.addTextChangedListener(new ChangeFuel());

		/*
		 * getDefault profile
		 */
		Profile pro = (Profile) SessionBean.getObject("activeProfile");
		if (pro == null) {
			ProfileDao dao = new ProfileDao(this);
			try {
				pro = dao.getActiveProfile();
				SessionBean.setObject("activeProfile", pro);
			} catch (DaoServiceException e) {
				pro=new Profile();
				pro.setDefType(-1);
				SessionBean.setObject("activeProfile", pro);
				e.printStackTrace();
			}
		}
		fuel_type.setSelection(pro.getDefType());
	}

	public class ChangeFuelByPrice implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (stat) {
				stat = false;
				DecimalFormat f = new DecimalFormat("##.000");
				if (fuel_unit_price != null
						&& fuel_unit_price.getText() != null
						&& fuel_unit_price.getText().toString().length() > 0) {
					double fuel_unit_priceD = Double
							.parseDouble(fuel_unit_price.getText().toString());
					if (s != null && s.toString().length() > 0) {
						double fuel_by_priceD = Double
								.parseDouble(s.toString());
						Double text = fuel_by_priceD / fuel_unit_priceD;
						fuel.setText(f.format(text));
					} else if (s.toString().length() == 0) {
						fuel.setText("");
					}
				}
			} else {
				stat = true;
			}
		}

	}

	public class ChangeFuel implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (stat) {
				stat = false;
				DecimalFormat f = new DecimalFormat("##.000");
				if (fuel_unit_price != null
						&& fuel_unit_price.getText() != null
						&& fuel_unit_price.getText().toString().length() > 0) {
					double fuel_unit_priceD = Double
							.parseDouble(fuel_unit_price.getText().toString());
					if (s != null && s.toString().length() > 0) {
						double fuelD = Double.parseDouble(s.toString());
						Double text = fuelD / fuel_unit_priceD;
						fuel_by_price.setText(f.format(text));
					} else if (s.toString().length() == 0) {
						fuel_by_price.setText("");
					}
				}
			} else {
				stat = true;
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(ResourceUtil.getText(R.string.help_activity_label)).setIcon(
				android.R.drawable.ic_menu_help);
		menu.add(ResourceUtil.getText(R.string.history_activity_label))
				.setIcon(android.R.drawable.ic_menu_agenda);
		menu.add(ResourceUtil.getText(R.string.aboutus_activity_label))
				.setIcon(android.R.drawable.ic_menu_gallery);
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
		intent.setClass(MainActivity.this, HelpActivity.class);
		startActivity(intent);
		finish();
	}

	public void toPast() {
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, HistoryActivity.class);
		startActivity(intent);
		finish();
	}

	public void toAbout() {
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, AboutUsActivity.class);
		startActivity(intent);
		finish();
	}

	public void toResult() {
		try {
			Record rec = new Record();
			rec.setType(-1);
			if (fuel_type != null && fuel_type.getSelectedItemPosition() >= 0
					&& fuel_type.getSelectedItem() != null
					&& fuel_type.getSelectedItem().toString().length() > 0) {
				String typeString = fuel_type.getSelectedItem().toString();
				String[] list = getResources().getStringArray(
						R.array.fuel_types);
				for (int i = 0; i < list.length; i++) {
					if (typeString.equalsIgnoreCase(list[i])) {
						rec.setType(i);
					}
				}
				if (fuel_unit_price != null
						&& fuel_unit_price.getText() != null
						&& fuel_unit_price.getText().toString().length() > 0) {

					rec.setUnitPrice(Double.parseDouble(fuel_unit_price
							.getText().toString()));
					if (fuel_by_price != null
							&& fuel_by_price.getText() != null
							&& fuel_by_price.getText().toString().length() > 0) {

						rec.setFuelByPrice(Double.parseDouble(fuel_by_price
								.getText().toString()));

						if (fuel != null && fuel.getText() != null
								&& fuel.getText().toString().length() > 0) {

							rec.setFuel(Double.parseDouble(fuel.getText()
									.toString()));
							if (km != null && km.getText() != null
									&& km.getText().toString().length() > 0) {

								rec.setKm(Double.parseDouble(km.getText()
										.toString()));
								SessionBean.setObject("record", rec);
								Intent intent = new Intent();
								intent.setClass(MainActivity.this,
										ResultActivity.class);
								startActivity(intent);
								finish();
							} else {
								Toast.makeText(this, ResourceUtil
										.getText(R.string.main_activity_km_ne),
										Toast.LENGTH_SHORT);
							}
						} else {
							Toast.makeText(this, ResourceUtil
									.getText(R.string.main_activity_fuel_ne),
									Toast.LENGTH_SHORT);
						}
					} else {
						Toast.makeText(
								this,
								ResourceUtil
										.getText(R.string.main_activity_fuel_by_price_ne),
								Toast.LENGTH_SHORT);
					}

				} else {
					Toast.makeText(
							this,
							ResourceUtil
									.getText(R.string.main_activity_fuel_unit_price_ne),
							Toast.LENGTH_SHORT);
				}
			} else {
				Toast.makeText(this, ResourceUtil
						.getText(R.string.main_activity_fuel_type_ne),
						Toast.LENGTH_SHORT);
			}

		} catch (Exception e) {
			Toast.makeText(this, "girdigniz bilgileri kontrol edin",
					Toast.LENGTH_SHORT);
			e.printStackTrace();
		}
	}
}