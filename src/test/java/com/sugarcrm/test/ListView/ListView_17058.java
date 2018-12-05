package com.sugarcrm.test.ListView;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class ListView_17058 extends SugarTest {
	DataSource accounts;

	public void setup() throws Exception {
		accounts = testData.get(testName);
		sugar.accounts.api.create(accounts);

		sugar.login();
	}

	/**
	 *  Verify Next/Previous widget grayed out when at end or beginning of "preview"
	 */
	@Test
	public void ListView_17058_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Account list view and click on preview icon of first Account record.
		sugar.accounts.navToListView();
		sugar.accounts.listView.previewRecord(1);

		// TODO: VOOD-623 Need support to access previous and next arrow in preview panel
		new VoodooControl("a", "css", "a[data-direction='left']").waitForElement();
		// Verify that the "previous" arrow is grayed out since this is the first record on the list.
		new VoodooControl("a", "css", "a[data-direction='left']").assertAttribute("class", "disabled", true);

		// Go to Account list view and click on preview icon of last Account record.
		sugar.accounts.navToListView();
		sugar.accounts.listView.previewRecord(10);

		// TODO: VOOD-623 Need support to access previous and next arrow in preview panel
		new VoodooControl("a", "css", "a[data-direction='right']").waitForElement();
		// Verify that the "next" arrow is grayed out since this is the last record on the list.
		new VoodooControl("a", "css", "a[data-direction='right']").assertAttribute("class", "disabled", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}