package com.sugarcrm.test.grimoire;

import com.sugarcrm.sugar.records.BugRecord;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * Contains self tests for menu dropdown items, listview headers, sortBy, edit/detail list & detail
 * hook values, preview pane & subpanels on record view.
 *
 * @author Snigdha Sivadas <ssivadas@sugarcrm.com>
 */
public class BugsModuleTests extends SugarTest {
	BugRecord myBug;

	public void setup() throws Exception {
		sugar().login();
		myBug = (BugRecord) sugar().bugs.api.create();
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
		sugar().bugs.navToListView();
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().navbar.clickModuleDropdown(sugar().bugs);

		// Verify menu items
		sugar().bugs.menu.getControl("createBug").assertVisible(true);
		sugar().bugs.menu.getControl("viewBugs").assertVisible(true);
		sugar().bugs.menu.getControl("viewBugReports").assertVisible(true);
		sugar().bugs.menu.getControl("importBugs").assertVisible(true);

		sugar().navbar.clickModuleDropdown(sugar().bugs); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyListHeaders() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListHeaders()...");

		sugar().bugs.listView.getControl("headerType").assertVisible(true);

		sugar().bugs.listView.toggleHeaderColumn("type");

		sugar().bugs.listView.getControl("headerType").assertVisible(false);

		// Verify all sort headers in listview
		for (String header : sugar().bugs.listView.getHeaders()) {
			// team_name without sort option
			if (!header.equals("type")) {
				sugar().bugs.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils
						.camelCase(header))).assertExists(true);
			}
		}

		VoodooUtils.voodoo.log.info("verifyListHeaders() complete.");
	}

	@Test
	public void sortOrderByName() throws Exception {
		VoodooUtils.voodoo.log.info("Running sortOrderByName()...");

		// Two bugs having one record set to default data and another with custom data
		FieldSet customBug = new FieldSet();
		customBug.put("name", "Error at upgrade");
		sugar().bugs.api.create(customBug);

		// need to reload UI, recently data created via API, refresh
		sugar().bugs.navToListView();

		// Verify records after sort by 'name ' in descending and ascending order
		sugar().bugs.listView.sortBy("headerName", false);
		sugar().bugs.listView.verifyField(1, "name", myBug.getRecordIdentifier());
		sugar().bugs.listView.verifyField(2, "name", customBug.get("name"));

		sugar().bugs.listView.sortBy("headerName", true);
		sugar().bugs.listView.verifyField(1, "name", customBug.get("name"));
		sugar().bugs.listView.verifyField(2, "name", myBug.getRecordIdentifier());

		VoodooUtils.voodoo.log.info("sortOrderByName() complete.");
	}

	@Test
	public void verifySubpanelsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySubpanelsInRecordView()...");

		sugar().bugs.listView.clickRecord(1);

		// Verify subpanels
		sugar().bugs.recordView.subpanels.get(sugar().calls.moduleNamePlural).assertVisible(true);
		sugar().bugs.recordView.subpanels.get(sugar().meetings.moduleNamePlural).assertVisible
				(true);
		sugar().bugs.recordView.subpanels.get(sugar().tasks.moduleNamePlural).assertVisible(true);
		sugar().bugs.recordView.subpanels.get(sugar().notes.moduleNamePlural).assertVisible(true);
		sugar().bugs.recordView.subpanels.get(sugar().documents.moduleNamePlural).assertVisible
				(true);
		sugar().bugs.recordView.subpanels.get(sugar().contacts.moduleNamePlural).assertVisible
				(true);
		sugar().bugs.recordView.subpanels.get(sugar().emails.moduleNamePlural).assertVisible(true);

		VoodooUtils.voodoo.log.info("verifySubpanelsInRecordView() complete.");
	}

	@Test
	public void verifyInlineRecordFieldsInListView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInlineRecordFieldsInListView()...");

		sugar().bugs.listView.editRecord(1);

		// name
		sugar().bugs.listView.getEditField(1, "name").assertEquals(myBug.get("name"), true);

		// priority
		sugar().bugs.listView.getEditField(1, "priority").assertEquals(myBug.get("priority"), true);

		// status
		sugar().bugs.listView.getEditField(1, "status").assertEquals(myBug.get("status"), true);

		// type
		sugar().bugs.listView.getEditField(1, "type").assertEquals(myBug.get("type"), true);

		// assigned user
		sugar().bugs.listView.getEditField(1, "relAssignedTo").assertEquals(myBug.get
				("relAssignedTo"), true);

		VoodooUtils.voodoo.log.info("verifyInlineRecordFieldsInListView() complete.");
	}

	@Test
	public void verifyPreviewPaneFields() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyPreviewPaneFields()...");

		// preview record
		sugar().bugs.listView.previewRecord(1);
		sugar().previewPane.showMore();

		// Verify preview field values
		// subject
		sugar().previewPane.getPreviewPaneField("name").assertEquals(myBug.get("name"), true);

		// priority
		sugar().previewPane.getPreviewPaneField("priority").assertVisible(true);

		// status
		sugar().previewPane.getPreviewPaneField("status").assertEquals(myBug.get("status"), true);

		// description
		sugar().previewPane.getPreviewPaneField("description").assertEquals(myBug.get("description"),
				true);

		// type
		sugar().previewPane.getPreviewPaneField("type").assertEquals(myBug.get("type"), true);

		// assigned
		sugar().bugs.listView.getDetailField(1, "relAssignedTo").assertEquals("Administrator", true);

		// work log
		sugar().previewPane.getPreviewPaneField("work_log").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyPreviewPaneFields() complete.");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		sugar().bugs.listView.clickRecord(1);
		sugar().bugs.recordView.edit();
		sugar().bugs.recordView.showMore();

		// name
		sugar().bugs.recordView.getEditField("name").assertEquals(myBug.get("name"), true);

		// priority
		VoodooSelect priority = (VoodooSelect) sugar().bugs.recordView.getEditField("priority");
		priority.set(myBug.get("priority"));
		priority.assertEquals(myBug.get("priority"), true);

		// status
		sugar().bugs.recordView.getEditField("status").assertEquals(myBug.get("status"), true);

		// assignedTo
		sugar().bugs.recordView.getEditField("relAssignedTo").assertEquals("Administrator", true);

		// description
		sugar().bugs.recordView.getEditField("description").assertEquals(myBug.get("description"),
				true);

		// resolution
		sugar().bugs.recordView.getEditField("resolution").assertExists(true);

		// type
		sugar().bugs.recordView.getEditField("type").assertEquals(myBug.get("type"), true);

		// source
		sugar().bugs.recordView.getEditField("source").assertExists(true);

		// work log
		sugar().bugs.recordView.getEditField("work_log").assertEquals(myBug.get("work_log"), true);

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	public void cleanup() throws Exception {}
}