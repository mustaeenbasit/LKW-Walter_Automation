package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest; 
import com.sugarcrm.candybean.datasource.DataSource;


public class Leads_17189 extends SugarTest {
	DataSource leads;

	@Override
	public void setup() throws Exception {		
		leads = testData.get("Leads_17189");
		
		sugar().login();
		sugar().leads.api.create(leads);
	}

	/**
	 * Verify you can select/unselect checkbox from the Leads module's List View.
	 * @throws Exception
	 */
	@Test
	public void Leads_17189_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running" + testName + "..."); 
			
		sugar().leads.navToListView();
		sugar().leads.listView.toggleSelectAll();
		VoodooUtils.pause(500);

		sugar().leads.listView.getControl("selectedRecordsAlert").assertContains("You have selected all 20 records in this view.", true);
		sugar().leads.listView.getControl("selectedRecordsAlert").assertContains("Select all records", true);
		sugar().leads.listView.getControl("selectedRecordsAlert").assertContains("in the result set.", true);
		
		sugar().leads.listView.toggleSelectAll();
		new VoodooControl("tr", "css", ".alert.alert-warning.hide").assertExists(true);		

		VoodooUtils.voodoo.log.info(testName + "complete.");
	} 
	
	@Override
	public void cleanup() throws Exception {}	
}
