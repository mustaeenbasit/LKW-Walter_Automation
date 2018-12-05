package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Cases_23433 extends SugarTest {
	public void setup() throws Exception {
		sugar().cases.api.create();
		sugar().login();
	}

	/**
	 * Schedule Meeting_Verify that right after failing for saving a meeting without subject in case detail view, the newly entered subject for the created meeting is displayed in "Activities" sub-panel when saving it again.
	 * @throws Exception
	 */
	@Test
	public void Cases_23433_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet messageData = testData.get(testName).get(0);

		// Go to Cases module.Go to a case detail view.
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		// Click "+" button in "Meetings" sub-panel.
		StandardSubpanel meetingsSubpanel = sugar().cases.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		meetingsSubpanel.addRecord();

		// Save the meeting without subject entered by clicking save button.
		sugar().meetings.createDrawer.save();

		// Verify that the Subject field should be highlighted in red color and error message is displayed.
		sugar().alerts.getError().assertContains(messageData.get("errorMessage"), true);
		sugar().alerts.getError().closeAlert();
		// No method to check red color of the field so assert 'error' text in class to verify that the field highlighted in red color
		new VoodooControl("span", "css", ".fld_name.edit").assertAttribute("class", "error", true);

		// Enter any characters in "Subject" text field -> Save
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.save();

		// Verify that the meeting is created with the subject entered
		FieldSet meetingName = new FieldSet();
		meetingName.put("name", testName);
		meetingsSubpanel.verify(1, meetingName, true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
