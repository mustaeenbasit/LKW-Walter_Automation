package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29366 extends SugarTest {
	DataSource kbData = new DataSource();
	ContactRecord myContact;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		kbData = testData.get(testName+"_kbData");
		sugar().knowledgeBase.api.create(kbData);
		// Portal user created
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
	 * Verify that KB without category should not appear in "KB Categories" Dashlet of own
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29366_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Sort KB records by name to update category fields in desired record
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.sortBy("headerName", true);

		// Go to knowledgeBase Record view and add Category
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.showMore();
		sugar().knowledgeBase.recordView.edit();
		sugar().knowledgeBase.recordView.getEditField("category").click();
		// TODO: VOOD-1754, CB-252
		new VoodooControl("div", "css", "[data-action='create-new']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[data-role='add-item']").set(kbData.get(0).get("category")+'\uE007');
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".jstree-focused li:nth-child(1) a").click();
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.recordView.save();

		// In KB record view, add "KB Categories" Dashlet.
		sugar().knowledgeBase.dashboard.clickCreate();
		FieldSet customData = testData.get(testName).get(0);
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

		// Open the KB without category setting, look at the Dashlet.
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(2);

		// Verify that the KB without Category should not appear in the Dashlet. 
		VoodooControl nodeCtrl = new VoodooControl("ins", "css", ".jstree-icon._parse_json_outer");
		nodeCtrl.click();
		new VoodooControl("div", "css", ".jstree-sugar.tree-component").assertContains(kbData.get(1).get("name"), false);

		// Open the KB with category setting, look at the Dashlet.
		sugar().knowledgeBase.recordView.gotoPreviousRecord();

		// Verify that the KB with Category should appear in the Dashlet. 
		new VoodooControl("a", "css", ".jstree-last.jstree-leaf a").assertEquals(kbData.get(0).get("name"), true);

		// Enable sugar portal 
		sugar().admin.portalSetup.enablePortal();

		// Log into Portal. 
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName",myContact.get("portalName"));
		portalUser.put("password",myContact.get("password"));
		portal.loginScreen.navigateToPortal();
		portal.login(portalUser);

		// Go to KB module
		portal.navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);
		VoodooUtils.waitForReady();
		// Open the KB without category setting, look at the Dashlet.
		// TODO: VOOD-1096
		new VoodooControl("a", "css", "[data-original-title='"+kbData.get(1).get("name")+"']"+" a").click();
		VoodooUtils.waitForReady();

		// Verify that the KB without Category should not appear in the Dashlet. 
		nodeCtrl.click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-1121
		VoodooControl dashletCtrl = new VoodooControl("div", "css", "[data-voodoo-name='dashlet-nestedset-list']");
		dashletCtrl.assertContains(kbData.get(1).get("name"), false);

		// Open the KB with category setting, look at the Dashlet.
		// TODO: VOOD-1047
		new VoodooControl("i", "css", ".fa.fa-chevron-right").click();
		VoodooUtils.waitForReady();

		// Verify that the KB with Category should appear in the Dashlet. 
		dashletCtrl.assertContains(kbData.get(0).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}