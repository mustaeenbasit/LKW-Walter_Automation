package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_19751 extends SugarTest {
	public void setup() throws Exception {
		FieldSet fs = testData.get(testName).get(0);
		sugar().login();
		sugar().navbar.selectMenuItem(sugar().meetings, "create" + sugar().meetings.moduleNameSingular);
		sugar().alerts.waitForLoadingExpiration();
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.getEditField("repeatType").set(fs.get("repeatType"));
		sugar().meetings.createDrawer.getEditField("repeatOccur").set(fs.get("repeatOccur"));
		sugar().meetings.createDrawer.save();
	}

	/**
	 * Verify that "Delete all recurrence" in Meeting record.
	 * @throws Exception
	 */
	@Test
	public void Meetings_19751_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete all recurrences
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-1257
		new VoodooControl("a", "css", ".fld_delete_recurrence_button.detail a").click();
		sugar().alerts.getWarning().confirmAlert();

		// Verify no meeting record in listview
		sugar().meetings.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}