package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21185 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify list view dashlet can be displayed correctly
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21185_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet dashletName = testData.get(testName).get(0);

		// Homepage > edit (under create button) > add a dashlet
		sugar().dashboard.edit();
		// TODO: VOOD-960 - Dashlet selection 
		// TODO: VOOD-958 - sugar().accounts.dashboard.addDashlet(1, 3); fails  
		// NOT WORKING: sugar().accounts.dashboard.addDashlet(row, column);
		new VoodooControl("div", "css", ".layout_Home li:nth-child(3) [data-voodoo-name='dashlet-row-empty']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", ".dashlet-container .layout_Home [data-action='addDashlet']").click();
		VoodooUtils.waitForReady();
		// Select "list view" from the list
		new VoodooControl("input", "css", ".layout_Home input[placeholder='Search by Title, Description...']").set(dashletName.get("listView"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();

		// Select a module like Case module
		// TODO: VOOD-1004 - Library support need to create dashlet
		new VoodooSelect("div", "css", "[data-name='module'] .select2-container").set(sugar().cases.moduleNamePlural);

		// Select "All cases" from Default Data Filter.
		// TODO: VOOD-1004 - Library support need to create dashlet
		new VoodooControl("a", "css", "span[data-voodoo-name='filter-filter-dropdown'] .select2-container a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("ul", "css", ".search-filter-dropdown li:nth-child(2)").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#drawers .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Verify case module list view with default columns should be shown on the dashboard.
		// TODO: VOOD-1004 - Library support need to create dashlet
		new VoodooControl("h4", "css", "li.layout_Home.span8 li:nth-child(3) h4").assertContains(dashletName.get("filterName"), true);
		new VoodooControl("th", "css", "th[data-fieldname='case_number']").assertContains(dashletName.get("column1"), true);
		new VoodooControl("th", "css", "th[data-fieldname='name']").assertContains(dashletName.get("column2"), true);
		new VoodooControl("th", "css", "th[data-fieldname='account_name']").assertContains(dashletName.get("column3"), true);
		new VoodooControl("th", "css", "th[data-fieldname='priority']").assertContains(dashletName.get("column4"), true);
		new VoodooControl("th", "css", "th[data-fieldname='status']").assertContains(dashletName.get("column5"), true);
		new VoodooControl("th", "css", "th[data-fieldname='assigned_user_name']").assertContains(dashletName.get("column6"), true);
		new VoodooControl("th", "css", "th[data-fieldname='date_modified']").assertContains(dashletName.get("column7"), true);
		new VoodooControl("th", "css", "th[data-fieldname='date_entered']").assertContains(dashletName.get("column8"), true);
		new VoodooControl("th", "css", "th[data-fieldname='team_name']").assertContains(dashletName.get("column9"), true);

		// Cancel the Dashboard edit mode
		// TODO: VOOD-963 - Some dashboard controls are needed
		new VoodooControl("a", "css", ".fld_cancel_button a").click();
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}