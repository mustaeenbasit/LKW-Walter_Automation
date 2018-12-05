package com.sugarcrm.test.contacts;

import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_23699 extends SugarTest {
	StandardSubpanel directReportsSubpanel;
	DataSource directReportsRecords = new DataSource();

	public void setup() throws Exception {
		directReportsRecords = testData.get(testName);
		ArrayList<Record> mydirectReports = sugar().contacts.api.create(directReportsRecords);
		sugar().contacts.api.create();

		// Login to sugar
		sugar().login();

		// Go to a Contact record view and link 8 directReports record 
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		directReportsSubpanel = sugar().contacts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		directReportsSubpanel.linkExistingRecords(mydirectReports);
	}

	/**
	 *  Paginate directReports_Verify that corresponding directReports records' list view is displayed after clicking the pagination control link on "directReports" sub-panel of a contact record detail view.
	 * @throws Exception
	 */
	@Test
	public void Contacts_23699_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify that 5 Records displaying before click on show more link in directReports subpanel
		int rowsInFirstView = directReportsSubpanel.countRows();
		Assert.assertTrue("Row count in subpanel is not equal to 5", rowsInFirstView == 5);
		
		// Sorting Needed for assertion
		directReportsSubpanel.sortBy("headerFullname", false);
		VoodooUtils.waitForReady();
		
		for (int i = directReportsRecords.size()-1, j = 1; j <= rowsInFirstView; i--, j++) {
			directReportsSubpanel.getDetailField(j, "fullName").assertContains(directReportsRecords.get(i).get("lastName") , true);
		}

		// Click on show more link
		directReportsSubpanel.showMore();
		VoodooUtils.waitForReady();

		// Verify that rest 3 records also display after clicking "show more" link
		int rowsInSecondView = directReportsSubpanel.countRows();

		System.out.println("  rowsInSecondView = " + rowsInSecondView);
		Assert.assertTrue("Row count in subpanel is not equal to 8", rowsInSecondView == 8);
		for (int i = directReportsRecords.size()-1, j = 1; j <= rowsInSecondView; i--, j++) {
			directReportsSubpanel.getDetailField(j, "fullName").assertContains(directReportsRecords.get(i).get("lastName"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}