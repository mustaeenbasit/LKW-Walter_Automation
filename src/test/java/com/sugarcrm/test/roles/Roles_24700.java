package com.sugarcrm.test.roles;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_24700 extends SugarTest {
	FieldSet notesData = new FieldSet();

	public void setup() throws Exception {
		// Creating notes record
		sugar().notes.api.create();

		// Login
		sugar().login();

		// Create a Role
		FieldSet roleData = testData.get("env_role_setup").get(0);
		AdminModule.createRole(roleData);
		VoodooUtils.focusFrame("bwc-frame");

		// Set 'View' permission to 'None' for Notes module
		// TODO: VOOD-580
		new VoodooControl("a", "css", "#ACLEditView_Access_Notes_view div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Notes_view div select").set(roleData.get("roleNone"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign QAuser to the Role and logout
		AdminModule.assignUserToRole(roleData);

		// Adding attachment to notes record 
		sugar().notes.navToListView();
		sugar().notes.listView.clickRecord(1);
		sugar().notes.recordView.edit();
		notesData = testData.get(testName).get(0);
		VoodooFileField browseToImport = new VoodooFileField("input", "css", "[name='filename']");
		browseToImport.set(notesData.get("attachment"));
		VoodooUtils.waitForReady();
		sugar().notes.recordView.save();
		sugar().logout();
	}

	/**
	 * Role management_Verify that attachments of note records cannot be viewed when the user 
	 * has no privilege of viewing notes.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_24700_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as qauser
		sugar().login(sugar().users.getQAUser());

		// Navigating to Notes module
		sugar().notes.navToListView();

		// Clicking on attachment link
		sugar().notes.listView.getDetailField(1, "attachment").click();

		// Asserting the error message
		VoodooUtils.focusWindow(1);
		Assert.assertTrue(VoodooUtils.contains(notesData.get("errorText"), false) == true);
		VoodooUtils.closeWindow();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}