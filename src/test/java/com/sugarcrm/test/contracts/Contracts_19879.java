package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19879 extends SugarTest {
	public void setup() throws Exception {
		sugar.contracts.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * New action dropdown list in contracts list view page
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19879_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	

		// Go to Contracts list view page
		sugar.contracts.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that the action drop down list is shown beside the select options, and is disabled when no record is selected
		// TODO: VOOD-1533
		VoodooControl actionDropDown = new VoodooControl("div", "id", "select_actions_disabled_top");
		actionDropDown.assertVisible(true);
		actionDropDown.assertAttribute("class", "selectActionsDisabled", true);
		VoodooUtils.focusDefault();

		// Select one or more records and click the down arrow beside Delete action
		sugar.contracts.listView.toggleSelectAll();
		sugar.contracts.listView.openActionDropdown();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that drop down shows all of the actions in the drop down list: Delete, Mass Update, Export
		sugar.contracts.listView.getControl("deleteButton").assertVisible(true);
		sugar.contracts.listView.getControl("massUpdateButton").assertVisible(true);
		sugar.contracts.listView.getControl("exportButton").assertVisible(true);

		// Click on any action in the list(Click on Delete button)
		sugar.contracts.listView.getControl("deleteButton").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.waitForReady();

		// Verify that the action is triggered(Assert that in list view no delete and select all button shows that list view is empty)
		sugar.contracts.listView.getControl("selectAllCheckbox").assertExists(false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}