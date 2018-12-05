package com.sugarcrm.test.ListView;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;

public class ListView_17022 extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		myAccount = (AccountRecord)sugar.accounts.api.create();
		sugar.login();
	}			
	/**
	 * Verify preview panel
	 */
	@Test
	public void ListView_17022_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.accounts.navToListView();
		sugar.accounts.listView.previewRecord(1);
	
		sugar.previewPane.getPreviewPaneField("name").assertEquals(myAccount.get("name"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
