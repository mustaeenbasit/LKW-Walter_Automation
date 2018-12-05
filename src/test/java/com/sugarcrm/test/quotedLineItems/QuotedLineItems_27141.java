package com.sugarcrm.test.quotedLineItems;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class QuotedLineItems_27141 extends SugarTest {
	DataSource ds;
	FieldSet fs;
	VoodooControl qliSubPanelControl;

	public void setup() throws Exception {

		ds = testData.get(testName);
		sugar().accounts.api.create();
		sugar().login();

		qliSubPanelControl = new VoodooControl("a", "id", "studiolink_Products");
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);

		// Change separator in user's profile, set '1000s separator:' to . and set 'Decimal Symbol:' to ,
		fs =  new FieldSet();
		fs.put("advanced_grouping_seperator", ".");
		fs.put("advanced_decimal_separator", ",");
		sugar().users.setPrefs(fs);

		// TODO: VOOD-1069, in studio, add 'Discount (US Dollar)' in record view Layout
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		qliSubPanelControl.click();
		new VoodooControl("a", "css", ".wizard #Buttons #layoutsBtn table.wizardButton a.studiolink").click();
		new VoodooControl("a", "css", "#viewBtnrecordview a.studiolink").click();
		VoodooControl quantityFld = new VoodooControl("div", "css", "div.le_panel div[data-name='quantity']");
		new VoodooControl("div", "css", "#toolbox div.le_row.special").dragNDrop(quantityFld);
		VoodooUtils.waitForReady();
		VoodooControl fillerFld = new VoodooControl("div", "css", "div.le_panel .le_field.special");
		new VoodooControl("div", "css", "#availablefields div[data-name='deal_calc_usdollar']").dragNDrop(fillerFld);
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#publishBtn").click();
		VoodooUtils.waitForReady();

		// create quote with QLI for testing
		VoodooUtils.focusDefault();
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
		// TODO: VODO-865
		VoodooUtils.focusWindow(0); // this line is needed, otherwise test run will hang here forever ...
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "css", "#add_group").click();
		new VoodooControl("input", "css", "input[name='Add Row']").click();
		new VoodooControl("input", "css", "#name_1").set(ds.get(0).get("product_name"));
		new VoodooControl("input", "css", "#cost_price_1").set(ds.get(0).get("cost_price"));
		new VoodooControl("input", "css", "#list_price_1").set(ds.get(0).get("list_price"));
		new VoodooControl("input", "css", "#discount_price_1").set(ds.get(0).get("unit_price"));
		new VoodooControl("input", "css", "#discount_amount_1").set(ds.get(0).get("discount_price"));
		new VoodooControl("input", "css", "#checkbox_select_1").set(ds.get(0).get("percent_check"));
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();
	}

	/**
	 * Verify that discount amount is calculated correctly in QLI when decimal separator is "," and thousands separator is ","
	 *
	 * @throws Exception
	 */
	@Test
	public void QuotedLineItems_27141_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click QLI link via Quotes detail view to open QLI record view and verify Discount Amount in record view
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", ".detail.view a[href*='Products']").click();
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("span", "css", ".record span[data-fieldname='deal_calc_usdollar'] .detail").assertContains(ds.get(0).get("discountAmount"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}