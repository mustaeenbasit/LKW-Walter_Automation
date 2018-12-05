package com.sugarcrm.test.calls;

import junit.framework.Assert;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_21158 extends SugarTest {
	public void setup() throws Exception {
		sugar.calls.api.create();
		sugar.login();
	}

	/**
	 * New action dropdown list in calls list view page.
	 * @throws Exception
	 */
	@Test
	public void Calls_21158_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource dropdownDS = testData.get(testName);

		sugar.calls.navToListView();

		// Verify the action dropdown list is disabled when no record is selected
		Assert.assertTrue("Expected action dropdown to be disabled.", sugar.calls.listView.getControl("actionDropdown").isDisabled());

		sugar.calls.listView.checkRecord(1);
		sugar.calls.listView.openActionDropdown();

		// Verify dropdown actions
		sugar.calls.listView.getControl("deleteButton").assertVisible(true);
		sugar.calls.listView.getControl("deleteButton").assertEquals(dropdownDS.get(0).get("dropdown_menu_actions"), true);
		sugar.calls.listView.getControl("massUpdateButton").assertVisible(true);
		sugar.calls.listView.getControl("massUpdateButton").assertEquals(dropdownDS.get(1).get("dropdown_menu_actions"), true);
		sugar.calls.listView.getControl("exportButton").assertVisible(true);
		sugar.calls.listView.getControl("exportButton").assertEquals(dropdownDS.get(2).get("dropdown_menu_actions"), true);

		// Trigger mass update dropdown action
		sugar.calls.listView.massUpdate();
		sugar.calls.massUpdate.getControl("massUpdateField02").set("Assigned to");
		sugar.calls.massUpdate.getControl("massUpdateValue02").set(sugar.users.getQAUser().get("userName"));
		sugar.calls.massUpdate.update();
		sugar.alerts.waitForLoadingExpiration(25000); // extra time needed to reflect data on listview

		// Verify assigned to field, after mass update
		// sugar.calls.listView.verifyField(1, "assignedTo", sugar.users.getQAUser().get("userName"));
		// TODO: VOOD-1217
		new VoodooControl("a", "css", ".fld_assigned_user_name.list").assertEquals(sugar.users.getQAUser().get("userName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}