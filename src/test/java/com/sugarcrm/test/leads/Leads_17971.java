package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Leads_17971 extends SugarTest {
	DataSource contactData = new DataSource();
	LeadRecord myLead;
	VoodooControl studio, leadModuleCtrl;

	public void setup() throws Exception {
		myLead = (LeadRecord)sugar().leads.api.create();
		contactData = testData.get(testName);
		sugar().contacts.api.create(contactData);
		studio = sugar().admin.adminTools.getControl("studio");
		sugar().login();	

		// Add a Many-to-Many relationship Leads-Contacts 
		// TODO: VOOD-1505
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studio.click();
		VoodooUtils.waitForReady();
		leadModuleCtrl = new VoodooControl("a", "id", "studiolink_Leads");
		leadModuleCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "relationshipsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='addrelbtn']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "id", "rhs_mod_field").set(sugar().contacts.moduleNamePlural);
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[name=saverelbtn]").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
	}

	/**
	 * Link existing record in Leads' sub panel 
	 * @throws Exception
	 */
	@Test
	public void Leads_17971_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");				

		myLead.navToRecord();
		// Select Contacts in Related field. 
		sugar().leads.recordView.setRelatedSubpanelFilter(sugar().contacts.moduleNamePlural);

		// Select "Link Existing Record" at each module, such as Contacts module.
		// TODO: VOOD-1382
		new VoodooControl("span", "css", ".filtered.layout_Contacts .fa.fa-caret-down").click();
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl ("a", "css", "[name='select_button']").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify records are displaying
		for (int i = 0 ; i < contactData.size() ; i++ ) {
			sugar().contacts.searchSelect.assertContains(sugar().contacts.getDefaultData().get("salutation")+" "+sugar().contacts.getDefaultData().get("firstName")+" "+contactData.get(i).get("lastName"), true);
		}
		
		// Select any one in the list by click on radio button for one of the records. 
		sugar().contacts.searchSelect.selectRecord(1);
		sugar().contacts.searchSelect.link();

		// Verify Contact record is listed as a related record in Contacts sub panel. 
		// TODO: VOOD-1382 
		// Click on the Contact record link in the Contacts sub panel in the Lead record view. 
		new VoodooControl("span", "css", ".filtered.layout_Contacts .fld_full_name").click();
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("div", "css", ".filtered.layout_Leads").scrollIntoView();

		// Verify Lead record appears in Leads sub panel in the Contact record view.
		// TODO: VOOD-1036
		new VoodooControl("div", "css", "[data-subpanel-link='leads_contacts_1']").click();
		new VoodooControl("div", "css", "[data-subpanel-link='leads_contacts_1'] .fld_full_name").assertContains(sugar().leads.getDefaultData().get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}