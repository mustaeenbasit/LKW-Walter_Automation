package com.sugarcrm.test.grimoire;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.test.SugarTest;

/**
 * Contains self tests for menu dropdown items, listview headers, sortBy, edit/detail list & record hook values,
 * preview pane on record view.
 * 
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class NotesModuleTests extends SugarTest {
	NoteRecord myNote;

	public void setup() throws Exception {
		myNote = (NoteRecord)sugar().notes.api.create();
		sugar().login();
		sugar().notes.navToListView();
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().navbar.clickModuleDropdown(sugar().notes);

		// Verify menu items
		sugar().notes.menu.getControl("createNote").assertVisible(true);
		sugar().notes.menu.getControl("viewNotes").assertVisible(true);
		sugar().notes.menu.getControl("importNotes").assertVisible(true);

		sugar().navbar.clickModuleDropdown(sugar().notes); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyListHeaders() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListHeaders()...");

		// TODO: VOOD-1768 - Once resolved "parent_name" header check should remove it from for loop
		new VoodooControl("th", "css", "th[data-fieldname=parent_name]").assertVisible(true);

		// Verify all sort headers in listview
		for(String header : sugar().notes.listView.getHeaders()) {
			if(!header.equals("parent_name")) // parent_name field has no sort feature
				sugar().notes.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
		}

		VoodooUtils.voodoo.log.info("verifyListHeaders() test complete.");
	}

	@Test
	public void sortOrderByName() throws Exception {
		VoodooUtils.voodoo.log.info("Running sortOrderByName()...");

		// 2 notes having 1 with default data and another with custom data
		FieldSet customNote = new FieldSet();
		customNote.put("subject", "Call to Jim");
		sugar().notes.api.create(customNote);
		sugar().notes.navToListView(); // to reload custom note record on listview, VoodooUtils.refresh() is not working on CI

		// Verify records after sort by 'subject ' in descending and ascending order
		sugar().notes.listView.sortBy("headerName", false);
		sugar().notes.listView.verifyField(1, "subject", customNote.get("subject"));
		sugar().notes.listView.verifyField(2, "subject", myNote.getRecordIdentifier());

		sugar().notes.listView.sortBy("headerName", true);
		sugar().notes.listView.verifyField(1, "subject", myNote.getRecordIdentifier());
		sugar().notes.listView.verifyField(2, "subject", customNote.get("subject"));

		VoodooUtils.voodoo.log.info("sortOrderByName() test complete.");
	}

	@Test
	public void verifyPreviewPaneFields() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyPreviewPaneFields()...");

		// preview record
		sugar().notes.listView.previewRecord(1);
		sugar().previewPane.showMore();

		// Verify preview field values
		// subject 
		sugar().previewPane.getPreviewPaneField("subject").assertEquals(myNote.get("subject"), true);

		// contact name
		sugar().previewPane.getPreviewPaneField("contact").assertVisible(true);

		// related to
		sugar().previewPane.getPreviewPaneField("relRelatedToValue").assertVisible(true);

		// description
		sugar().previewPane.getPreviewPaneField("description").assertEquals(myNote.get("description"), true);

		// teams
		sugar().previewPane.getPreviewPaneField("relTeam").assertVisible(true);

		// tags
		sugar().previewPane.getPreviewPaneField("tags").assertVisible(true);

		// assigned
		sugar().previewPane.getPreviewPaneField("relAssignedTo").assertEquals("Administrator", true);

		// Date Created
		sugar().previewPane.getPreviewPaneField("date_entered_date").assertVisible(true);

		// Date modified
		sugar().previewPane.getPreviewPaneField("date_modified_date").assertVisible(true);

		// show in portal (disabled and not checked)
		VoodooControl showInPortal = sugar().previewPane.getPreviewPaneField("checkDisplayInPortal");
		Assert.assertTrue("portal viewable is enabled", showInPortal.isDisabled());
		Assert.assertTrue("portal viewable is checked", !(showInPortal.isChecked()));

		VoodooUtils.voodoo.log.info("verifyPreviewPaneFields() complete.");
	}

	@Test
	public void verifyInlineRecordFieldsInListView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInlineRecordFieldsInListView()...");

		sugar().notes.listView.editRecord(1);

		// subject
		sugar().notes.listView.getEditField(1, "subject").assertEquals(myNote.get("subject"), true);

		// contact
		sugar().notes.listView.getEditField(1, "contact").assertVisible(true);

		// Related module
		sugar().notes.listView.getEditField(1, "relRelatedToModule").assertVisible(true);

		// Related value
		sugar().notes.listView.getEditField(1, "relRelatedToValue").assertVisible(true);

		// attachment
		sugar().notes.listView.getEditField(1, "attachment").assertVisible(true);

		// created by (read-only)
		VoodooControl createdBy = sugar().notes.listView.getEditField(1, "created_by");
		createdBy.assertVisible(true);
		createdBy.assertAttribute("class", "edit", false);

		// date created (read-only)
		VoodooControl dateCreated = sugar().notes.listView.getEditField(1, "date_entered_date");
		dateCreated.assertVisible(true);
		dateCreated.assertAttribute("class", "edit", false);

		// date modified (read-only)
		VoodooControl dateModified = sugar().notes.listView.getEditField(1, "date_modified_date");
		dateModified.assertVisible(true);
		dateModified.assertAttribute("class", "edit", false);

		// cancel record
		sugar().notes.listView.cancelRecord(1);

		// verify detail fields
		// subject
		sugar().notes.listView.getDetailField(1, "subject").assertEquals(myNote.get("subject"), true);

		// contact name	
		sugar().notes.listView.getDetailField(1, "contact").assertVisible(true);

		// Related value
		sugar().notes.listView.getDetailField(1, "relRelatedToValue").assertVisible(true);

		// attachment
		sugar().notes.listView.getDetailField(1, "attachment").assertVisible(true);

		// created by		
		sugar().notes.listView.getDetailField(1, "created_by").assertVisible(true);

		// Date Created
		sugar().notes.listView.getDetailField(1, "date_entered_date").assertVisible(true);

		// Date modified
		sugar().notes.listView.getDetailField(1, "date_modified_date").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyInlineRecordFieldsInListView() complete.");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		sugar().notes.listView.clickRecord(1);
		sugar().notes.recordView.edit();
		sugar().notes.recordView.showMore();

		// subject
		sugar().notes.recordView.getEditField("subject").assertEquals(myNote.get("subject"), true);

		// contact name
		sugar().notes.recordView.getEditField("contact").assertVisible(true);

		// Related module
		sugar().notes.recordView.getEditField("relRelatedToModule").assertVisible(true);

		// Related value
		sugar().notes.recordView.getEditField("relRelatedToValue").assertVisible(true);

		// attachment
		sugar().notes.recordView.getEditField("attachment").assertVisible(true);

		// description
		sugar().notes.recordView.getEditField("description").assertEquals(myNote.get("description"), true);

		// teams
		sugar().notes.recordView.getEditField("relTeam").assertEquals("Global", true);

		// assignedTo
		sugar().notes.recordView.getEditField("relAssignedTo").assertEquals("Administrator", true);

		// tags
		sugar().notes.recordView.getEditField("tags").assertVisible(true);

		// date created (read only)
		VoodooControl dateCreated = sugar().notes.recordView.getEditField("date_entered_date");
		dateCreated.assertVisible(true);
		dateCreated.assertAttribute("class", "edit", false);

		// date modified (read only)
		VoodooControl dateModified = sugar().notes.recordView.getEditField("date_modified_date");
		dateModified.assertVisible(true);
		dateModified.assertAttribute("class", "edit", false);

		// shows in portal
		VoodooControl showInPortal = sugar().notes.recordView.getEditField("checkDisplayInPortal");
		showInPortal.assertVisible(true);
		Assert.assertTrue("portal viewable is checked", !(showInPortal.isChecked()));

		// cancel the record 
		sugar().notes.recordView.cancel();

		// verify detail view fields
		// name
		sugar().notes.recordView.getDetailField("subject").assertEquals(myNote.get("subject"), true);

		// contact name
		sugar().notes.recordView.getDetailField("contact").assertVisible(true);

		// Related module
		sugar().notes.recordView.getDetailField("relRelatedToModule").assertVisible(true);

		// Related value
		sugar().notes.recordView.getDetailField("relRelatedToValue").assertVisible(true);

		// description
		sugar().notes.recordView.getDetailField("description").assertEquals(myNote.get("description"), true);

		// teams
		sugar().notes.recordView.getDetailField("relTeam").assertContains("Global", true);

		// assignedTo
		sugar().notes.recordView.getDetailField("relAssignedTo").assertEquals("Administrator", true);

		// tags
		sugar().notes.recordView.getDetailField("tags").assertVisible(true);

		// date created
		sugar().notes.recordView.getDetailField("date_entered_date").assertVisible(true);

		// date modified
		sugar().notes.recordView.getDetailField("date_modified_date").assertVisible(true);

		// shows in portal
		sugar().notes.recordView.getDetailField("checkDisplayInPortal").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	public void cleanup() throws Exception {}
}