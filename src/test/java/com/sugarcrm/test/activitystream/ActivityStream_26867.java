package com.sugarcrm.test.activitystream;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.ActivityStream;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_26867 extends SugarTest {
	DataSource activityStreamData;
	LeadRecord myLead;
	ArrayList<Record> myTaskRecords = new ArrayList<Record>();

	public void setup() throws Exception {
		activityStreamData = testData.get(testName);

		// Create a Lead record
		myLead = (LeadRecord) sugar.leads.api.create();

		// Create 3 Tasks records 
		FieldSet taskName = new FieldSet();
		for (int i = 0; i < activityStreamData.size(); i++) {
			taskName.put("subject", activityStreamData.get(i).get("taskName"));
			myTaskRecords.add(sugar.tasks.api.create(taskName));
		}

		// Login as Admin user
		sugar.login();
	}

	/**
	 * Linked Activity Stream should generate correct messages when link multiple records at once
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_26867_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		//  In Leads record view, link existing 3 records
		sugar.leads.navToListView();
		sugar.leads.listView.clickRecord(1);
		sugar.leads.recordView.subpanels.get(sugar.tasks.moduleNamePlural).linkExistingRecords(myTaskRecords);

		// Check the Activity Stream in Home -> Activity Stream
		sugar.navbar.selectMenuItem(sugar.home, "activityStream");
		VoodooUtils.waitForReady();

		// 3 AS messages, all in Le module badge. Linked Task1 to Lead, Linked Task2 to Lead and Linked Task3 to Lead
		ActivityStream stream = new ActivityStream();
		for(int i = 0; i < activityStreamData.size(); i++) {
			stream.assertContains(activityStreamData.get(0).get("assertMessageLinked") + " " + activityStreamData.get(i).get("taskName") + " " + activityStreamData.get(0).get("assertMessageTo") + " " + sugar.leads.getDefaultData().get("fullName"), true);
			// TODO: VOOD-977 need defined control for the module icon on activity stream
			new VoodooControl("div", "css", ".activitystream-list.results > li:nth-of-type(" + (i+1) + ") div.label-module").assertEquals(activityStreamData.get(0).get("moduleIconLead"), true);
		}

		// Navigate to Home cube
		// TODO VOOD-954 the clean up will fail if not change back to Home dashboard page
		sugar.navbar.clickModuleDropdown(sugar.home);
		// TODO VOOD-953
		new VoodooControl("a", "css", "li[data-module='Home'] ul[role='menu'] li:nth-of-type(5) a").click();	
		VoodooUtils.waitForReady();

		// Check the Activity Stream in Leads -> List View -> Activity Stream.
		sugar.leads.navToListView();
		sugar.leads.listView.showActivityStream();

		// 3 AS messages, all in Ts module badge. Linked Task1 to Lead, Linked Task2 to Lead and Linked Task3 to Lead
		for(int i = 0; i < activityStreamData.size(); i++) {
			stream.assertContains(activityStreamData.get(0).get("assertMessageLinked") + " " + activityStreamData.get(i).get("taskName") + " " + activityStreamData.get(0).get("assertMessageTo") + " " + sugar.leads.getDefaultData().get("fullName"), true);
			// TODO: VOOD-977 need defined control for the module icon on activity stream
			new VoodooControl("div", "css", ".activitystream-list.results > li:nth-of-type(" + (i+1) + ") div.label-module").assertEquals(activityStreamData.get(0).get("moduleIconTask"), true);
		}
		sugar.leads.listView.showListView(); // Click back to List view icon

		// Check the Activity Stream in Leads -> Record View -> Activity Stream
		sugar.leads.listView.clickRecord(1);
		sugar.leads.recordView.showActivityStream();

		// 3 AS messages, all in Ts module badge. Linked Task1 to Lead, Linked Task2 to Lead and Linked Task3 to Lead
		for(int i = 0; i < activityStreamData.size(); i++) {
			stream.assertContains(activityStreamData.get(0).get("assertMessageLinked") + " " + activityStreamData.get(i).get("taskName") + " " + activityStreamData.get(0).get("assertMessageTo") + " " + sugar.leads.getDefaultData().get("fullName"), true);
			// TODO: VOOD-977 need defined control for the module icon on activity stream
			new VoodooControl("div", "css", ".activitystream-list.results li:nth-of-type(" + (i+1) + ") div.label-module").assertEquals(activityStreamData.get(0).get("moduleIconTask"), true);
		}
		sugar.leads.recordView.showDataView(); // Click back to Data view icon

		// Check the Activity Stream in Tasks -> List View -> Activity Stream
		sugar.tasks.navToListView();
		sugar.tasks.listView.showActivityStream();

		// Verify that no AS messages for Linked Task to Lead
		for(int i = 0; i < activityStreamData.size(); i++) {
			stream.assertContains(activityStreamData.get(0).get("assertMessageLinked") + " " + activityStreamData.get(i).get("taskName") + " " + activityStreamData.get(0).get("assertMessageTo") + " " + sugar.leads.getDefaultData().get("fullName"), false);
		}
		sugar.tasks.listView.showListView(); // Click back to List view icon

		// Check the Activity Stream in Tasks -> Record View -> Activity Stream
		// TODO: VOOD-977 need defined control for the module icon on activity stream
		VoodooControl badgeCtrl = new VoodooControl("div", "css", ".activitystream-list.results > li:nth-of-type(1) div.label-module");

		for(int i = 0; i < myTaskRecords.size(); i++) {
			// 1 AS message, in Le module badge. Linked Task to Lead(As order is not fix so using navToRecord() instead of gotoNextRecord() on record view)
			myTaskRecords.get(i).navToRecord();
			sugar.tasks.recordView.showActivityStream();
			stream.assertCommentContains(activityStreamData.get(0).get("assertMessageLinked") + " " + myTaskRecords.get(i).getRecordIdentifier() + " " + activityStreamData.get(0).get("assertMessageTo") + " " + sugar.leads.getDefaultData().get("fullName"), 1, true);
			badgeCtrl.assertEquals(activityStreamData.get(0).get("moduleIconLead"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}