package com.sugarcrm.test.leads;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_26151 extends SugarTest {
	DataSource taskData = new DataSource();
	StandardSubpanel taskSubpanel;

	public void setup() throws Exception {
		taskData = testData.get(testName);
		ArrayList<Record> taskRecords = sugar().tasks.api.create(taskData);
		sugar().leads.api.create();
		
		// Logging in as admin
		sugar().login();

		// Relate Tasks records to Lead
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		taskSubpanel = sugar().leads.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		taskSubpanel.linkExistingRecords(taskRecords);
	}

	/**
	 * Verify new filter can be created in a sidecar sub panel 
	 * @throws Exception
	 */
	@Test
	public void Leads_26151_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet filterData = testData.get(testName+"_filterData").get(0);
		
		// Set the Related Subpanel list to display Task module subpanel
		sugar().leads.recordView.setRelatedSubpanelFilter(sugar().tasks.moduleNamePlural);

		// Create filter
		// TODO: VOOD-486 Need lib support of filter dropdown for subpanels
		new VoodooSelect("a", "css", "span[data-voodoo-name='filter-filter-dropdown'] div a").set(filterData.get("createLabel"));
		VoodooUtils.waitForReady();

		// Enter some filter criteria
		new VoodooSelect("input", "css", ".filter-definition-container div div:nth-child(1) > span").set(filterData.get("fieldName"));
		new VoodooSelect("span", "css", ".filter-definition-container div:nth-child(2) > span").set(filterData.get("operator"));
		new VoodooControl("input", "css", ".filter-definition-container div:nth-child(3) span input").set(taskData.get(0).get("subject"));
		new VoodooControl("input", "css", ".filter-header .controls.span6 input").set(testName);
		VoodooUtils.waitForReady();

		// Save custom filter
		new VoodooControl("a", "css", ".btn.btn-primary.save_button").click();
		VoodooUtils.waitForReady();

		// Verify filter can be saved and result is returned accordingly
		FieldSet fs = new FieldSet();
		fs.put("subject", taskData.get(0).get("subject"));
		taskSubpanel.verify(1, fs, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}