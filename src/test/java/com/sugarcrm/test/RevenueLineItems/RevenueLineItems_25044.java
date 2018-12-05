package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_25044 extends SugarTest {
	
	public void setup() throws Exception {
		DataSource multipleRliFS = testData.get(testName+"_rli");
		
		// Create two Opportunity with different name
		OpportunityRecord myOpp1 = (OpportunityRecord) sugar().opportunities.api.create();
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		OpportunityRecord myOpp2 = (OpportunityRecord) sugar().opportunities.api.create(fs);
		
		// Create multiple Rli records
		sugar().revLineItems.api.create(multipleRliFS);
		sugar().login();
		
		// Make sure there are RLI records that have the same Opportunity records. Also there are RLI records that don't have the same Opportunity records.
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		for(int i = 0; i < multipleRliFS.size(); i++) {
			sugar().revLineItems.recordView.edit();
			if(i == 0)
				sugar().revLineItems.recordView.getEditField("relOpportunityName").set(myOpp1.getRecordIdentifier());
			else 
				sugar().revLineItems.recordView.getEditField("relOpportunityName").set(myOpp2.getRecordIdentifier());
			sugar().revLineItems.recordView.save();
			sugar().revLineItems.recordView.gotoNextRecord();
		}
	}	

	/**
	 * Verify that same Opportunity in RLI record is required when to merge RLI records
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_25044_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customFS = testData.get(testName).get(0);
		
		// Go to RLI module listView
		sugar().revLineItems.navToListView();
		
		// VOOD-1828 Records created via api call are displayed in random order.
		sugar().revLineItems.listView.sortBy("headerName", true);
		
		// Select top two records
		sugar().revLineItems.listView.checkRecord(1);
		sugar().revLineItems.listView.checkRecord(2);
		
		// Open action drop-down
		sugar().revLineItems.listView.openActionDropdown();

		// Click to 'Merge' two RLI with same Opportunity
		// TODO: VOOD-681
		new VoodooControl("a", "css", ".fld_merge_button.list").click();
		VoodooUtils.waitForReady();
		
		// Without changing anything in Primary record, just click on Save.
		new VoodooControl("a", "css", ".fld_save_button.merge-duplicates-headerpane a").click();
		
		// Verify that a yellow color message bar pop-up, saying: Warning This action will delete following record(s) 
		sugar().alerts.getWarning().assertContains(customFS.get("warningMsg"), true);
		
		// Confirm Warning alert message
		sugar().alerts.getWarning().confirmAlert();
		VoodooUtils.voodoo.log.info(sugar().alerts.getSuccess().getText() + " <<<<");
		
		// Verify A green color message bar, displays message like: Success 2 records were merged successfully. 1 related records were added.
		sugar().alerts.getSuccess().assertContains(customFS.get("successMsg"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}