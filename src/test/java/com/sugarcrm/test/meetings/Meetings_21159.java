package com.sugarcrm.test.meetings;

import junit.framework.Assert;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_21159 extends SugarTest {
	public void setup() throws Exception {
		sugar().meetings.api.create();
		sugar().login();
	}

	/**
	 * New action dropdown list in meetings list view page.
	 * @throws Exception
	 */
	@Test
	public void Meetings_21159_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);
		sugar().meetings.navToListView();

		// Verify the action dropdown list is disabled when no record is selected
		Assert.assertTrue("Expected action dropdown to be disabled.", sugar().meetings.listView.getControl("actionDropdown").isDisabled());

		// Verify actions in the dropdown list
		sugar().meetings.listView.checkRecord(1);
		sugar().meetings.listView.openActionDropdown();
		sugar().meetings.listView.getControl("deleteButton").assertVisible(true);
		sugar().meetings.listView.getControl("deleteButton").assertEquals(ds.get(0).get("dropdown_menu_action"), true);
		sugar().meetings.listView.getControl("massUpdateButton").assertVisible(true);
		sugar().meetings.listView.getControl("massUpdateButton").assertEquals(ds.get(1).get("dropdown_menu_action"), true);
		sugar().calls.listView.getControl("exportButton").assertVisible(true);
		sugar().calls.listView.getControl("exportButton").assertEquals(ds.get(2).get("dropdown_menu_action"), true);

		// Triggering delete action, and verifying no record in list
		sugar().meetings.listView.delete();
		sugar().alerts.getWarning().confirmAlert();
		sugar().meetings.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}