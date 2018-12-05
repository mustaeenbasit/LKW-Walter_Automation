package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_30597 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that 'undefined' should not be seen for the validation message while creating and updating Team, Role, Dropdown Editor
	 * @throws Exception
	 */
	@Test
	public void Admin_30597_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = testData.get(testName).get(0);

		// Navigate to team Management
		sugar().admin.navToAdminPanelLink("teamsManagement");

		// Create Team
		sugar().navbar.selectMenuItem(sugar().teams, "createTeam");	
		VoodooUtils.focusFrame("bwc-frame");
		sugar().teams.editView.getControl("save").click();

		// Verify Proper Validation Message appears when Mandatory fields remains unfilled and clicked Save.
		// TODO: VOOD-948
		VoodooControl validateMsgCtrl = new VoodooControl("div", "css", ".required.validation-message");
		validateMsgCtrl.assertEquals(fs.get("validateTeamMsg"), true);
		sugar().teams.editView.getControl("cancel").click();

		// Update Team 
		sugar().teams.listView.getControl("nameBasic").set(fs.get("team"));
		sugar().teams.listView.getControl("searchButton").click();
		VoodooUtils.focusDefault();
		sugar().teams.listView.clickRecord(1);
		sugar().teams.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().teams.editView.getEditField("name").set("");
		sugar().teams.editView.getControl("save").click();

		// Verify Proper Validation Message appears when Mandatory fields remains unfilled while updating Team
		// TODO: VOOD-948
		validateMsgCtrl.assertEquals(fs.get("validateTeamMsg"), true);
		sugar().teams.editView.getControl("cancel").click();

		// Navigate to Role Management
		VoodooUtils.focusDefault();
		sugar().admin.navToAdminPanelLink("rolesManagement");

		// Create Role
		// TODO: VOOD-580
		new VoodooControl("button", "css","li[data-module='ACLRoles'] button[data-toggle='dropdown']").click();
		new VoodooControl("a", "css","li[data-module='ACLRoles'] ul[role='menu'] a").click();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl saveRole = new VoodooControl("input", "id", "save_button");
		saveRole.click();
		VoodooUtils.waitForReady();

		// Verify Proper Validation Message appears when Mandatory fields remains unfilled and clicked Save.
		// TODO: VOOD-948,VOOD-580
		validateMsgCtrl.assertEquals(fs.get("validateRoleMsg"), true);
		VoodooControl cancelRole = new VoodooControl("input", "css", ".cancel_button");
		cancelRole.click();

		// Update Role
		// TODO: VOOD-580
		new VoodooControl("a", "css", ".oddListRowS1 td:nth-child(3) a").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "#ACLROLE_EDIT_BUTTON").click();
		new VoodooControl("input", "css", "#name").set("");
		saveRole.click();
		VoodooUtils.waitForReady();

		// Verify Proper Validation Message appears when Mandatory fields remains unfilled while Updating Role
		// TODO: VOOD-948
		validateMsgCtrl.assertEquals(fs.get("validateRoleMsg"), true);
		cancelRole.click();
		VoodooUtils.focusDefault();

		// Navigate to Dropdown Editor
		sugar().admin.navToAdminPanelLink("dropdownEditor");
		VoodooUtils.focusFrame("bwc-frame");

		// Create a New Dropdown List	
		// TODO: VOOD-781
		new VoodooControl("input", "css", "[name='adddropdownbtn']").click();
		new VoodooControl("input", "css", "#saveBtn").click();

		// Verify Proper Validation Message appears when Mandatory fields remains unfilled and clicked Save.
		// TODO: VOOD-948
		validateMsgCtrl.assertEquals(fs.get("validateDropDownEditorMsg"),true);
		new VoodooControl("input", "css", "[name='cancel']").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}