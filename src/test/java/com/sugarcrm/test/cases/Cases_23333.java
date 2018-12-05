package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.test.SugarTest;

public class Cases_23333 extends SugarTest {
	CaseRecord myCase;
	FieldSet defaultCase;

	public void setup() throws Exception {
		defaultCase = sugar().cases.getDefaultData();
		sugar().login();
		sugar().accounts.api.create();
		myCase = (CaseRecord) sugar().cases.api.create();
	}

	/**
	 * Test Case 23333: Create Case_Verify that case is not duplicated when using cancel function.
	 */
	@Test
	public void Cases_23333_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to the created case
		myCase.navToRecord();

		// Click Copy button
		sugar().cases.recordView.copy();
		// And cancel duplication
		sugar().cases.createDrawer.waitForVisible();
		sugar().cases.createDrawer.cancel();

		// Verify that case is not duplicated
		// Delete current case
		sugar().cases.recordView.waitForVisible();
		sugar().cases.recordView.delete();
		sugar().alerts.getAlert().confirmAlert();

		// Search for the duplicate (no result should be found)
		sugar().cases.navToListView();
		sugar().cases.listView.setSearchString(defaultCase.get("name"));
		sugar().cases.listView.getControl("emptyListViewMsg").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
