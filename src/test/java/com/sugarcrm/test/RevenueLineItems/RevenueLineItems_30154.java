package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_30154 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().opportunities.api.create();
		sugar().login();
		
		// TODO: VOOD-444
		sugar().revLineItems.create();
	}

	/**
	 * Verify "Amount" field should be edited with out any errors when values are entered comma separated ","
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_30154_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customData = testData.get(testName).get(0);
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.getEditField("likelyCase").set(customData.get("editViewLikelyValue"));
		sugar().revLineItems.recordView.save();
		
		// Verifying Amount field for likely is edited when values contains comma.
		sugar().revLineItems.recordView.getDetailField("likelyCase").assertEquals(customData.get("recordViewLikelyValue"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}