package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest; 

public class Leads_17972 extends SugarTest {
	FieldSet myTasks;
	public void setup() throws Exception {
		myTasks = testData.get("Leads_17972").get(0);
		sugar().login();
		sugar().leads.api.create();
	}

	/**
	 * TC 17972: Create a related record from Leads' sub panel
	 * @throws Exception
	 */
	@Test
	public void Leads_17972_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// TODO: VOOD-598.  Once it is fixed, please update the following steps
		// Select Tasks module in Related  
		new VoodooControl("i", "css", "span.select2-chosen i.fa-caret-down").click();
		new VoodooControl("li", "css", "ul.select2-results li:nth-child(4)").click();

		// Click on + in Tasks to create a Task record
		StandardSubpanel taskRecord = sugar().leads.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		taskRecord.addRecord();
		// Create a Task record
		sugar().tasks.createDrawer.setFields(myTasks);
		sugar().tasks.createDrawer.save();

		// Verify the Task record is appearing in Tasks sub panel
		FieldSet fs = new FieldSet();
		fs.put("subject", myTasks.get("subject"));
		taskRecord.verify(1,fs , true);

		// Return back to default Filter
		new VoodooControl("i", "css", "span.select2-chosen i.fa-caret-down").click();
		new VoodooControl("li", "css", "ul.select2-results li:first-child").click();

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}