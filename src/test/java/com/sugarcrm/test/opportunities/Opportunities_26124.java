package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_26124 extends SugarTest {
	FieldSet opportunitiesRecord;
	
	public void setup() throws Exception {
		opportunitiesRecord = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();
		
		// TODO: VOOD-444 Support creating relationships via API
		sugar().opportunities.create();
	}

	/**
	 * Verify that the in line edit works for Opportunities List View
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_26124_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Inline Edit the record and save
		sugar().opportunities.listView.updateRecord(1, opportunitiesRecord);
		
		// Verify Record updated
		sugar().opportunities.listView.verifyField(1, "name",	opportunitiesRecord.get("name"));
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}