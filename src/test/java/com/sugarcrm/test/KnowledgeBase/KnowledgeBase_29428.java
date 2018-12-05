package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29428 extends SugarTest {
	ContactRecord myContact;
	FieldSet kbData = new FieldSet();

	public void setup() throws Exception {
		// Creating account and contact record for portal
		sugar().accounts.api.create();
		kbData = testData.get(testName + "_KbData").get(0);
		sugar().knowledgeBase.api.create(kbData);
		FieldSet portalContactData = testData.get("env_portal_contact_setup").get(0);
		myContact = (ContactRecord) sugar().contacts.api.create(portalContactData);
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Relate contact with account
		// TODO: VOOD-1108 - Provide a Portal Setup method
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().alerts.getAlert().cancelAlert();
		sugar().contacts.recordView.save();
	}

	/**
	 * Verify that custom nested Category is able to be appearing in Portal
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29428_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource customData = testData.get(testName);

		// TODO: VOOD-1754 - Need Lib support for Categories field and Help Text (in RHS) in KnowledgeBase create Drawer.
		// Controls for Category
		VoodooControl categoryNameCtrl = new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])");
		VoodooControl addNewCategoryCtrl = new VoodooControl("span", "css", ".btn.jstree-addnode .fa.fa-plus");

		// View Category from mega menu of KB
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");

		// TODO: VOOD-1754 - Need Lib support for Categories field and Help Text (in RHS) in KnowledgeBase create Drawer.
		// TODO: CB-252 - Need CB support to simulate input of any keyboard key i.e tab, esc etc.
		// Creating 1 parent category
		new VoodooControl("a", "css", ".fld_add_node_button a").click();
		VoodooUtils.waitForReady();
		categoryNameCtrl.set(customData.get(0).get("parentCategory") + '\uE007');
		VoodooUtils.waitForReady();

		// Adding 5 child node to created parent category
		// TODO: VOOD-1754 - Need Lib support for Categories field and Help Text (in RHS) in KnowledgeBase create Drawer.
		// TODO: CB-252 - Need CB support to simulate input of any keyboard key i.e tab, esc etc.
		for (int i = 0; i < customData.size(); i++) {
			addNewCategoryCtrl.click();
			VoodooUtils.waitForReady();
			categoryNameCtrl.set(customData.get(i).get("childCategory") + '\uE007');
			VoodooUtils.waitForReady();
		}

		// Cancel the category create drawer
		new VoodooControl("a", "css", ".fld_close a").click();

		// Adding KB category dashlet at listview of KB
		if (sugar().knowledgeBase.dashboard.getControl("dashboardTitle").queryContains(customData.get(0).get("helpDashboard"), true)) {
			sugar().knowledgeBase.dashboard.chooseDashboard(customData.get(0).get("myDashboard"));
		}

		// Verifying custom categories appears with 5 nested categories in KB Categories" Dashlet in Listview.
		// Verifying parent category
		VoodooControl dashletClosedCategory = new VoodooControl("a", "css", ".jstree-last.jstree-closed a");
		dashletClosedCategory.assertEquals(customData.get(0).get("parentCategory"), true);
		// Expand Parent category
		dashletClosedCategory.click();
		// Pause needed here
		VoodooUtils.pause(100);
		// Verifying all child category
		for (int i = 0; i < customData.size(); i++) {
			new VoodooControl("li", "css", ".jstree-last.jstree-open li:nth-child(" + (i + 1) + ") a").assertEquals(customData.get(i).get("childCategory"), true);
		}

		// Enable sugar portal 
		sugar().admin.portalSetup.enablePortal();

		// Login to Portal
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);

		// Go to KB module
		portal().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);

		// Verifying articles are appearing in list view 
		portal().knowledgeBase.listView.getDetailField(1, "name").assertEquals(sugar().knowledgeBase.getDefaultData().get("name"), true);

		// Verifying No data is availble in KB dashlet in portal
		VoodooControl kbCategoryPortalDashlet = new VoodooControl("div", "css", "[data-voodoo-name='dashlet-nestedset-list']");
		kbCategoryPortalDashlet.assertContains(customData.get(0).get("kbDashletData"), true);

		// Logout from portal
		portal().logout();

		// Navigate to sugar
		VoodooUtils.waitForReady();
		sugar().loginScreen.navigateToSugar();

		// Navigate to KB
		// Adding one more published kb record with parent category
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "createArticle");
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.createDrawer.getEditField("status").set(kbData.get("status"));
		sugar().knowledgeBase.createDrawer.showMore();
		sugar().knowledgeBase.createDrawer.getEditField("isExternal").set(kbData.get("isExternal"));
		sugar().knowledgeBase.createDrawer.getEditField("category").click();
		new VoodooControl("a", "css", ".list .jstree-sugar.tree-component a").click();
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.createDrawer.save();

		// Login to portal
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);

		// Navigate to Kb listview
		portal().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);

		// Verifying new created kb record is shown in kb list view of portal
		portal().knowledgeBase.listView.getDetailField(1, "name").assertEquals(testName, true);

		// Verifying published article and category is shown in kb category dashlet of portal
		kbCategoryPortalDashlet.assertContains(customData.get(0).get("parentCategory"), true);
		VoodooUtils.waitForReady();
		dashletClosedCategory.click();
		// Pause needed here
		VoodooUtils.pause(100);
		kbCategoryPortalDashlet.assertContains(testName, true);

		// Logout from portal
		portal().logout();
		VoodooUtils.waitForReady();

		// Navigate to sugar
		sugar().loginScreen.navigateToSugar();

		// Navigate to KB
		// Edit the same record with last sub category
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.edit();
		sugar().knowledgeBase.recordView.getEditField("category").click();

		// TODO: VOOD-1754 -  Need Lib support for Categories field and Help Text (in RHS) in KnowledgeBase create Drawer.
		// Selecting last nested category
		new VoodooControl("ins", "css", ".jstree-icon._parse_json_outer").click();
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", ".jstree-last.jstree-open li:nth-child(5)").click();
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.recordView.save();

		// Login to portal
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);

		// Navigate to Kb listview
		portal().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);

		// Verifying published article and category is shown in kb category dashlet of portal
		kbCategoryPortalDashlet.assertContains(customData.get(0).get("parentCategory"), true);
		VoodooUtils.waitForReady();

		// Expand the parent category
		dashletClosedCategory.click();
		// Pause needed here
		VoodooUtils.pause(100);

		// Verifying Child category is shown
		kbCategoryPortalDashlet.assertContains(customData.get(4).get("childCategory"), true);

		// Expand the child category to verifying kb record is showing in this child category
		// Need to click to times to expand child category
		VoodooControl dashletChildCategory = new VoodooControl("ins", "css", ".jstree-last.jstree-open ul ins");
		dashletChildCategory.click();
		VoodooUtils.waitForReady();
		dashletChildCategory.click();
		// Pause needed here
		VoodooUtils.pause(100);
		kbCategoryPortalDashlet.assertContains(testName, true);
		portal().logout();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}