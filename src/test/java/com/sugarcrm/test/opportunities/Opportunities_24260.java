package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Opportunities_24260 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Create Opportunity_Verify that opportunity can be created using "Create Opportunity" navigation shortcuts
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24260_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Create Opportunity" link in navigation shortcuts
		sugar().navbar.selectMenuItem(sugar().opportunities, "createOpportunity");

		// Fill all the mandatory fields and save
		sugar().opportunities.createDrawer.getEditField("name").set(testName);
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.getEditField("date_closed").set(sugar().opportunities.getDefaultData().get("date_closed")+'\uE004');
		sugar().opportunities.createDrawer.getEditField("likelyCase").set(sugar().opportunities.getDefaultData().get("likelyCase"));
		sugar().opportunities.createDrawer.save();

		// Click on the Opportunity record created from listview
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);

		// Verify The information of the created opportunity is displayed in "Opportunity" detail view as entered
		sugar().opportunities.recordView.getDetailField("name").assertEquals(testName, true);
		sugar().opportunities.recordView.getDetailField("relAccountName").assertEquals(sugar().accounts.getDefaultData().get("name"), true);

		// Not fill all the mandatory fields and click save button
		sugar().navbar.selectMenuItem(sugar().opportunities, "createOpportunity");
		sugar().opportunities.createDrawer.save();

		// Verify warning message is displayed and record cannot be saved
		FieldSet warningMsg = testData.get(testName).get(0);
		sugar().alerts.getError().assertContains(warningMsg.get("errorMessage"), true);

		// Click on Cancel button
		sugar().accounts.createDrawer.cancel();

		// Verify that second record is not saved i.e. only 1 record exixt in listview
		sugar().opportunities.navToListView();
		int recordCount = sugar().opportunities.listView.countRows();
		Assert.assertTrue("Row count in listview is not equal to 1", recordCount == 1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}