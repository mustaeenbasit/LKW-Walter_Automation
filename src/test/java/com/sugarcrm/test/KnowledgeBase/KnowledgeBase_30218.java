package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_30218 extends SugarTest {
	ContactRecord myContact;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		FieldSet portalContactData = testData.get("env_portal_contact_setup").get(0);
		myContact = (ContactRecord) sugar().contacts.api.create(portalContactData);
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Enable sugar portal 
		sugar().admin.portalSetup.enablePortal();

		// TODO: VOOD-1108 -Provide a Portal Setup method
		// Account record Associate with contact record
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().alerts.getAlert().cancelAlert();
		sugar().contacts.recordView.save();
	}

	/**
	 * Verify that title is correct for Category Dashlet in the default Dashboard
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30218_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to KB listView 
		sugar().knowledgeBase.navToListView();
		FieldSet customFS = testData.get(testName).get(0);

		// Select My Dashboard from the right hand side
		VoodooControl dashboardTitle = sugar().accounts.dashboard.getControl("dashboardTitle");
		if(!dashboardTitle.queryContains(customFS.get("dashboardTitle"), true))
			sugar().dashboard.chooseDashboard(customFS.get("dashboardTitle"));

		// TODO: VOOD-960
		// Verify this the Dashlet "KB Categories" title should be "Knowledge Base Categories & Published Articles".
		VoodooControl dashleTitle = new VoodooControl("h4", "css", ".dashlet-title");
		dashleTitle.assertContains("Knowledge Base Categories & Published Articles", true);

		// Login to Portal
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);

		// Go to KB module
		portal().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);

		// TODO: VOOD-960, VOOD-1121
		dashleTitle.assertContains(customFS.get("dashletHeaderTitle"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}