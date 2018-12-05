package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_24509 extends SugarTest {
	VoodooControl quotesButtonCtrl;
	DataSource customFieldData = new DataSource();

	public void setup() throws Exception {
		customFieldData = testData.get(testName);
		sugar().accounts.api.create();
		sugar().login();

		// TODO: VOOD-542
		quotesButtonCtrl = new VoodooControl("a", "id", "studiolink_Quotes");
		VoodooControl fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		VoodooControl moduleFieldName = new VoodooControl("input", "id", "field_name_id");
		VoodooControl fieldSaveBtn = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		VoodooControl addFieldBtn = new VoodooControl("input", "css", "#studiofields > input:nth-child(1)");
		VoodooControl fieldDataType = new VoodooControl("select", "css", "#type");
		VoodooControl calculateBtnCtrl = new VoodooControl("input", "id", "calculated");
		VoodooControl editFormulaBtnControl = new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']");
		VoodooControl formulaInputControl = new VoodooControl("textarea", "css", "#formulaInput");
		VoodooControl formulaSaveControl = new VoodooControl("input", "id", "fomulaSaveButton");
		VoodooControl layoutBtnCtrl = new VoodooControl("td", "id", "layoutsBtn");

		// Navigate to Admin > Studio > Quotes > Fields	
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		quotesButtonCtrl.click(); 
		VoodooUtils.waitForReady();

		// Create custom calculated fields
		for (int i = 0 ; i < customFieldData.size() ; i++) {
			fieldCtrl.click();
			addFieldBtn.click();
			fieldDataType.set(customFieldData.get(i).get("type"));
			VoodooUtils.waitForReady();
			moduleFieldName.set(customFieldData.get(i).get("fieldName"));
			calculateBtnCtrl.click();
			editFormulaBtnControl.click();
			// Set formula
			formulaInputControl.set(customFieldData.get(0).get("formula"));
			formulaSaveControl.click();
			VoodooUtils.waitForReady();
			fieldSaveBtn.click();
			VoodooUtils.waitForReady();
			sugar().admin.studio.clickStudio();
			quotesButtonCtrl.click();
			VoodooUtils.waitForReady();
		}

		// Layout
		layoutBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Record view
		new VoodooControl("td", "id", "viewBtneditview").click();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		for (int i = 0; i < customFieldData.size(); i++) {		
			new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
			String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]",customFieldData.get(i).get("fieldName")+"_c"); 
			new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);			
		}
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		
		sugar().admin.studio.clickStudio();
		quotesButtonCtrl.click();
		layoutBtnCtrl.click();
		VoodooUtils.waitForReady();
		
		// Add custom field to List view
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooControl defaultSubPanelCtrl = new VoodooControl("td", "id", "Default");
		defaultSubPanelCtrl.waitForVisible();
		for (int i = 0; i < customFieldData.size(); i++) {
			String dataNameDraggableLi = String.format("li[data-name=%s]",customFieldData.get(i).get("fieldName")+"_c"); 
			new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultSubPanelCtrl);
		}
		VoodooControl saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		
		// Layout
		sugar().admin.studio.clickStudio();
		quotesButtonCtrl.click();
		layoutBtnCtrl.click();
		VoodooUtils.waitForReady();
		
		// Add custom field to Advance search layout
		new VoodooControl("td", "id", "searchBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "AdvancedSearchBtn").click();
		VoodooUtils.waitForReady();
		VoodooControl dropDefaultColumnCtrl = new VoodooControl("td", "id", "Default");
		for (int i = 0; i < customFieldData.size(); i++) {
			new VoodooControl("li", "css", "#Hidden [data-name="+customFieldData.get(i).get("fieldName")+"_c]").dragNDrop(dropDefaultColumnCtrl);
		}
		new VoodooControl("input", "css", "input[name='savebtn']").click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();
		
		// Create Quote Records
		for (int i = 0; i < 3; i++) {
			FieldSet fs = new FieldSet();
			fs.put("name", customFieldData.get(i).get("name"));
			sugar().quotes.create(fs);
			fs.clear();
		}
	}

	/**
	 * Show calculated fields in search panel
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_24509_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to list view of the Quotes module
		sugar().quotes.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		
		VoodooControl advanceSearchCtrl = sugar.quotes.listView.getControl("advancedSearchLink");
		
		// Go to Advanced Search if not there
		if (advanceSearchCtrl.queryVisible())
			advanceSearchCtrl.click();
		
		// Input data in the calculated fields in search panel 
		new VoodooControl("input", "id", "myformula_c_advanced").set("8");
		new VoodooControl("input", "id", "myfloat_c_advanced").set("8");
		new VoodooControl("input", "id", "mycurrency_c_advanced").set("8");
		
		// Click search button
		// TODO: VOOD-975
		VoodooControl submitBtn = new VoodooControl("input", "id", "search_form_submit_advanced");
		submitBtn.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Verify that correct result displays in listview
		sugar().quotes.listView.verifyField(1, "name", customFieldData.get(0).get("name"));
		VoodooUtils.focusFrame("bwc-frame");
		
		// Clear search panel
		// TODO: VOOD-975
		new VoodooControl("input", "id", "search_form_clear_advanced").click();
		submitBtn.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}