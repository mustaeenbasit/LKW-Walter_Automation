package com.sugarcrm.test.quotedLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class QuotedLineItems_26905 extends SugarTest {
	DataSource ds, ds1, ds2;

	public void setup() throws Exception {
		sugar().login();
		sugar().accounts.api.create();
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);

		ds = testData.get(testName);
		ds1 = testData.get(testName + "_Manufacturers");
		ds2 = testData.get(testName + "_Product");

		// TODO: VOOD-1068, setup Manufacture record for testing
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "#manufacturers").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "#btn_create").click();
		new VoodooControl("input", "css", ".edit.view input[name='name']").set(ds1.get(0).get("name"));
		new VoodooControl("input", "css", ".formHeader.h3Row #btn_save").click();
		VoodooUtils.focusDefault();

		// TODO: VOOD-1069, in studio edit QLI record view to add Manufacturer Name field in layouts
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		new VoodooControl("a", "css", "#studiolink_Products").click();
		new VoodooControl("a", "css", ".wizard #Buttons #layoutsBtn table.wizardButton a.studiolink").click();
		new VoodooControl("a", "css", "#viewBtnrecordview a.studiolink").click();
		VoodooControl quantityFld = new VoodooControl("div", "css", "div.le_panel div[data-name='quantity']");
		new VoodooControl("div", "css", "#toolbox div.le_row.special").dragNDrop(quantityFld);
		VoodooControl fillerFld = new VoodooControl("div", "css", "div.le_panel .le_field.special");
		new VoodooControl("div", "css", "#availablefields div[data-name='manufacturer_name']").dragNDrop(fillerFld);
		new VoodooControl("input", "css", "#publishBtn").click();
		sugar().alerts.waitForLoadingExpiration();

		// in studio edit QLI list view to add Manufacturer Name field in layouts
		new VoodooControl("a", "css", ".bodywrapper a[onclick*='layouts']").click();
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("a", "css", "#viewBtnlistview a.studiolink").click();
		VoodooUtils.waitForAlertExpiration();
		VoodooControl quotenameFld = new VoodooControl("li", "css", "#ul0 li[data-name='quote_name']");
		new VoodooControl("li", "css", "#ul2 li[data-name='manufacturer_name']").dragNDrop(quotenameFld);
		new VoodooControl("input", "css", "#savebtn").click();
		sugar().alerts.waitForLoadingExpiration();

		// TODO: 931, setup a product with Manufacturer relationship for QLI testing
		VoodooUtils.focusDefault(); // workaround for hanging issue after edits in studio list view
		sugar().accounts.navToListView(); // workaround for hanging issue after edits in studio list view
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "#product_catalog").click();
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("a", "css", ".headerpane a[name='create_button']").click();
		new VoodooControl("input", "css", ".fld_name.edit input[name='name']").set(ds2.get(0).get("name"));

		// Setup Manufacturer data
		new VoodooControl("a", "css", "div[data-name='manufacturer_name'] a[href*='javascript']").click();
		new VoodooControl("input", "css", "#select2-drop .select2-search input[type='text']").set(ds1.get(0).get("name"));
		new VoodooControl("span", "css", ".select2-results .select2-result-label span.select2-match").click();

		// Setup product related price required fields
		new VoodooControl("input", "css", ".fld_cost_price.edit input[name=cost_price]").set(ds2.get(0).get("cost_price"));
		new VoodooControl("input", "css", ".fld_discount_price.edit input[name=discount_price]").set(ds2.get(0).get(
				"discount_price"));
		new VoodooControl("input", "css", ".fld_list_price.edit input[name='list_price']").set(ds2.get(0).get("list_price"));
		sugar().productCatalog.createDrawer.save();
		VoodooUtils.focusDefault();

		// create quote with Quote line item for testing
		sugar().quotes.navToListView();
		sugar().navbar.selectMenuItem(sugar().quotes, "create" + sugar().quotes.moduleNameSingular);
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.editView.getEditField("name").set(ds.get(0).get("name"));
		sugar().quotes.editView.getEditField("date_quote_expected_closed").set(ds.get(0).get("date_quote_expected_closed"));
		// TODO: VOOD-1066, workaround to sometime pass, sometime failed situation with popup window
		new VoodooControl("button", "css", "#btn_billing_account_name").click();
		VoodooUtils.pause(1000); // to wait for window rendered
		VoodooUtils.focusWindow(1);
		new VoodooControl("a", "css", ".list.view tr.oddListRowS1 td:nth-of-type(1) a[href*='javascript']").click();
		// TODO: VODO-930, VODO-865, VOOD-1066
		VoodooUtils.focusWindow(0); // this line is needed, otherwise test run will hang here forever ...
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "css", "#add_group").click();
		new VoodooControl("input", "css", "input[name='Add Row']").click();
		new VoodooControl("button", "css", "#product_name_select_1").click();
		VoodooUtils.pause(1000); // to wait for window rendered, and select the setup product data
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", "#name_advanced").set(ds2.get(0).get("name"));
		new VoodooControl("input", "css", "#search_form_submit").click();
		new VoodooControl("a", "css", ".list.view tr.oddListRowS1 td:nth-of-type(1) a[href*='javascript']").click();
		VoodooUtils.focusWindow(0); // this line is needed, otherwise test run will hang here forever ...
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();
	}

	/**
	 * Verify that Manufacturer Name is populated in Quoted Line Items List View
	 *
	 * @throws Exception
	 */
	@Test
	public void QuotedLineItems_26905_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// click QLI link via Quotes detail view to open QLI record view and verify Manufacturer Name in record view
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", ".detail.view a[href*='Products']").click();
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("span", "css", ".record span[data-fieldname='manufacturer_name'] .detail").assertContains(
				ds1.get(0).get("name"), true);

		// verify Manufacturer Name in list view
		sugar().quotedLineItems.navToListView();
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("a", "css", ".flex-list-view-content span.list a[href*='Manufacture']").assertContains(
				ds1.get(0).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}