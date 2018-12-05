package com.sugarcrm.test.contacts;

import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_23697 extends SugarTest {
	StandardSubpanel bugsSubpanel;
	DataSource bugsRecords = new DataSource();

	public void setup() throws Exception {
		bugsRecords = testData.get(testName);
		ArrayList<Record> mybugs = sugar().bugs.api.create(bugsRecords);
		sugar().contacts.api.create();

		// Login to sugar
		sugar().login();

		// Enabling bugs module and subpanel
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);

		// Go to a Contact record view and link 8 bugs record 
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		bugsSubpanel = sugar().contacts.recordView.subpanels.get(sugar().bugs.moduleNamePlural);
		bugsSubpanel.linkExistingRecords(mybugs);
	}

	/**
	 *  Paginate Bugs_Verify that corresponding bugs records' list view is displayed after clicking the pagination control link on "Bugs" sub-panel of a contact record detail view.
	 * @throws Exception
	 */
	@Test
	public void Contacts_23697_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify that 5 Records displaying before click on show more link in bugs subpanel
		int rowsInFirstView = bugsSubpanel.countRows();
		Assert.assertTrue("Row count in subpanel is not equal to 5", rowsInFirstView == 5);
		
		// Sorting Needed for assertion
		bugsSubpanel.sortBy("headerName", false);
		VoodooUtils.waitForReady();
		
		for (int i = bugsRecords.size()-1, j = 1; j <= rowsInFirstView; i--, j++) {
			bugsSubpanel.getDetailField(j, "name").assertEquals(bugsRecords.get(i).get("name"), true);
		}

		// Click on show more link
		bugsSubpanel.showMore();
		VoodooUtils.waitForReady();

		// Verify that rest 3 records also display after clicking "show more" link
		int rowsInSecondView = bugsSubpanel.countRows();
		Assert.assertTrue("Row count in subpanel is not equal to 8", rowsInSecondView == 8);
		for (int i = bugsRecords.size()-1, j = 1; j <= rowsInSecondView; i--, j++) {
			bugsSubpanel.getDetailField(j, "name").assertEquals(bugsRecords.get(i).get("name"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}