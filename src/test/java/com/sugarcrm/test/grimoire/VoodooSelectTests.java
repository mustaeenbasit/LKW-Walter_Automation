package com.sugarcrm.test.grimoire;

import com.sugarcrm.sugar.UnfoundSelectListOptionException;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class VoodooSelectTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void verifyClickSearchForMore() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyClickSearchForMore()...");

		// Go to CreateDrawer of Contacts module
		sugar().contacts.navToListView();
		sugar().contacts.listView.create();

		// Relate 'Account name' to Contacts by clicking "Search for more" in select box
		VoodooSelect account = (VoodooSelect)sugar().contacts.createDrawer.getEditField("relAccountName");
		account.clickSearchForMore();
		sugar().accounts.searchSelect.getControl("create").assertVisible(true);
		sugar().accounts.searchSelect.cancel();
		sugar().contacts.createDrawer.cancel();

		VoodooUtils.voodoo.log.info("verifyClickSearchForMore() test complete.");
	}

	@Test(expected = UnfoundSelectListOptionException.class)
	public void unfoundSelectListOptionExceptionTest() throws Exception {
		VoodooUtils.voodoo.log.info("Running unfoundSelectListOptionExceptionTest()...");

		sugar().accounts.api.create();
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.getEditField("industry").set("Garbage");

		VoodooUtils.voodoo.log.info("unfoundSelectListOptionExceptionTest() test complete.");
	}

	public void cleanup() throws Exception {}
}
