package com.sugarcrm.test.users;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_24728 extends SugarTest {

	public void setup() throws Exception {
		sugar().users.api.create();
		sugar().login();
	}

	/**
	 * Users can be searched with saved search condition.
	 * @throws Exception
	 */
	@Test
	public void Users_24728_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to User Management
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("userManagement").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// Click Advanced Link on the Users List View
		sugar().users.listView.getControl("advancedSearchLink").click();
		VoodooUtils.waitForReady();
		FieldSet fs = testData.get(testName).get(0);

		// Set the condition to search
		// TODO: VOOD-975 - need defined controls for bwc module's basic/ advanced search panel
		new VoodooControl("select", "id", "is_admin_advanced").set(fs.get("isNotAdmin"));

		// Expand the layout option section
		new VoodooControl("a", "id", "tabFormAdvLink").click();

		// Set userName in the "Order By Column"
		new VoodooControl("select", "id", "orderBySelect").set(fs.get("userNameColumn"));

		// Assert that the sorting order of the userName column is Ascending
		assertTrue(new VoodooControl("input", "id", "sort_order_asc_radio").isChecked());

		// Enter the Search Name
		new VoodooControl("input", "css", "[name='saved_search_name']").set(testName);

		// Save the Search
		new VoodooControl("input", "css", "[name='saved_search_submit']").click();
		VoodooUtils.waitForReady();

		// Assert that the option selected in the Saved Searches drop down is the "Search Name" that we created above
		new VoodooControl("option", "css", "#saved_search_select option[selected]").assertEquals(testName, true);

		// Assert that Displayed search result are sorted by the selected field
		// TODO: VOOD-1533 - BWC Listview needs to provide access to all disabled dropdown menus, buttons and other fields
		new VoodooControl("td", "css", ".oddListRowS1 td:nth-child(4)").assertEquals(sugar().users.defaultData.get("userName"), true);
		new VoodooControl("td", "css", ".evenListRowS1 td:nth-child(4)").assertEquals(sugar().users.getQAUser().get("userName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}