package com.sugarcrm.test.reports;

import java.text.DecimalFormat;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_18951 extends SugarTest {
	FieldSet currencyData, customData;
	VoodooControl reportModuleCtrl;

	public void setup() throws Exception {	
		sugar().accounts.api.create();
		sugar().quotes.api.create();
		customData = testData.get(testName).get(0);
		sugar().login();

		// Create custom Currency
		currencyData = new FieldSet();
		currencyData.put("currencyName", testName);
		currencyData.put("conversionRate", customData.get("conversion_rate"));
		currencyData.put("currencySymbol", customData.get("currency_symbol"));
		currencyData.put("ISOcode", customData.get("iso_4217_code"));
		sugar().admin.setCurrency(currencyData);

		// Create product catalog
		sugar().productCatalog.create();
		
		// TODO: VOOD-865
		// Edit quote record to link with product catalog (Add Group)
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);
		sugar().quotes.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "id", "btn_billing_account_name").click();
		VoodooUtils.focusWindow(1);
		VoodooControl recordSelectCtrl = new VoodooControl("a", "css", "tr.oddListRowS1 td:nth-of-type(1) a");
		recordSelectCtrl.click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("select", "id", "currency_id").set(String.format("%s : %s",testName, customData.get("currency_symbol")));
		new VoodooControl("input", "id", "add_group").click();
		new VoodooControl("input", "css", "input[name='Add Row']").click();
		new VoodooControl("button", "id", "product_name_select_1").click();
		VoodooUtils.focusWindow(1);
		recordSelectCtrl.click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that currency symbol displayed under "Subtotal" column in report result for quote is the same as the item displayed in "Currency" field in quote edit view.
	 * @throws Exception
	 */
	@Test
	public void Reports_18951_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		// TODO: VOOD-822
		// Create Rows and Columns Report against Quotes module
		sugar().navbar.navToModule(customData.get("module_plural_name"));
		reportModuleCtrl = new VoodooControl("li", "css", ".dropdown.active .fa-caret-down");
		reportModuleCtrl.click();
		new VoodooControl("li", "css", ".dropdown.active .dropdown-menu.scroll li:nth-of-type(1)").click();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("td", "css", "#report_type_div table tbody tr:nth-child(2) td:nth-child(1) table tbody tr:nth-child(1) td:nth-of-type(1)").click();
		new VoodooControl("table", "id", "Quotes").click();
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		nextBtnCtrl.click();

		// Fields => Quote Subject, Subtotal, Subtotal(US-Dollars)
		new VoodooControl("tr", "id", "Quotes_name").click();
		new VoodooControl("tr", "id", "Quotes_subtotal").click();
		new VoodooControl("tr", "id", "Quotes_subtotal_usdollar").click();
		nextBtnCtrl.click();

		// Save & Run Report
		new VoodooControl("input", "id", "save_report_as").set(testName);
		new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton").click();
		sugar().alerts.waitForLoadingExpiration();

		DecimalFormat formatter = new DecimalFormat("##,###.00");
		String unitPrice = formatter.format(Double.parseDouble(sugar().productCatalog.getDefaultData().get("unitPrice")));
		
		// Verify Quote record with new currency calculations
		Double subTotal = Double.parseDouble(customData.get("conversion_rate")) * Integer.parseInt(sugar().productCatalog.getDefaultData().get("unitPrice"));
		new VoodooControl("tr", "css", "tr.oddListRowS1 td:nth-of-type(1) a").assertEquals(sugar().quotes.getDefaultData().get("name"), true);
		new VoodooControl("tr", "css", "tr.oddListRowS1 td:nth-of-type(2)").assertContains(String.format("%s%s",customData.get("currency_symbol"), Double.toString(subTotal)), true);
		new VoodooControl("tr", "css", "tr.oddListRowS1 td:nth-of-type(3)").assertContains(String.format("$%s",unitPrice), true);

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}