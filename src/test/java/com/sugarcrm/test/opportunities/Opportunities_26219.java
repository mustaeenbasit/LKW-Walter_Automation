package com.sugarcrm.test.opportunities;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_26219 extends SugarTest {
	OpportunityRecord myOpp;
	RevLineItemRecord myRLI; 
	FieldSet myTestData;

	public void setup() throws Exception {
		myTestData = testData.get(testName).get(0);
		
		myOpp = (OpportunityRecord)sugar().opportunities.api.create();
		myRLI = (RevLineItemRecord)sugar().revLineItems.api.create();
		
		sugar().login();
	}

	/** 
	 * Verify the opportunity is updated after related RLI is edited in the RLI sub-panel of opportunity record view
	 * 
	 *  @throws Exception
	 */
	@Test
	public void Opportunities_26219_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Link RLI to Opportunity
		FieldSet fs =  new FieldSet();
		fs.put("relOpportunityName", myOpp.getRecordIdentifier());
		myRLI.edit(fs);

		myOpp.navToRecord();
		StandardSubpanel rliSubpanel = sugar().opportunities.recordView.subpanels.get("RevenueLineItems");
		rliSubpanel.expandSubpanel();
		rliSubpanel.editRecord(1);
		
		// Update fields inside sub-panel
		// TODO: Need inline edit on sub-panel support. See VOOD-667 (https://sugarcrm.atlassian.net/browse/VOOD-687)
		new VoodooControl("input","css",".fld_likely_case.edit input[name=likely_case]").set(myTestData.get("likelyCase"));
		new VoodooControl("input","css",".fld_best_case.edit input[name=best_case]").set(myTestData.get("bestCase"));
		new VoodooControl("input","css",".fld_worst_case.edit input[name=worst_case]").set(myTestData.get("worstCase"));
		
		// TODO: VOOD-1110. Jenkins is not able to store date from date picker.
		// new VoodooControl("input","css",".fld_date_closed.edit input").set(myTestData.get("date_closed"));
		
		// Save changes 
		rliSubpanel.saveAction(1);
		sugar().alerts.waitForLoadingExpiration();
		
		// TODO: remove this line when SFA-2957 is fixed in 7.6
		VoodooUtils.refresh(); // Reload the page with updated values
		
		// Verify that opportunity record view is updated based on the changes of RLI record
		sugar().opportunities.recordView.getDetailField("oppAmount").assertContains(myTestData.get("likelyCase"), true);
		sugar().opportunities.recordView.getDetailField("bestCase").assertContains(myTestData.get("bestCase"), true);
		sugar().opportunities.recordView.getDetailField("worstCase").assertContains(myTestData.get("worstCase"), true);
		// TODO: VOOD-1110. Jenkins is not able to store date from date picker.
		// sugar().opportunities.recordView.getDetailField("date_closed").assertContains(myTestData.get("date_closed"), true);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}