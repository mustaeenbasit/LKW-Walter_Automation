package com.sugarcrm.test.studio;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_24526 extends SugarTest {
	VoodooControl fieldCreateCtrl;
	VoodooControl fieldTypeCtrl;
	VoodooControl fieldNameCtrl;
	VoodooControl fieldSaveBtnCtrl, accountSubPanelCtrl, fieldsBtn, studioFooterCtrl;
	VoodooControl layoutsButtonCtrl,recordViewButtonCtrl, integerTypeCtlr, floatTypeCtrl, decimalTypeCtrl, currencyTypeCtrl;
	DataSource ds, fieldValueDs;

	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);
		fieldValueDs = testData.get(testName + "_field_values");
	}

	/**
	 * Check calculation with average function in the formula of calculated field
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_24526_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-938
		// click on studio link  
		// click on Studio > Accounts > Fields   
		accountSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		fieldsBtn = new VoodooControl("td", "id", "fieldsBtn"); 
		studioFooterCtrl =new VoodooControl("input", "css", "#footerHTML input[value='Studio']");

		// new voodooControls
		fieldCreateCtrl = new VoodooControl("input", "css", "#studiofields input[value='Add Field']");
		fieldTypeCtrl= new VoodooControl("select", "id", "type");
		fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		fieldSaveBtnCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		layoutsButtonCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewButtonCtrl = new VoodooControl("td", "id", "viewBtnrecordview");

		// create custom fields in accounts module  
		for(int i =0 ; i < 5 ;i++){
			fieldsBtn.click();
			VoodooUtils.waitForReady();
			fieldCreateCtrl.click();
			VoodooUtils.waitForReady();
			fieldTypeCtrl.click();
			VoodooUtils.waitForReady();
			switch(i){
			case 0:
				fieldTypeCtrl.set("Integer");
				VoodooUtils.waitForReady();
				break;
			case 1:
				fieldTypeCtrl.set("Float");
				VoodooUtils.waitForReady();
				break;
			case 2:
				fieldTypeCtrl.set("Decimal");
				VoodooUtils.waitForReady();
				break;
			case 3:	
				fieldTypeCtrl.set("Currency");
				VoodooUtils.waitForReady();
				break;
			case 4:
				fieldTypeCtrl.set("Integer");
				VoodooUtils.waitForReady();
				new VoodooControl("input", "id", "calculated").set(Boolean.toString(true));
				new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
				new VoodooControl("textarea", "css", "#formulaInput").set(ds.get(0).get("formula"));
				new VoodooControl("input", "id", "fomulaSaveButton").click();
				break;
			}
			fieldNameCtrl.set(ds.get(0).get("fieldName") + i);
			fieldSaveBtnCtrl.click();
			sugar().admin.studio.waitForAJAX(30000); // Need extra time to save the fields
			// multiple fields not working on JENKINS, that's why below code add
			studioFooterCtrl.click();
			VoodooUtils.waitForReady();
			accountSubPanelCtrl.click();
			VoodooUtils.waitForReady();
		}

		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		recordViewButtonCtrl.click();	
		VoodooUtils.waitForReady();
		
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		
		for (int i = 0; i < 5; i++) {		
			new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
			VoodooUtils.waitForReady();
			String dataNameDraggableFieldToRecordSubpanel;
			dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]","number"+ i +"_c"); 
			new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);			
			VoodooUtils.waitForReady();
		}
		
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();

		// navigate to accounts module
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(ds.get(0).get("accountName"));

		// Define controls for created custome fields
		// TODO: VOOD-1036
		VoodooControl zeroNumberFieldCtrl = new VoodooControl("input", "css", "[name = 'number0_c']");
		VoodooControl oneNumberFieldCtrl = new VoodooControl("input", "css", "[name = 'number1_c']");
		VoodooControl twoNumberFieldCtrl = new VoodooControl("input", "css", "[name = 'number2_c']");
		VoodooControl threeNumberFieldCtrl = new VoodooControl("input", "css", "[name = 'number3_c']");
		VoodooControl fourNumberFieldCtrl = new VoodooControl("input", "css", "[name = 'number4_c']");
		VoodooControl detailFourNumberFieldCtrl = new VoodooControl("span", "css", "[data-voodoo-name = 'number4_c']");

		// Input valid data in the formula fields to calculate average 
		zeroNumberFieldCtrl.set(fieldValueDs.get(0).get("val0"));
		oneNumberFieldCtrl.set(fieldValueDs.get(0).get("val1"));
		twoNumberFieldCtrl.set(fieldValueDs.get(0).get("val2"));
		threeNumberFieldCtrl.set(fieldValueDs.get(0).get("val3"));
		fourNumberFieldCtrl.click();
		// assert the average field value
		fourNumberFieldCtrl.assertEquals(fieldValueDs.get(0).get("val4"), true);

		// Save the record and navigate to the created Account record
		sugar().accounts.createDrawer.save();
		sugar().accounts.listView.clickRecord(1);

		// assert the average field value on RecordView 
		detailFourNumberFieldCtrl.assertEquals(fieldValueDs.get(0).get("val4"), true);

		sugar().accounts.recordView.edit();

		// Input invalid data in the formula fields to calculate average 
		zeroNumberFieldCtrl.set(fieldValueDs.get(1).get("val0"));
		oneNumberFieldCtrl.set(fieldValueDs.get(1).get("val1"));
		twoNumberFieldCtrl.set(fieldValueDs.get(1).get("val2"));
		threeNumberFieldCtrl.set(fieldValueDs.get(1).get("val3"));
		fourNumberFieldCtrl.click();
		// assert the average field value in case of invalid data. In this case, the value does not change. 
		fourNumberFieldCtrl.assertEquals(fieldValueDs.get(0).get("val4"), true);

		// Input valid data in the formula fields to calculate average 
		zeroNumberFieldCtrl.set(fieldValueDs.get(2).get("val0"));
		oneNumberFieldCtrl.set(fieldValueDs.get(2).get("val1"));
		twoNumberFieldCtrl.set(fieldValueDs.get(2).get("val2"));
		threeNumberFieldCtrl.set(fieldValueDs.get(2).get("val3"));
		fourNumberFieldCtrl.click();
		// assert the average field value
		assertTrue("Calculated field value wrong. " + fourNumberFieldCtrl.getText(), 
				fourNumberFieldCtrl.queryAttributeContains("value", fieldValueDs.get(2).get("val4")) || 
				fourNumberFieldCtrl.queryAttributeContains("value", fieldValueDs.get(2).get("val5")));
		sugar().accounts.recordView.save();

		// assert the average field value RecordView
		assertTrue("Calculated field value wrong. " + detailFourNumberFieldCtrl.getText(), 
				detailFourNumberFieldCtrl.queryContains(fieldValueDs.get(2).get("val4"), true) || 
				detailFourNumberFieldCtrl.queryContains(fieldValueDs.get(2).get("val5"), true));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}