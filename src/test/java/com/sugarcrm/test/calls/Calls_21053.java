package com.sugarcrm.test.calls;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_21053 extends SugarTest {
	FieldSet customData;
	String currentDate, qaUserName;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);

		// Get current date
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date dt = new Date();
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(dt);
		currentDate = sdf.format(dt);

		// Create two calls records with different data
		sugar.calls.api.create();
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		fs.put("status", customData.get("status"));
		fs.put("date_start_date", currentDate);
		fs.put("date_end_date", currentDate);
		fs.put("direction", customData.get("direction"));
		sugar.calls.api.create(fs);
		sugar.login();

		// Edit team and assigned to field (proper coverage with all sort header lists)
		qaUserName = sugar.users.getQAUser().get("userName");
		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.edit();
		sugar.calls.recordView.getEditField("assignedTo").set(qaUserName);
		sugar.calls.recordView.save();
	}

	/**
	 * Sort call_Verify that calls can be sorted by some columns in call list view.
	 * @throws Exception
	 */
	@Test
	public void Calls_21053_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	

		// Verify that DESC records according to "Direction","Subject","Contacts" and "Start Date" column titles
		sugar.calls.navToListView();

		// Verify that first row record after load listView
		sugar.calls.listView.verifyField(1, "name", testName);

		// sort by 'subject' in descending and ascending order
		sugar.calls.listView.sortBy("headerName", false);
		sugar.calls.listView.verifyField(1, "name", testName);
		sugar.calls.listView.verifyField(2, "name", sugar.calls.getDefaultData().get("name"));

		sugar.calls.listView.sortBy("headerName", true);
		sugar.calls.listView.verifyField(1, "name", sugar.calls.getDefaultData().get("name"));
		sugar.calls.listView.verifyField(2, "name", testName);

		// sort by 'date start' in descending and ascending order
		sugar.calls.listView.sortBy("headerDatestart", false);
		sugar.calls.listView.verifyField(1, "date_start_date", sugar.calls.getDefaultData().get("date_start_date"));
		sugar.calls.listView.verifyField(2, "date_start_date", currentDate);

		sugar.calls.listView.sortBy("headerDatestart", true);
		sugar.calls.listView.verifyField(1, "date_start_date", currentDate);
		sugar.calls.listView.verifyField(2, "date_start_date", sugar.calls.getDefaultData().get("date_start_date"));

		// sort by 'status' in descending and ascending order
		sugar.calls.listView.sortBy("headerStatus", false);
		sugar.calls.listView.verifyField(1, "status", sugar.calls.getDefaultData().get("status"));
		sugar.calls.listView.verifyField(2, "status", customData.get("status"));

		sugar.calls.listView.sortBy("headerStatus", true);
		sugar.calls.listView.verifyField(1, "status", customData.get("status"));
		sugar.calls.listView.verifyField(2, "status", sugar.calls.getDefaultData().get("status"));

		// sort by 'assigned to' in descending and ascending order
		sugar.calls.listView.sortBy("headerAssignedusername", false);
		sugar.calls.listView.verifyField(1, "assignedTo", qaUserName);
		sugar.calls.listView.verifyField(2, "assignedTo", customData.get("admin"));

		sugar.calls.listView.sortBy("headerAssignedusername", true);
		sugar.calls.listView.verifyField(1, "assignedTo", customData.get("admin"));
		sugar.calls.listView.verifyField(2, "assignedTo", qaUserName);

		// sort by 'direction' in descending and ascending order
		sugar.calls.listView.sortBy("headerDirection", false);
		sugar.calls.listView.verifyField(1, "direction", customData.get("direction"));
		sugar.calls.listView.verifyField(2, "direction", sugar.calls.getDefaultData().get("direction"));

		sugar.calls.listView.sortBy("headerAssignedusername", true);
		sugar.calls.listView.verifyField(1, "direction", sugar.calls.getDefaultData().get("direction"));
		sugar.calls.listView.verifyField(2, "direction", customData.get("direction"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}