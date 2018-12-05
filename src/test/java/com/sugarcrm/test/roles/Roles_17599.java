package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_17599 extends SugarTest {
	LeadRecord lead;
	DataSource leadFields, roleFields;
	
	public void setup() throws Exception {
		sugar().login();
		
		leadFields = testData.get("Roles_17599");
		roleFields = testData.get("Roles_17599_roledata");
		
		// relAssignedTo is not set: VOOD-912
		lead = (LeadRecord)sugar().leads.api.create(leadFields.get(0));
		
		AdminModule.createRole(roleFields.get(0));
		AdminModule.assignUserToRole(roleFields.get(0));
		
		// TODO VOOD-858
		// assignUserToRole does not wait until user is really added
		VoodooUtils.pause(9000); // Pause is required here
		
		// Set a module's edit access to owner and delete access to owner in a role
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css",	"td#ACLEditView_Access_Leads_edit div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Leads_edit div select").set("Owner");

		new VoodooControl("div", "css",	"td#ACLEditView_Access_Leads_delete div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Leads_delete div select").set("Owner");

		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.pause(3000); // Pause is required here
		VoodooUtils.focusDefault();
		sugar().logout(); // admin

		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * 17599 Available action should be auto updated to match the new assigned to user's ACL control(remove actions)
	 * @throws Exception
	 */
	@Test
	public void Roles_17599_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.navToListView();
		
		// Open record actions dropdown 
		sugar().leads.listView.openRowActionDropdown(1);
						
		// Verify there are no "Edit" and "Delete" actions
		sugar().accounts.listView.getControl("follow01").assertVisible(true);
		sugar().accounts.listView.getControl("edit01").assertVisible(false);
		sugar().accounts.listView.getControl("delete01").assertVisible(false);
		
		// Close record actions dropdown
		sugar().accounts.listView.getControl("dropdown01").click();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}