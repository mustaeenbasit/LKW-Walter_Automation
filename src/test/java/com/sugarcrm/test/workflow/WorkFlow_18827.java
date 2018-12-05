package com.sugarcrm.test.workflow;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class WorkFlow_18827 extends SugarTest {
	
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify set old value in condition of Time elapse workflow.
	 * 
	 * @throws Exception
	 */
	@Test
	public void WorkFlow_18827_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-1042
		// Workflow
		new VoodooControl("a", "id", "workflow_management").click();
		new VoodooControl("i", "css", "li.active .fa-caret-down").click();
		
		// click on Create Workflow Definition  
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_NEW_WORKFLOW']").click();
		VoodooUtils.pause(1000); // 1 sec. pause is needed to run consistently on the faster connection machines
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Create new workflow on Cases module
		new VoodooControl("input", "id", "name").set("Cases test workflow");
		new VoodooControl("option", "css", "select[name=type] option:nth-of-type(2)").click();
		new VoodooControl("option", "css", "select[name=base_module] option:nth-of-type(5)").click();
		
		// save workflow
		new VoodooControl("input", "id", "save_workflow").click();
		
		// Create a condition for the workflow
		new VoodooControl("a", "id", "NewWorkFlowTriggerShells").click();
		VoodooUtils.pause(1000); // 1 sec. pause is needed to run consistently on the faster connection machines
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", "body table:nth-child(2) tr:nth-child(1)  td:nth-child(1) input[type='radio']").click();
		new VoodooControl("a", "id", "href_compare_specific_1").click();
		VoodooUtils.focusWindow(2);
		new VoodooControl("option", "css", "#selector > option:nth-child(11)").click();
		new VoodooControl("input", "css", "[name='Save']").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "next").click();
		
		// Verification part
		// "option to specify the old value" not available due to SI defect 47891. So asserting non existences of the "Specify old" for now 
		//  When the defect is fixed, please update the behavior accordingly
		new VoodooControl("table", "css", "body > table > tbody > tr > td > table:nth-child(2)").assertContains("Specify old", false);
		new VoodooControl("table", "css", "body > table > tbody > tr > td > table:nth-child(2)").assertContains("Specify new", true);
		new VoodooControl("input", "css", "[title='Cancel']").click();
		VoodooUtils.focusWindow(0);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}