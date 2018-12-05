package com.sugarcrm.test.quotedLineItems;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class QuotedLineItems_26903 extends SugarTest {
	DataSource ds;
	FieldSet quotesFieldSet;

	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);
		sugar().accounts.api.create();
	}

	/**
	 * Verify that Quoted Line Items displaying Discount Amount in % correctly
	 * @throws Exception
	 */
	@Test
	public void QuotedLineItems_26903_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// create quote with line item with discount info in percentage
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

		// back to main window to add QLI item for testing
		// TODO: VODO-930, VODO-865, VOOD-1066
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

		// verify discount data in QLI detail view via click on QLI link in Quote detail view after save quote
		// TODO: VOOD-1064
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "a[href*='Products']").click();
		VoodooUtils.focusDefault();
		sugar().quotedLineItems.recordView.getDetailField("discountAmount").assertContains(ds.get(0).get("QLIPercentDiscount"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}