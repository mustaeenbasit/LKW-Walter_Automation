package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Raina  <araina@sugarcrm.com>
 */
public class KnowledgeBase_21479 extends SugarTest {
	DataSource myDS = new DataSource();

	public void setup() throws Exception {
		myDS= testData.get(testName);

		// Create 3 KB articles with default Author
		sugar().knowledgeBase.api.create(myDS);

		// Login 
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Add Author name column in KB list View
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);

		// Sort Desc KB listview items by Article name
		// Sorting is needed because order is not consistent in list view
		sugar().knowledgeBase.listView.sortBy("headerName", false);

		// Add Author column to the listview layout
		// TODO: VOOD-1761 Define hidden columns on ListView  
		// TODO: VOOD-1517 VOOD-1761 After fix Use toggleHeaderColumn("headerAuthor");
		sugar().knowledgeBase.listView.getControl("moreColumn").click();
		new VoodooControl("li", "css", ".dropdown-menu.left " + "[data-field-toggle='assigned_user_name']").click();
		sugar().knowledgeBase.listView.getControl("moreColumn").click();

		// Update Author names with unique values
		sugar().knowledgeBase.listView.editRecord(2);
		sugar().knowledgeBase.listView.getEditField(2, "relAssignedTo").set(myDS.get(1).get("relAssignedTo"));
		sugar().knowledgeBase.listView.saveRecord(2);
		sugar().knowledgeBase.listView.editRecord(1);
		sugar().knowledgeBase.listView.getEditField(1,"relAssignedTo").set(myDS.get(0).get("relAssignedTo"));
		sugar().knowledgeBase.listView.saveRecord(1);
	}

	/**
	 * Verify KB listview can be sorted correctly by Name and Author fields
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21479_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Sort Asc KB listview items by Article name and verify sort
		sugar().knowledgeBase.listView.sortBy("headerName", true);
		sugar().knowledgeBase.listView.verifyField(1, "name", myDS.get(2).get("name"));
		sugar().knowledgeBase.listView.verifyField(2, "name", myDS.get(1).get("name"));
		sugar().knowledgeBase.listView.verifyField(3, "name", myDS.get(0).get("name"));

		// Sort Desc KB listview items by Article name and verify sort
		sugar().knowledgeBase.listView.sortBy("headerName", false);
		sugar().knowledgeBase.listView.verifyField(1, "name", myDS.get(0).get("name"));
		sugar().knowledgeBase.listView.verifyField(2, "name", myDS.get(1).get("name"));
		sugar().knowledgeBase.listView.verifyField(3, "name", myDS.get(2).get("name"));

		// Sort KB items by Author name in Desc order
		// TODO: VOOD-1761 Define hidden columns on ListView
		VoodooControl headerAuthor = new VoodooControl("th", "css", ".orderByassigned_user_name");
		// TODO: VOOD-1371 Add ability to override the scroll parent in scrollIntoViewIfNeeded
		VoodooControl headerAuthorScrollDiv = new VoodooControl("div", "css", ".orderByassigned_user_name .ui-draggable.ui-draggable-handle");
		headerAuthorScrollDiv.scrollIntoViewIfNeeded(false);
		headerAuthor.click();
		VoodooUtils.waitForReady();

		// Verify KB items sorted desc by Author 
		headerAuthor.assertAttribute("class", "sorting_desc", true);
		sugar().knowledgeBase.listView.verifyField(1,"relAssignedTo",myDS.get(1).get("relAssignedTo"));
		sugar().knowledgeBase.listView.verifyField(2,"relAssignedTo",myDS.get(2).get("relAssignedTo"));
		sugar().knowledgeBase.listView.verifyField(3,"relAssignedTo",myDS.get(0).get("relAssignedTo"));

		// Sort KB items by Author name in Asc order
		headerAuthorScrollDiv.scrollIntoViewIfNeeded(false);
		headerAuthor.click();
		VoodooUtils.waitForReady();

		// Verify KB items sorted asc by Author 
		headerAuthor.assertAttribute("class", "sorting_asc", true);
		sugar().knowledgeBase.listView.verifyField(1,"relAssignedTo",myDS.get(0).get("relAssignedTo"));
		sugar().knowledgeBase.listView.verifyField(2,"relAssignedTo",myDS.get(2).get("relAssignedTo"));
		sugar().knowledgeBase.listView.verifyField(3,"relAssignedTo",myDS.get(1).get("relAssignedTo"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}