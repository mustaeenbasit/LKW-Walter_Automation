package com.sugarcrm.test.notes;

import org.junit.Test;
import java.util.ArrayList;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.BugRecord;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.records.NoteRecord;

public class Notes_20988 extends SugarTest {

	ArrayList<Record> myRecordsList = new ArrayList<Record>();
	public void setup() throws Exception {
		// Create test data
		AccountRecord myAccount = (AccountRecord) sugar().accounts.api.create();
		BugRecord myBug = (BugRecord) sugar().bugs.api.create();
		CaseRecord myCase = (CaseRecord) sugar().cases.api.create();
		ContactRecord myContact = (ContactRecord) sugar().contacts.api.create();
		LeadRecord myLead = (LeadRecord) sugar().leads.api.create();
		OpportunityRecord myOpportunity = (OpportunityRecord) sugar().opportunities.api.create();
		TaskRecord myTask = (TaskRecord) sugar().tasks.api.create();
		
		// Add test data records to a list
		myRecordsList.add(myAccount);
		myRecordsList.add(myBug);
		myRecordsList.add(myCase);
		myRecordsList.add(myContact);
		myRecordsList.add(myLead);
		myRecordsList.add(myOpportunity);
		myRecordsList.add(myTask);
		
		sugar().login();
	}

	/**
	 * Create Note_Verify that note can be created with all the fields entered.
	 * @throws Exception
	 */
	@Test
	public void Notes_20988_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Initialize test data
		DataSource testDS = testData.get(testName);
		FieldSet noteRelateFS = new FieldSet();
		
		for (int i = 0; i < testDS.size(); i++) {
			
			// Create notes data with unique Subject and related module + record value
			noteRelateFS.put("subject", testName + testDS.get(i).get("moduleName"));
			noteRelateFS.put("relRelatedToModule", testDS.get(i).get("moduleName"));
			noteRelateFS.put("relRelatedToValue", myRecordsList.get(i).get("name"));
			
			// Create notes with related records
			NoteRecord myNote = (NoteRecord)sugar().notes.create(noteRelateFS);
			
			// Verify notes with related records and other fields
			myNote.verify(noteRelateFS);
		}

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}