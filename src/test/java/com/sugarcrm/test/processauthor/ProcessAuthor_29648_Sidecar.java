package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29648_Sidecar extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 *  Verify Show Process detail view title format is consistent for Sidecar module
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29648_Sidecar_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet processData = testData.get(testName).get(0);

		// Importing process Definition file
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");

		// Navigate to process Definition list view and open the action drop down of the process definition record
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);

		// Enable Process Definition
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Create a Account Record
		sugar().navbar.selectMenuItem(sugar().accounts, "createAccount");
		sugar().accounts.createDrawer.getEditField("name").set(sugar().accounts.getDefaultData().get("name"));
		sugar().accounts.createDrawer.save();

		// Go to Processes and show Processes
		// TODO: VOOD-1706
		sugar().processes.navToListView();
		sugar().processes.listView.openRowActionDropdown(1);
		new VoodooControl("a", "css", "span[data-voodoo-name='edit_button'] a").click();

		// Verify the title format is as "Process Definition Name | Activity Name"
		new VoodooControl("h1", "css", ".headerpane div div h1").assertEquals(processData.get("title"), true);

		// Under the title, there has Approve, Reject, Edit 
		// Used the CSS selectors with "+" implies that Approve,Edit etc buttons are immediate siblings
		// TODO: VOOD-1706
		new VoodooControl("a", "css", ".headerpane > div + div [name='approve_button']").assertVisible(true);
		new VoodooControl("a", "css", ".headerpane > div + div [name='reject_button']").assertVisible(true);
		new VoodooControl("a", "css", ".headerpane > div + div [name='edit_button']").assertVisible(true);
		new VoodooControl("a", "css", ".fld_main_dropdown a:nth-child(2)").click();

		// Under Edit Action dropdown History,Status & Add Notes are available.
		new VoodooControl("a", "css", ".fld_history a").assertVisible(true);
		new VoodooControl("a", "css", ".fld_status a").assertVisible(true);
		new VoodooControl("a", "css", ".fld_add-notes a").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
