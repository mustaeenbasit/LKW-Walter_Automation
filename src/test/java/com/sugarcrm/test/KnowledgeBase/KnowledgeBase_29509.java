package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29509 extends SugarTest {
	public void setup() throws Exception {
		// Login
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that frequency is the time to view in record view
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29509_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource frequency = testData.get(testName);

		// Create a KB record (Frequency = 1 by default)
		sugar().knowledgeBase.create();

		// Inline Edit the KB record
		sugar().knowledgeBase.listView.editRecord(1);

		// Cancel the Edit action
		sugar().knowledgeBase.listView.cancelRecord(1);

		VoodooControl frequencyListView = sugar().knowledgeBase.listView.getEditField(1, "viewCount");
		// Assert that Frequency won't increase (It's still 1)
		frequencyListView.assertEquals(frequency.get(0).get("frequency"), true);

		// Navigate to the KB record's Record View
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.showMore();

		// Assert that Frequency is increased by 1 (i.e now Frequency = 2)
		sugar().knowledgeBase.recordView.getDetailField("viewCount").assertEquals(frequency.get(1).get("frequency"), true);

		// Click the Edit button in the record view
		sugar().knowledgeBase.recordView.edit();

		// Click the cancel button in the record view
		sugar().knowledgeBase.recordView.cancel();

		// Click the View Articles option from the KB navigation to navigate to the KB list view
		sugar().navbar.clickModuleDropdown(sugar().knowledgeBase);
		sugar().knowledgeBase.menu.getControl("viewArticles").click();
		VoodooUtils.waitForReady();

		// Assert that the frequency is same as that in the record view (i.e Frequency = 2) 
		frequencyListView.assertEquals(frequency.get(1).get("frequency"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}