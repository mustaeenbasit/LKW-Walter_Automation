package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_28815 extends SugarTest {
	DataSource kbRecords = new DataSource();
	ContactRecord myContact;
	
	public void setup() throws Exception {
		kbRecords = testData.get(testName + "_kbData");
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
		
		// Navigate to KB categories page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");

		// Clicking on Create button
		// TODO: VOOD-1754 : Need Lib support for Categories in KnowledgeBase Module
		new VoodooControl("a", "css", ".fld_add_node_button a").click();

		// Entering KB Category name and enter
		new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])").set(kbRecords.get(0).get("category") + '\ue007');
		VoodooUtils.waitForReady();
		
		// Navigating to KB module
		sugar().knowledgeBase.navToListView();

		VoodooControl categoryOption = new VoodooControl("li", "css", ".jstree-last.jstree-leaf");
		
		// Editing each KB record and updating category to 'Cat1'
		for (int i=1 ; i <= kbRecords.size() ; i++) {
			sugar().knowledgeBase.listView.editRecord(i);
			sugar().knowledgeBase.listView.getEditField( i , "category").click();
			categoryOption.click();
			sugar().knowledgeBase.listView.saveRecord(i);
		}
		
		// Enable portal in admin
		sugar().admin.portalSetup.enablePortal();

		// Logout as admin user
		sugar().logout();
	}

	/**
	 * Knowledge Base & Portal: Verify user can view KB article with categories after filtering
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28815_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData  = testData.get(testName).get(0);
		
		// Navigate to portal URL, login as portal user
		portal().loginScreen.navigateToPortal();
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));
		portal().login(portalUser);

		// Navigate to KnowledgeBase
		portal().navbar.navToModule(portal().knowledgeBase.moduleNamePlural);
		
		// TODO: VOOD-1096: Portal Module Listview support
		new VoodooControl("input", "css", ".dataTables_filter input").set(customData.get("searchItem"));
		VoodooUtils.waitForReady();

		// Verify that upon filtering, only one article is visible.
		new VoodooControl("span", "class", "count").assertContains(customData.get("recordCount"), true);

		// Verify that upon filtering, the article 'article2' is visible.
		new VoodooControl("a", "css", ".list.fld_name a").assertEquals(kbRecords.get(1).get("name"), true);
		
		// Verify that category 'Cat1' is available as text
		new VoodooControl("div", "css", ".fld_category_name div").assertEquals(kbRecords.get(0).get("category"), true);
		
		// Verify that category 'Cat1' is not available as a hyperlink
		new VoodooControl("a", "css", ".fld_category_name a").assertExists(false);
			
		// logging out of portal
		portal().logout();
		
		// Logging in back as admin
		sugar().loginScreen.navigateToSugar();
		sugar().login();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}