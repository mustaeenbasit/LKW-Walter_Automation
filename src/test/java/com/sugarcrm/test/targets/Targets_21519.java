package com.sugarcrm.test.targets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Targets_21519 extends SugarTest {
	DataSource targetRecords;

	public void setup() throws Exception {
		targetRecords = testData.get(testName);
		sugar().targets.api.create(targetRecords);
		sugar().login();
	}

	/**
	 * Verify Targets list view can be sorted accordingly.
	 *
	 * @throws Exception
	 */
	@Test
	public void Targets_21519_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().targets.navToListView();

		// Sorting by Name.
		// Click the 'name' column to sort (descending order).
		//nameField.click();
		sugar().targets.listView.sortBy("headerFullname", false);
		sugar().alerts.waitForLoadingExpiration();

		// Verify columns are in descending order.
		sugar().targets.listView.getDetailField(1, "fullName").assertContains(targetRecords.get(3).get("lastName"), true);
		sugar().targets.listView.getDetailField(1, "title").assertContains(targetRecords.get(3).get("title"), true);
		sugar().targets.listView.getDetailField(1, "phoneWork").assertContains(targetRecords.get(3).get("phoneWork"), true);

		// Click the 'name' column to sort (ascending order).
		sugar().targets.listView.sortBy("headerFullname", true);
		sugar().alerts.waitForLoadingExpiration();

		// Verify columns are in ascending order.
		sugar().targets.listView.getDetailField(1, "fullName").assertContains(targetRecords.get(0).get("lastName"), true);
		sugar().targets.listView.getDetailField(1, "title").assertContains(targetRecords.get(0).get("title"), true);
		sugar().targets.listView.getDetailField(1, "phoneWork").assertContains(targetRecords.get(0).get("phoneWork"), true);

		// Sorting by Title.
		// Click the 'title' column to sort (descending order).
		sugar().targets.listView.sortBy("headerTitle", false);
		sugar().alerts.waitForLoadingExpiration();

		// Verify columns are in descending order.
		sugar().targets.listView.getDetailField(1, "fullName").assertContains(targetRecords.get(3).get("lastName"), true);
		sugar().targets.listView.getDetailField(1, "title").assertContains(targetRecords.get(3).get("title"), true);
		sugar().targets.listView.getDetailField(1, "phoneWork").assertContains(targetRecords.get(3).get("phoneWork"), true);

		// Click the 'title' column to sort (ascending order).
		sugar().targets.listView.sortBy("headerTitle", true);

		// Verify columns are in ascending order.
		sugar().targets.listView.getDetailField(1, "fullName").assertContains(targetRecords.get(0).get("lastName"), true);
		sugar().targets.listView.getDetailField(1, "title").assertContains(targetRecords.get(0).get("title"), true);
		sugar().targets.listView.getDetailField(1, "phoneWork").assertContains(targetRecords.get(0).get("phoneWork"), true);

		// Sorting by Phone.
		// Click the 'phone' column to sort (descending order).
		sugar().targets.listView.sortBy("headerPhonework", false);

		// Verify columns are in descending order.
		sugar().targets.listView.getDetailField(1, "fullName").assertContains(targetRecords.get(3).get("lastName"), true);
		sugar().targets.listView.getDetailField(1, "title").assertContains(targetRecords.get(3).get("title"), true);
		sugar().targets.listView.getDetailField(1, "phoneWork").assertContains(targetRecords.get(3).get("phoneWork"), true);

		// Click the 'phone' column to sort (ascending order).
		sugar().targets.listView.sortBy("headerPhonework", true);

		// Verify columns are in ascending order.
		sugar().targets.listView.getDetailField(1, "fullName").assertContains(targetRecords.get(0).get("lastName"), true);
		sugar().targets.listView.getDetailField(1, "title").assertContains(targetRecords.get(0).get("title"), true);
		sugar().targets.listView.getDetailField(1, "phoneWork").assertContains(targetRecords.get(0).get("phoneWork"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
