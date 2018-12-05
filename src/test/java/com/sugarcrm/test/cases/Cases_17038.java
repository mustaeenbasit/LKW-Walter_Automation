package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.AccountRecord;

public class Cases_17038 extends SugarTest {
	DataSource casesRecord;
	AccountRecord myAccount;

	public void setup() throws Exception {
		casesRecord = testData.get(testName);
		myAccount = (AccountRecord) sugar().accounts.api.create();

		// Create Case 1 & Case 2
		sugar().cases.api.create(casesRecord);
		sugar().login();

		// Link Account with Cases
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		for(int i = 0; i < casesRecord.size(); i++) {
			sugar().cases.recordView.edit();
			sugar().cases.recordView.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
			sugar().cases.recordView.save();
			sugar().cases.recordView.gotoNextRecord();
		}
	}

	/**
	 * Verify auto duplicate check during the creation of a Case
	 * @throws Exception
	 * */
	@Test
	public void Cases_17038_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Via UI Create New Cases and select the same account/status as the one in the setup
		sugar().cases.navToListView();
		sugar().cases.listView.create();
		sugar().alerts.waitForLoadingExpiration(); // TODO: Need more wait to load createDrawer
		sugar().cases.createDrawer.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		sugar().cases.createDrawer.getEditField("name").set(testName);
		sugar().cases.createDrawer.getEditField("status").set(casesRecord.get(0).get("status"));
		sugar().cases.createDrawer.save();

		//	Verify 1 duplicate found
		FieldSet dupeData = testData.get("env_dupe_assertion").get(0);
		sugar().cases.createDrawer.getControl("duplicateCount").assertVisible(true);
		sugar().cases.createDrawer.getControl("duplicateCount").assertElementContains(dupeData.get("dupe_check"), true);

		// Verify 'Ignore Duplicate and Save' button is on the page
		sugar().cases.createDrawer.getControl("ignoreDuplicateAndSaveButton").assertVisible(true);

		// Verify that the duplicate found panel is shown with the matched record 1 info. Record 2 info should not be shown.
		// TODO: VOOD-513
		VoodooControl duplicateTable = new VoodooControl("table", "css", ".duplicates > tbody");
		duplicateTable.assertElementContains(casesRecord.get(0).get("name"), true);
		duplicateTable.assertElementContains(casesRecord.get(1).get("name"), false);
		sugar().cases.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
