package com.sugarcrm.test.studio;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_25815 extends SugarTest {
	VoodooControl casesButtonCtrl;
	VoodooControl layoutsButtonCtrl;
	VoodooControl recordViewButtonCtrl;
	VoodooControl fieldCtrl;
	VoodooControl formulaInputCtrl;
	VoodooControl formulaSaveCtrl;
	VoodooControl moveToLayoutPanelCtrl;
	VoodooControl moveToNewFilter1;
	VoodooControl moveToNewFilter2;
	VoodooControl moveToNewFilter3;
	VoodooControl formulaResultCtrl;
	VoodooControl defaultPanelListViewCtrl;
	VoodooControl hiddenPanelListViewCtrl;
	VoodooControl saveBtnCtrl;
	VoodooControl trashCtrl;
	VoodooControl fieldSaveCtrl;	
	VoodooControl publishButtonCtrl;

	VoodooControl addNewFieldCtrl;
	VoodooControl fieldNameCtrl;
	VoodooControl formulaCalculatedCtrl;
	VoodooControl formulaDependentCtrl;
	VoodooControl dependentCtrl;
	VoodooControl studioFooterCtrl;
	VoodooControl historyDefaultCtrl;

	VoodooControl resetButtonCtrl;
	VoodooControl resetClickCtrl;
	VoodooControl relationshipsCtrl;
	VoodooControl fieldsCtrl;
	VoodooControl layoutsCtrl;
	VoodooControl labelsCtrl;
	VoodooControl extensionsCtrl;

	DataSource ds;
	AccountRecord myAccount;
	CaseRecord myCase;

	public void setup() throws Exception {
		sugar().login();

		// TODO: VOOD-938
		casesButtonCtrl = new VoodooControl("a", "id", "studiolink_Cases");
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		addNewFieldCtrl = new VoodooControl("input", "xpath", "//*[@id='studiofields']/input[1]");

		fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		dependentCtrl = new VoodooControl("input", "id", "dependent");
		formulaInputCtrl = new VoodooControl("textarea", "css", "#formulaInput");

		formulaCalculatedCtrl = new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']");
		formulaDependentCtrl = new VoodooControl("input", "css", "#visFormulaRow td input[name='editFormula']");
		formulaSaveCtrl = new VoodooControl("input", "id", "fomulaSaveButton");
		layoutsButtonCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewButtonCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		moveToNewFilter1 =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) > div.le_row > .le_field.special:nth-of-type(1)"); 
		moveToNewFilter2 =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) > div.le_row > .le_field.special:nth-of-type(2)"); 
		defaultPanelListViewCtrl = new VoodooControl("ul", "css", "td#Default ul");
		hiddenPanelListViewCtrl = new VoodooControl("ul", "css", "td#Hidden ul");
		saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		trashCtrl = new VoodooControl("div", "id", "delete");
		fieldSaveCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		publishButtonCtrl = new VoodooControl("input", "id", "publishBtn");
		historyDefaultCtrl = new VoodooControl("td", "id", "historyDefault");

		resetButtonCtrl = new VoodooControl("input", "id", "exportBtn");
		resetClickCtrl = new VoodooControl("button", "id", "execute_repair");		
		relationshipsCtrl = new VoodooControl("input", "name", "relationships");
		fieldsCtrl = new VoodooControl("input", "name", "fields");
		layoutsCtrl = new VoodooControl("input", "name", "layouts");
		labelsCtrl = new VoodooControl("input", "name", "labels");
		extensionsCtrl = new VoodooControl("input", "name", "extensions");

		ds = testData.get(testName);

		// Navigate to Admin > Studio > Cases > Fields 	
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		sugar().admin.adminTools.getControl("studio").click();
		casesButtonCtrl.click();

		// Select Fields button
		fieldCtrl.click();
		VoodooUtils.waitForReady();

		// Create three custom fields
		for (int i = 0; i < ds.size(); i++) {
			addNewFieldCtrl.click();

			fieldNameCtrl.set(ds.get(i).get("customFieldName"));		
			dependentCtrl.set("true");

			// Create a dependent formula
			formulaDependentCtrl.click();
			formulaInputCtrl.set("");
			String formulaWith = String.format("%s"+ds.get(i).get("dependentFormula"), formulaInputCtrl.getAttribute("value"));
			formulaInputCtrl.set(formulaWith);
			formulaSaveCtrl.click();
			VoodooUtils.waitForReady();

			// Field Save button
			fieldSaveCtrl.click();
			VoodooUtils.waitForReady();

			// Again go to add fields but via Footer Studio link
			studioFooterCtrl.click();		
			VoodooUtils.waitForReady();
			casesButtonCtrl.click();
			VoodooUtils.waitForReady();

			// Select Fields button
			fieldCtrl.click();
		}

		// Add three custom fields to recordview
		// Click Cases in breadcrumb
		new VoodooControl("a", "xpath", "//*[@id='mbtabs']/div/div/div/div[1]/a[4]").click();

		// Select Layouts
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Goto Record view
		recordViewButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Add first row
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();

		// Add all new fields to Cases Record view
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",ds.get(0).get("customFieldName"));
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter1);

		dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",ds.get(1).get("customFieldName"));
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter2);

		// Add second row
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();

		dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",ds.get(2).get("customFieldName"));
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter1);

		// Save and Deploy
		publishButtonCtrl.click();
		VoodooUtils.waitForReady();

		myAccount = (AccountRecord) sugar().accounts.api.create();

		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that dependencies between dependent fields works correctly
	 * @throws Exception
	 */
	@Test
	public void Studio_25815_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().cases.navToListView();
		sugar().cases.listView.create();
		sugar().alerts.waitForLoadingExpiration();

		sugar().cases.recordView.getEditField("name").set(ds.get(0).get("caseName"));

		String custom1 = String.format("%s_c",ds.get(0).get("customFieldName"));
		String custom2 = String.format("%s_c",ds.get(1).get("customFieldName"));
		String custom3 = String.format("%s_c",ds.get(2).get("customFieldName"));

		VoodooControl customField1 = new VoodooControl("input", "css", "input[name='"+custom1+"']");
		VoodooControl customField2 = new VoodooControl("input", "css", "input[name='"+custom2+"']");
		VoodooControl customField3 = new VoodooControl("input", "css", "input[name='"+custom3+"']");

		// Before description is set, all custom fields are not visible
		customField1.assertVisible(false);
		customField2.assertVisible(false);
		customField3.assertVisible(false);

		// Now set description field
		sugar().cases.recordView.getEditField("description").set(ds.get(0).get("descValue"));
		sugar().cases.createDrawer.getEditField("name").click();
		customField1.waitForVisible();

		// First custom Field 1 only should become visible
		customField1.assertVisible(true);
		customField2.assertVisible(false);
		customField3.assertVisible(false);

		// Now set Custom field 1
		customField1.set(ds.get(0).get("custom1Value"));
		sugar().cases.createDrawer.getEditField("name").click();
		customField2.waitForVisible();

		// Now both custom Field 1 and custom field 2 should become visible
		customField1.assertVisible(true);
		customField2.assertVisible(true);
		customField3.assertVisible(false);

		// Now set Custom field 2
		customField2.set(ds.get(1).get("custom2Value"));
		sugar().cases.createDrawer.getEditField("name").click();
		customField3.waitForVisible();

		// Now all three custom Fields should become visible
		customField1.assertVisible(true);
		customField2.assertVisible(true);
		customField3.assertVisible(true);

		// Now set Custom field 3
		customField3.set(ds.get(2).get("custom3Value"));

		// Link Account
		sugar().cases.recordView.getEditField("relAccountName").set(myAccount.getRecordIdentifier());

		// Save and navigate to the created record
		sugar().cases.createDrawer.save();
		sugar().cases.listView.clickRecord(1);
		sugar().alerts.waitForLoadingExpiration();

		// Now Check values at Detail view also
		// TODO: VOOD-1036
		custom1 = String.format(".fld_%s_c.detail",ds.get(0).get("customFieldName"));
		custom2 = String.format(".fld_%s_c.detail",ds.get(1).get("customFieldName"));
		custom3 = String.format(".fld_%s_c.detail",ds.get(2).get("customFieldName"));

		customField1 = new VoodooControl("span", "css", custom1);
		customField2 = new VoodooControl("span", "css", custom2);
		customField3 = new VoodooControl("span", "css", custom3);

		// In Detail View, all custom fields are visible
		customField1.assertVisible(true);
		customField2.assertVisible(true);
		customField3.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}