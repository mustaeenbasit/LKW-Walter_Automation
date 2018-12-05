/* QA UPDATES PACKAGE TO MATCH THE TEST FILENAME AND LOCATION */
package com.sugarcrm.test;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/* QA ENTERS ADDITIONAL IMPORTS HERE */

/* QA CHANGES THE NAME OF THE CLASS TO REFLECT THE TEST NAME */
public class TestTemplate extends SugarTest {
	/* QA DECLARES ANY NEEDED CLASS VARS HERE */
	
	public void setup() throws Exception {
		sugar.login();

		/* QA ENTERS SETUP HERE */
	}

	/**
	 * QA ENTERS TEST SHORT DESCRIPTION HERE.
	 * 
	 * @throws Exception
	 */
	@Test
	public void execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		/* QA ENTERS TEST STEPS HERE */

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {
		/* QA ENTERS CLEANUP HERE */
		sugar.logout();
	}
}