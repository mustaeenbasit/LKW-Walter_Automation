package com.sugarcrm.test.grimoire;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.QuoteRecord;
import com.sugarcrm.test.SugarTest;

public class BWCListViewTests extends SugarTest {
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void basicSearch() throws Exception {
		VoodooUtils.voodoo.log.info("Running basicSearch()...");

		QuoteRecord myQuote = (QuoteRecord)sugar().quotes.api.create();
		sugar().quotes.navToListView();
		sugar().quotes.listView.basicSearch("Blank");
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl link = sugar().quotes.listView.getControl("link01");
		link.assertExists(false);
		VoodooUtils.focusDefault();
		sugar().quotes.listView.basicSearch(myQuote.get("name"));
		VoodooUtils.focusFrame("bwc-frame");
		link.assertVisible(true);
		VoodooUtils.focusDefault();
		sugar().quotes.listView.basicSearch("");

		VoodooUtils.voodoo.log.info("basicSearch() test complete.");
	}

	@Test
	public void checkUncheckRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running checkUncheckRecord()...");

		sugar().quotes.api.create();
		sugar().quotes.api.create();

		sugar().quotes.navToListView();
		sugar().quotes.listView.checkRecord(2);
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.listView.getControl("checkbox02").assertAttribute("checked", "true");
		VoodooUtils.focusDefault();

