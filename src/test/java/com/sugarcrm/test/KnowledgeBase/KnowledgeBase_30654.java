package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_30654 extends SugarTest {
	VoodooControl categoriesLayoutCtrl, categoryNameCtrl;
	DataSource categoryData = new DataSource();
	UserRecord myCustomUser;

	public void setup() throws Exception {
		categoryData = testData.get(testName);

		// Login
		sugar().login();

		// Create a custom Regular User
		// TODO: VOOD-1200
		myCustomUser = (UserRecord) sugar().users.create();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to KB categories page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");
		VoodooUtils.waitForReady();

		// Define controls for the KB category page
		// TODO: VOOD-1754
		VoodooControl addBtnCtrl = new VoodooControl("a", "css", "a[name='add_node_button']");
		categoryNameCtrl = new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])");		
		categoriesLayoutCtrl = new VoodooControl("div", "css", ".layout_Categories .nested-set-full");

		// In KB Category, there are 4 parent Categories. Documents has child level categories. 
		// Create 4 categories Named 'QAUser_Doc' and 'QAUser_Email' -> Doc
		for(int i = 0; i < categoryData.size(); i++) {
			addBtnCtrl.click();
			VoodooUtils.waitForReady();
			// TODO: VOOD-1437 and CB-252
			categoryNameCtrl.set(categoryData.get(i).get("categoryName_1") + '\uE007');		
			VoodooUtils.waitForReady();
			addBtnCtrl.click();
			VoodooUtils.waitForReady();
			// TODO: VOOD-1437 and CB-252
			categoryNameCtrl.set(categoryData.get(i).get("categoryName_2") + '\uE007');		
			VoodooUtils.waitForReady();
			// TODO: VOOD-1754
			new VoodooControl("span", "css", categoriesLayoutCtrl.getHookString() + " .jstree-leaf:nth-child(" + ((2*i)+2) + ") .jstree-addnode span").click();
			// TODO: VOOD-1437 and CB-252
			categoryNameCtrl.set(categoryData.get(0).get("subCategoryName") + '\uE007');
		}

		// Logout from admin user and login as QAUser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that KB Categories should all be public for 7.7 and have no ACL restrictions for 7.7
	 *   
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30654_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to KB list view and view Category
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");

		// Create a couple of Categories, such as cat1 under QAUser_Doc, Max cat2 under QAUser_Email -> Doc
		// TODO: VOOD-1754
		new VoodooControl("span", "css", categoriesLayoutCtrl.getHookString() + " .jstree-leaf .jstree-addnode span").click();
		// TODO: VOOD-1437 and CB-252
		categoryNameCtrl.set(categoryData.get(0).get("firstCat") + '\uE007');
		VoodooControl parentCategoryCtrl = new VoodooControl("div", "css", ".jstree-sugar.tree-component");
		new VoodooControl("ins", "css", parentCategoryCtrl.getHookString() + " li:nth-child(2) ins").click();
		new VoodooControl("span", "css", categoriesLayoutCtrl.getHookString() + " li:nth-child(2) .jstree-leaf .jstree-addnode span").click();
		// TODO: VOOD-1437 and CB-252
		categoryNameCtrl.set(categoryData.get(0).get("secondCat") + '\uE007');

		// Logout from QAUser and login as other regular user
		sugar().logout();
		myCustomUser.login();

		// Navigate to KB -> View Category
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");

		// Create a couple of Categories, such as cat1 at the same level of other 4 demo data Categories, cat2 under Documents -> Email -> cat2.
		new VoodooControl("span", "css", categoriesLayoutCtrl.getHookString() + " .jstree-leaf:nth-child(3) .jstree-addnode span").click();
		// TODO: VOOD-1437 and CB-252
		categoryNameCtrl.set(categoryData.get(0).get("firstCat") + '\uE007');
		new VoodooControl("ins", "css", parentCategoryCtrl.getHookString() + " li:nth-child(4) ins").click();
		new VoodooControl("span", "css", categoriesLayoutCtrl.getHookString() + " li:nth-child(4) .jstree-leaf .jstree-addnode span").click();
		// TODO: VOOD-1437 and CB-252
		categoryNameCtrl.set(categoryData.get(0).get("secondCat") + '\uE007');

		// Logout from QAUser and login as other an admin user
		sugar().logout();
		sugar().login();

		FieldSet roleRecordData = testData.get("env_role_setup").get(0);
		// Create a Role and assign QAUser user
		// Create a new Role 
		AdminModule.createRole(roleRecordData);

		// In KB module, select List and set to Owner
		// TODO: VOOD-580
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css", "td#ACLEditView_Access_KBContents_list div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_KBContents_list div select").set(roleRecordData.get("roleOwner"));
		// Save the Role
		new VoodooControl("input", "css", "div#category_data .button").click();
		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();

		// Assign role to "QAUser"
		AdminModule.assignUserToRole(roleRecordData);

		// Logout from admin user and login as QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Navigate to KB module -> View Category
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");

		// Expand all the Categories
		// TODO: VOOD-1754
		new VoodooControl("ins", "css", parentCategoryCtrl.getHookString() + " li ins").click();
		VoodooUtils.waitForReady();
		new VoodooControl("ins", "css", parentCategoryCtrl.getHookString() + " li:nth-child(2) ins").click();
		VoodooUtils.waitForReady();
		new VoodooControl("ins", "css", parentCategoryCtrl.getHookString() + " li:nth-child(2) ul ins").click();
		VoodooUtils.waitForReady();
		new VoodooControl("ins", "css", parentCategoryCtrl.getHookString() + " li:nth-child(3) ins").click();
		VoodooUtils.waitForReady();
		new VoodooControl("ins", "css", parentCategoryCtrl.getHookString() + " li:nth-child(4) ins").click();
		VoodooUtils.waitForReady();
		new VoodooControl("ins", "css", parentCategoryCtrl.getHookString() + " li:nth-child(4) ul ins").click();
		VoodooUtils.waitForReady();

		// Verify that the all Categories are available and viewable for QAUser
		// TODO: VOOD-1754
		new VoodooControl("a", "css", parentCategoryCtrl.getHookString() + " li a").assertContains(categoryData.get(0).get("categoryName_1"), true);
		new VoodooControl("a", "css", parentCategoryCtrl.getHookString() + " li ul a").assertContains(categoryData.get(0).get("firstCat"), true);
		new VoodooControl("a", "css", parentCategoryCtrl.getHookString() + " li:nth-child(2) a").assertContains(categoryData.get(0).get("categoryName_2"), true);
		new VoodooControl("a", "css", parentCategoryCtrl.getHookString() + " li:nth-child(2) ul a").assertContains(categoryData.get(0).get("subCategoryName"), true);
		new VoodooControl("a", "css", parentCategoryCtrl.getHookString() + " li:nth-child(2) ul ul a").assertContains(categoryData.get(0).get("secondCat"), true);
		new VoodooControl("a", "css", parentCategoryCtrl.getHookString() + " li:nth-child(3) a").assertContains(categoryData.get(1).get("categoryName_1"), true);
		new VoodooControl("a", "css", parentCategoryCtrl.getHookString() + " li:nth-child(3) ul a").assertContains(categoryData.get(0).get("firstCat"), true);
		new VoodooControl("a", "css", parentCategoryCtrl.getHookString() + " li:nth-child(4) a").assertContains(categoryData.get(1).get("categoryName_2"), true);
		new VoodooControl("a", "css", parentCategoryCtrl.getHookString() + " li:nth-child(4) ul a").assertContains(categoryData.get(0).get("subCategoryName"), true);
		new VoodooControl("a", "css", parentCategoryCtrl.getHookString() + " li:nth-child(4) ul ul a").assertContains(categoryData.get(0).get("secondCat"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}