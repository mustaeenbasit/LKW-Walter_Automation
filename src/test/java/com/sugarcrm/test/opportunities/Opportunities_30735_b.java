package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Opportunities_30735_b extends SugarTest {
	DataSource opportunityData = new DataSource();
	
	public void setup() throws Exception {
		opportunityData = testData.get(testName);
		VoodooSelect oppSalesStage = new VoodooSelect("span", "css", ".fld_sales_stage.edit");
		AccountRecord accountRecord = (AccountRecord) sugar().accounts.api.create();
		sugar().login();
		for (int i = 0; i < opportunityData.size(); i++) {
			sugar().navbar.selectMenuItem(sugar().opportunities, "createOpportunity");
			sugar().opportunities.createDrawer.getEditField("name").set(opportunityData.get(i).get("name"));
			sugar().opportunities.createDrawer.getEditField("relAccountName").set(accountRecord.getRecordIdentifier());
			sugar().opportunities.createDrawer.getEditField("likelyCase").set(sugar().opportunities.getDefaultData().get("likelyCase"));
			sugar().opportunities.createDrawer.getEditField("date_closed").set(sugar().opportunities.getDefaultData().get("date_closed"));
			
			// TODO: VOOD-1359
			oppSalesStage.set(opportunityData.get(i).get("sales_stage"));
			sugar().opportunities.createDrawer.save();
		}
	}

	/**
	 * Verify that Opportunity with sales stage "Closed Won" & "Closed Lost" should not get deleted when set to opportunity only mode.
	 * @throws Exception
	 */
	@Test
	public void Opportunities_30735_b_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Verification for opportunity list view
		sugar().opportunities.listView.toggleSelectAll();
		sugar().opportunities.listView.openActionDropdown();
		sugar().opportunities.listView.delete();
		
		// Verifying warning message appeared for sales stage Closed Won or Closed Lost after clicking on delete.
		sugar().alerts.getWarning().assertEquals(opportunityData.get(0).get("warningMessage"), true);
		sugar().alerts.getWarning().closeAlert();
		
		// Verifying Check-box of all records with sales stage Closed Won or Closed Lost get unchecked after click on delete
		sugar().opportunities.listView.getControl("checkbox01").assertChecked(false);
		sugar().opportunities.listView.getControl("checkbox02").assertChecked(false);
		sugar().opportunities.listView.getControl("checkbox03").assertChecked(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}