package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_21851 extends SugarTest {
	DataSource leadsDS = new DataSource();
	public void setup() throws Exception {
		 leadsDS = testData.get(testName);

		// Create three Leads record
		sugar().leads.api.create(leadsDS);
		
		// Login as Admin
		sugar().login();
	}

	/**
	 * Mass Update Leads_Verify that the selected leads can be deleted by using "Mass Update".
	 * @throws Exception
	 */
	@Test
	public void Leads_21851_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Leads listView
		sugar().leads.navToListView();

		// Check the check-box in front of the leads records to be selected in list view
		for (int i = 0; i < leadsDS.size(); i++) {
			sugar().leads.listView.checkRecord(i+1);
		}

		// Select "Delete" button and confirm action on the popup menu
		sugar().leads.listView.openActionDropdown();
		sugar().leads.listView.delete();
		sugar().leads.listView.confirmDelete();

		// Verify no leads records are displayed in leads list view
		sugar().leads.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}