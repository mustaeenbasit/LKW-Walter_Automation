package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_24570 extends SugarTest {
	VoodooControl accountsSubPanelCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl, fieldCtrl, studioFooterCtrl;
	DataSource customData, fieldDataSet;
	AccountRecord myAccountRecord;
	
	public void setup() throws Exception {
		customData = testData.get(testName);
		fieldDataSet = testData.get(testName + "_data_set");
		sugar().login();
		myAccountRecord = (AccountRecord)sugar().accounts.api.create();
	}

	/**
	 * Function max in calculated field
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_24570_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-938
		// studio
		sugar().admin.adminTools.getControl("studio").click();
		accountsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsSubPanelCtrl.click();
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]");
		VoodooControl saveFieldCtrl  = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		VoodooControl fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl calculatedCtrl = new VoodooControl("input", "id", "calculated");
		VoodooControl editFormulaCtrl = new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']");
		VoodooControl formulaInputCtrl = new VoodooControl("textarea", "css", "#formulaInput");
		VoodooControl formulaSaveCtrl = new VoodooControl("input", "id", "fomulaSaveButton");
		// TODO: VOOD-999
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");

		// Adding multiple custom fields, formula add on result field (i.e max(number($mynum1_c), number($mynum2_c), number($mynum3_c)))
		for( int i = 0; i < customData.size(); i++ ) { 
			fieldCtrl.click();
			addFieldCtrl.click();
			VoodooUtils.waitForReady();
			fieldNameCtrl.set(customData.get(i).get("module_field_name"));
			if( i == 3 ){
				calculatedCtrl.click();
				editFormulaCtrl.click();
				formulaInputCtrl.set(customData.get(i).get("formula"));
				formulaSaveCtrl.click();
				VoodooUtils.waitForReady();
			}
			saveFieldCtrl.click();
			VoodooUtils.waitForReady();
			VoodooUtils.pause(1000); // needed for consistent runs on fast connection machines 
			// multiple fields not working on JENKINS, that's why below code add
			studioFooterCtrl.click();
			accountsSubPanelCtrl.click();
			VoodooUtils.waitForReady();
		}

		// layout subpanel
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();

		// Record view
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click(); 
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		for(int i=0; i< customData.size(); i++){
			new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
			moveToLayoutPanelCtrl.waitForVisible();
			String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get(i).get("module_field_name")); 
			new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		}
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		myAccountRecord.navToRecord();
		// Enter any numbers in "MyNum1", "MyNum2", and "MyNum3" fields, e.g., 1, 2, 3
		for (int i = 0; i < fieldDataSet.size(); i++) {
			sugar().accounts.recordView.edit();
			new VoodooControl("input", "css", "[data-voodoo-name='mynum1_c'] [name = 'mynum1_c']").set(fieldDataSet.get(i).get("num1"));
			new VoodooControl("input", "css", "[data-voodoo-name='mynum2_c'] [name = 'mynum2_c']").set(fieldDataSet.get(i).get("num2"));
			new VoodooControl("input", "css", "[data-voodoo-name='mynum3_c'] [name = 'mynum3_c']").set(fieldDataSet.get(i).get("num3"));
			sugar().accounts.recordView.save();
			sugar().alerts.waitForLoadingExpiration();
			//Verify that Calculated field should show the max value
			new VoodooControl("span", "css", "[data-voodoo-name='maxnum_c']").assertContains(fieldDataSet.get(i).get("avg"), true);
		}	
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}