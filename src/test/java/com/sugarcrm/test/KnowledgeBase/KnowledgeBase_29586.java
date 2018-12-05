package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.PortalTest;

public class KnowledgeBase_29586 extends PortalTest {
	DataSource kbData = new DataSource();
	AccountRecord myAccount;
	ContactRecord myContact;

	public void setup() throws Exception {
		kbData = testData.get(testName);
		FieldSet portalContactData = testData.get("env_portal_contact_setup").get(0);

		// Create portal contact
		myContact = (ContactRecord) sugar().contacts.api.create(portalContactData);

		// Create account record 
		myAccount = (AccountRecord) sugar().accounts.api.create();

		// Login as Admin user
		sugar().login();

		// Enable sugar portal 
		sugar().admin.portalSetup.enablePortal();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Link Contact with Accounts
		// TODO: VOOD-444
		myContact.navToRecord();
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		sugar().alerts.getAlert().cancelAlert();
		sugar().contacts.recordView.save();

		// Navigate to KB categories page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");

		// Create a Category
		// TODO: VOOD-1754
		new VoodooControl("a", "css", "a[name='add_node_button']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])").set(kbData.get(0).get("categoryName")+'\uE007');
		VoodooUtils.waitForReady();

		// Create a valid KB document: 'External Article' is checked, 'Status' is 'Published' and 'Teams' is belongs to the portal user assigned.
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.showMore();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.createDrawer.getEditField("category").click();
		// TODO: VOOD-1754
		new VoodooControl("a", "css", ".jstree-focused li a").click();
		sugar().knowledgeBase.createDrawer.getEditField("isExternal").set(kbData.get(0).get("trueValue"));
		sugar().knowledgeBase.createDrawer.getEditField("status").set(kbData.get(0).get("status"));
		sugar().knowledgeBase.createDrawer.save();

		// Logout from the Admin
		sugar().logout();
	}

	/**
	 * Verify default fields for KB Documents list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29586_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login to portal as a valid portal user
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName",myContact.get("portalName"));
		portalUser.put("password",myContact.get("password"));
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);

		// Navigate to Knowledge Base Articles
		portal().navbar.navToModule(portal().knowledgeBase.moduleNamePlural);

		// Verify that the list view should display these fields (and in this order): KB Document Name, Category, Language and Date Created
		for(int i = 0; i < kbData.size(); i++) {
			// TODO: VOOD-1517 and VOOD-1473
			new VoodooControl("span", "css", ".list-view thead tr th:nth-child(" + (i+1) + ") span").assertEquals(kbData.get(i).get("listViewFields"), true);
		}

		// Verify that the list view should display these values (and in this order): KB Document Name, Category, Language and Date Create
		// TODO: VOOD-1517 and VOOD-1473
		new VoodooControl("a", "css", ".list-view tbody td div a").assertEquals(testName, true);
		new VoodooControl("div", "css", ".list-view tbody td:nth-child(2) div").assertEquals(kbData.get(0).get("categoryName"), true);
		new VoodooControl("div", "css", ".list-view tbody td:nth-child(3) div").assertEquals(kbData.get(0).get("english"), true);
		new VoodooControl("div", "css", ".list-view tbody td:nth-child(4) div").assertContains(VoodooUtils.getCurrentTimeStamp("yyyy-MM-dd"), true);

		VoodooUtils.voodoo.log.info(testName + " completed...");
	}

	public void cleanup() throws Exception {}
} 