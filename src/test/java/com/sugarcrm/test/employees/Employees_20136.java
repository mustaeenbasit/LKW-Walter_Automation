package com.sugarcrm.test.employees;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20136 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Click email address from employees list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Employees_20136_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Go to employees list
		// TODO VOOD-1041 - need lib support of employees module
		sugar().navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();	
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		// Sometime can't find the BWC frame without the wait
		new VoodooControl("div", "css", ".listViewBody").waitForVisible();

		// Click the employee's email address mentioned in the list
		// TODO VOOD-1041 - need lib support of employees module
		new VoodooControl("a", "css", "table.list.view tr.oddListRowS1 td:nth-child(7) a").click();
		new VoodooControl("form", "id", "emailCompose0").waitForVisible();
		if(new VoodooControl("div", "id", "sugarMsgWindow").queryVisible()) {
			new VoodooControl("a", "css", "#sugarMsgWindow a.container-close").click();
		}

		FieldSet emailAddressData = testData.get(testName).get(0);
		// Verify that the email address is shown in "To:" field.
		// TODO: EA-2821 - QAUser is created with a different email ID from the default QAUser data when installing the VOODOO enabled Sugar instance.
		new VoodooControl("input", "id", "addressTO0").assertContains(emailAddressData.get("emailAddress"), true);
		new VoodooControl("a", "css", "#container1 a.container-close").click();		

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}