package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29426 extends SugarTest {
	ContactRecord myContact;
	FieldSet customFS = new FieldSet();

	public void setup() throws Exception {
		customFS = testData.get(testName).get(0);
		FieldSet portalContactData = testData.get("env_portal_contact_setup").get(0);
		sugar().accounts.api.create();
		myContact = (ContactRecord) sugar().contacts.api.create(portalContactData);
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Enable sugar portal 
		sugar().admin.portalSetup.enablePortal();

		// VOOD-1833
		// Relate contact with account
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().alerts.getAlert().cancelAlert();
		sugar().contacts.recordView.save();

		// Navigate to KB categories page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");

		// TODO: VOOD-1754
		// Create two categories: Calls and Meeting
		new VoodooControl("a", "css", ".layout_Categories.active a[name='add_node_button']").click();
		VoodooUtils.waitForReady();

		// TODO: CB-252
		new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])").set(testName+'\uE007');
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify that KB records that are not set External Article=true won't be appearing in portal
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29426_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create KB
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.createDrawer.showMore();

		// TODO: VOOD-695 & VOOD-1754
		new VoodooSelect("div", "css", ".fld_category_name.edit [name='category_name']").click();
		new VoodooSelect("li", "css", ".fld_category_name.edit .dropdown-menu .tree ul li").click();
		sugar().knowledgeBase.createDrawer.save();

		// Add "KB Categories" Dashlet in KB module list view
		sugar().knowledgeBase.dashboard.clickCreate();
		sugar().knowledgeBase.dashboard.getControl("title").set(customFS.get("dashboardTitle"));
		sugar().knowledgeBase.dashboard.addRow();
		sugar().knowledgeBase.dashboard.addDashlet(1, 1);

		// TODO: VOOD-960
		// Add a dashlet -> select "Active Tasks" // Knowledge Base Categories & Published Articles
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(customFS.get("selectKBtask"));
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		new VoodooControl("a", "css", ".layout_KBContents.drawer.active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Save the home page Dashboard
		sugar().knowledgeBase.dashboard.save();
		
		// TODO: VOOD-960
		// Verify that the KB record appears in the list view of KB, and appears in the Dashlet.
		sugar().knowledgeBase.listView.verifyField(1, "name", testName);
		VoodooControl dashletViewCtrl = new VoodooControl("a", "css", "[data-place='dashlet-tree'] [data-action='jstree-select']");
		dashletViewCtrl.assertContains(testName, true);

		// login to portal
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName",myContact.get("portalName"));
		portalUser.put("password",myContact.get("password"));
		portal.loginScreen.navigateToPortal();
		portal.login(portalUser);

		// Navigate to case module 
		portal.navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);
		portal.alerts.waitForLoadingExpiration();

		// Verify that the KB record doesn't appear in the KB module list view nor in the Dashlet. 
		portal.knowledgeBase.listView.assertIsEmpty();
		dashletViewCtrl.assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}