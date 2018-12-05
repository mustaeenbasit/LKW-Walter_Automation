package com.sugarcrm.test.quotes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Quotes_18603 extends SugarTest {
	DataSource systemSettingsData;

	public void setup() throws Exception {
		systemSettingsData = testData.get(testName);

		// Create four Quotes record
		FieldSet quotesName = new FieldSet();
		for(int i = 4; i > 0; i--) {
			quotesName.put("name", testName+"_"+i);
			sugar.quotes.api.create(quotesName);
			quotesName.clear();
		}

		// Login to sugar
		sugar.login();

		// Navigate to Admin -> System Setting -> Set "List View Items" to be 3.
		sugar.admin.setSystemSettings(systemSettingsData.get(0));
	}

	/**
	 *[delete record]-Select all records-Verify that no records are deleted after pagination
	 * @throws Exception
	 */
	@Test
	public void Quotes_18603_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// go to any bwc module(for e.g Quotes).In list view,select all records in "select" dropdown list
		sugar.quotes.navToListView();
		sugar.quotes.listView.openSelectDropdown();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.quotes.listView.getControl("selectAll").click();

		// Click "next" button
		sugar.quotes.listView.getControl("nextButton").click();
		VoodooUtils.focusDefault();

		// Click "Delete" button
		sugar.quotes.listView.delete();

		// Verify that message display "Are you sure you want to delete the * selected records" (Not able to assert this message due to VOOD-1045)
		// VOOD-1045: Library support needed for asserting the message appeared in javascript dialog box.

		// Click "cancel" button
		VoodooUtils.dismissDialog();

		// Verify that Cancel dialog box returns to list view page
		sugar.quotes.listView.getControl("moduleTitle").assertContains(sugar.quotes.moduleNamePlural, true);
		sugar.quotes.listView.getControl("selectAllCheckbox").assertVisible(true);

		// Verify that no records are deleted(On second page)
		VoodooUtils.focusDefault();
		sugar.quotes.listView.verifyField(1, "name", testName+"_"+4);

		// Click "previous" button
		VoodooUtils.focusFrame("bwc-frame");
		sugar.quotes.listView.getControl("prevButton").click();
		VoodooUtils.focusDefault();

		// Verify that no records are deleted(On first page)
		for(int i = 1; i < 4; i++) {
			sugar.quotes.listView.verifyField(i, "name", testName+"_"+i);
		}

		// Deselect all records
		sugar.quotes.listView.openSelectDropdown();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.quotes.listView.getControl("deselectAll").click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}