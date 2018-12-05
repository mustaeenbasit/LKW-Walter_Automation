package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest; 

import com.sugarcrm.sugar.records.LeadRecord; 

public class Leads_18018 extends SugarTest {
	LeadRecord testLead; 
	FieldSet firstEditLead;
	
	@Override
	public void setup() throws Exception {
		firstEditLead = testData.get("Leads_18018").get(0);
		sugar().login();
		testLead = (LeadRecord)sugar().leads.api.create();
	}

	/**
	 * Cancel editing a Leads record from the Leads module's List View via cancel button.
	 * @throws Exception
	 */
	@Test
	public void Leads_18018_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		//Read value from csv file that intent to change
		String editLastName = firstEditLead.get("lastName");
		String editFirstName = firstEditLead.get("firstName");
		
		String originalLastname = testLead.get("firstName");
		String originalFirstname = testLead.get("lastName");
		sugar().leads.navToListView();
		sugar().leads.listView.editRecord(1);
		// TODO : VOOD-586 is reported to support.  Once it is fixed, please replace the following VoodooControl calls.
		new VoodooControl("input", "css", ".fld_first_name.edit input").set(editFirstName);
		new VoodooControl("input", "css", ".fld_last_name.edit input").set(editLastName);
		sugar().leads.listView.cancelRecord(1);
		
		// Open preview to look at lastName and firstName are same as original value
		sugar().leads.navToListView();
		sugar().leads.listView.previewRecord(1);
		new VoodooControl("div", "css", ".fld_full_name.detail").assertContains(originalFirstname, true);
		new VoodooControl("div", "css", ".fld_full_name.detail").assertContains(originalLastname, true);
		
		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}
		
	@Override
	public void cleanup() throws Exception {}
}