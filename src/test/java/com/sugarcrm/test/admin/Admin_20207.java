package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20207 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify action menu sync with updating module name
	 * @throws Exception
	 */
	@Test
	public void Admin_20207_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Update Contacts module name, set "Plural Label" as "Cons" and set "Singular Label" as "Con"
		sugar().admin.renameModule(sugar().contacts, customData.get("singularLabel"), customData.get("pluralLabel"));
		
		// Check contacts module which name "Cons"
		sugar().contacts.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().contacts);
		sugar().contacts.menu.getControl("createContact").assertElementContains(customData.get("create_con"), true);
		sugar().contacts.menu.getControl("createContactFromVcard").assertElementContains(customData.get("create_vcard"), true);
		sugar().contacts.menu.getControl("viewContacts").assertElementContains(customData.get("view_con"), true);
		sugar().contacts.menu.getControl("viewContactReports").assertElementContains(customData.get("view_reports"), true);
		sugar().contacts.menu.getControl("importContacts").assertElementContains(customData.get("import_con"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}