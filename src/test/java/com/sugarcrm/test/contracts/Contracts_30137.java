package com.sugarcrm.test.contracts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_30137 extends SugarTest {
	
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify "Contract Type" should be created without "List Order" 
	 * @throws Exception
	 */
	@Test
	public void Contracts_30137_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("contractManagement").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-1592: Need lib support for Admin > Contract Types
		// Creating one contract type without entering list order
		new VoodooControl("span", "css", "[data-module='ContractTypes'] .fa.fa-caret-down").click();
		VoodooUtils.waitForReady();
		new VoodooControl("span", "css", "[data-module='ContractTypes'] .dropdown-menu ul li").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "name").set(testName);
		new VoodooControl("input", "css", "[title='Save']").click();
		VoodooUtils.waitForReady();
		
		// Verifying contract type is created and shown in contract type list view.
		new VoodooControl("tr", "css", ".list.view tr:nth-child(3)").assertContains(testName, true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}