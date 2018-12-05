package com.sugarcrm.test.profiles;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Profiles_28797 extends SugarTest {
	VoodooControl holidayDate;
	
	public void setup() throws Exception {
		sugar.login();
		
		// Create User Holidays in Admin -> Profile -> User Holidays sub panel
		// TODO: VOOD-1643
		sugar.navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "users_holidays_create_button").click();
		new VoodooControl("input", "id", "holiday_date").set(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		new VoodooControl("input", "id", "Holidays_subpanel_save_button").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify that User holidays in Profile does not disappear from user holidays subpanel 
	 * when saved from edit page
	 * 
	 * @throws Exception
	 */
	@Test
	public void Profiles_28797_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
		
		// Click on created holiday record(date field)
		// TODO: VOOD-1643
		holidayDate = new VoodooControl("a", "css", "#list_subpanel_holidays tr.oddListRowS1 td:nth-child(1) a");
		holidayDate.click();
		
		// Click on Edit and click on save
		new VoodooControl("input", "css", "[title='Edit']").click();
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Go to Admin -> Profile -> User Holidays sub panel
		sugar.navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that 'User holidays' record is displayed
		holidayDate.assertVisible(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}