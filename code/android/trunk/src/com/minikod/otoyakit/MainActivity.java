package com.minikod.otoyakit;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.minikod.bean.SessionBean;
import com.minikod.dao.ProfileDao;
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

	public boolean stat = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ResourceUtil.res = getResources();

		fuel_unit_price = (EditText) findViewById(R.id.fuel_unit_price_i);
		fuel_by_price = (EditText) findViewById(R.id.main_activity_fuel_by_price_i);
		fuel = (EditText) findViewById(R.id.main_activity_fuel_i);
		km = (EditText) findViewById(R.id.main_activity_km_i);
		calculate = (Button) findViewById(R.id.main_activity_calculate_b);
		fuel_type = (Spinner) findViewById(R.id.fuel_type_i);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.fuel_types, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		fuel_type.setAdapter(adapter);
		try {
			Profile pro = (Profile) SessionBean.getObject("profile");
			if (pro == null) {
				ProfileDao dao = new ProfileDao(this);
				pro = dao.getActiveProfile();
				SessionBean.setObject("profile", pro);
				dao.close();
			}
			if (pro != null) {
				fuel_type.setSelection(pro.fuel_type);
				DecimalFormat f = new DecimalFormat("#0.000");
				fuel_unit_price.setText(f.format(pro.getUnit_price()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		calculate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toResult();
			}
		});
		fuel_by_price.addTextChangedListener(new ChangeFuelByPrice());
		fuel.addTextChangedListener(new ChangeFuel());
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
			if (stat && s != null && s.length() > 0) {
				stat = false;
				DecimalFormat f = new DecimalFormat("#0.000");
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
			if (stat && s != null && s.length() > 0) {
				stat = false;
				DecimalFormat f = new DecimalFormat("#0.000");
				if (fuel_unit_price != null
						&& fuel_unit_price.getText() != null
						&& fuel_unit_price.getText().toString().length() > 0) {
					double fuel_unit_priceD = Double
							.parseDouble(fuel_unit_price.getText().toString());

					if (s != null && s.toString().length() > 0) {
						double fuelD = Double.parseDouble(s.toString());
						Double text = fuelD * fuel_unit_priceD;
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
		intent.setClass(MainActivity.this, HelpActivity.class);
		startActivity(intent);
	}

	public void toPast() {
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, HistoryActivity.class);
		startActivity(intent);
	}

	public void toAbout() {
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, AboutUsActivity.class);
		startActivity(intent);
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

								Profile pro = (Profile) SessionBean
										.getObject("profile");
								if (pro == null) {
									ProfileDao dao = new ProfileDao(this);
									pro = dao.getActiveProfile();
									dao.close();
								}
								if (pro != null) {
									pro.setFuel_type(fuel_type
											.getSelectedItemPosition());
									pro.setUnit_price(rec.getUnitPrice());
									ProfileDao dao = new ProfileDao(this);
									dao.update(pro);
									SessionBean.setObject("profile", pro);
									dao.close();
								}

								Intent intent = new Intent();
								intent.setClass(MainActivity.this,
										ResultActivity.class);
								startActivity(intent);
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