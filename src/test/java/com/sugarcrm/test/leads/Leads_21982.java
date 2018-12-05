package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_21982 extends SugarTest {
	DataSource leadsData;
		
	public void setup() throws Exception {
		sugar().login();
		
		leadsData = testData.get("Leads_21982");
		sugar().leads.api.create(leadsData);
	}

	/**
	 * 21982 Verify that leads with special characters can be searched
	 * @throws Exception
	 */
	@Test
	public void Leads_21982_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.navToListView();
		
		String leadName;
		for(int i=0; i<leadsData.size(); i++){
			leadName = leadsData.get(i).get("firstName");
			sugar().leads.listView.setSearchString(leadName);
			
			// TODO VOOD-959 verifyField() does not work for Leads
			// sugar().leads.listView.verifyField(1, "firstName", leadName);
			new VoodooControl("tr", "css", "div[data-voodoo-name='recordlist'] tbody tr:nth-of-type(1)" ).assertContains(leadName, true);
		}
		sugar().leads.listView.setSearchString("");
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}