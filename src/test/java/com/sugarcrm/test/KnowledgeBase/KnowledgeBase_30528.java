package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_30528 extends SugarTest {
	VoodooControl categoriesLayoutCtrl, categoryNameCtrl, addBtnCtrl, rolesSaveBtnCtrl;
	FieldSet categoryData = new FieldSet();
	FieldSet roleRecordData = new FieldSet();

	public void setup() throws Exception {
		categoryData = testData.get(testName).get(0);

		// Login
		sugar().login();

		// Admin -> Role Management, create a new role
		roleRecordData = testData.get("env_role_setup").get(0);
		// Create a Role and assign QAUser user
		// Create a new Role 
		AdminModule.createRole(roleRecordData);

		// In KB module, Change Edit and Delete set to Owner
		// TODO: VOOD-580
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css", "td#ACLEditView_Access_KBContents_edit div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_KBContents_edit div select").set(roleRecordData.get("roleOwner"));
		new VoodooControl("div", "css", "td#ACLEditView_Access_KBContents_delete div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_KBContents_delete div select").set(roleRecordData.get("roleOwner"));

		// Save the Role
		rolesSaveBtnCtrl = new VoodooControl("input", "css", "div#category_data .button");
		rolesSaveBtnCtrl.click();
		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();

		// Assign role to "QAUser"
		AdminModule.assignUserToRole(roleRecordData);

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to KB categories page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");
		VoodooUtils.waitForReady();

		// Define controls for the KB category page
		// TODO: VOOD-1754
		addBtnCtrl = new VoodooControl("a", "css", "a[name='add_node_button']");
		categoryNameCtrl = new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])");		
		categoriesLayoutCtrl = new VoodooControl("div", "css", ".layout_Categories .nested-set-full");

		// Admin create a Category in KB. e.g "admin cat".
		addBtnCtrl.click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-1437 and CB-252
		categoryNameCtrl.set(categoryData.get("categoryName_1") + '\uE007');	
		VoodooUtils.waitForReady();

		// Logout from admin user and login as QAUser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that changing KB module ACL won't change Categories at all
	 *   
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30528_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to KB list view and view Category
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");

		// Create a new Category. e.g. "Sally cat"
		addBtnCtrl.click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-1437 and CB-252
		categoryNameCtrl.set(categoryData.get("categoryName_2") + '\uE007');	
		VoodooUtils.waitForReady();

		// Define Controls
		// TODO: VOOD-1754
		VoodooControl sallyCatCtrl = new VoodooControl("a", "css", categoriesLayoutCtrl.getHookString() + " .jstree-leaf:nth-child(2) .btn-group .jstree-contextmenu");
		VoodooControl admintCatCtrl = new VoodooControl("a", "css", categoriesLayoutCtrl.getHookString() + " .jstree-leaf .btn-group .jstree-contextmenu"); 
		VoodooControl editBtnCtrl = new VoodooControl("a", "css", "#vakata-contextmenu li a");
		VoodooControl deleteBtnCtrl = new VoodooControl("a", "css", "#vakata-contextmenu li:nth-child(9) a");

		// Click on the action of the "Sally cat"
		sallyCatCtrl.click();

		// Verify that the Edit or Delete option remains in Category's action menu
		editBtnCtrl.assertVisible(true);
		deleteBtnCtrl.assertVisible(true);
		editBtnCtrl.assertContains(categoryData.get("edit"), true);
		deleteBtnCtrl.assertContains(categoryData.get("delete"), true);

		// Click on the action of "admin cat" or an existing Category
		admintCatCtrl.click();

		// Verify that the Edit or Delete option remains in Category's action menu
		editBtnCtrl.assertVisible(true);
		deleteBtnCtrl.assertVisible(true);
		editBtnCtrl.assertContains(categoryData.get("edit"), true);
		deleteBtnCtrl.assertContains(categoryData.get("delete"), true);

		// Logout from QAUser and login as Admin
		sugar().logout();
		sugar().login();

		// Admin -> Role Management, create a new role
		// Create a new Role 
		roleRecordData.put("roleName", testName);
		AdminModule.createRole(roleRecordData);

		// In KB module, Change View and List set to Owner.
		// TODO: VOOD-580
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css", "td#ACLEditView_Access_KBContents_view div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_KBContents_view div select").set(roleRecordData.get("roleOwner"));
		new VoodooControl("div", "css", "td#ACLEditView_Access_KBContents_list div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_KBContents_list div select").set(roleRecordData.get("roleOwner"));

		// Save the Role
		rolesSaveBtnCtrl.click();
		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();

		// Assign role to "QAUser"
		AdminModule.assignUserToRole(roleRecordData);

		// Logout from admin user and login as QAUser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Navigate to KB list view and view Category
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");

		// Verify that the KB ategory list view still observe all Categories and action menu are same too
		// TODO: VOOD-1754
		new VoodooControl("a", "css", categoriesLayoutCtrl.getHookString() + " li a").assertContains(categoryData.get("categoryName_1"), true);
		new VoodooControl("a", "css", categoriesLayoutCtrl.getHookString() + " li:nth-child(2) a").assertContains(categoryData.get("categoryName_2"), true);

		// Click on the action of the "Sally cat"
		sallyCatCtrl.click();

		// Verify that the Edit or Delete option remains in Category's action menu
		editBtnCtrl.assertVisible(true);
		deleteBtnCtrl.assertVisible(true);
		editBtnCtrl.assertContains(categoryData.get("edit"), true);
		deleteBtnCtrl.assertContains(categoryData.get("delete"), true);

		// Click on the action of "admin cat" or an existing Category
		admintCatCtrl.click();

		// Verify that the Edit or Delete option remains in Category's action menu
		editBtnCtrl.assertVisible(true);
		deleteBtnCtrl.assertVisible(true);
		editBtnCtrl.assertContains(categoryData.get("edit"), true);
		deleteBtnCtrl.assertContains(categoryData.get("delete"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 