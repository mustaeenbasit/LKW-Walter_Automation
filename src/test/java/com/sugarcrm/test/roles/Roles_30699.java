package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Roles_30699 extends SugarTest {
	FieldSet roleRecord = new FieldSet();
	Record notesRecord;

	public void setup() throws Exception {
		roleRecord = testData.get(testName).get(0);
		sugar().contacts.api.create();
		notesRecord = sugar().notes.api.create();
		sugar().login();
	}

	/**
	 * Verify that Historical Data action should not hangs on 'loading' when email module is disabled for a role.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_30699_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Creating a Role
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// Now on the Access matrix, Set the Email Access cell to Disabled
		// TODO: VOOD-580 -Create a Roles (ACL) Module LIB
		new VoodooControl("div", "css", "td#ACLEditView_Access_Emails_access div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Emails_access div select").set(roleRecord.get("emailAccess"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();

		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);

		// Log out of Sugar as Admin and log in as qauser 
		VoodooUtils.focusDefault();
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Navigate to contacts record view
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// Navigating to contact's historical summary page
		// TODO: VOOD-738
		sugar().contacts.recordView.openPrimaryButtonDropdown();
		VoodooControl history = new VoodooControl("span", "css", "[name='historical_summary_button']");
		history.click();

		VoodooUtils.waitForReady();
		
		// Asserting the default message on the historical summary page.
		// TODO: VOOD-965
		new VoodooControl("div", "css","[data-voodoo-name='history-summary'] .block-footer").waitForVisible();
		new VoodooControl("div", "css","[data-voodoo-name='history-summary'] .block-footer").assertContains(roleRecord.get("defaultMessage"), true);
		VoodooControl cancelBtn = new VoodooControl("a", "css", "span.fld_cancel_button.history-summary-headerpane a");
		cancelBtn.click();

		// Adding a record in notes subpanel of contacts record view
		StandardSubpanel notesSubpanel = sugar().contacts.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.scrollIntoViewIfNeeded(false);
		notesSubpanel.linkExistingRecord(notesRecord);

		// Navigating to historical summary page of contact to assert the data
		sugar().contacts.recordView.openPrimaryButtonDropdown();
		history.click();
		new VoodooControl("a", "css", "[data-voodoo-name='history-summary'] .fld_name a").assertEquals(sugar().notes.getDefaultData().get("subject"), true);
		cancelBtn.click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}