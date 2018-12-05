package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.Alert;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29599 extends SugarTest {

	public void setup() throws Exception {
		// Create an account record (i.e required while creating the quote)
		sugar().accounts.api.create();

		// Create a note record
		sugar().notes.api.create();

		// Log-In as an Admin
		sugar().login();

		// Create a Quote recordx1
		sugar().quotes.create();
	}

	/**
	 * Verify the process is triggered each time while updating record
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29599_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet quoteNnoteInfo = testData.get(testName).get(0);
		String processDefNameQuotes = String.format("%s%s", testName, quoteNnoteInfo.get("quotesSuffix"));
		VoodooControl enableDisableButton = sugar().processDefinitions.listView.getControl("enableAndDisable01");
		Alert warningAlert = sugar().alerts.getWarning();

		// Test for BWC module = Quotes
		// Import a process definition for Quotes module that triggers a process when a record is updated(all updates)
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + processDefNameQuotes + ".bpm");

		// Enable the process Definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		enableDisableButton.click();
		warningAlert.confirmAlert();

		// Update the quote record to trigger the process
		sugar().navbar.navToModule(sugar().quotes.moduleNamePlural);
		sugar().quotes.listView.clickRecord(1);
		sugar().quotes.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl quoteNameEditField = sugar().quotes.editView.getEditField("name");
		quoteNameEditField.set(quoteNnoteInfo.get("quoteUpdate1"));
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();

		// Navigate to My Processes, Click show process option in the rowActionDropdown
		sugar().navbar.navToModule(sugar().processes.moduleNamePlural);
		sugar().processes.listView.editRecord(1);
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1706 - Need Lib Support for the Process List View and Record view
		// Approve the triggered process
		new VoodooControl("input", "id", "ApproveBtn").click();
		VoodooUtils.focusDefault();
		warningAlert.confirmAlert();

		// Update the same Quote record to trigger the process again
		sugar().navbar.navToModule(sugar().quotes.moduleNamePlural);
		sugar().quotes.listView.clickRecord(1);
		sugar().quotes.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		quoteNameEditField.set(quoteNnoteInfo.get("quoteUpdate2"));
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();

		// Assert that under My Process list view, a new process should get triggered for Quotes module since the record has been updated
		sugar().navbar.navToModule(sugar().processes.moduleNamePlural);
		VoodooControl processRecordNameRow1 = sugar().processes.listView.getDetailField(1, "rec_name");
		VoodooControl processDefinitionNameRow1 = sugar().processes.listView.getDetailField(1, "pr_definition");
		processRecordNameRow1.assertEquals(quoteNnoteInfo.get("quoteUpdate2"), true);
		processDefinitionNameRow1.assertEquals(processDefNameQuotes, true);

		// Navigate to the Process Management list view
		sugar().navbar.clickModuleDropdown(sugar().processes);
		sugar().processes.menu.getControl("processManagement").click();
		VoodooUtils.waitForReady();
		// Assert that under Process Management list view, a new process should be displayed
		VoodooControl processManagementRecordName = sugar().processes.processManagementListView.getDetailField(1, "rec_name");
		VoodooControl processManagementDefinitionName = sugar().processes.processManagementListView.getDetailField(1, "pr_definition");
		processManagementRecordName.assertEquals(quoteNnoteInfo.get("quoteUpdate2"), true);
		processManagementDefinitionName.assertEquals(processDefNameQuotes, true);

		// Test for Sidecar module = Notes
		String processDefNameNotes = String.format("%s%s", testName, quoteNnoteInfo.get("notesSuffix"));

		// Import a process definition for Notes module that triggers a process when a record is updated(all updates) 
		// TODO: VOOD-1979 - "importProcessDefinition" method fails when we do more than 1 import per user per session
		sugar().navbar.selectMenuItem(sugar().processDefinitions, "importProcessDefinition");
		new VoodooFileField("input", "css", ".fld_project_import.edit input").set("src/test/resources/data/" + processDefNameNotes + ".bpm");
		// TODO: VOOD-1365
		new VoodooControl("a", "css", "span[data-voodoo-name='project_finish_button'] a").click();
		sugar().alerts.waitForLoadingExpiration();

		// Enable the process definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		enableDisableButton.click();
		warningAlert.confirmAlert();

		// Update the existing Notes record to trigger the process
		sugar().navbar.navToModule(sugar().notes.moduleNamePlural);
		sugar().notes.listView.editRecord(1);
		VoodooControl noteNameEditField = sugar().notes.listView.getEditField(1, "subject");
		noteNameEditField.set(quoteNnoteInfo.get("noteUpdate1"));
		sugar().notes.listView.saveRecord(1);

		// Navigate to My Processes, Click show process option in the rowActionDropdown
		sugar().navbar.navToModule(sugar().processes.moduleNamePlural);
		sugar().processes.listView.editRecord(1);

		// Approve the triggered process
		new VoodooControl("a", "css", "[data-voodoo-name='approve_button'] a").click();
		warningAlert.confirmAlert();
		VoodooUtils.waitForReady();

		// Update the same Notes Record to trigger the process again
		sugar().navbar.navToModule(sugar().notes.moduleNamePlural);
		sugar().notes.listView.editRecord(1);
		noteNameEditField.set(quoteNnoteInfo.get("noteUpdate2"));
		sugar().notes.listView.saveRecord(1);

		// Assert that under My Process list view, a new process should get triggered for Notes module since the record has been updated
		sugar().navbar.navToModule(sugar().processes.moduleNamePlural);
		processRecordNameRow1.assertEquals(quoteNnoteInfo.get("noteUpdate2"), true);
		processDefinitionNameRow1.assertEquals(processDefNameNotes, true);

		// Navigate to the Process Management list view
		sugar().navbar.clickModuleDropdown(sugar().processes);
		sugar().processes.menu.getControl("processManagement").click();
		VoodooUtils.waitForReady();
		// Assert that under Process Management list view, a new process should be displayed
		processManagementRecordName.assertEquals(quoteNnoteInfo.get("noteUpdate2"), true);
		processManagementDefinitionName.assertEquals(processDefNameNotes, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}