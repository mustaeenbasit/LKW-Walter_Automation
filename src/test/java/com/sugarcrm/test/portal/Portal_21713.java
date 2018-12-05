package com.sugarcrm.test.portal;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.SugarUrl;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.PortalTest;

public class Portal_21713 extends PortalTest {
	ContactRecord myPortalContact;

	public void setup() throws Exception {
		FieldSet portalSetupData = testData.get("env_portal_contact_setup").get(0);
		sugar().accounts.api.create();
		myPortalContact = (ContactRecord) sugar().contacts.api.create(portalSetupData);

		sugar().login();

		// Enable Bugs module
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);

		// Enable portal set up. We should ALWAYS have an account linked to our Portal user as per Story VOOD-1833 
		sugar().admin.portalSetup.enablePortal();
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).linkExistingRecord(myPortalContact);
		sugar().logout();
	}

	/**
	 *  Verify the default fields in portal bug dashlet.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Portal_21713_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet bugData = testData.get(testName).get(0);

		// Log into Portal
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName",myPortalContact.get("portalName"));
		portalUser.put("password", myPortalContact.get("password"));
		portal().loginScreen.navigateToPortal();

		// login to portal
		portal().login(portalUser);

		portal().bugs.portalCreate(bugData);

		// Goto Portal Home
		VoodooUtils.go(new SugarUrl().getPortalUrl());
		VoodooUtils.waitForReady();

		// TODO: VOOD-1046
		new VoodooControl("div", "css", "#content  tr td:nth-child(2) div").assertEquals(bugData.get("name"),true);
		new VoodooControl("div", "css", "#content  tr td:nth-child(3) div").assertEquals(bugData.get("status"),true);
		new VoodooControl("div", "css", "#content  tr td:nth-child(4) div").assertEquals(bugData.get("priority"),true);
		new VoodooControl("div", "css", "#content  tr td:nth-child(5) div").assertEquals(bugData.get("type"),true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}