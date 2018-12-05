package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest; 
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.LeadRecord; 

public class Leads_17188 extends SugarTest {
	DataSource leads;
	FieldSet firstLead;
	
	@Override
	public void setup() throws Exception {				
		leads = testData.get("Leads_17188");
		firstLead = leads.get(0);		
		sugar().login();
	}

	/**
	 * Verify Preview can preview a Leads record from the Leads module's List View.
	 * @throws Exception
	 */	
	@Test
	public void Leads_17188_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running" + testName + "..."); 
	
		LeadRecord myFirstLead = (LeadRecord)sugar().leads.api.create(firstLead);
		myFirstLead.verify(firstLead);
		
		sugar().leads.navToListView();
		// Open preview to look at
		sugar().leads.listView.previewRecord(1);
		String firstLastname = myFirstLead.get("lastName");

		// Verify lastname of the Leads record is correctly displayed in preview panel.
		sugar().previewPane.getPreviewPaneField("fullName").assertContains(firstLastname, true);
		
		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}
	
	@Override
	public void cleanup() throws Exception {}
}
