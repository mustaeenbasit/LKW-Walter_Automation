package com.sugarcrm.test.grimoire;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Ignore;
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
public class QuotesModuleTests extends SugarTest {

	public void setup() throws Exception {
		sugar().quotes.api.create();
		sugar().login();
		sugar().quotes.navToListView();
	}

	@Test
	public void verifyElements() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyElements()...");

		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.listView.getControl("moduleTitle").assertVisible(true);
		sugar().quotes.listView.getControl("nameBasic").assertVisible(true);
		sugar().quotes.listView.getControl("myItemsCheckbox").assertVisible(true);
		sugar().quotes.listView.getControl("myFavoritesCheckbox").assertVisible(true);
		sugar().quotes.listView.getControl("searchButton").assertVisible(true);
		sugar().quotes.listView.getControl("clearButton").assertVisible(true);
		sugar().quotes.listView.getControl("advancedSearchLink").assertVisible(true);
		sugar().quotes.listView.getControl("startButton").assertVisible(true);
		sugar().quotes.listView.getControl("endButton").assertVisible(true);
		sugar().quotes.listView.getControl("prevButton").assertVisible(true);
		sugar().quotes.listView.getControl("nextButton").assertVisible(true);
		sugar().quotes.listView.getControl("selectAllCheckbox").assertExists(true);
		sugar().quotes.listView.getControl("selectDropdown").assertExists(true);
		sugar().quotes.listView.getControl("actionDropdown").assertExists(true);
		sugar().quotes.listView.getControl("massUpdateButton").assertExists(true);
		sugar().quotes.listView.getControl("deleteButton").assertExists(true);
		sugar().quotes.listView.getControl("exportButton").assertExists(true);
		sugar().quotes.listView.getControl("selectThisPage").assertExists(true);
		sugar().quotes.listView.getControl("selectAll").assertExists(true);
		sugar().quotes.listView.getControl("deselectAll").assertExists(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyElements() complete");
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().navbar.clickModuleDropdown(sugar().quotes);

		// Verify menu items
		sugar().quotes.menu.getControl("createQuote").assertVisible(true);
		sugar().quotes.menu.getControl("viewQuotes").assertVisible(true);
		sugar().quotes.menu.getControl("viewQuoteReports").assertVisible(true);

		sugar().navbar.clickModuleDropdown(sugar().quotes); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyModuleTitle() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyModuleTitle()...");

		String expected = sugar().quotes.moduleNamePlural;
		String found = sugar().quotes.listView.getModuleTitle();
		assertTrue("getModuleTitle did not return " + expected + "; " + found + " was found instead.", found.contains(expected));

		VoodooUtils.voodoo.log.info("verifyModuleTitle() complete.");
	}

