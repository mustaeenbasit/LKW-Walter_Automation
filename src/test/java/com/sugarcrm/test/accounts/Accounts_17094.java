package com.sugarcrm.test.accounts;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;

public class Accounts_17094 extends SugarTest {
	AccountRecord account1;
	DataSource ds;

	public void setup() throws Exception {
		sugar().login();
		account1 = (AccountRecord) sugar().accounts.api.create();
		ds = testData.get(testName);
		account1.navToRecord();
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getEditField("description").set(ds.get(0).get("des"));
		sugar().accounts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Verify using "...more" and "...less" to hide and display the contents in
	 * the OOTB text area fields in preview
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17094_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();
		sugar().accounts.listView.previewRecord(01);

		sugar().previewPane.setModule(sugar().accounts);
		sugar().previewPane.showMore();
		sugar().previewPane.getPreviewPaneField("description").assertContains(ds.get(0).get("des1"), true);

		sugar().previewPane.showLess();
		assertFalse("Description field in preview is still visible", sugar().previewPane.getPreviewPaneField("description").queryVisible());
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
