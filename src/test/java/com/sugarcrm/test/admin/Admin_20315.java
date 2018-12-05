package com.sugarcrm.test.admin;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class Admin_20315 extends SugarTest {
	FieldSet fs, fsRLI;
	AccountRecord myAcc;
	OpportunityRecord myOpp;
	RevLineItemRecord myRLI;
	
	public void setup() throws Exception {
		sugar().login();
		
		fs = testData.get(testName).get(0);
		fs.put("name", testName); // Use testName as currency name
		fsRLI = testData.get(testName+"_RLI").get(0);
		
		myAcc = (AccountRecord) sugar().accounts.api.create();
		myOpp = (OpportunityRecord)sugar().opportunities.api.create();
		myRLI = (RevLineItemRecord)sugar().revLineItems.api.create();

		// Create custom currency
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO VOOD-840
		new VoodooControl("a", "css", "#currencies_management").click();

		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// If Currency name already exist, then activate it otherwise create a new one
		// Since a currency cannot be deleted, only deactivated, running a test multiple times
		// unnecessarily creates many records of the same currency, hence this arrangement
		if ((new VoodooControl("a", "xpath","//table[@class='list view']//a[text()='"+fs.get("name")+"']")).queryExists()) {
			new VoodooControl("a", "xpath","//table[@class='list view']//a[text()='"+fs.get("name")+"']").click();
			VoodooUtils.focusFrame("bwc-frame");
			VoodooUtils.pause(1000);
			new VoodooControl("input", "css", "table.edit.view input[name='conversion_rate']").set(fs.get("rate"));
			new VoodooControl("input", "css", "table.edit.view input[name='symbol']").set(fs.get("symbol"));
			new VoodooControl("input", "css", "table.edit.view input[name='iso4217']").set(fs.get("code"));
			new VoodooControl("select", "css", "select[name='status']").set("Active");
			new VoodooControl("input", "css", "input[value='Save']").click();
			VoodooUtils.focusDefault();
			sugar().alerts.waitForLoadingExpiration();
		}
		else {
			// TODO VOOD-840
			new VoodooControl("input", "css", "table.edit.view input[name='name']").set(fs.get("name"));
			new VoodooControl("input", "css", "table.edit.view input[name='conversion_rate']").set(fs.get("rate"));
			new VoodooControl("input", "css", "table.edit.view input[name='symbol']").set(fs.get("symbol"));
			new VoodooControl("input", "css", "table.edit.view input[name='iso4217']").set(fs.get("code"));
			new VoodooControl("input", "css", "input[value='Save']").click();
			VoodooUtils.pause(1000); // Wait for processing to complete
			VoodooUtils.focusDefault();
		}
	}

	/**
	 * Verify new exchange rate is used when edit an existing opportunity 
	 * 
	 * @throws Exception
	 */
	@Ignore("VOOD-444")
	@Test	
	public void Admin_20315_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
		
		// Edit RLI - set sales stage, amt, currency and related opportunity
		myRLI.navToRecord();
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(myOpp.getRecordIdentifier());
		new VoodooControl("a", "css", "span.fld_likely_case.edit span.currency.edit.fld_currency_id a.select2-choice").click();
		new VoodooControl("div", "xpath", "//*[@id='select2-drop']/ul/li/div[contains(.,'"+fs.get("code")+"')]").click();
		sugar().revLineItems.createDrawer.getEditField("salesStage").set(fsRLI.get("salesStage"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(fsRLI.get("likelyCase"));
		sugar().revLineItems.recordView.save();
		sugar().alerts.waitForLoadingExpiration();

		// Verify amt per exchange rate
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.verifyField(1, "oppAmount", fsRLI.get("verifyAmt1"));

		// Change exchange rate
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO VOOD-840
		new VoodooControl("a", "css", "#currencies_management").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "xpath","//table[@class='list view']//a[text()='"+fs.get("name")+"']").click();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.pause(1000);
		new VoodooControl("input", "css", "table.edit.view input[name='conversion_rate']").set(fs.get("newRate"));
		new VoodooControl("input", "css", "input[value='Save']").click();
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();

		// Edit RLI again
		myRLI.navToRecord();
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.save();
		sugar().alerts.waitForLoadingExpiration();

		// Verify amt per new exchange rate
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.verifyField(1, "oppAmount", fsRLI.get("verifyAmt2"));
		
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}