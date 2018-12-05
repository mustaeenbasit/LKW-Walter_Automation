package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Roles_17600 extends SugarTest {
	FieldSet roleRecord;

	public void setup() throws Exception {
		roleRecord = testData.get("env_role_setup").get(0);
		
		// 2 Leads via API with default data and custom Fieldset
		sugar().leads.api.create();
		FieldSet fs = new FieldSet();
		fs.put("firstName", roleRecord.get("userName"));
		fs.put("lastName", testName);
		fs.put("fullName", fs.get("firstName")+" "+fs.get("lastName"));
		sugar().leads.api.create(fs);

		sugar().login();

		// Create role => Leads => Delete=Owner
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580, VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_Leads_delete").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Leads_delete div select").set("Owner");
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign QAuser to the Role
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();
		sugar().logout();
	}

	/**
	 * Available action should be auto updated to match the new assigned to user's ACL control(enable actions)
	 *  
	 * @throws Exception
	 */
	@Test
	public void Roles_17600_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as qauser
		sugar().login(sugar().users.getQAUser());

		// Go to Leads module
		sugar().leads.navToListView();
		VoodooControl firstRowDeleteButton = sugar().leads.listView.getControl("delete01");
		VoodooControl firstRowCloseDropdown = sugar().leads.listView.getControl("dropdown01");

		// 'Delete' button is not displayed on action dropdown for non-owned records
		sugar().leads.listView.openRowActionDropdown(1);
		firstRowDeleteButton.assertVisible(false);
		firstRowCloseDropdown.click(); // to close dropdown

		sugar().leads.listView.openRowActionDropdown(2);
		sugar().leads.listView.getControl("delete02").assertVisible(false);
		sugar().leads.listView.getControl("dropdown02").click(); // to close dropdown

		// Edit first record, set assignedTo=Owner(i.e qauser)
		sugar().leads.listView.editRecord(1);
		VoodooSelect assignedTo = (VoodooSelect)sugar().leads.listView.getEditField(1, "relAssignedTo");
		assignedTo.scrollIntoViewIfNeeded(false);
		assignedTo.set(roleRecord.get("userName"));
		sugar().leads.listView.saveRecord(1);

		// TODO: TR-8565 - seems to be same kind of issue, refresh is necessary to show delete button on listview
		VoodooUtils.refresh();

		// Verify 'Delete' action is displayed on action dropdown
		sugar().leads.listView.openRowActionDropdown(1);
		firstRowDeleteButton.assertVisible(true);
		firstRowCloseDropdown.click(); // to close dropdown

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}