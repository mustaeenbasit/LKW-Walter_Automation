package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest; 

public class Tasks_29827 extends SugarTest {
	FieldSet customData = new FieldSet();
	
	public void setup() throws Exception {	
		customData = testData.get(testName).get(0);
		sugar().tasks.api.create();  

		// Logging in as admin
		sugar().login();
	}

	/**
	 * Tasks_Verify upon Mass Updating the appended teams' dropdown shows 'Search and Select...' option
	 * @throws Exception
	 */	
	@Test
	public void Tasks_29827_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 
		
		// Verifying that teams mass update dropdown shows 'Search and Select...' option (on an unedited task record)
		checkIfSearchAndSelectOptionIsVisible();
		
		// Editing the task record 
		sugar().tasks.navToListView();
		sugar().tasks.listView.clickRecord(1);
		sugar().tasks.recordView.edit();
		sugar().tasks.recordView.getEditField("subject").set(testName);
		sugar().tasks.recordView.getEditField("priority").set(customData.get("newPriority"));
		sugar().tasks.recordView.save();
		
		// Verifying that teams mass update dropdown shows 'Search and Select...' option (on an Edited task record)
		checkIfSearchAndSelectOptionIsVisible();
                
		VoodooUtils.voodoo.log.info(testName + " complete."); 
	}

	void checkIfSearchAndSelectOptionIsVisible() throws Exception {
		sugar().tasks.navToListView();  
		sugar().tasks.listView.checkRecord(1);
		sugar().tasks.listView.openActionDropdown();
		
		// Attempting to mass update task record
		sugar().tasks.listView.massUpdate();
		sugar().tasks.massUpdate.getControl("massUpdateField02").set(customData.get("massUpdateField"));
		VoodooUtils.waitForReady();

		// Clicking 'Add Team' (+) button within 'Teams' fields
		// TODO: VOOD-1160 Need lib support for adding multiple teams during mass update
		new VoodooControl("button", "css", ".btn.first").click();

		// Clicking the second team select dropdown
		new VoodooControl("div", "css", ".control-group:nth-child(2) .select2-container").click();

		// Verify that dropdown shows 'Search and Select...' option
		VoodooControl searchAndSelect = new VoodooControl("div", "css", ".select2-drop .select2-result-label");
		searchAndSelect.assertVisible(true);
		searchAndSelect.assertEquals(customData.get("searchSelectText"), true);

		// Clicking on 'Search and Select...' option in order to bring focus back on page
		searchAndSelect.click();
	}
	
	public void cleanup() throws Exception {}
}
