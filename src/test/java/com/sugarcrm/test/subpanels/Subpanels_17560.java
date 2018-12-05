package com.sugarcrm.test.subpanels;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_17560 extends SugarTest {
	ContactRecord myContact;
	CaseRecord myCase;

	// Common VoodooControl and VoodooSelect references
	VoodooSelect filterModuleDropdown;
	VoodooSelect filterFilterDropdown;
	VoodooControl choiceRelated;
	VoodooControl caseRow;

	public void setup() throws Exception {
		// Initialize the common Control references
		filterModuleDropdown = new VoodooSelect("a", "css", "div.related-filter a");
		filterFilterDropdown = new VoodooSelect("a", "css", "span[data-voodoo-name='filter-filter-dropdown'] div a");
		choiceRelated = new VoodooControl("div", "css", "span[data-voodoo-name=filter-module-dropdown] div.choice-related");
		caseRow = new VoodooControl("div", "css", "div[data-voodoo-name=Cases] table tbody tr:nth-of-type(1) .fld_name.list div");
		myContact = (ContactRecord) sugar.contacts.api.create();
		sugar.login();

		// Relate a case to a contact
		myCase = (CaseRecord) sugar.cases.api.create();
		myContact.navToRecord();

		StandardSubpanel casesSubpanel = sugar.contacts.recordView.subpanels.get(sugar.cases.moduleNamePlural);
		casesSubpanel.linkExistingRecord(myCase);
		caseRow.assertEquals(myCase.get("name"), true);
	}

	/**
	 * Verify record view sub panel - ability to dynamically filter related
	 * records data
	 * @throws Exception
	 */
	@Test
	public void Subpanels_17560_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet filter = testData.get(testName).get(0);
		//myContact.navToRecord();

		// Select the Cases module
		filterModuleDropdown.set(sugar.cases.moduleNamePlural);

		// Pause until screen is rendered
		sugar.alerts.waitForLoadingExpiration();
		choiceRelated.assertEquals(sugar.cases.moduleNamePlural, true);

		// Filter for a Case that exists 
		new VoodooControl("input", "css", "[data-voodoo-name='filter-quicksearch'] input").set(myCase.get("name"));

		// Pause until screen is rendered
		sugar.alerts.waitForLoadingExpiration();
		caseRow.assertEquals(myCase.get("name"), true);

		// Verify the filter has changed to include All Cases
		filterFilterDropdown.set(filter.get("filter_cases"));

		// Change the Related to a different module - Leads
		filterModuleDropdown.set(sugar.leads.moduleNamePlural);

		// Pause until screen is rendered
		sugar.alerts.waitForLoadingExpiration();
		choiceRelated.assertEquals(sugar.leads.moduleNamePlural, true);

		// Verify the filter has changed to include All Leads
		sugar.alerts.waitForLoadingExpiration();
		filterFilterDropdown.set(filter.get("filter_leads"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
