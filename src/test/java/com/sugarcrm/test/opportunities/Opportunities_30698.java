package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_30698 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();	
	}

	/**
	 * Verify that status field should update on creating an Opportunity with RLI 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_30698_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Creating a OPP + RLI record with Closed Won stage
		FieldSet oppData = testData.get(testName).get(0);
		FieldSet fs = new FieldSet();
		fs.put("rli_stage", oppData.get("rli_stage"));
		sugar().opportunities.create(fs);

		// Asserting the status of Opportunity is Closed Won in Record View
		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.getDetailField("status").assertEquals(oppData.get("status"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}