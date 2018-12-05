package com.sugarcrm.test.RevenueLineItems;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.views.Alert;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class RevenueLineItems_25045 extends SugarTest {
	public void setup() throws Exception {
		// Create two Opportunity with different name
		OpportunityRecord myOpp1 = (OpportunityRecord) sugar().opportunities.api.create();
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		OpportunityRecord myOpp2 = (OpportunityRecord) sugar().opportunities.api.create(fs);

		// Create multiple Rli records
		DataSource multipleRLI = testData.get(testName+"_rli");
		sugar().revLineItems.api.create(multipleRLI);
		sugar().login();

		// TODO: VOOD-444- Once resolved, opp relation should call via API in RLI
		// Make sure there are RLI records that donot have the same Opportunity records
		sugar().revLineItems.navToListView();
		for(int i = 1; i <=multipleRLI.size(); i++) {
			sugar().revLineItems.listView.editRecord(i);
			VoodooSelect relOppName = (VoodooSelect)sugar().revLineItems.listView.getEditField(i, "relOpportunityName");
			if(i == 1)
				relOppName.set(myOpp1.getRecordIdentifier());
			else 
				relOppName.set(myOpp2.getRecordIdentifier());

			sugar().revLineItems.listView.saveRecord(i);
		}
	}	

	/**
	 * Verify that different Opp in RLI records are not allowed to merge
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_25045_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Select RLI records
		sugar().revLineItems.listView.toggleSelectAll();

		// Open action drop-down
		sugar().revLineItems.listView.openActionDropdown();

		// Click to 'Merge' two RLI with different Opportunity
		// TODO: VOOD-681
		new VoodooControl("a", "css", ".fld_merge_button.list a").click();
		VoodooUtils.waitForReady();

		// Verify warning alert message
		Alert warning = sugar().alerts.getWarning();
		warning.assertVisible(true);
		warning.assertEquals(testData.get(testName).get(0).get("warning_message"), true);
		warning.closeAlert();

		// Verify "Records are not merged, and they remain in list view."
		Assert.assertTrue("Records are merged as one record.", sugar().revLineItems.listView.countRows() == 2);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}