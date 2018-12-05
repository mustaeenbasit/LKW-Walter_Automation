package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29972 extends SugarTest {

	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}
	/**
	 * Verify that Process should be shown "Terminated" in Process Management after meeting the criteria.
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29972_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customData = testData.get(testName).get(0);
		
		// Import Process Definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + "_7700.bpm");
		
		// Enable Imported Process Definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();
		
		// Navigate to Leads
		sugar().leads.navToListView();
		sugar().leads.listView.editRecord(1);
		
		// Set Status = Dead so that above created process is triggered
		sugar().leads.listView.getEditField(1, "status").set(customData.get("leadStatus"));
		sugar().leads.listView.saveRecord(1);
		
		// Navigate to Processes Management
		sugar().navbar.selectMenuItem(sugar().processes, "processManagement");

		// TODO: VOOD-1698
		// Verify Process is listed in Process management and status marked as "TERMINATED".  
		new VoodooControl("div", "css", ".list.fld_pro_title div").assertEquals(testName + "_7700", true);
		new VoodooControl("div", "css", ".list.fld_cas_status div").assertEquals(customData.get("processStatus"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	public void cleanup() throws Exception {}
}