package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29698 extends SugarTest {
	public void setup() throws Exception {
		// Login as Admin
		sugar().login();

		// Import a new Process Definitions: "Applies to" to "New record only", Activity -> Users -> Select "Static Assignment" to "Administrator" and End process
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		// Enable the Process Definition
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();
	}

	/**
	 * User should able to save Processes dashlet in dashboard. 
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29698_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet processDefinitionsData = testData.get(testName).get(0);

		// Create a new Contact record to trigger the event
		sugar().contacts.api.create();

		// In Home -> Add a Processes dashlet in dashboard and click on 'Save'
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		sugar().home.dashboard.edit();
		sugar().home.dashboard.addRow();
		sugar().home.dashboard.addDashlet(4, 1);

		// TODO: VOOD-960, VOOD-1645
		// Add a dashlet -> select "Active Tasks"
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(processDefinitionsData.get("processes"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();

		// Save
		new VoodooControl("a", "css", ".layout_pmse_Inbox.drawer.active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Save the home page Dashboard
		new VoodooControl("a", "css", "div.dashboard.edit a[name='save_button']").click();
		VoodooUtils.waitForReady();

		// Verify that the Processes dashlet should be save and display appropriate processes
		// TODO: VOOD-960
		new VoodooControl("a", "css", ".row-fluid.sortable:nth-of-type(4) .dashlet-container:nth-of-type(1) .tab-content a").assertEquals(processDefinitionsData.get("dashletRecord") + sugar().contacts.getDefaultData().get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	public void cleanup() throws Exception {}
}