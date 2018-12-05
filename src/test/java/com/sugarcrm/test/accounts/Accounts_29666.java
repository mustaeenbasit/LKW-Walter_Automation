package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_29666 extends SugarTest {
	AccountRecord myAccRecord;

	public void setup() throws Exception {
		myAccRecord = (AccountRecord) sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify Account name link available in the preview section is clickable.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_29666_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts listView
		sugar().accounts.navToListView();

		// Click on the preview icon of any account record.
		sugar().accounts.listView.previewRecord(1);

		// Verify Account record in PreviewPane
		myAccRecord.verifyPreview();

		// In preview section, click on the Account's name link. 
		sugar().previewPane.getPreviewPaneField("name").click();

		// Verify that it should be re-directed to the respective Account's record view.
		sugar().accounts.recordView.getDetailField("name").assertContains(myAccRecord.get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}