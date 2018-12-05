package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20208 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify list view column name sync with updating module name
	 * @throws Exception
	 */
	@Test
	public void Admin_20208_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Update Accounts module name, set "Singular Label" as "Acc" and set "Plural Label" as "Accs"
		sugar().admin.renameModule(sugar().accounts, customData.get("singularLabel"), customData.get("pluralLabel"));
		
		// Go to Contacts module and verify list view column title
		sugar().contacts.navToListView();
		sugar().contacts.listView.assertElementContains(customData.get("acc_name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
