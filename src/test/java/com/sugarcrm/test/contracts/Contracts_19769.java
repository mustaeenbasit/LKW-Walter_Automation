package com.sugarcrm.test.contracts;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Contracts_19769 extends SugarTest {
	public void setup() throws Exception {		
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * Navigate to Contracts Module_Verify that the Contracts page is displayed correctly with clicking "Contracts" tab.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19769_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Go to Contracts module
		sugar.navbar.navToModule(sugar.contracts.moduleNamePlural);
		String expected = sugar.contracts.moduleNamePlural;
		String found = sugar.contracts.listView.getModuleTitle();

		// Verify Contracts page is displayed correctly
		Assert.assertTrue("Contracts page is not displayed", found.contains(expected));
		assertTrue("getModuleTitle did not return " + expected + "; " + found + " was found instead.", found.contains(expected));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}