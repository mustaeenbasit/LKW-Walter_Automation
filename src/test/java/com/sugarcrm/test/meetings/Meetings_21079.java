package com.sugarcrm.test.meetings;

import junit.framework.Assert;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_21079 extends SugarTest {
	public void setup() throws Exception {
		sugar().meetings.api.create();
		sugar().login();
	}

	/**
	 * Verify you cannot select any action if no records are selected in meeting
	 * @throws Exception
	 */
	@Test
	public void Meetings_21079_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource dropdownDS = testData.get(testName);
		sugar().meetings.navToListView();

		// Verify the action dropdown list is disabled when no record is selected
		Assert.assertTrue("Expected action dropdown to be disabled.", sugar().meetings.listView.getControl("actionDropdown").isDisabled());
		sugar().meetings.listView.checkRecord(1);
		sugar().meetings.listView.openActionDropdown();

		// Verify dropdown actions
		sugar().meetings.listView.getControl("deleteButton").assertVisible(true);
		sugar().meetings.listView.getControl("deleteButton").assertEquals(dropdownDS.get(0).get("dropdown_menu_actions"), true);
		sugar().meetings.listView.getControl("massUpdateButton").assertVisible(true);
		sugar().meetings.listView.getControl("massUpdateButton").assertEquals(dropdownDS.get(1).get("dropdown_menu_actions"), true);
		sugar().meetings.listView.getControl("exportButton").assertVisible(true);
		sugar().meetings.listView.getControl("exportButton").assertEquals(dropdownDS.get(2).get("dropdown_menu_actions"), true);
		sugar().meetings.listView.getControl("actionDropdown").click(); // to close dropdown

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}