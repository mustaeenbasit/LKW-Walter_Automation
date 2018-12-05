package com.sugarcrm.test.contacts;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Contacts_24107 extends SugarTest {
	FieldSet verifyMerge = new FieldSet();

	public void setup() throws Exception {
		DataSource contacts = testData.get(testName);
		sugar().contacts.api.create(contacts);
		verifyMerge = testData.get(testName +"_Merged").get(0);
		sugar().login();
	}

	/**
	 * Verify that records selected from Contacts list view can be merged.
	 * @throws Exception
	 */
	@Test
	public void Contacts_24107_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running" + testName + "...");

		// Verifying that Two records are available before Merge
		sugar().contacts.navToListView();
		Assert.assertEquals("Total No. of records not equals two", 2, sugar().contacts.listView.countRows());

		// Merging the Records
		// TODO VOOD-681
		sugar().contacts.listView.toggleSelectAll();
		sugar().contacts.listView.openActionDropdown();
		new VoodooControl("a", "css", ".fld_merge_button.list").click();
		new VoodooControl("input", "css", ".fld_first_name.edit input")
		.set(verifyMerge.get("newFirstName"));
		new VoodooControl("input", "css", ".fld_last_name.edit input")
		.set(verifyMerge.get("newLastName"));
		new VoodooControl("a", "css",
				".fld_save_button.merge-duplicates-headerpane a").click();

		// Confirming the Warning pop-up
		sugar().alerts.getWarning().confirmAlert();

		// Verifying that Merged Record is only displayed
		// TODO: VOOD-697
		Assert.assertEquals("Total No. of records not equals One", 1, sugar().contacts.listView.countRows());
		sugar().contacts.listView.verifyField(1, "fullName", verifyMerge.get("newFullName"));

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
