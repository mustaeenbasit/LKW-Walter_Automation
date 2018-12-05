package com.sugarcrm.test.tasks;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_17460 extends SugarTest {	
	public void setup() throws Exception {			
		sugar.login();	
	}

	/**
	 * Verify "Date Created" and "Date Modified" fields are read only on create. (and edit)
	 * 
	 * @throws Exception
	 */	
	@Test
	public void Tasks_17460_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		// Navbar > Tasks > Create New > Enter name > Click Show more
		sugar.navbar.selectMenuItem(sugar.tasks, "createTask");
		sugar.tasks.createDrawer.getEditField("subject").set(testName);
		sugar.tasks.createDrawer.showMore();

		//Verify Date Created (entered), Modified fields are not editable and show "No Data"
		// TODO VOOD-868: Lib support to verify "Date Created" column 
		new VoodooControl("div", "css",".fld_date_entered_by.edit").assertVisible(false);
		new VoodooControl("div", "css",".fld_date_entered_by").assertEquals("No data", true);
		new VoodooControl("div", "css",".fld_date_modified_by.edit").assertVisible(false);
		new VoodooControl("div", "css",".fld_date_modified_by").assertEquals("No data", true);

		// Save and navigate to the record
		sugar.tasks.createDrawer.save();
		sugar.tasks.listView.clickRecord(1);

		// Edit the record
		sugar.tasks.recordView.edit();

		//Verify that the "Date Created" and "Date Modified" fields are read only, and should be populated.
		// TODO VOOD-868: Lib support to verify "Date Created" column 
		SimpleDateFormat sdFmt = new SimpleDateFormat("MM/dd/yyyy");
		String todaysDate = sdFmt.format(new Date());
		new VoodooControl("div", "css",".fld_date_entered.edit").assertVisible(false);
		new VoodooControl("div", "css",".fld_date_entered.detail").assertContains(todaysDate, true);
		new VoodooControl("div", "css",".fld_date_modified.edit").assertVisible(false);
		new VoodooControl("div", "css",".fld_date_modified.detail").assertContains(todaysDate, true);
		sugar.tasks.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}