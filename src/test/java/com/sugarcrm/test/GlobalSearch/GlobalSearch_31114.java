package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_31114 extends SugarTest {

	public void setup() throws Exception {
		// Log-In as Admin
		sugar().login();
	}

	/**
	 * Verify employee email address should be searched out using global search
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_31114_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet employeeData = testData.get(testName).get(0);
		String fullName = employeeData.get("empFullName");
		String emailAddress = employeeData.get("empEmailAddress");

		// Navigate to Employees module -> Create new employee with email address
		sugar().navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1041 - need lib support of employees module
		new VoodooControl("i", "css", "li[data-module='Employees'] i.fa-caret-down").click();
		new VoodooControl("a", "css", "li[data-module='Employees'] a[data-navbar-menu-item='LNK_NEW_EMPLOYEE']").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "last_name").set(employeeData.get("empLastName"));
		new VoodooControl("input", "id", "first_name").set(employeeData.get("empFirstName"));
		new VoodooControl("input", "id", "Users0emailAddress0").set(emailAddress);
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Navigate to global search bar and enter employee name in the global search bar and observe the list
		sugar().navbar.getControl("globalSearch").set(fullName);
		VoodooUtils.waitForReady();

		// Assert that Employee name should be searched with email address in the type ahead dropdown list
		// TODO: VOOD-1868 - Support Global search typeahead results
		new VoodooControl("h3", "css", ".typeahead .search-result .primary h3").assertEquals(fullName, true);
		new VoodooControl("strong", "css", ".typeahead .search-result .secondary strong").assertEquals(emailAddress, true);

		// Assert that Employee name should be searched with email address in the search results page
		// TODO: VOOD-1437 - Need lib support to send "Key" to a control
		// Hit the Enter key
		sugar().navbar.getControl("globalSearch").append("\uE007");
		VoodooControl globalSearchResultRow1 = sugar().globalSearch.getRow(1);
		globalSearchResultRow1.assertContains(fullName, true);
		globalSearchResultRow1.assertContains(emailAddress, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}