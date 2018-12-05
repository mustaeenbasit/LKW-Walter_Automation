package com.sugarcrm.test.accounts;

import com.sugarcrm.candybean.datasource.FieldSet;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_22888 extends SugarTest {
	DataSource ds1 = new DataSource();
	DataSource ds2 = new DataSource();

	public void setup() throws Exception {
		sugar().login();
		ds1 = testData.get(testName);
		ds2 = testData.get(testName + "_1");
		// create accounts
		sugar().accounts.api.create(ds1);

		FieldSet systemSettingsData = new FieldSet();
		systemSettingsData.put("maxEntriesPerPage", ds2.get(0).get("num1"));
		// change system settings
		sugar().admin.setSystemSettings(systemSettingsData);
	}

	/**
	 * Verify that corresponding account records are displayed after clicking the "More accounts..." link in list view.
	 *
	 * @throws Exception
	 */
	@Test
	public void Accounts_22888_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();
		int i, j = 1, k, l;
		// TODO VOOD-727
		VoodooControl loadMore = new VoodooControl("button", "css", "button.btn-link.more");
		k = Integer.parseInt(ds2.get(0).get("num1"));
		l = Integer.parseInt(ds2.get(0).get("num1"));
		// click More accounts link and verify it only load the number of records that set in system setting
		while (loadMore.queryVisible()) {
			new VoodooControl("tr", "css", "div.flex-list-view-content tbody tr:nth-child(" + k + 1 + ")").assertExists(false);
			loadMore.click();
			j = j + 1;
			for (i = k + 1; i < l * j && i <= ds1.size(); i++) {
				String checkbox = String.format("checkbox%02d", i);
				sugar().accounts.listView.getControl(checkbox).assertExists(true);
			}
			k = i;
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
}

	public void cleanup() throws Exception {}
}