package com.sugarcrm.test.leads;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_17670 extends SugarTest {
	DataSource taskData;
	FieldSet filterData;
	LeadRecord myLead;
	StandardSubpanel tasksSubpanel;
	ArrayList <Record> taskRecords;

	public void setup() throws Exception {
		myLead = (LeadRecord)sugar().leads.api.create();
		taskData = testData.get(testName);
		taskRecords = sugar().tasks.api.create(taskData);
		filterData = testData.get(testName+"_data").get(0);

		sugar().login();
		myLead.navToRecord();
		tasksSubpanel = sugar().leads.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		tasksSubpanel.linkExistingRecords(taskRecords);
	}

	/**
	 * Verify that For the default stack view for All subpanel there will be no option to create filter (only search)
	 * @throws Exception
	 */
	@Test
	public void Leads_17670_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");				

		tasksSubpanel.scrollIntoView();
		// Select All in Related, Click on Filter to observe
		// TODO: VOOD-468
		new  VoodooControl("div", "css", ".select2-container.select2-container-disabled.select2.search-filter").click();
		// Verify  No "Create New". 
		new VoodooControl("span", "css", "[data-voodoo-name='filter-filter-dropdown']").assertContains(filterData.get("newFilter"), false);

		// Search any starting string of the Tasks associated with this lead record.
		sugar().leads.recordView.setSearchString(taskData.get(0).get("subject").substring(0,2));

		// Verify "Found the searched records in Tasks sub panel"
		for (int i = 0; i < taskData.size()-1; i++){
			if (i == 2)
				tasksSubpanel.assertContains(taskData.get(i).get("subject"), false);
			else
				tasksSubpanel.assertContains(taskData.get(i).get("subject"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}