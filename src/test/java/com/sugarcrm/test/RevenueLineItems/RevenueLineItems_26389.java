package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_26389 extends SugarTest {
	FieldSet massUpdateData;
	FieldSet myTestData;
	OpportunityRecord myOpp;

	public void setup() throws Exception {
		massUpdateData = testData.get(testName).get(0);
		sugar().login();
		myOpp = (OpportunityRecord)sugar().opportunities.api.create();
		sugar().revLineItems.api.create();
		sugar().revLineItems.api.create();
	}

	/**
	 * TC 26389: Verify Mass Update of Revenue Line Items
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26389_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.checkRecord(1);
		sugar().revLineItems.listView.checkRecord(2);
		sugar().revLineItems.massUpdate.performMassUpdate(massUpdateData);
		sugar().alerts.waitForLoadingExpiration();

		// Verify first RLI record 		
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.showMore();
		sugar().revLineItems.recordView.getDetailField("leadSource").assertEquals(massUpdateData.get("Lead Source"), true);
		sugar().revLineItems.recordView.getDetailField("salesStage").assertEquals(massUpdateData.get("Sales Stage"), true);
		sugar().revLineItems.recordView.getDetailField("relOpportunityName").assertEquals(massUpdateData.get("Opportunity Name"), true);

		// Verify second RLI record 		
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(2);
		sugar().revLineItems.recordView.showMore();
		sugar().revLineItems.recordView.getDetailField("leadSource").assertEquals(massUpdateData.get("Lead Source"), true);
		sugar().revLineItems.recordView.getDetailField("salesStage").assertEquals(massUpdateData.get("Sales Stage"), true);
		sugar().revLineItems.recordView.getDetailField("relOpportunityName").assertEquals(massUpdateData.get("Opportunity Name"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}