package com.sugarcrm.test.ListView;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class ListView_17046 extends SugarTest {
	DataSource accounts;

	public void setup() throws Exception {
		accounts = testData.get(testName);
		sugar.accounts.api.create(accounts);

		sugar.login();
	}
	/**
	 * Verify Next/Previous widget greyed out when at end or beginning of record view
	 */
	@Test
	public void ListView_17046_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Account list view and click on first Account record.
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);

		// TODO: VOOD-622 Need support to access previous and next arrow in record view
		new VoodooControl("a", "css", ".btn.btn-invisible.previous-row.disabled").waitForElement();
		// Verify that the "previous" arrow is grayed out since this is the first record on the list.
		new VoodooControl("a", "css", ".btn.btn-invisible.previous-row.disabled").assertExists(true);

		// Go to Account list view and click on last Account record.
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(10);

		// TODO: VOOD-622 Need support to access previous and next arrow in record view
		new VoodooControl("a", "css", ".btn.btn-invisible.next-row.disabled").waitForElement();
		// Verify that the "next" arrow is grayed out since this is the last record on the list.
		new VoodooControl("a", "css", ".btn.btn-invisible.next-row.disabled").assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	public void cleanup() throws Exception {}
}