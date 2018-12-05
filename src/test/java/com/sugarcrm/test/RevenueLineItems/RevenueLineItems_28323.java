package com.sugarcrm.test.RevenueLineItems;

import java.text.DecimalFormat;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_28323 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that Best and worst amounts are copied from likely if they are blank on save
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_28323_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().opportunities.create();
		sugar().opportunities.listView.clickRecord(1);
		String toVerify = sugar().revLineItems.getDefaultData().get("likelyCase");
		VoodooUtils.waitForReady();
		StandardSubpanel rli = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);

		// Setting format of the Amount field to assert
		double amount = Double.parseDouble(toVerify);
		DecimalFormat formatter = new DecimalFormat("##,###.00");
		String toVerify1 = formatter.format(amount);
		rli.scrollIntoViewIfNeeded(false);
		rli.expandSubpanel();
		// Setting the currency to match equality
		String currency = String.format("%s%s","$",toVerify1); 

		// Verify that "Best" & "Worst" Fields of RLI contains the same value as that of "Likely"
		rli.getDetailField(1, "worstCase").assertContains(currency,true);
		rli.getDetailField(1, "bestCase").assertContains(currency,true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}