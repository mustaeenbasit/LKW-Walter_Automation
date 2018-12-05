package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_17032 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify "Search for more" brings up selection view.
	 * @throws Exception
	 */
	@Test
	public void Accounts_17032_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Contacts List View.
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.showMore();

		// Hover on "Assigned To" field
		sugar().accounts.recordView.getDetailField("relAssignedTo").hover();

		// Click Pencil icon
		// TODO: VOOD-854
		new VoodooControl("i", "css", "[data-name='assigned_user_name'] .fa.fa-pencil").click();
		VoodooUtils.waitForReady();

		VoodooControl assignedToEditCtrl = sugar().accounts.recordView.getEditField("relAssignedTo");
		VoodooSelect relatedAssignedTo = (VoodooSelect)assignedToEditCtrl;
		// Click "Search and Select"
		relatedAssignedTo.clickSearchForMore();
		DataSource customData = testData.get(testName);

		// Verify drawer appears displaying a radio button next to each item
		for (int i = 1; i <= customData.size(); i++) {
			sugar().users.searchSelect.getControl(String.format("selectInput%02d", i)).assertVisible(true);
		}

		// Verify drawer appears displaying the available options
		for (int i = 0; i < customData.size(); i++) {
			sugar().users.searchSelect.assertContains(customData.get(i).get("username"), true);
		}

		// Need to sort before selecting record on search select drawer to verify selected record.
		// TODO: VOOD-1067
		new VoodooControl("th", "css", ".sorting.orderByname").click();
		VoodooUtils.waitForReady();

		// Select one item from the list
		sugar().users.searchSelect.selectRecord(1);
		VoodooUtils.waitForReady();

		// Verify user returns back to original window displaying the proper selection.
		assignedToEditCtrl.assertEquals(customData.get(1).get("username"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}