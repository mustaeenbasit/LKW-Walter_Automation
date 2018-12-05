package com.sugarcrm.test.calendar;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Shagun Kaushik <skaushik@sugarcrm.com>
 */
public class Calendar_20350 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify that click "Cancel" button in "User" dropdown list to close "Select users for calendar display" panel.
	 * @throws Exception
	 */
	@Test
	public void Calendar_20350_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-863: Lib support for Calendar module
		VoodooControl sharedBtn = new VoodooControl("input", "id", "shared-tab");
		VoodooControl userListBtn = new VoodooControl("button", "id", "userListButtonId");
		VoodooControl upArrow = new VoodooControl("a", "css", "#shared_cal_edit td:nth-child(3) a:nth-child(1)");
		VoodooControl administratorUserOption = new VoodooControl("option", "css", "#shared_ids option:nth-child(2)");
		VoodooControl qaUserOption = new VoodooControl("option", "css", "#shared_ids option:nth-child(3)");
		VoodooControl cancelButton = new VoodooControl("input", "css", "[value='Cancel']");
		VoodooControl currentUserPane = new VoodooControl("h5", "css", ".calSharedUser");
		VoodooControl userListPanel = new VoodooControl("div", "id", "shared_cal_edit_c");
		VoodooControl firstPlaceOption = new VoodooControl("option", "css", "#shared_ids option:nth-child(1)");
		
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		sharedBtn.click();
		userListBtn.click();
		
		// Selecting the 'Administrator' option from the user dropdown to deselect it
		administratorUserOption.click();
		
		// Selecting the 'qauser' option from the user dropdown
		qaUserOption.click();
		
		// Moving 'qauser' to first place from third place
		upArrow.click();
		upArrow.click();
		
		// Selecting Cancel button
		cancelButton.click();
		
		// Verifying that 'User List' panel is closed
		userListPanel.assertVisible(false);
		
		// Verifying that the result view doesn't change i.e. it is still displayed for user Administrator
		currentUserPane.assertEquals("Administrator", true);
		
		userListBtn.click();
		
		// Verifying that user 'qauser' is displayed on the first place
		firstPlaceOption.assertEquals((sugar.users.getQAUser().get("userName")), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}

