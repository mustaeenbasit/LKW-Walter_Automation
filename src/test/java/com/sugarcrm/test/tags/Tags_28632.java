package com.sugarcrm.test.tags;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_28632 extends SugarTest {
	FieldSet accountRecord = new FieldSet();
	FieldSet customData = new FieldSet();
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		for (int i = 0; i < 4; i++) {
			accountRecord.put("name", testName+"_"+i);
			sugar.accounts.api.create(accountRecord);
		}
		sugar.login();
	}

	/**
	 * Verify that Warning Alert to confirm deletion of tags on mass update appears
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tags_28632_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to any sidecar modules, such as Accounts
		sugar.accounts.navToListView();
		
		// Select several records
		sugar.accounts.listView.checkRecord(1);
		sugar.accounts.listView.checkRecord(2);
		
		// Select "Mass Update" from list view action drop down
		sugar.accounts.listView.openActionDropdown();
		sugar.accounts.listView.massUpdate();
		
		// Select "Tags" field and leave the second field empty
		sugar.accounts.massUpdate.getControl("massUpdateField02").set(sugar.tags.moduleNamePlural);
		
		// TODO: VOOD-1003
		new VoodooControl("input", "css", "input[name='append_tag']").click();
		
		// Click on Update button
		sugar.accounts.massUpdate.getControl("update").click();
		
		// Verify, Warning Alert to confirm deletion of Tags appears
		sugar.alerts.getWarning().assertContains(customData.get("warningText1"), true);
		sugar.alerts.getWarning().assertContains(customData.get("warningText2"), true);
		sugar.alerts.getWarning().assertContains(customData.get("warningText3"), true);
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}