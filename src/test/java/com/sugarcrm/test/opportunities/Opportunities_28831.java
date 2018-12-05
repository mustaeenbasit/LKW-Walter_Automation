package com.sugarcrm.test.opportunities;

import org.junit.Ignore;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Opportunities_28831 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}
	/**
	 * Verify that Database failure is not occurred on running the Summation Report for Opportunities
	 * @throws Exception
	 */
	@Ignore ("Marking as Ignore because related Bug SFA-3785 has been resolved in Sugar 7.8.0.0")
	@Test
	public void Opportunities_28831_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		FieldSet verifyMessage = testData.get(testName).get(0);

		// TODO: VOOD-822
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl createSummationReportCtrl = new VoodooControl("img", "css", "[name='summationImg']");
		createSummationReportCtrl.click();
		new VoodooControl("table", "id", sugar().opportunities.moduleNamePlural).click();
		VoodooUtils.waitForReady();

		// In Related Modules, Select Assigned to user & User name in the Available Fields
		new VoodooControl("a", "css", ".ygtvchildren div:nth-of-type(2) .ygtvrow td:nth-child(3) a").click();
		new VoodooControl("tr", "id", "Users_user_name").click();
		VoodooControl selectUser = new VoodooControl("select", "css", "[title='select filter input']");
		selectUser.set(sugar().users.qaUser.get("userName"));
		VoodooUtils.waitForReady();
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		nextBtnCtrl.click();
		VoodooUtils.waitForReady();

		// In Define Group By select Opp Name & Forecast field 
		new VoodooControl("tr", "id", "Opportunities_name").click();	
		new VoodooControl("tr", "id", "Opportunities_commit_stage").click();

		// Click Next twice to move to the next page
		for(int i =1; i<=2; i++) {
			nextBtnCtrl.click();
			VoodooUtils.waitForReady();
		}
		new VoodooControl("input", "css", "#chart_options_div #nextButton").click();
		VoodooUtils.waitForReady();

		// Set report Name and click Save and Run Report
		new VoodooControl("input", "id", "save_report_as").set(testName);
		new VoodooControl("input", "id", "saveAndRunButton").click();
		VoodooUtils.waitForReady();

		// Verify that Database failure message doesn't exist on the page.
		// TODO: VOOD-1171
		new VoodooControl("div", "id", "main").assertContains(verifyMessage.get("message"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}