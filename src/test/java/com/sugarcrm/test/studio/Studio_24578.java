package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_24578 extends SugarTest {
	VoodooControl accountsSubPanelCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl, fieldCtrl, studioFooterCtrl;
	DataSource customData;
	AccountRecord myAccountRecord;
	
	public void setup() throws Exception {
		customData = testData.get(testName);
		sugar().login();
		myAccountRecord = (AccountRecord)sugar().accounts.api.create();
	}

	/**
	 * Function log in calculated field
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_24578_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-938
		// studio
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		accountsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsSubPanelCtrl.click();
		VoodooUtils.waitForReady();

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

		// Adding multiple custom fields, formula add on result field (i.e log(number($mynumber_c),number($mybase_c)))
		for( int i = 0; i < customData.size(); i++ ) { 
			fieldCtrl.click();
			VoodooUtils.waitForReady();
			addFieldCtrl.click();
			VoodooUtils.waitForReady();
			fieldNameCtrl.set(customData.get(i).get("module_field_name"));
			if( i == 2 ){
				calculatedCtrl.click();
				editFormulaCtrl.click();
				formulaInputCtrl.set(customData.get(i).get("formula"));
				formulaSaveCtrl.click();
				VoodooUtils.waitForReady(30000);
			}
			saveFieldCtrl.click();
			VoodooUtils.waitForReady(30000);
			// multiple fields not working on JENKINS, that's why below code add
			studioFooterCtrl.click();
			VoodooUtils.waitForReady();
			accountsSubPanelCtrl.click();
			VoodooUtils.waitForReady();
		}
		// layout subpanel
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		// Record view
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		for(int i=0; i< customData.size(); i++){
			new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
			VoodooUtils.waitForReady();
			moveToLayoutPanelCtrl.waitForVisible();
			String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get(i).get("module_field_name")); 
			new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
			VoodooUtils.waitForReady();
		}
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		
		myAccountRecord.navToRecord();
		sugar().accounts.recordView.edit();
		
		// Enter any numbers in "mybase", "mynumber" fields, e.g. 10, 100
		new VoodooControl("input", "css", "[data-voodoo-name='mybase_c'] [name = 'mybase_c']").set(customData.get(0).get("field_data"));
		new VoodooControl("input", "css", "[data-voodoo-name='mynumber_c'] [name = 'mynumber_c']").set(customData.get(1).get("field_data"));
		
		sugar().accounts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		//Verify that calculated field should show the value as 2
		new VoodooControl("span", "css", "[data-voodoo-name='mylog_c']").assertContains(customData.get(2).get("field_data"), true);

		myAccountRecord.navToRecord();
		sugar().accounts.recordView.edit();
		
		// Enter any letter in "mybase", "mynumber" fields, e.g. a, b
		new VoodooControl("input", "css", "[data-voodoo-name='mybase_c'] [name = 'mybase_c']").set(customData.get(0).get("field_invalid_value"));
		new VoodooControl("input", "css", "[data-voodoo-name='mynumber_c'] [name = 'mynumber_c']").set(customData.get(1).get("field_invalid_value"));
		sugar().accounts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		//Verify that calculated field is empty. NAN value can not be assert due to TODO: TR-4842 
		new VoodooControl("span", "css", "[data-voodoo-name='mylog_c']").assertContains(customData.get(2).get("field_invalid_value"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}