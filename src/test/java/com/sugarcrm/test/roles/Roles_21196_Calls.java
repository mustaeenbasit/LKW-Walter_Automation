package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21196_Calls extends SugarTest {
	FieldSet rolePermissions = new FieldSet();

	public void setup() throws Exception {
		rolePermissions = testData.get(testName).get(0);
		
		// Create Account and Task records because it is required while creating and editing call record
		sugar().accounts.api.create();
		sugar().tasks.api.create();
		sugar().login();
	}
	/**
	 * Field Permissions not_set_with_access_permission_enable
	 * @throws Exception
	 */
	@Test
	public void Roles_21196_Calls_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-856
		VoodooControl saveRole = new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON");

		// Create Role with  Field Permissions not_set_with_access_permission_enable
		AdminModule.createRole(rolePermissions);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("td", "css", "#ACLEditView_Access_Calls_access div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Calls_access div select").set(rolePermissions.get("moduleAccess"));
		VoodooUtils.waitForReady();

		// Click Save button to save the roleNone
		saveRole.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign roleNone to qaUser
		AdminModule.assignUserToRole(sugar().users.getQAUser());

		// Creating a call with additional information missing in default csv
		// TODO: VOOD-444
		FieldSet callData = testData.get(testName+"_defaultFields").get(0);
		FieldSet fs = new FieldSet();
		String assignedUser = callData.get("assignedTo");
		fs.put("relatedToParentType", callData.get("relatedToParentType"));
		fs.put("relatedToParentName", sugar().accounts.getDefaultData().get("name"));
		fs.put("assignedTo", assignedUser);
		fs.put("repeatType", callData.get("repeatType"));
		fs.put("repeatOccur", callData.get("repeatOccur"));
		sugar().calls.create(fs);

		// Logout from Admin
		sugar().logout();

		// Login as qaUser
		sugar().login(sugar().users.getQAUser());
		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(1);
		DataSource fieldsData = testData.get(testName+"_editFields");
		int fieldCount = fieldsData.size();

		// Asserting the values for Team and AssignedTo field 
		sugar().calls.recordView.getDetailField("teams").assertContains(callData.get("teams"), true);
		sugar().calls.recordView.getDetailField("assignedTo").assertEquals(assignedUser, true);

		// Asserting the visibility of fields on record view
		for (int i = 0; i < fieldCount; i++) {
			sugar().calls.recordView.getDetailField(fieldsData.get(i).get("sugarField")).assertVisible(true);
		}

		// Editing the fields on calls record view
		sugar().calls.recordView.edit();
		sugar().calls.recordView.showMore();
		for (int j = 0; j < fieldCount; j++) {
			if (!fieldsData.get(j).get("value").isEmpty())
				sugar().calls.recordView.getEditField(fieldsData.get(j).get("sugarField")).set(fieldsData.get(j).get("value"));
		}

		// Asserting the field values on calls record view 
		sugar().calls.recordView.save();
		sugar().calls.recordView.showMore();
		for (int k = 0; k < fieldCount; k++) {
			if (!fieldsData.get(k).get("value").isEmpty())
				sugar().calls.recordView.getDetailField(fieldsData.get(k).get("sugarField")).assertContains(fieldsData.get(k).get("value"), true);
		}

		// Searching the record in list view
		String callName = sugar().calls.getDefaultData().get("name");
		sugar().calls.navToListView();
		sugar().calls.listView.setSearchString(callName);
		sugar().calls.listView.getDetailField(1, "name").assertEquals(callName, true);

		// Updating call record using massupdate panel
		FieldSet massUp = new FieldSet();
		massUp.put(callData.get("massAssigned"),assignedUser);
		sugar().calls.listView.toggleSelectAll();
		sugar().calls.massUpdate.performMassUpdate(massUp);

		// Verifying call gets updated by massupdate
		sugar().calls.listView.getDetailField(1,"assignedTo").assertContains(assignedUser, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}