package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Cases_23379 extends SugarTest {
	DataSource ds;
	CaseRecord myCase;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().login();
		myCase = (CaseRecord)sugar().cases.api.create();
	}

	/**
	 * Test Case 23379: Create Note_Verify that note for case is created when entering special characters
	 * to several fields (Such as entering Chinese characters in "Subject" filed) for the "Create Note" function.
	 */
	@Test
	public void Cases_23379_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// navigate to cases record
		myCase.navToRecord();

		StandardSubpanel subNotes = sugar().cases.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		FieldSet noteCustSubjData = new FieldSet();
		for (int i = 0; i < ds.size(); i++) {
			noteCustSubjData.put("subject", ds.get(i).get("subject"));

			// Create a new note
			subNotes.scrollIntoView();
			subNotes.addRecord();

			// Enter data in "Subject" and Description fields with Chinese characters and Save
			sugar().notes.createDrawer.showMore();
			sugar().notes.createDrawer.setFields(ds.get(i));
			sugar().notes.createDrawer.save();

			// Verify the note related to the case has been created
			subNotes.verify(1, noteCustSubjData, true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
