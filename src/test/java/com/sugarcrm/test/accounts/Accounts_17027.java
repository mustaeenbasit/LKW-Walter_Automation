package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_17027 extends SugarTest {
	DataSource accountsData;
		
	public void setup() throws Exception {
		accountsData = testData.get("Accounts_17027");
		
		sugar().login();
		sugar().accounts.api.create(accountsData.get(0));
		sugar().accounts.api.create(accountsData.get(1));
		sugar().accounts.api.create(accountsData.get(2));
	}

	/**
	 * 17027 Verify preview panel pagination
	 * @throws Exception
	 */
	@Test
	public void Accounts_17027_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();
		
		sugar().accounts.listView.previewRecord(2);
		
		// TODO VOOD-803
		// It should be added to PreviewPane.java
		sugar().previewPane.addControl("nextRecordLink", "i", "css", ".fa.fa-chevron-right");
		sugar().previewPane.addControl("previousRecordLink", "i", "css", ".fa.fa-chevron-left");
		
		// Verify that the preview panel displays a left and right arrow.
		sugar().previewPane.getControl("nextRecordLink").assertExists(true);
		sugar().previewPane.getControl("previousRecordLink").assertExists(true);
		
		// TODO VOOD-807
		// Verify the row of the record is highlighted.		
		new VoodooControl("tr", "css", ".single.current.highlighted:nth-child(2)").assertExists(true);
		
		// Click the right arrow.
		// Verify that the next record in the list view is now highlighted 
		// and the preview panel now displays data from the new record
		sugar().previewPane.getControl("nextRecordLink").click();
		
		// TODO VOOD-807
		new VoodooControl("tr", "css", ".single.current.highlighted:nth-child(3)").assertExists(true);
		
		// verifyPreview() method can be used here from pull request https:// github.com/sugarcrm/VoodooGrimoire/pull/550
		for(String controlname : accountsData.get(0).keySet())
			sugar().previewPane.getPreviewPaneField(controlname)
				.assertContains(accountsData.get(0).get(controlname), true);
		
		// Click the left arrow
		// The previous record is again highlighted
		// and the preview panel now displays the previous data.
		sugar().previewPane.getControl("previousRecordLink").click();
		
		// TODO VOOD-807
		new VoodooControl("tr", "css", ".single.current.highlighted:nth-child(2)").assertExists(true);
		
		for(String controlname : accountsData.get(1).keySet())
			sugar().previewPane.getPreviewPaneField(controlname)
				.assertContains(accountsData.get(1).get(controlname), true);
		
		// Click the left arrow once again.
		// The record is highlighted and the proper data is displayed on the preview panel.
		sugar().previewPane.getControl("previousRecordLink").click();
		
		// TODO VOOD-807
		new VoodooControl("tr", "css", ".single.current.highlighted:nth-child(1)").assertExists(true);
		
		for(String controlname : accountsData.get(2).keySet())
			sugar().previewPane.getPreviewPaneField(controlname)
				.assertContains(accountsData.get(2).get(controlname), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}