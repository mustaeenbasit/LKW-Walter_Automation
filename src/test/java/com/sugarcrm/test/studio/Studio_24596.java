package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Studio_24596 extends SugarTest {
	FieldSet customData;
	AccountRecord myAccount;
	ContactRecord contactRecord;
	RevLineItemRecord myRLI1, myRLI2, myRLI3;
	OpportunityRecord myOpportunity1, myOpportunity2, myOpportunity3;
	VoodooControl contactsSubPanelCtrl, fieldCtrl, studioFooterCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl;
	
	public void setup() throws Exception {
		sugar().login();
		
		customData = testData.get(testName).get(0);
		
		// Create a calculated field in Contacts module
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-938
		contactsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		contactsSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));		
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "calculated").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
		new VoodooControl("a", "css", "#markItUpFormulaInput ul li.rollup.button a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "#rollwiz_type").set("Sum");
		new VoodooControl("select", "css", "#rollwiz_rmodule").set("Opportunities (Opportunities)");
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "#rollwiz_rfield").set("Likely");
		VoodooUtils.waitForReady();
		new VoodooControl("button", "css", "button[name='selrf_insertbtn']").click();
		
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady(30000);
		
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady(30000);
		
		// TODO: VOOD-999
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		studioFooterCtrl.click();
		VoodooUtils.waitForReady();
		contactsSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		// Record view
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();	
		VoodooUtils.waitForReady();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get("module_field_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady(30000);

		// List view
		studioFooterCtrl.click();
		VoodooUtils.waitForReady();
		contactsSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#viewBtnlistview tr:nth-child(1) td a").click(); 
		VoodooControl moveHere = new VoodooControl("td", "css", "#Default");
		new VoodooControl("li", "css", ".draggable[data-name='"+customData.get("module_field_name")+"_c").dragNDrop(moveHere);
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		
		myAccount = (AccountRecord)sugar().accounts.api.create();
		contactRecord = (ContactRecord)sugar().contacts.api.create();
		
		FieldSet fs = new FieldSet();
		fs.put("name", customData.get("name1"));
		myOpportunity1 = (OpportunityRecord) sugar().opportunities.api.create(fs);
		sugar().alerts.closeAllError();
		
		fs.clear();
		fs.put("name", customData.get("name2"));
		myOpportunity2 = (OpportunityRecord) sugar().opportunities.api.create(fs);
		sugar().alerts.closeAllError();
		
		fs.clear();
		fs.put("name", customData.get("name3"));
		myOpportunity3 = (OpportunityRecord) sugar().opportunities.api.create(fs);
		sugar().alerts.closeAllError();
		
		fs.clear();
		fs.put("likelyCase", customData.get("likelyCase1"));
		fs.put("name", customData.get("rli_name1"));
		myRLI1 = (RevLineItemRecord) sugar().revLineItems.api.create(fs);
		
		fs.clear();
		fs.put("likelyCase", customData.get("likelyCase2"));
		fs.put("name", customData.get("rli_name2"));
		myRLI2 = (RevLineItemRecord) sugar().revLineItems.api.create(fs);
		
		fs.clear();
		fs.put("likelyCase", customData.get("likelyCase3"));
		fs.put("name", customData.get("rli_name3"));
		myRLI3 = (RevLineItemRecord) sugar().revLineItems.api.create(fs);
	}

	/**
	 * Check the formula consisted of greaterThan function and other functions
	 * @throws Exception
	 */
	@Test
	public void Studio_24596_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Make relation between opportunity and RLI
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.getEditField("relOpportunityName").set(customData.get("name1"));
		sugar().revLineItems.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(2);
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.getEditField("relOpportunityName").set(customData.get("name2"));
		sugar().revLineItems.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(3);
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.getEditField("relOpportunityName").set(customData.get("name3"));
		sugar().revLineItems.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
			
		// Link existing lead to Contact record
		contactRecord.navToRecord();
		
		// Verify "0" value with calculated field 
		new VoodooControl("div", "css", "div[data-name='"+customData.get("module_field_name")+"_c'] span div.ellipsis_inline").assertContains("0", true);
		
		// link opportunity with contacts 
		StandardSubpanel contactsSubpanel = sugar().contacts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);
		contactsSubpanel.linkExistingRecord(myOpportunity1); 
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify "300" value with calculated field 
		new VoodooControl("div", "css", "div[data-name='"+customData.get("module_field_name")+"_c'] span div.ellipsis_inline").assertContains(customData.get("likelyCase3"), true);

		// Unlink the related Opportunity by clicking Unlink action in the contacts subpanel
		contactsSubpanel.unlinkRecord(1);
		
		// Verify "0" value with calculated field 
		new VoodooControl("div", "css", "div[data-name='"+customData.get("module_field_name")+"_c'] span div.ellipsis_inline").assertContains("0", true);
		sugar().alerts.waitForLoadingExpiration();
		
		// link opportunity with contacts 
		contactsSubpanel.linkExistingRecord(myOpportunity2);
		
		// Verify "200" value with calculated field 
		new VoodooControl("div", "css", "div[data-name='"+customData.get("module_field_name")+"_c'] span div.ellipsis_inline").assertContains(customData.get("likelyCase2"), true);
		
		// Unlink the related lead by clicking Unlink action in the leads subpanel
		contactsSubpanel.unlinkRecord(1);
		new VoodooControl("div", "css", "div[data-name='"+customData.get("module_field_name")+"_c'] span div.ellipsis_inline").assertContains("0", true);
		sugar().alerts.waitForLoadingExpiration();
		
		// link opportunity with contacts
		contactsSubpanel.linkExistingRecord(myOpportunity3);
		
		// Verify "100" value with calculated field 
		new VoodooControl("div", "css", "div[data-name='"+customData.get("module_field_name")+"_c'] span div.ellipsis_inline").assertContains(customData.get("likelyCase1"), true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}