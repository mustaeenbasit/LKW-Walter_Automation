package com.sugarcrm.test.opportunities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_27792 extends SugarTest {
	VoodooControl currencySaveCtrl;
	FieldSet currencyData = new FieldSet();
	FieldSet currencyMngt = new FieldSet();
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		currencyMngt = testData.get(testName + "_currency").get(0);
		ds = testData.get(testName);
		sugar().accounts.api.create();
		sugar().opportunities.api.create( ds.get(0));
		sugar().login();
		
		// Add new Currency
		currencyData = new FieldSet();
		currencyData.put("currencyName", testName);
		currencyData.put("conversionRate", currencyMngt.get("conversion_rate"));
		currencyData.put("currencySymbol", currencyMngt.get("currency_symbol"));
		currencyData.put("ISOcode", currencyMngt.get("currency_code"));
		sugar().admin.setCurrency(currencyData);
		
		// Set User's preferred currency is Euro and User's Show Preferred Currency option is checked.
		sugar().navbar.navToProfile();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("tab4").click();
		sugar().users.userPref.getControl("advanced_preferedCurrency").set(testName + " : " + currencyMngt.get("currency_symbol") );
		sugar().users.userPref.getControl("advanced_showpreferedCurrency").click();
		sugar().users.userPref.getControl("save").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 *  Verify that Opportunities displays both transactional values and preferred values when user pref currency is being used
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_27792_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Calendar calendar = Calendar.getInstance();         
		calendar.add(Calendar.MONTH, 1);
		Date date = calendar.getTime();
		String closeDate = df.format(date);

		// Create an opportunity (euro1) with likely set to Euro 100.
		sugar().opportunities.create(ds.get(1));

		// navigate to list view
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();

		// Create an RLI with likely set to USD 100 and link this to opportunity (usd1) 
		sugar().revLineItems.createDrawer.getEditField("name").set(ds.get(0).get("rli_name"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(closeDate);
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(ds.get(0).get("name"));
		new VoodooControl("span", "css", "div[data-name='discount_price'] .currency.edit.fld_currency_id").click();
		VoodooControl currencySelect = new VoodooControl("li", "css", "#select2-drop > ul > li:nth-child(1)");
		currencySelect.waitForVisible();
		currencySelect.click();
		VoodooUtils.waitForReady();
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(ds.get(0).get("rli_likely"));
		sugar().revLineItems.createDrawer.save();

		// navigate to opportunity listview
		sugar().opportunities.navToListView();
		
		// On Listview verify that the opp's Likely column includes 2 values: $100 €90.00 for USD1 opp.  Opp EURO1 will show only 1 value (€100)
		sugar().opportunities.listView.getDetailField(1, "likelyCase").assertContains(currencyMngt.get("opp_likely"), true);
		sugar().opportunities.listView.getDetailField(1, "likelyCase").assertContains(ds.get(0).get("rli_likely"), true);
		sugar().opportunities.listView.getDetailField(2, "likelyCase").assertContains(ds.get(0).get("rli_likely"), true);
		
		// On recordView verify that The opp's Likely column includes 2 values: $100 €90.00 for USD1 opp.  Opp EURO1 will show only 1 value (€100)
		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.getDetailField("likelyCase").assertContains(currencyMngt.get("opp_likely"), true);
		sugar().opportunities.recordView.getDetailField("likelyCase").assertContains(ds.get(0).get("rli_likely"), true);
		sugar().opportunities.recordView.gotoNextRecord();
		sugar().opportunities.recordView.getDetailField("likelyCase").assertContains(ds.get(0).get("rli_likely"), true);
	
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}


	public void cleanup() throws Exception {}
}