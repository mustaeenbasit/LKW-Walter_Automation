package com.sugarcrm.test.ListView;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_29118 extends SugarTest {
	FieldSet filterData;

	public void setup() throws Exception {
		filterData = testData.get(testName).get(0);
		sugar.login();
	}

	/**
	 * Verify that "Today" filter is appearing in List view and Dashlet
	 */
	@Test
	public void ListView_29118_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verifying "Today" operator appearing in List View for a filter based on date
		sugar.accounts.navToListView();
		// Create a filter based on date "Date created" 
		sugar.accounts.listView.openFilterDropdown();
		sugar.accounts.listView.selectFilterCreateNew();
		// TODO: VOOD-1478
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set(filterData.get("filterField"));
		// Click on the dropdown in front of earlier dropdown
		new VoodooControl("span", "css", ".detail.fld_filter_row_operator").click();
		VoodooUtils.waitForReady();

		// Verify One of the value should be "today"
		new VoodooControl("ul", "css", "#select2-drop ul").assertContains(filterData.get("Operator"), true);

		new VoodooControl("li", "css", "#select2-drop ul li:nth-child(5)").click();
		// Cancel filter creation
		// TODO: VOOD-1570
		new VoodooControl("a", "css", "div[data-voodoo-name='Accounts'] .filter-options.extend .filter-close").click();

		// Verifying "Today" operator appearing in Dashlet for a filter based on date
		sugar.navbar.navToModule(sugar.home.moduleNamePlural);
		// Go to any dashlet
		// TODO: VOOD-1004
		new VoodooControl("a", "css", ".layout_Home li:nth-child(3) .row-fluid.layout_Home li div:nth-child(1) div > a").click();
		new VoodooControl("li", "css", ".row-fluid.sortable:nth-of-type(3) .btn-group .dropdown-menu li:nth-of-type(1)").click();
		VoodooUtils.waitForReady();
		new VoodooControl("span", "css", ".filter-view.search.layout_Home a span.select2-chosen").click();
		new VoodooControl("li", "css", "#select2-drop ul li:nth-child(1)").click();
		new VoodooSelect("a", "css", ".detail.fld_filter_row_name a").click();

		// Select "Date Created" field
		new VoodooControl("li", "css", "#select2-drop ul li:nth-child(15)").click();
		new VoodooSelect("span", "css", ".detail.fld_filter_row_operator").click();

		// Verify the dropdown should have the value "today" 
		new VoodooControl("div", "id", "select2-drop").assertContains(filterData.get("Operator"), true);

		new VoodooControl("li", "css", "#select2-drop ul li:nth-child(2)").click();
		// Cancel
		new VoodooControl("a", "css", ".main-pane.span8 span.detail.fld_cancel_button a").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}