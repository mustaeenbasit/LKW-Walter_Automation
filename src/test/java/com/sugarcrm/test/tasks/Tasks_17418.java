package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest; 

public class Tasks_17418 extends SugarTest {	
	public void setup() throws Exception {	
		sugar.tasks.api.create(); 
		sugar.login();	
	}

	/**
	 * Verify Delete action on Tasks record view
	 * @throws Exception
	 */	
	@Test
	public void Tasks_17418_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 
		
		sugar.tasks.navToListView();
		sugar.tasks.listView.clickRecord(1);
		
		//Open detail view drop-down > delete >confirm Delete
		sugar.tasks.recordView.openPrimaryButtonDropdown();
		sugar.tasks.recordView.getControl("deleteButton").click();
		sugar.alerts.getAlert().confirmAlert();
		
		//Assert record is deleted
		sugar.tasks.listView.assertIsEmpty();
				
		VoodooUtils.voodoo.log.info(testName + " complete."); 
	}

	public void cleanup() throws Exception {}
}
