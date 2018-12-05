package com.sugarcrm.test.portal;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.PortalTest;

public class Portal_18093 extends PortalTest {
	FieldSet portalSetupData = new FieldSet();
	ContactRecord myContact;

	public void setup() throws Exception {
		portalSetupData = testData.get("env_portal_contact_setup").get(0);
		sugar().accounts.api.create();
		sugar().login();

		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);

		// Enable portal in admin
		sugar().admin.portalSetup.enablePortal();

		// Create portal set up
		myContact = (ContactRecord) sugar().contacts.api.create(portalSetupData);

		// Link Contact with Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).linkExistingRecord(myContact);

		sugar().logout();
	}

	/**
	 * Verify portal user able to access three save features under Bugs creation page
	 *
	 * @throws Exception
	 */
	@Test
	public void Portal_18093_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));

		// Navigate to portal URL
		portal().loginScreen.navigateToPortal();

		// login as portal user
		portal().login(portalUser);

		// TODO: VOOD-1096
		VoodooControl bugRecordCtrl = new VoodooControl("a", "css", "tr:nth-child(1) td:nth-child(2) span div a");

		// Report Bugs
		// TODO: VOOD-1031
		sugar().navbar.clickModuleDropdown(sugar().bugs);
		portal().bugs.menu.getControl("createBug").click();
		portal().bugs.createDrawer.getEditField("name").set(testName);
		portal().bugs.createDrawer.save();

		// Verify the new record appear in list view after save.
		bugRecordCtrl.assertContains(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 