package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_25865 extends SugarTest {
	AccountRecord accountRecord;
	FieldSet accountsData = new FieldSet();

	public void setup() throws Exception {
		accountsData = testData.get(testName).get(0);
		FieldSet accountRecordValues = new FieldSet();
		accountRecordValues.put("name", accountsData.get("name"));
		accountRecordValues.put("description", accountsData.get("name"));
		accountRecord = (AccountRecord)sugar().accounts.api.create(accountRecordValues);

		// Login
		sugar().login();

		// Admin -> Studio -> Accounts -> Fields
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-542 and VOOD-1509
		// Go to Studio -> Accounts
		new VoodooControl("a", "id", "studiolink_Accounts").click();
		VoodooUtils.waitForReady();

		// layout subPanel
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();

		// Enable the Full Text Searchable for Name field, and disable the Full Text Searchable for Description filed
		new VoodooControl("a", "id", "description").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "id", "fts_field_config").set(accountsData.get("disabled"));

		// Save
		new VoodooControl("input", "css", "input[name='fsavebtn']").click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify Global search only works on the fields which global search property is enabled.
	 * @throws Exception
	 */
	@Test
	public void Studio_25865_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: HACK: After save studio fields need to Navigate to any module
		sugar().accounts.navToListView();

		// Define controls for global search 
		VoodooControl globalSearchTextField = sugar().navbar.getControl("globalSearch");
		VoodooControl firstSearchResult = sugar().navbar.search.getControl("searchResults");

		// Search the record by name
		globalSearchTextField.set(accountRecord.getRecordIdentifier());
		VoodooUtils.waitForReady();
		firstSearchResult.assertContains(accountRecord.getRecordIdentifier(), true);

		// Search the record by description
		globalSearchTextField.set(accountsData.get("description"));
		VoodooUtils.waitForReady();

		// Verify that No record searched out.
		firstSearchResult.assertContains(accountRecord.getRecordIdentifier(), false);
		firstSearchResult.assertContains(accountsData.get("noResult"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}