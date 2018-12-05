package com.sugarcrm.test.RevenueLineItems;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_20161 extends SugarTest {
	OpportunityRecord myOpportunityRecord;

	public void setup() throws Exception {
		AccountRecord myAcc = (AccountRecord)sugar().accounts.api.create();
		myOpportunityRecord = (OpportunityRecord) sugar().opportunities.api.create();
		sugar().productCatalog.api.create();
		sugar().login();

		// Link account with opportunity
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.edit();
		sugar().opportunities.recordView.getEditField("relAccountName").set(myAcc.getRecordIdentifier());
		sugar().opportunities.recordView.save();
	}

	/**
	 * Verify that default Likely/Best/Worst amounts is copied from Calculated Revenue Line Item amount when create a new RLI
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_20161_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet prodCatalogFS = testData.get(testName).get(0);

		// Get current date
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date dt = new Date();
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(dt);
		String currentDate = sdf.format(dt);

		// Go to to RLI list view
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();
		sugar().revLineItems.createDrawer.getEditField("name").set(testName);
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(currentDate);
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(myOpportunityRecord.getRecordIdentifier());

		// Select created product
		sugar().revLineItems.createDrawer.getEditField("product").set(sugar().productCatalog.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.showMore();

		// Verify that the calculated RLI amount field has some value it should be copied to likely, worst and best amounts with the ability for user to override
		DecimalFormat formatter = new DecimalFormat("##,###.00");
		String unitPrice = formatter.format(Double.parseDouble(sugar().productCatalog.getDefaultData().get("unitPrice")));
		//sugar().productCatalog.getDefaultData().get("unitPrice");
		sugar().revLineItems.createDrawer.getEditField("likelyCase").assertContains(unitPrice, true);
		sugar().revLineItems.createDrawer.getEditField("bestCase").assertContains(unitPrice, true);
		sugar().revLineItems.createDrawer.getEditField("worstCase").assertContains(unitPrice, true);

		// Change the default values for worst/likely/best amount and save RLIs 
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(prodCatalogFS.get("likelyCase"));
		sugar().revLineItems.createDrawer.getEditField("bestCase").set(prodCatalogFS.get("bestCase"));
		sugar().revLineItems.createDrawer.getEditField("worstCase").set(prodCatalogFS.get("worstCase"));

		// Save create drawer
		sugar().revLineItems.createDrawer.save();

		// Verify worst/likely/best values on the list view
		sugar().revLineItems.listView.getDetailField(1, "likelyCase").assertContains(prodCatalogFS.get("likelyCase"), true);
		sugar().revLineItems.listView.getDetailField(1, "bestCase").assertContains(prodCatalogFS.get("bestCase"), true);
		sugar().revLineItems.listView.getDetailField(1, "worstCase").assertContains(prodCatalogFS.get("worstCase"), true);

		// Go to created RLI recordView
		sugar().revLineItems.listView.clickRecord(1);

		// Verify that test overridden values are saved successfully
		sugar().revLineItems.recordView.getDetailField("likelyCase").assertContains(prodCatalogFS.get("likelyCase"), true);
		sugar().revLineItems.recordView.getDetailField("bestCase").assertContains(prodCatalogFS.get("bestCase"), true);
		sugar().revLineItems.recordView.getDetailField("worstCase").assertContains(prodCatalogFS.get("worstCase"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}