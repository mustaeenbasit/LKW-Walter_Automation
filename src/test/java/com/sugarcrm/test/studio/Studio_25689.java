package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_25689 extends SugarTest {
	VoodooControl accountsButtonCtrl;
	VoodooControl layoutsButtonCtrl;
	VoodooControl recordViewButtonCtrl;
	VoodooControl fieldCtrl;
	VoodooControl formulaInputCtrl;
	VoodooControl moveToLayoutPanelCtrl;
	VoodooControl moveToNewFilter1;
	VoodooControl moveToNewFilter2;
	VoodooControl formulaResultCtrl;
	VoodooControl listViewButtonCtrl;
	VoodooControl defaultPanelListViewCtrl;
	VoodooControl hiddenPanelListViewCtrl;
	VoodooControl saveBtnCtrl;
	VoodooControl trashCtrl;
	
	VoodooControl addNewFieldCtrl;
	VoodooControl fieldNameCtrl;
	VoodooControl displayLabelCtrl;
	VoodooControl helpCtrl;
	VoodooControl commentsCtrl;
	VoodooControl defaultCtrl;
	VoodooControl maxLenCtrl;
	VoodooControl maxIntLenCtrl;
	VoodooControl minIntLenCtrl;
	VoodooControl intLenCtrl;
	VoodooControl precisionCtrl;
	VoodooControl maxDecimalLenCtrl;
	VoodooControl fullTextSearchCtrl;
	VoodooControl calculatedCtrl;
	VoodooControl formulaCtrl;
	VoodooControl dependentCtrl;
	VoodooControl requiredCtrl;
	VoodooControl reportableCtrl;
	VoodooControl auditedCtrl;
	VoodooControl importableCtrl;
	VoodooControl duplicateMergeCtrl;
	VoodooControl massUpdateCtrl;
	VoodooControl htmlAreaCtrl;
	VoodooControl htmlAreaInputCtrl;
	VoodooControl fieldSaveButtonCtrl;
	VoodooControl studioFooterCtrl;
	VoodooControl accountsBreadcrumbCtrl;
	VoodooControl layoutsBreadcrumbCtrl;
	VoodooControl publishButtonCtrl;
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
	
	public void setup() throws Exception {
		sugar().login();

		// TODO: VOOD-938
		accountsButtonCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		addNewFieldCtrl = new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]");

		fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		displayLabelCtrl = new VoodooControl("input", "id", "label_value_id");
		helpCtrl = new VoodooControl("input", "name", "help");
		commentsCtrl = new VoodooControl("input", "name", "comments");
		defaultCtrl = new VoodooControl("input", "id", "default");
		maxLenCtrl = new VoodooControl("input", "id", "field_len");
		maxIntLenCtrl = new VoodooControl("input", "id", "int_max");
		minIntLenCtrl = new VoodooControl("input", "id", "int_min");
		intLenCtrl = new VoodooControl("input", "id", "int_len");
		maxDecimalLenCtrl = new VoodooControl("input", "name", "len");
		precisionCtrl = new VoodooControl("input", "id", "precision");
		fullTextSearchCtrl = new VoodooControl("input", "id", "full_text_search");
		calculatedCtrl = new VoodooControl("input", "id", "calculated");
		formulaCtrl = new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']");
		dependentCtrl = new VoodooControl("input", "id", "dependent");
		requiredCtrl = new VoodooControl("input", "name", "required");
		reportableCtrl = new VoodooControl("input", "name", "reportableCheckbox");
		auditedCtrl = new VoodooControl("input", "name", "audited");
		importableCtrl = new VoodooControl("select", "name", "importable");
		duplicateMergeCtrl = new VoodooControl("select", "name", "duplicate_merge");
		massUpdateCtrl = new VoodooControl("input", "id", "massupdate");
		htmlAreaCtrl = new VoodooControl("textarea", "id", "htmlarea");
		htmlAreaInputCtrl = new VoodooControl("body", "id", "tinymce");
		fieldSaveButtonCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");

		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");

		accountsBreadcrumbCtrl = new VoodooControl("a", "xpath", "//*[@id='mbtabs']/div/div/div/div[1]/a[contains(.,'Accounts')]");
		layoutsBreadcrumbCtrl = new VoodooControl("a", "xpath", "//*[@id='mbtabs']/div/div/div/div[1]/a[contains(.,'Layouts')]");

		formulaInputCtrl = new VoodooControl("textarea", "css", "#formulaInput");
		layoutsButtonCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewButtonCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		moveToNewFilter1 =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) > div.le_row > .le_field.special:nth-of-type(1)"); 
		moveToNewFilter2 =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) > div.le_row > .le_field.special:nth-of-type(2)"); 
		listViewButtonCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		defaultPanelListViewCtrl = new VoodooControl("ul", "css", "td#Default ul");
		hiddenPanelListViewCtrl = new VoodooControl("ul", "css", "td#Hidden ul");
		saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		trashCtrl = new VoodooControl("div", "id", "delete");
		
		historyDefaultCtrl = new VoodooControl("td", "id", "historyDefault");
		publishButtonCtrl = new VoodooControl("input", "id", "publishBtn");

		resetButtonCtrl = new VoodooControl("input", "id", "exportBtn");
		resetClickCtrl = new VoodooControl("button", "id", "execute_repair");		
		relationshipsCtrl = new VoodooControl("input", "name", "relationships");
		fieldsCtrl = new VoodooControl("input", "name", "fields");
		layoutsCtrl = new VoodooControl("input", "name", "layouts");
		labelsCtrl = new VoodooControl("input", "name", "labels");
		extensionsCtrl = new VoodooControl("input", "name", "extensions");

		ds = testData.get(testName);

		// Navigate to Admin > Studio > Accounts > Fields 	
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		sugar().admin.adminTools.getControl("studio").click();
		accountsButtonCtrl.click();

		// Select Fields button
		fieldCtrl.click();

		// Add field and save		
		addNewFieldCtrl.click();

		// Select Data Type
		new VoodooControl("select", "id", "type").set("HTML");
		VoodooUtils.waitForReady();
		
		// Set other properties
		fieldNameCtrl.set(ds.get(0).get("Field Name"));
		displayLabelCtrl.set(ds.get(0).get("Field Label"));
		helpCtrl.set(ds.get(0).get("Help Text"));
		auditedCtrl.set(ds.get(0).get("Audit"));
		duplicateMergeCtrl.set(ds.get(0).get("Duplicate Merge"));

		// Save button
		fieldSaveButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Goto Studio -> Accounts
		studioFooterCtrl.click();
		accountsButtonCtrl.click();
		
		// Goto Layouts
		layoutsButtonCtrl.click();

		// Add new field to Accounts Record view
		recordViewButtonCtrl.click();

		// Create a new row 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();

		// Move new field to new row
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",ds.get(0).get("Field Name"));
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter1);

		// Save and Deploy
		publishButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		// Add new fields to Accounts List View
		// Goto Layouts
		studioFooterCtrl.click();
		accountsButtonCtrl.click();
		layoutsButtonCtrl.click();

		// Goto Listview
		listViewButtonCtrl.click();
		defaultPanelListViewCtrl.waitForVisible();

		// Move field from Hidden to Default
		String dataNameDraggableLi = String.format("li[data-name=%s_c]",ds.get(0).get("Field Name")); 
		new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultPanelListViewCtrl);
						
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		
		// All done. Now change focus to default.
		VoodooUtils.focusDefault();

		// Create an example rec in Accounts module
		myAccount = (AccountRecord)sugar().accounts.api.create();
	}

	/**
	 * Verify that Custom HTML field value in Recordview and Listview is consistent with the 
	 * defined value in Studio - both pre and post edit 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_25689_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// On Listview, this column should be blank as we have not provided any default value
		sugar().accounts.navToListView();
		String htmlFieldname = String.format("%s_c",ds.get(0).get("Field Name")); 
		new VoodooControl("span", "css", "span[class='htmlareafield'][name='"+htmlFieldname+"']").assertContains("", true);
		
		// Goto Recordview
		// Since we are already on Listview, no need to invoke navToRecord(), just click the rec
		sugar().accounts.listView.clickRecord(1);
		
		// On Recordview, it should contain "No data"
		new VoodooControl("span", "css", "span[class='htmlareafield'][name='"+htmlFieldname+"']").assertContains("", true);
		
		// Now, edit HTML field to give it some value
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		
		sugar().admin.adminTools.getControl("studio").click();
		accountsButtonCtrl.click();
		
		// Select Fields button
		fieldCtrl.click();
		
		// Find new field and edit
		String customFieldName = String.format("%s_c", ds.get(0).get("Field Name"));
		new VoodooControl("tr", "xpath", "//*[@id='field_table']/div[3]/table/tbody[2]/tr[contains(.,'"+customFieldName+"')]").click();
		VoodooUtils.waitForReady();

		VoodooUtils.focusFrame("htmlarea_ifr");
		htmlAreaInputCtrl.set(ds.get(0).get("html_value"));
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Save button
		fieldSaveButtonCtrl.click();
		VoodooUtils.waitForReady();

		VoodooUtils.focusDefault();

		// Verify HTML field value in Recordview and Listview after field edit
		sugar().accounts.navToListView();
		htmlFieldname = String.format("span[class='htmlareafield'][name='%s_c']",ds.get(0).get("Field Name")); 
		new VoodooControl("span", "css", htmlFieldname).assertContains(ds.get(0).get("html_value").replaceAll("\\<.*?\\>", ""), true);
		
		// Goto Recordview
		// Since we are already on Listview, no need to invoke navToRecord(), just click the rec
		sugar().accounts.listView.clickRecord(1);
		
		// On Recordview, it should contain provided value
		new VoodooControl("span", "css", htmlFieldname).assertContains(ds.get(0).get("html_value").replaceAll("\\<.*?\\>", ""), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}