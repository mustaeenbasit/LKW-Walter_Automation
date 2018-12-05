package com.sugarcrm.test.contacts;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_30637 extends SugarTest {
	DataSource customData = new DataSource();
	StandardSubpanel directReportsSubpanel;

	public void setup() throws Exception {
		customData = testData.get(testName);
		ArrayList<Record> directReportsRecords = sugar().contacts.api.create(customData);
		sugar().login();
		directReportsSubpanel = sugar().contacts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);

		// Navigate to contacts Record View and Link Direct Reports to it.
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		directReportsSubpanel.linkExistingRecords(directReportsRecords);
	}

	/**
	 * Verify that in Direct Reports sub-panel sorting functionality should work correctly.
	 * @throws Exception
	 */
	@Test
	public void Contacts_30637_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Sort Direct Reports records by Name in Descending Order.
		// TODO: VOOD-2053 - Need library support of column headers for pending subpanels
		new VoodooControl("th", "css", ".layout_Contacts .sorting.orderByfull_name").click();
		VoodooUtils.waitForReady();

		// Verify Direct Reports Records are sorted by Name in Descending Order
		directReportsSubpanel.getDetailField(1, "fullName").assertEquals(customData.get(2).get("firstName") + " " + customData.get(2).get("lastName") , true);
		directReportsSubpanel.getDetailField(2, "fullName").assertEquals(customData.get(0).get("firstName") + " " + customData.get(0).get("lastName") , true);
		directReportsSubpanel.getDetailField(3, "fullName").assertEquals(customData.get(1).get("firstName") + " " + customData.get(1).get("lastName") , true);

		// Sort Direct Reports records by Name in Ascending Order.
		new VoodooControl("th", "css", ".layout_Contacts .sorting_desc.orderByfull_name").click();
		VoodooUtils.waitForReady();

		// Verify Direct Reports Records are sorted by Name in Ascending Order
		directReportsSubpanel.getDetailField(1, "fullName").assertEquals(customData.get(1).get("firstName") + " " + customData.get(1).get("lastName") , true);
		directReportsSubpanel.getDetailField(2, "fullName").assertEquals(customData.get(0).get("firstName") + " " + customData.get(0).get("lastName") , true);
		directReportsSubpanel.getDetailField(3, "fullName").assertEquals(customData.get(2).get("firstName") + " " + customData.get(2).get("lastName") , true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}