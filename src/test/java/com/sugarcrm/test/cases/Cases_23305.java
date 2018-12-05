package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.test.SugarTest;

public class Cases_23305 extends SugarTest {
	FieldSet casesRecord;
	CaseRecord myCase;

	public void setup() throws Exception {
		sugar().cases.api.create();
		sugar().login();
	}

	/**
	 * Verify that a note for case is not created when the mandatory
	 * field is left empty for the "Create Note or Attachment" function.
	 *
	 * @throws Exception
	 */
	@Test
	public void Cases_23305_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Cases Detailview
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		// Create a new record under the Notes subpanel
		sugar().cases.recordView.subpanels.get(sugar().notes.moduleNamePlural).addRecord();
		sugar().notes.createDrawer.getEditField("description").set(testName);
		sugar().notes.createDrawer.save();

		// Verify that show a error message about the name field
		sugar().alerts.getError().assertContains("Error Please resolve any errors before proceeding", true);
		sugar().alerts.getAlert().closeAlert();
		sugar().notes.createDrawer.getEditField("subject").assertAttribute("class", "required", true);
		sugar().notes.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
