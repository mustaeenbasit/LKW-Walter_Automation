package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Users_29767 extends SugarTest {
	FieldSet customFS = new FieldSet();
	UserRecord chris;

	public void setup() throws Exception {
		customFS = testData.get(testName).get(0);
		chris = (UserRecord)sugar().users.api.create();
		sugar().login();

		// Create a holiday for this user under "User Holidays" subpanel.
		sugar().users.navToListView();
		VoodooUtils.waitForReady();
		sugar().users.listView.clickRecord(1);

		// TODO: VOOD-1643
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "users_holidays_create_button").click();
		new VoodooControl("input", "id", "holiday_date").set(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		new VoodooControl("input", "css", "#detailpanel_1 #description").set(customFS.get("holiday_description"));
		new VoodooControl("input", "id", "Holidays_subpanel_save_button").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify "Holidays" page is displayed when clicked on "cancel" button of a user holiday record.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_29767_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Click on created holiday record
		// TODO: VOOD-1643
		new VoodooControl("a", "css", "#list_subpanel_holidays tr.oddListRowS1 td a").click();

		// Verify holidays page
		VoodooControl holiday = new VoodooControl("a", "css", ".moduleTitle h2");
		holiday.assertEquals(customFS.get("holiday_module_title"), true);
		new VoodooControl("td", "css", "tr:nth-of-type(1) td:nth-of-type(2)").assertContains(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"), true);
		new VoodooControl("td", "css", "tr:nth-of-type(2) td:nth-of-type(2)").assertContains(customFS.get("holiday_description"), true);

		// Click on Edit and click on cancel
		new VoodooControl("input", "css", "[title='Edit']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "CANCEL_HEADER").click();
		VoodooUtils.waitForReady();

		// Verify that 'Holidays' page is displayed
		holiday.assertEquals(customFS.get("holiday_module_title"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}