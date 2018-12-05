package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Opportunities_30735 extends SugarTest {
	DataSource opportunityData = new DataSource();
	
	public void setup() throws Exception {
		opportunityData = testData.get(testName);
		AccountRecord accountRecord = (AccountRecord) sugar().accounts.api.create();
		FieldSet customData = new FieldSet();
		sugar().login();
		for (int i = 0; i < opportunityData.size(); i++) {
			customData.put("name", opportunityData.get(i).get("name"));
			customData.put("relAccountName", accountRecord.getRecordIdentifier());
			customData.put("rli_name", opportunityData.get(i).get("rli_name"));
			customData.put("rli_expected_closed_date", opportunityData.get(i).get("rli_expected_closed_date"));
			customData.put("rli_likely", opportunityData.get(i).get("rli_likely"));
			customData.put("rli_stage", opportunityData.get(i).get("rli_stage"));
			sugar().opportunities.create(customData);
			customData.clear();
		}
	}

	/**
	 * Verify that Opportunity & RLI with sales stage "Closed Won" & "Closed Lost" should not get deleted
	 * @throws Exception
	 */
	@Test
	public void Opportunities_30735_execute() throws Exception {
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
		
		// Verification for revenueLineItem list view
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.toggleSelectAll();
		sugar().revLineItems.listView.openActionDropdown();
		sugar().revLineItems.listView.delete();
		
		// Verifying Check-box of all records with sales stage Closed Won or Closed Lost get unchecked after click on delete
		sugar().alerts.getWarning().assertEquals(opportunityData.get(0).get("warningMessage"), true);
		sugar().alerts.getWarning().closeAlert();
		sugar().revLineItems.listView.getControl("checkbox01").assertChecked(false);
		sugar().revLineItems.listView.getControl("checkbox02").assertChecked(false);
		sugar().revLineItems.listView.getControl("checkbox03").assertChecked(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}