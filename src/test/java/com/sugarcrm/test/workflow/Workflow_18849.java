package com.sugarcrm.test.workflow;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Workflow_18849 extends SugarTest {	
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Amount Field should be available in a Workflow Alert Email Template for Opportunities
	 * 
	 * @throws Exception
	 */
	@Test
	public void Workflow_18849_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1042
		// Workflow
		new VoodooControl("a", "id", "workflow_management").click();
		VoodooUtils.focusDefault();
		
		// Select workflow caret
		new VoodooControl("i", "css", "li.active .fa-caret-down").click();

		// Select Workflow Email Template
		new VoodooControl("li", "css", "li.active .scroll ul li:nth-of-type(3)").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Select Opportunities module and create
		new VoodooControl("option", "css", "select[name='base_module'] option[value='Opportunities']").click();
		new VoodooControl("input", "css", "[type='submit']").click();

		VoodooUtils.focusFrame("fields_iframe");
		VoodooUtils.pause(2000); // small pause needed to wait for element visibility 
		
		new VoodooControl("select", "id", "target_dropdown").waitForVisible();
		
		new VoodooControl("option", "css", "select#target_dropdown option[value='amount']").assertExists(true);
		new VoodooControl("option", "css", "select#target_dropdown option[value='best_case']").assertExists(true);
		new VoodooControl("option", "css", "select#target_dropdown option[value='worst_case']").assertExists(true);
		new VoodooControl("option", "css", "select#target_dropdown option[value='amount_usdollar']").assertExists(true);
		
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}