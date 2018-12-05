package com.sugarcrm.test.tasks;

import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;

public class Tasks_29109 extends SugarTest {
	ArrayList<Record> taskRecords = new ArrayList<Record>();
	
	public void setup() throws Exception {
		DataSource myTasks = new DataSource();
		myTasks = testData.get(testName);
		sugar.contacts.api.create();
		taskRecords = sugar.tasks.api.create(myTasks);
		sugar.login();
	}

	/**
	 * TC 29109: Verify that pagination should work for the Task subpanel
	 * @throws Exception
	 */
	@Test
	public void Tasks_29109_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.navbar.navToModule(sugar.contacts.moduleNamePlural);
		sugar.contacts.listView.clickRecord(1);
		
		// Navigating to tasks subpanel in Contacts record view
		StandardSubpanel taskSubpanel = sugar.contacts.recordView.subpanels
			.get(sugar.tasks.moduleNamePlural);
		
		// Adding 6 tasks in tasks subpanel
		taskSubpanel.linkExistingRecords(taskRecords);
		
		// Verifying tasks subpanel shows only 5 tasks
		Assert.assertEquals(5, taskSubpanel.countRows());
		
		// Clicking on more tasks to view all the tasks
		taskSubpanel.showMore();
		VoodooUtils.waitForReady();
		
		// Verifying appearance of all 6 tasks after click on more tasks
		Assert.assertEquals(6, taskSubpanel.countRows());
		
		// Adding one more tasks in tasks subpanel from default data
	    taskSubpanel.create(sugar.tasks.getDefaultData());
	    
		// Verifying after reloads tasks subpanel shows only 5 tasks
		Assert.assertEquals(5, taskSubpanel.countRows());
		
		// Verifying visibility of recently created task
		taskSubpanel.getDetailField(1, "subject")
			.assertEquals(sugar.tasks.getDefaultData().get("subject"), true);
		taskSubpanel.getDetailField(1, "subject").assertVisible(true);
		
		// Clicking on more tasks to view all the tasks
		taskSubpanel.showMore();
		VoodooUtils.waitForReady();
				
		// Verifying appearance of all 7 tasks after click on more tasks
		Assert.assertEquals(7, taskSubpanel.countRows());
		
		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}