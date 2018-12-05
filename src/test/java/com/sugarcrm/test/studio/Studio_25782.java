package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_25782 extends SugarTest {
	VoodooControl meetingsButtonCtrl;
	VoodooControl layoutsButtonCtrl;
	VoodooControl recordViewButtonCtrl;
	VoodooControl fieldCtrl;
	VoodooControl formulaInputCtrl;
	VoodooControl formulaSaveCtrl;
	VoodooControl moveToLayoutPanelCtrl;
	VoodooControl moveToNewFilter1;
	VoodooControl moveToNewFilter2;
	VoodooControl formulaResultCtrl;
	VoodooControl defaultPanelListViewCtrl;
	VoodooControl hiddenPanelListViewCtrl;
	VoodooControl saveBtnCtrl;
	VoodooControl trashCtrl;
	VoodooControl fieldSaveCtrl;

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
	VoodooControl formulaCalculatedCtrl;
	VoodooControl formulaDependentCtrl;
	VoodooControl dependentCtrl;
	VoodooControl requiredCtrl;
	VoodooControl reportableCtrl;
	VoodooControl auditedCtrl;
	VoodooControl importableCtrl;
	VoodooControl duplicateMergeCtrl;
	VoodooControl massUpdateCtrl;
	VoodooControl htmlAreaCtrl;
	VoodooControl htmlAreaInputCtrl;

	VoodooControl reportModuleCtrl;
	VoodooControl createReportCtrl;
	VoodooControl createRowsColsReportCtrl;
	VoodooControl meetingsModuleCtrl;
	VoodooControl nextBtnCtrl;
	VoodooControl cancelBtnCtrl;

	DataSource ds, edit_ds;
	AccountRecord myAccount;

	public void setup() throws Exception {
		sugar().login();

		// TODO: VOOD-938
		meetingsButtonCtrl = new VoodooControl("a", "id", "studiolink_Meetings");
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
		formulaCalculatedCtrl = new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']");
		formulaDependentCtrl = new VoodooControl("input", "css", "#visFormulaRow td input[name='editFormula']");
		dependentCtrl = new VoodooControl("input", "id", "dependent");
		requiredCtrl = new VoodooControl("input", "name", "required");
		reportableCtrl = new VoodooControl("input", "name", "reportableCheckbox");
		auditedCtrl = new VoodooControl("input", "name", "audited");
		importableCtrl = new VoodooControl("select", "name", "importable");
		duplicateMergeCtrl = new VoodooControl("select", "name", "duplicate_merge");
		massUpdateCtrl = new VoodooControl("input", "id", "massupdate");
		htmlAreaCtrl = new VoodooControl("input", "id", "htmlarea");
		htmlAreaInputCtrl = new VoodooControl("body", "id", "tinymce");

		formulaInputCtrl = new VoodooControl("textarea", "css", "#formulaInput");
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

		reportModuleCtrl = new VoodooControl("li", "css", ".dropdown.active .fa-caret-down");
		createReportCtrl = new VoodooControl("li", "css", ".dropdown.active .dropdown-menu.scroll li:nth-of-type(1)");
		createRowsColsReportCtrl = new VoodooControl("img", "name", "rowsColsImg");
		meetingsModuleCtrl = new VoodooControl("table", "id", "Meetings");
		nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		cancelBtnCtrl = new VoodooControl("input", "id", "cancelBtn");

		ds = testData.get(testName);
	}

	/**
	 * Verify that modifications in OOB field in Studio can be done satisfactorily
	 * 
	 * @throws Exception
	 */

	@Test
	public void Studio_25782_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Admin > Studio > Meetings > Fields 	
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		meetingsButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Select Fields button
		fieldCtrl.click();
		VoodooUtils.waitForReady();

		// Edit Subject Field
		new VoodooControl("tr", "xpath", "//*[@id='field_table']/div[3]/table/tbody[2]/tr[contains(.,'Subject')]").click();

		// Modify field values and save
		displayLabelCtrl.set(ds.get(1).get("displayLabel"));
		helpCtrl.set(ds.get(1).get("helpText"));
		commentsCtrl.set(ds.get(1).get("commentText"));
		defaultCtrl.set(ds.get(1).get("defaultValue"));
		maxLenCtrl.set("");
		maxLenCtrl.set(ds.get(1).get("maxSize"));
		calculatedCtrl.set(ds.get(1).get("calculatedValue"));
		dependentCtrl.set(ds.get(1).get("dependent"));
		requiredCtrl.set(ds.get(1).get("requiredField"));
		auditedCtrl.set(ds.get(1).get("audit"));
		importableCtrl.set(ds.get(1).get("importable"));
		duplicateMergeCtrl.set(ds.get(1).get("duplicateMerge"));

		// Create a calculated formula
		formulaCalculatedCtrl.click();
		formulaInputCtrl.set("");
		String formulaWith = String.format("%s"+ds.get(1).get("calculatedFormula"), formulaInputCtrl.getAttribute("value"));
		formulaInputCtrl.set(formulaWith);
		formulaSaveCtrl.click();
		VoodooUtils.waitForReady();

		// Create a dependent formula
		formulaDependentCtrl.click();
		formulaInputCtrl.set("");
		formulaWith = String.format("%s"+ds.get(1).get("dependentFormula"), formulaInputCtrl.getAttribute("value"));
		formulaInputCtrl.set(formulaWith);
		formulaSaveCtrl.click();
		VoodooUtils.waitForReady();
		// Field Save button
		fieldSaveCtrl.click();
		VoodooUtils.waitForReady();

		// Goto Meetings module
		VoodooUtils.focusDefault();

		sugar().navbar.selectMenuItem(sugar().meetings, "createMeeting");

		// Check dependent field formula. Subject should be hidden till location is set to "New York"
		sugar().meetings.createDrawer.getEditField("name").assertVisible(false);

		// Now set location to "New York" and description to some value
		sugar().meetings.createDrawer.getEditField("location").set(ds.get(1).get("myLocation"));
		sugar().meetings.createDrawer.getEditField("description").set(ds.get(1).get("description"));

		sugar().meetings.createDrawer.getEditField("name").click();

		// Subject should become visible now
		sugar().meetings.createDrawer.getEditField("name").assertVisible(true);

		// Check calculated field formula. Subject should be equal to description field value but in disabled state
		new VoodooControl("input", "css", "input[name='name'][disabled]").assertExists(true);
		sugar().meetings.createDrawer.getEditField("name").assertContains(ds.get(1).get("description"), true);

		// Check Display Label
		new VoodooControl("span", "css", ".record-label[data-name='name']").assertContains(ds.get(1).get("displayLabel"), true);

		// Check Help Text
		new VoodooControl("p", "css", ".help-block").assertContains(ds.get(1).get("helpText"), true);

		// "Default Value" cannot be tested with calculated field set to true. Checked below with calculated set to false
		// "Comment Text" also could not be tested as it is meant for display with Studio and Module Builder only
		// "Importable" changes to "No" as soon as calculated is set, hence cannot be tested
		// "Audit" and "Duplicate Merge" cannot be tested as no such functionality could be found in Meetings module

		sugar().meetings.createDrawer.cancel();

		// Check reportable
		sugar().navbar.navToModule("Reports");
		reportModuleCtrl.click();
		createReportCtrl.click();

		VoodooUtils.focusFrame("bwc-frame");
		createRowsColsReportCtrl.click();
		meetingsModuleCtrl.click();

		// Check if reportable is set to true, then the fields list will contain the Subject field
		new VoodooControl("tr", "xpath", "//*[@id='module_fields']/div[3]/table/tbody[2]/tr[@id='Meetings_name']").assertExists(true);

		VoodooUtils.focusDefault();

		// Let us now check if reportable works when set to true
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		meetingsButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Select Fields button
		fieldCtrl.click();
		VoodooUtils.waitForReady();

		// Edit Subject Field
		new VoodooControl("tr", "xpath", "//*[@id='field_table']/div[3]/table/tbody[2]/tr[contains(.,'Subject')]").click();

		reportableCtrl.click();

		// Field Save button
		fieldSaveCtrl.click();
		VoodooUtils.waitForReady();

		VoodooUtils.focusDefault();

		sugar().navbar.navToModule("Reports");
		reportModuleCtrl.click();
		createReportCtrl.click();
		VoodooUtils.waitForReady();

		VoodooUtils.focusFrame("bwc-frame");
		createRowsColsReportCtrl.click();
		meetingsModuleCtrl.click();

		// Check if reportable is set to false, then the fields list will not contain the Subject field
		new VoodooControl("tr", "xpath", "//*[@id='module_fields']/div[3]/table/tbody[2]/tr[@id='Meetings_name']").assertExists(false);
		cancelBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Now let us check "Default Value" by unchecking the calculated checkbox.
		// Also, "Importable" cannot be tested as external import operation will be required which is not possible in an automation script

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		meetingsButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Select Fields button
		fieldCtrl.click();
		VoodooUtils.waitForReady();

		// Edit Subject Field
		new VoodooControl("tr", "xpath", "//*[@id='field_table']/div[3]/table/tbody[2]/tr[contains(.,'Subject')]").click();

		// Remove calculated formula
		formulaCalculatedCtrl.click();
		formulaInputCtrl.set("");
		formulaSaveCtrl.click();
		VoodooUtils.waitForReady();

		// Remove dependent formula
		formulaDependentCtrl.click();
		formulaInputCtrl.set("true");
		formulaSaveCtrl.click();
		VoodooUtils.waitForReady();

		calculatedCtrl.set("false");
		dependentCtrl.set("false");
		defaultCtrl.set(ds.get(1).get("defaultValue"));

		// Field Save button
		fieldSaveCtrl.click();
		VoodooUtils.waitForReady();

		VoodooUtils.focusDefault();

		// Goto Meetings module and check if default value appears
		sugar().navbar.selectMenuItem(sugar().meetings, "createMeeting");

		// Check default text
		sugar().meetings.recordView.getEditField("name").assertContains(ds.get(1).get("defaultValue"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}