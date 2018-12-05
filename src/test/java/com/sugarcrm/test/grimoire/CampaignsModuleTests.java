package com.sugarcrm.test.grimoire;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * Contains self tests for elements, menu dropdown, selection menu, action menu items,
 * toggle record, basic clear search form, countRows, hook values for listview edit & detail view.
 * 
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class CampaignsModuleTests extends SugarTest {
	public void setup() throws Exception {
		sugar().campaigns.api.create();
		sugar().login();
		sugar().campaigns.navToListView();
	}

	@Test
	public void verifyElements() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyElements()...");

		VoodooUtils.focusFrame("bwc-frame");
		sugar().campaigns.listView.getControl("moduleTitle").assertVisible(true);
		sugar().campaigns.listView.getControl("nameBasic").assertVisible(true);
		sugar().campaigns.listView.getControl("myItemsCheckbox").assertVisible(true);
		sugar().campaigns.listView.getControl("myFavoritesCheckbox").assertVisible(true);
		sugar().campaigns.listView.getControl("searchButton").assertVisible(true);
		sugar().campaigns.listView.getControl("clearButton").assertVisible(true);
		sugar().campaigns.listView.getControl("advancedSearchLink").assertVisible(true);
		sugar().campaigns.listView.getControl("startButton").assertVisible(true);
		sugar().campaigns.listView.getControl("endButton").assertVisible(true);
		sugar().campaigns.listView.getControl("prevButton").assertVisible(true);
		sugar().campaigns.listView.getControl("nextButton").assertVisible(true);
		sugar().campaigns.listView.getControl("selectAllCheckbox").assertExists(true);
		sugar().campaigns.listView.getControl("selectDropdown").assertExists(true);
		sugar().campaigns.listView.getControl("actionDropdown").assertExists(true);
		sugar().campaigns.listView.getControl("massUpdateButton").assertExists(true);
		sugar().campaigns.listView.getControl("deleteButton").assertExists(true);
		sugar().campaigns.listView.getControl("exportButton").assertExists(true);
		sugar().campaigns.listView.getControl("selectThisPage").assertExists(true);
		sugar().campaigns.listView.getControl("selectAll").assertExists(true);
		sugar().campaigns.listView.getControl("deselectAll").assertExists(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyElements() test complete");
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().navbar.clickModuleDropdown(sugar().campaigns);

		// Verify menu items
		sugar().campaigns.menu.getControl("createCampaign").assertVisible(true);
		sugar().campaigns.menu.getControl("createCampaignWizard").assertVisible(true);
		sugar().campaigns.menu.getControl("createCampaignClassic").assertVisible(true);
		sugar().campaigns.menu.getControl("viewCampaigns").assertVisible(true);
		sugar().campaigns.menu.getControl("viewNewsletters").assertVisible(true);
		sugar().campaigns.menu.getControl("createEmailTemplate").assertVisible(true);
		sugar().campaigns.menu.getControl("viewEmailTemplates").assertVisible(true);
		sugar().campaigns.menu.getControl("setUpEmail").assertVisible(true);
		sugar().campaigns.menu.getControl("viewDiagnostics").assertVisible(true);
		sugar().campaigns.menu.getControl("createLeadForm").assertVisible(true);

		sugar().navbar.clickModuleDropdown(sugar().campaigns); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyModuleTitle() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyModuleTitle()...");

		String expected = sugar().campaigns.moduleNamePlural;
		String found = sugar().campaigns.listView.getModuleTitle();
		assertTrue("getModuleTitle did not return " + expected + "; " + found + " was found instead.", found.contains(expected));

		VoodooUtils.voodoo.log.info("verifyModuleTitle() test complete.");
	}

	@Test
	public void verifySelectionMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySelectionMenuItems()...");

		VoodooUtils.focusFrame("bwc-frame");
		sugar().campaigns.listView.getControl("selectAllCheckbox").assertVisible(true);
		VoodooUtils.focusDefault();
		sugar().campaigns.listView.openSelectDropdown();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().campaigns.listView.getControl("selectThisPage").assertVisible(true);
		sugar().campaigns.listView.getControl("selectAll").assertVisible(true);
		sugar().campaigns.listView.getControl("deselectAll").assertVisible(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifySelectionMenuItems() test complete.");
	}

	@Test
	public void verifyActionMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyActionMenuItems()...");

		sugar().campaigns.listView.checkRecord(1);
		sugar().campaigns.listView.openActionDropdown();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().campaigns.listView.getControl("deleteButton").assertVisible(true);
		sugar().campaigns.listView.getControl("massUpdateButton").assertVisible(true);
		sugar().campaigns.listView.getControl("exportButton").assertVisible(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyActionMenuItems() test complete.");
	}

	@Test
	public void verifyListviewField() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListviewField()...");

		sugar().campaigns.listView.verifyField(1, "name", sugar().campaigns.defaultData.get("name"));
		sugar().campaigns.listView.verifyField(1, "status", sugar().campaigns.defaultData.get("status"));
		sugar().campaigns.listView.verifyField(1, "type", sugar().campaigns.defaultData.get("type"));
		sugar().campaigns.listView.verifyField(1, "date_end", sugar().campaigns.defaultData.get("date_end"));

		VoodooUtils.voodoo.log.info("verifyListviewField() test complete");
	}

	@Test
	public void verifyCheckUncheckRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyCheckUncheckRecord()...");

		VoodooControl checkbox = sugar().campaigns.listView.getControl("checkbox01");
		sugar().campaigns.listView.checkRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		Assert.assertTrue("Record is not selected", checkbox.isChecked());
		VoodooUtils.focusDefault();

		sugar().campaigns.listView.uncheckRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		Assert.assertTrue("Record is selected", !(checkbox.isChecked()));
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyCheckUncheckRecord() test complete.");
	}

	@Test
	public void verifyBasicAndClearSearch() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyBasicAndClearSearch()...");

		sugar().campaigns.listView.basicSearch("Blank");
		int row = sugar().campaigns.listView.countRows();
		Assert.assertTrue("Number of rows did not equal zero.", row == 0);
		VoodooUtils.focusDefault();

		sugar().campaigns.listView.basicSearch(sugar().campaigns.getDefaultData().get("name"));
		row = sugar().campaigns.listView.countRows();
		Assert.assertTrue("Number of rows did not equal one.", row == 1);

		VoodooUtils.voodoo.log.info("verifyBasicAndClearSearch() test complete.");
	}

	@Test
	public void countRows() throws Exception {
		VoodooUtils.voodoo.log.info("Running countRows()...");

		// Verify 1 record count
		int row = sugar().campaigns.listView.countRows();
		Assert.assertTrue("Number of rows did not equal one.", row == 1);

		// Verify 3 record count
		DataSource ds = new DataSource();
		FieldSet name1 = new FieldSet();
		name1.put("name", "Door-to-Door");
		FieldSet name2 = new FieldSet();
		name2.put("name", "Pamphlet");
		ds.add(name1);
		ds.add(name2);
		sugar().campaigns.api.create(ds);
		VoodooUtils.refresh(); // to populate data

		row = sugar().campaigns.listView.countRows();
		Assert.assertTrue("Number of rows did not equal three.", row == 3);
		VoodooUtils.focusDefault();

		// Verify 2 records after deleting 1 record
		sugar().campaigns.listView.deleteRecord(1);
		sugar().campaigns.listView.confirmDelete();
		VoodooUtils.focusDefault();
		row = sugar().campaigns.listView.countRows();
		Assert.assertTrue("Number of rows did not equal two.", row == 2);

		// Verify no records after deleting all records
		VoodooUtils.focusDefault();
		sugar().campaigns.listView.toggleSelectAll();
		VoodooUtils.focusDefault();
		sugar().campaigns.listView.delete();
		sugar().campaigns.listView.confirmDelete();
		VoodooUtils.focusDefault();
		row = sugar().campaigns.listView.countRows();
		Assert.assertTrue("Number of rows did not equal zero.", row == 0);

		VoodooUtils.voodoo.log.info("countRows() test complete");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");
		FieldSet defaultData = sugar().campaigns.getDefaultData();

		sugar().campaigns.listView.clickRecord(1);
		sugar().campaigns.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().campaigns.editView.getEditField("name").assertEquals(defaultData.get("name"), true);
		sugar().campaigns.editView.getEditField("date_start").assertEquals(defaultData.get("date_start"), true);
		sugar().campaigns.editView.getEditField("date_end").assertEquals(defaultData.get("date_end"), true);

		// TODO: VOOD-1477
		VoodooControl type = sugar().campaigns.editView.getEditField("type");
		type.getChildElement("option", "css", "#"+type.getHookString()+" option[value=Telesales]").assertEquals(defaultData.get("type"), true);
		VoodooControl status = sugar().campaigns.editView.getEditField("status"); 
		status.getChildElement("option", "css", "#"+status.getHookString()+" option[value=Planning]").assertEquals(defaultData.get("status"), true);
		sugar().campaigns.editView.getEditField("description").assertVisible(true);
		sugar().campaigns.editView.getEditField("objective").assertVisible(true);
		sugar().campaigns.editView.getEditField("expectedCost").assertVisible(true);
		sugar().campaigns.editView.getEditField("budget").assertVisible(true);
		sugar().campaigns.editView.getEditField("impressions").assertVisible(true);
		sugar().campaigns.editView.getEditField("actualCost").assertVisible(true);
		sugar().campaigns.editView.getEditField("expectedRevenue").assertVisible(true);
		sugar().campaigns.editView.getEditField("assignedTo").assertVisible(true);
		sugar().campaigns.editView.getEditField("teams").assertVisible(true);
		VoodooUtils.focusDefault();
		sugar().campaigns.editView.cancel();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().campaigns.detailView.getDetailField("name").assertEquals(defaultData.get("name"), true);
		sugar().campaigns.detailView.getDetailField("type").assertEquals(defaultData.get("type"), true);
		sugar().campaigns.detailView.getDetailField("status").assertEquals(defaultData.get("status"), true);
		sugar().campaigns.detailView.getDetailField("date_start").assertEquals(defaultData.get("date_start"), true);
		sugar().campaigns.detailView.getDetailField("date_end").assertEquals(defaultData.get("date_end"), true);
		sugar().campaigns.detailView.getDetailField("description").assertVisible(true);
		sugar().campaigns.detailView.getDetailField("objective").assertExists(true);
		sugar().campaigns.detailView.getDetailField("expectedCost").assertVisible(true);
		sugar().campaigns.detailView.getDetailField("budget").assertVisible(true);
		sugar().campaigns.detailView.getDetailField("impressions").assertVisible(true);
		sugar().campaigns.detailView.getDetailField("actualCost").assertVisible(true);
		sugar().campaigns.detailView.getDetailField("expectedRevenue").assertVisible(true);
		sugar().campaigns.detailView.getDetailField("assignedTo").assertVisible(true);
		sugar().campaigns.detailView.getDetailField("teams").assertVisible(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	@Test
	public void setUpEmail() throws Exception {
		VoodooUtils.voodoo.log.info("Running setUpEmail()...");

		// SMTP settings
		FieldSet emailSetup =  testData.get("env_email_setup").get(0);
		sugar().admin.setEmailServer(emailSetup);

		// Set up an Email account for Campaign
		emailSetup.put("protocol", "IMAP");
		sugar().campaigns.setupEmail(emailSetup);

		VoodooUtils.voodoo.log.info("Completed setUpEmail()...");
	}

	@Test
	public void setUpEmailCampaign() throws Exception {
		VoodooUtils.voodoo.log.info("Running setUpEmailCampaign()...");

		// SMTP settings
		FieldSet emailSetup =  testData.get("env_email_campaign").get(0);

		// Set up an Email account for Campaign
		emailSetup.put("protocol", "IMAP");
		sugar().campaigns.setupEmail(emailSetup);

		VoodooUtils.voodoo.log.info("Completed setUpEmailCampaign()...");
	}

	// For all cases
	public void cleanup() throws Exception {}
}