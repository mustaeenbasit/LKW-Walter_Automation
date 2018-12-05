package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_24751 extends SugarTest {

	public void setup() throws Exception {
		sugar().cases.api.create();
		sugar().login();
	}

	/**
	 * Click user name on Dashlet should bring you to the user detail view.
	 * @throws Exception
	 */
	@Test
	public void Users_24751_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet dataRecord = testData.get(testName).get(0);

		// Define Controls for Dashlets
		// TODO: VOOD-960 - Dashlet selection
		VoodooControl dashletSearchCtrl = new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input");
		VoodooControl dashletSelectCtrl = new VoodooControl("a", "css", ".list-view .fld_title a");
		VoodooControl selectModuleCtrl = new VoodooSelect("div", "css", ".fld_module .select2-container");
		VoodooControl saveBtnCtrl = new VoodooControl("a", "css", ".layout_Home.drawer.active .fld_save_button a");

		// Creating List View dashlet for My Cases
		sugar().home.dashboard.edit();
		sugar().home.dashboard.addRow();
		sugar().home.dashboard.addDashlet(4, 1);
		dashletSearchCtrl.set(dataRecord.get("dashletName"));
		VoodooUtils.waitForReady();
		dashletSelectCtrl.click();
		VoodooUtils.waitForReady();
		selectModuleCtrl.set(sugar().cases.moduleNamePlural);
		VoodooUtils.waitForReady();
		saveBtnCtrl.click();

		// Save the home page dashboard
		// TODO: VOOD-1645 - Need to update method(s) in 'Dashboard.java' for Edit page.
		new VoodooControl("a", "css", ".fld_save_button a").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-960 - Dashlet selection
		new VoodooControl("a", "css", ".table-striped.dataTable .fld_assigned_user_name a").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1041 - need lib support of employees module
		new VoodooControl("span", "id", "first_name").assertEquals(dataRecord.get("userName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
