package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29771 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that drop-down values should be completely visible while creating Business Rule in Process Business Rules
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29771_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet fieldData = testData.get(testName).get(0);
		
		// Navigate to Process Business Rules
		sugar().processBusinessRules.navToListView();
		
		// Create new rule and click on 'Save & Design' button
		sugar().processBusinessRules.listView.create();
		sugar().processBusinessRules.createDrawer.getEditField("name").set(testName);
		sugar().processBusinessRules.createDrawer.getControl("saveAndDesignButton").click();
		VoodooUtils.waitForReady();
		
		// Click in the rectangle just under the Conditions title and Select an item from the list
		// TODO: VOOD-1936
		new VoodooControl("select", "css", "#businessruledesigner select").set(fieldData.get("fieldName"));
		
		// Verify that drop-down value is completely visible
		// TODO: VOOD-1936
		new VoodooControl("div", "css", ".decision-table-conditions-header").assertContains(fieldData.get("fieldName"), true);
		new VoodooControl("option", "css", "[label='<Accounts>'] [label='"+fieldData.get("fieldName")+"']").assertVisible(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
