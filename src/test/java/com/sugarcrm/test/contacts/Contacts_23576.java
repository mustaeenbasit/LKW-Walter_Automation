package com.sugarcrm.test.contacts;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_23576 extends SugarTest{
	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 *  View contact_View Change Log_Verify that changes are logged correctly in the pop-up Change Log window when 
	 *  clicking "View Change Log" button.
	 *  
	 *  @throws Exception
	 */
	@Test
	public void Contacts_23576_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet editedContactsValues = testData.get(testName).get(0);
		
		// Go to a contacts Record view
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.showMore();
		VoodooUtils.waitForReady();
		
		// Edit the some fields and click "Save" button: Do Not Call, Team ID, Office Phone, Assigned User 
		sugar().contacts.recordView.edit();
		sugar().contacts.createDrawer.getEditField("checkDoNotCall").set("true");
		sugar().contacts.createDrawer.getEditField("relTeam").set(editedContactsValues.get("relTeam"));
		sugar().contacts.createDrawer.getEditField("phoneWork").set(editedContactsValues.get("phoneWork"));
		sugar().contacts.createDrawer.getEditField("relAssignedTo").set(editedContactsValues.get("relAssignedTo"));
		sugar().contacts.recordView.save();
		
		// Click "View Change Log" button
		// TODO: VOOD-695, VOOD-738
		sugar().contacts.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", ".detail.fld_audit_button a").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-695, VOOD-738
		VoodooControl changelogDiv = new VoodooControl("div", "css", ".layout_Audit.drawer");

		// Verify Change Log window is popped up
		changelogDiv.waitForVisible();
		changelogDiv.assertContains("View Change Log", true);
		
		// Verify that the change log is displayed correctly
		// TODO: VOOD-738
		new VoodooControl("input", "css", ".fld_after input").assertAttribute("checked", "", true);
		for(String newValue : editedContactsValues.values())
			changelogDiv.assertContains(newValue, true);
		
		// Click "Close" icon of the pop-up window
		// TODO: VOOD-738
		new VoodooControl("a", "css", ".fld_close_button a").click();
		VoodooUtils.waitForReady();
		
		// Verify that Change Log window is closed
		changelogDiv.assertExists(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
