package com.sugarcrm.test.calls;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_27663 extends SugarTest {
	public void setup() throws Exception {
		sugar().calls.api.create();
		sugar().login(sugar().users.getQAUser());		
	}

	/**
	 * Verify that changing between Repeat Until & Repeat Occurrences fields in Edit call
	 *
	 * @throws Exception
	 */
	@Test
	public void Calls_27663_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource customData = testData.get(testName);

		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(1);
		sugar().calls.recordView.edit();
		sugar().calls.recordView.showMore();

		// Fill Repeat Type = Monthly
		VoodooControl repeatTypeCtrl = sugar().calls.recordView.getEditField("repeatType");
		repeatTypeCtrl.set(customData.get(0).get("repeat_type"));

		// Select 'Repeat Until' set more than 62 days after start date 
		VoodooControl repeatOccurTypeCtrl = sugar().calls.recordView.getEditField("repeatOccurType");
		repeatOccurTypeCtrl.set(customData.get(0).get("repeatOccurType"));
		sugar().calls.recordView.getEditField("repeatUntil").set(customData.get(0).get("repeat_until"));
		sugar().calls.recordView.save();

		// Verify, 1st call has Start Date as today (in script start date is from default data), 
		// 2nd call has Start Date as next month of Start date as the 1st call and so on.
		sugar().calls.navToListView();
		for (int i = 0; i < customData.size() - 1; i++) {
			// Using XPath to assert calls by unique date.
			new VoodooControl("tr", "xpath", "//div[@class='flex-list-view-content']/table/tbody/tr[contains(.,'" + customData.get(i).get("start_date_monthly") + "')]").assertVisible(true);
		}

		// Verify that 3 monthly repeated calls are created
		Assert.assertTrue("Only 3 monthly repeated calls are exist in the list view", sugar().calls.listView.countRows() == 3);

		// Open first call and Edit All Recurrence.
		sugar().calls.listView.clickRecord(1);
		sugar().calls.recordView.openPrimaryButtonDropdown();
		sugar().calls.recordView.getControl("editAllReocurrences").click();
		VoodooUtils.waitForReady();

		// Change Repeat Type to Daily
		repeatTypeCtrl.set(customData.get(1).get("repeat_type"));
		// Select 4 in 'Repeat Occurrence'
		repeatOccurTypeCtrl.set(customData.get(1).get("repeatOccurType"));
		sugar().calls.recordView.getEditField("repeatOccur").set(customData.get(0).get("repeat_count"));

		// Save and navigate to list view
		sugar().calls.recordView.save();
		sugar().calls.navToListView();

		// Verify that 4 daily repeated calls are created
		Assert.assertTrue("Only 4 monthly repeated calls are exist in the list view", sugar().calls.listView.countRows() == 4);

		// Verify start date of calls
		for (int i = 0; i < customData.size(); i++) {
			// Using XPath to assert calls by unique date.
			new VoodooControl("tr", "xpath", "//div[@class='flex-list-view-content']/table/tbody/tr[contains(.,'" + customData.get(i).get("start_date_daily") + "')]").assertVisible(true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}