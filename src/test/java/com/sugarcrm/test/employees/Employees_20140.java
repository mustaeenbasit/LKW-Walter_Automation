package com.sugarcrm.test.employees;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20140 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		sugar.login();		
	}

	/**
	 * Group user shouldn't show in employees module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Employees_20140_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		ds = testData.get(testName);
		sugar.users.navToListView();
		sugar.navbar.clickModuleDropdown(sugar.users);
		// TODO VOOD-1053
		new VoodooControl("a", "css", "li[data-module='Users'] a[data-navbar-menu-item='LNK_NEW_GROUP_USER']").click();
		VoodooUtils.focusFrame("bwc-frame"); 		
		new VoodooControl("div", "id", "EditViewGroup_tabs").waitForVisible();
		new VoodooControl("input", "id", "user_name").set(ds.get(0).get("userName"));
		new VoodooControl("input", "id", "last_name").set(ds.get(0).get("name"));
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		new VoodooControl("div", "id", "Users_detailview_tabs").waitForVisible();
		// will fail to click employees link without the wait
		VoodooUtils.pause(1000);
		VoodooUtils.focusDefault();
		sugar.navbar.toggleUserActionsMenu();
		// TODO VOOD-1041
		new VoodooControl("a", "css", "li.profileactions-employees a").click();
		// sometime can't find the bwc frame without the wait
		VoodooUtils.pause(1000);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "search_name_basic").set(ds.get(0).get("name"));
		new VoodooControl("input", "id", "search_form_submit").click();
		new VoodooControl("a", "css", "table.list.view tr.oddListRowS1").assertExists(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}