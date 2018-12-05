package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_17258 extends SugarTest {
	AccountRecord account1;
	
	public void setup() throws Exception {
		sugar().login();
		account1 = (AccountRecord)sugar().accounts.api.create();
		sugar().accounts.navToListView();
	}

	/**
	 * Show alert message when doing mass delete from list view
	 * @throws Exception
	 */
	@Test
	public void Accounts_17258_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.listView.toggleSelectAll();
		sugar().accounts.listView.openActionDropdown();
		sugar().accounts.listView.delete();
		DataSource ds = testData.get(testName);
		// TODO: VOOD-664
		new VoodooControl("div", "css", "div.alert.alert-warning.alert-block").assertContains(ds.get(0).get("alert"), true);
		new VoodooControl("a", "css", "div.alert.alert-warning.alert-block a[data-action='confirm']").assertEquals(ds.get(0).get("confirm"), true);
		new VoodooControl("a", "css", "div.alert.alert-warning.alert-block a[data-action='cancel']").assertEquals(ds.get(0).get("cancel"), true);
		sugar().alerts.getAlert().cancelAlert();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 
