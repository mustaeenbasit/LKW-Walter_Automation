package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29851 extends SugarTest {
	FieldSet customFS = new FieldSet();

	public void setup() throws Exception {
		customFS = testData.get(testName).get(0);
		sugar().login();

		// Import Process Definition
		sugar.processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");

		// Enable Imported Process Definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Processes record should have status=Terminated.
		FieldSet fs = new FieldSet();
		fs.put("name", customFS.get("accountName"));
		sugar().accounts.create(fs);
		sugar().accounts.api.deleteAll();
	}

	/**
	 * Verify user should get correct records when a filter is applied and some text is searched in Quick Search field.
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29851_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to My Processes
		sugar().processes.navToListView();
		sugar().navbar.selectMenuItem(sugar().processes, "processManagement");
		VoodooUtils.waitForReady();

		// TODO: VOOD-1698
		// Now select the "Processes In-Progress" filter to search the records.
		new VoodooControl("i", "css", ".search-filter .fa.fa-caret-down").click();
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", ".search-filter-dropdown ul li:nth-child(4)").click();
		VoodooUtils.waitForReady();

		String noData = customFS.get("noDataAvailableText");

		// Verify that "No data available. " should be displayed.
		// TODO: VOOD-1698 - Need Lib support for Process Management and Unattended Processes list view.
		VoodooControl editDropDown = sugar().processes.processManagementListView.getControl("dropdown01");
		editDropDown.assertExists(false);
		VoodooControl footerText = new VoodooControl("div", "css", ".layout_pmse_Inbox .block-footer");
		footerText.assertContains(noData, true);

		// Enter some text (not related to any record) i.e "qwerty" in the quick search field.
		new VoodooControl("input", "css", ".search-filter .search-name").set(customFS.get("searchTxt"));
		VoodooUtils.waitForReady();

		// Verify that "No data available. " should be displayed.
		editDropDown.assertExists(false);
		footerText.assertContains(noData, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}