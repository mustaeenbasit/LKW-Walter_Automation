package com.sugarcrm.test.portal;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Portal_29983 extends SugarTest {
	ContactRecord myContact;
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		sugar().accounts.api.create();

		// Portal user created
		FieldSet portalContactData = testData.get("env_portal_contact_setup").get(0);
		myContact = (ContactRecord) sugar().contacts.api.create(portalContactData);

		// Making a new KB article as externally visible with status as Published
		customData = testData.get(testName).get(0);
		sugar().knowledgeBase.api.create(customData);

		// Logging in as admin
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Enable portal in admin
		sugar().admin.portalSetup.enablePortal();

		// Relate contact with account
		// TODO: VOOD-1108
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().alerts.getAlert().cancelAlert();
		sugar().contacts.recordView.save();
	}

	/**
	 * Verify that Portal user is able to view published KB articles
	 *
	 * @throws Exception
	 */
	@Test
	public void Portal_29983_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to KB module
		sugar().knowledgeBase.navToListView();

		// Asserting the values for Name, isExternal and Status for created KB record
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.showMore();
		String kbName = sugar().knowledgeBase.getDefaultData().get("name");
		sugar().knowledgeBase.recordView.getDetailField("name").assertEquals(kbName, true);
		Assert.assertTrue("isExternal checkbox is not checked", sugar().knowledgeBase.recordView.getDetailField("isExternal").isChecked());
		sugar().knowledgeBase.recordView.getDetailField("status").assertEquals(customData.get("status"), true);

		// Log into Portal. 
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));
		portal.loginScreen.navigateToPortal();
		portal.login(portalUser);

		// Navigate to Knowledgebase in Portal
		portal.navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);

		// Verify KB article verified above is displayed on KB list view in Portal
		// TODO: VOOD-1096 : Portal Module Listview support
		new VoodooControl("a", "css", ".list.fld_name a").assertEquals(kbName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}