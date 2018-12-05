package com.sugarcrm.test.ListView;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_17174 extends SugarTest {
	DataSource accountsData;

	public void setup() throws Exception {
		accountsData = testData.get(testName);
		sugar.accounts.api.create(accountsData);
		sugar.login();
	}

	/**
	 * Verify message when sorting
	 * @throws Exception
	 */
	@Test
	public void ListView_17174_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar.accounts.navToListView();
		
		// Set Name in Ascending Order.
		sugar.accounts.listView.sortBy("headerName", true);

		// Verification of Name in Ascending Order
		sugar.accounts.listView.verifyField(1, "name", (accountsData.get(0).get("name")));
		sugar.accounts.listView.verifyField(2, "name", (accountsData.get(2).get("name")));
		sugar.accounts.listView.verifyField(3, "name", (accountsData.get(1).get("name")));
		
		// Set Billing City in Ascending Order
		sugar.accounts.listView.sortBy("headerBillingaddresscity", true);
		
		// Verification of Billing City in Ascending Order
		sugar.accounts.listView.verifyField(1, "billingAddressCity", accountsData.get(2).get("billingAddressCity"));
		sugar.accounts.listView.verifyField(2, "billingAddressCity", accountsData.get(1).get("billingAddressCity"));
		sugar.accounts.listView.verifyField(3, "billingAddressCity", accountsData.get(0).get("billingAddressCity")); 

		// Set Billing Country in Ascending Order.
		sugar.accounts.listView.sortBy("headerBillingaddresscountry", true);
		
		// Verification of Billing Country in Ascending Order
		sugar.accounts.listView.verifyField(1, "billingAddressCountry", accountsData.get(1).get("billingAddressCountry"));
		sugar.accounts.listView.verifyField(2, "billingAddressCountry", accountsData.get(0).get("billingAddressCountry"));
		sugar.accounts.listView.verifyField(3, "billingAddressCountry", accountsData.get(2).get("billingAddressCountry"));

		// Set Name in Descending Order
		sugar.accounts.listView.sortBy("headerName", false);
		
		// Verification of Name in Descending Order
		sugar.accounts.listView.verifyField(1, "name", (accountsData.get(1).get("name")));
		sugar.accounts.listView.verifyField(2, "name", (accountsData.get(2).get("name")));
		sugar.accounts.listView.verifyField(3, "name", (accountsData.get(0).get("name")));

		// Set Billing City in Descending Order
		sugar.accounts.listView.sortBy("headerBillingaddresscity", false);
		
		// Verification of Billing City in Descending Order
		sugar.accounts.listView.verifyField(1, "billingAddressCity", accountsData.get(0).get("billingAddressCity")); 
		sugar.accounts.listView.verifyField(2, "billingAddressCity", accountsData.get(1).get("billingAddressCity"));
		sugar.accounts.listView.verifyField(3, "billingAddressCity", accountsData.get(2).get("billingAddressCity"));

		// Set Billing Country in Descending Order
		sugar.accounts.listView.sortBy("headerBillingaddresscountry", false);
		
		// Verification of Billing Country in Descending Order
		sugar.accounts.listView.verifyField(1, "billingAddressCountry", accountsData.get(2).get("billingAddressCountry"));
		sugar.accounts.listView.verifyField(2, "billingAddressCountry", accountsData.get(0).get("billingAddressCountry"));
		sugar.accounts.listView.verifyField(3, "billingAddressCountry", accountsData.get(1).get("billingAddressCountry"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}