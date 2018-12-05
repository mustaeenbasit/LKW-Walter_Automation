package com.sugarcrm.test.quotes;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Quotes_26456 extends SugarTest {
	FieldSet separator = new FieldSet();
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
		ds = testData.get(testName);
		separator = testData.get(testName + "_sep").get(0);

		// Set '1000s separator:' to "."  and   Set 'Decimal Symbol:' to ","
		sugar().users.setPrefs(separator);
		FieldSet quotesFieldSet;
		quotesFieldSet = new FieldSet();
		quotesFieldSet.put("name",ds.get(0).get("name"));
		quotesFieldSet.put("date_quote_expected_closed",ds.get(0).get("date_quote_expected_closed"));
		quotesFieldSet.put("billingAccountName",ds.get(0).get("billingAccountName"));
		sugar().quotes.create(quotesFieldSet);

		// TODO: VOOD-930 
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "#tax_rates").click();
		VoodooUtils.focusDefault();		
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "btn_create").click();	
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[name='name']").set(ds.get(0).get("sugarTaxTitle"));
		new VoodooControl("input", "css", "input[name='value']").set(ds.get(0).get("sugarTaxRate"));
		new VoodooControl("input", "id", "btn_save").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that quote quantity calculated properly in case thousands separator changed to "."
	 * @throws Exception
	 */
	@Test
	public void Quotes_26456_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-865
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);
		sugar().quotes.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "css", "input[name='add_group']").click();		
		new VoodooControl("input", "css", "input[name='Add Row']").click();
		new VoodooControl("input", "css", "input[name='quantity[1]']").set(ds.get(0).get("quantity"));
		new VoodooControl("input", "css", "input[name='product_name[1]']").set(ds.get(0).get("product_name"));
		new VoodooControl("input", "css", "input[name='cost_price[1]']").set(ds.get(0).get("cost_price"));
		new VoodooControl("input", "css", "input[name='list_price[1]']").set(ds.get(0).get("list_price"));
		new VoodooControl("input", "css", "input[name='discount_price[1]']").set(ds.get(0).get("discount_price"));
		new VoodooControl("input", "css", "input[id='SAVE_FOOTER']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "css", "tr:nth-child(15) td:nth-child(3)").assertContains("$1.082,50", true);
		VoodooUtils.focusDefault();	

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}