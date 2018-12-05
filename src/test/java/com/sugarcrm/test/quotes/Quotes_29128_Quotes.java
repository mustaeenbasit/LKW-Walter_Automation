package com.sugarcrm.test.quotes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Quotes_29128_Quotes extends SugarTest {
	UserRecord firstUser, secondUser;
	DataSource userData = new DataSource(); 

	public void setup() throws Exception {
		userData = testData.get(testName);
		sugar.login();

		// Create two users
		firstUser = (UserRecord) sugar.users.create(userData.get(3));
		secondUser = (UserRecord) sugar.users.create(userData.get(4));
	}

	/**
	 * Verify that "Assigned To" search box should sort the users by user's first and last name
	 * 
	 * @throws Exception
	 */
	// For Quotes module
	@Test
	public void Quotes_29128_Quotes_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + " for Quotes modules...");

		// Go to Quote module
		sugar.quotes.navToListView();

		// Select View Quotes from Quote module drop-down
		sugar.navbar.selectMenuItem(sugar.quotes, "viewQuotes");

		// Click on Advanced Search
		VoodooUtils.focusFrame("bwc-frame");
		sugar.quotes.listView.getControl("advancedSearchLink").click();

		// Verify that the name should be sorted by user's first name and last name
		// TODO: VOOD-975
		for(int i = 0; i < userData.size() ; i++){
			if(i > userData.size() - 3)
				new VoodooControl("option", "css", "#assigned_user_id_advanced option:nth-child(" + (i+1) + ")").assertEquals(userData.get(i).get("firstName") + " " + userData.get(i).get("lastName"), true);
			else
				new VoodooControl("option", "css", "#assigned_user_id_advanced option:nth-child(" + (i+1) + ")").assertEquals(userData.get(i).get("lastName"), true);
		}
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " for Quotes module is complete.");
	}

	public void cleanup() throws Exception {}
}