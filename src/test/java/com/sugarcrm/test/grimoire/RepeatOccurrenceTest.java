package com.sugarcrm.test.grimoire;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class RepeatOccurrenceTest extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet repeatData = new FieldSet();
		repeatData.put("repeatType", "Daily");
		repeatData.put("repeatOccur", "5");
		repeatData.put("date_start_date", null);
		repeatData.put("date_end_date", null);
		repeatData.put("date_start_time", null);
		repeatData.put("date_end_time", null);
		sugar().meetings.create(repeatData);

		sugar().meetings.navToListView();
		Assert.assertTrue("Meeting records not equals to 5.", sugar().meetings.listView.countRows() == 5);
		sugar().meetings.listView.clickRecord(5);

		Calendar now = Calendar.getInstance();
		String date = (now.get(Calendar.MONTH) + 2) + "/" + now.get(Calendar.DATE) + "/" + now.get(Calendar.YEAR);

		repeatData.clear();
		repeatData.put("date_start_date", null);
		repeatData.put("date_end_date", null);
		repeatData.put("date_start_time", null);
		repeatData.put("date_end_time", null);
		repeatData.put("repeatType", "Weekly");
		repeatData.put("repeatInterval", "2");
		repeatData.put("repeatUntil", date);
		sugar().calls.create(repeatData);
		Assert.assertTrue("Call records not equals to 3.", sugar().calls.listView.countRows() == 3);
		sugar().calls.listView.clickRecord(2);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}