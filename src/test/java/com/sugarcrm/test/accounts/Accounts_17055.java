package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_17055 extends SugarTest {
		
	public void setup() throws Exception {
		sugar().login();
		sugar().accounts.api.create();
		sugar().accounts.navToListView();
	}

	/**
	 * Date Created field is read only on list view inline edit 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17055_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
		
		sugar().accounts.listView.editRecord(1);
		new VoodooControl("span","css","span.fld_date_entered.list").assertVisible(true);
		new VoodooControl("input","css","fld_date_entered.list input").assertExists(false);		
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}