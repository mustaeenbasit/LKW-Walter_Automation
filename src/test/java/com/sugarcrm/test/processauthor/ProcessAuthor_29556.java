package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29556 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify no error icon is shown by default on the process definition designer
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29556_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Import the process definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");
		
		// Observe designer canvas & error message area
		// Verify that No error icon is display while no errors are present. The error icon appears when there are at least 1 or more errors present.  
		VoodooControl errorMsgCtrl = new VoodooControl("span", "css", "#error-div[style*='display: none']");
		errorMsgCtrl.assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}