package com.sugarcrm.test.grimoire;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

import org.junit.Assert;
import org.junit.Test;

/**
 * Contains self tests for menu dropdown items, listview headers, sortBy, edit/detail list & detail hook values,
 * preview pane & subpanels on record view with OPP + RLI settings.
 * 
 * @author Ashish Jabble <ajabble@sugar()crm.com>
 */
public class OpportunityModuleTests extends SugarTest {
	AccountRecord myAccount;
	FieldSet oppDefaultData = new FieldSet();

	public void setup() throws Exception {
		oppDefaultData = sugar().opportunities.getDefaultData();
		myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().opportunities.api.create();
		sugar().login();
		sugar().opportunities.navToListView();
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().navbar.clickModuleDropdown(sugar().opportunities);

		// Verify menu items
		sugar().opportunities.menu.getControl("createOpportunity").assertVisible(true);
		sugar().opportunities.menu.getControl("viewOpportunities").assertVisible(true);
		sugar().opportunities.menu.getControl("viewOpportunityReports").assertVisible(true);
		sugar().opportunities.menu.getControl("importOpportunities").assertVisible(true);
		sugar().navbar.clickModuleDropdown(sugar().opportunities); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyListHeaders() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListHeaders()...");

		// Verify all headers in listview
		for(String header : sugar().opportunities.listView.getHeaders()){
			// For opportunity with RLI view no "sales_stage" and "probability" column in listview
			// commit_stage available if "forecasts" enabled 
			if(!(header.equals("commit_stage") || header.equals("sales_stage") || header.equals("probability"))){
				sugar().opportunities.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
			}
		}

		VoodooUtils.voodoo.log.info("verifyListHeaders() test complete.");
	}

	@Test
	public void sortOrderByLeadSource() throws Exception {
		VoodooUtils.voodoo.log.info("Running sortOrderByLeadSource()...");

		String leadSource = "Cold Call";

		// 2 Opps having 1 with default data and another with custom data (lead source)
		FieldSet customLeadSource = new FieldSet();
		customLeadSource.put("leadSource", leadSource);
		sugar().opportunities.api.create(customLeadSource);
		sugar().opportunities.navToListView(); // reload data source

		// Verify records after sort by 'lead source' in descending and ascending order
		sugar().opportunities.listView.sortBy("headerLeadsource", false);
		sugar().opportunities.listView.verifyField(1, "leadSource", oppDefaultData.get("leadSource"));
		sugar().opportunities.listView.verifyField(2, "leadSource", leadSource);

		sugar().opportunities.listView.sortBy("headerLeadsource", true);
		sugar().opportunities.listView.verifyField(1, "leadSource", leadSource);
		sugar().opportunities.listView.verifyField(2, "leadSource", oppDefaultData.get("leadSource"));

		VoodooUtils.voodoo.log.info("sortOrderByLeadSource() test complete.");
	}

	@Test
	public void verifyInlineRecordFieldsInListView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInlineRecordFieldsInListView()...");

		sugar().opportunities.listView.editRecord(1);

		// Verify inline edit fields
		// name
		sugar().opportunities.listView.getEditField(1, "name").assertEquals(oppDefaultData.get("name"), true);

		// account name
		VoodooSelect accountName = (VoodooSelect)sugar().opportunities.listView.getEditField(1, "relAccountName");
		accountName.set(myAccount.getRecordIdentifier());
		accountName.assertEquals(myAccount.getRecordIdentifier(), true);

		// status (read only)
		sugar().opportunities.listView.getEditField(1, "status").assertVisible(true);

		// likelyCase (disabled)
		VoodooControl likelyCase = sugar().opportunities.listView.getEditField(1, "likelyCase");
		likelyCase.assertVisible(true);
		Assert.assertTrue("Likely case field is enabled", likelyCase.isDisabled());

		// type
		VoodooSelect type = (VoodooSelect)sugar().opportunities.listView.getEditField(1, "type");
		type.scrollIntoViewIfNeeded(false);
		type.set(oppDefaultData.get("type"));
		type.assertEquals(oppDefaultData.get("type"), true);

		// lead source
		VoodooSelect leadSource = (VoodooSelect)sugar().opportunities.listView.getEditField(1, "leadSource");
		leadSource.scrollIntoViewIfNeeded(false);
		leadSource.assertEquals(oppDefaultData.get("leadSource"), true);

		// next step
		VoodooControl nextStep = sugar().opportunities.listView.getEditField(1, "nextStep");
		nextStep.scrollIntoViewIfNeeded(false);
		nextStep.assertEquals(oppDefaultData.get("nextStep"), true);

		// date closed (disabled)
		VoodooControl dateClose = sugar().opportunities.listView.getEditField(1, "date_closed");
		dateClose.scrollIntoViewIfNeeded(false);
		Assert.assertTrue("Expected close date field is enabled", dateClose.isDisabled());

