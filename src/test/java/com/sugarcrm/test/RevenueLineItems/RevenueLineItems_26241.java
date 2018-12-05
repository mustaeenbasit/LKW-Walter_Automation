package com.sugarcrm.test.RevenueLineItems;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;

import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_26241 extends SugarTest {
	OpportunityRecord myOpp;
	RevLineItemRecord myRLI; 
	FieldSet myTestData;

	public void setup() throws Exception {
		myTestData = testData.get("RevenueLineItems_26241").get(0);
		
		myOpp = (OpportunityRecord)sugar().opportunities.api.create();
		myRLI = (RevLineItemRecord)sugar().revLineItems.api.create();

		sugar().login();
	}

	/** 
	 * TC 26241: Verify that editing of RLI record can be canceled in RLI sub-panel of Opportunity record view
	 * 
	 *  @throws Exception
	 */	
	@Test
	public void RevenueLineItems_26241_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Link RLI to Opportunity
		FieldSet fs =  new FieldSet();
		fs.put("relOpportunityName", myOpp.getRecordIdentifier());
		myRLI.edit(fs);

		// Go to Opportunity Record View
		myOpp.navToRecord();
		
		StandardSubpanel rliSubpanel = sugar().opportunities.recordView.subpanels.get("RevenueLineItems");
		rliSubpanel.expandSubpanel();
		rliSubpanel.editRecord(1);
		
		// Update fields inside sub-panel
		// TODO: Need inline edit on sub-panel support. See VOOD-667 (https://sugarcrm.atlassian.net/browse/VOOD-687)
		new VoodooControl("input","css",".fld_likely_case.edit input[name=likely_case]").set(myTestData.get("likelyCase"));
		new VoodooControl("input","css",".fld_best_case.edit input[name=best_case]").set(myTestData.get("bestCase"));
		new VoodooControl("input","css",".fld_worst_case.edit input[name=worst_case]").set(myTestData.get("worstCase"));
		new VoodooControl("input","css",".fld_date_closed.edit input").set(myTestData.get("date_closed"));
		
		// Cancel changes 
		rliSubpanel.cancelAction(1);
		
		// Verify that opportunity record view is not updated 
		sugar().opportunities.recordView.getDetailField("oppAmount").assertContains(myTestData.get("likelyCase"), false);
		sugar().opportunities.recordView.getDetailField("bestCase").assertContains(myTestData.get("bestCase"), false);
		sugar().opportunities.recordView.getDetailField("worstCase").assertContains(myTestData.get("worstCase"), false);
		sugar().opportunities.recordView.getDetailField("date_closed").assertContains(myTestData.get("date_closed"), false);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
