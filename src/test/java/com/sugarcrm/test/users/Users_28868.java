package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Users_28868 extends SugarTest{
	FieldSet customFS = new FieldSet();

	public void setup() throws Exception {
		customFS = testData.get(testName).get(0);

		// Create one account
		AccountRecord myRecord = (AccountRecord) sugar().accounts.api.create();
		sugar().opportunities.api.create();
		sugar().login();

		// link account with opportunities 
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.edit();
		sugar().opportunities.recordView.getEditField("likelyCase").set(customFS.get("amount"));
		sugar().opportunities.recordView.getEditField("relAccountName").set(myRecord.getRecordIdentifier());
		sugar().opportunities.recordView.save();
	}

	/**
	 * Verify that only punctuation characters are accepted as decimal and thousands separator
	 * @throws Exception
	 */
	@Test
	public void Users_28868_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to user Profile page
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.waitForReady();
		
		// Edit profile and click on Advance tab
		sugar().users.userPref.getControl("edit").click();
		sugar().users.userPref.getControl("tab4").click();

		VoodooControl groupingSeparetor = sugar().users.userPref.getControl("advanced_grouping_seperator");
		VoodooControl decimalSeparetor = sugar().users.userPref.getControl("advanced_decimal_separator");
		DataSource customDS = testData.get(testName+"_separator");
		int sourceSize = (customDS.size()-1); // escape default separator 
		for(int i = 0; i < sourceSize; i++) {
			// Try to enter alphabetical characters or digits as thousands separator
			groupingSeparetor.set(customDS.get(i).get("advanced_grouping_seperator"));

			// Alphabetical characters or digits are not accepted
			groupingSeparetor.assertContains(customDS.get(i).get("verify_grouping"), true);
			VoodooUtils.waitForReady();

			// Try to enter alphabetical characters or digits as decimal separator
			decimalSeparetor.set(customDS.get(i).get("advanced_decimal_separator"));

			// Alphabetical characters or digits are not accepted
			decimalSeparetor.assertContains(customDS.get(i).get("verify_decimal"), true);
		}

		// Save profile changes
		sugar().users.userPref.getControl("save").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Go to Opportunity listView
		sugar().opportunities.navToListView();

		// Verify that Opportunities module list view loads properly and numeric fields in the records displayed based on the selected separators 
		sugar().opportunities.listView.getDetailField(1, "likelyCase").assertContains(customFS.get("verifyAmount"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}