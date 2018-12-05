package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_17053 extends SugarTest {	
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Date Created/Modified group field display format on preview panel
	 * @throws Exception
	 */
	@Test
	public void Accounts_17053_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to accounts module and click preview action of a record in list view
		sugar().accounts.navToListView();
		sugar().accounts.listView.previewRecord(1);

		// Click Show More link to display Date Created/Modified field if needed
		sugar().previewPane.showMore();

		// Verify that the Date Created grouped with Created By, Date Modified grouped with Modified By
		sugar().previewPane.getPreviewPaneField("date_entered_date").assertExists(true);
		sugar().previewPane.getPreviewPaneField("dateEnteredBy").assertExists(true);

		sugar().previewPane.getPreviewPaneField("date_modified_date").assertExists(true);
		sugar().previewPane.getPreviewPaneField("dateModifiedBy").assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}