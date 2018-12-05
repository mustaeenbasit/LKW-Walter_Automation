package com.sugarcrm.test.portal;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Portal_29715 extends SugarTest {
	ContactRecord myContact;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().manufacturers.api.create();

		// Portal user created
		FieldSet portalContactData = testData.get("env_portal_contact_setup").get(0);
		myContact = (ContactRecord) sugar().contacts.api.create(portalContactData);
		sugar().login();

		// Relate contact with account
		// TODO: VOOD-1833
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().alerts.getAlert().cancelAlert();
		sugar().contacts.recordView.save();
	}

	/**
	 * Verify that KB without category should not appear in "KB Categories" Dashlet of own
	 * 
	 * @throws Exception
	 */
	@Test
	public void Portal_29715_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Enable sugar portal 
		sugar().admin.portalSetup.enablePortal();

		// Log into Portal. 
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName",myContact.get("portalName"));
		portalUser.put("password",myContact.get("password"));
		portal.loginScreen.navigateToPortal();
		portal.login(portalUser);

		// Create some records in bug module in portal.
		// Navigate to bugs module 
		portal().navbar.navToModule(portal().bugs.moduleNamePlural);

		// Create two  bug records
		DataSource customData = testData.get(testName);
		VoodooControl nameCtrl = portal().bugs.createDrawer.getEditField("name");
		for (int i = 0 ; i < customData.size() ; i++) {
			portal().bugs.listView.create();
			nameCtrl.set(customData.get(i).get("name"));
			portal().bugs.createDrawer.save();
		}

		// Search any string say "t" in global search bar
		// TODO: VOOD-1031
		new VoodooControl("input", "css", ".search-query").set(customData.get(0).get("name").substring(0, 1));
		VoodooUtils.waitForReady();
		new VoodooControl("ul", "css", ".typeahead.dropdown-menu").assertContains(sugar().manufacturers.getDefaultData().get("name"), false);


		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}