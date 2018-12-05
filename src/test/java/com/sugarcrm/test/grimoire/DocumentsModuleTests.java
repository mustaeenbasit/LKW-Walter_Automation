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
 * Contains self tests for elements, menu dropdown, selection menu, action menu items, toggle
 * record, basic clear search form, countRows, hook values for listview edit & detail view.
 *
 * @author Snigdha Sivadas <ssivadas@sugarcrm.com>
 */
public class DocumentsModuleTests extends SugarTest {

	public void setup() throws Exception {
		sugar().documents.api.create();
		sugar().login();
		sugar().documents.navToListView();
	}

	@Test
	public void verifyElements() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyElements()...");

		VoodooUtils.focusFrame("bwc-frame");
		sugar().documents.listView.getControl("moduleTitle").assertVisible(true);
		sugar().documents.listView.getControl("nameBasic").assertVisible(true);
		sugar().documents.listView.getControl("myFavoritesCheckbox").assertVisible(true);
		sugar().documents.listView.getControl("searchButton").assertVisible(true);
		sugar().documents.listView.getControl("clearButton").assertVisible(true);
		sugar().documents.listView.getControl("advancedSearchLink").assertVisible(true);
		sugar().documents.listView.getControl("startButton").assertVisible(true);
		sugar().documents.listView.getControl("endButton").assertVisible(true);
		sugar().documents.listView.getControl("prevButton").assertVisible(true);
		sugar().documents.listView.getControl("nextButton").assertVisible(true);
		sugar().documents.listView.getControl("selectAllCheckbox").assertExists(true);
		sugar().documents.listView.getControl("selectDropdown").assertExists(true);
		sugar().documents.listView.getControl("actionDropdown").assertExists(true);
		sugar().documents.listView.getControl("massUpdateButton").assertExists(true);
		sugar().documents.listView.getControl("deleteButton").assertExists(true);
		sugar().documents.listView.getControl("exportButton").assertExists(true);
		sugar().documents.listView.getControl("selectThisPage").assertExists(true);
		sugar().documents.listView.getControl("selectAll").assertExists(true);
		sugar().documents.listView.getControl("deselectAll").assertExists(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyElements() complete");
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().navbar.clickModuleDropdown(sugar().documents);

		// Verify menu items
		sugar().documents.menu.getControl("createDocument").assertVisible(true);
		sugar().documents.menu.getControl("viewDocuments").assertVisible(true);

		sugar().navbar.clickModuleDropdown(sugar().documents); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyModuleTitle() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyModuleTitle()...");

		String expected = sugar().documents.moduleNamePlural;
		String found = sugar().documents.listView.getModuleTitle();
		assertTrue("getModuleTitle did not return " + expected + "; " + found + " was found instead.", found.contains(expected));

		VoodooUtils.voodoo.log.info("verifyModuleTitle() complete.");
	}

	@Test
	public void verifySelectionMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySelectionMenuItems()...");

