package com.sugarcrm.test.opportunities;

import java.text.DecimalFormat;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Opportunities_28298 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
		sugar().opportunities.create();
	}

	/**
	 * Verify that not calculated fields are copied when copy opportunity record
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28298_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// switch to opp view via API
		sugar().admin.api.switchToOpportunitiesView();
		
		// reload listview after opp settings
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		
		// Click on Copy option in Action drop down
		sugar().opportunities.recordView.copy();

		// Verify All not calculated fields are copied including Likely, Best, Worst and Expected Close Date fields.
		// TODO: VOOD-1402
		String toVerify = sugar().opportunities.getDefaultData().get("likelyCase");
		double amount = Double.parseDouble(toVerify);
		DecimalFormat formatter = new DecimalFormat("##,###.00");
		String toVerify1 = formatter.format(amount);
		sugar().opportunities.recordView.getEditField("likelyCase").assertContains(toVerify1, true);
		sugar().opportunities.recordView.getEditField("bestCase").assertContains(toVerify1, true);
		sugar().opportunities.recordView.getEditField("worstCase").assertContains(toVerify1, true);
		sugar().opportunities.recordView.getEditField("date_closed").assertContains(sugar().opportunities.getDefaultData().get("date_closed"), true);
		sugar().opportunities.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}