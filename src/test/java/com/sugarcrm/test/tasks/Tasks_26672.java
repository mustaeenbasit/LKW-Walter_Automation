package com.sugarcrm.test.tasks;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.test.SugarTest;

public class Tasks_26672 extends SugarTest {
	TaskRecord myTask;
	ContactRecord myContact;
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		// 2 contacts and 2 tasks created via API
		myContact = (ContactRecord)sugar.contacts.api.create();
		FieldSet editedData = new FieldSet();
		editedData.put("firstName", customData.get("firstName"));
		editedData.put("lastName", customData.get("lastName"));
		sugar.contacts.api.create(editedData);
		myTask = (TaskRecord)sugar.tasks.api.create();
		editedData.clear();
		editedData.put("subject", customData.get("subject"));
		sugar.tasks.api.create(editedData);
		sugar.login();
	}

	/**
	 * Verify sorting by Contact column in Task listview is working fine
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tasks_26672_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// contact linked with task record
		sugar.tasks.navToListView();
		sugar.tasks.listView.clickRecord(1);
		sugar.tasks.recordView.edit();
		sugar.tasks.recordView.showMore();
		VoodooControl contactNameCtrl = sugar.tasks.recordView.getEditField("contactName");
		contactNameCtrl.set(myContact.getRecordIdentifier());
		sugar.tasks.recordView.save();
		sugar.tasks.recordView.gotoNextRecord();
		sugar.tasks.recordView.edit();
		sugar.tasks.recordView.showMore();
		contactNameCtrl.set(customData.get("lastName"));
		sugar.tasks.recordView.save();

		// Verify records in listview
		sugar.tasks.navToListView();
		sugar.tasks.listView.verifyField(1, "subject", myTask.get("subject"));
		sugar.tasks.listView.verifyField(2, "subject",customData.get("subject"));

		// Verify records after sorting by Contact name column
		sugar.tasks.listView.sortBy("headerContactname", false);
		sugar.tasks.listView.verifyField(1, "subject",customData.get("subject"));
		sugar.tasks.listView.verifyField(2, "subject", myTask.get("subject"));
		sugar.tasks.listView.sortBy("headerContactname", true);
		sugar.tasks.listView.verifyField(1, "subject", myTask.get("subject"));
		sugar.tasks.listView.verifyField(2, "subject",customData.get("subject"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}