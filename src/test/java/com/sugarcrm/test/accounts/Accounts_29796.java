package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_29796 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that User is able to change the Assigned User.
	 * @throws Exception
	 */
	@Test
	public void Accounts_29796_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Account record view and Click "Create" button in "Calls" sub-panel
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel callsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural);

		// Create call from subpanel
		callsSubpanel.addRecord();
		// Fill Subject of Call
		sugar().calls.createDrawer.getEditField("name").set(sugar().calls.getDefaultData().get("name"));
		// Save call creation for selected account
		sugar().calls.createDrawer.save();

		// Edit call from call subpanel.
		callsSubpanel.editRecord(1);

		// Cancel the Assigned User, Need to bring the element in view
		// TODO: VOOD-1422
		callsSubpanel.getEditField(1, "assignedTo").scrollIntoViewIfNeeded(false);
		// TODO: VOOD-806 Clear VoodooSelect value
		new VoodooControl("abbr", "css", ".fld_assigned_user_name.edit abbr").click();

		// Click on "Save"
		callsSubpanel.saveAction(1);

		// Verify "Assigned User" field should be blank/deleted.
		// TODO: VOOD-911
		new VoodooControl("div", "css", ".fld_assigned_user_name.list div").assertEquals("", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}