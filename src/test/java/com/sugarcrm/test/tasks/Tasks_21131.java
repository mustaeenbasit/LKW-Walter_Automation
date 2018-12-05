package com.sugarcrm.test.tasks;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_21131 extends SugarTest {

	public void setup() throws Exception {
		sugar.login();

		// Create tasks with different Date Created
		sugar.navbar.navToModule(sugar.tasks.moduleNamePlural);
		sugar.navbar.clickModuleDropdown(sugar.tasks);
		sugar.tasks.menu.getControl("importTasks").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// Create Task records by importing the csv
		VoodooFileField browseToImport = new VoodooFileField("input", "id", "userfile");
		browseToImport.set("src/test/resources/data/" + testName + ".csv");

		// TODO: VOOD-1396 - Need Controls for the Import Tasks functionality
		VoodooControl nextButton = new VoodooControl("input", "id", "gonext");
		nextButton.click();
		VoodooUtils.waitForReady();
		new VoodooControl("form", "id", "importconfirm").assertVisible(true);
		nextButton.click();
		VoodooUtils.waitForReady();
		new VoodooControl("form", "id", "importstep3").assertVisible(true);
		nextButton.click();
		VoodooUtils.waitForReady();
		new VoodooControl("form", "id", "importstepdup").assertVisible(true);
		new VoodooControl("input", "id", "importnow").click();
		VoodooUtils.waitForReady();
		new VoodooControl("form", "id", "importlast").assertVisible(true);
		new VoodooControl("input", "id", "finished").click();
		VoodooUtils.focusDefault();
		sugar.tasks.listView.waitForVisible();
	}

	/**
	 * Verify Tasks list view is sorted by "Date Created" by default.
	 * @throws Exception
	 */
	@Test
	public void Tasks_21131_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Assert that "Date Created" is in list view by default
		sugar.tasks.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("date_entered"))).assertVisible(true);

		// Assert that "Date Created" is the farthest to the right in list view
		// (class = '.th-droppable-placeholder-last' after "date created" header ensures that its the farthest)
		// TODO: VOOD-1473 - Not able to verify "Date Created" is the farthest to the right in list view
		new VoodooControl("th", "class", "orderBydate_entered").getChildElement("div", "class", "th-droppable-placeholder-last").assertExists(true);

		// Assert that the List view is sorted by "Date Created" by default, in descending order
		DataSource taskDateCreated = testData.get(testName);
		int totalTasks = sugar.tasks.listView.countRows();

		// TODO: VOOD-1450 - Need Controls for Date Created and Date Modified
		for(int rec=1; rec<=totalTasks; rec++){
			new VoodooControl("td", "css", ".flex-list-view-content .single:nth-child(" + rec + ") td:nth-child(8)").assertContains(taskDateCreated.get(totalTasks-rec).get("Date Created"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}