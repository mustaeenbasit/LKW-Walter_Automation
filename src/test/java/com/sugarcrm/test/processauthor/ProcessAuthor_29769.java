package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29769 extends SugarTest {
	VoodooControl accountsCtrl;
	DataSource ds = new DataSource();
	
	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);
		
		// TODO: VOOD-542
		accountsCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		VoodooControl fieldCreateCtrl = new VoodooControl("input", "css", "#studiofields input[value='Add Field']");
		VoodooControl fieldTypeCtrl= new VoodooControl("select", "id", "type");
		VoodooControl fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl fieldSaveBtnCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		VoodooControl layoutsButtonCtrl = new VoodooControl("td", "id", "layoutsBtn");
		VoodooControl recordViewButtonCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		VoodooControl fieldsBtn = new VoodooControl("td", "id", "fieldsBtn"); 
		
		// Studio -> Accounts -> Fields
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		accountsCtrl.click();
		VoodooUtils.waitForReady();
		fieldsBtn.click();
		VoodooUtils.waitForReady();
		
		// Create 2 custom fields (Image & Phone) for Accounts module 
		for(int i = 0 ; i < 2 ;i++){
			fieldCreateCtrl.click();
			VoodooUtils.waitForReady();
			fieldTypeCtrl.click();
			VoodooUtils.waitForReady();
			switch(i){
			case 0:
				fieldTypeCtrl.set("Image");
				VoodooUtils.waitForReady();
				break;
			case 1:
				fieldTypeCtrl.set("Phone");
				VoodooUtils.waitForReady();
				break;
			}
			fieldNameCtrl.set(ds.get(i).get("fieldName"));
			VoodooUtils.waitForReady();
			fieldSaveBtnCtrl.click();
			VoodooUtils.waitForReady(30000);
		}
		
		// Add fields to Record view
		// TODO: VOOD-542
		sugar().admin.studio.clickStudio();
		
		accountsCtrl.click();
		VoodooUtils.waitForReady();
		
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		recordViewButtonCtrl.click();	
		VoodooUtils.waitForReady();
		
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		for (int i = 0; i < 2; i++) {		
			new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
			String dataNameDraggableFieldToRecordSubpanel;
			dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]", ds.get(i).get("fieldName") +"_c"); 
			new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);			
		}
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady(30000);
		
		// Add fields to Listview
		sugar().admin.studio.clickStudio();

		accountsCtrl.click();
		VoodooUtils.waitForReady();

		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();
		
		VoodooControl dropHere = new VoodooControl("td", "css", "#Default");
		for(int i=0; i < 2; i++){
			new VoodooControl("li", "css", ".draggable[data-name='"+ds.get(i).get("fieldName")+"_c']").dragNDrop(dropHere);
			VoodooUtils.waitForReady();
		}
		VoodooControl saveBtnCtrl=new VoodooControl("input", "css" ,"input[name='savebtn']");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that Custom Image field should not be removed after click on Approve in My Processes
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29769_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Import Process Definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");

		// Navigate to process Definition list view and open the action drop down of the process definition record
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);

		// Enable Process Definition
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();
		
		// Navigate to Accounts module and Create one Accounts so that above created process is triggered
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(sugar().accounts.getDefaultData().get("name"));
		// TODO: VOOD-1036
		new VoodooControl("input", "css", ".fld_phonefield_c.edit input").set(ds.get(0).get("phoneFieldValue"));
		sugar().accounts.createDrawer.save();
		
		// Navigate to My Processes	
		sugar().processes.navToListView();
		// TODO: VOOD-1706
		// Click on Show Process through Action drop-down
		sugar().processes.listView.editRecord(1);
		VoodooUtils.waitForReady();
		
		// Click on Approve button
		new VoodooControl("a", "css", ".detail.fld_approve_button a").click();
		sugar().alerts.confirmAllWarning();
		
		// Verify that Custom Image field not removed
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		// TODO: VOOD-1036
		new VoodooControl("div", "css", ".record-label[data-name='imagefield_c']").assertVisible(true);
		
		// Verify that process is completed
		// TODO: VOOD-1706
		sugar().processes.navToListView();
		sugar().navbar.selectMenuItem(sugar().processes, "processManagement");
		new VoodooControl("div", "css", ".label-success").assertContains(ds.get(0).get("completed"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
