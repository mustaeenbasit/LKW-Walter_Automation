package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_30000 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}
	/**
	 * Verify that process definition action are processed correctly
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_30000_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customData = testData.get(testName).get(0);
		
		// Import Process Definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + "_7700.bpm");
		
		// Enable Imported Process Definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();
		
		// Creating an Account with name as 'XYZ'
		FieldSet fs = new FieldSet();
		fs.put("name", customData.get("accountName"));
		sugar().accounts.create(fs);
		fs.clear();
		
		// Verify that Account record is changed to name 'Testing Purpose'
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.getDetailField("name").assertEquals(customData.get(
				"changedAccountName"), true);
		
		// Navigate to Processes Management
		sugar().navbar.selectMenuItem(sugar().processes, "processManagement");
		
		// TODO: VOOD-1698
		// Verify Process is listed in Process management and status marked as "Completed".  
		new VoodooControl("div", "css", ".list.fld_pro_title div").assertEquals(testName + "_7700", true);
		new VoodooControl("div", "css", ".list.fld_cas_status div").assertEquals(
				customData.get("processStatus"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	public void cleanup() throws Exception {}
}