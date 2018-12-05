package com.sugarcrm.test.quotes;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Quotes_27932 extends SugarTest {
	DataSource quotesData = new DataSource();
	DataSource accountData = new DataSource();

	public void setup() throws Exception {
		quotesData = testData.get(testName);
		accountData = testData.get(testName + "_accounts");
		sugar().accounts.api.create(accountData);
		sugar().login();
		sugar().quotes.create(quotesData);
	}

	/**
	 * Verify that all columns in the Quotes list view are sortable
	 * @throws Exception
	 */
	@Test
	public void Quotes_27932_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Navigate to Quotes Listview and verify default order of records in listview
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);
		sugar().quotes.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-930
		new VoodooControl("input", "id", "billing_account_name").set(accountData.get(1).get("name"));
		new VoodooControl("li", "css", "#EditView_billing_account_name_results li").click();
		VoodooUtils.waitForReady();
		new VoodooControl("button", "id", "btn_clr_assigned_user_name").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "assigned_user_name").set(sugar().users.getQAUser().get("userName"));
		new VoodooControl("li", "css", "#EditView_assigned_user_name_results li").click();
		VoodooUtils.waitForReady();

		// Add group & Add row
		new VoodooControl("input", "id", "add_group").scrollIntoViewIfNeeded(true);
		new VoodooControl("input", "css", "input[name='Add Row']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "name_1").set(testName);
		new VoodooControl("input", "id", "discount_price_1").set(quotesData.get(0).get("description"));
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();

		// Navigate to List View and verify Quotes records are created successfully
		sugar().quotes.navToListView();
		sugar().quotes.listView.verifyField(1, "name", quotesData.get(1).get("name"));
		sugar().quotes.listView.verifyField(2, "name", quotesData.get(0).get("name"));

		// Sort records by each column both ascending and descending
		for (int i = 0; i < 9; i++) {
			if (i != 4) {
				// Click on each column to sort in ascending order
				VoodooUtils.focusFrame("bwc-frame");
				VoodooControl numberColumnCtrl = new VoodooControl("a", "css", ".list.view tr:nth-child(2) th:nth-child("+(i+4)+") a");
				numberColumnCtrl.click();
				VoodooUtils.waitForReady();
				VoodooUtils.focusDefault();

				// Verify that sorting works as expected for each column (Ascending order)
				sugar().quotes.listView.verifyField(1, "name", quotesData.get(0).get("name"));
				sugar().quotes.listView.verifyField(2, "name", quotesData.get(1).get("name"));

				// Again click on the same column to sort
				VoodooUtils.focusFrame("bwc-frame");
				numberColumnCtrl.click();
				VoodooUtils.waitForReady();
				VoodooUtils.focusDefault();

				// Verify that sort order is reversed (Descending order)
				sugar().quotes.listView.verifyField(1, "name", quotesData.get(1).get("name"));
				sugar().quotes.listView.verifyField(2, "name", quotesData.get(0).get("name"));
			}
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}