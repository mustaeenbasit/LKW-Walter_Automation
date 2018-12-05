package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Leads_28448 extends SugarTest {
	FieldSet leadsData = new FieldSet();
	FieldSet fs = new FieldSet();
	LeadRecord myLead;

	public void setup() throws Exception {
		leadsData = testData.get(testName).get(0);
		myLead = (LeadRecord) sugar().leads.api.create();

		// Login as an qauser
		sugar().login();
		
		myLead.navToRecord();
		sugar().leads.recordView.edit();
		sugar().leads.recordView.getEditField("firstName").set(leadsData.get("firstName"));
		sugar().leads.recordView.getEditField("lastName").set(leadsData.get("lastName"));
		sugar().leads.recordView.save();

		sugar().logout();
	}
	/**
	 * Verify that Lead icon and name are displaying correct when running global search
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_28448_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as an qauser
		sugar().login(sugar().users.getQAUser());

		try {
			// Change Name Display Format of qauser
			fs.put("advanced_nameFormat", leadsData.get("newNameFormat"));
			sugar().users.setPrefs(fs);
			
			// Set Global search
			sugar().navbar.setGlobalSearch(leadsData.get("lastName").substring(4));

			// Verify Global search result
			new VoodooControl("span", "css", "ul.dropdown-menu.search-results > li:first-child.search-result a div span.label-Leads").assertVisible(true);
			new VoodooControl("span", "css", "ul.dropdown-menu.search-results > li:first-child.search-result a div span.label-Leads").assertContains("Le", true);
			new VoodooControl("span", "css", "ul.dropdown-menu.search-results > li:first-child.search-result a div h3").assertContains(leadsData.get("fullName"), true);
		}
		finally {
			// Restore Name Display Format of qauser
			fs.clear();
			fs.put("advanced_nameFormat", leadsData.get("oldNameFormat"));
			sugar().users.setPrefs(fs);
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}