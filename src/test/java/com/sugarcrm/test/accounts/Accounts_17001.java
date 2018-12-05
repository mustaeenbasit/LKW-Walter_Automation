package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_17001 extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		sugar().login();
		myAccount = (AccountRecord) sugar().accounts.api.create();
		sugar().navbar.navToAdminTools();
		//TODO VOOD-542
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "a#studio").click();
		VoodooControl account = new VoodooControl("a", "id", "studiolink_Accounts");
		account.waitForVisible();
		account.click();
		new VoodooControl("a", "css", "td#fieldsBtn a").click();
		new VoodooControl("a", "css", "div#field_table a#phone_office").click();
		new VoodooControl("input", "name", "required").set("true");		
		new VoodooControl("input", "name", "fsavebtn").click();
		VoodooUtils.pause(2000);
		VoodooUtils.refresh();		
		sugar().navbar.getControl("showAllModules").waitForElement(30000);
	}

	/**
	 * Validation check for phone type field on edit form
	 * @throws Exception
	 */
	@Test
	public void Accounts_17001_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		myAccount.navToRecord();
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.getEditField("workPhone").set("");
		sugar().accounts.recordView.save();
		new VoodooControl("i", "css", "span.fld_phone_office.edit.error i.fa-exclamation-circle").assertExists(true);
		sugar().accounts.recordView.getControl("saveButton").assertVisible(true);
		sugar().accounts.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}