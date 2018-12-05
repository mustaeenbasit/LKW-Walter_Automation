package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_22647 extends SugarTest {

	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Schedule Meeting By Full Form_Verify that meeting can be created by using Full Form for Lead in meetings sub-panel
	 * @throws Exception
	 */
	@Test
	public void Leads_22647_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to the Leads module and navigate to the existing lead record
		sugar().navbar.navToModule(sugar().leads.moduleNamePlural);
		sugar().leads.listView.clickRecord(1);

		// Click the "+" icon in the meetings subpanel on the Lead record view
		StandardSubpanel meetingSubpanel = sugar().leads.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		meetingSubpanel.addRecord();
		String meetingName = sugar().meetings.getDefaultData().get("name");
		FieldSet meetingStatus = testData.get(testName).get(0);
		String statusCanceled = meetingStatus.get("statusCanceled");

		// Enter the name and the status in the meeting create form and click save
		sugar().meetings.createDrawer.getEditField("name").set(meetingName);
		sugar().meetings.createDrawer.getEditField("status").set(statusCanceled);
		sugar().meetings.createDrawer.save();

		// Assert that the meeting is created in the subpanel with the data entered
		meetingSubpanel.getDetailField(1, "name").assertEquals(meetingName, true);
		meetingSubpanel.getDetailField(1, "status").assertEquals(statusCanceled, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}