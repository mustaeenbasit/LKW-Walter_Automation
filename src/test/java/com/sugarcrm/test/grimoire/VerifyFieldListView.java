package com.sugarcrm.test.grimoire;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;

public class VerifyFieldListView extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
		sugar().accounts.api.create();

		FieldSet secondAccount = new FieldSet();
		secondAccount.put("name", "Second Account");
		secondAccount.put("billingAddressCity", "Mountain View");
		sugar().accounts.api.create(secondAccount);
	}

	@Test
	public void verifyField() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyField()...");

		// Check verifyField method for listView works
		sugar().accounts.navToListView();

		sugar().accounts.listView.verifyField(1, "name", "Second Account");
		sugar().accounts.listView.verifyField(1, "billingAddressCity", "Mountain View");
		sugar().accounts.listView.verifyField(2, "name", "Aperture Laboratories");
		sugar().accounts.listView.verifyField(2, "billingAddressCity", "Cupertino");

		VoodooUtils.voodoo.log.info("verifyField() complete.");
	}

	public void cleanup() throws Exception {}
}