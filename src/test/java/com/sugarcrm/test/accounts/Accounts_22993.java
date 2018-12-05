package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_22993 extends SugarTest{
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().accounts.api.create();

		// Create an Account record with custom name.
		FieldSet accountName = new FieldSet();
		accountName.put("name", testName);
		sugar().accounts.api.create(accountName);
		sugar().login();

		// Edit Name, Parent Account ID, Team ID, Phone Office, Assigned to, and click on Save button
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getEditField("name").set(customData.get("name"));
		sugar().accounts.recordView.getEditField("workPhone").set(customData.get("workPhone"));
		sugar().accounts.recordView.getEditField("memberOf").set(customData.get("memberOf"));
		sugar().accounts.recordView.getEditField("relTeam").set(customData.get("relTeam"));
		sugar().accounts.recordView.getEditField("relAssignedTo").set(customData.get("relAssignedTo"));
		sugar().accounts.recordView.save();
	}
	
	/**
	 * View Change Log_Verify that corresponding change logs are displayed in pop-up window after 
	 * auditing fields, such as: "Parent Account ID, Team ID, Name, Phone Office, Assigned to".
	 * @throws Exception
	 */
	@Test
	public void Accounts_22993_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click on primary action drop down on account record view
		sugar().accounts.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-695, VOOD-738, VOOD-1990
		// Click "View Change Log" button 
		new VoodooControl("a", "css", ".fld_audit_button [name='audit_button']").click();
		VoodooControl changelogDiv = new VoodooControl("div", "css", ".layout_Audit.drawer");

		// Verify that the change log is displayed correctly
		for(String newValue : customData.values()) {
			changelogDiv.assertContains(newValue, true);
		}

		// TODO: VOOD-1990
		// Click "Close" icon of the pop-up window
		new VoodooControl("a", "css", ".fld_close_button a").click();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}