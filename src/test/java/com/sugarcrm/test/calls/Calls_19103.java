package com.sugarcrm.test.calls;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_19103 extends SugarTest {	
	public void setup() throws Exception {
		sugar.login();			
	}

	/**
	 * Verify the Call duration is saved correctly.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_19103_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to Calls module and create a Call
		sugar.navbar.selectMenuItem(sugar.calls, "create" + sugar.calls.moduleNameSingular);
		
		// Get current date
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String todaysDate = sdf.format(date);
		
		// Select start date and end date so the difference between them is 30 min and fill all required fields
		DataSource ds = testData.get(testName);
		VoodooUtils.focusDefault();
		sugar.calls.createDrawer.setFields(ds.get(0));
		sugar.calls.createDrawer.save();
		sugar.calls.listView.clickRecord(1);
		
		// Verify that in the record view, after 'start date' and 'end date' in the brackets: eg.- 04/15/2015 9:00-9:30 (30 minutes)
		sugar.calls.recordView.getDetailField("date_start_time").assertContains(todaysDate+" "+ds.get(1).get("durationHours"),true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");		
	}

	public void cleanup() throws Exception {}
}