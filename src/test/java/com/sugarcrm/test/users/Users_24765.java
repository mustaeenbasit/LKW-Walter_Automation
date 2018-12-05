package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_24765 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify action dropdown list in user list view page
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_24765_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// user management
		sugar().admin.navToAdminPanelLink("userManagement");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1041
		// The action dropdown list is shown beside the select options, and is disabled when no record is selected
		VoodooControl actionDropDownCtrl = new VoodooControl("ul", "css", "div#select_actions_disabled_top span#selectedRecordsTop[style*='display: none']");
		actionDropDownCtrl.assertExists(true);

		// Select one or more records
		VoodooUtils.focusDefault();
		sugar().users.listView.checkRecord(1);

		// The action dropdown list is shown beside the select options, and it is enable when record is selected
		VoodooUtils.focusFrame("bwc-frame");
		actionDropDownCtrl.assertExists(false);

		// Click the down arrow beside action
		VoodooUtils.focusDefault();
		sugar().users.listView.openActionDropdown();

		// After step4, show all of the actions in the dropdown list: Mass Update, Export
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.listView.getControl("massUpdateButton").assertExists(true);
		sugar().users.listView.getControl("exportButton").assertExists(true);

		// Click on any action in the list
		VoodooUtils.focusDefault();
		sugar().users.listView.massUpdate();

		// TODO: VOOD-768
		// The action is triggered and open MassUpdate form 
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("h3", "css", "#massupdate_form h3").assertContains("Mass Update", true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}