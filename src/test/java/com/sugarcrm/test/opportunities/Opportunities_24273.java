package com.sugarcrm.test.opportunities;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24273 extends SugarTest {
	DataSource quotesData,lineItemsData;
	ArrayList<Record> quotesRecords;
	StandardSubpanel quotesSubpanel;
	
	public void setup() throws Exception {
		quotesData = testData.get(testName);
		lineItemsData = testData.get(testName+"_quoteItems");
	
		sugar().opportunities.api.create();
		sugar().accounts.api.create();
		quotesRecords = sugar().quotes.api.create(quotesData);
		sugar().login();
		
		// Edit Quote records to add Line Items
		// TODO: VOOD-930
		for (int i = 0; i < lineItemsData.size(); i++) {
			quotesRecords.get(i).navToRecord();
			sugar().quotes.detailView.edit();
			VoodooUtils.focusFrame("bwc-frame");
			sugar().quotes.editView.getEditField("billingAccountName").set(sugar().quotes.getDefaultData().get("billingAccountName"));
			new VoodooControl("input", "css", "input#add_group").click();
			new VoodooControl("input", "css", "input#bundle_name_group_0").set(lineItemsData.get(i).get("groupName"));
			new VoodooControl("input", "css", "input[name='Add Row']").click();
			new VoodooControl("input", "css", "input#quantity_1").set(lineItemsData.get(i).get("quantity"));
			new VoodooControl("input", "css", "input#name_1").set(lineItemsData.get(i).get("name"));
			new VoodooControl("input", "css", "input#discount_price_1").set(lineItemsData.get(i).get("unitPrice"));
			new VoodooControl("input", "css", "input#discount_amount_1").set(lineItemsData.get(i).get("discountAmount"));
			VoodooUtils.focusDefault();
			sugar().quotes.editView.save();
		}
		
		// Navigate to Opportunities record view and relate 3 Quotes records to it
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		quotesSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().quotes.moduleNamePlural);
		quotesSubpanel.scrollIntoViewIfNeeded(false);
		quotesSubpanel.linkExistingRecords(quotesRecords);
	}

	/**
	 * Detail View Opportunity Verify that the list in the sub-panel Quotes of "Opportunity" detail view can be sorted.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24273_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click on "Name" column to sort and Verify list of sub-panel is sorted by the selected column
		// TODO: VOOD-2105
		new VoodooControl("span", "css", ".layout_Quotes .sorting.orderByname span").click();
		VoodooUtils.waitForReady();
		quotesSubpanel.verify(1, quotesData.get(0), true);
		quotesSubpanel.verify(2, quotesData.get(1), true);
		quotesSubpanel.verify(3, quotesData.get(2), true);
		
		// Click on "Valid Until" column to sort and Verify list of sub-panel is sorted by the selected column
		// TODO: VOOD-2105
		new VoodooControl("span", "css", ".layout_Quotes .sorting.orderBydate_quote_expected_closed span").click();
		VoodooUtils.waitForReady();
		quotesSubpanel.verify(1, quotesData.get(0), true);
		quotesSubpanel.verify(2, quotesData.get(1), true);
		quotesSubpanel.verify(3, quotesData.get(2), true);
		
		// Click on "Converted Amount" column to sort and Verify list of sub-panel is sorted by the selected column
		// TODO: VOOD-2105
		new VoodooControl("span", "css", ".layout_Quotes .sorting.orderBytotal_usdollar span").click();
		VoodooUtils.waitForReady();
		quotesSubpanel.verify(1, quotesData.get(0), true);
		quotesSubpanel.verify(2, quotesData.get(1), true);
		quotesSubpanel.verify(3, quotesData.get(2), true);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}