package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29425 extends SugarTest {
	DataSource kbRecords = new DataSource();
	ContactRecord myContact;
	
	public void setup() throws Exception {
		// Initializing KB test data records
		kbRecords = testData.get(testName + "_kbData");
		
		// Creating test account record to be later associated with Contact record
		sugar().accounts.api.create();
		
		// Creating a Contact record with Portal Data
		FieldSet portalData = testData.get("env_portal_contact_setup").get(0);
		myContact = (ContactRecord) sugar().contacts.api.create(portalData);
				
		// Creating 2 kb records
		sugar().knowledgeBase.api.create(kbRecords);
		
		// Login as admin
		sugar().login();
		
		// Associating account record with Contact record
		// TODO: VOOD-1833 Ensure Portal users always have an account
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(sugar().accounts.defaultData.get("name"));
		sugar().alerts.getAlert().confirmAlert();
		sugar().contacts.recordView.save();
		
		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
				
		// Navigate to KB Categories page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");
		
		// Clicking on Create button
		// TODO: VOOD-1754 Need Lib support for Categories in KnowledgeBase Module
		VoodooControl createBtn = new VoodooControl("a", "css", ".fld_add_node_button a");
		
		// Create 2 Categories.
		for(int i = 0 ; i < 2 ; i++) {
			// Clicking on Create button
			createBtn.click();

			// Entering KB Category name and enter
			new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])").set(kbRecords.get(i).get("category") + '\ue007');
			VoodooUtils.waitForReady();
		}
		
		// Navigating to KB module
		sugar().knowledgeBase.navToListView();

		// Sorting KB Records before editing their categories
		sugar().knowledgeBase.listView.sortBy("headerName", true);
	
		// Editing both KB records and updating their category value
		for (int i = 1 ; i <= kbRecords.size() ; i++) {
			sugar().knowledgeBase.listView.editRecord(i);
			sugar().knowledgeBase.listView.getEditField( i , "category").click();
			int categoryOptionNumber = Integer.parseInt(kbRecords.get(i - 1).get("category").replace("Cat", ""));
			new VoodooControl("li", "css", ".jstree li:nth-child( " + categoryOptionNumber +") a").click();
			sugar().knowledgeBase.listView.saveRecord(i);
		}
		
		// Enable portal in admin
		sugar().admin.portalSetup.enablePortal();
	}

	/**
	 * Verify that Categories are shown correctly related to externally published articles
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29425_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		// Initializing test data
		FieldSet customData  = testData.get(testName).get(0);
		
		// Navigating to KB module
		sugar().knowledgeBase.navToListView();
		
		// Select My Dashboard from RHS pane so as to get the 'Knowledge Base Categories & Published Articles' dashlet
		if(!sugar().knowledgeBase.dashboard.getControl("dashboardTitle").queryContains(customData.get("myDashboard"), true)) {
			sugar().dashboard.chooseDashboard(customData.get("myDashboard"));
		}
		
		// Verifying Categories Nodes and KB Articles under each node in 'Knowledge Base Categories & Published Articles' dashlet  
		verifyNodesAndKbArticles();
		
		// Logout as admin user
		sugar().logout();
		
		// Navigate to portal URL and login as portal user
		portal().loginScreen.navigateToPortal();
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));
		portal().login(portalUser);

		// Navigate to KnowledgeBase
		portal().navbar.navToModule(portal().knowledgeBase.moduleNamePlural);
		
		// Verifying Categories Nodes and KB Articles under each node in 'Knowledge Base Categories & Published Articles' dashlet in Portal 
		verifyNodesAndKbArticles();
		
		// logging out of portal
		portal().logout();
		
		// Logging in back as admin
		sugar().loginScreen.navigateToSugar();
		sugar().login();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	// Method to verify Categories Nodes and KB Articles under each node in 'Knowledge Base Categories & Published Articles' dashlet
	private void verifyNodesAndKbArticles() throws Exception {
		// TODO: VOOD-960 Dashlet selection
		VoodooControl categoryNode1 = new VoodooControl("a", "css", ".nested-list-widget .jstree-closed a");
		VoodooControl categoryNode2 = new VoodooControl("a", "css", ".nested-list-widget .jstree-closed:nth-child(2) a");
		VoodooControl kbRecord1 = new VoodooControl("a", "css", ".nested-list-widget li.jstree-open li a");
		VoodooControl kbRecord2 = new VoodooControl("a", "css", ".nested-list-widget li.jstree-open:nth-child(2) li a");
		
		// Verify that the Category nodes are displayed as created
		categoryNode1.assertEquals(kbRecords.get(0).get("category"), true);
		categoryNode2.assertEquals(kbRecords.get(1).get("category"), true);

		// Clicking Category nodes in order to expand
		categoryNode1.click();
		VoodooUtils.waitForReady();
		categoryNode2.click();
		VoodooUtils.waitForReady();

		// Verify that KB records are displayed under their respective categories
		kbRecord1.assertEquals(kbRecords.get(0).get("name"), true);
		kbRecord2.assertEquals(kbRecords.get(1).get("name"), true);
	}
	
	public void cleanup() throws Exception {}
}