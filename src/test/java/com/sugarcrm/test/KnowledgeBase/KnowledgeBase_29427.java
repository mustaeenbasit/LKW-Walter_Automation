package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29427 extends SugarTest {
	ContactRecord myContact;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		FieldSet kbData = testData.get(testName+"_KbData").get(0);
		sugar().knowledgeBase.api.create(kbData);
		FieldSet portalContactData = testData.get("env_portal_contact_setup").get(0);
		myContact = (ContactRecord) sugar().contacts.api.create(portalContactData);
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

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
	 * Verify that deleted KB and its Category are also removed from KB Category Dashlet in Portal
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29427_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to knowledgeBase Record view and add Category and External Article
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.showMore();
		sugar().knowledgeBase.recordView.edit();
		FieldSet customData = testData.get(testName).get(0);
		sugar().knowledgeBase.recordView.getEditField("category").click();
		// TODO: VOOD-1754, CB-252
		new VoodooControl("div", "css", "[data-action='create-new']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[data-role='add-item']").set(customData.get("category")+'\uE007');
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".jstree-focused li:nth-child(1) a").click();
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.recordView.save();

		// Add "KB Categories" Dashlet at KB module list view.
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.dashboard.clickCreate();
		sugar().knowledgeBase.dashboard.getControl("title").set(customData.get("dashboardTitle"));
		sugar().knowledgeBase.dashboard.addRow();
		sugar().knowledgeBase.dashboard.addDashlet(1, 1);

		// TODO: VOOD-960
		// Add a dashlet ->  Knowledge Base Categories & Published Articles
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(customData.get("kbDashlet"));
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		new VoodooControl("a", "css", ".layout_KBContents.drawer.active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Save the home page Dashboard
		sugar().knowledgeBase.dashboard.save();

		// Expand all nodes that contains kb records.
		VoodooControl nodeCtrl = new VoodooControl("ins", "css", ".jstree-icon._parse_json_outer");
		nodeCtrl.click();
		VoodooUtils.waitForReady();

		// Verify The KB record appears at the Calendar node.
		String kbName = sugar().knowledgeBase.getDefaultData().get("name");
		VoodooControl recordCtrl = new VoodooControl("a", "css", ".jstree-last.jstree-leaf a");
		recordCtrl.waitForVisible();
		recordCtrl.assertEquals(kbName, true);

		// Enable sugar portal 
		sugar().admin.portalSetup.enablePortal();

		// Login to Portal
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName",myContact.get("portalName"));
		portalUser.put("password",myContact.get("password"));
		portal.loginScreen.navigateToPortal();
		portal.login(portalUser);

		// Go to KB module
		portal.navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);

		// Expand all nodes that contains kb records.
		nodeCtrl.click();

		// Verify KB record appears at calendar node
		recordCtrl.waitForVisible();
		recordCtrl.assertEquals(kbName, true);

		portal.logout();
		VoodooUtils.waitForReady();
		sugar().loginScreen.navigateToSugar();

		// At sugar side, remove the KB record that is created in step1.
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.deleteRecord(1);
		sugar().knowledgeBase.listView.confirmDelete();

		// Verify The KB record is not appearing at the Dashlet. 
		nodeCtrl.click();
		// TODO: VOOD-960, VOOD-1121
		VoodooControl dashletCtrl = new VoodooControl("div", "css", "[data-voodoo-name='dashlet-nestedset-list']");
		dashletCtrl.assertContains(kbName, false);

		// Log into Portal.
		portal.loginScreen.navigateToPortal();
		portal.login(portalUser);

		// Go to KB module list view.
		portal.navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);

		// Verify Category & the nested KB record is not appearing at the Dashlet. 
		dashletCtrl.assertContains(customData.get("category"), false);
		dashletCtrl.assertContains(kbName, false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}