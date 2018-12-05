package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class RevenueLineItems_18412 extends SugarTest {
	AccountRecord myAcc;
	OpportunityRecord myOpp;
	RevLineItemRecord myRLI1, myRLI2, myRLI3;
	DataSource testDS;

	public void setup() throws Exception {
		sugar().login();

		testDS = testData.get(testName);
		myAcc = (AccountRecord) sugar().accounts.api.create();
		myOpp = (OpportunityRecord) sugar().opportunities.create();
		myRLI1 = (RevLineItemRecord) sugar().revLineItems.api.create();
		myRLI2 = (RevLineItemRecord) sugar().revLineItems.api.create();
		myRLI3 = (RevLineItemRecord) sugar().revLineItems.api.create();

		// Edit all RLIs - set different names, sales stage and add the related opportunity
		myRLI1.navToRecord();
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.createDrawer.getEditField("name").set(testDS.get(0).get("name"));
		sugar().revLineItems.createDrawer.getEditField("salesStage").set(testDS.get(0).get("salesStage"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(myOpp.getRecordIdentifier());
		sugar().revLineItems.recordView.save();
		sugar().alerts.waitForLoadingExpiration();

		myRLI2.navToRecord();
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.createDrawer.getEditField("name").set(testDS.get(1).get("name"));
		sugar().revLineItems.createDrawer.getEditField("salesStage").set(testDS.get(1).get("salesStage"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(myOpp.getRecordIdentifier());
		sugar().revLineItems.recordView.save();
		sugar().alerts.waitForLoadingExpiration();

		myRLI3.navToRecord();
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.createDrawer.getEditField("name").set(testDS.get(2).get("name"));
		sugar().revLineItems.createDrawer.getEditField("salesStage").set(testDS.get(2).get("salesStage"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(myOpp.getRecordIdentifier());
		sugar().revLineItems.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Test Case 18412: Verify closed status Revenue Line Item record is not deleted in list view
	 *
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_18412_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to RLI module listview and try to delete all RLI records
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.toggleSelectAll();
		sugar().revLineItems.listView.openActionDropdown();
		sugar().revLineItems.listView.delete();

		// Verify that warning message is displayed
		sugar().alerts.getWarning().assertContains(testDS.get(0).get("warningMessage"), true);
		sugar().alerts.getWarning().closeAlert();

		// Verify that no records were deleted
		sugar().revLineItems.listView.assertContains(testDS.get(0).get("name"), true);
		sugar().revLineItems.listView.assertContains(testDS.get(1).get("name"), true);
		sugar().revLineItems.listView.assertContains(testDS.get(2).get("name"), true);

		// Verify the status of listview checkboxes
		VoodooControl checkbox1 = new VoodooControl("input", "css", "tbody tr:nth-child(1) input[type='checkbox']");
		VoodooControl checkbox2 = new VoodooControl("input", "css", "tbody tr:nth-child(2) input[type='checkbox']");
		VoodooControl checkbox3 = new VoodooControl("input", "css", "tbody tr:nth-child(3) input[type='checkbox']");

		assertTrue("Should be checked!", (Boolean.parseBoolean(checkbox1.getAttribute("checked"))));
		assertFalse("Should be unchecked!", (Boolean.parseBoolean(checkbox2.getAttribute("checked"))));
		assertFalse("Should be unchecked!", (Boolean.parseBoolean(checkbox3.getAttribute("checked"))));
		// TODO: VOOD-1020 - verification of the checkboxes status should be changed when story is implemented

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}