		VoodooUtils.focusFrame("bwc-frame");
		sugar().documents.listView.getControl("selectAllCheckbox").assertVisible(true);
		VoodooUtils.focusDefault();
		sugar().documents.listView.openSelectDropdown();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().documents.listView.getControl("selectThisPage").assertVisible(true);
		sugar().documents.listView.getControl("selectAll").assertVisible(true);
		sugar().documents.listView.getControl("deselectAll").assertVisible(true);
		sugar().documents.listView.getControl("selectDropdown").click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifySelectionMenuItems() complete.");
	}

	@Test
	public void verifyActionMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyActionMenuItems()...");

		sugar().documents.listView.checkRecord(1);
		sugar().documents.listView.openActionDropdown();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().documents.listView.getControl("deleteButton").assertVisible(true);
		sugar().documents.listView.getControl("massUpdateButton").assertVisible(true);
		sugar().documents.listView.getControl("exportButton").assertVisible(true);
		sugar().documents.listView.getControl("actionDropdown").click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyActionMenuItems() complete.");
	}

	@Test
	public void verifyListviewField() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListviewField()...");

		FieldSet documentFS = sugar().documents.getDefaultData();
		sugar().documents.listView.verifyField(1, "documentName", documentFS.get("documentName"));
		sugar().documents.listView.verifyField(1, "categoryID", documentFS.get
				("categoryID"));

		VoodooUtils.voodoo.log.info("verifyListviewField() complete");
	}

	@Test
	public void verifyCheckUncheckRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyCheckUncheckRecord()...");

		VoodooControl checkbox = sugar().documents.listView.getControl("checkbox01");
		sugar().documents.listView.checkRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		checkbox.assertChecked(true);
		VoodooUtils.focusDefault();

		sugar().documents.listView.uncheckRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		checkbox.assertChecked(false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyCheckUncheckRecord() complete.");
	}

	@Test
	public void verifyBasicAndClearSearch() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyBasicAndClearSearch()...");

		// Add 2 records
		DataSource ds = new DataSource();
		FieldSet name1 = new FieldSet();
		name1.put("documentName", "Clearsearch_doc_1");
		FieldSet name2 = new FieldSet();
		name2.put("documentName", "Clearsearch_doc_2");
		ds.add(name1);
		ds.add(name2);
		sugar().documents.api.create(ds);
		VoodooUtils.refresh(); // to populate data

		sugar().documents.navToListView();
		sugar().documents.listView.basicSearch("Blank");
		int row = sugar().documents.listView.countRows();
		Assert.assertTrue("Number of rows did not equal zero.", row == 0);
		VoodooUtils.focusDefault();

		// Clear the search and submit the search form
		sugar().documents.listView.clearSearchForm();
		sugar().documents.listView.submitSearchForm();
		VoodooUtils.focusDefault();
		row = sugar().documents.listView.countRows();
		Assert.assertTrue("After clear Number of rows did not equal to three.", row == 3);
		VoodooUtils.focusDefault();

		// Search for the keyword
		sugar().documents.listView.basicSearch(sugar().documents.getDefaultData().get("documentName"));
		row = sugar().documents.listView.countRows();
		Assert.assertTrue("Number of rows did not equal one.", row == 1);

		VoodooUtils.voodoo.log.info("Completed verifyBasicAndClearSearch()...");
	}

	@Test
	public void countRows() throws Exception {
		VoodooUtils.voodoo.log.info("Running countRows()...");

		// Verify 1 record count
		int row = sugar().documents.listView.countRows();
		Assert.assertTrue("Number of rows did not equal one.", row == 1);

		// Verify 3 record count
		DataSource ds = new DataSource();
		FieldSet name1 = new FieldSet();
		name1.put("documentName", "Door-to-Door");
		FieldSet name2 = new FieldSet();
		name2.put("documentName", "Pamphlet");
		ds.add(name1);
		ds.add(name2);
		sugar().documents.api.create(ds);
		VoodooUtils.refresh(); // to populate data

		row = sugar().documents.listView.countRows();
		Assert.assertTrue("Number of rows did not equal three.", row == 3);
		VoodooUtils.focusDefault();

		// Verify 2 records after deleting 1 record
		sugar().documents.listView.deleteRecord(1);
		sugar().documents.listView.confirmDelete();
		VoodooUtils.focusDefault();
		row = sugar().documents.listView.countRows();
		Assert.assertTrue("Number of rows did not equal two.", row == 2);

		// Verify no records after deleting all records
		VoodooUtils.focusDefault();
		sugar().documents.listView.toggleSelectAll();
		VoodooUtils.focusDefault();
		sugar().documents.listView.delete();
		sugar().documents.listView.confirmDelete();
		VoodooUtils.focusDefault();
		row = sugar().documents.listView.countRows();
		Assert.assertTrue("Number of rows did not equal zero.", row == 0);

		VoodooUtils.voodoo.log.info("countRows() complete");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		sugar().documents.listView.clickRecord(1);
		sugar().documents.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().documents.editView.getEditField("documentName").assertVisible(true);
		sugar().documents.editView.getEditField("fileNameFile").assertVisible(true);
		sugar().documents.editView.getEditField("status").assertVisible(true);
		sugar().documents.editView.getEditField("description").assertVisible(true);
		sugar().documents.editView.getEditField("categoryID").assertVisible(true);
		sugar().documents.editView.getEditField("subCategoryID").assertVisible(true);
		VoodooUtils.focusDefault();
		sugar().documents.editView.cancel();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().documents.detailView.getDetailField("documentName").assertExists(true);
		sugar().documents.detailView.getDetailField("fileNameFile").assertExists(true);
		sugar().documents.detailView.getDetailField("status").assertExists(true);
		sugar().documents.detailView.getDetailField("description").assertExists(true);
		sugar().documents.detailView.getDetailField("categoryID").assertExists(true);
		sugar().documents.detailView.getDetailField("subCategoryID").assertExists(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	@Ignore("VOOD-1674 Need library support for column headers control in BWCListview")
	public void verifySortBy() throws Exception {
		VoodooUtils.voodoo.log.info("Documents/BWCListview sortBy() not implemented yet!");
	}

	@Ignore("VOOD-1674 Need library support for column headers control in BWCListview")
	public void verifyHeader() throws Exception {
		VoodooUtils.voodoo.log.info("Documents/BWCListview getHeaders() not implemented yet!");
	}

	public void cleanup() throws Exception {}
}