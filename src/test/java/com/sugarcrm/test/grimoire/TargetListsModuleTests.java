package com.sugarcrm.test.grimoire;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooTag;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class TargetListsModuleTests extends SugarTest {
	TargetListRecord myTargetList;

	public void setup() throws Exception {
		myTargetList = (TargetListRecord)sugar().targetlists.api.create();
		sugar().login();
		sugar().targetlists.navToListView();
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().navbar.clickModuleDropdown(sugar().targetlists);

		// Verify menu items
		// create target list and verify record
		sugar().targetlists.menu.getControl("createTargetList").click();
		sugar().targetlists.createDrawer.getEditField("targetlistName").set(testName);
		sugar().targetlists.createDrawer.save();
		if(sugar().alerts.getSuccess().queryVisible()) {
			sugar().alerts.getSuccess().closeAlert();
		}

		sugar().targetlists.listView.verifyField(1, "targetlistName", testName);

		// view target list and verify records
		sugar().navbar.clickModuleDropdown(sugar().targetlists);
		sugar().targetlists.menu.getControl("viewTargetLists").click();
		sugar().targetlists.listView.verifyField(1, "targetlistName", testName);
		sugar().targetlists.listView.verifyField(2, "targetlistName", myTargetList.getRecordIdentifier());

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyListHeaders() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListHeaders()...");

		// Verify all sort headers in listview
		for(String header : sugar().targetlists.listView.getHeaders()) {
			sugar().targetlists.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
		}

		VoodooUtils.voodoo.log.info("verifyListHeaders() complete.");
	}

	@Test
	public void sortOrderByTargetName() throws Exception {
		VoodooUtils.voodoo.log.info("Running sortOrderByTargetName()...");

		// 2 Target lists having 1 with default data and another with custom data
		FieldSet customTargetList = new FieldSet();
		customTargetList.put("targetlistName", "Sugar");
		sugar().targetlists.api.create(customTargetList);
		// VoodooUtils.refresh(); // Not working on Jenkins (btw, it should)
		sugar().targetlists.navToListView(); // needed to populate recently created record

		// Verify records after sort by 'target List name ' in descending and ascending order
		sugar().targetlists.listView.sortBy("headerName", false);
		sugar().targetlists.listView.verifyField(1, "targetlistName", myTargetList.getRecordIdentifier());
		sugar().targetlists.listView.verifyField(2, "targetlistName", customTargetList.get("targetlistName"));

		sugar().targetlists.listView.sortBy("headerName", true);
		sugar().targetlists.listView.verifyField(1, "targetlistName", customTargetList.get("targetlistName"));
		sugar().targetlists.listView.verifyField(2, "targetlistName", myTargetList.getRecordIdentifier());

		VoodooUtils.voodoo.log.info("sortOrderByTargetName() complete.");
	}

	@Test
	public void verifyInlineRecordFieldsInListView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInlineRecordFieldsInListView()...");

		String typeVal = "Seed";
		sugar().targetlists.listView.editRecord(1);

		// Verify inline edit fields
		// name
		sugar().targetlists.listView.getEditField(1, "targetlistName").assertEquals(myTargetList.get("targetlistName"), true);

		// type
		VoodooSelect type = (VoodooSelect)sugar().targetlists.listView.getEditField(1, "listType");
		type.set(typeVal);
		type.assertEquals(typeVal, true);

		// description (read only)
		VoodooControl description = sugar().targetlists.listView.getEditField(1, "description");
		description.assertAttribute("class", "edit", false);
		description.assertEquals(myTargetList.get("description"), true);

		// assigned
		VoodooSelect assignedTo = (VoodooSelect)sugar().targetlists.listView.getEditField(1, "assignedTo");
		assignedTo.set(sugar().users.getQAUser().get("userName"));
		assignedTo.assertEquals(sugar().users.getQAUser().get("userName"), true);

		// Date Created (read only)
		sugar().targetlists.listView.getEditField(1, "date_created_date").assertAttribute("class", "edit", false);

		// save the record with relation
		sugar().targetlists.listView.saveRecord(1);

		// verify detail fields
		// name
		sugar().targetlists.listView.getDetailField(1, "targetlistName").assertEquals(myTargetList.get("targetlistName"), true);

		// type
		sugar().targetlists.listView.getDetailField(1, "listType").assertEquals(typeVal, true);

		// description
		sugar().targetlists.listView.getDetailField(1, "description").assertEquals(myTargetList.get("description"), true);

		// assigned
		sugar().targetlists.listView.getDetailField(1, "assignedTo").assertEquals(sugar().users.getQAUser().get("userName"), true);

		// Date Created
		sugar().targetlists.listView.getDetailField(1, "date_created_date").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyInlineRecordFieldsInListView() complete.");
	}

	@Test
	public void verifyPreviewPaneFields() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyPreviewPaneFields()...");

		// preview record
		sugar().targetlists.listView.previewRecord(1);

		// Verify preview field values
		// name 
		sugar().previewPane.getPreviewPaneField("targetlistName").assertContains(myTargetList.get("targetlistName"), true);

		// description
		sugar().previewPane.getPreviewPaneField("description").assertEquals(myTargetList.get("description"), true);

		// list type
		sugar().previewPane.getPreviewPaneField("listType").assertVisible(true);

		// entry count
		sugar().previewPane.getPreviewPaneField("entryCount").assertVisible(true);

		// domain Name
		sugar().previewPane.getPreviewPaneField("domainName").assertVisible(true);

		// assigned to
		sugar().previewPane.getPreviewPaneField("assignedTo").assertEquals("Administrator", true);

		// date modified
		sugar().previewPane.getPreviewPaneField("date_modified_date").assertVisible(true);

		// date created
		sugar().previewPane.getPreviewPaneField("date_created_date").assertVisible(true);

		// teams
		sugar().previewPane.getPreviewPaneField("teams").assertContains("Global", true);

		// tags
		sugar().previewPane.getPreviewPaneField("tags").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyPreviewPaneFields() complete.");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		String typeVal = "Suppression List - By Domain";
		sugar().targetlists.listView.clickRecord(1);
		sugar().targetlists.recordView.edit();

		// name
		sugar().targetlists.recordView.getEditField("targetlistName").assertEquals(myTargetList.get("targetlistName"), true);

		// description
		sugar().targetlists.recordView.getEditField("description").assertEquals(myTargetList.get("description"), true);

		// type
		VoodooSelect type = (VoodooSelect)sugar().targetlists.recordView.getEditField("listType");
		type.set(typeVal);
		type.assertEquals(typeVal, true);

		// domain
		sugar().targetlists.recordView.getEditField("domainName").assertVisible(true);

		// entry count (read only)
		VoodooControl entryCount = sugar().targetlists.recordView.getEditField("entryCount");
		entryCount.assertVisible(true);
		entryCount.assertAttribute("class", "edit", false);

		// assigned to
		sugar().targetlists.recordView.getEditField("assignedTo").assertEquals("Administrator", true);

		// date modified (read only)
		VoodooControl dateModified = sugar().targetlists.recordView.getEditField("date_modified_date");
		dateModified.assertVisible(true);
		dateModified.assertAttribute("class", "edit", false);

		// date created (read only)
		VoodooControl dateCreated = sugar().targetlists.recordView.getEditField("date_created_date");
		dateCreated.assertVisible(true);
		dateCreated.assertAttribute("class", "edit", false);

		// team 
		sugar().targetlists.recordView.getEditField("teams").assertEquals("Global", true);

		// tag
		VoodooTag tag = (VoodooTag)sugar().targetlists.recordView.getEditField("tags");
		tag.set(testName);
		tag.assertContains(testName, true);

		// save the record 
		sugar().targetlists.recordView.save();

		// verify detail view fields
		sugar().targetlists.recordView.getDetailField("targetlistName").assertEquals(myTargetList.get("targetlistName"), true);

		// description
		sugar().targetlists.recordView.getDetailField("description").assertEquals(myTargetList.get("description"), true);

		// type
		sugar().targetlists.recordView.getDetailField("listType").assertEquals(typeVal, true);

		// domain
		sugar().targetlists.recordView.getDetailField("domainName").assertVisible(true);

		// entry count 
		sugar().targetlists.recordView.getDetailField("entryCount").assertVisible(true);

		// assigned to
		sugar().targetlists.recordView.getDetailField("assignedTo").assertEquals("Administrator", true);

		// team Name
		sugar().targetlists.recordView.getDetailField("teams").assertVisible(true);

		// created date
		sugar().targetlists.recordView.getDetailField("date_created_date").assertVisible(true);

		// modified date
		sugar().targetlists.recordView.getDetailField("date_modified_date").assertVisible(true);

		// tags
		sugar().targetlists.recordView.getDetailField("tags").assertContains(testName, true);

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	@Test
	public void verifySubpanelsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySubpanelsInRecordView()...");

		sugar().targetlists.listView.clickRecord(1);

		// Verify subpanels
		sugar().targetlists.recordView.subpanels.get(sugar().targets.moduleNamePlural).assertVisible(true);
		sugar().targetlists.recordView.subpanels.get(sugar().contacts.moduleNamePlural).assertVisible(true);
		sugar().targetlists.recordView.subpanels.get(sugar().leads.moduleNamePlural).assertVisible(true);
		sugar().targetlists.recordView.subpanels.get(sugar().users.moduleNamePlural).assertVisible(true);
		sugar().targetlists.recordView.subpanels.get(sugar().accounts.moduleNamePlural).assertVisible(true);
		sugar().targetlists.recordView.subpanels.get(sugar().campaigns.moduleNamePlural).assertVisible(true);		

		VoodooUtils.voodoo.log.info("verifySubpanelsInRecordView() complete.");
	}

	public void cleanup() throws Exception {}
}