package com.sugarcrm.test.accounts;

import org.junit.Test;
import org.junit.Ignore;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_20270 extends SugarTest {
	FieldSet myData;
	
	public void setup() throws Exception {
		myData = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();
		
		// Go to Accounts Record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		
		// Add the Active tasks and Inactive tasks dashboards to RHS of an account record
		sugar().accounts.dashboard.clickCreate();
		sugar().accounts.dashboard.getControl("title").set(myData.get("dashBoardName"));
		sugar().accounts.dashboard.addRow();
		sugar().accounts.dashboard.addDashlet(1,1);
		
		// TODO: VOOD-960
		VoodooControl searchDashlet = new VoodooControl("input", "css", ".span4.search");
		VoodooControl inactiveTasks = new VoodooControl("a", "css", ".ellipsis_inline[data-original-title='Inactive Tasks'] a");
		
		searchDashlet.set(myData.get("firstDashboard"));
		VoodooControl activeTasks = new VoodooControl("a", "css", "[data-original-title='Active Tasks'] a");
		activeTasks.waitForVisible();
		activeTasks.click();
		sugar().accounts.createDrawer.save();
		sugar().accounts.dashboard.addRow();
		sugar().accounts.dashboard.addDashlet(2,1);
		searchDashlet.set(myData.get("secondDashboard"));
		inactiveTasks.waitForVisible();
		inactiveTasks.click();
		sugar().accounts.createDrawer.save();
		sugar().accounts.dashboard.save();
	}

	/**
	 * Verify tasks with no due date are shown on the To Do tab on the active tasks dashboard
	 * 
	 * @throws Exception
	 */
	@Ignore("TR-862: Active Tasks dashlet value is not refreshed when tasks added due to TR-862")
	@Test
	public void Accounts_20270_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to Active tasks dashlet and click + icon to create several Tasks
		for (int i=0; i<3; i++){
			// TODO: VOOD-960
			new VoodooControl("span", "css", ".row-fluid > li div:nth-child(1) span a span").click();
			new VoodooControl("a", "css", "[data-dashletaction='createRecord']").click();
			
			sugar().tasks.createDrawer.getEditField("subject").set(myData.get("subject")+i);
			sugar().tasks.createDrawer.getEditField("date_start_date").set("04/"+i+"1/2015");
			sugar().tasks.createDrawer.save();
		}
		
		// Verify number of tasks shown on the "To Do" tab to indicate the number of todo tasks
		new VoodooControl("a", "css", "div.dashlet-tabs.tab3 div:nth-child(3) a").click();
		new VoodooControl("span", "css", "div.dashlet-tabs.tab3 div:nth-child(3) a .count").assertContains("3", true);
		
		// Verify newly created Tasks shown under "To Do" tab under Active Tasks dashlet, ordered by date entered ASC
		for (int i=0; i<3; i++){
			new VoodooControl("a", "css", ".dashlet-content li:nth-child("+(i+1)+") a:nth-child(2)").assertContains(myData.get("subject")+i, true);
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
