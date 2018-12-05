package com.sugarcrm.test.grimoire;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class OpportunityModuleTestsNoRli extends SugarTest {
	AccountRecord myAccount;
	
	public void setup() throws Exception {
		myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().login();
	}

	@Ignore("VOOD-1402")
	@Test
	public void createOppNoRli() throws Exception {
		VoodooUtils.voodoo.log.info("Running createOppNoRli()...");

		OpportunityRecord myOpp = (OpportunityRecord)sugar().opportunities.create();
		myOpp.verify();

		VoodooUtils.voodoo.log.info("createOppNoRli() complete.");
	}

	@Test
	public void verifyOppFilter() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyOppFilter()...");

		FieldSet oppRecord = sugar().opportunities.getDefaultData();
		FieldSet secodOppData = new FieldSet();
		secodOppData.put("name", "Second Opp");
		sugar().opportunities.api.create(secodOppData);
		sugar().opportunities.api.create();
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.editRecord(1);
		sugar().opportunities.listView.getEditField(1, "relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.listView.saveRecord(1);
		sugar().opportunities.listView.openFilterDropdown();
		sugar().opportunities.listView.selectFilterCreateNew();
		sugar().opportunities.listView.filterCreate.setFilterFields("name", "Opportunity Name", "starts with", secodOppData.get("name"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().opportunities.listView.verifyField(1, "name", secodOppData.get("name"));

		sugar().opportunities.listView.filterCreate.setFilterFields("relAccountName", "Account Name", "is any of", oppRecord.get("relAccountName"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().opportunities.listView.verifyField(1, "relAccountName", oppRecord.get("relAccountName"));

		sugar().opportunities.listView.filterCreate.setFilterFields("likelyCase", "Likely", "is equal to", oppRecord.get("likelyCase"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().opportunities.listView.assertIsEmpty();

		sugar().opportunities.listView.filterCreate.setFilterFields("bestCase", "Best", "is equal to", "1000", 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().opportunities.listView.assertIsEmpty();

		sugar().opportunities.listView.filterCreate.setFilterFields("worstCase", "Worst", "is equal to","1000", 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().opportunities.listView.assertIsEmpty();

		sugar().opportunities.listView.filterCreate.setFilterFields("nextStep", "Next Step", "exactly matches", oppRecord.get("nextStep"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().opportunities.listView.verifyField(1, "nextStep", oppRecord.get("nextStep"));

		sugar().opportunities.listView.filterCreate.setFilterFields("leadSource", "Lead Source", "is any of", oppRecord.get("leadSource"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().opportunities.listView.verifyField(1, "leadSource", oppRecord.get("leadSource"));

		sugar().opportunities.listView.filterCreate.setFilterFields("type", "Type", "is any of", oppRecord.get("type"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().opportunities.listView.assertIsEmpty();

		sugar().opportunities.listView.filterCreate.setFilterFields("date_entered_date", "Date Created", "is equal to", "07/24/2015", 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().opportunities.listView.assertIsEmpty();

		sugar().opportunities.listView.filterCreate.setFilterFields("date_modified_date", "Date Modified", "is equal to", "07/24/2015", 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().opportunities.listView.assertIsEmpty();

		sugar().opportunities.listView.filterCreate.setFilterFields("date_closed", "Expected Close Date", "is equal to", oppRecord.get("date_closed"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().opportunities.listView.verifyField(1, "date_closed", oppRecord.get("date_closed"));

		sugar().opportunities.listView.filterCreate.setFilterFields("relAssignedTo", "Assigned to", "is any of", "Administrator", 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().opportunities.listView.verifyField(1, "relAssignedTo", "Administrator");

		VoodooUtils.voodoo.log.info("verifyOppFilter() completed.");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		// field set data to create a opportunity record
		FieldSet oppDefaultData = new FieldSet();
		oppDefaultData = sugar().opportunities.getDefaultData();
		oppDefaultData.put("bestCase", "1000");
		oppDefaultData.put("worstCase", "1000");
		oppDefaultData.put("likelyCase", "1000");
		oppDefaultData.put("salesStage", "Prospecting");

		sugar().opportunities.api.create();
		sugar().opportunities.navToListView();

		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.edit();
		sugar().opportunities.recordView.showMore();

		// verify edit record fields 
		// name
		sugar().opportunities.recordView.getEditField("name").assertVisible(true);
		// account name
		sugar().opportunities.recordView.getEditField("relAccountName").assertVisible(true);
		// Best Case
		sugar().opportunities.recordView.getEditField("bestCase").assertVisible(true);
		// Worst Case
		sugar().opportunities.recordView.getEditField("worstCase").assertVisible(true);
		// Likely Case
		sugar().opportunities.recordView.getEditField("likelyCase").assertVisible(true);
		// Date closed
		sugar().opportunities.recordView.getEditField("date_closed").assertVisible(true);
		// Tags
		sugar().opportunities.recordView.getEditField("tags").assertVisible(true);
		// Probability
		sugar().opportunities.recordView.getEditField("probability").assertVisible(true);
		// Sales Stage
		sugar().opportunities.recordView.getEditField("salesStage").assertVisible(true);
		// Next Step
		sugar().opportunities.recordView.getEditField("nextStep").assertVisible(true);
		// Type
		sugar().opportunities.recordView.getEditField("type").assertVisible(true);
		// Lead Source
		sugar().opportunities.recordView.getEditField("leadSource").assertVisible(true);
		// Campaign
		sugar().opportunities.recordView.getEditField("relCampaign").assertVisible(true);
		// Assigned To
		sugar().opportunities.recordView.getEditField("relAssignedTo").assertVisible(true);
		// Description
		sugar().opportunities.recordView.getEditField("description").assertVisible(true);
		// Teams
		sugar().opportunities.recordView.getEditField("relTeam").assertVisible(true);

		// put data to all edit fields
		// name
		sugar().opportunities.recordView.getEditField("name").set(oppDefaultData.get("name"));
		// Account name
		sugar().opportunities.recordView.getEditField("relAccountName").set(oppDefaultData.get("relAccountName"));
		// Date Close
		sugar().opportunities.recordView.getEditField("date_closed").set(oppDefaultData.get("date_closed"));
		// Best Case
		sugar().opportunities.recordView.getEditField("bestCase").set(oppDefaultData.get("bestCase"));
		// Worst Case
		sugar().opportunities.recordView.getEditField("worstCase").set(oppDefaultData.get("worstCase"));
		// Likely Case
		sugar().opportunities.recordView.getEditField("likelyCase").set(oppDefaultData.get("likelyCase"));
		// Sales Stage
		sugar().opportunities.recordView.getEditField("salesStage").set(oppDefaultData.get("salesStage"));
		// Next Step
		sugar().opportunities.recordView.getEditField("nextStep").set(oppDefaultData.get("nextStep"));
		// Type
		sugar().opportunities.recordView.getEditField("type").set(oppDefaultData.get("type"));
		// Lead Source
		sugar().opportunities.recordView.getEditField("leadSource").set(oppDefaultData.get("leadSource"));
		// Assigned To 
		sugar().opportunities.recordView.getEditField("relAssignedTo").set("Administrator");
		// Teams
		sugar().opportunities.recordView.getEditField("relTeam").set("qauser");
		// Description
		sugar().opportunities.recordView.getEditField("description").set(oppDefaultData.get("description"));

		// save the record
		sugar().opportunities.recordView.save();

		// verify all detail view fields
		// Name
		sugar().opportunities.recordView.getDetailField("name").assertEquals(oppDefaultData.get("name"), true);
		// Account Name
		sugar().opportunities.recordView.getDetailField("relAccountName").assertEquals(oppDefaultData.get("relAccountName"), true);
		// Date Close
		sugar().opportunities.recordView.getDetailField("date_closed").assertContains(oppDefaultData.get("date_closed"), true);
		// Best Case
		sugar().opportunities.recordView.getDetailField("bestCase").assertEquals("$1,000.00", true);
		// Worst Case
		sugar().opportunities.recordView.getDetailField("worstCase").assertEquals("$1,000.00", true);
		// Likely Case
		sugar().opportunities.recordView.getDetailField("likelyCase").assertEquals("$1,000.00", true);
		// Probability
		sugar().opportunities.recordView.getDetailField("probability").assertEquals("10", true);
		// Sales Stage
		sugar().opportunities.recordView.getDetailField("salesStage").assertEquals("Prospecting", true);
		// Next Step
		sugar().opportunities.recordView.getDetailField("nextStep").assertEquals(oppDefaultData.get("nextStep"), true);
		// Type
		sugar().opportunities.recordView.getDetailField("type").assertEquals(oppDefaultData.get("type"), true);
		// Lead Source
		sugar().opportunities.recordView.getDetailField("leadSource").assertEquals(oppDefaultData.get("leadSource"), true);
		// Assigned to
		sugar().opportunities.recordView.getDetailField("relAssignedTo").assertEquals("Administrator",true);
		// Teams
		sugar().opportunities.recordView.getDetailField("relTeam").assertContains("qauser",true);
		// Description
		sugar().opportunities.recordView.getDetailField("description").assertEquals(oppDefaultData.get("description"),true);

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	@Test
	public void verifyListHeaders() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListHeaders()...");

		sugar().opportunities.navToListView();
		// Verify all headers in listview
		for(String header : sugar().opportunities.listView.getHeaders()){
			// in only opportunity view no "Status" column in listview
			// commit_stage available if "forecasts" enabled 
			if(!(header.equals("commit_stage") || header.equals("sales_status"))){
				sugar().opportunities.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
			}
		}
		VoodooUtils.voodoo.log.info("verifyListHeaders() test complete.");
	}
	
	@Test
	public void verifyPreviewPaneFields() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyPreviewPaneFields()...");

		FieldSet oppDefaultData = new FieldSet();
		oppDefaultData = sugar().opportunities.getDefaultData();
		oppDefaultData.put("bestCase", "1000");
		oppDefaultData.put("worstCase", "1000");
		oppDefaultData.put("likelyCase", "1000");
		oppDefaultData.put("salesStage", "Prospecting");

		sugar().opportunities.api.create();
		sugar().opportunities.navToListView();
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
		sugar().previewPane.getPreviewPaneField("probability").assertExists(true);

		// Sales Stage
		sugar().previewPane.getPreviewPaneField("salesStage").assertExists(true);

		// Next Step
		sugar().previewPane.getPreviewPaneField("nextStep").assertExists(true);

		// type
		sugar().previewPane.getPreviewPaneField("type").assertExists(true);

		// Lead Source
		sugar().previewPane.getPreviewPaneField("leadSource").assertExists(true);

		// Assigned to
		sugar().previewPane.getPreviewPaneField("relAssignedTo").assertExists(true);

		// Teams
		sugar().previewPane.getPreviewPaneField("relTeam").assertExists(true);

		// date modified
		sugar().previewPane.getPreviewPaneField("date_modified_date").assertVisible(true);

		// date created
		sugar().previewPane.getPreviewPaneField("date_entered_date").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyPreviewPaneFields() complete.");
	}

	@Test
	public void verifyInlineRecordFieldsInListView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInlineRecordFieldsInListView()...");

		FieldSet oppDefaultData = new FieldSet();
		oppDefaultData = sugar().opportunities.getDefaultData();
		oppDefaultData.put("bestCase", "1000");
		oppDefaultData.put("worstCase", "1000");
		oppDefaultData.put("likelyCase", "1000");
		oppDefaultData.put("salesStage", "Prospecting");

		sugar().opportunities.api.create(oppDefaultData);
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.editRecord(1);

		// Verify inline edit fields
		// name
		sugar().opportunities.listView.getEditField(1, "name").assertEquals(oppDefaultData.get("name"), true);

		// account name
		VoodooSelect accountName = (VoodooSelect)sugar().opportunities.listView.getEditField(1, "relAccountName");
		accountName.set(oppDefaultData.get("relAccountName"));
		accountName.assertEquals(oppDefaultData.get("relAccountName"), true);

		// sales stage 
		sugar().opportunities.listView.getEditField(1, "salesStage").set(oppDefaultData.get("salesStage"));

		// likelyCase 
		sugar().opportunities.listView.getEditField(1, "likelyCase").set("10000");

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

		// date closed 
		sugar().opportunities.listView.getEditField(1, "date_closed").assertVisible(true);

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
		sugar().opportunities.listView.getDetailField(1, "relAccountName").assertEquals(oppDefaultData.get("relAccountName"), true);

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

	public void cleanup() throws Exception {}
}
