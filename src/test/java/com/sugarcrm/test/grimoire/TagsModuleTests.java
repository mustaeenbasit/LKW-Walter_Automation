package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.TagRecord;
import com.sugarcrm.test.SugarTest;

/**
 * Contains self tests for menu dropdown items, listview headers, sortBy, edit/detail list & record hook values,
 * preview pane and subpanels on record view & create Tags in RecordView.
 * 
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class TagsModuleTests extends SugarTest {
	TagRecord myTag;

	public void setup() throws Exception {
		myTag = (TagRecord)sugar().tags.api.create();
		sugar().login();
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().tags.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().tags);

		// Verify menu items
		sugar().tags.menu.getControl("createTag").assertVisible(true);
		sugar().tags.menu.getControl("viewTags").assertVisible(true);
		sugar().tags.menu.getControl("importTags").assertVisible(true);

		sugar().navbar.clickModuleDropdown(sugar().tags); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyListHeaders() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListHeaders()...");

		sugar().tags.navToListView();
		for(String header : sugar().tags.listView.getHeaders()) {
			sugar().tags.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
		}

		VoodooUtils.voodoo.log.info("verifyListHeaders() complete.");
	}

	@Test
	public void sortOrderByName() throws Exception {
		VoodooUtils.voodoo.log.info("Running sortOrderByName()...");

		// 2 tags having 1 with default data and another with custom data
		FieldSet customData = new FieldSet();
		customData.put("name", "Nerd");
		sugar().tags.api.create(customData);
		sugar().tags.navToListView();

		// Verify records after sort by 'name ' in descending and ascending order
		sugar().tags.listView.sortBy("headerName", false);
		sugar().tags.listView.verifyField(1, "name", myTag.get("name"));
		sugar().tags.listView.verifyField(2, "name", customData.get("name"));

		sugar().tags.listView.sortBy("headerName", true);
		sugar().tags.listView.verifyField(1, "name", customData.get("name"));
		sugar().tags.listView.verifyField(2, "name", myTag.get("name"));

		VoodooUtils.voodoo.log.info("sortOrderByName() complete.");
	}

	@Test
	public void verifyPreviewPaneFields() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyPreviewPaneFields()...");

		sugar().tags.navToListView();
		sugar().tags.listView.previewRecord(1);
		sugar().previewPane.showMore();

		// Verify preview field values
		// name
		VoodooControl name = sugar().previewPane.getPreviewPaneField("name");
		name.assertVisible(true);
		name.assertEquals(myTag.get("name"), true);

		// assignedTo
		sugar().previewPane.getPreviewPaneField("relAssignedTo").assertVisible(true);

		// description
		VoodooControl description = sugar().previewPane.getPreviewPaneField("description");
		description.assertVisible(true);
		description.assertEquals(myTag.get("description"), true);

		// date created
		sugar().previewPane.getPreviewPaneField("date_entered_date").assertVisible(true);

		// created by
		sugar().previewPane.getPreviewPaneField("dateEnteredBy").assertVisible(true);

		// Date modified
		sugar().previewPane.getPreviewPaneField("date_modified_date").assertVisible(true);

		// modified by
		sugar().previewPane.getPreviewPaneField("dateModifiedBy").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyPreviewPaneFields() complete.");
	}

	@Test
	public void verifyInlineRecordFieldsInListView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInlineRecordFieldsInListView()...");

		sugar().tags.navToListView();
		sugar().tags.listView.editRecord(1);

		// name
		VoodooControl name = sugar().tags.listView.getEditField(1, "name");
		name.assertVisible(true);
		name.assertEquals(myTag.get("name"), true);

		// created by (read-only)
		VoodooControl createdBy = sugar().tags.listView.getEditField(1, "dateEnteredBy");
		createdBy.assertVisible(true);
		createdBy.assertAttribute("class", "edit", false);

		// assignedTo
		sugar().tags.listView.getEditField(1, "relAssignedTo").assertVisible(true);

		// date created (read-only)
		VoodooControl dateCreated = sugar().tags.listView.getEditField(1, "date_entered_date");
		dateCreated.assertVisible(true);
		dateCreated.assertAttribute("class", "edit", false);

		// date modified (read-only)
		VoodooControl dateModified = sugar().tags.listView.getEditField(1, "date_modified_date");
		dateModified.assertVisible(true);
		dateModified.assertAttribute("class", "edit", false);

		// cancel record
		sugar().tags.listView.cancelRecord(1);

		// verify detail fields
		// name
		VoodooControl tagName = sugar().tags.listView.getDetailField(1, "name");
		tagName.assertVisible(true);
		tagName.assertEquals(myTag.get("name"), true);

		// assignedTo
		sugar().tags.listView.getDetailField(1, "relAssignedTo").assertVisible(true);

		// date created
		sugar().tags.listView.getDetailField(1, "date_entered_date").assertVisible(true);

		// date modified
		sugar().tags.listView.getDetailField(1, "date_modified_date").assertVisible(true);

		// created by
		sugar().tags.listView.getDetailField(1, "dateEnteredBy").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyInlineRecordFieldsInListView() complete.");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		sugar().tags.navToListView();
		sugar().tags.listView.clickRecord(1);
		sugar().tags.recordView.edit();
		sugar().tags.recordView.showMore();

		// name
		VoodooControl name = sugar().tags.recordView.getEditField("name");
		name.assertVisible(true);
		name.assertEquals(myTag.getRecordIdentifier(), true);

		// description
		VoodooControl description = sugar().tags.recordView.getEditField("description");
		description.assertVisible(true);
		description.assertEquals(myTag.get("description"), true);

		// relAssignedTo
		sugar().tags.recordView.getEditField("relAssignedTo").assertVisible(true);

		// date created (read-only)
		VoodooControl dateCreated = sugar().tags.recordView.getEditField("date_entered_date");
		dateCreated.assertVisible(true);
		dateCreated.assertAttribute("class", "edit", false);

		// created by (read-only)
		VoodooControl createdBy = sugar().tags.recordView.getEditField("dateEnteredBy");
		createdBy.assertVisible(true);
		createdBy.assertAttribute("class", "edit", false);

		// date modified (read-only)
		VoodooControl dateModified = sugar().tags.recordView.getEditField("date_modified_date");
		dateModified.assertVisible(true);
		dateModified.assertAttribute("class", "edit", false);

		// modified by (read-only)
		VoodooControl modifiedBy = sugar().tags.recordView.getEditField("dateModifiedBy");
		modifiedBy.assertVisible(true);
		modifiedBy.assertAttribute("class", "edit", false);

		// cancel the record 
		sugar().tags.recordView.cancel();
		VoodooUtils.waitForReady();

		// verify detail view fields
		// name
		VoodooControl tagName = sugar().tags.recordView.getDetailField("name");
		tagName.assertVisible(true);
		tagName.assertEquals(myTag.getRecordIdentifier(), true);

		// description
		VoodooControl tagDescription = sugar().tags.recordView.getDetailField("description");
		tagDescription.assertVisible(true);
		tagDescription.assertEquals(myTag.get("description"), true);

		// relAssignedTo
		sugar().tags.recordView.getDetailField("relAssignedTo").assertVisible(true);

		// date created
		sugar().tags.recordView.getDetailField("date_entered_date").assertVisible(true);

		// created by
		sugar().tags.recordView.getDetailField("dateEnteredBy").assertVisible(true);

		// date modified
		sugar().tags.recordView.getDetailField("date_modified_date").assertVisible(true);

		// modified by
		sugar().tags.recordView.getDetailField("dateModifiedBy").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	@Test
	public void verifySubpanelsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySubpanelsInRecordView()...");

		sugar().tags.navToListView();
		sugar().tags.listView.clickRecord(1);

		// Verify subpanels
		sugar().tags.recordView.subpanels.get(sugar().leads.moduleNamePlural).assertVisible(true);
		sugar().tags.recordView.subpanels.get(sugar().cases.moduleNamePlural).assertVisible(true);
		sugar().tags.recordView.subpanels.get(sugar().targetlists.moduleNamePlural).assertVisible(true);
		sugar().tags.recordView.subpanels.get(sugar().targets.moduleNamePlural).assertVisible(true);
		sugar().tags.recordView.subpanels.get(sugar().contacts.moduleNamePlural).assertVisible(true);
		sugar().tags.recordView.subpanels.get(sugar().accounts.moduleNamePlural).assertVisible(true);
		sugar().tags.recordView.subpanels.get(sugar().opportunities.moduleNamePlural).assertVisible(true);
		sugar().tags.recordView.subpanels.get(sugar().notes.moduleNamePlural).assertVisible(true);
		sugar().tags.recordView.subpanels.get(sugar().calls.moduleNamePlural).assertVisible(true);
		sugar().tags.recordView.subpanels.get(sugar().meetings.moduleNamePlural).assertVisible(true);
		sugar().tags.recordView.subpanels.get(sugar().tasks.moduleNamePlural).assertVisible(true);
		sugar().tags.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural).assertVisible(true);
		sugar().tags.recordView.subpanels.get(sugar().productCatalog.moduleNamePlural).assertVisible(true);
		sugar().tags.recordView.subpanels.get(sugar().knowledgeBase.moduleNamePlural).assertVisible(true);

		VoodooUtils.voodoo.log.info("verifySubpanelsInRecordView() complete.");
	}

	@Test
	public void addTagsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running addTagsInRecordView()...");

		AccountRecord myAcc = (AccountRecord)sugar().accounts.api.create();

		myAcc.navToRecord();
		sugar().accounts.recordView.edit();
		VoodooControl editTagControl = sugar().accounts.recordView.getEditField("tags");
		editTagControl.set("Tag1");
		editTagControl.set("Tag2");
		sugar().accounts.recordView.save();
		VoodooControl detailTagControl = sugar().accounts.recordView.getDetailField("tags");
		detailTagControl.assertContains("Tag1", true);
		detailTagControl.assertContains("Tag2", true);

		VoodooUtils.voodoo.log.info("addTagsInRecordView() complete.");
	}

	public void cleanup() throws Exception {}
}