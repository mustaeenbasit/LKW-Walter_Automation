package com.sugarcrm.test.opportunities;

import java.text.DecimalFormat;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Opportunities_28299 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
		// Opportunity Record
		sugar().opportunities.create();
	}

	/**
	 * Verify that calculated fields are not copied when copy opportunity record
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28299_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().opportunities.listView.clickRecord(1);
		
		// Click on Copy option in Action drop down
		sugar().opportunities.recordView.copy();

		// Verify that calculated fields Likely, BestCase, WorstCase are not copied when copy opportunity record
		String toVerify = sugar().opportunities.getDefaultData().get("likelyCase");
		double amount = Double.parseDouble(toVerify);
		DecimalFormat formatter = new DecimalFormat("##,###.00");
		String toVerify1 = formatter.format(amount);
		sugar().opportunities.recordView.getEditField("likelyCase").assertContains(toVerify1, false);
		sugar().opportunities.recordView.getEditField("bestCase").assertContains(toVerify1, false);
		sugar().opportunities.recordView.getEditField("worstCase").assertContains(toVerify1, false);
		sugar().opportunities.recordView.getEditField("date_closed").assertContains(sugar().opportunities.getDefaultData().get("date_closed"), false);
		sugar().opportunities.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}