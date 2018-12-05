package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29234 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify the added notes during select new process use should be displayed afterwards
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29234_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource notesDS = testData.get(testName);

		// Import Process definition => Lead start event (new record) -> activity > Static Assignment, Forms > General ->  mark both checkbox for Change Assigned To User and Select New Process User > End event
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/"+testName+".bpm");

		// Enable Imported Process Definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Trigger the process by new lead record
		sugar().leads.create();

		sugar().processes.navToListView();
		sugar().processes.myProcessesListView.showProcess(1);

		// Assigned user => Note
		String qauser = sugar().users.getQAUser().get("userName");
		sugar().leads.recordView.openPrimaryButtonDropdown();

		// Verify matched user list should be shown after enter for "Select New Process User"
		// TODO: VOOD-1706
		new VoodooControl("a", "css", ".detail.fld_find_duplicates_button").click();
		VoodooUtils.waitForReady();
		VoodooSelect user = new VoodooSelect("div", "css", ".adam-window-body .adam-field .select2-container");
		user.set(qauser);
		new VoodooControl("textarea", "css", ".adam-window-body .adam-field #reassign_comment").set(notesDS.get(0).get("note"));
		VoodooControl saveBtn = new VoodooControl("a", "css", ".adam-panel-footer .btn-primary");
		saveBtn.click();

		// TODO: VOOD-1934, VOOD-1935
		//VoodooUtils.waitForReady(); //- not working, thats why pause required
		VoodooUtils.pause(3000);

		// Select new process user => Note
		sugar().leads.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", ".detail.fld_duplicate_button").click();
		VoodooUtils.waitForReady();
		user.set(qauser);
		new VoodooControl("textarea", "css", ".adam-window-body .adam-field #not_content").set(notesDS.get(1).get("note"));
		saveBtn.click();
		VoodooUtils.waitForReady();

		sugar().navbar.selectMenuItem(sugar().processes, "processManagement");
		sugar().processes.processManagementListView.showNotes(1);
		VoodooUtils.waitForReady();

		// TODO: VOOD-1698
		// Verify note added from new process user and assigned user
		new VoodooControl("p", "css", ".adam-window-body .adam-panel-body .adam-field:nth-of-type(2) div p").assertContains(notesDS.get(0).get("note"), true);
		new VoodooControl("p", "css", ".adam-window-body .adam-panel-body .adam-field:nth-of-type(3) div p").assertContains(notesDS.get(1).get("note"), true);
		new VoodooControl("span", "css", ".adam-window-close").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}