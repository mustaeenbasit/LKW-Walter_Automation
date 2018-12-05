package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21546 extends SugarTest {
	DataSource ds = new DataSource();
	VoodooControl saveBtnCtrl;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().login();

		// Edit dashboard
		sugar().dashboard.edit();

		// TODO: VOOD-960 - Dashlet selection 
		// TODO: VOOD-1004 - Library support need to create dashlet
		sugar().dashboard.addRow();
		sugar().dashboard.addDashlet(4, 1);
		new VoodooControl("input", "css", ".layout_Home input[placeholder='Search by Title, Description...']").set(ds.get(0).get("listView"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();
		saveBtnCtrl = new VoodooControl("a", "css", "#drawers .fld_save_button a");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify dashlet can be edited or configured
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21546_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1004 - Library support need to create dashlet
		VoodooControl dashletTitleCtrl = new VoodooControl("h4", "css", ".layout_Home li:nth-child(4) .row-fluid.layout_Home h4");
		VoodooControl accountNameCtrl = new VoodooControl("span", "css", "th[data-fieldname='name']");
		VoodooControl accountCityCtrl =  new VoodooControl("span", "css", "th[data-fieldname='billing_address_city']");
		VoodooControl accountAddressCtrl = new VoodooControl("span", "css", "th[data-fieldname='billing_address_country']");

		// assert dashlet options before modifying the dashlet options.
		// Verify dashlet name
		dashletTitleCtrl.assertContains(ds.get(0).get("module_name"), true);

		// Verify columns names
		accountNameCtrl.assertContains(ds.get(0).get("fieldName1"), true);
		accountCityCtrl.assertContains(ds.get(0).get("fieldName2"), true);
		accountAddressCtrl.assertContains(ds.get(0).get("fieldName3"), true);

		// In "My Dashboard", click on the Configure icon(Gear icon) in the "List View"
		// TODO: VOOD-963 - Some dashboard controls are needed
		new VoodooControl("i", "css", "li.row-fluid.sortable:nth-of-type(4) .btn-group button i").click();
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "li.row-fluid.sortable:nth-of-type(4) .btn-group .dropdown-menu li").click();
		VoodooSelect moduleContainer = new VoodooSelect("div", "css", "[data-name='module'] .select2-container");
		moduleContainer.waitForVisible();
		moduleContainer.set(sugar().calls.moduleNamePlural);
		VoodooUtils.waitForReady();

		// Save
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Verify dashlet options after modifying the dashlet options.
		// Verify dashlet name
		dashletTitleCtrl.assertContains(ds.get(1).get("module_name"), true);

		// Verify columns names
		// TODO: VOOD-963 - Some dashboard controls are needed
		accountNameCtrl.assertContains(ds.get(1).get("fieldName1"), true);
		new VoodooControl("span", "css", "th[data-fieldname='parent_name']").assertContains(ds.get(1).get("fieldName2"), true);
		new VoodooControl("span", "css", "th[data-fieldname='date_start']").assertContains(ds.get(1).get("fieldName3"), true);

		// Cancel the Dashboard edit mode
		// TODO: VOOD-963 - Some dashboard controls are needed
		new VoodooControl("a", "css", ".fld_cancel_button a").click();
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}