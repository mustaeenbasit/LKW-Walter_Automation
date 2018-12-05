package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21476 extends SugarTest {
	DataSource knowledgeBaseDS = new DataSource();
	
	public void setup() throws Exception {
		// Login using admin user
		sugar().login();
		
		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
		
		// Create KB records from UI so that it will be sorted based on created date
		knowledgeBaseDS = testData.get(testName);
		sugar().knowledgeBase.create(knowledgeBaseDS);
	}
		

	/**
	 * Verify Knowledge Bases list view is sorted by "Date Created" by default.
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21476_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify that "Date Created" is in list view by default
		sugar().knowledgeBase.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("date_entered"))).assertVisible(true);

		// Verify that "Date Created" is the farthest to the right in list view
		// (class = '.th-droppable-placeholder-last' after "date created" header ensures that its the farthest)
		// TODO: VOOD-1473 - Not able to verify "Date Created" is the farthest to the right in list view
		new VoodooControl("th", "class", "orderBydate_entered").getChildElement("div", "class", "th-droppable-placeholder-last").assertExists(true);
		
		// Assert that the List view is sorted by "Date Created" by default, in descending order
		int kbRecordCount = knowledgeBaseDS.size();
		for(int i = 1; i < kbRecordCount ; i++) {
			sugar().knowledgeBase.listView.getDetailField(i, "name").assertEquals(knowledgeBaseDS.get(kbRecordCount - i).get("name"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}