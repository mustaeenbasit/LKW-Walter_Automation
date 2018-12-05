package com.sugarcrm.test.subpanels;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_17344 extends SugarTest {
	CaseRecord testCase;	
		
	public void setup() throws Exception {
		sugar.login();
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		testCase = (CaseRecord)sugar.cases.api.create(fs);		
	}

	/**
	 * Test Case 17344: Verify default subpanels for module - Cases
	 * 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_17344_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	
		
		StandardSubpanel callSP = (StandardSubpanel)sugar.bugs.recordView.subpanels.get(sugar.calls.moduleNamePlural);
		StandardSubpanel meetingsSP = (StandardSubpanel)sugar.bugs.recordView.subpanels.get(sugar.meetings.moduleNamePlural);
		StandardSubpanel tasksSP = (StandardSubpanel)sugar.bugs.recordView.subpanels.get(sugar.tasks.moduleNamePlural);
		StandardSubpanel notesSP = (StandardSubpanel)sugar.bugs.recordView.subpanels.get(sugar.notes.moduleNamePlural);
		StandardSubpanel docsSP = (StandardSubpanel)sugar.bugs.recordView.subpanels.get(sugar.documents.moduleNamePlural);
		StandardSubpanel contactsSP = (StandardSubpanel)sugar.bugs.recordView.subpanels.get(sugar.contacts.moduleNamePlural);		
		StandardSubpanel EmailSP = (StandardSubpanel)sugar.bugs.recordView.subpanels.get(sugar.emails.moduleNamePlural);		
		
		testCase.navToRecord();
		
		// Verify default subpanels for module
		callSP.assertVisible(true);
		meetingsSP.assertVisible(true);
		tasksSP.assertVisible(true);
		notesSP.assertVisible(true);
		docsSP.assertVisible(true);
		contactsSP.assertVisible(true);
		EmailSP.assertVisible(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}