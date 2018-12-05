package com.sugarcrm.test.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_26723 extends SugarTest {

	public void setup() throws Exception {

		// Get current date and time
		SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mma");
		Date date = new Date();
		Calendar calObj = Calendar.getInstance();
		calObj.setTime(date);
		String currentDate = simpleDateFormat.format(calObj.getTime());
		String currentTime = simpleTimeFormat.format(calObj.getTime());

		// Create a task record 
		FieldSet taskData = new FieldSet();
		taskData.put("subject", testName);
		taskData.put("date_due_date", currentDate);
		taskData.put("date_start_date", currentDate);
		taskData.put("date_due_time", currentTime);
		sugar.tasks.api.create(taskData);

		sugar.login();
	}
	/**
	 * Verify Task is displayed in Sidecar mode when click on the "i" or edit or view icon in task pop up in Calendar
	 * @throws Exception
	 */
	@Test
	public void Calendar_26723_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Calendar" navigation tab.
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Verify the created task is present on Calendar's week view and click on 'i' icon 
		// TODO: VOOD-863
		new VoodooControl("div", "css", ".head div:nth-of-type(2)").assertEquals(testName, true);
		new VoodooControl("div", "css", ".head div:nth-of-type(1)").click();

		// Verify task record view open's in Side Car on clicking 'View' icon  
		VoodooUtils.waitForReady();
		new VoodooControl("img", "css", ".ui-dialog-title [title='View'] img").click();
		VoodooUtils.focusDefault();
		sugar.tasks.recordView.assertVisible(true);
		sugar.tasks.recordView.getDetailField("subject").assertEquals(testName,true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}