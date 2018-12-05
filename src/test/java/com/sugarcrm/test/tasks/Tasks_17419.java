package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest; 

public class Tasks_17419 extends SugarTest {	
	@Override
	public void setup() throws Exception {			
		sugar.login();	
		sugar.tasks.api.create();
	}

	/**
	 * Verify Close action on Tasks record view.
	 * @throws Exception
	 */	
	@Test
	public void Tasks_17419_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 
		
		sugar.tasks.navToListView();	
		sugar.tasks.listView.clickRecord(1);

		// TODO: VOOD-606.  Once it is fixed, please update the following steps.
		//Before click on Close, status=Not Started
		new VoodooControl("div", "css", ".fld_status.detail div").assertContains("Not Started", true);

		// TODO: VOOD-607.  Once it is fixed, please update the following steps.
		//Before click on Close, status=Not Started
		new VoodooControl("div", "css", ".fld_status.detail div").assertContains("Not Started", true);

		//Select Close action from record view
		sugar.tasks.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", ".fld_record-close.detail a").click();
		new VoodooControl("div", "css", ".fld_status.detail div").assertContains("Completed", true);
		
		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	@Override
	public void cleanup() throws Exception {}
}