package com.sugarcrm.test.ListView;

import java.util.HashMap;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.test.SugarTest;

public class ListView_17033 extends SugarTest {
	TaskRecord task;
	HashMap<String, String> relatedRecords = new HashMap<String, String>();

	public void setup() throws Exception {
		task = (TaskRecord) sugar.tasks.api.create();

		relatedRecords = new HashMap<String, String>();

		relatedRecords.put("Account", sugar.accounts.api.create().getRecordIdentifier());

		relatedRecords.put("Contact", sugar.contacts.api.create().getRecordIdentifier());

		relatedRecords.put("Task", sugar.tasks.api.create().getRecordIdentifier());

		relatedRecords.put("Opportunity", sugar.opportunities.api.create().getRecordIdentifier());

		relatedRecords.put("Bug", sugar.bugs.api.create().getRecordIdentifier());

		relatedRecords.put("Case", sugar.cases.api.create().getRecordIdentifier());

		relatedRecords.put("Lead", sugar.leads.api.create().getRecordIdentifier());

		relatedRecords.put("Target", sugar.targets.api.create().getRecordIdentifier());

		sugar.login();

		task.navToRecord();
	}

	/**
	 * 17033 Verify "Search for more" brings up selection and related module.
	 *
	 * @throws Exception
	 */
	@Test
	public void ListView_17033_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-854
		// Click pencil icon
		new VoodooControl("span", "css", "div.record-cell[data-name='parent_name']").hover();
		new VoodooControl("a", "css", "a[data-name='parent_name']").click();

		for (String moduleName : relatedRecords.keySet()) {
			sugar.tasks.recordView.getEditField("relRelatedToParentType").set(moduleName);

			// TODO VOOD-795
			// Click on the "Related to" dropdown to expand it
			// Click "Search and Select..." link in the dropdown
			new VoodooControl("a", "css", ".fld_parent_name.edit div:nth-child(2) a").click();
			new VoodooControl("div", "xpath", "//div[@id='select2-drop']//div[text()='Search and Select...']").click();
			VoodooUtils.waitForReady();

			// Click the first row
			new VoodooControl("a", "css", ".search-and-select tr:first-child input").click();
			VoodooUtils.waitForReady();

			sugar.tasks.recordView.getEditField("relRelatedToParent").assertContains(relatedRecords.get(moduleName), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}