		// TODO: VOOD-436
		//		sugar().quotes.listView.uncheckRecord(2);
		//		VoodooUtils.focusFrame("bwc-frame");
		//		sugar().quotes.listView.getControl("checkbox02").assertAttribute("checked", "false");
		//		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("checkUncheckRecord() test complete.");
	}

	@Test
	public void clearSearchForm() throws Exception {
		VoodooUtils.voodoo.log.info("Running clearSearchForm()...");

		sugar().quotes.api.create();
		sugar().quotes.navToListView();
		sugar().quotes.listView.basicSearch("Blank");
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl link = sugar().quotes.listView.getControl("link01");
		link.assertExists(false);
		VoodooUtils.focusDefault();
		sugar().quotes.listView.basicSearch("");
		VoodooUtils.focusFrame("bwc-frame");
		link.assertVisible(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("clearSearchForm() test complete.");
	}

	@Test
	public void clickRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running clickRecord()...");

		QuoteRecord myQuote = (QuoteRecord)sugar().quotes.api.create();
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.detailView.getDetailField("name").assertContains(myQuote.getRecordIdentifier(), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("clickRecord() test complete.");
	}

	@Test
	public void deleteRecordAndConfirm() throws Exception {
		VoodooUtils.voodoo.log.info("Running deleteRecordAndConfirm()...");

		sugar().quotes.api.create();
		sugar().quotes.navToListView();
		sugar().quotes.listView.deleteRecord(1);
		sugar().quotes.listView.confirmDelete();
		sugar().quotes.listView.getControl("link01").assertExists(false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("deleteRecordAndConfirm() test complete.");
	}

	@Test
	public void editRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running editRecord()...");

		sugar().quotes.api.create();
		sugar().quotes.navToListView();
		sugar().quotes.listView.editRecord(1);

		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.editView.getEditField("name").assertVisible(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("editRecord() test complete.");
	}

	@Test
	public void getModuleTitle() throws Exception {
		VoodooUtils.voodoo.log.info("Running getModuleTitle()...");

		sugar().quotes.navToListView();
		String expected = sugar().quotes.moduleNamePlural;
		String found = sugar().quotes.listView.getModuleTitle();
		assertTrue("getModuleTitle did not return " + expected + "; " + found + " was found instead.", found.contains(expected));

		VoodooUtils.voodoo.log.info("getModuleTitle() test complete.");
	}

	@Test
	public void massUpdate() throws Exception {
		VoodooUtils.voodoo.log.info("Running massUpdate()...");

		sugar().quotes.api.create();
		sugar().quotes.navToListView();
		sugar().quotes.listView.checkRecord(1);
		sugar().quotes.listView.openActionDropdown();
		sugar().quotes.listView.massUpdate();

		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-768, VOOD-1723
		new VoodooControl("input", "css", "#mass_assigned_user_name").assertVisible(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("massUpdate() test complete.");
	}

	@Test
	public void selectionMenu() throws Exception {
		VoodooUtils.voodoo.log.info("Running selectionMenu()...");

		sugar().quotes.api.create();
		sugar().quotes.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.listView.getControl("selectAllCheckbox").assertVisible(true);
		VoodooUtils.focusDefault();
		sugar().quotes.listView.openSelectDropdown();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.listView.getControl("selectThisPage").assertVisible(true);
		sugar().quotes.listView.getControl("selectAll").assertVisible(true);
		sugar().quotes.listView.getControl("deselectAll").assertVisible(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("selectionMenu() test complete.");
	}

	@Test
	public void actionMenu() throws Exception {
		VoodooUtils.voodoo.log.info("Running actionMenu()...");

		sugar().quotes.api.create();
		sugar().quotes.navToListView();
		sugar().quotes.listView.checkRecord(1);
		sugar().quotes.listView.openActionDropdown();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.listView.getControl("deleteButton").assertVisible(true);
		sugar().quotes.listView.getControl("massUpdateButton").assertVisible(true);
		sugar().quotes.listView.getControl("exportButton").assertVisible(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("actionMenu() test complete.");
	}

	@Test
	public void elements() throws Exception {
		VoodooUtils.voodoo.log.info("Running elements()...");

		sugar().quotes.api.create();
		sugar().quotes.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.listView.getControl("moduleTitle").assertExists(true);
		sugar().quotes.listView.getControl("nameBasic").assertExists(true);
		sugar().quotes.listView.getControl("myItemsCheckbox").assertExists(true);
		sugar().quotes.listView.getControl("myFavoritesCheckbox").assertExists(true);
		sugar().quotes.listView.getControl("searchButton").assertExists(true);
		sugar().quotes.listView.getControl("clearButton").assertExists(true);
		sugar().quotes.listView.getControl("advancedSearchLink").assertExists(true);
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

		VoodooUtils.voodoo.log.info("elements() test complete");
	}

	@Test
	public void toggles() throws Exception {
		VoodooUtils.voodoo.log.info("Running toggles()...");

		sugar().quotes.api.create();
		VoodooUtils.pause(2000); // to ensure order of created calls
		FieldSet callData = new FieldSet();
		callData.put("name", "Test Call");
		sugar().quotes.api.create(callData);

		sugar().quotes.navToListView();
		sugar().quotes.listView.toggleFavorite(1);
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl myFavoriteCheckbox = sugar().quotes.listView.getControl("myFavoritesCheckbox");
		myFavoriteCheckbox.set("true");
		VoodooUtils.focusDefault();
		sugar().quotes.listView.submitSearchForm();
		sugar().quotes.listView.verifyField(1, "name", callData.get("name"));
		VoodooUtils.focusDefault();
		sugar().quotes.listView.toggleFavorite(1);
		VoodooUtils.focusFrame("bwc-frame");
		myFavoriteCheckbox.set("false");
		VoodooUtils.focusDefault();
		sugar().quotes.listView.submitSearchForm();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1426
		VoodooControl element = new VoodooControl("a", "css", ".list.view");
		element.assertElementContains(callData.get("name"), true);
		element.assertElementContains(sugar().quotes.defaultData.get("name"), true);
		VoodooUtils.focusDefault();

		sugar().quotes.listView.clearSearchForm();
		sugar().quotes.listView.submitSearchForm();
		sugar().quotes.listView.toggleRecordCheckbox(2);
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.listView.getControl("checkbox02").assertAttribute("checked", "true");
		VoodooUtils.focusDefault();

		// TODO: VOOD-436
		//		sugar().quotes.listView.toggleRecordCheckbox(2);
		//		sugar().quotes.listView.getControl("checkbox02").assertAttribute("checked", "false");

		sugar().quotes.listView.toggleSelectAll();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.listView.getControl("checkbox01").assertAttribute("checked", "true");
		sugar().quotes.listView.getControl("checkbox02").assertAttribute("checked", "true");
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("toggles() test complete");
	}

	@Test
	public void verifyField() throws Exception {
		VoodooUtils.voodoo.log.info("Running toggles()...");

		sugar().quotes.api.create();
		sugar().quotes.navToListView();
		sugar().quotes.listView.verifyField(1, "name", sugar().quotes.defaultData.get("name"));

		VoodooUtils.voodoo.log.info("verifyField() test complete");
	}

	@Test
	public void countRows() throws Exception {
		VoodooUtils.voodoo.log.info("Running countRows()...");

		sugar().campaigns.navToListView();

		// Verify no record count
		int row = sugar().campaigns.listView.countRows();
		Assert.assertTrue("Number of rows did not equal zero.", row == 0);

		// Verify 1 record count
		sugar().campaigns.api.create();
		VoodooUtils.refresh(); // to populate data

		row = sugar().campaigns.listView.countRows();
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

	public void cleanup() throws Exception {}
}