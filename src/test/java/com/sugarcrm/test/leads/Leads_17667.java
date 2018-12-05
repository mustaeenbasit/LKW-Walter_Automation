package com.sugarcrm.test.leads;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Leads_17667 extends SugarTest {
	DataSource oppRecords = new DataSource();
	int oppRecordsCount = 0;
	StandardSubpanel oppSubpanel;

	public void setup() throws Exception {
		// Creating Leads, Contacts and Account
		sugar().leads.api.create();
		sugar().contacts.api.create();
		sugar().accounts.api.create();

		// Creating three Opportunities records
		oppRecords = testData.get(testName);
		ArrayList<Record> allOpportunities = sugar().opportunities.api.create(oppRecords);
		sugar().login();

		// Navigating to Opportunities ListView to update Sales Stage & Account (required)
		sugar().opportunities.navToListView();
		VoodooUtils.waitForReady();

		// Descending sort Opportunities ListView
		sugar().opportunities.listView.sortBy("headerName",false);

		// TODO: VOOD-1359
		VoodooSelect salesStageEditField = new VoodooSelect("span", "css", ".fld_sales_stage.edit");

		// Updating the opportunities' sales stages as per requirements
		oppRecordsCount = oppRecords.size();
		for(int i=0; i<oppRecordsCount ; i++){
			sugar().opportunities.listView.editRecord(i+1);
			sugar().opportunities.listView.getEditField(i+1, "relAccountName")
			.set(sugar().opportunities.getDefaultData().get("relAccountName"));

			if (i!=oppRecordsCount-1) salesStageEditField.set(oppRecords.get(i).get("rli_stage"));
			sugar().opportunities.listView.saveRecord(i+1);
		}

		// TODO: VOOD-542 & VOOD-1505
		// Creating a custom Many-to-Many relationship Leads-Opportunity
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "id", "studiolink_Leads").click();
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

		// TODO: VOOD-1382
		// Linking opportunities with Leads
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		new VoodooControl("a", "css", ".layout_Opportunities .dropdown-toggle").click();
		new VoodooControl("li", "css", ".layout_Opportunities .actions li").click();
		VoodooUtils.waitForReady(); 
		new VoodooControl("input", "css", "span[data-voodoo-type='field'] .toggle-all").click();
		new VoodooControl("a", "css", "[name='link_button']").click();
		VoodooUtils.waitForReady(); 

		// Linking opportunities with Contacts
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		VoodooUtils.waitForReady(); 
		oppSubpanel = sugar().contacts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);

		// TODO: VOOD-1540 Pointer triggers List Item#1 Tool-tip which overlaps Item#2  
		//oppSubpanel.linkExistingRecords(allOpportunities);
		oppSubpanel.clickLinkExisting();
		for(Record record : allOpportunities) {
			// TODO: VOOD-1540 Pointer triggers List Item#1 Toop-tip which overlaps Item#2  
			sugar().opportunities.searchSelect.getControl("cancel").hover();
			sugar().opportunities.searchSelect.selectRecord(record);
		}
		sugar().opportunities.searchSelect.link();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Verify user can access to the user defined filter
	 * @throws Exception
	 */
	@Test
	public void Leads_17667_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		VoodooUtils.waitForReady();

		// Open 'Related' drop down and select Opportunities 
		sugar().leads.recordView.setRelatedSubpanelFilter(sugar().opportunities.moduleNamePlural);
		VoodooUtils.waitForReady();

		// TODO: VOOD-598: Need lib support for Leads sub panel 
		// Initializing Opportunities Filter Controls
		VoodooControl filterDropdownButton = new VoodooControl("div", "css", ".search-filter .select2-container.select2.search-filter");
		VoodooControl custFilterInDropdownMenu = new VoodooControl("li", "css", "#select2-drop li:nth-child(2)");

		// Calculating yesterday's date
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);  
		String yesterdayDate = dateFormat.format(cal.getTime());

		// TODO: VOOD-598: Need lib support for Leads sub panel 
		// Creating a new Opportunities Filter
		FieldSet customData = testData.get(testName+"_custom").get(0);
		new VoodooControl("span", "class", "choice-filter-label").click();
		new VoodooSelect("div", "css", ".filter-definition-container .controls.span4").set(customData.get("moduleFieldsDropdown"));
		new VoodooSelect("select", "css", ".filter-definition-container div:nth-child(2)").set(customData.get("filterCriteria"));
		new VoodooControl("input", "css", ".filter-definition-container div:nth-child(3) input").set(yesterdayDate);
		new VoodooControl("button", "css", ".search-filter button:nth-child(2)").click();
		new VoodooSelect("button", "css", ".select2-default").set(customData.get("secondmodFieldsDropdown"));
		new VoodooSelect("select", "css", ".select2-default").set(customData.get("secondfilterCriteria"));
		new VoodooControl("input", "css", "[name='probability']").set(customData.get("probability"));
		new VoodooControl("input", "css", ".search-filter .filter-header input").set(customData.get("filterNameField"));
		VoodooUtils.waitForReady(); 
		new VoodooControl("a", "css", ".filter-header div:nth-child(2) .btn.btn-primary.save_button").click();
		VoodooUtils.waitForReady(); 

		// Selecting All Opportunities filter
		filterDropdownButton.click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "[data-id='all_records']").click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-1382
		new VoodooControl("span", "css", ".layout_Opportunities .sorting.orderByname span").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1382
		// Verifying that all Opportunities are displayed
		for(int i=0; i<oppRecordsCount ; i++)
			new VoodooControl("span", "css", ".layout_Opportunities tr:nth-of-type(" + (i+1) 
					+") .fld_name").assertContains(oppRecords.get(i).get("name"), true);

		// Selecting the custom filter created above
		filterDropdownButton.click();
		custFilterInDropdownMenu.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1382
		// Verifying that Opportunities only as per the filter are displayed
		for(int i=1; i<oppRecordsCount ; i++){
			new VoodooControl("span", "css", ".layout_Opportunities tr:nth-of-type(" + i 
					+ ") .fld_name").assertContains(oppRecords.get(i-1).get("name"), true);
			VoodooUtils.waitForReady();
		}
		// Navigating to Contacts Module
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// Open 'Related' drop down and select Opportunities 
		sugar().contacts.recordView.setRelatedSubpanelFilter(sugar().opportunities.moduleNamePlural);

		// Verifying that the custom filter created above is displayed here also (in Contacts Module)
		filterDropdownButton.click();
		VoodooUtils.waitForReady();
		custFilterInDropdownMenu.assertContains(customData.get("filterNameField"), true);
		custFilterInDropdownMenu.click();
		oppSubpanel.expandSubpanel();
		VoodooUtils.waitForReady(); // wait needed here as results need time to get displayed

		// Verify that filtered opportunities are displayed when the custom filter is selected
		Assert.assertTrue("Number of rows did not equal TWO, when they should be.", oppSubpanel.countRows() == 2);

		VoodooUtils.voodoo.log.info(testName + " complete."); 
	}

	public void cleanup() throws Exception {}
}