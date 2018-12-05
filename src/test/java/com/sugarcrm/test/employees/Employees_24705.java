package com.sugarcrm.test.employees;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_24705 extends SugarTest {
	
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Employee - Sort_Verify that sort order indicator is displayed after column titles in employee list view.
	 * @throws Exception
	 */
	@Test
	public void Employees_24705_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// TODO VOOD-1041 -need lib support of employees module
		sugar.navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that two records exist
		new VoodooControl("tr", "xpath", "//*[@id='MassUpdate']/table/tbody/tr[contains(.,'admin2')]").assertExists(true);
		new VoodooControl("tr", "xpath", "//*[@id='MassUpdate']/table/tbody/tr[contains(.,'qauser')]").assertExists(true);
		new VoodooControl("tr", "xpath", "//*[@id='MassUpdate']/table/tbody/tr[contains(.,'Administrator')]").assertExists(true);
		
		// Verify that Descending Order: first row have "admin2" and second row have qauser
		new VoodooControl("a", "css", "#MassUpdate > table > tbody > tr.oddListRowS1 > td:nth-child(3) > b > a").assertContains("admin2", true);
		new VoodooControl("a", "css", "#MassUpdate > table > tbody > tr.evenListRowS1 > td:nth-child(3) > b > a").assertContains("qauser", true);
		
		// Click on "Name" column title to sort the employee records Ascending Order
		new VoodooControl("a", "css", "#MassUpdate > table > tbody > tr:nth-child(2) > th:nth-child(3) > div > a").click();
		sugar.alerts.waitForLoadingExpiration();
		
		// Verify that Ascending Order: first row have "admin2" and second row have "Administrator"		
		new VoodooControl("a", "css", "#MassUpdate > table > tbody > tr.oddListRowS1 > td:nth-child(3) > b > a").assertContains("admin2", true);
		new VoodooControl("a", "css", "#MassUpdate > table > tbody > tr.evenListRowS1 > td:nth-child(3) > b > a").assertContains("Administrator", true);
		
		// Click on "Name" column title to sort the employee records Descending Order
		new VoodooControl("a", "css", "#MassUpdate > table > tbody > tr:nth-child(2) > th:nth-child(3) > div > a").click();
		sugar.alerts.waitForLoadingExpiration();
		
		// Verify that Descending Order: first row have "qauser" and second row have "Administrator"
		new VoodooControl("a", "css", "#MassUpdate > table > tbody > tr.oddListRowS1 > td:nth-child(3) > b > a").assertContains("qauser", true);
		new VoodooControl("a", "css", "#MassUpdate > table > tbody > tr.evenListRowS1 > td:nth-child(3) > b > a").assertContains("Administrator", true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
	
}
