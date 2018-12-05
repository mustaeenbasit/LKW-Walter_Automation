package com.sugarcrm.test.contracts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContractRecord;
import com.sugarcrm.sugar.records.QuoteRecord;
import com.sugarcrm.test.SugarTest;

public class Contracts_19863 extends SugarTest {
	ContractRecord myContract;
	QuoteRecord myQuote;
	AccountRecord myAccount;
	
	DataSource ds;
	FieldSet quotesFieldSet;
	
	public void setup() throws Exception {
		sugar.login();

		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);

		myContract = (ContractRecord) sugar.contracts.api.create();
		myAccount = (AccountRecord)sugar.accounts.api.create();
		
		ds = testData.get(testName);
		
		quotesFieldSet = new FieldSet();
		quotesFieldSet.put("name",ds.get(0).get("name"));
		quotesFieldSet.put("date_quote_expected_closed",ds.get(0).get("date_quote_expected_closed"));
		quotesFieldSet.put("billingAccountName",myAccount.getRecordIdentifier());
		sugar.quotes.create(quotesFieldSet);
	}

	/**
	 * Verify that Quote can be selected by clicking Subject for Contract
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19863_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myContract.navToRecord();
		
		VoodooUtils.focusFrame("bwc-frame");
		
		if (new VoodooControl("span", "id", "show_link_quotes").queryVisible())
			new VoodooControl("span", "id", "show_link_quotes").click();
		
		new VoodooControl("a", "id", "contracts_quotes_select_button").click();
		
		// Pick Quote from pop up window
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "massall_top").click();
		new VoodooControl("input", "id", "MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0);
		
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Assert Quote name
		new VoodooControl("td", "css", "div#list_subpanel_quotes table tr:nth-of-type(3) td:nth-of-type(1)").assertContains(ds.get(0).get("name"), true);
		
		// Assert Account name
		new VoodooControl("td", "css", "div#list_subpanel_quotes table tr:nth-of-type(3) td:nth-of-type(2)").assertContains(myAccount.getRecordIdentifier(), true);
		
		// Assert Valid Until
		new VoodooControl("td", "css", "div#list_subpanel_quotes table tr:nth-of-type(3) td:nth-of-type(4)").assertContains(ds.get(0).get("date_quote_expected_closed"), true);
		
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}