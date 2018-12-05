package com.sugarcrm.test.ListView;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_26578 extends SugarTest {
	DataSource accounts;

	public void setup() throws Exception {
		accounts = testData.get(testName);
		sugar.accounts.api.create(accounts);
		sugar.login();		
	}

	/**
	 *  Verify inline editing works on the records that is from "more" in list view
	 * @throws Exception
	 */
	@Test
	public void ListView_26578_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts list view.
		sugar.accounts.navToListView();
		sugar.accounts.listView.getControl("showMore").waitForVisible();

		// Click "Show More" link to display 20 more Account records.
		sugar.accounts.listView.showMore();
		sugar.accounts.listView.getControl("showMore").waitForVisible();

		// TODO: VOOD-662
		// Hover on "show more" added to make the 40th record visible for inline edit
		sugar.accounts.listView.getControl("showMore").hover();

		// Inline on the last record after clicking show more link i.e 40th record. 
		sugar.accounts.listView.editRecord(40);

		// Verify cancel button and save button are shown 
		sugar.accounts.listView.getControl("save40").assertVisible(true);
		sugar.accounts.listView.getControl("cancel40").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
