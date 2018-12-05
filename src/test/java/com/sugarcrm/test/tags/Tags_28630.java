package com.sugarcrm.test.tags;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Assert;
import org.junit.Test;

public class Tags_28630 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * [Tags] Importing records with tags (with empty and updated)-> verify tags are properly imported
	 * @throws Exception
	 */
	@Test
	public void Tags_28630_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet importRecords = testData.get(testName + "_accounts").get(0);
		FieldSet updatedImportRecords = testData.get(testName + "_updatedAccounts").get(0);

		// Click on Imports Accounts in Accounts module
		sugar.navbar.selectMenuItem(sugar.accounts, "importAccounts");

		// Need to change focus for bwc-frame
		VoodooUtils.focusFrame("bwc-frame");

		// Create Account record with empty tag name by importing the csv file
		VoodooFileField browseToImport = new VoodooFileField("input", "id", "userfile");

		// TODO: VOOD-1396 - Need Controls for the Import Tasks functionality
		VoodooControl nextButton = new VoodooControl("input", "id", "gonext");
		nextButton.click();
		browseToImport.set("src/test/resources/data/" + testName + "_accounts.csv");
		
		// Need to click next button for importing
		nextButton.click();
		nextButton.click();
		nextButton.click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "importnow").click();
		VoodooUtils.focusDefault();

		// Navigating to Created Account record
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);

		// Verifying Tag field is empty in Created Account
		sugar.accounts.recordView.getDetailField("tags").assertEquals(importRecords.get("Tags"), true);

		// Click on Imports Accounts in Accounts module
		sugar.navbar.selectMenuItem(sugar.accounts, "importAccounts");

		// Need to change focus for bwc-frame
		VoodooUtils.focusFrame("bwc-frame");

		// Create Account record with tag value by importing the csv file
		nextButton.click();
		browseToImport.set("src/test/resources/data/" + testName + "_updatedAccounts.csv");
		new VoodooControl("input", "id", "import_update").click();
		nextButton.click();
		VoodooUtils.acceptDialog();
		nextButton.click();
		nextButton.click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "importnow").click();

		// Need focus to default 
		VoodooUtils.focusDefault();

		// Navigating to Created Account record
		sugar.accounts.navToListView();
		
		// Verifying Account is only updated not create new duplicate account.
		Assert.assertEquals("Asserting 1 record in list view FAILED", 1, sugar.accounts.listView.countRows());
		sugar.accounts.listView.clickRecord(1);

		// Verifying Tag field is updated in Created Account
		sugar.accounts.recordView.getDetailField("tags").assertEquals(updatedImportRecords.get("Tags"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}