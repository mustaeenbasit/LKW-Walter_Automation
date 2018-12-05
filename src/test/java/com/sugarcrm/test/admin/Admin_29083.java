package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_29083 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that text appearing during enabling "only Opportunity" mode is correct
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_29083_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);
		
		// Navigate to Admin > Opportunities
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("opportunityManagement").click();
		VoodooUtils.focusDefault();
		
		// Click the "Opportunities and Revenue Line Items" radio button to switch to Opps only mode
		sugar().admin.oppViewSettings.getControl("oppView").click();

		// Assert sales stage text
		// TODO: VOOD-2082
		new VoodooControl("label", "css", "[data-name='opps_closedate_rollup']").assertContains(customFS.get("closedateRollup"), true);
		new VoodooControl("label", "css", "#sales-stage-text label").assertContains(customFS.get("salesStage"), true);

		VoodooControl text1 = new VoodooControl("p", "css", "#sales-stage-text p:nth-child(2)");
		text1.assertContains(customFS.get("line1"), true);
		text1.assertContains(customFS.get("line2"), true);
		
		VoodooControl text2 = new VoodooControl("p", "css", "#sales-stage-text p:nth-child(3)");
		text2.assertContains(customFS.get("line3"), true);
		text2.assertContains(customFS.get("line4"), true);
		
		VoodooControl text3 = new VoodooControl("p", "css", "#sales-stage-text p:nth-child(4)");
		text3.assertContains(customFS.get("line5"), true);
		text3.assertContains(customFS.get("line6"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}