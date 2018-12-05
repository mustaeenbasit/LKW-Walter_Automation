package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Opportunities_NoRli_update extends SugarTest {
	OpportunityRecord myOpp;
	
	public void setup() throws Exception {
		sugar().login();
		sugar().accounts.api.create();
		myOpp = (OpportunityRecord)sugar().opportunities.create();
	}

	@Test
	public void Opportunities_NoRli_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("name", "International BM Inc.");
		newData.put("description", "THINK");

		// Edit the opportunity using the UI.
		myOpp.edit(newData);
		
		// Verify the opportunity was edited.
		myOpp.verify(newData);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
} 