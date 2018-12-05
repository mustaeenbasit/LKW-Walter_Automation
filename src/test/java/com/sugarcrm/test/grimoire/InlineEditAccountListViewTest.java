package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

public class InlineEditAccountListViewTest extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	@Test
	public void verifyInlineEditAccount() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInlineEditAccount()...");

		sugar().accounts.navToListView();

		FieldSet data = new FieldSet();
		data.put("name", "Edited Name");
		data.put("workPhone", "408.454.9600");

		// Inline Edit the record and save
		sugar().accounts.listView.updateRecord(1, data);

		// Verify Record updated
		sugar().accounts.listView.verifyField(1, "name", data.get("name"));
		sugar().accounts.listView.verifyField(1, "workPhone", data.get("workPhone"));

		VoodooUtils.voodoo.log.info("verifyInlineEditAccount() complete.");
	}

	public void cleanup() throws Exception {}
}