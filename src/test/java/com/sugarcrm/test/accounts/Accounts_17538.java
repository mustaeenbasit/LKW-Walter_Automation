package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_17538 extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		sugar().login();
		myAccount = (AccountRecord)sugar().accounts.api.create();
	}

	/**
	 * Shouldn't open record filter dropdown when selecting all related modules
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17538_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		myAccount.navToRecord();
		
		// TODO VOOD-468 VOOD-486 need replace these controls after lib file is done
		new VoodooControl ("span", "css", "span[data-voodoo-name='filter-filter-dropdown']").click();
		new VoodooControl ("div", "css", "div#select2-drop").assertExists(false);	

		new VoodooSelect ("a", "css", "div.related-filter a").set("Contacts");
		VoodooUtils.waitForAlertExpiration();
		
		new VoodooSelect ("a", "css", "div.related-filter a").set("All");
		VoodooUtils.waitForAlertExpiration();
		
		new VoodooControl ("span", "css", "span[data-voodoo-name='filter-filter-dropdown']").click();
		new VoodooControl ("div", "css", "div#select2-drop").assertExists(false);		
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}