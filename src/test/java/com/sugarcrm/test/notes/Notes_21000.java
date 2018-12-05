package com.sugarcrm.test.notes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Notes_21000 extends SugarTest {

	public void setup() throws Exception {
		sugar().notes.api.create();
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Mass Update Note_Verify that note can be modified by "Mass Update" function.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_21000_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Notes" link in navigation shortcuts.
		sugar().navbar.navToModule(sugar().notes.moduleNamePlural);
		
		// Select the record by checking the check box in front of the notes record.
		sugar().notes.listView.checkRecord(1);

		FieldSet fs = new FieldSet();
		fs.put("Contact", sugar().contacts.getDefaultData().get("firstName"));
		fs.put("Assigned to", sugar().users.getQAUser().get("userName"));
		
		// Perform mass update
		sugar().notes.massUpdate.performMassUpdate(fs);
		
		// Verify that the list information of the selected note is displayed as modified. 
		// TODO: SC-4641
		//sugar().notes.listView.getDetailField(1, "contact").assertEquals(sugar().contacts.getDefaultData().get("fullName"), true);
		
		// Navigate to notes detail view
		sugar().notes.listView.clickRecord(1);
		
		// Verify that the detail information of the notes record is displayed as modified.
		sugar().notes.recordView.getDetailField("contact").assertEquals(sugar().contacts.getDefaultData().get("fullName"), true);
		sugar().notes.recordView.getDetailField("relAssignedTo").assertEquals(sugar().users.getQAUser().get("userName"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}