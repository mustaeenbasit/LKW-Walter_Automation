package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_29007 extends SugarTest {
	public void setup() throws Exception {
		sugar.calls.api.create();
		sugar.login();
	}
	/**
	 * Verify the message for "close and create new" in call module.
	 * @throws Exception
	 */
	@Test
	public void Calls_29007_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);	

		// Verify call record is 'Planned/Scheduled'
		sugar.calls.recordView.getDetailField("status").assertEquals(sugar.calls.getDefaultData().get("status"), true);

		// Edit call record with close and create New dropdown action
		FieldSet callData = testData.get(testName).get(0);
		sugar.calls.recordView.openPrimaryButtonDropdown();
		sugar.calls.recordView.getControl("closeAndCreateNew").click();	

		//Verify the success message 
		sugar.alerts.getSuccess().assertContains(callData.get("successString"), true);

		// Verify the data on create drawer of new call record
		sugar.calls.createDrawer.getEditField("name").assertEquals(sugar.calls.getDefaultData().get("name"), true);
		sugar.calls.createDrawer.getEditField("status").assertEquals(sugar.calls.getDefaultData().get("status"), true);
		sugar.calls.createDrawer.getEditField("date_end_date").assertEquals(sugar.calls.getDefaultData().get("date_end_date"), true);
		sugar.calls.createDrawer.getEditField("date_end_time").assertEquals(sugar.calls.getDefaultData().get("date_end_time"), true);
		sugar.calls.createDrawer.getEditField("date_start_date").assertEquals(sugar.calls.getDefaultData().get("date_start_date"), true);
		sugar.calls.createDrawer.getEditField("date_start_time").assertEquals(sugar.calls.getDefaultData().get("date_start_time"), true);

		// Verify first call is in 'Held' status
		sugar.calls.createDrawer.cancel();
		sugar.calls.recordView.getDetailField("status").assertEquals(callData.get("status"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}