	@Test
	public void verifySelectionMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySelectionMenuItems()...");

		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.listView.getControl("selectAllCheckbox").assertVisible(true);
		VoodooUtils.focusDefault();
		sugar().quotes.listView.openSelectDropdown();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.listView.getControl("selectThisPage").assertVisible(true);
		sugar().quotes.listView.getControl("selectAll").assertVisible(true);
		sugar().quotes.listView.getControl("deselectAll").assertVisible(true);
		sugar().quotes.listView.getControl("selectDropdown").click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifySelectionMenuItems() complete.");
	}

	@Test
	public void verifyActionMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyActionMenuItems()...");

		sugar().quotes.listView.checkRecord(1);
		sugar().quotes.listView.openActionDropdown();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.listView.getControl("deleteButton").assertVisible(true);
		sugar().quotes.listView.getControl("massUpdateButton").assertVisible(true);
		sugar().quotes.listView.getControl("exportButton").assertVisible(true);
		sugar().quotes.listView.getControl("actionDropdown").click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyActionMenuItems() complete.");
	}

	@Test
	public void verifyListviewField() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListviewField()...");

		FieldSet quoteFS = sugar().quotes.getDefaultData();
		sugar().quotes.listView.verifyField(1, "name", quoteFS.get("name"));
		sugar().quotes.listView.verifyField(1, "date_quote_expected_closed", quoteFS.get("date_quote_expected_closed"));

		VoodooUtils.voodoo.log.info("verifyListviewField() complete");
	}

	@Test
	public void verifyCheckUncheckRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyCheckUncheckRecord()...");

		VoodooControl checkbox = sugar().quotes.listView.getControl("checkbox01");
		sugar().quotes.listView.checkRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		checkbox.assertChecked(true);
		VoodooUtils.focusDefault();

		sugar().quotes.listView.uncheckRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		checkbox.assertChecked(false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyCheckUncheckRecord() complete.");
	}

	@Test
	public void verifyBasicAndClearSearch() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyBasicAndClearSearch()...");

		sugar().quotes.listView.basicSearch("Blank");
		int row = sugar().quotes.listView.countRows();
		Assert.assertTrue("Number of rows did not equal zero.", row == 0);
		VoodooUtils.focusDefault();

		sugar().quotes.listView.basicSearch(sugar().quotes.getDefaultData().get("name"));
		row = sugar().quotes.listView.countRows();
		Assert.assertTrue("Number of rows did not equal one.", row == 1);

		VoodooUtils.voodoo.log.info("verifyBasicAndClearSearch() complete.");
	}

	@Test
	public void countRows() throws Exception {
		VoodooUtils.voodoo.log.info("Running countRows()...");

		// Verify 1 record count
		int row = sugar().quotes.listView.countRows();
		Assert.assertTrue("Number of rows did not equal one.", row == 1);

		// Verify 3 record count
		DataSource ds = new DataSource();
		FieldSet name1 = new FieldSet();
		name1.put("name", "Door-to-Door");
		FieldSet name2 = new FieldSet();
		name2.put("name", "Pamphlet");
		ds.add(name1);
		ds.add(name2);
		sugar().quotes.api.create(ds);
		VoodooUtils.refresh(); // to populate data

		row = sugar().quotes.listView.countRows();
		Assert.assertTrue("Number of rows did not equal three.", row == 3);
		VoodooUtils.focusDefault();

		// Verify 2 records after deleting 1 record
		sugar().quotes.listView.deleteRecord(1);
		sugar().quotes.listView.confirmDelete();
		VoodooUtils.focusDefault();
		row = sugar().quotes.listView.countRows();
		Assert.assertTrue("Number of rows did not equal two.", row == 2);

		// Verify no records after deleting all records
		VoodooUtils.focusDefault();
		sugar().quotes.listView.toggleSelectAll();
		VoodooUtils.focusDefault();
		sugar().quotes.listView.delete();
		sugar().quotes.listView.confirmDelete();
		VoodooUtils.focusDefault();
		row = sugar().quotes.listView.countRows();
		Assert.assertTrue("Number of rows did not equal zero.", row == 0);

		VoodooUtils.voodoo.log.info("countRows() complete");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		sugar().quotes.listView.clickRecord(1);
		sugar().quotes.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.editView.getEditField("name").assertVisible(true);
		sugar().quotes.editView.getEditField("opportunityName").assertVisible(true);
		sugar().quotes.editView.getEditField("quoteStage").assertVisible(true);
		sugar().quotes.editView.getEditField("purchaseOrderNum").assertVisible(true);
		sugar().quotes.editView.getEditField("date_quote_expected_closed").assertVisible(true);
		sugar().quotes.editView.getEditField("paymentTerms").assertVisible(true);
		sugar().quotes.editView.getEditField("date_original_po").assertVisible(true);
		sugar().quotes.editView.getEditField("description").assertVisible(true);
		sugar().quotes.editView.getEditField("billingAccountName").assertVisible(true);
		sugar().quotes.editView.getEditField("shippingAccountName").assertVisible(true);
		sugar().quotes.editView.getEditField("billingContactName").assertVisible(true);
		sugar().quotes.editView.getEditField("shippingContactName").assertVisible(true);
		sugar().quotes.editView.getEditField("billingStreet").assertVisible(true);
		sugar().quotes.editView.getEditField("billingCity").assertVisible(true);
		sugar().quotes.editView.getEditField("billingState").assertVisible(true);
		sugar().quotes.editView.getEditField("billingPostalCode").assertVisible(true);
		sugar().quotes.editView.getEditField("billingCountry").assertVisible(true);
		sugar().quotes.editView.getEditField("assignedTo").assertVisible(true);
		sugar().quotes.editView.getEditField("teams").assertVisible(true);
		VoodooUtils.focusDefault();
		sugar().quotes.editView.cancel();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.detailView.getDetailField("name").assertExists(true);
		sugar().quotes.detailView.getDetailField("opportunityName").assertExists(true);
		sugar().quotes.detailView.getDetailField("quoteStage").assertExists(true);
		sugar().quotes.detailView.getDetailField("purchaseOrderNum").assertExists(true);
		sugar().quotes.detailView.getDetailField("date_quote_expected_closed").assertExists(true);
		sugar().quotes.detailView.getDetailField("paymentTerms").assertExists(true);
		sugar().quotes.detailView.getDetailField("date_original_po").assertExists(true);
		sugar().quotes.detailView.getDetailField("description").assertExists(true);
		sugar().quotes.detailView.getDetailField("billingAccountName").assertExists(true);
		sugar().quotes.detailView.getDetailField("shippingAccountName").assertExists(true);
		sugar().quotes.detailView.getDetailField("billingContactName").assertExists(true);
		sugar().quotes.detailView.getDetailField("shippingContactName").assertExists(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	@Ignore("VOOD-1674 Need library support for column headers control in BWCListview")
	public void verifySortBy() throws Exception {
		VoodooUtils.voodoo.log.severe("Quotes/BWCListview sortBy() not implemented yet!");
	}

	@Ignore("VOOD-1674 Need library support for column headers control in BWCListview")
	public void verifyHeader() throws Exception {
		VoodooUtils.voodoo.log.info("Quotes/BWCListview getHeaders() not implemented yet!");
	}

	public void cleanup() throws Exception {}
}