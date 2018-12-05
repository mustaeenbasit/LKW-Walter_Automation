package com.sugarcrm.test.processauthor;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_30006 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}
	/**
	 * Verify that Salutation, First_Name and Last_name fields should not be editable if they are made ReadOnly in Activity element.
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_30006_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Import Process Definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + "_7700.bpm");
		
		// Enable Imported Process Definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();
		
		// Navigate to Leads and create a lead
		sugar().leads.create();
		
		// Navigate to Processes 
		sugar().processes.navToListView();
		
		// Clicking on Inline edit Actions drop down corresponding to the process created and click Show Process.
		sugar().processes.listView.openRowActionDropdown(1);
		
		// TODO: VOOD-1706
		new VoodooControl("a", "css", ".dropdown-menu .fld_edit_button a").click();
		VoodooUtils.waitForReady();
		
		// Try to edit the lead record
		sugar().leads.recordView.edit();
		
		// Verifying that Salutation, First Name and Last Name fields should be read-only i.e. disabled
		// TODO: VOOD-1445
		Assert.assertTrue("Salutation field is not disabled when it should", new VoodooControl("span", "class", "fld_salutation").isDisabled());
		Assert.assertTrue("First Name field is not disabled when it should", new VoodooControl("span", "class", "fld_first_name").isDisabled());
		Assert.assertTrue("Last Name field is not disabled when it should", new VoodooControl("span", "class", "fld_last_name").isDisabled());
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	public void cleanup() throws Exception {}
}