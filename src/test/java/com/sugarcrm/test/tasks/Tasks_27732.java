package com.sugarcrm.test.tasks;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_27732 extends SugarTest {
	DataSource status = new DataSource();

	public void setup() throws Exception {
		status = testData.get(testName);
		sugar().tasks.api.create();
		sugar().login();
	}

	/**
	 * Verify that Due Date should be no color if Due Date is later than 24 hours in Tasks listview.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tasks_27732_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Date of after two days comparing now.
		Date dt = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.add(Calendar.DATE, 2);
		dt = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String newDate = sdf.format(dt);
		
		// Change Status = Pending Input, Due Date is beyond 24 hours comparing now.
		sugar().tasks.navToListView();
		sugar().tasks.listView.clickRecord(1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().tasks.recordView.showMore();
		sugar().tasks.recordView.edit();
		sugar().tasks.createDrawer.getEditField("status").set(status.get(0).get("status"));
		sugar().tasks.createDrawer.getEditField("date_start_time").set(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		sugar().tasks.createDrawer.getEditField("date_due_date").set(newDate);
		new VoodooControl("a", "css", ".rowaction[name='save_button']").click();
		sugar().alerts.waitForLoadingExpiration();
		sugar().tasks.navToListView();
		
		// Verify Due Date is no color.
		VoodooUtils.voodoo.log.info("Due date color: " + new VoodooControl("span", "css", ".fld_date_due.list").getCssAttribute("background-color"));
		new VoodooControl("span", "css", ".fld_date_due.list").assertCssAttribute("background-color", "rgba(0, 0, 0, 0)");
		
		// Verify that Date and Time appears in Due Date column.
		new VoodooControl("span", "css", ".fld_date_due.list").assertContains(newDate, true);
		
		// Change Status = Not Started, Due Date is beyond 24 hours comparing now.
		sugar().tasks.listView.clickRecord(1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().tasks.recordView.showMore();
		sugar().tasks.recordView.edit();
		sugar().tasks.createDrawer.getEditField("status").set(status.get(1).get("status"));
		sugar().tasks.createDrawer.getEditField("date_start_time").set(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		sugar().tasks.createDrawer.getEditField("date_due_date").set(newDate);
		new VoodooControl("a", "css", ".rowaction[name='save_button']").click();
		sugar().alerts.waitForLoadingExpiration();
		sugar().tasks.navToListView();
		
		// Verify Due Date is no color.
		VoodooUtils.voodoo.log.info("Due date color: " + new VoodooControl("span", "css", ".fld_date_due.list").getCssAttribute("background-color"));
		new VoodooControl("span", "css", ".fld_date_due.list").assertCssAttribute("background-color", "rgba(0, 0, 0, 0)");
				
		// Verify that Date and Time appears in Due Date column.
		new VoodooControl("span", "css", ".fld_date_due.list").assertContains(newDate, true);

		// Change Status = In Progress, Due Date is beyond 24 hours comparing now.
		sugar().tasks.listView.clickRecord(1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().tasks.recordView.showMore();
		sugar().tasks.recordView.edit();
		sugar().tasks.createDrawer.getEditField("status").set(status.get(2).get("status"));
		sugar().tasks.createDrawer.getEditField("date_start_time").set(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		sugar().tasks.createDrawer.getEditField("date_due_date").set(newDate);
		new VoodooControl("a", "css", ".rowaction[name='save_button']").click();
		sugar().alerts.waitForLoadingExpiration();
		sugar().tasks.navToListView();
		
		// Verify Due Date is no color.
		VoodooUtils.voodoo.log.info("Due date color: " + new VoodooControl("span", "css", ".fld_date_due.list").getCssAttribute("background-color"));
		new VoodooControl("span", "css", ".fld_date_due.list").assertCssAttribute("background-color", "rgba(0, 0, 0, 0)");
		
		// Verify that Date and Time appears in Due Date column.
		new VoodooControl("span", "css", ".fld_date_due.list").assertContains(newDate, true);
		
		// Change Status = Deferred, Due Date is beyond 24 hours comparing now.
		sugar().tasks.listView.clickRecord(1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().tasks.recordView.showMore();
		sugar().tasks.recordView.edit();
		sugar().tasks.createDrawer.getEditField("status").set(status.get(3).get("status"));
		sugar().tasks.createDrawer.getEditField("date_start_time").set(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		sugar().tasks.createDrawer.getEditField("date_due_date").set(newDate);
		new VoodooControl("a", "css", ".rowaction[name='save_button']").click();
		sugar().alerts.waitForLoadingExpiration();
		sugar().tasks.navToListView();
		
		// Verify Due Date is no color.
		VoodooUtils.voodoo.log.info("Due date color: " + new VoodooControl("span", "css", ".fld_date_due.list").getCssAttribute("background-color"));
		new VoodooControl("span", "css", ".fld_date_due.list").assertCssAttribute("background-color", "rgba(0, 0, 0, 0)");
		
		// Verify that Date and Time appears in Due Date column.
		new VoodooControl("span", "css", ".fld_date_due.list").assertContains(newDate, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}