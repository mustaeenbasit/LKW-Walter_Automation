package com.sugarcrm.test.portal;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.PortalTest;

public class Portal_21716 extends PortalTest {
	ContactRecord myCon;
	FieldSet portalContact = new FieldSet();
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		ds = testData.get(testName);
		portalContact = testData.get("env_portal_contact_setup").get(0);
		sugar().accounts.api.create();

		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
		sugar().admin.portalSetup.enablePortal();

		// Create contact for portal access
		myCon = (ContactRecord) sugar().contacts.api.create(portalContact);

		// Link Contact with Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).linkExistingRecord(myCon);

		sugar().logout();
	}

	/**
	 * Verify Portal user creates a new bug and verify Sugar user edits in Sugar side
	 *
	 * @throws Exception
	 */
	@Test
	public void Portal_21716_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myCon.get("portalName"));
		portalUser.put("password", myCon.get("password"));
		portal().loginScreen.navigateToPortal();

		// login to portal
		portal().login(portalUser);

		// Go to Bugs module. 
		portal().navbar.navToModule(portal().bugs.moduleNamePlural);

		// Create new bug
		portal().navbar.clickModuleDropdown(portal().bugs);

		//  Click on "+" sign as "Report Bug".
		portal().bugs.menu.getControl("createBug").click();
		portal().bugs.portalCreateDrawer.setFields(ds.get(0));
		portal().bugs.portalCreateDrawer.save();
		portal().alerts.waitForLoadingExpiration();
		portal().logout();

		// Login to Sugar side as a valid admin user 
		sugar().loginScreen.navigateToSugar();
		sugar().login();

		//  Go to Bugs module
		sugar().navbar.navToModule(sugar().bugs.moduleNamePlural);

		// Assert the bug created in portal
		sugar().bugs.listView.verifyField(1, "name", (ds.get(0).get("name")));

		// navigate to bug record
		sugar().bugs.listView.clickRecord(1);

		// edit the record 
		sugar().bugs.recordView.edit();
		sugar().bugs.recordView.setFields(ds.get(1));
		sugar().bugs.recordView.save();
		sugar().alerts.waitForLoadingExpiration(30000);
		sugar().logout();

		// login to portal
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);

		// Go to Bugs module 
		portal().navbar.navToModule(portal().bugs.moduleNamePlural);

		// TODO: VOOD-1046 - Support ListView in Portal
		//portal().bugs.listView.clickRecord(1); // not working
		new VoodooControl("div", "css", "#content  tr:nth-child(1)  td:nth-child(2)").click();
		portal().alerts.waitForLoadingExpiration();

		// Assert the update record in portal 
		portal().bugs.recordView.getDetailField("name").assertContains(ds.get(1).get("name"), true);
		portal().bugs.recordView.getDetailField("priority").assertContains(ds.get(1).get("priority"), true);
		portal().bugs.recordView.getDetailField("type").assertContains(ds.get(1).get("type"), true);
		portal().bugs.recordView.getDetailField("status").assertContains(ds.get(1).get("status"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}