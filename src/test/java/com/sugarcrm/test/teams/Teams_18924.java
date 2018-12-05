package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_18924 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();

		// Create two teams
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("teamsManagement").click();

		// TODO: VOOD-518
		for(int i=0; i<2; i++){
			new VoodooControl("button", "css", "li[data-module='Teams'] button[data-toggle='dropdown']").click();
			new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_NEW_TEAM']").click();
			VoodooUtils.focusFrame("bwc-frame");
			new VoodooControl("input", "name", "name").set(testName + i);
			new VoodooControl("input", "css", "#btn_save").click();
			VoodooUtils.focusDefault();
		}
	}

	/**
	 * Mass delete teams in its list view page.
	 * @throws Exception
	 */
	@Test
	public void Teams_18924_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet assertData = testData.get(testName).get(0);

		// Go to Admin -> Teams Management
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("teamsManagement").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify dropdown list disabled when no record is selected.
		new VoodooControl("span", "css", ".ab").assertVisible(false);

		// TODO: VOOD-518
		// Search both the teams created in setup
		VoodooControl teamNameCtrl = new VoodooControl("input", "id", "name_basic");
		VoodooControl searchBtnCtrl = new VoodooControl("input", "id", "search_form_submit");
		teamNameCtrl.set(testName);
		searchBtnCtrl.click();

		// Select both the teams created in setup
		new VoodooControl("input", "css", "form#MassUpdate input.checkbox.massall").click();

		// Click Delete
		new VoodooControl("a", "id", "delete_listview_top").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that the team is deleted
		new VoodooControl("div", "css", "div.list.view.listViewEmpty").assertElementContains(assertData.get("assertMessage") + " \"" +testName+ "\"", true);

		// Clear Search field
		teamNameCtrl.set("");
		searchBtnCtrl.click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}