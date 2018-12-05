package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Admin_20217 extends SugarTest {
	VoodooControl renameTabCtrl, editTaskCtrl, singularLabelCtrl, pluralLabelCtrl, renameBtnCtrl;
	ContactRecord myContact;

	public void setup() throws Exception {
		FieldSet portalSetupData = testData.get("env_portal_contact_setup").get(0);
		AccountRecord acc = (AccountRecord) sugar().accounts.api.create();
		myContact = (ContactRecord)sugar().contacts.api.create(portalSetupData);
		sugar().login();

		// Enable portal in admin
		sugar().admin.portalSetup.enablePortal();

		// Case module enable - link Contact with Account
		acc.navToRecord();
		StandardSubpanel contactSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.scrollIntoViewIfNeeded(false);
		contactSubpanel.linkExistingRecord(myContact);		
	}

	/**
	 * Verify module name in self-portal sync with updating module name
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20217_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		sugar().admin.renameModule(sugar().cases, customData.get("singularName"), customData.get("pluralName"));
		sugar().logout();

		// Navigate to portal URL
		portal().loginScreen.navigateToPortal();

		// login as portal user
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));
		portal().login(portalUser);

		sugar().navbar.clickModuleDropdown(sugar().cases);

		// Verify that "Cases" module name changed to "Cas" and "Case" to "Ca"
		portal().cases.menu.getControl("createCase").assertContains(customData.get("singularName"), true);
		portal().cases.menu.getControl("viewCases").assertContains(customData.get("pluralName"), true);
		new VoodooControl("a", "css", "#content div.thumbnail.layout_Cases  h1 a").assertContains(customData.get("pluralName"), true);

		sugar().navbar.clickModuleDropdown(sugar().cases); // to close dropdown
		
		// TODO: VOOD-1056 Revise PortalTest.java and SugarTest.java
		// Logout and Login as Admin since this test extends SugarTest
		portal().logout();
		sugar().loginScreen.navigateToSugar();
		sugar().login();
	
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}