package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_29706 extends SugarTest {
	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().calls.api.create();
		sugar().login();
	}

	/**
	 * Verify that 'Close and Create New' functionality of Calls works after adding a Contact as an Invitee.
	 * @throws Exception
	 */
	@Test
	public void Calls_29706_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Call's list view and click first record
		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(1);

		// Edit the call record and enter related to information
		sugar().calls.recordView.edit();
		sugar().calls.recordView.getEditField("relatedToParentType").set(sugar().contacts.moduleNameSingular);
		sugar().calls.recordView.getEditField("relatedToParentName").set(sugar().contacts.getDefaultData().get("lastName"));
		sugar().calls.recordView.save();

		// Clicking on Close and Create New
		sugar().calls.recordView.closeAndCreateNew();

		// Asserting the status of closed record
		FieldSet callData = testData.get(testName).get(0);
		sugar().calls.recordView.getDetailField("status").assertEquals(callData.get("status"), true);

		// Asserting that create drawer of new call record is visible after 
		// Close and Create New option selected for previous call record
		// TODO: VOOD-1887
		sugar().calls.createDrawer.getEditField("name").assertEquals(sugar().calls.getDefaultData().get("name"), true);
		sugar().calls.createDrawer.getEditField("status").assertEquals(sugar().calls.getDefaultData().get("status"), true);
		sugar().calls.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}