package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;

public class Leads_22646 extends SugarTest {
	TaskRecord myTaskRecord;
	StandardSubpanel tasksSubpanel;
	
	public void setup() throws Exception {
		// Initialize test data.
		LeadRecord myLead = (LeadRecord) sugar().leads.api.create();
		sugar().login();
		
		// Create a task record and link it to the leads record in tasks sub panel.
		myLead.navToRecord();
		myTaskRecord = (TaskRecord)sugar().tasks.api.create();
		tasksSubpanel = sugar().leads.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		tasksSubpanel.linkExistingRecord(myTaskRecord);	
	}

	/**
	 * Remove Task_Verify that task for "Activities" can be cancel removing under lead.
	 * @throws Exception
	 */
	@Test
	public void Leads_22646_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Click "Leads" tab on navigation bar and click on existing lead in "Leads" list view.
		sugar().navbar.navToModule(sugar().leads.moduleNamePlural);
		sugar().leads.listView.clickRecord(1);
		
		// Click unlink button in action drop down for a task record in "Tasks" sub-panel and click cancel button.
		tasksSubpanel.expandSubpanelRowActions(1);
		tasksSubpanel.getControl("unlinkActionRow01").click();
		sugar().alerts.getWarning().cancelAlert();
		
		// Verify that task for the Lead is not deleted in the "Tasks" sub-panel.
		Assert.assertTrue("Number of records not equals to ONE", tasksSubpanel.countRows() == 1);
		tasksSubpanel.getDetailField(1, "subject").assertEquals(myTaskRecord.get("subject"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 