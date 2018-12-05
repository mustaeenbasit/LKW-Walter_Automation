package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Leads_21847 extends SugarTest {
	FieldSet leadDefaultData;

	public void setup() throws Exception {
		leadDefaultData = sugar().leads.getDefaultData();
		sugar().login();
	}

	/**
	 * Create Lead_Verify that lead can be created using quick create
	 */
	@Test
	public void Leads_21847_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Open QuickCreate and click "Create Lead"
		sugar().navbar.quickCreateAction(sugar().leads.moduleNamePlural);
		
		// Set required fields
		sugar().leads.createDrawer.showMore();
		sugar().leads.createDrawer.setFields(leadDefaultData);
		sugar().leads.createDrawer.save();
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify that user is returned to Home module after lead is created
		sugar().home.dashboard.assertContains("My Dashboard", true);

		// Verify a lead record was created
		LeadRecord myLead = new LeadRecord(leadDefaultData);
		myLead.verify(leadDefaultData);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
