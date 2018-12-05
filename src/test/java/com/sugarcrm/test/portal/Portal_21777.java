package com.sugarcrm.test.portal;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.PortalTest;

public class Portal_21777 extends PortalTest {
	ContactRecord myCon;
	FieldSet kbData = new FieldSet();

	public void setup() throws Exception {
		kbData = testData.get(testName).get(0);
		FieldSet portalContact = testData.get("env_portal_contact_setup").get(0);

		// Create contact for portal access
		myCon = (ContactRecord)sugar().contacts.api.create(portalContact);

		// Create an Account record
		AccountRecord myAcc = (AccountRecord)sugar().accounts.api.create();

		// Login
		sugar().login();

		// Add Account to Contact
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(myAcc.getRecordIdentifier());
		sugar().alerts.getAlert().confirmAlert();
		sugar().contacts.recordView.save();
		VoodooUtils.waitForReady();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Enable Portal
		sugar().admin.portalSetup.enablePortal();

		// navigate to KB module 
		sugar().navbar.navToModule("KBContents");

		// Create KB via UI
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.showMore();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(kbData.get("name"));
		sugar().knowledgeBase.createDrawer.getEditField("status").set(kbData.get("status"));
		sugar().knowledgeBase.createDrawer.getEditField("isExternal").set("true");
		sugar().knowledgeBase.createDrawer.getEditField("date_expiration").set(kbData.get("date_of_expiry"));

		VoodooUtils.focusFrame(0);
		new VoodooControl("body", "id", "tinymce").set(kbData.get("doc_text"));
		VoodooUtils.focusDefault();

		// Save
		sugar().knowledgeBase.createDrawer.save();

		// Logout from the Admin user
		sugar().logout();
	}

	/**
	 * Test Case 21714: Verify the default fields in portal kb dashlet in the Portal home page 
	 * Test Case 21777: Verify all the relevant fields in detailed view of a kb article
	 * 
	 * @throws Exception
	 */
	@Test
	public void Portal_21777_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Log into Portal
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myCon.get("portalName"));
		portalUser.put("password", myCon.get("password"));
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);

		// Verify the default fields in portal kb dashlet in the Portal home page 
		// Verify Name
		// TODO: VOOD-1047 and VOOD-1121
		new VoodooControl("span", "css", ".fld_name").assertContains(kbData.get("name"), true);

		// Verify Published Date
		new VoodooControl("span", "css", ".fld_date_entered").assertContains(VoodooUtils.getCurrentTimeStamp("yyyy-MM-dd"), true); 

		// TODO: VOOD-1118 - Need Lib support for KnowledgeBase module controls and functions in Portal
		portal().navbar.navToModule("KBContents");
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".fld_name a").click();
		VoodooUtils.waitForReady();

		// Verify all the relevant fields in detailed view of a kb article
		// Verify Name
		new VoodooControl("span", "css", ".record-cell [data-voodoo-name='name']").assertContains(kbData.get("name"), true);

		// Verify Published Date
		new VoodooControl("span", "css", ".record-cell .fld_active_date[data-voodoo-name='active_date']").assertContains(VoodooUtils.getCurrentTimeStamp("yyyy-MM-dd"), true); 

		// Verify Body text
		new VoodooControl("span", "css", ".record-cell .fld_kbdocument_body[data-voodoo-name='kbdocument_body']").assertContains(kbData.get("doc_text"), true); 

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}