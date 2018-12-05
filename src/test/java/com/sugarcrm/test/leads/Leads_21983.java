package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
/**
 * @author Shagun Kaushik <skaushik@sugarcrm.com>
 */
public class Leads_21983 extends SugarTest {
	DataSource leadsData;
		
	public void setup() throws Exception {
		leadsData = testData.get(testName);
		sugar().leads.api.create(leadsData);
		sugar().login();
	}

	/**
 	 * Search Leads_Verify that all the leads are displayed when using empty search
 	 * 
 	 * @throws Exception
 	 */
	@Test
	public void Leads_21983_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.navToListView();
		
		String textInSearchField = sugar().leads.listView.getControl("searchFilter").getText();
		
		// verifying that search field is empty 
		Assert.assertSame("Search Field is not empty, when it should", true, textInSearchField.isEmpty());
		
		int count = sugar().leads.listView.countRows();
		
		// verifying that all records saved are displayed
		Assert.assertTrue("Total number of records not found.", count == leadsData.size());
		sugar().leads.listView.sortBy("headerFullname", true);
		for(int i=0; i<leadsData.size(); i++){
			// TODO VOOD-959 verifyField() does not work for Leads
			//sugar().leads.listView.verifyField(1, "firstName", leadName);
			new VoodooControl("a", "css", "div[data-voodoo-name='recordlist'] tbody tr:nth-of-type("+(i+1)+") a" ).assertContains(leadsData.get(i).get("lastName"), true);
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}