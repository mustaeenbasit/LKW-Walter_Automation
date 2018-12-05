package com.sugarcrm.test.subpanels;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;

public class Subpanels_26296 extends SugarTest {
	StandardSubpanel tasksSubpanel;
	TaskRecord taskRecord;
	FieldSet customData;

	public void setup() throws Exception {
		sugar.opportunities.api.create();
		// Non-closed Tasks records are existing
		taskRecord = (TaskRecord)sugar.tasks.api.create();
		customData =  testData.get(testName).get(0);
		sugar.login();
		// Link task record to Opportunity
		sugar.opportunities.navToListView();
		sugar.opportunities.listView.clickRecord(1);
		tasksSubpanel = sugar.opportunities.recordView.subpanels.get(sugar.tasks.moduleNamePlural);
		tasksSubpanel.linkExistingRecord(taskRecord);
	}

	/**
	 * A Task can be closed in sub panel
	 * 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_26296_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1480
		tasksSubpanel.scrollIntoView();
		// Click on Close on task record
		tasksSubpanel.closeRecord(1);
		// Verify Tasks' Status is Completed.
		// TODO: VOOD-1424 - we need to implement verify method once this VOOD story Resolved
		tasksSubpanel.getDetailField(1, "status").assertContains(customData.get("status"), true);

		// Click on the Tasks link to open up the Tasks record view.
		tasksSubpanel.clickLink(sugar.tasks.getDefaultData().get("subject"), 1);
		// Verify that Tasks' Status is Completed in Tasks record view
		sugar.tasks.recordView.getDetailField("status").assertContains(customData.get("status"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}