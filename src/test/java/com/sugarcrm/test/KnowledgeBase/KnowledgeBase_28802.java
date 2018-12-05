package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_28802 extends SugarTest {
	DataSource kbRecords = new DataSource();
	int noOfKbRecords = 0;
	ContactRecord myContact;
	
	public void setup() throws Exception {
		kbRecords = testData.get(testName + "_kbData");
		FieldSet customData  = testData.get(testName).get(0);
		sugar().accounts.api.create();
		
		// Creating a Contact record with Portal Data
		FieldSet portalData = testData.get("env_portal_contact_setup").get(0);
		myContact = (ContactRecord) sugar().contacts.api.create(portalData);
				
		// Creating 3 kb records
		sugar().knowledgeBase.api.create(kbRecords);
		sugar().login();
		
		// Associating account record with Contact record
		// TODO: VOOD-1833: Ensure Portal users always have an account
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(sugar.accounts.defaultData.get("name"));
		sugar().alerts.getAlert().confirmAlert();
		sugar().contacts.recordView.save();
		
		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
				
		// Navigate to KB Categories page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");
		
		// Clicking on Create button
		// TODO: VOOD-1754 Need Lib support for Categories in KnowledgeBase Module
		VoodooControl createBtn = new VoodooControl("a", "css", ".fld_add_node_button a");
		noOfKbRecords = kbRecords.size();
		
		// Create 3 Categories.
		for(int i = 0 ; i < noOfKbRecords ; i++) {

			// Clicking on Create button
			createBtn.click();

			// Entering KB Category name and enter
			new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])").set(kbRecords.get(i).get("category") + '\ue007');
			VoodooUtils.waitForReady();
		}
		
		// Navigating to KB module
		sugar().knowledgeBase.navToListView();

		// Tagging several records via Mass Update
		sugar().knowledgeBase.listView.toggleSelectAll();
		sugar().knowledgeBase.listView.openActionDropdown();
		sugar().knowledgeBase.listView.massUpdate();
		sugar().knowledgeBase.massUpdate.getControl("massUpdateField02").set(customData.get("tags"));
		VoodooUtils.waitForReady();
			
		// TODO: VOOD-1785 Add/Update Tag-Strategy-Hook Values for Mass Update, Filters in KB, Tags
		VoodooControl tagMassupdateField = new VoodooControl("input", "css", ".massupdate.fld_tag ul input");
		// TODO: CB-252, VOOD-1437
		tagMassupdateField.set(testName + '\uE007');
		
		// Click on Update button
		sugar().knowledgeBase.massUpdate.getControl("update").click();
		
		// Sorting KB Records before updating each
		sugar().knowledgeBase.listView.sortBy("headerName", true);
	
		// Editing each KB record and updating its category value
		for (int i = 1 ; i <= kbRecords.size() ; i++) {
			sugar().knowledgeBase.listView.editRecord(i);
			sugar().knowledgeBase.listView.getEditField( i , "category").click();
			new VoodooControl("li", "css", ".jstree li:nth-child( " + i +") a").click();
			sugar().knowledgeBase.listView.saveRecord(i);
		}
		
		// Enable portal in admin
		sugar().admin.portalSetup.enablePortal();

		// Logout as admin user
		sugar().logout();
	}

	/**
	 * Knowledge Base & Portal: Verify user can correctly sort KB in Listview by Category in portal
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28802_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Navigate to portal URL, login as portal user
		portal().loginScreen.navigateToPortal();
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));
		portal().login(portalUser);

		// Navigate to KnowledgeBase
		portal().navbar.navToModule(portal().knowledgeBase.moduleNamePlural);
		
		// Clicking on Category Header to sort records in Descending Order
		// TODO: VOOD-1096: Portal Module Listview support
		new VoodooControl("th", "css", ".sorting.orderBycategory_name").click();
		VoodooUtils.waitForReady();
		
		// Verifying that KB Records are displayed in Descending order w.r.t. Category
		for (int i = 1 ; i <= noOfKbRecords ; i++) {
			new VoodooControl("span", "css", ".dataTable tr:nth-child("+ i +") .fld_category_name").assertEquals(kbRecords.get(noOfKbRecords - i).get("category"), true);
			new VoodooControl("span", "css", ".dataTable tr:nth-child("+ i +") .fld_name").assertEquals(kbRecords.get(noOfKbRecords - i).get("name"), true);
		}
		
		// Clicking on Category Header to sort records in Ascending Order
		new VoodooControl("th", "css", ".sorting_desc.orderBycategory_name").click();
		VoodooUtils.waitForReady();
		
		// Verifying that KB Records are displayed in Ascending order w.r.t. Category
		for (int i = 1 ; i <= noOfKbRecords ; i++) {
			new VoodooControl("span", "css", ".dataTable tr:nth-child("+ i +") .fld_category_name").assertEquals(kbRecords.get(i - 1).get("category"), true);
			new VoodooControl("span", "css", ".dataTable tr:nth-child("+ i +") .fld_name").assertEquals(kbRecords.get(i - 1).get("name"), true);
		}
		
		// logging out of portal
		portal().logout();
		
		// Logging in back as admin
		sugar().loginScreen.navigateToSugar();
		sugar().login();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}