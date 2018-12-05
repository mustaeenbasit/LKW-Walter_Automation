package com.sugarcrm.test.grimoire;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * Contains self tests for elements, menu dropdown, selection menu, action menu items,
 * toggle record, basic clear search form, countRows, hook values for listview edit & detail view, 
 * getHeaders and sortBy in subpanels and in listview.
 * 
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class ContractsModuleTests extends SugarTest {
	public void setup() throws Exception {
		sugar().contracts.api.create();
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().contracts);
		sugar().contracts.navToListView();
	}

	@Test
	public void verifyModuleTitle() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyModuleTitle()...");

		String expected = sugar().contracts.moduleNamePlural;
		String found = sugar().contracts.listView.getModuleTitle();
		Assert.assertTrue("getModuleTitle did not return " + expected + "; " + found + " was found instead.", found.contains(expected));

		VoodooUtils.voodoo.log.info("verifyModuleTitle() complete.");
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().navbar.clickModuleDropdown(sugar().contracts);

		// Verify menu items
		sugar().contracts.menu.getControl("createContract").assertVisible(true);
		sugar().contracts.menu.getControl("viewContracts").assertVisible(true);
		sugar().contracts.menu.getControl("importContracts").assertVisible(true);
		sugar().navbar.clickModuleDropdown(sugar().contracts); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyPageElements() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyPageElements()...");

		VoodooUtils.focusFrame("bwc-frame");
		sugar().contracts.listView.getControl("moduleTitle").assertVisible(true);
		sugar().contracts.listView.getControl("nameBasic").assertVisible(true);
		sugar().contracts.listView.getControl("myItemsCheckbox").assertVisible(true);
		sugar().contracts.listView.getControl("myFavoritesCheckbox").assertVisible(true);
		sugar().contracts.listView.getControl("searchButton").assertVisible(true);
		sugar().contracts.listView.getControl("clearButton").assertVisible(true);
		sugar().contracts.listView.getControl("advancedSearchLink").assertVisible(true);
		sugar().contracts.listView.getControl("startButton").assertVisible(true);
		sugar().contracts.listView.getControl("endButton").assertVisible(true);
		sugar().contracts.listView.getControl("prevButton").assertVisible(true);
		sugar().contracts.listView.getControl("nextButton").assertVisible(true);
		sugar().contracts.listView.getControl("selectAllCheckbox").assertExists(true);
		sugar().contracts.listView.getControl("selectDropdown").assertExists(true);
		sugar().contracts.listView.getControl("actionDropdown").assertExists(true);
		sugar().contracts.listView.getControl("massUpdateButton").assertExists(true);
		sugar().contracts.listView.getControl("deleteButton").assertExists(true);
		sugar().contracts.listView.getControl("exportButton").assertExists(true);
		sugar().contracts.listView.getControl("selectThisPage").assertExists(true);
		sugar().contracts.listView.getControl("selectAll").assertExists(true);
		sugar().contracts.listView.getControl("deselectAll").assertExists(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyPageElements() complete");
	}	

	@Test
	public void verifyActionMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyActionMenuItems()...");

		sugar().contracts.listView.checkRecord(1);
		sugar().contracts.listView.openActionDropdown();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().contracts.listView.getControl("deleteButton").assertVisible(true);
		sugar().contracts.listView.getControl("massUpdateButton").assertVisible(true);
		sugar().contracts.listView.getControl("exportButton").assertVisible(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyActionMenuItems() complete.");
	}

	@Test
	public void verifySelectionMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySelectionMenuItems()...");

		VoodooUtils.focusFrame("bwc-frame");
		sugar().contracts.listView.getControl("selectAllCheckbox").assertVisible(true);
		VoodooUtils.focusDefault();
		sugar().contracts.listView.openSelectDropdown();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().contracts.listView.getControl("selectThisPage").assertVisible(true);
		sugar().contracts.listView.getControl("selectAll").assertVisible(true);
		sugar().contracts.listView.getControl("deselectAll").assertVisible(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifySelectionMenuItems() complete.");
	}

	@Test
	public void verifyListviewField() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListviewField()...");

		sugar().contracts.listView.verifyField(1, "name", sugar().contracts.defaultData.get("name"));
		sugar().contracts.listView.verifyField(1, "status", sugar().contracts.defaultData.get("status"));
		sugar().contracts.listView.verifyField(1, "assignedTo", "Administrator");

		VoodooUtils.voodoo.log.info("verifyListviewField() complete");
	}

	@Test
	public void verifyCheckUncheckRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyCheckUncheckRecord()...");

		VoodooControl checkbox = sugar().contracts.listView.getControl("checkbox01");
		sugar().contracts.listView.checkRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		checkbox.assertChecked(true);
		VoodooUtils.focusDefault();

		sugar().contracts.listView.uncheckRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		checkbox.assertChecked(false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyCheckUncheckRecord() complete.");
	}

	@Test
	public void verifyBasicAndClearSearch() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyBasicAndClearSearch()...");

		sugar().contracts.listView.basicSearch("Blank");
		int rowCount = sugar().contracts.listView.countRows();
		Assert.assertTrue("Number of rows did not equal zero.", rowCount == 0);
		VoodooUtils.focusDefault();

		sugar().contracts.listView.basicSearch(sugar().contracts.getDefaultData().get("name"));
		rowCount = sugar().contracts.listView.countRows();
		Assert.assertTrue("Number of rows did not equal one.", rowCount == 1);

		VoodooUtils.voodoo.log.info("verifyBasicAndClearSearch() complete.");
	}

	@Test
	public void verifyRowCount() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyRowCount()...");

		// Verify 1 record count
		int rowCount = sugar().contracts.listView.countRows();
		Assert.assertTrue("Number of rows did not equal one.", rowCount == 1);

		// Verify 3 record count
		DataSource ds = new DataSource();
		FieldSet name1 = new FieldSet();
		name1.put("name", "Semi Brain Contract");
		FieldSet name2 = new FieldSet();
		name2.put("name", "Full Brain Contract");
		ds.add(name1);
		ds.add(name2);
		sugar().contracts.api.create(ds);
		VoodooUtils.refresh(); // to populate data

		rowCount = sugar().contracts.listView.countRows();
		Assert.assertTrue("Number of rows did not equal three.", rowCount == 3);
		VoodooUtils.focusDefault();

		// Verify 2 records after deleting 1 record
		sugar().contracts.listView.deleteRecord(1);
		sugar().contracts.listView.confirmDelete();
		VoodooUtils.focusDefault();
		rowCount = sugar().contracts.listView.countRows();
		Assert.assertTrue("Number of rows did not equal two.", rowCount == 2);

		// Verify no records after deleting all records
		VoodooUtils.focusDefault();
		sugar().contracts.listView.toggleSelectAll();
		VoodooUtils.focusDefault();
		sugar().contracts.listView.delete();
		sugar().contracts.listView.confirmDelete();
		VoodooUtils.focusDefault();
		rowCount = sugar().contracts.listView.countRows();
		Assert.assertTrue("Number of rows did not equal zero.", rowCount == 0);

		VoodooUtils.voodoo.log.info("verifyRowCount() complete");
	}

	@Test
	public void verifyQuickCreate() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyQuickCreate()...");

		// Go to admin -> Configure Navigation Bar Quick Create
		sugar().admin.navToAdminPanelLink("configurationShortcutBar");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1805 - Need Lib support for enabling/disabling modules via Javascript for Navigation Bar Quick Create Configuration
		VoodooControl dropHereCtrl = new VoodooControl("tbody", "css", "#enabled_div tbody[tabindex='0']");

		// XPATH used because no unique class, id selector for the control
		new VoodooControl("tr", "xpath", "//*[@id='disabled_div']/div[3]/table/tbody[2]/tr[contains(.,'" + sugar().contracts.moduleNamePlural + "')]").dragNDrop(dropHereCtrl);

		// Save it
		new VoodooControl("input", "css", "input[value='Save']").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady(40000);

		// Quick Create Contracts
		sugar().navbar.quickCreateAction(sugar().contracts.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		sugar().contracts.editView.getEditField("name").assertVisible(true);
		VoodooUtils.focusDefault();
		sugar().contracts.editView.cancel();

		VoodooUtils.voodoo.log.info("verifyQuickCreate() complete.");
	}

	@Test
	public void verifyEditFieldsInDetailView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditFieldsInDetailView()...");

		FieldSet defaultData = sugar().contracts.getDefaultData();
		sugar().contracts.listView.clickRecord(1);
		sugar().contracts.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().contracts.editView.getEditField("name").assertEquals(defaultData.get("name"), true);
		sugar().contracts.editView.getEditField("date_start").assertVisible(true);
		sugar().contracts.editView.getEditField("date_end").assertVisible(true);

		// TODO: VOOD-1843 - Improve ChildElement to detect the parent strategy.
		// Once resolved it should access via getChildElement
		new VoodooControl("option", "css", "#" + sugar().contracts.editView.getEditField("status").getHookString() + " option[value=notstarted]").assertEquals(defaultData.get("status"), true);
		sugar().contracts.editView.getEditField("description").assertEquals(defaultData.get("description"), true);
		sugar().contracts.editView.getEditField("reference_code").assertVisible(true);
		sugar().contracts.editView.getEditField("account_name").assertVisible(true);
		sugar().contracts.editView.getEditField("opportunity").assertVisible(true);
		sugar().contracts.editView.getEditField("type").assertVisible(true);
		sugar().contracts.editView.getEditField("contract_value").assertVisible(true);
		sugar().contracts.editView.getEditField("date_company_signed").assertVisible(true);
		sugar().contracts.editView.getEditField("currency").assertVisible(true);
		sugar().contracts.editView.getEditField("date_expiration_notice_date").assertVisible(true);
		sugar().contracts.editView.getEditField("date_expiration_notice_hours").assertVisible(true);
		sugar().contracts.editView.getEditField("date_expiration_notice_minutes").assertVisible(true);
		sugar().contracts.editView.getEditField("date_expiration_notice_meridiem").assertVisible(true);
		sugar().contracts.editView.getEditField("date_customer_signed").assertVisible(true);
		sugar().contracts.editView.getEditField("assignedTo").assertVisible(true);
		sugar().contracts.editView.getEditField("teams").assertVisible(true);
		VoodooUtils.focusDefault();
		sugar().contracts.editView.cancel();

		VoodooUtils.voodoo.log.info("verifyEditFieldsInDetailView() complete.");
	}

	@Test
	public void verifyDetailFieldsInDetailView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyDetailFieldsInDetailView()...");

		FieldSet defaultData = sugar().contracts.getDefaultData();
		sugar().contracts.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		sugar().contracts.detailView.getDetailField("name").assertEquals(defaultData.get("name"), true);
		sugar().contracts.detailView.getDetailField("description").assertEquals(defaultData.get("description"), true);
		sugar().contracts.detailView.getDetailField("status").assertVisible(true);
		sugar().contracts.detailView.getDetailField("type").assertVisible(true);
		sugar().contracts.detailView.getDetailField("contract_value").assertVisible(true);
		sugar().contracts.detailView.getDetailField("date_start").assertExists(true);
		sugar().contracts.detailView.getDetailField("date_end").assertExists(true);
		sugar().contracts.detailView.getDetailField("date_company_signed").assertExists(true);
		sugar().contracts.detailView.getDetailField("date_expiration_notice_date").assertExists(true);
		sugar().contracts.detailView.getDetailField("account_name").assertExists(true);
		sugar().contracts.detailView.getDetailField("opportunity").assertExists(true);
		sugar().contracts.detailView.getDetailField("assignedTo").assertVisible(true);
		sugar().contracts.detailView.getDetailField("teams").assertVisible(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyDetailFieldsInDetailView() complete.");
	}

	@Test
	public void verifySubpanelsInDetailView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySubpanelsInDetailView()...");

		sugar().contracts.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		sugar().contracts.detailView.subpanels.get(sugar().contacts.moduleNamePlural).assertExists(true);
		sugar().contracts.detailView.subpanels.get(sugar().quotes.moduleNamePlural).assertExists(true);

		// TODO: VOOD-1948 - Documents Subpanel failing on Contracts recordView. And notes supanel too
		new VoodooControl("li", "id", "whole_subpanel_contracts_documents").assertExists(true);
		new VoodooControl("li", "id", "whole_subpanel_history").assertExists(true);

		VoodooUtils.voodoo.log.info("verifySubpanelsInDetailView() complete.");
	}

	@Test
	public void verifyHeadersAndSortBySidecarSubpanel() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyHeadersAndSortBySidecarSubpanel()...");

		sugar().admin.enableSubpanelDisplayViaJs(sugar().contracts);
		sugar().accounts.api.create();
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		StandardSubpanel contractSub = sugar().accounts.recordView.subpanels.get(sugar().contracts.moduleNamePlural);
		contractSub.expandSubpanel();
		Assert.assertTrue("Get Header Contracts ",!contractSub.getHeaders().isEmpty());

		for (String header : contractSub.getHeaders()) {
			contractSub.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
		}

		contractSub.sortBy("headerName", false);
		contractSub.sortBy("headerStartdate", false);
		contractSub.sortBy("headerEnddate", false);
		contractSub.sortBy("headerStatus", false);
		contractSub.sortBy("headerTotalcontractvalue", false);

		VoodooUtils.voodoo.log.info("verifyHeadersAndSortBySidecarSubpanel() complete.");
	}

	@Ignore("VOOD-1674 - Need library support for column headers control in BWCListview")
	public void verifyHeadersAndSortByViaListview() throws Exception {
		VoodooUtils.voodoo.log.info("functionality not implemented yet!");
	}

	public void cleanup() throws Exception {}
}