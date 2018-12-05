package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;

public class RevenueLineItems_26868 extends SugarTest {
	OpportunityRecord myOpportunity;
	DataSource customData;
	
	public void setup() throws Exception {
		sugar().login();

		customData = testData.get(testName);
		
		sugar().accounts.api.create();
		
		myOpportunity = (OpportunityRecord) sugar().opportunities.create();
	}

	/**
	 * Verify that calculation among calculated RLI amount, likely, unit price is correct.
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26868_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();
		
		// Verify Unit price, likely, calculated RLI should be blank
		sugar().revLineItems.createDrawer.getEditField("likelyCase").assertContains("", true);
		sugar().revLineItems.createDrawer.getEditField("unitPrice").assertContains("", true);
		sugar().revLineItems.createDrawer.getEditField("calcRLIAmount").assertContains("", true);
		
		// Scenario 1
		// Enter a value in likely
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(customData.get(0).get("likelyCase"));
		sugar().revLineItems.createDrawer.getEditField("name").set(sugar().revLineItems.getDefaultData().get("name"));

		// Verify Unit price has the same value as likely.
		sugar().revLineItems.createDrawer.getEditField("unitPrice").click();
		sugar().revLineItems.createDrawer.getEditField("unitPrice").assertContains(customData.get(0).get("unitPrice"), true);
		sugar().revLineItems.createDrawer.cancel();
		
		// Scenario 2
		// Enter likely = 0.00
		sugar().revLineItems.listView.create();
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(customData.get(1).get("likelyCase"));
		sugar().revLineItems.createDrawer.getEditField("name").set(sugar().revLineItems.getDefaultData().get("name"));

		// Verify Unit price has the same value as likely.
		sugar().revLineItems.createDrawer.getEditField("unitPrice").click();
		sugar().revLineItems.createDrawer.getEditField("unitPrice").assertContains(customData.get(1).get("unitPrice"), true);
		
		// Now, enter likely = $100
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(customData.get(2).get("likelyCase"));
		sugar().revLineItems.createDrawer.getEditField("name").set(sugar().revLineItems.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		sugar().revLineItems.createDrawer.save();
		
		// Verify Unit price is still $0.00
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.getDetailField("unitPrice").assertContains(customData.get(2).get("unitPrice"), true);
		
		// Scenario 3
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();
		// Enter quantity 0, unit price = $500, Total discount = $100
		sugar().revLineItems.createDrawer.getEditField("quantity").set(customData.get(3).get("quantity"));
		sugar().revLineItems.createDrawer.getEditField("unitPrice").set(customData.get(3).get("unitPrice"));
		sugar().revLineItems.createDrawer.getEditField("discountPrice").set(customData.get(3).get("discountPrice"));
		// Enter other required field and save
		sugar().revLineItems.createDrawer.getEditField("name").set(sugar().revLineItems.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		sugar().revLineItems.createDrawer.save();
		
		// Verify that record can not save. 
		// Verify, Quantity field should give error like "The value of this field must be greater than 0"
		new VoodooControl("span", "css", "[data-original-title='"+customData.get(3).get("assert_error")+"']").assertExists(true);
		
		// Scenario 4
		// Enter quantity 3, unit price = $15.00, Total discount = $4.00
		sugar().revLineItems.createDrawer.getEditField("quantity").set(customData.get(4).get("quantity"));
		sugar().revLineItems.createDrawer.getEditField("unitPrice").set(customData.get(4).get("unitPrice"));
		sugar().revLineItems.createDrawer.getEditField("discountPrice").set(customData.get(4).get("discountPrice"));
		// Enter other required field and save
		sugar().revLineItems.createDrawer.getEditField("name").set(customData.get(4).get("name"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		sugar().revLineItems.createDrawer.save();
		
		// Verify, Calculated RLI = Likely and should be calculated correctly. (i.e. likely = quantity * unit price - total discount)
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.getDetailField("calcRLIAmount").assertContains(customData.get(4).get("calcRLIAmount"), true);
		sugar().revLineItems.recordView.getDetailField("likelyCase").assertContains(customData.get(4).get("likelyCase"), true);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
