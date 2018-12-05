package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class CurrencyTests extends SugarTest {
	FieldSet currencyData = new FieldSet();

	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void verifyCurrencySettingsControls() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyCurrencySettingsControls()...");

		sugar().admin.navToCurrencySettings();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify hook values of available currencySettings controls
		sugar().admin.currencySettings.getControl("currencyName").assertVisible(true);
		sugar().admin.currencySettings.getControl("conversionRate").assertVisible(true);
		sugar().admin.currencySettings.getControl("currencySymbol").assertVisible(true);
		sugar().admin.currencySettings.getControl("ISOcode").assertVisible(true);
		sugar().admin.currencySettings.getControl("status").assertVisible(true);
		sugar().admin.currencySettings.getControl("save").assertVisible(true);
		sugar().admin.currencySettings.getControl("cancel").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyCurrencySettingsControls() test complete.");
	}

	@Test
	public void verifyCurrencyNavigation() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyCurrencyNavigation()...");

		sugar().admin.navToCurrencySettings();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1456
		// Verify navigation to 'Currency' page
		new VoodooControl("h2", "css", ".moduleTitle").assertContains("Currencies", true);		

		VoodooUtils.voodoo.log.info("verifyCurrencyNavigation() test complete.");
	}

	@Test
	public void currencyCreationAndInactivationTest() throws Exception {
		VoodooUtils.voodoo.log.info("Running currencyCreationAndInactivationTest()...");		

		// Create new currency
		currencyData.put("currencyName", "Test_Currency");
		currencyData.put("conversionRate", "0.9");
		currencyData.put("currencySymbol", "C");
		currencyData.put("ISOcode", "TC");
		currencyData.put("status", "Active");

		sugar().admin.setCurrency(currencyData);

		sugar().admin.navToCurrencySettings();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1456
		// Verify that currency is successfully created
		new VoodooControl("a","xpath","//*[@id='contentTable']/tbody/tr/td/table[1]/tbody/tr[contains(.,'"+currencyData.get("currencyName")+"')]/td/slot/a").assertVisible(true);
		VoodooUtils.focusDefault();

		// Set currency 'Inactive'
		sugar().admin.inactiveCurrency(currencyData.get("currencyName"));

		// Verify that currency successfully set to 'Inactive'
		sugar().admin.navToCurrencySettings();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1456
		new VoodooControl("a","xpath","//*[@id='contentTable']/tbody/tr/td/table[1]/tbody/tr[contains(.,'Inactive')]/td/slot/a").assertVisible(true);

		VoodooUtils.voodoo.log.info("Completed currencyCreationAndInactivationTest()");
	}

	@Test
	public void editCurrencyTest() throws Exception {
		VoodooUtils.voodoo.log.info("Running editCurrencyTest()...");

		// Create new currency
		currencyData.put("currencyName", "New_Currency");
		currencyData.put("conversionRate", "0.9");
		currencyData.put("currencySymbol", "C");
		currencyData.put("ISOcode", "TC");
		currencyData.put("status", "Active");
		sugar().admin.setCurrency(currencyData);

		// Edit currency
		FieldSet currencyNewData = new FieldSet();
		currencyNewData.put("currencyName", "My_Currency");
		currencyNewData.put("conversionRate", "0.8");
		currencyNewData.put("currencySymbol", "T");
		currencyNewData.put("ISOcode", "CD");
		currencyNewData.put("status", "Inactive");
		sugar().admin.editCurrency(currencyData.get("currencyName"), currencyNewData);

		// Verify that currency successfully edited
		sugar().admin.navToCurrencySettings();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1456
		VoodooControl currencyRow = new VoodooControl("tr", "css", "#contentTable .list.view tr.evenListRowS1");
		currencyRow.assertContains(currencyNewData.get("currencyName"), true);
		currencyRow.assertContains(currencyNewData.get("conversionRate"), true);
		currencyRow.assertContains(currencyNewData.get("currencySymbol"), true);
		currencyRow.assertContains(currencyNewData.get("ISOcode"), true);
		currencyRow.assertContains(currencyNewData.get("status"), true);

		VoodooUtils.voodoo.log.info("editCurrencyTest() test complete.");
	}

	public void cleanup() throws Exception {}
}