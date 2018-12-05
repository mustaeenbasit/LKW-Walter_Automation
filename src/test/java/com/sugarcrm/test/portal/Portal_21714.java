package com.sugarcrm.test.portal;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.PortalTest;

public class Portal_21714 extends PortalTest {
	ContactRecord myCon;
	FieldSet portalContact = new FieldSet();
	FieldSet kbData = new FieldSet();

	public void setup() throws Exception {
		kbData = testData.get(testName).get(0);
		portalContact = testData.get("env_portal_contact_setup").get(0);
		sugar().accounts.api.create();

		sugar().login();

		// Enable knowledgeBase Module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Enable Portal
		sugar().admin.portalSetup.enablePortal();

		// Create contact for portal access
		myCon = (ContactRecord) sugar().contacts.api.create(portalContact);

		// Link Contact with Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).linkExistingRecord(myCon);

		// Navigate to KB module and create a KB record for portal user exists in portal
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.showMore();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(kbData.get("name"));
		sugar().knowledgeBase.createDrawer.getEditField("status").set(kbData.get("status"));
		sugar().knowledgeBase.createDrawer.getEditField("isExternal").set("true");
		sugar().knowledgeBase.createDrawer.getEditField("date_expiration").set(kbData.get("date_of_expiry"));
		VoodooUtils.focusFrame(0);
		new VoodooControl("body", "id", "tinymce").set(kbData.get("doc_text"));
		VoodooUtils.focusDefault();
		sugar().knowledgeBase.createDrawer.save();

		sugar().logout();
	}

	/**
	 * Verify the default fields in portal kb dashlet in the Portal home page
	 *
	 * @throws Exception
	 */
	@Test
	public void Portal_21714_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Log into Portal
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myCon.get("portalName"));
		portalUser.put("password", myCon.get("password"));
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);

		// TODO: VOOD-1121
		// Verify the default fields in portal kb dashlet in the Portal home page 
		// Verify Name
		new VoodooControl("span", "css", ".fld_name").assertContains(kbData.get("name"), true);

		// Verify Published Date
		new VoodooControl("span", "css", ".fld_date_entered").assertContains(VoodooUtils.getCurrentTimeStamp("yyyy-MM-dd"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}