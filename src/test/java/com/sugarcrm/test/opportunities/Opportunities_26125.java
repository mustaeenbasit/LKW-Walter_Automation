package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_26125 extends SugarTest {
	FieldSet opportunitiesRecord;

	public void setup() throws Exception {
		opportunitiesRecord = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();
		
		// TODO: VOOD-444 Support creating relationships via API
		sugar().opportunities.create();
	}

	/**
	 * Verify the user can Cancel the inline editing from the record level
	 * action drop down on Opportunity List View
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_26125_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to the Opportunities List View and 'in line' Edit the Opportunity just created
		sugar().opportunities.listView.editRecord(1);
		sugar().opportunities.listView.setEditFields(1, opportunitiesRecord);
		sugar().opportunities.listView.cancelRecord(1);
		
		// Assert the original default name is present
		sugar().opportunities.listView.verifyField(1, "name",	sugar().opportunities.getDefaultData().get("name"));
	}

	public void cleanup() throws Exception {}
}