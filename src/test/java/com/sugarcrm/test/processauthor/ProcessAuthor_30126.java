package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_30126 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();

		// Import Process Definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + "_7700.bpm");

		// Enable Imported Process Definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Creating to Accounts record to trigger process 
		sugar().accounts.create();
	}

	/**
	 * [PA] Verify Related Filters should be displayed in the activity record view of processes
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_30126_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource relatedData = testData.get(testName);
		// Navigate to triggered Process
		sugar().processes.navToListView();
		sugar().processes.myProcessesListView.showProcess(1);

		// Clicking on related subpanel filter
		sugar().processes.recordView.getControl("relatedSubpanelFilter").click();
		
		// TODO: VOOD-629
		// Verifying modules in related filter in process record view subpanels
		for(int i = 0 ; i < relatedData.size() ; i++) 
			new VoodooControl("li", "css", ".select2-results li:nth-child("+ (i+1) +")").assertEquals(relatedData.get(i).get("relatedModuleName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}