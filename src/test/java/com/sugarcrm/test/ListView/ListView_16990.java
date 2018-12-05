package com.sugarcrm.test.ListView;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class ListView_16990 extends SugarTest {
	DataSource accounts;
	FieldSet roleRecord;
	public void setup() throws Exception {
		accounts = testData.get(testName+"_accounts");
		sugar.accounts.api.create(accounts);
		roleRecord = testData.get(testName).get(0);
		sugar.login();
		
		// Create and Save the Role 
		AdminModule.createRole(roleRecord);
		
		// Setting the Account Edit field permissions >> "Assigned to" to "None"
		// TODO: VOOD-856
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl accountLink = new VoodooControl("a", "css","table tr:nth-child(2) td a");
		VoodooControl assignedToCell = new VoodooControl("div", "id","assigned_user_namelink");
		VoodooControl assignedToDropDown = new VoodooControl("select", "id","flc_guidassigned_user_name");
		VoodooControl saveButton = new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON");
		
		accountLink.click();
		assignedToCell.click();
		assignedToDropDown.set(roleRecord.get("option"));
		saveButton.click();
		
		// Assign role to qauser
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);
		sugar.accounts.recordView.edit();
		
		// Changing the "Assigned To/ User" of account to 'qauser'
		sugar.accounts.recordView.getEditField("relAssignedTo").set(roleRecord.get("userName"));
		sugar.accounts.recordView.save();
	
		sugar.logout();
	}

	/**
	 * Edit inline record - Field permission set to "None"
	 * 
	 * @throws Exception
	 */
	@Test
	public void ListView_16990_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.login(sugar.users.getQAUser());
		
		// TODO: VOOD-1445
		VoodooControl assignedToField = new VoodooControl("span", "css",".noaccess.fld_assigned_user_name");
		
		sugar.accounts.navToListView();
		// Assert the 'User'/'Assigned To' field is displayed as 'No access' in List View for both Owned and Non-Owned Records
		for(int i=1 ; i<= 2 ; i++)
			new VoodooControl("td", "css","tr:nth-child("+i+") td:nth-child(6)").assertEquals(roleRecord.get("userVisibility"), true);
		
		sugar.accounts.listView.clickRecord(1);
		// Assert the 'User'/'Assigned To' field is displayed as 'No access' in Record View in a Owned Record
		assignedToField.assertEquals(roleRecord.get("userVisibility"), true);
		
		sugar.accounts.recordView.gotoNextRecord();
		// Assert the 'User'/'Assigned To' field is displayed as 'No access' in Record View in a Non-Owned Record
		assignedToField.assertEquals(roleRecord.get("userVisibility"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}