package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21482 extends SugarTest {

	public void setup() throws Exception {
		// Create a KB record with name 'Aperture Laboratories FAQ' 
		sugar().knowledgeBase.api.create();
		
		// Create a KB record with name as the TestName
		FieldSet kbData = new FieldSet();
		kbData.put("name", testName);
		sugar().knowledgeBase.api.create(kbData);
		
		// Login as admin
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Logout as admin
		sugar().logout();
	}

	/**
	 * Verify that Regular User is able to Search Knowledge Base
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21482_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		// Initializing test data
		FieldSet customData = testData.get(testName).get(0);

		// Login as regular user i.e. qauser
		sugar().login(sugar().users.getQAUser());
		
		// Navigate to KB Module
		sugar().knowledgeBase.navToListView();
		
		// Verify that 2 KB records are displayed before applying filter
		Assert.assertTrue("Row count is not equal to 2, when it should.", sugar().knowledgeBase.listView.countRows() == 2);
		
		// Create a filter
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterCreateNew();

		// Setting up filter in list view of KB module
		// TODO: VOOD-1785 - Add/Update Tag-Strategy-Hook Values for Mass Update, Filters in KB, Tags
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set(customData.get("rowName"));
		new VoodooSelect("a", "css", ".detail.fld_filter_row_operator .select2-choice").set(customData.get("rowOperator"));
		new VoodooControl("input", "css", ".detail.fld_name input").set(testName);
		VoodooUtils.waitForReady();
		
		// Verify that only one KB record is returned
		Assert.assertTrue("Row count is greater than 1, when it should not", sugar().knowledgeBase.listView.countRows() == 1);
		
		// Verify that the search result is displayed as per the filter criteria
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}