		// date created by (read only)
		VoodooControl createdBy = sugar().opportunities.listView.getEditField(1, "created_by");
		createdBy.assertEquals("admin2", true);
		createdBy.assertAttribute("class", "edit", false);

		// user
		sugar().opportunities.listView.getEditField(1, "relAssignedTo").assertExists(true);

		// Date Created (read only)
		VoodooControl dateCreated = sugar().opportunities.listView.getEditField(1, "date_entered_date");
		dateCreated.assertExists(true);
		dateCreated.assertAttribute("class", "edit", false);

		//  modified by (read only)
		VoodooControl modifiedBy = sugar().opportunities.listView.getEditField(1, "modified_by");
		modifiedBy.assertEquals("admin2", true);
		modifiedBy.assertAttribute("class", "edit", false);

		// save the record
		sugar().opportunities.listView.saveRecord(1);
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// verify list fields
		// name
		sugar().opportunities.listView.getDetailField(1, "name").assertEquals(oppDefaultData.get("name"), true);

		// account name
		sugar().opportunities.listView.getDetailField(1, "relAccountName").assertEquals(myAccount.getRecordIdentifier(), true);

		// status
		sugar().opportunities.listView.getDetailField(1, "status").assertVisible(true);

		// likelyCase
		sugar().opportunities.listView.getDetailField(1, "likelyCase").assertVisible(true);

		// type
		VoodooControl typeList = sugar().opportunities.listView.getDetailField(1, "type");
		typeList.scrollIntoViewIfNeeded(false);
		typeList.assertEquals(oppDefaultData.get("type"), true);

		// lead source
		VoodooControl leadSourceList = sugar().opportunities.listView.getDetailField(1, "leadSource");
		leadSourceList.scrollIntoViewIfNeeded(false);
		leadSourceList.assertEquals(oppDefaultData.get("leadSource"), true);

		// next step
		nextStep = sugar().opportunities.listView.getDetailField(1, "nextStep");
		nextStep.scrollIntoViewIfNeeded(false);
		nextStep.assertEquals(oppDefaultData.get("nextStep"), true);

		// date closed
		dateClose = sugar().opportunities.listView.getDetailField(1, "date_closed");
		dateClose.scrollIntoViewIfNeeded(false);

		// created by
		sugar().opportunities.listView.getDetailField(1, "created_by").assertEquals("admin2", true);

		// assigned
		sugar().opportunities.listView.getDetailField(1, "relAssignedTo").assertExists(true);

		// Date Created
		sugar().opportunities.listView.getDetailField(1, "date_entered_date").assertExists(true);

		// modified by 
		sugar().opportunities.listView.getDetailField(1, "modified_by").assertEquals("Administrator", true);

