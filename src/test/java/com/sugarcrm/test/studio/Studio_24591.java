package com.sugarcrm.test.studio;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Studio_24591 extends SugarTest {
	VoodooControl fieldsBtn,layoutSubPanelCtrl,defaultSubPanelCtrl,recordViewSubPanelCtrl,studioFooterCtrl,
						moveToLayoutPanelCtrl,moveToNewFilter,contactsSubPanelCtrl,listViewSubPanelCtrl,
						resetButtonCtrl,resetClickCtrl,relationshipsCtrl,fieldsCtrl,labelsCtrl,layoutsCtrl,
						extensionsCtrl,contactsButtonCtrl ;
	FieldSet myData, oppRecord;
	DataSource RLIrecord;
	OpportunityRecord myOpportunity1, myOpportunity2;
	RevLineItemRecord RLIrecord1, RLIrecord2;
	ContactRecord myContact;
	StandardSubpanel opportunitySubpanel;
	ArrayList<Record> myOpportunities;
	
	public void setup() throws Exception {
		sugar().login();
		sugar().accounts.api.create();
		
		contactsButtonCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		resetButtonCtrl = new VoodooControl("input", "id", "exportBtn");
		resetClickCtrl = new VoodooControl("button", "id", "execute_repair");		
		relationshipsCtrl = new VoodooControl("input", "name", "relationships");
		fieldsCtrl = new VoodooControl("input", "name", "fields");
		layoutsCtrl = new VoodooControl("input", "name", "layouts");
		labelsCtrl = new VoodooControl("input", "name", "labels");
		extensionsCtrl = new VoodooControl("input", "name", "extensions");

	}

	/**
	 * Auto updating calculated field that contain rollupMax function when related record changes.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_24591_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		myData = testData.get(testName).get(0);
		oppRecord = testData.get(testName+"_oppRecord").get(0);
		RLIrecord = testData.get(testName+"_RLIrecord");
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		contactsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		contactsSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		fieldsBtn = new VoodooControl("td", "id", "fieldsBtn");
		fieldsBtn.click();
		VoodooUtils.waitForReady();

		// Create a custom calculated field in contacts module
		// TODO: VOOD-938
		new VoodooControl("input", "css", "input[value='Add Field']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(myData.get("field_name"));
		new VoodooControl("input", "id", "calculated").click();
		new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
		new VoodooControl("a", "css", ".rollup.button a").click();
		new VoodooControl("option", "css", "select#rollwiz_type option[value='Max']").click();
		new VoodooControl("option", "css", "select#rollwiz_rmodule option[value='opportunities']").click();
		new VoodooControl("button", "css", "[name='selrf_insertbtn']").click();
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		studioFooterCtrl.click();
		VoodooUtils.waitForReady();
		
		contactsSubPanelCtrl.click();
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();
		
		// Add custom field to List view
		// TODO: VOOD-938
		listViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		listViewSubPanelCtrl.click();
		defaultSubPanelCtrl = new VoodooControl("td", "id", "Default");
		defaultSubPanelCtrl.waitForVisible();
		String dataNameDraggableLi = String.format("li[data-name=%s]",myData.get("display_name")); 
		new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultSubPanelCtrl);
		VoodooControl saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "div.bodywrapper div a:nth-of-type(5)").click();
		
		// Add custom field to Record view
		// TODO: VOOD-938
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row");
		moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		recordViewSubPanelCtrl.click();
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]",myData.get("display_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Create two opportunities records
		myOpportunity1 = (OpportunityRecord) sugar().opportunities.api.create();
		myOpportunity2 = (OpportunityRecord) sugar().opportunities.api.create(oppRecord);
		
		myOpportunities = new ArrayList<Record>();
		myOpportunities.add(myOpportunity1);
		myOpportunities.add(myOpportunity2);
		
		// Create two RLI records related to opportunities.
		RLIrecord1 = (RevLineItemRecord) sugar().revLineItems.api.create(RLIrecord.get(0));
		RLIrecord2 = (RevLineItemRecord) sugar().revLineItems.api.create(RLIrecord.get(1));
		
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.getEditField("relOpportunityName").set(RLIrecord.get(0).get("relOpportunityName"));
		sugar().revLineItems.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(2);
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.getEditField("relOpportunityName").set(RLIrecord.get(1).get("relOpportunityName"));
		sugar().revLineItems.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		// Create a contact record and select related opportunities for it.
		myContact = (ContactRecord) sugar().contacts.api.create();
		myContact.navToRecord();
		opportunitySubpanel = sugar().contacts.recordView.subpanels.get("Opportunities");
		opportunitySubpanel.linkExistingRecords(myOpportunities);
		
		// Verify the max value of related opportunities' amount will show in the calculated field 
		sugar().contacts.navToListView();
		new VoodooControl("div", "css", ".fld_"+myData.get("display_name")+" div").assertContains(myData.get("value"), true);
		
		// Edit record, modify the amount field and save.
		RLIrecord1.navToRecord();
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.getEditField("likelyCase").set(myData.get("new_value"));
		sugar().revLineItems.recordView.save();
		VoodooUtils.waitForAlertExpiration();
		
		// Verify the calculated field value auto updated to the new value
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		
		// Need to go Edit view and then save (TR-5783)
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.save();
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("div", "css", ".fld_"+myData.get("display_name")+" div").assertContains(myData.get("new_value"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}