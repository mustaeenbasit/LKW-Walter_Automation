package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Opportunities_NoRli_create extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		sugar().login();
		myAccount = (AccountRecord) sugar().accounts.api.create();
	}

	@Test
	public void Opportunities_NoRli_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		OpportunityRecord myOpp = (OpportunityRecord) sugar().opportunities.create();

		// TODO: VOOD-1402 Need to support verification of formatted currency fields
		myOpp.put("likelyCase", "$10,000.00");
		myOpp.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
