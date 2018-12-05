package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29285 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify "Date Modified/Created, Modified/Created By" fields can be shown and used in process author
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29285_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Import Process Definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName +".bpm");

		// Navigate to process Definition list view and open the action drop down of the process definition record
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);

		// Enable Process Definition
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Create an Account record to trigger the Process
		sugar().navbar.selectMenuItem(sugar().accounts, "createAccount");
		sugar().accounts.createDrawer.getEditField("name").set(sugar().accounts.getDefaultData().get("name"));
		sugar().accounts.createDrawer.save();

		// Navigate to Processes Management
		sugar().navbar.selectMenuItem(sugar.processes, "processManagement");
		
		// TODO: VOOD-1698
		// Verify that A new process triggered based on the event criteria and wait setting time period.  
		new VoodooControl("span", "css", ".list.fld_pro_title").assertEquals(testName, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}