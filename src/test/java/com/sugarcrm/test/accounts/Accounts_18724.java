package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_18724 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify cannot edit the date created, date modified, modified by user and created by user fields on record views
	 * @throws Exception
	 */
	@Test
	public void Accounts_18724_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Account record
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();

		// TODO:VOOD-597:need lib support for date created and date updated fields
		new VoodooControl("span", "css", "span[data-voodoo-name='date_entered']").assertAttribute("class", "detail", true);
		new VoodooControl("span", "css", "span[data-voodoo-name='created_by_name']").assertAttribute("class", "detail", true);
		new VoodooControl("span", "css", "span[data-voodoo-name='date_modified']").assertAttribute("class", "detail", true);
		new VoodooControl("span", "css", "span[data-voodoo-name='modified_by_name']").assertAttribute("class", "detail", true);

		// Cancel Edit view page
		sugar().accounts.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}