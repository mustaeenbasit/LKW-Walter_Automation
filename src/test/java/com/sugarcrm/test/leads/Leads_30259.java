package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_30259 extends SugarTest {

	public void setup() throws Exception {
		// Initialize test data
		sugar().leads.api.create();
		
		// Login as admin
		sugar().login();
	}

	/**
	 * Verify that during Lead convert user can change "Status" and "Lead Source" for the Contact
	 * @throws Exception
	 */
	@Test
	public void Leads_30259_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Create custom data for leads
		FieldSet leadsFS = testData.get(testName).get(0);
		
		// Navigate to leads record view and convert the Lead
		// TODO: VOOD-585 - Need to have method (library support) to define Convert function in Leads
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", ".fld_lead_convert_button.detail a").click();
		VoodooUtils.waitForReady();
		
		// Expanding Contacts panel to change the "Department" and "Lead Source" to different value in Contact session
		new VoodooControl("i", "css", "[data-module='Contacts'] .fa-chevron-down").click();
		VoodooUtils.waitForReady();
		
		// Editing the Department Name in Contacts
		new VoodooControl("input", "css", "[name='department']").set(testName);
		
		// Click on more link in record view
		new VoodooControl("button", "css", "#collapseContacts button.btn-link.btn-invisible.more").click();
		VoodooUtils.waitForReady();
		
		// Editing the Lead Source in Contacts
		new VoodooSelect("span", "css", "[class='fld_lead_source edit'] .select2-arrow").set(leadsFS.get("leadSource"));
		
		// Expanding Accounts panel to set account name
		new VoodooControl("i", "css", "[data-module='Accounts'] .fa-chevron-down").click();
		VoodooUtils.waitForReady();
		
		// Editing the name in Accounts
		new VoodooControl("input", "css", "[name='name']").set(testName);
		
		// Clicking 'Create Account' button
		new VoodooControl("a", "css", "[data-module='Accounts'] [name='associate_button']").click();
		VoodooUtils.waitForReady();
		
		// Click Save and Convert.
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		VoodooUtils.waitForReady();
		
		// Click on preview of Contact record
		new VoodooControl("a", "css", "table.converted-results tbody tr:nth-of-type(1) .preview-list-cell a").click();
		VoodooUtils.waitForReady();
		
		// Click on show more in contacts preview pane
		new VoodooControl("button", "css", ".preview-data .more").click();
		VoodooUtils.waitForReady();

		// Verify department value in contacts preview pane  
		new VoodooControl("div", "css", ".preview-data .fld_department div").assertEquals(testName, true);
				
		// Verify lead source value in contacts preview pane
		new VoodooControl("div", "css", ".preview-data .fld_lead_source div").assertEquals(leadsFS.get("leadSource"), true);

		// Click on show more in leads record view
		sugar().leads.recordView.showMore();
		
		// Verify fields in leads record view
		sugar().leads.recordView.getDetailField("department").assertEquals(sugar().leads.defaultData.get("department"), true);
		sugar().leads.recordView.getDetailField("leadSource").assertEquals(sugar().leads.defaultData.get("leadSource"), true);
		sugar().leads.recordView.getDetailField("status").assertEquals(leadsFS.get("status"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}