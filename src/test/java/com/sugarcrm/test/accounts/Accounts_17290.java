package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_17290 extends SugarTest {
	protected AccountRecord myAccount0,myAccount1,myAccount2,myAccount3;

	public void setup() throws Exception {
		// create accounts - calling 4 creates separately to preserve order in which accounts are created 
		myAccount0 = (AccountRecord)sugar().accounts.api.create(testData.get(testName).get(0)); // acc1
		myAccount1 = (AccountRecord)sugar().accounts.api.create(testData.get(testName).get(1)); // acc2
		myAccount2 = (AccountRecord)sugar().accounts.api.create(testData.get(testName).get(2)); // acc3
		myAccount3 = (AccountRecord)sugar().accounts.api.create(testData.get(testName).get(3)); // acc4
		sugar().login();
	}

	/**
	 * Verify Next/Previous buttons should be displayed on the preview header
	 * when more than one record selected
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17290_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();
		sugar().accounts.listView.toggleFavorite(1);
		sugar().accounts.listView.toggleFavorite(2);
		sugar().accounts.listView.toggleFavorite(3);
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterMyFavorites();
		sugar().alerts.waitForLoadingExpiration();

		for (int i = 1; i <= 3; i++) {
			sugar().accounts.listView.getControl(
					"favoriteStar" + String.format("%02d", i)).assertAttribute(
							"class", "active");
		}

		new VoodooControl("table", "css",
				".flex-list-view-content table.table-striped.dataTable")
		.waitForVisible();
		new VoodooControl("table", "css",
				".flex-list-view-content table.table-striped.dataTable")
		.assertElementContains(myAccount0.getRecordIdentifier(),
				false);
		sugar().accounts.listView.previewRecord(1);

		// TODO VOOD-511 need lib support for preview pane navigation
		VoodooControl next = new VoodooControl("i", "css",
				"div[data-voodoo-name='preview-header'] i.fa-chevron-right");
		VoodooControl previous = new VoodooControl("i", "css",
				"div[data-voodoo-name='preview-header'] i.fa-chevron-left");
		next.assertVisible(true);
		previous.assertVisible(true);
		VoodooControl prePane = new VoodooControl("div", "css",
				"div[data-voodoo-name='preview']");
		prePane.assertContains(myAccount3.getRecordIdentifier(), true);
		prePane.assertContains(myAccount3.get("workPhone"), true);
		next.click();
		sugar().alerts.waitForLoadingExpiration();
		prePane.assertContains(myAccount2.getRecordIdentifier(), true);
		prePane.assertContains(myAccount2.get("workPhone"), true);
		previous.click();
		sugar().alerts.waitForLoadingExpiration();
		prePane.assertContains(myAccount3.getRecordIdentifier(), true);
		prePane.assertContains(myAccount3.get("workPhone"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}