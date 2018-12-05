package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21461 extends SugarTest {
	String qauser = "";
	DataSource customData = new DataSource();

	public void setup() throws Exception {
		customData = testData.get(testName);

		// Creating KB records
		sugar().knowledgeBase.api.create(customData);

		// Login
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Adding "AssignedTo" field in one KB article.
		sugar().knowledgeBase.navToListView();
		// Sorting to add "AssignedTo" field to a specific record.
		sugar().knowledgeBase.listView.sortBy("headerName", false);
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.edit();
		sugar().knowledgeBase.recordView.showMore();
		qauser = sugar().users.getQAUser().get("userName");
		sugar().knowledgeBase.recordView.getEditField("relAssignedTo").set(qauser);
		sugar().knowledgeBase.recordView.save();
	}

	/**
	 * KB: Articles can be searched by "Author" field
	 *  
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21461_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to KB list view
		sugar().knowledgeBase.navToListView();

		// In list view, create new Filter
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterCreateNew();

		// Set filter field, operator & value
		// TODO: VOOD-1785
		FieldSet filterData = testData.get(testName+"_filterData").get(0);
		new VoodooSelect("div", "css", "div[data-filter='field']").set(filterData.get("assignedTo"));
		new VoodooSelect("div", "css", "div[data-filter='operator']").set(filterData.get("operator"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", ".detail.fld_assigned_user_name .select2-input").set(qauser);
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", ".select2-results li").click();
		VoodooUtils.waitForReady();

		// Verify that all the articles that are create by corresponding user are displayed in the list below.
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(customData.get(1).get("name"), true);

		// Verifying "count", only 1 record with user set as "qauser" in set up is appearing in list view. 
		Assert.assertTrue("List view has more than 1 record", sugar().knowledgeBase.listView.countRows() == 1);

		sugar().knowledgeBase.listView.clickRecord(1);

		// Verifying "Assigned To" field of the search result.
		sugar().knowledgeBase.recordView.getDetailField("relAssignedTo").assertEquals(qauser, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}