package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Cases_28061 extends SugarTest {
	AccountRecord relAccount;
	FieldSet dashboardData;

	public void setup() throws Exception {
		dashboardData = testData.get(testName).get(0);
		// Create 51 Cases Record
		for(int i = 0; i <= 50; i++) {
			sugar().cases.api.create();
		}
		relAccount = (AccountRecord)sugar().accounts.api.create();

		// Login as Admin
		sugar().login();

		// Link a Account to the Cases (Mass update to all the cases)
		sugar().cases.navToListView();

		// Need to click on show more two times to select 51 records, as by default 20 records are appears
		sugar().cases.listView.showMore();
		sugar().cases.listView.showMore();

		// Select all 51 Cases
		sugar().cases.listView.getControl("selectAllCheckbox").click();

		// Mass Update the selected cases with Account
		sugar().cases.listView.openActionDropdown();
		sugar().cases.listView.massUpdate();
		FieldSet massUpdateFS = new FieldSet();
		massUpdateFS.put(dashboardData.get("accountName"),relAccount.getRecordIdentifier());

		// Press update button and commit the update
		sugar().cases.massUpdate.performMassUpdate(massUpdateFS);
		sugar().alerts.waitForLoadingExpiration(); // extra time needed to populate/reflect data on listview
	}

	/**
	 * Verify the record's information in case summary dashlet
	 * @throws Exception
	 */
	@Test
	public void Cases_28061_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		//  Navigate to Account record view.
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// TODO VOOD-976
		// open My Dashboard on Account's record view if it doesn't show up by default
		new VoodooControl("span", "css", "span[data-type='dashboardtitle'] span.fld_name.detail i").click();
		VoodooControl dashboardCtrl = new VoodooControl("a", "css", "span[data-type='dashboardtitle'] ul.dropdown-menu a");
		if(dashboardCtrl.getText().equals(dashboardData.get("myDashboard")))
			dashboardCtrl.click();

		// TODO: VOOD-977
		// Verify the case summary dashlet.Records information should be correct when number of cases exceeds 50
		// Verify two tabs: 1)'Summary' tab (Active by default), 2)'New' Tab
		VoodooControl numberOfCasesCtrl = new VoodooControl("span", "css", "li.active a span");
		numberOfCasesCtrl.waitForVisible(); // Extra time needed to populate the data over Case Summary dashlet
		numberOfCasesCtrl.assertEquals(dashboardData.get("totalCases"), true);
		new VoodooControl("span", "css", "#tab li:nth-child(2) a span").assertEquals(dashboardData.get("totalCases"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
