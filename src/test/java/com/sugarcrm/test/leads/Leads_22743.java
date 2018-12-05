package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Leads_22743 extends SugarTest {
	LeadRecord lead;
	FieldSet editedLeadValues;
	
	public void setup() throws Exception {
		editedLeadValues = testData.get("Leads_22743").get(0);
		lead = (LeadRecord)sugar().leads.api.create();
		
		sugar().login();
	}

	/**
	 * 22743 Verify that changes for audited fields can be viewed by clicking "View Change Log" link
	 * @throws Exception
	 */
	@Test
	public void Leads_22743_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
	
		lead.navToRecord();
		sugar().leads.recordView.edit();
		sugar().leads.recordView.showMore();
		
		// I have to set this field in order to scroll the page down. scroll() method does not work in chrome.
		new VoodooControl("input", "name", "opportunity_amount").set("123");
		
		for (String controlName : editedLeadValues.keySet()) 
			sugar().leads.recordView.getEditField(controlName).set(editedLeadValues.get(controlName));
		
		sugar().leads.recordView.save();
		// TODO: Please remove this wait after VOOD-1013 is implemented 
		VoodooUtils.pause(5000);
		// Verify changelog
		sugar().leads.recordView.openPrimaryButtonDropdown();
		
		// TODO VOOD-695 (or VOOD-738, VOOD-691, VOOD-578)
		// Click View Change Log link
		new VoodooControl("a", "css", ".fld_audit_button a").click();
		
		VoodooControl changelogDiv = new VoodooControl("div", "css", "div[data-voodoo-name='audit']");

		// Wait for the first cell with field name to appear
		new VoodooControl("span", "css", "div[data-voodoo-name='audit'] span[data-voodoo-name='field_name']").waitForVisible();
		
		for(String newValue : editedLeadValues.values())
			changelogDiv.assertContains(newValue, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}