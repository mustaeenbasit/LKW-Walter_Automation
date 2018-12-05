package com.sugarcrm.test.opportunities;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;
import java.text.NumberFormat;
import java.util.Locale;

public class Opportunities_17870 extends SugarTest {
	AccountRecord acc1;
	OpportunityRecord opp1;
	RevLineItemRecord rli1,rli2;
	DataSource rliDS;
	FieldSet rliUpdate = new FieldSet();
	FieldSet oppRelate = new FieldSet();
	FieldSet verify1 = new FieldSet();
	FieldSet verify2 = new FieldSet();
	NumberFormat form = NumberFormat.getCurrencyInstance(Locale.US);

	public void setup() throws Exception {
		sugar().login();
		
		rliDS = testData.get(testName);
		
		opp1 = (OpportunityRecord)sugar().opportunities.api.create();
		rli1 = (RevLineItemRecord)sugar().revLineItems.api.create();
		rli2 = (RevLineItemRecord)sugar().revLineItems.api.create();
		
		// Compile Verification field set 1
		Integer worstCase = Integer.valueOf(rliDS.get(0).get("worstCase")) + Integer.valueOf(rliDS.get(1).get("worstCase"));
		Integer oppAmount = Integer.valueOf(rliDS.get(0).get("likelyCase")) + Integer.valueOf(rliDS.get(1).get("likelyCase"));
		Integer bestCase = Integer.valueOf(rliDS.get(0).get("bestCase")) + Integer.valueOf(rliDS.get(1).get("bestCase"));
		verify1.put("worstCase", form.format(worstCase));
		verify1.put("oppAmount", form.format(oppAmount));
		verify1.put("bestCase", form.format(bestCase));
		// verify1.put("date_closed",rliDS.get(1).get("date_closed") ); // Uncomment after SC-2765 is fixed

		// Compile Verification field set 0  
		verify2.put("worstCase", form.format(Integer.valueOf(rliDS.get(0).get("worstCase"))));
		verify2.put("oppAmount", form.format(Integer.valueOf(rliDS.get(0).get("likelyCase"))));
		verify2.put("bestCase", form.format(Integer.valueOf(rliDS.get(0).get("bestCase"))));
		// verify2.put("date_closed",rliDS.get(0).get("date_closed") ); // Uncomment after SC-2765 is fixed
		
		
		// Update first RLI record and relate it to opportunity
		rliDS.get(0).put("relOpportunityName", opp1.getRecordIdentifier());
		rliDS.get(0).remove("date_closed"); // TODO: Remove this line after SC-2765 is fixed 
		rli1.edit(rliDS.get(0));
		
		// Update second RLI record and relate it to Opportunity 
		rliDS.get(1).put("relOpportunityName", opp1.getRecordIdentifier());
		rliDS.get(1).remove("date_closed"); // TODO: Remove this line after SC-2765 is fixed
		rli2.edit(rliDS.get(1));
		
		// Verify that Opportunity values are correct when 2 RLI records linked to it 
		opp1.verify(verify1);
	}

	/**
	 * Test Case 17870: Verify that an opportunity amount and expected close date are updated after removing revenue line item linked to the above opportunity
	 * @throws Exception 
	 */
	@Test
	public void Opportunities_17870_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		rli2.delete();
		opp1.verify(verify2);
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}