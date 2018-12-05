package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_27822 extends SugarTest {
	AccountRecord myAccount;
	FieldSet accData;
	
	public void setup() throws Exception {
		accData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify that historical Summary page correctly displayed special characters in the record name
	 * @throws Exception
	 */
	@Test
	public void Accounts_27822_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = new FieldSet();
		fs.put("name", accData.get("accName"));

		AccountRecord myAccount = (AccountRecord)sugar().accounts.create(fs);
		myAccount.verify(fs);

		sugar().accounts.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", ".fld_historical_summary_button.detail a").click();
		sugar().alerts.waitForLoadingExpiration();
		
		new VoodooControl("span", "css", "span.fld_title.history-summary-headerpane").assertContains(accData.get("accName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}