		VoodooUtils.voodoo.log.info("verifyInlineRecordFieldsInListView() complete.");
	}

	@Test
	public void verifyPreviewPaneFields() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyPreviewPaneFields()...");

		// preview record
		sugar().opportunities.listView.previewRecord(1);
		sugar().previewPane.showMore();

		// Verify preview field values
		// name 
		sugar().previewPane.getPreviewPaneField("name").assertEquals(oppDefaultData.get("name"), true);

		// account name
		//sugar().previewPane.getPreviewPaneField("relAccountName").assertExists(true);

		// date closed
		sugar().previewPane.getPreviewPaneField("date_closed").assertExists(true);

		// Likely
		sugar().previewPane.getPreviewPaneField("likelyCase").assertExists(true);

		// Best
		sugar().previewPane.getPreviewPaneField("bestCase").assertExists(true);

		// Worst
		sugar().previewPane.getPreviewPaneField("worstCase").assertExists(true);

		// status
		sugar().previewPane.getPreviewPaneField("status").assertExists(true);

		// Next Step
		sugar().previewPane.getPreviewPaneField("nextStep").assertExists(true);

		// type
		sugar().previewPane.getPreviewPaneField("type").assertExists(true);

		// Lead Source
		sugar().previewPane.getPreviewPaneField("leadSource").assertExists(true);

		// Campaign
		//sugar().previewPane.getPreviewPaneField("relCampaign").assertExists(true);

		// Assigned
		sugar().previewPane.getPreviewPaneField("relAssignedTo").assertExists(true);

		// Teams
		sugar().previewPane.getPreviewPaneField("relTeam").assertExists(true);

		// Description
		sugar().previewPane.getPreviewPaneField("description").assertVisible(true);

		// date modified
		sugar().previewPane.getPreviewPaneField("date_modified_date").assertVisible(true);

		// date created
		sugar().previewPane.getPreviewPaneField("date_entered_date").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyPreviewPaneFields() complete.");
	}

	@Test
	public void verifySubpanelsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySubpanelsInRecordView()...");

		sugar().opportunities.listView.clickRecord(1);

		// Verify subpanels on record view
		sugar().opportunities.recordView.subpanels.get(sugar().calls.moduleNamePlural).assertVisible(true);
		sugar().opportunities.recordView.subpanels.get(sugar().meetings.moduleNamePlural).assertVisible(true);
		sugar().opportunities.recordView.subpanels.get(sugar().tasks.moduleNamePlural).assertVisible(true);
		sugar().opportunities.recordView.subpanels.get(sugar().notes.moduleNamePlural).assertVisible(true);
		sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural).assertVisible(true);
		sugar().opportunities.recordView.subpanels.get(sugar().quotes.moduleNamePlural).assertVisible(true);
		sugar().opportunities.recordView.subpanels.get(sugar().contacts.moduleNamePlural).assertVisible(true);		
		sugar().opportunities.recordView.subpanels.get(sugar().leads.moduleNamePlural).assertVisible(true);		
		sugar().opportunities.recordView.subpanels.get(sugar().documents.moduleNamePlural).assertVisible(true);		
		sugar().opportunities.recordView.subpanels.get(sugar().emails.moduleNamePlural).assertVisible(true);

		VoodooUtils.voodoo.log.info("verifySubpanelsInRecordView() complete.");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.edit();

		// Verify edit record fields
		// name (required field)
		VoodooControl name = sugar().opportunities.recordView.getEditField("name");
		name.assertEquals(oppDefaultData.get("name"), true);
		name.assertAttribute("class", "required", true);

		// account name
		sugar().opportunities.recordView.getEditField("relAccountName").assertExists(true);

		// expected close date (disabled)
		VoodooControl expectedCloseDate = sugar().opportunities.recordView.getEditField("date_closed");
		expectedCloseDate.assertExists(true);
		expectedCloseDate.assertAttribute("disabled", Boolean.toString(true));

		// Likely (disabled)
		VoodooControl likelyCase = sugar().opportunities.recordView.getEditField("likelyCase");
		likelyCase.assertVisible(true);
		likelyCase.assertAttribute("disabled", Boolean.toString(true));

		// Best case (disabled)
		VoodooControl bestCase = sugar().opportunities.recordView.getEditField("bestCase");
		bestCase.assertVisible(true);
		bestCase.assertAttribute("disabled", Boolean.toString(true));

		// Worst case (disabled)
		VoodooControl worstCase = sugar().opportunities.recordView.getEditField("worstCase");
		worstCase.assertVisible(true);
		worstCase.assertAttribute("disabled", Boolean.toString(true));

		// show more fields
		sugar().opportunities.recordView.showMore();

		// status
		sugar().opportunities.recordView.getEditField("status").assertExists(true);

		// next step
		sugar().opportunities.recordView.getEditField("nextStep").assertVisible(true);

		// type
		sugar().opportunities.recordView.getEditField("type").assertVisible(true);

		// lead source
		sugar().opportunities.recordView.getEditField("leadSource").assertVisible(true);

		// campaign
		sugar().opportunities.recordView.getEditField("relCampaign").assertVisible(true);

		// description
		sugar().opportunities.recordView.getEditField("description").assertEquals(oppDefaultData.get("description"), true);

		// assigned To
		sugar().opportunities.recordView.getEditField("relAssignedTo").assertVisible(true);

		// teams
		sugar().opportunities.recordView.getEditField("relTeam").assertVisible(true);

		// date created
		sugar().opportunities.recordView.getEditField("date_entered_date").assertVisible(true);

		// date modified
		sugar().opportunities.recordView.getEditField("date_modified_date").assertVisible(true);

		// cancel record
		sugar().opportunities.recordView.cancel();

		// Verify detail fields
		// name
		sugar().opportunities.recordView.getDetailField("name").assertEquals(oppDefaultData.get("name"), true);

		// account name
		//sugar().opportunities.recordView.getDetailField("relAccountName").assertExists(true);

		// expected close date
		sugar().opportunities.recordView.getDetailField("date_closed").assertExists(true);

		// Likely
		sugar().opportunities.recordView.getDetailField("likelyCase").assertExists(true);

		// best
		sugar().opportunities.recordView.getDetailField("bestCase").assertExists(true);

		// worst
		sugar().opportunities.recordView.getDetailField("worstCase").assertExists(true);

		// next step
		sugar().opportunities.recordView.getDetailField("nextStep").assertExists(true);

		// type
		sugar().opportunities.recordView.getDetailField("type").assertExists(true);

		// lead source
		sugar().opportunities.recordView.getDetailField("leadSource").assertExists(true);

		// campaign
		//sugar().opportunities.recordView.getDetailField("relCampaign").assertExists(true);

		// assigned To
		sugar().opportunities.recordView.getDetailField("relAssignedTo").assertEquals("Administrator", true);

		// teams
		sugar().opportunities.recordView.getDetailField("relTeam").assertExists(true);

		// description
		sugar().opportunities.recordView.getDetailField("description").assertEquals(oppDefaultData.get("description"), true);

		// date created
		sugar().opportunities.recordView.getDetailField("date_entered_date").assertExists(true);

		// date modified
		sugar().opportunities.recordView.getDetailField("date_modified_date").assertExists(true);

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	public void cleanup() throws Exception {}
}