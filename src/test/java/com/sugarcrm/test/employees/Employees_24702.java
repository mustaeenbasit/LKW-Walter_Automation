package com.sugarcrm.test.employees;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_24702 extends SugarTest {
	public void setup() throws Exception {
		sugar.login(sugar.users.getQAUser());
	}

	/**
	 * Verify that no check box is displayed in employee list view when the current user is not an admin privilege user.
	 * @throws Exception
	 */
	@Test
	public void Employees_24702_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
		
		// TODO VOOD-1041
		sugar.navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();
		// sometime can't find the bwc frame without the wait
		VoodooUtils.pause(1000);
		VoodooUtils.focusFrame("bwc-frame");	
		new VoodooControl("div", "css", ".listViewBody").waitForVisible();	
		new VoodooControl("input", "css", "input.checkbox.massall").assertExists(false);
		new VoodooControl("input", "css", "table.list.view tr.oddListRowS1 input.checkbox").assertExists(false);
		new VoodooControl("input", "css", "table.list.view tr.evenListRowS1 input.checkbox").assertExists(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}