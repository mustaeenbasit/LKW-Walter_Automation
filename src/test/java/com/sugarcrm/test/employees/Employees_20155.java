package com.sugarcrm.test.employees;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Employees_20155 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		sugar.login();		
	}

	/**
	 * Verify that uncheck "display employee record" checkbox  of a user hides the record in the Employee module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Employees_20155_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		ds = testData.get(testName);
		
		// create user with uncheck "display employee record" checkbox
		sugar.users.navToListView();
		sugar.navbar.selectMenuItem(sugar.users, "createNewUser");
		VoodooUtils.focusFrame("bwc-frame"); 		
		sugar.users.editView.getEditField("firstName").set(ds.get(0).get("firstName"));
		sugar.users.editView.getEditField("lastName").set(ds.get(0).get("lastName"));
		sugar.users.editView.getEditField("userName").set(ds.get(0).get("userName"));
		sugar.users.editView.getEditField("emailAddress").set(ds.get(0).get("email"));
		
		// TODO VOOD-1065
		new VoodooControl("input", "id", "show_on_employees").set(ds.get(0).get("display"));
		sugar.users.editView.getControl("passwordTab").click();		
		sugar.users.editView.getEditField("newPassword").set(ds.get(0).get("password"));
		sugar.users.editView.getEditField("confirmPassword").set(ds.get(0).get("password"));
		sugar.users.editView.getControl("save").click();
		sugar.users.editView.getControl("confirmCreate").click();
		
		// will fail to click employees link without the wait
		VoodooUtils.pause(1000);
		VoodooUtils.focusDefault();
		sugar.navbar.toggleUserActionsMenu();
		
		// TODO VOOD-1041
		new VoodooControl("a", "css", "li.profileactions-employees a").click();	
		
		// sometime can't find the bwc frame without the wait
		VoodooUtils.pause(1000);
		VoodooUtils.focusFrame("bwc-frame");
		
		// check the employee doesn't display
		new VoodooControl("input", "id", "search_name_basic").set(ds.get(0).get("firstName"));
		new VoodooControl("input", "id", "search_form_submit").click();
		new VoodooControl("a", "css", "table.list.view tr.oddListRowS1").assertExists(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}