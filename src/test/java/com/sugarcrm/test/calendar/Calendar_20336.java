package com.sugarcrm.test.calendar;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Raina <araina@sugarcrm.com>
 */
public class Calendar_20336 extends SugarTest {
	UserRecord myUser;
	public void setup() throws Exception {
		sugar.login();
		
		myUser = (UserRecord)sugar.users.create();
	}

	/**
	 * Verify that call is displayed in "My Calls" dashlet  of the invited users Home page.
	 * @throws Exception
	 */
	@Test
	public void Calendar_20336_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Schedule Call" link in "Calendar" navigation tab
		sugar.navbar.selectMenuItem(sugar.calendar, "scheduleCall");

		// Enter required subject, add invitee QAuser and save.
		sugar.calls.createDrawer.getEditField("name").set(testName);
		sugar.calls.createDrawer.clickAddInvitee();
		sugar.calls.createDrawer.selectInvitee(sugar.users.getDefaultData().get("fullName"));
		sugar.calls.createDrawer.save();
		
		// Logout Admin and login with QAuser 
		sugar.logout();
		sugar.login(myUser);

		// Create dashboard and add a dashlet
		sugar.dashboard.clickCreate();
		sugar.dashboard.getControl("title").set(testName);
		sugar.dashboard.addRow();
		sugar.dashboard.addDashlet(1, 1);
		
		// Choose Planned Activities dashlet and save
		// TODO: VOOD-960 - Dashlet Selection
		new VoodooControl("a", "css", ".table-striped tr:nth-child(12) a").click();
		new VoodooControl("a", "css", ".active .fld_save_button a").click();
		VoodooUtils.waitForReady();
		sugar.dashboard.save();
		
		// TODO: VOOD-1305 Need lib support for RHS Planned Activities Dashlets
		// Verify call is in Planned Dashlet under Calls tab 
		new VoodooControl("div", "css", "div[data-voodoo-name='planned-activities'] "
				+ "	.dashlet-unordered-list .dashlet-tabs-row div.dashlet-tab:nth-of-type(2)").click();
		new VoodooControl("div", "css", "div.tab-pane.active p a:nth-of-type(2)")
			.assertEquals(testName, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
