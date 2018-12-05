package com.sugarcrm.test.accounts;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.test.SugarTest;

public class Accounts_17289 extends SugarTest {
	protected AccountRecord myAccount;
	protected ArrayList<Record> myAccounts;

	public void setup() throws Exception {
		sugar().login();
		// create accounts
		sugar().accounts.api.create(testData.get(testName));
	}

	/**
	 * Verify hide Next/Previous widget on the preview header when one record
	 * selected
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17289_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();
		sugar().accounts.listView.toggleFavorite(1);
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterMyFavorites();

		sugar().accounts.listView.previewRecord(1);

		// TODO VOOD-511 need lib support for preview pane navigation
		VoodooControl next = new VoodooControl("i", "css", "div[data-voodoo-name='preview-header'] i.fa-chevron-right");
		VoodooControl previous = new VoodooControl("i", "css", "div[data-voodoo-name='preview-header'] i.fa-chevron-left");
		next.assertVisible(false);
		previous.assertVisible(false);
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
