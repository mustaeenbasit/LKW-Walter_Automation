package com.sugarcrm.test.users;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_24719  extends SugarTest {
	FieldSet customData = new FieldSet();
	VoodooControl activeDropdownCtrl;

	public void setup() throws Exception {
		sugar().login();
		// Pre-requisite: Existing custom query record
		// Go to Reports module, Click Manage Advanced Reports link in shortcut navigation.
		sugar().navbar.selectMenuItem(sugar().reports, "manageAdvancedReports");

		// Click on Advanced Reports -> Create Custom Queries
		// TODO: VOOD-1559
		activeDropdownCtrl = new VoodooControl("li", "css", ".dropdown.active .fa-caret-down");
		activeDropdownCtrl.click();
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_NEW_CUSTOMQUERY']").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Create a Custom Query record
		customData = testData.get(testName).get(0);
		new VoodooControl("input", "css", "input[name='name']").set(customData.get("name"));
		new VoodooControl("textarea", "css", "textarea[name='custom_query']").set(customData.get("customQuery"));
		new VoodooControl("input", "css", "input[title='Save']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * User-Normal_Verify that non-admin user can't create custom query.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_24719_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as valid non-admin user
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Go to Reports module, Click Manage Advanced Reports link in shortcut navigation.
		sugar().navbar.selectMenuItem(sugar().reports, "manageAdvancedReports");

		// Click "Advanced Reports" caret down to display Advanced Reports mega menu.
		// TODO: VOOD-1057
		activeDropdownCtrl.click();

		// Verify that the "Create Custom Query" option is not available in the Advanced Reports mega menu.
		new VoodooControl("ul", "css", ".dropdown.active [role='menu']").assertContains(customData.get("advancedReportsMenu"), false);

		// Click on "View Custom Queries" link
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_CUSTOMQUERIES']").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Very QueryName is not a link
		Assert.assertFalse("Query Name field is a link", (new VoodooControl("slot", "css", ".list.view tbody tr.oddListRowS1 td slot").getTag().equals(customData.get("tag"))));

		// Verify 'Run Query' option is available
		VoodooControl runQueryCtrl = new VoodooControl("a", "css", ".list.view tbody td:nth-child(5) a:nth-child(2)");
		runQueryCtrl.assertContains(customData.get("option"), true);

		// Click on "Run Query" option
		runQueryCtrl.click();
		VoodooUtils.waitForReady();

		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify QueryResult is displayed, to check "Run Query" option is clickable for non-admin user
		new VoodooControl("span", "css", "#contentTable table:nth-child(6) tbody tr td h3 span").assertEquals(customData.get("heading"), true);
		VoodooUtils.focusDefault();   

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}