package com.sugarcrm.test.ListView;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_28159 extends SugarTest {
	DataSource customData;

	public void setup() throws Exception {
		customData = testData.get(testName);
		sugar.leads.api.create(customData);
		sugar.login();
	}

	/**
	 * Verify that list view should return the list quickly for regular user
	 * 
	 * @throws Exception
	 */
	@Test
	public void ListView_28159_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.leads.navToListView();

		// Verify the list is populated quickly when navigating to List View
		sugar.leads.listView.getDetailField(1, "fullName").assertVisible(true);
		sugar.leads.listView.getDetailField(20, "fullName").scrollIntoView();
		sugar.leads.listView.getDetailField(20, "fullName").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}