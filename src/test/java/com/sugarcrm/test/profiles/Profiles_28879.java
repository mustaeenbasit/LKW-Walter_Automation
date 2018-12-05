package com.sugarcrm.test.profiles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Profiles_28879 extends SugarTest {
	VoodooControl firstHolidayDate;

	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify that copying a holiday should not override the existing holiday details with New changes
	 * 
	 * @throws Exception
	 */
	@Test
	public void Profiles_28879_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	

		FieldSet holidaysData = testData.get(testName).get(0);

		//  Navigate to user->profile
		sugar.navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");

		// Define Controls for Holidays page
		// TODO: VOOD-1643
		firstHolidayDate = new VoodooControl("a", "css", "#list_subpanel_holidays tr.oddListRowS1 td:nth-child(1) a");
		VoodooControl descriptionDetailFieldCtrl = new VoodooControl("slot", "css", "#content table:nth-child(4) tr:nth-child(2) td:nth-child(2) slot");

		// Now navigate to User Holidays and create a User holiday
		// TODO: TR-4734 - No Text box is assigned to Resource name (Mandatory Field)
		// TODO: VOOD-1643
		new VoodooControl("a", "id", "users_holidays_create_button").click();
		new VoodooControl("input", "id", "holiday_date").set(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		new VoodooControl("textarea", "css", "#form_SubpanelQuickCreate_Holidays_tabs #description").set(holidaysData.get("description"));
		new VoodooControl("input", "id", "Holidays_subpanel_save_button").click();
		VoodooUtils.waitForReady();

		// Now open this newly created Holiday
		firstHolidayDate.click();

		// Now click on copy
		new VoodooControl("input", "css", "input[name='Duplicate']").click();

		// Now Edit the changes for newly copied holiday and save the changes
		new VoodooControl("textarea", "id", "description").set(holidaysData.get("updatedDescription"));
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Go to Admin -> Profile -> User Holidays sub panel
		sugar.navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that Copying a holiday should not override the holiday details with the existing holiday
		// Using xPath to find specific holiday records as order of the created and updated holiday records is not fix and thats why the shorting option is also not working properly
		new VoodooControl("a", "xpath", "//*[@id='list_subpanel_holidays']/table/tbody/tr[contains(.,'"+ holidaysData.get("description") +"')]/td[1]/span/a").click();
		descriptionDetailFieldCtrl.assertContains(holidaysData.get("description"), true);
		VoodooUtils.focusDefault();

		// Go to Admin -> Profile -> User Holidays sub panel
		sugar.navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that Copying functionality should create another holiday with same or different details
		// Using xPath to find specific holiday records as order of the created and updated holiday records is not fix and thats why the shorting option is also not working properly
		new VoodooControl("a", "xpath", "//*[@id='list_subpanel_holidays']/table/tbody/tr[contains(.,'"+ holidaysData.get("updatedDescription") +"')]/td[1]/span/a").click();
		descriptionDetailFieldCtrl.assertContains(holidaysData.get("updatedDescription"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}