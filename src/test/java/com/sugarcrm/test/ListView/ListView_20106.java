package com.sugarcrm.test.ListView;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_20106 extends SugarTest {
	DataSource acccountsData;
	FieldSet filterData;
	
	public void setup() throws Exception {
		acccountsData = testData.get(testName+"_accounts");
		filterData = testData.get(testName).get(0);
		
		sugar.accounts.api.create(acccountsData);
		sugar.login();
		
		// Add "Type" to the list of display columns in List view of Accounts module  
		// TODO: VOOD-938
		VoodooControl accountsLink = new VoodooControl("a", "id", "studiolink_Accounts");
		VoodooControl layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		VoodooControl listViewCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		VoodooControl typeOptionCtrl = new VoodooControl("li", "css", "[data-name='account_type']");
		VoodooControl defaultColumnsCtrl = new VoodooControl("td", "id", "Default");
		VoodooControl saveAndDeployButton = new VoodooControl("input", "id", "savebtn");

		// navigate to Admin panel 
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
		// click on Accounts in studio panel 
		accountsLink.click();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		listViewCtrl.click();
		VoodooUtils.waitForReady();
		typeOptionCtrl.scrollIntoViewIfNeeded(true);
		typeOptionCtrl.dragNDrop(defaultColumnsCtrl);
		saveAndDeployButton.click();
		sugar.alerts.waitForLoadingExpiration(); 
		
		VoodooUtils.focusDefault();
	}
	/**
	*  Verify that results are filtered correctly when Searching from dropdown.
	*  
	*  @throws Exception
	*/
	@Test
	public void ListView_20106_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		//Selecting first record on Accounts ListView
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);
		sugar.accounts.recordView.edit();
		
		// Changing the "Type" of first account to 'Customer'
		sugar.accounts.recordView.getEditField("type").set(filterData.get("typeField1"));
		sugar.accounts.recordView.save();
		sugar.accounts.recordView.gotoNextRecord();
		sugar.accounts.recordView.edit();
		
		// Changing the "Type" of second account to 'Analyst'
		sugar.accounts.recordView.getEditField("type").set(filterData.get("typeField2"));
		sugar.accounts.recordView.save();
		
		sugar.accounts.navToListView();
		int rows = sugar.accounts.listView.countRows();
		
		// Verifying that all 4 records are visible 
		Assert.assertEquals("Number of records not equal to 4 when they should be", rows, (acccountsData.size()));
		
		// Selecting the Create New option from drop down
		sugar.accounts.listView.openFilterDropdown();
		sugar.accounts.listView.selectFilterCreateNew();
		
		// TODO: VOOD-555
		// Controls for Accounts filters
		VoodooSelect moduleFieldsDropdown = new VoodooSelect("div", "css", ".filter-definition-container .controls.span4");
		VoodooSelect filterCriteria = new VoodooSelect("select", "css", ".filter-definition-container div:nth-child(2)");
		VoodooSelect industryField = new VoodooSelect("input", "css", ".filter-definition-container div:nth-child(3) input");
		VoodooControl filterNameField = new VoodooControl("input", "css", ".search-filter .filter-header input");
		VoodooControl saveButton = new VoodooControl("a", "css", ".filter-header div:nth-child(2) .btn.btn-primary.save_button");
		
		moduleFieldsDropdown.set(filterData.get("moduleField"));
		filterCriteria.set(filterData.get("filterCriteria"));
		industryField.set(filterData.get("typeField1"));
		VoodooUtils.waitForReady();
		industryField.set(filterData.get("typeField2"));
		VoodooUtils.waitForReady();
		filterNameField.set(filterData.get("filterName"));
		VoodooUtils.waitForReady();
		saveButton.click();
		VoodooUtils.waitForReady();
		
		rows = sugar.accounts.listView.countRows();
		
		/* Verifying that 2 records are visible 
		 * Where 2 is the number of non-Customer or non-Analyst type accounts
		 */
		Assert.assertEquals("Number of records not equal to 2 when they should be", rows, (acccountsData.size() - 2));
		
		// Verifying that only records with type as Customer and Analyst are displayed
		sugar.accounts.listView.verifyField(1, "name", acccountsData.get(2).get("name"));
		sugar.accounts.listView.verifyField(2, "name", acccountsData.get(3).get("name"));
		
		// TODO: VOOD-823    
		// Verifying that the Accounts type(s) are displayed as Analyst and Customer only
		new VoodooControl("td", "css", "tr:nth-child(1) [data-type='enum']").assertEquals(filterData.get("typeField2"), true);
		new VoodooControl("td", "css", "tr:nth-child(2) [data-type='enum']").assertEquals(filterData.get("typeField1"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
		
	public void cleanup() throws Exception {}
}