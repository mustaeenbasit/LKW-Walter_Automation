package com.sugarcrm.test.accounts;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_17931 extends SugarTest {
	DataSource accRecPast = new DataSource();
	DataSource accRecCurrent = new DataSource();

	public void setup() throws Exception {
		// Create 3 Accounts record with created date < (currentDate - 7)
		accRecPast= testData.get(testName);
		sugar().accounts.api.create(accRecPast);

		// Create 2 Accounts record with created date > (currentDate - 7)
		accRecCurrent = testData.get(testName + "_accNames");
		DateTime date = DateTime.now();
		FieldSet accTempFS = new FieldSet();
		for (int i = 0; i < accRecCurrent.size(); i++) {
			accTempFS.put("date_entered_date", date.minusDays(i+5).toString("MM/dd/yyyy"));
			accTempFS.put("date_entered_time", date.toString("hh:mma"));
			accTempFS.put("name", accRecCurrent.get(i).get("name"));
			sugar().accounts.api.create(accTempFS);
			accTempFS.clear();
		}

		sugar().login();
	}

	/**
	 * Verify Recently created / Viewed filter is returning items in the past 7 days
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17931_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Accounts Record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.sortBy("headerName", true);

		// Update any existing account record
		sugar().accounts.listView.clickRecord(2);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getEditField("emailAddress").set(accRecCurrent.get(0).get("emailAddress"));
		sugar().accounts.recordView.save();

		// Viewing one account record
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Verify the records for Recently Created filter
		sugar().accounts.navToListView();
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterRecentlyCreated();
		VoodooUtils.waitForReady();
		Assert.assertTrue("Incorrect no. of records in Recently Created filter" , sugar().accounts.listView.countRows() == 2);
		sugar().accounts.listView.verifyField(1, "name", accRecCurrent.get(0).get("name"));
		sugar().accounts.listView.verifyField(2, "name", accRecCurrent.get(1).get("name"));

		// Verify the records for Recently Viewed filter
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterRecentlyViewed();
		VoodooUtils.waitForReady();
		Assert.assertTrue("Incorrect no. of records in Recently Viewed filter" , sugar().accounts.listView.countRows() == 2);
		sugar().accounts.listView.verifyField(1, "name", accRecPast.get(0).get("name"));
		sugar().accounts.listView.verifyField(2, "name", accRecPast.get(1).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}