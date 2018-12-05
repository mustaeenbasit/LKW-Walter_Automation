package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_28207 extends SugarTest {
	VoodooControl moduleCtrl, layoutCtrl, searchBtnCtrl, filterSearchBtnCtrl, historyDefault, saveBtnCtrl;
	DataSource filterData;

	public void setup() throws Exception {
		filterData = testData.get(testName);
		sugar().login();
	}

	/**
	 * Verify that Contact address fields should not be disappear when removed from the Default Search list
	 * @throws Exception
	 */
	@Test
	public void Studio_28207_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-938
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		searchBtnCtrl = new VoodooControl("td", "id", "searchBtn");
		filterSearchBtnCtrl = new VoodooControl("td", "id", "FilterSearchBtn");
		historyDefault = new VoodooControl("input", "id", "historyDefault");
		saveBtnCtrl = new VoodooControl("td", "id", "savebtn");

		// 'address_street', 'address_city', 'address_state', 'address_postalcode', and 'address_country' field in the Default column
		VoodooControl address_streetDefault = new VoodooControl("li", "css", "#Default [data-name='address_street']");
		VoodooControl address_cityDefault = new VoodooControl("li", "css", "#Default [data-name='address_city']");
		VoodooControl address_stateDefault = new VoodooControl("li", "css", "#Default [data-name='address_state']");
		VoodooControl address_postalcodeDefault = new VoodooControl("li", "css", "#Default [data-name='address_postalcode']");
		VoodooControl address_countryDefault = new VoodooControl("li", "css", "#Default [data-name='address_country']");

		// 'address_street', 'address_city', 'address_state', 'address_postalcode', and 'address_country' field in the Hidden column
		VoodooControl address_streetHidden = new VoodooControl("li", "css", "#Hidden [data-name='address_street']");
		VoodooControl address_cityHidden = new VoodooControl("li", "css", "#Hidden [data-name='address_city']");
		VoodooControl address_stateHidden = new VoodooControl("li", "css", "#Hidden [data-name='address_state']");
		VoodooControl address_postalcodeHidden = new VoodooControl("li", "css", "#Hidden [data-name='address_postalcode']");
		VoodooControl address_countryHidden = new VoodooControl("li", "css", "#Hidden [data-name='address_country']");

		// Go to Contacts > Layouts > Search
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		searchBtnCtrl.click();
		VoodooUtils.waitForReady();
		filterSearchBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Verify the fields 'address_street', 'address_city', 'address_state', 'address_postalcode', and 'address_country' in the Default column
		address_streetDefault.assertExists(true);
		address_cityDefault.assertExists(true);
		address_stateDefault.assertExists(true);
		address_postalcodeDefault.assertExists(true);
		address_countryDefault.assertExists(true);

		// Drag and drop all five (5) of these fields into the Hidden column
		VoodooControl dropHiddenColumnCtrl = new VoodooControl("td", "id", "Hidden");
		address_streetDefault.dragNDrop(dropHiddenColumnCtrl);
		address_cityDefault.dragNDrop(dropHiddenColumnCtrl);
		address_stateDefault.dragNDrop(dropHiddenColumnCtrl);
		address_postalcodeDefault.dragNDrop(dropHiddenColumnCtrl);
		address_countryDefault.dragNDrop(dropHiddenColumnCtrl);

		// Save & Deploy
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Go to Contacts list view
		sugar().contacts.navToListView();

		// Create a new filter
		sugar().contacts.listView.openFilterDropdown();
		sugar().contacts.listView.selectFilterCreateNew();

		// TODO: VOOD-1879
		VoodooControl filterFieldCtrl = new VoodooSelect("a", "css", "[data-filter='field'] [data-voodoo-type='field'] a");
		VoodooControl filterFieldTextboxCtrl = new VoodooControl("input", "css", "#select2-drop div input");
		VoodooControl filterFieldSearchResultCtrl = new VoodooControl("span", "css", ".select2-results span");
		VoodooControl noMatchFoundCtrl = new VoodooControl("li", "css", ".select2-no-results");
		VoodooControl cancelFilterCtrl = new VoodooControl("a", "css", ".btn-link.btn-invisible.filter-close");

		// Verify that the five (5) address fields are not appears on the List View search filters.
		filterFieldCtrl.click();
		for(int i = 0; i< filterData.size(); i++) {
			filterFieldTextboxCtrl.set(filterData.get(i).get("filterValues"));
			noMatchFoundCtrl.assertContains(filterData.get(0).get("noMatchesFound"), true);
		}

		// Cancel the filter
		filterFieldTextboxCtrl.set(filterData.get(0).get("defaultValue")); // need to make cancel button clickable, therefore set default value 
		filterFieldSearchResultCtrl.click();
		cancelFilterCtrl.click();

		// Navigate to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Go to Contacts > Layouts > Search
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		searchBtnCtrl.click();
		VoodooUtils.waitForReady();
		filterSearchBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Drag and drop all five (5) of these fields back into the Default column
		VoodooControl dropDefaultColumnCtrl = new VoodooControl("td", "id", "Default");
		address_streetHidden.dragNDrop(dropDefaultColumnCtrl);
		address_cityHidden.dragNDrop(dropDefaultColumnCtrl);
		address_stateHidden.dragNDrop(dropDefaultColumnCtrl);
		address_postalcodeHidden.dragNDrop(dropDefaultColumnCtrl);
		address_countryHidden.dragNDrop(dropDefaultColumnCtrl);

		// Save & Deploy
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that the five (5) address fields will be saved to the Default Column 
		address_streetDefault.assertExists(true);
		address_cityDefault.assertExists(true);
		address_stateDefault.assertExists(true);
		address_postalcodeDefault.assertExists(true);
		address_countryDefault.assertExists(true);
		VoodooUtils.focusDefault();

		// Go to Contacts list view
		sugar().contacts.navToListView();

		// Create a new filter
		sugar().contacts.listView.openFilterDropdown();
		sugar().contacts.listView.selectFilterCreateNew();

		// Verify that the five (5) address fields will be usable as List View search filters.
		filterFieldCtrl.click();
		for(int i = 0; i< filterData.size(); i++) {
			filterFieldTextboxCtrl.set(filterData.get(i).get("filterValues"));
			filterFieldSearchResultCtrl.assertContains(filterData.get(i).get("filterValues"), true);
		}

		// Cancel the filter
		filterFieldTextboxCtrl.set(filterData.get(0).get("defaultValue")); // need to make cancel button clickable, therefore set default value 
		filterFieldSearchResultCtrl.click();
		cancelFilterCtrl.click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}