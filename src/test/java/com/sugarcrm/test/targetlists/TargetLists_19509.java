package com.sugarcrm.test.targetlists;

import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_19509 extends SugarTest{
	ArrayList<Record> leadRecords;
	FieldSet maxEntries;
	FieldSet fs;

	public void setup() throws Exception {
		maxEntries = testData.get(testName+"_systemSetting").get(0);
		DataSource leads = testData.get(testName);
		leadRecords = sugar.leads.api.create(leads);
		sugar.targetlists.api.create();
		sugar.login();	

		// Changing subpanel Items per page in Admin >> System Settings to '2'
		fs = new FieldSet();
		fs.put("maxEntriesPerSubPanel", maxEntries.get("maxEntriesPerPage2"));
		sugar.admin.setSystemSettings(fs);
		fs.clear();
		VoodooUtils.waitForReady();
	}
	/**
	 * Target List - Verify that the "More" functionality in the "Leads" sub-panel works correctly.
	 * @throws Exception
	 */
	@Test
	public void TargetLists_19509_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Nav to target list record view and add a leads records to subpanel
		sugar.targetlists.navToListView();
		sugar.targetlists.listView.clickRecord(1);
		StandardSubpanel leadsSubpanel = sugar.targetlists.recordView.subpanels
				.get(sugar.leads.moduleNamePlural);
		leadsSubpanel.scrollIntoViewIfNeeded(false);
		leadsSubpanel.linkExistingRecords(leadRecords);

		// Asserting the no. of records default present in leads subpanel
		Assert.assertTrue("No. of Records in leads subpanel is not equal to '2'", 
				leadsSubpanel.countRows() == 2 );

		// Asserting the no. of records present in leads subpanel
		leadsSubpanel.showMore();
		VoodooUtils.waitForReady();
		Assert.assertTrue("No. of Records in leads subpanel is not equal to '4' ", 
				leadsSubpanel.countRows() == 4 );

		// Asserting the no. of records present in leads subpanel and non-existence of
		//'More' link after second click on 'More' link
		leadsSubpanel.showMore();
		VoodooUtils.waitForReady();
		leadsSubpanel.getControl("moreLink").assertVisible(false);
		Assert.assertTrue("No. of Records in leads subpanel is not equal to '5' ",
				leadsSubpanel.countRows() == 5 );

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}