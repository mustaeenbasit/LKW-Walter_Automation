package com.sugarcrm.test.notes;

import java.util.HashMap;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Notes_17157 extends SugarTest {
	HashMap<String, String> relatedRecords;

	public void setup() throws Exception {
		sugar().login();
		sugar().notes.api.create().navToRecord();

		relatedRecords = new HashMap<String, String>();
		relatedRecords.put("Account", sugar().accounts.api.create().getRecordIdentifier());
		relatedRecords.put("Contact", sugar().contacts.api.create().getRecordIdentifier());
		relatedRecords.put("Bug", sugar().bugs.api.create().getRecordIdentifier());
		relatedRecords.put("Case", sugar().cases.api.create().getRecordIdentifier());
		relatedRecords.put("Lead", sugar().leads.api.create().getRecordIdentifier());
		relatedRecords.put("Target", sugar().targets.api.create().getRecordIdentifier());
	}

	/**
	 * 17157 Verify flex relate field widget on edit
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_17157_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().notes.recordView.edit();

		for (String moduleName : relatedRecords.keySet()) {
			sugar().notes.recordView.getEditField("relRelatedToModule").set(moduleName);

			// TODO VOOD-795
			// Click on the "Related to" dropdown to expand it
			new VoodooControl("a", "css", ".fld_parent_name.edit div:nth-child(2) a").click();
			
			// Click "Search and Select..." link in the dropdown
			new VoodooControl("div", "xpath", "//div[@id='select2-drop']//div[text()='Search and Select...']").click();

			// Click the first row
			new VoodooControl("a", "css", ".search-and-select tr:first-child input").click();

			// TODO: VOOD-990. After selection, drawer closes and returns user back to original window displaying the proper selection.
			VoodooUtils.pause(1000);

			sugar().notes.recordView.getEditField("relRelatedToValue").assertContains(relatedRecords.get(moduleName), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}