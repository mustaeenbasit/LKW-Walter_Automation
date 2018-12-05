package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.test.SugarTest;

public class Cases_23359 extends SugarTest {
	CaseRecord Cases;

	public void setup() throws Exception {
		Cases = (CaseRecord) sugar().cases.api.create();
		sugar().login();
	}

	/**
	 * Schedule Meeting_Verify that scheduled meeting for case can be created when several fields are entered special characters
	 * (Such as entering "\","#" in "Subject" field) for the  "Schedule Meeting" function.
	 * @throws Exception
	 */
	@Test
	public void Cases_23359_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		Cases.navToRecord();
		FieldSet meetingName = testData.get("env_specialchars").get(0);

		// Toggle to MyDashboard
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
		
		String dashboardTitle = sugar().cases.dashboard.getControl("dashboard").getText().trim();
		if(!dashboardTitle.equals("My Dashboard")) {
			sugar().cases.dashboard.chooseDashboard("My Dashboard");
		}

		// Creating Meeting Record and asserting it in the Cases Record View
		// TODO: VOOD-960
		new VoodooControl("div", "css",".btn.dropdown-toggle.btn-invisible span").click();
		new VoodooControl("a", "css", "[data-dashletaction='createRecord']").click();
		sugar().meetings.createDrawer.getEditField("name").set(meetingName.get("punctuation"));
		sugar().meetings.createDrawer.getEditField("description").set(Cases.getRecordIdentifier());
		sugar().meetings.createDrawer.save();

		// Asserting Meeting in Subpanels through the Cases Record View
		FieldSet fs = new FieldSet();
		fs.put("name", meetingName.get("punctuation"));

		Cases.navToRecord();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
		sugar().cases.recordView.subpanels.get(sugar().meetings.moduleNamePlural).expandSubpanel();
		sugar().cases.recordView.subpanels.get(sugar().meetings.moduleNamePlural).verify(1, fs, true);
		fs.clear();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
