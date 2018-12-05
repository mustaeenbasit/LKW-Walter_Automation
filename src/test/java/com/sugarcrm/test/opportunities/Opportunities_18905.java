package com.sugarcrm.test.opportunities;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class Opportunities_18905 extends SugarTest {
	AccountRecord acc1;
	OpportunityRecord opp1;
	RevLineItemRecord rli1,rli2;
	FieldSet rliRelate = new FieldSet();
	FieldSet fs = new FieldSet();
	List<String> statuses = Arrays.asList("Closed Won", "Closed Lost");

	public void setup() throws Exception {
		sugar().login();
		sugar().accounts.api.create();
		opp1 = (OpportunityRecord)sugar().opportunities.create();
		rli1 = (RevLineItemRecord)sugar().revLineItems.api.create(testData.get(testName).get(0));
		rli2 = (RevLineItemRecord)sugar().revLineItems.api.create(testData.get(testName).get(1));
		rliRelate.put("relOpportunityName", testData.get(testName).get(0).get("relOpportunityName"));
	}

	/**
	 * Test Case 18905: ENT/ULT: Verify Opportunity cannot be deleted in the record view if sales stage of one or more RLIs is closed won
	 * Test Case 18906: ENT/ULT: Verify Opportunity cannot be deleted in the list view if sales stage of one or more RLIs is closed won/lost
	 */
	@Ignore("VOOD-1394 - Opportunity field 'status' not updating from Revenue Line Item")
	@Test
	public void Opportunities_18905_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		//ToDo VOOD-718
		rli1.edit(rliRelate);
		rli2.edit(rliRelate);

		for(String status : statuses){
			fs.clear();
			fs.put("salesStage", status);
			rli2.edit(fs);

			sugar().opportunities.navToListView();
			sugar().opportunities.listView.setSearchString(opp1.getRecordIdentifier());
			sugar().opportunities.listView.openRowActionDropdown(1);
			Assert.assertTrue("List View: Delete button should be disabled for Opportunity with Closed Won|Lost RLI."
					,sugar().opportunities.listView.getControl(String.format("delete%02d", 1)).getAttribute("class").contains("disabled"));

			sugar().opportunities.listView.checkRecord(1);
			sugar().opportunities.listView.openActionDropdown();
			sugar().opportunities.listView.delete();
			//ToDo VOOD-665
			Assert.assertTrue("Mass Update: Delete button shouldn't work with Closed Won|Lost RLI. Warning alert should appear."
					,new VoodooControl("div", "css", "div[id=alerts] div.alert-warning").getText()
					.equalsIgnoreCase("Warning One or more of the selected records contains closed revenue line items and cannot be deleted."));

			sugar().opportunities.listView.clickRecord(1);
			sugar().opportunities.recordView.openPrimaryButtonDropdown();
			Assert.assertTrue("Record View: Delete button should be disabled for Opportunity with Closed Won|Lost RLI."
					,sugar().opportunities.recordView.getControl("deleteButton").getAttribute("class").contains("disabled"));
		}

		//Change the associated RLI's sales stage from close won/lost to any other sales stage
		//Delete action is now available in opportunity list view single record actions and mass update actions
		fs.clear();
		fs.put("salesStage", "Prospecting");
		rli2.edit(fs);

		sugar().opportunities.recordView.openPrimaryButtonDropdown();
		Assert.assertFalse("Record View: Delete button shouldn't be disabled."
				, sugar().opportunities.recordView.getControl("deleteButton").getAttribute("class").contains("disabled"));

		sugar().opportunities.navToListView();
		sugar().opportunities.listView.setSearchString(opp1.getRecordIdentifier());
		sugar().opportunities.listView.openRowActionDropdown(1);
		Assert.assertFalse("List View: Delete button shouldn't be disabled."
				, sugar().opportunities.listView.getControl(String.format("delete%02d", 1)).getAttribute("class").contains("disabled"));

		sugar().opportunities.listView.checkRecord(1);
		sugar().opportunities.listView.openActionDropdown();
		sugar().opportunities.listView.delete();
		sugar().alerts.getAlert().confirmAlert();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}