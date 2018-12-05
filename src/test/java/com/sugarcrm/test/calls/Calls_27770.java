package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.candybean.datasource.FieldSet;


public class Calls_27770 extends SugarTest {
	FieldSet customData = new FieldSet();
	CallRecord myCall;
	
	public void setup() throws Exception {
		// Initialize records
		myCall = (CallRecord) sugar().calls.api.create();
		
		// Login as admin
		sugar().login();
		
		// Update call with recurring type until date
		FieldSet callsData = new FieldSet();
		customData = testData.get(testName).get(0);
		String today = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		
		// Create a recurring call with repeat type = until date
		callsData.put("date_start_date",today);
		callsData.put("repeatType", customData.get("repeatType"));
		callsData.put("repeatOccurType", customData.get("repeatOccurType1"));
		callsData.put("repeatUntil",today);
		myCall.edit(callsData);
	}

	/**
	 * Verify editing one meeting of a repeated meeting set - repeat type is not editable
	 * @throws Exception
	 */
	@Test
	public void Calls_27770_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit call to check not editable fields
		sugar().calls.recordView.edit();
		
		// Verify  Repeat Type + other fields are not editable
		VoodooControl repeatTypeView = sugar().calls.recordView.getDetailField("repeatType");
		VoodooControl repeatTypeEdit = sugar().calls.recordView.getEditField("repeatType");
		repeatTypeView.assertVisible(true);
		repeatTypeEdit.assertVisible(false);
		
		VoodooControl repeatIntervalView = sugar().calls.recordView.getDetailField("repeatInterval");
		VoodooControl repeatIntervalEdit = sugar().calls.recordView.getEditField("repeatInterval");
		repeatIntervalView.assertVisible(true);
		repeatIntervalEdit.assertVisible(false);
		
		VoodooControl repeatUntilView = sugar().calls.recordView.getDetailField("repeatUntil");
		VoodooControl repeatUntilEdit = sugar().calls.recordView.getEditField("repeatUntil");
		repeatUntilView.assertVisible(true);
		repeatUntilEdit.assertVisible(false);
		
		// Cancel single Occurrence edit
		sugar().calls.recordView.cancel();
		
		// Edit all recurrences to change to Repeat Occurrences type 2 - repeat by number of Occurrences
		myCall.editAllReocurrenses();
		sugar().calls.recordView.getEditField("repeatOccurType").set(customData.get("repeatOccurType2"));
		VoodooControl repeatOccurEdit = sugar().calls.recordView.getEditField("repeatOccur");
		repeatOccurEdit.set(customData.get("repeatOccur"));
		sugar().calls.recordView.save();
		
		// Edit call to check editable fields
		sugar().calls.recordView.edit();
		
		// Verify  Repeat Type + other fields are not editable
		repeatTypeView.assertVisible(true);
		repeatTypeEdit.assertVisible(false);
		
		repeatIntervalView.assertVisible(true);
		repeatIntervalEdit.assertVisible(false);
		
		VoodooControl repeatOccurView = sugar().calls.recordView.getDetailField("repeatOccur");
		repeatOccurView.assertVisible(true);
		repeatOccurEdit.assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}