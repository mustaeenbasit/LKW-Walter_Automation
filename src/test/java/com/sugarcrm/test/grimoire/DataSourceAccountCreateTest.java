package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class DataSourceAccountCreateTest extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void verifyAccountRecordsViaDS() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyAccountRecordsViaDS()...");

		FieldSet account1 = new FieldSet();
		account1.put("name", "Account 1");

		FieldSet account2 = new FieldSet();
		account2.put("name", "Account 2");

		DataSource data = new DataSource();
		data.add(account1);
		data.add(account2);

		sugar().accounts.create(data);

		// Verify account records
		sugar().accounts.listView.verifyField(1, "name", "Account 2");
		sugar().accounts.listView.verifyField(2, "name", "Account 1");

		VoodooUtils.voodoo.log.info("verifyAccountRecordsViaDS() complete.");
	}

	public void cleanup() throws Exception {}
}