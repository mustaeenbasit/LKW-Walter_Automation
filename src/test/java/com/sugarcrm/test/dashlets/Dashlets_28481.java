package com.sugarcrm.test.dashlets;

import org.joda.time.DateTime;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_28481 extends SugarTest {
	FieldSet userAndRliData = new FieldSet();
	FieldSet multiPurposeFS = new FieldSet();

	public void setup() throws Exception {
		userAndRliData = testData.get(testName).get(0);
		sugar().accounts.api.create();
		DateTime today = new DateTime();
		String dateToday = today.toString("MM/dd/yyyy");

		// Login to Sugar
		sugar().login();

		// Opportunity record is created linked the account and a RLI the amount of 1234.567890 
		multiPurposeFS.put("rli_likely", userAndRliData.get("rli_likely"));
		multiPurposeFS.put("rli_expected_closed_date", dateToday);
		sugar().opportunities.create(multiPurposeFS);
		multiPurposeFS.clear();
	}

	/**
	 * Verify that tooltip in chart dashlets displays decimals based on user preference 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_28481_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to profile and edit and change the significant digits dropdown and save (ex: 4)
		multiPurposeFS.put("advanced_significant_digits", userAndRliData.get("updatedSignificantDigits"));
		sugar().users.setPrefs(multiPurposeFS);
		multiPurposeFS.clear();

		// Go to Dashboard
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);

		// In the Pipeline chart hover your mouse over any segment so tooltip comes up
		// TODO: VOOD-960
		new VoodooControl("text", "css", "div[data-voodoo-name='forecast-pipeline'] .nv-groups text").hover();

		// Verify that the tool tip show amounts to respect user preference setting (ex: 4 decimals)
		// TODO: VOOD-1292
		VoodooControl toolTipValueCtrl = new VoodooControl("div", "css", ".tooltip-inner");
		toolTipValueCtrl.assertContains(userAndRliData.get("tooltipValue"), true);

		// In Top 10 Sales chart hover your mouse over any segment so tooltip comes up
		// TODO: VOOD-960
		VoodooControl topSalesCtrl = new VoodooControl("g", "css", "div[data-voodoo-name='bubblechart'] .nv-groups");
		topSalesCtrl.scrollIntoView();
		topSalesCtrl.hover();

		// Verify that the tool tip show amounts to respect user preference setting (ex: 4 decimals)
		toolTipValueCtrl.assertContains(userAndRliData.get("tooltipValue"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}