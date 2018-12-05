package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21567 extends SugarTest {
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		// Creating an RLI with the close date and custom likely value
		customData = testData.get(testName).get(0);
		FieldSet rliData = new FieldSet();
		rliData.put("date_closed", VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		rliData.put("likelyCase", customData.get("likelyAmount"));
		sugar().revLineItems.api.create(rliData);

		// Login as admin
		sugar().login();
	}

	/**
	 * Verify the precision of Dashboard charts are consistent with
	 * "Currency Significant Digits" setting in My Account
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21567_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify the amount in Pipeline Dashlet
		// TODO: VOOD-1376
		VoodooControl pipeLineAmt = new VoodooControl("text", "css", "g.nv-group.nv-series-0 text.nv-value");
		pipeLineAmt.assertEquals(customData.get("dashletAmount"), true);

		// Verify the amount in Pipeline Dashlet tooltip
		pipeLineAmt.hover();
		new VoodooControl("div", "css", ".tooltip-inner").assertContains(customData.get("tooltipAmount"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}