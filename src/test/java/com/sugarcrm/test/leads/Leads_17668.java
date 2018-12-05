package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_17668 extends SugarTest {
	VoodooControl leadModuleCtrl, filterButton, studio;

	public void setup() throws Exception {	
		sugar().leads.api.create();

		// TODO: VOOD-1505
		// Studio Controls 
		leadModuleCtrl = new VoodooControl("a", "id", "studiolink_Leads");
		studio = sugar().admin.adminTools.getControl("studio");
		sugar().login();	

		// Creating a custom Many-to-Many relationship Leads-Opportunity
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studio.click();
		VoodooUtils.waitForReady();
		leadModuleCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "relationshipsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='addrelbtn']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "id", "rhs_mod_field").set(sugar().opportunities.moduleNamePlural);
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[name=saverelbtn]").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that All Saved Filters created in the corresponding List View of the Related Module should be available in this section.
	 * @throws Exception
	 */	
	@Test
	public void Leads_17668_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		DataSource filters = testData.get(testName);

		// TODO: VOOD-598: Need lib support for Leads sub panel 
		// Controls for Leads filters
		VoodooControl filterDropdownButton = new VoodooControl("div", "css", ".search-filter .select2-container.select2.search-filter");
		VoodooControl filterInDropdownMenu = new VoodooControl("li", "css", "#select2-drop li:nth-child(2)");
		filterButton = new VoodooControl("span", "class", "choice-filter-label");
		VoodooSelect moduleFieldsDropdown = new VoodooSelect("div", "css", ".filter-definition-container .controls.span4");
		VoodooSelect filterCriteria = new VoodooSelect("select", "css", ".filter-definition-container div:nth-child(2)");
		VoodooControl filterText = new VoodooControl("input", "css", ".filter-definition-container div:nth-child(3) input");
		VoodooControl filterNameField = new VoodooControl("input", "css", ".search-filter .filter-header input");
		VoodooControl saveButton = new VoodooControl("a", "css", ".filter-header div:nth-child(2) .btn.btn-primary.save_button");

		sugar().leads.navToListView();	
		sugar().leads.listView.clickRecord(1);

		// Open 'Related' drop down and select Tasks 
		sugar().leads.recordView.setRelatedSubpanelFilter(sugar().tasks.moduleNamePlural);

		// Creating a new Tasks Filter
		filterButton.click();
		moduleFieldsDropdown.set(filters.get(0).get("moduleFieldsDropdown"));
		filterCriteria.set(filters.get(0).get("filterCriteria"));
		filterText.set(sugar().tasks.moduleNameSingular);
		filterNameField.set(filters.get(0).get("filterNameField"));
		VoodooUtils.waitForReady(); 
		saveButton.click();
		VoodooUtils.waitForReady(); 

		// Verify that Tasks Filter has been created
		filterButton.assertContains(filters.get(0).get("filterNameField"), true);

		// Verify that Tasks Filter is displayed in filters dropdown
		filterDropdownButton.click();
		filterInDropdownMenu.assertContains(filters.get(0).get("filterNameField"), true);
		filterInDropdownMenu.click();

		// Open 'Related' drop down and select Opportunities 
		sugar().leads.recordView.setRelatedSubpanelFilter(sugar().opportunities.moduleNamePlural);

		// Creating a new Opportunities Filter
		filterButton.click();
		moduleFieldsDropdown.set(filters.get(1).get("moduleFieldsDropdown"));
		filterCriteria.set(filters.get(1).get("filterCriteria"));
		filterText.set(sugar().opportunities.moduleNameSingular);
		filterNameField.set(filters.get(1).get("filterNameField"));
		VoodooUtils.waitForReady(); 
		saveButton.click();
		VoodooUtils.waitForReady(); 

		// Verify that Opportunities Filter has been created
		filterButton.assertContains(filters.get(1).get("filterNameField"), true);

		// Verify that Opportunities Filter is displayed in filters dropdown
		filterDropdownButton.click();
		filterInDropdownMenu.assertContains(filters.get(1).get("filterNameField"), true);
		filterInDropdownMenu.click();

		VoodooUtils.voodoo.log.info(testName + " complete."); 
	}

	public void cleanup() throws Exception {}
}