package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest; 
import com.sugarcrm.sugar.records.LeadRecord; 

public class Leads_17191 extends SugarTest {
	LeadRecord testLead; 
	
	@Override
	public void setup() throws Exception {
		sugar().login();
		testLead = (LeadRecord)sugar().leads.api.create();
	}

	/**
	 * Verify you can Mark/Unmarking a Leads record as favorite from the Leads module's List View.
	 * @throws Exception
	 */
	@Test
	public void Leads_17191_execute() throws Exception {

		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 
		
		sugar().leads.navToListView();
		
		//Select it as favorite 
		sugar().leads.listView.toggleFavorite(1);
		sugar().leads.listView.getControl("favoriteStar01").assertAttribute("class", "active", true);
		
		//Un-select it as favorite
		sugar().leads.listView.toggleFavorite(1);
		sugar().leads.listView.getControl("favoriteStar01").assertAttribute("class", "active", false);
		
		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	@Override
	public void cleanup() throws Exception {}
}
