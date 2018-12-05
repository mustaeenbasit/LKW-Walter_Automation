package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_30170 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}
	/**
	 * Verify the required field should NOT be empty/blank after creating any record
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_30170_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customData = testData.get(testName).get(0);
		
		// Import Process Definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + "_7700.bpm");
		
		// Enable Imported Process Definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();
		
		// Creating an Account with billing address country as blank
		sugar().navbar.selectMenuItem(sugar().accounts, "createAccount");
		sugar().accounts.createDrawer.getEditField("name").set(sugar().accounts.getDefaultData().get("name"));
		sugar().accounts.createDrawer.save();
		
		// Verify that Account record is not displayed as blank as it's a Required field.
		sugar().accounts.navToListView();
		sugar().accounts.listView.verifyField(1, "name", sugar().accounts.getDefaultData().get("name"));
		
		// Navigate to Processes Management
		sugar().navbar.selectMenuItem(sugar().processes, "processManagement");
		
		// TODO: VOOD-1698
		// Verify Process is listed in Process management and status marked as "ERROR".  
		new VoodooControl("div", "css", ".list.fld_pro_title div").assertEquals(testName + "_7700", true);
		new VoodooControl("div", "css", ".list.fld_cas_status div").assertEquals(
				customData.get("processStatus"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	public void cleanup() throws Exception {}
}