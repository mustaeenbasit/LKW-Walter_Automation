package com.sugarcrm.test.workflow;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;

public class WorkFlow_18821 extends SugarTest {
	FieldSet customData;
	VoodooControl opportunitySubPanelCtrl, fieldCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl, wfMgtCtrl, wfDropDownCtrl;
	OpportunityRecord myOpportunity;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.accounts.api.create();
		myOpportunity = (OpportunityRecord)sugar.opportunities.api.create();
		sugar.login();

		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-938
		// studio
		sugar.admin.adminTools.getControl("studio").click();
		opportunitySubPanelCtrl = new VoodooControl("a", "id", "studiolink_Opportunities");
		opportunitySubPanelCtrl.click();
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		fieldCtrl.click();

		// Add field [type=Date] and save
		for(int j=0; j<4; j++){
			new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
			new VoodooControl("select", "css", "#type").set(customData.get("data_type"));
			VoodooUtils.waitForReady();
			String fieldName = String.format("%s%d", customData.get("module_field_name"),j+1);
			new VoodooControl("input", "id", "field_name_id").set(fieldName);
			new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
			VoodooUtils.waitForReady();
		}
		// Studio Footer hack
		sugar.admin.studio.clickStudio();
		opportunitySubPanelCtrl.click();

		// TODO: VOOD-938
		// layout subpanel
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();

