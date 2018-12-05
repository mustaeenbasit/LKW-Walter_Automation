package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_26161 extends SugarTest {
	RevLineItemRecord myRevenueLineItem;

	public void setup() throws Exception {
		sugar().opportunities.api.create();
		sugar().login();
		myRevenueLineItem = (RevLineItemRecord)sugar().revLineItems.create();
	}

	/**
	 * Verify Navigation and display of the Revenue Line Items Preview pane
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26161_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet myTestData = testData.get(testName).get(0);
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.previewRecord(1);
		myRevenueLineItem.verifyPreview(myTestData);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}