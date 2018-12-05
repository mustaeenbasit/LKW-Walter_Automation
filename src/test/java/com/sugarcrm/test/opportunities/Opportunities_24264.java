package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24264 extends SugarTest {
	FieldSet oppData;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		oppData=testData.get(testName).get(0);

		sugar().login();
	}

	/**
	 * Test Case 24264: Create Opportunity_Verify that opportunity can be created using "Quick Create" menu
	 */
	@Test
	public void Opportunities_24264_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Open QuickCreate and click "Create Opportunity"
		sugar().navbar.quickCreateAction(sugar().opportunities.moduleNamePlural);
		
		// Set required fields
		sugar().opportunities.createDrawer.showMore();
		sugar().opportunities.createDrawer.setFields(oppData);

		// Click Save
		sugar().opportunities.createDrawer.save();

		// Verify that user is returned to Home module after opp is created
		sugar().dashboard.assertVisible(true);

		// Verify an opportunity record is created
		OpportunityRecord myOpportunity = new OpportunityRecord(oppData);
		myOpportunity.verify(oppData);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}