package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_24729 extends SugarTest {
	VoodooControl layoutOptionCtrl;
	VoodooControl advancedSearchBtnCtrl;
	UserRecord myUser;

	public void setup() throws Exception {
		myUser = (UserRecord)sugar().users.api.create();

		// Initializing Controls for "Layout option" and "Search button" available on Advance Search of User Module.
		// TODO: VOOD-975
		layoutOptionCtrl = new VoodooControl("a", "id", "tabFormAdvLink");
		advancedSearchBtnCtrl = new VoodooControl("input", "id", "search_form_submit_advanced");
		sugar().login();
	}

	/**
	 * User's detail view can be displayed after searching with saved search condition of setting "Name" for "Order By column".
	 * @throws Exception
	 */
	@Test
	public void Users_24729_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = testData.get(testName).get(0);

		// Navigate to User Management
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("userManagement").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// Click Advanced Link on the Users List View
		sugar().users.listView.getControl("advancedSearchLink").click();
		VoodooUtils.waitForReady();

		// Input the First Name & Last Name in Advance Search panel
		// TODO: VOOD-975
		FieldSet userdefaultData = sugar().users.getDefaultData();
		new VoodooControl("input", "id", "first_name_advanced").set(userdefaultData.get("firstName"));
		new VoodooControl("input", "id", "last_name_advanced").set(userdefaultData.get("lastName"));

		// Expand the layout option section
		layoutOptionCtrl.click();

		// Verify Name is selected by Default in the "Order By Column"
		new VoodooControl("option", "css", "#orderBySelect option").assertEquals(fs.get("name"), true);

		// Click the search button available on Advanced Search Page
		advancedSearchBtnCtrl.click();

		// Click on the first Record Populated based on above searched criteria
		VoodooUtils.focusDefault();
		sugar().users.listView.clickRecord(1);
		VoodooUtils.waitForReady();

		// Verify the detail view of user clicked, is populated.
		sugar().users.detailView.assertVisible(true);
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.detailView.getDetailField("userName").assertEquals(userdefaultData.get("userName"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}