package com.sugarcrm.test.employees;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20152 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * New action dropdown list in employee detail view page
	 * 
	 * @throws Exception
	 */
	@Test
	public void Employees_20152_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);
		// open employee detail view
		// TODO VOOD-1041
		sugar.navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();	
		// sometime can't find the bwc frame without the wait
		VoodooUtils.pause(1000);
		VoodooUtils.focusFrame("bwc-frame");	
		new VoodooControl("div", "css", ".listViewBody").waitForVisible();
		new VoodooControl("input", "id", "search_name_basic").set(sugar.users.getQAUser().get("firstName"));
		new VoodooControl("input", "id", "search_form_submit").click();
		new VoodooControl("a", "css", "table.list.view tr.oddListRowS1 td:nth-child(3) a").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "id", "Employees_detailview_tabs").waitForVisible();
		
		// check available actions
		new VoodooControl("a", "id", "edit_button").assertEquals(ds.get(0).get("edit"),true);
		new VoodooControl("span", "css", "li.sugar_action_button span.ab").click();
		new VoodooControl("a", "id", "duplicate_button").assertEquals(ds.get(0).get("copy"),true);
		new VoodooControl("a", "id", "delete_button").assertEquals(ds.get(0).get("delete"),true);		
		new VoodooControl("a", "id", "edit_button").click();
		new VoodooControl("input", "id", "last_name").assertEquals(sugar.users.getQAUser().get("lastName"),true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}