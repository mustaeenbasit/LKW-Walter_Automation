package com.sugarcrm.test.calendar;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20325 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().login(); 
	}

	/**
	 * Call is scheduled using Calendar -> Schedule Call action from Module Drop Down.
	 * @throws Exception
	 */
	@Test
	public void Calendar_20325_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Initialize test data
		DataSource callDataSource= testData.get(testName);
		
		// Click "Schedule Call" link on Navigation shortcuts.
		sugar().navbar.selectMenuItem(sugar().calendar, "scheduleCall");

		// Enter call data and save.
		sugar().calls.createDrawer.getEditField("name").set(callDataSource.get(0).get("name"));
		sugar().calls.createDrawer.getEditField("status").set(callDataSource.get(0).get("status"));
		sugar().calls.createDrawer.getEditField("description").set(callDataSource.get(0).get("description"));
		sugar().calls.createDrawer.getEditField("date_start_date").set(callDataSource.get(0).get("date_start_date"));
		sugar().calls.createDrawer.getEditField("date_start_time").set(callDataSource.get(0).get("date_start_time"));
		sugar().calls.createDrawer.getEditField("date_end_time").set(callDataSource.get(0).get("date_end_time"));
		sugar().calls.createDrawer.save();
		
		// Click on created call link record which appear in success message.
		sugar().alerts.waitForVisible();
		sugar().alerts.getSuccess().clickLink(0);

		// Verify record saved and check subject in Calendar view
		sugar().calls.recordView.getDetailField("name").assertEquals(callDataSource.get(0).get("name"), true);
		sugar().calls.recordView.getDetailField("status").assertEquals(callDataSource.get(0).get("status"), true);
		sugar().calls.recordView.getDetailField("description").assertEquals(callDataSource.get(0).get("description"), true);
		sugar().calls.recordView.getDetailField("date_start_time").assertEquals(callDataSource.get(1).get("date_start_time"), true);
	
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}