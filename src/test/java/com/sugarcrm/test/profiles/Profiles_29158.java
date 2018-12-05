package com.sugarcrm.test.profiles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Profiles_29158 extends SugarTest {
	FieldSet holidaysData;
	VoodooControl holidayDate, deleteBtnCtrl;

	public void setup() throws Exception {
		holidaysData = testData.get(testName).get(0);
		sugar.login();

		//  Navigate to User -> Profile
		sugar.navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");

		// Define Controls for Holidays page
		// TODO: VOOD-1643
		holidayDate = new VoodooControl("a", "css", "#list_subpanel_holidays tr.oddListRowS1 td:nth-child(1) a");

		// Now navigate to User Holidays and create a User holiday
		// TODO: TR-4734 - No Text box is assigned to Resource name (Mandatory Field)
		// TODO: VOOD-1643
		new VoodooControl("a", "id", "users_holidays_create_button").click();
		new VoodooControl("input", "id", "holiday_date").set(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		new VoodooControl("textarea", "css", "#form_SubpanelQuickCreate_Holidays_tabs #description").set(holidaysData.get("description"));
		new VoodooControl("input", "id", "Holidays_subpanel_save_button").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that Data not Available page should not be displayed, when user click on cancel button of a User Holiday record
	 * 
	 * @throws Exception
	 */
	@Test
	public void Profiles_29158_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to User -> Profile -> User Holidays sub panel
		sugar.navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");

		// Click on Holiday Date of a record which is created in the set up
		holidayDate.click();

		// Define Controls for Holidays page
		// TODO: VOOD-1643
		VoodooControl cancelBtnCtrl = new VoodooControl("input", "id", "CANCEL_HEADER");
		VoodooControl moduleTitle = new VoodooControl("h2", "css", ".moduleTitle h2");
		VoodooControl editBtnCtrl = new VoodooControl("input", "css", "input[name='Edit']");
		VoodooControl copyBtnCtrl = new VoodooControl("input", "css", "input[name='Duplicate']");
		VoodooControl descriptionFieldCtrl = new VoodooControl("slot", "css", "#content table:nth-child(4) tr:nth-child(2) td:nth-child(2) slot");
		deleteBtnCtrl = new VoodooControl("input", "css", "[title='Delete']");

		// Click on Edit button
		editBtnCtrl.click();

		// Click on Cancel button
		cancelBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that Data not Available page should not be displayed
		// Asserting the Holidays detail page elements shows that shows that after cancel the edit page Sugar instance remains on the detail page 
		moduleTitle.assertEquals(holidaysData.get("moduleTitle"), true);
		editBtnCtrl.assertVisible(true);
		copyBtnCtrl.assertVisible(true);
		deleteBtnCtrl.assertVisible(true);
		descriptionFieldCtrl.assertContains(holidaysData.get("description"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}