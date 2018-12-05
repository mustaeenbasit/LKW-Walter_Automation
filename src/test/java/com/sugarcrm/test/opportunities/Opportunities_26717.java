package com.sugarcrm.test.opportunities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_26717 extends SugarTest {
	VoodooControl currencySaveCtrl;
	FieldSet customFS;

	public void setup() throws Exception {
		FieldSet currencyData = testData.get(testName + "_currency").get(0);
		customFS = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();

		// Add new Currency
		sugar().admin.setCurrency(currencyData);

		// Set User's preferred currency is Euro and User's Show Preferred Currency option is checked.
		sugar().navbar.navToProfile();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("tab4").click();
		sugar().users.userPref.getControl("advanced_preferedCurrency").set(testName + " : " + currencyData.get("currencySymbol") );
		sugar().users.userPref.getControl("advanced_showpreferedCurrency").click();
		sugar().users.userPref.getControl("save").click();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that the likely value in opportunity on list/detail/preview is converted to user preferred currency 
	 * if "Show Preferred Currency" is checked in user prefs
	 * @throws Exception
	 */
	@Test
	public void Opportunities_26717_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Calendar calendar = Calendar.getInstance();         
		calendar.add(Calendar.MONTH, 1);
		Date date = calendar.getTime();
		String closeDate = df.format(date);

		// Create an Opportunity with RLI and likely set to EUR 100 
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		sugar().opportunities.createDrawer.getEditField("name").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));

		// Add RLI records linked to this opportunity
		VoodooControl rliName = sugar().opportunities.createDrawer.getEditField("rli_name");
		VoodooControl rliExpectedCloseDate = sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date");
		VoodooControl rliLikely = sugar().opportunities.createDrawer.getEditField("rli_likely");
		rliName.set(customFS.get("rli_name"));
		rliExpectedCloseDate.set(closeDate);
		rliLikely.set(customFS.get("rli_likely"));

		sugar().opportunities.createDrawer.save();
		sugar().opportunities.listView.clickRecord(1);

		// Verify that all currency related numbers are displayed in user preferred currency on Opportunity record view. 
		sugar().opportunities.recordView.getDetailField("likelyCase").assertContains(customFS.get("opp_likely"), true);
		sugar().opportunities.recordView.getDetailField("likelyCase").assertContains(customFS.get("rli_likely"), true);

		// Navigate to opportunity listview
		sugar().opportunities.navToListView();

		// Verify that all currency related numbers are displayed in the user preferred currency on Opportunity list view as well.
		sugar().opportunities.listView.getDetailField(1, "likelyCase").assertContains(customFS.get("opp_likely"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}