		// Record view
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click(); 
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1)"); 
		VoodooControl moveToNewFilter = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		for(int k=0; k<4; k++){
			new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
			moveToLayoutPanelCtrl.waitForVisible();
			String fieldName = String.format("%s%d", customData.get("module_field_name"),k+1);
			String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",fieldName.toLowerCase()); 
			moveToLayoutPanelCtrl.waitForVisible();
			new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		}
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Workflow_Sequence_Condition_With_Custom_Field
	 * @author AJ
	 * @throws Exception
	 */
	@Ignore ("SC-2765")
	@Test
	public void WorkFlow_18821_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1042
		// Workflow
		wfMgtCtrl = new VoodooControl("a", "id", "workflow_management");
		wfMgtCtrl.click();
		for(int i =0; i<4; i++){
			wfDropDownCtrl = new VoodooControl("button", "css", "#header div.module-list ul li.dropdown.active button");
			wfDropDownCtrl.click();
			new VoodooControl("li", "css", "li.active .scroll ul li:nth-of-type(1)").click();
			sugar.alerts.waitForLoadingExpiration();
			VoodooUtils.focusFrame("bwc-frame");

			// Create workflow with target as Opportunity Module
			String wfName = String.format("%s%d", customData.get("workflow_name"),i+1);
			new VoodooControl("input", "id", "name").set(wfName);
			new VoodooControl("option", "css", "select[name=base_module] option:nth-of-type(15)").click();
			new VoodooControl("input", "id", "save_workflow").click();

			// Condition: When a field in the target module changes  
			// Field: Date (custom)
			new VoodooControl("a", "id", "NewWorkFlowTriggerShells").click();
			VoodooUtils.focusWindow(1);
			new VoodooControl("input", "css", "body table tbody tr td table:nth-child(2) tbody tr:nth-child(3) td:nth-child(1) input[type='radio']").click();
			new VoodooControl("a", "css", "#lang_compare_change a").click();
			VoodooUtils.focusWindow(2);
			String dateField = "";
			if(i==0)
				dateField = "#selector option:nth-of-type(10)";
			else if(i==1)
				dateField = "#selector option:nth-of-type(11)";
			else if(i==2)
				dateField = "#selector option:nth-of-type(12)";
			else if(i==3)
				dateField = "#selector option:nth-of-type(13)";

			new VoodooControl("option", "css", dateField).click();
			new VoodooControl("input", "css", "tr:nth-child(1) td:nth-child(4) input:nth-child(1)").click();
			VoodooUtils.focusWindow(0);
			VoodooUtils.focusWindow(1);
			new VoodooControl("input", "id", "save").click();
			VoodooUtils.focusWindow(0);
			VoodooUtils.focusFrame("bwc-frame");

			// Action: Update fields in the target module
			// Lead Source field: Partner, SelfGenerated, Employee, Direct mail
			new VoodooControl("a", "id", "NewWorkFlowActionShells").click();
			VoodooUtils.focusWindow(1);
			new VoodooControl("input", "css", "body table tbody tr td table:nth-child(2) tbody tr:nth-child(1) td:nth-child(1) input[type='radio']").click();
			new VoodooControl("input", "id", "step1_next").click();
			VoodooUtils.focusFrame("selectiframe");
			new VoodooControl("input", "css", "body table tbody tr td table:nth-child(2) tbody tr:nth-child(8) td:nth-child(1) input[type='checkbox']").click();
			VoodooUtils.focusDefault();
			VoodooUtils.focusWindow(1);
			new VoodooControl("a", "css", "table tr:nth-of-type(3) td table tbody tr:nth-child(9) td  a").click();
			VoodooUtils.focusWindow(2);
			String leadSource = "";
			if(i==0)
				leadSource = "#basic_options td:nth-child(2) select option:nth-of-type(10)";
			else if(i==1)
				leadSource = "#basic_options td:nth-child(2) select option:nth-of-type(12)";
			else if(i==2)
				leadSource = "#basic_options td:nth-child(2) select option:nth-of-type(7)";
			else if(i==3)
				leadSource = "#basic_options td:nth-child(2) select option:nth-of-type(5)";

			new VoodooControl("option", "css", leadSource).click();
			new VoodooControl("input", "css", "table tr td table tr:nth-child(5) td input:nth-child(2)").click();
			VoodooUtils.focusWindow(0);
			VoodooUtils.focusWindow(1);
			new VoodooControl("input", "css", "table tr:nth-child(5) td input:nth-child(2)").click();
			VoodooUtils.focusWindow(0);
		}

		// Opportunity
		myOpportunity.navToRecord();
		sugar.opportunities.recordView.edit();
		sugar.opportunities.recordView.getEditField("relAccountName").set(sugar.accounts.getDefaultData().get("name"));
		for(int d=1; d<=4; d++){
			String date = String.format(".fld_%s%d_c.edit div input", customData.get("module_field_name").toLowerCase(),d);
			new VoodooControl("input", "css", date).set(customData.get("date_"+d)); 
		} 
		sugar.opportunities.recordView.save();
		sugar.opportunities.recordView.showMore();

		// Verifying work sequence with WF1->WF2->WF3->WF4 
		// Lead Source - Direct Mail
		sugar.opportunities.recordView.getDetailField("leadSource").assertEquals(customData.get("lead_source_4"), true);

		for (int ws=1; ws<=3; ws++){
			// workflow sequence changing
			sugar.navbar.navToAdminTools();
			VoodooUtils.focusFrame("bwc-frame");
			wfMgtCtrl.waitForVisible();
			wfMgtCtrl.click();
			VoodooUtils.focusDefault();
			wfDropDownCtrl.click();
			new VoodooControl("li", "css", "li.active .scroll ul li:nth-of-type(4)").click();
			VoodooUtils.focusFrame("bwc-frame");
			new VoodooControl("input", "css", "#contentTable td:nth-child(4) input").click();
			if(ws==1){
				// Verifying work sequence with WF1->WF2->WF4->WF3
				new VoodooControl("a", "css", "#contentTable  tr:nth-child(6) td:nth-child(4) a:nth-child(1)").click();
			}else if(ws==2){
				// Verifying work sequence with WF1->WF4->WF3->WF2 
				new VoodooControl("a", "css", "#contentTable tr:nth-child(4) td:nth-child(4) a:nth-of-type(2)").click();
				VoodooUtils.waitForAlertExpiration();
				VoodooUtils.focusFrame("bwc-frame");
				new VoodooControl("a", "css", "#contentTable tr:nth-child(5) td:nth-child(4) a:nth-child(2)").click();
			}else if(ws==3){
				// Verifying work sequence with WF4->WF3->WF2->WF1
				new VoodooControl("a", "css", "#contentTable tr:nth-child(3) td:nth-child(4) a:nth-of-type(2)").click();
				VoodooUtils.waitForAlertExpiration();
				VoodooUtils.focusFrame("bwc-frame");
				new VoodooControl("a", "css", "#contentTable tr:nth-child(4) td:nth-child(4) a:nth-of-type(2)").click();
				VoodooUtils.waitForAlertExpiration();
				VoodooUtils.focusFrame("bwc-frame");
				new VoodooControl("a", "css", "#contentTable tr:nth-child(5) td:nth-child(4) a:nth-of-type(2)").click();
			}
			VoodooUtils.waitForAlertExpiration();
			VoodooUtils.focusDefault();
			myOpportunity.navToRecord();
			sugar.opportunities.recordView.edit();
			if(ws==1){
				String date3 = String.format(".fld_%s3_c.edit div input", customData.get("module_field_name").toLowerCase());
				new VoodooControl("input", "css", date3).set(customData.get("date_4")); 
			}else if(ws==2){
				String date2 = String.format(".fld_%s2_c.edit div input", customData.get("module_field_name").toLowerCase());
				new VoodooControl("input", "css", date2).set(customData.get("date_3")); 
			}else if(ws==3){
				String date1 = String.format(".fld_%s1_c.edit div input", customData.get("module_field_name").toLowerCase());
				new VoodooControl("input", "css", date1).set(customData.get("date_2"));
			}

			sugar.opportunities.recordView.save();
			sugar.opportunities.recordView.showMore();
			if(ws==1)
				// Lead Source - Employee
				sugar.opportunities.recordView.getDetailField("leadSource").assertEquals(customData.get("lead_source_3"), true);
			else if(ws==2)
				// Lead Source - Self Generated
				sugar.opportunities.recordView.getDetailField("leadSource").assertEquals(customData.get("lead_source_2"), true);
			else if(ws==3)
				// Lead Source - Partner
				sugar.opportunities.recordView.getDetailField("leadSource").assertEquals(customData.get("lead_source_1"), true);    
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}