package com.sugarcrm.test.quotes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Quotes_28633 extends SugarTest {
	VoodooControl taxRateCtrl, btnSaveCtrl;
	FieldSet customFS = new FieldSet();
	AccountRecord myAccount;

	public void setup() throws Exception {
		customFS = testData.get(testName).get(0);
		myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().login();

		// Create tax rate
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		taxRateCtrl = sugar().admin.adminTools.getControl("taxRate");
		taxRateCtrl.click();

		// TODO: VOOD-930 
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "btn_create").click();	
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[name='name']").set(customFS.get("sugarTaxTitle"));
		new VoodooControl("input", "css", "input[name='value']").set(customFS.get("sugarTaxRate"));
		btnSaveCtrl = new VoodooControl("input", "id", "btn_save");
		btnSaveCtrl.click();		
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that setting 'Tax Rate' inactive via Admin doesn't add Tax in existing Quote
	 * @throws Exception
	 */
	@Test
	public void Quotes_28633_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().quotes.navToListView();
		sugar().navbar.selectMenuItem(sugar().quotes, "create" + sugar().quotes.moduleNameSingular);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-865
		// Verify that Added tax rate display properly 
		VoodooControl taxRatDropDownCtrl = new VoodooControl("select", "id", "taxrate_id");
		taxRatDropDownCtrl.assertContains(customFS.get("sugarTaxTitle"), true);

		// Enter valid required fields
		sugar().quotes.editView.getEditField("name").set(testName);
		sugar().quotes.editView.getEditField("date_quote_expected_closed").set(sugar().quotes.getDefaultData().get("date_quote_expected_closed"));
		sugar().quotes.editView.getEditField("billingAccountName").set(myAccount.getRecordIdentifier());

		// Add group & Add row
		new VoodooControl("input", "id", "add_group").click();
		new VoodooControl("input", "css", "input[name='Add Row']").click();
		new VoodooControl("input", "id", "name_1").set(testName);
		new VoodooControl("input", "id", "discount_price_1").set(customFS.get("unitPrice"));
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();

		// In-activate created tax rate
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		taxRateCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-930 
		new VoodooControl("a", "css", ".oddListRowS1 td slot a").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("select", "css", "[name='status']").set(customFS.get("inactiveTaxRate"));
		btnSaveCtrl.click();
		VoodooUtils.focusDefault();

		// Go to quotes module editView
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);
		VoodooUtils.waitForReady();
		sugar().quotes.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that The'Tax Rate' drop-down does not contain inactive Tax rate now
		taxRatDropDownCtrl.assertContains("", true);

		// Verify that Tax field shows 0.00.
		new VoodooControl("input", "css", "#add_tables table:nth-child(5) tbody tr:nth-child(4) td:nth-child(3) input[type='text']:nth-child(1)").assertContains(customFS.get("tax"), true);

		// Add row
		new VoodooControl("input", "css", "input[name='Add Row']").click();
		new VoodooControl("input", "id", "name_2").set(testName+"_1");
		new VoodooControl("input", "id", "discount_price_2").set(customFS.get("unitPriceTwo"));
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that user must not see any Tax amount getting added in the Subtotal after save.
		new VoodooControl("td", "css", ".detail.view table tbody tr:nth-child(7) td:nth-child(6)").assertContains(customFS.get("subTotalAmount"), true);
		VoodooUtils.focusDefault();	

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}