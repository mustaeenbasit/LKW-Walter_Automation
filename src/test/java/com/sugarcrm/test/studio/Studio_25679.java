package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_25679 extends SugarTest {
	VoodooControl accountsButtonCtrl;
	VoodooControl layoutsButtonCtrl;
	VoodooControl recordViewButtonCtrl;
	VoodooControl fieldCtrl;
	VoodooControl formulaInputCtrl;
	VoodooControl moveToLayoutPanelCtrl;
	VoodooControl moveToNewFilter1;
	VoodooControl moveToNewFilter2;
	VoodooControl formulaResultCtrl;
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
	
	VoodooControl listViewButtonCtrl;
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

	DataSource ds, edit_ds;
	AccountRecord myAccount;
	
	String currDate;
	
	public void setup() throws Exception {
		sugar().login();

		currDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		
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
		htmlAreaCtrl = new VoodooControl("input", "id", "htmlarea");
		htmlAreaInputCtrl = new VoodooControl("body", "id", "tinymce");

		fieldSaveButtonCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");

		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");

		accountsBreadcrumbCtrl = new VoodooControl("a", "xpath", "//*[@id='mbtabs']/div/div/div/div[1]/a[4]");
		layoutsBreadcrumbCtrl = new VoodooControl("a", "xpath", "//*[@id='mbtabs']/div/div/div/div[1]/a[5]");

		listViewButtonCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		
		historyDefaultCtrl = new VoodooControl("td", "id", "historyDefault");
		publishButtonCtrl = new VoodooControl("input", "id", "publishBtn");

		resetButtonCtrl = new VoodooControl("input", "id", "exportBtn");
		resetClickCtrl = new VoodooControl("button", "id", "execute_repair");		
		relationshipsCtrl = new VoodooControl("input", "name", "relationships");
		fieldsCtrl = new VoodooControl("input", "name", "fields");
		layoutsCtrl = new VoodooControl("input", "name", "layouts");
		labelsCtrl = new VoodooControl("input", "name", "labels");
		extensionsCtrl = new VoodooControl("input", "name", "extensions");
		
		formulaInputCtrl = new VoodooControl("textarea", "css", "#formulaInput");
		layoutsButtonCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewButtonCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		moveToNewFilter1 =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) > div.le_row > .le_field.special:nth-of-type(1)"); 
		moveToNewFilter2 =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) > div.le_row > .le_field.special:nth-of-type(2)"); 
		defaultPanelListViewCtrl = new VoodooControl("ul", "css", "td#Default ul");
		hiddenPanelListViewCtrl = new VoodooControl("ul", "css", "td#Hidden ul");
		saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		trashCtrl = new VoodooControl("div", "id", "delete");
		
		ds = testData.get(testName);
		edit_ds = testData.get(testName+"_edit");
		
		// Per Setup condition of the test case, example data of TC# 7802 is now being created below
		
		// Navigate to Admin > Studio > Accounts > Fields 	
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		accountsButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Select Fields button
		fieldCtrl.click();
		VoodooUtils.waitForReady();

		// Add multiple fields and save		
		for (int i=0; i<=ds.size()-1; i++) {
			sugar().alerts.waitForLoadingExpiration();
			addNewFieldCtrl.click();
			VoodooUtils.waitForReady();

			// Select Data Type
			new VoodooControl("option", "css", "select#type option[value='"+ds.get(i).get("Data Type")+"']").click();
			VoodooUtils.waitForReady();
			
			fieldNameCtrl.set(ds.get(i).get("Field Name"));
			VoodooUtils.waitForReady();

			if (ds.get(i).get("Data Type") == "enum") { // DropDown
				if (!ds.get(i).get("Drop Down List").isEmpty() && !ds.get(i).get("Drop Down List").contentEquals("Leave as default")) {
					// Select DropDown
					new VoodooControl("select", "id", "options").set("account_type_dom");
				}
			}
			if (defaultCtrl.queryVisible() && !ds.get(i).get("Default Value").isEmpty() && !ds.get(i).get("Default Value").contentEquals("Leave as default")) {
				defaultCtrl.set(ds.get(i).get("Default Value"));
			}
			if (maxLenCtrl.queryVisible() && !ds.get(i).get("Max Size").isEmpty() && !ds.get(i).get("Max Size").contentEquals("Leave as default")) {
				maxLenCtrl.set(ds.get(i).get("Max Size"));
			}
			if (reportableCtrl.queryVisible() && ds.get(i).get("Reportable").contentEquals("Checked")) {
				reportableCtrl.set("true");
			}
			if (ds.get(i).get("Duplicate Merge").contentEquals("Disabled")) {
				duplicateMergeCtrl.set("Disabled");
			}
			else if (ds.get(i).get("Duplicate Merge").contentEquals("Enabled")) {
				duplicateMergeCtrl.set("Enabled");
			}
			
			// Field Save button
			fieldSaveButtonCtrl.click();
			VoodooUtils.waitForReady();

			studioFooterCtrl.click();
			accountsButtonCtrl.click();

			fieldCtrl.click();
			VoodooUtils.waitForReady();
		}
		
		// Accounts in breadcrumb
		accountsBreadcrumbCtrl.click();
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Add all new fields to Accounts Record view
		recordViewButtonCtrl.click();
		VoodooUtils.waitForReady();

		VoodooControl myMoveToNewFilter;
		String j = "2";
		String dataNameDraggableFieldToRecordSubpanel;
		
		for (int i=0; i<=ds.size()-1; i++) {
			// Address field is handled separately, out of this loop as it breaks into 5 different pieces
			if (ds.get(i).get("Field Name").contentEquals("property_address")) {
				continue;
			}
			
			j = j.equals("2") ? "1" : "2"; // Toggle j

			// Create a new row 
			if (j == "1") {
				new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
				moveToLayoutPanelCtrl.waitForVisible();
			}
			
			myMoveToNewFilter = j.equals("1") ? moveToNewFilter1 : moveToNewFilter2;
			
			dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",ds.get(i).get("Field Name"));
			new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(myMoveToNewFilter);
		}
		
		// Handle Address Separately
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]","property_address_street");
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter2);
		
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]","property_address_city");
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter2);
		
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]","property_address_state");
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter2);

		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]","property_address_country");
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter2);
		
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]","property_address_postalcode");
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter2);
				
		// Save and Deploy
		publishButtonCtrl.click();
		VoodooUtils.waitForReady(30000);
		
		// Add new fields to Accounts List View
		
		// Layouts in breadcrumb
		layoutsBreadcrumbCtrl.click();
		VoodooUtils.waitForReady();
		listViewButtonCtrl.click();
		VoodooUtils.waitForReady();

		defaultPanelListViewCtrl.waitForVisible();

		String dataNameDraggableLi;
		for (int i=0; i<=ds.size()-1; i++) {
			// Address field is handled separately, out of this loop as it breaks into 5 different pieces
			if (ds.get(i).get("Field Name").contentEquals("property_address")) {
				continue;
			}
			
			dataNameDraggableLi = String.format("li[data-name=%s_c]",ds.get(i).get("Field Name")); 
			new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultPanelListViewCtrl);
		}
		
		// Handle Address Separately
		dataNameDraggableLi = String.format("li[data-name=%s_c]","property_address_street");
		new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultPanelListViewCtrl);
		
		dataNameDraggableLi = String.format("li[data-name=%s_c]","property_address_city");
		new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultPanelListViewCtrl);
		
		dataNameDraggableLi = String.format("li[data-name=%s_c]","property_address_state");
		new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultPanelListViewCtrl);

		dataNameDraggableLi = String.format("li[data-name=%s_c]","property_address_country");
		new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultPanelListViewCtrl);
		
		dataNameDraggableLi = String.format("li[data-name=%s_c]","property_address_postalcode");
		new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultPanelListViewCtrl);
				
		saveBtnCtrl.click();
		VoodooUtils.waitForReady(30000);
		
		// All done. Now change focus to default.
		VoodooUtils.focusDefault();

		// Jenkins hack
		sugar().logout();
		sugar().login();

		// Create an example rec in Accounts module
		myAccount = (AccountRecord)sugar().accounts.api.create();

		myAccount.navToRecord();
		sugar().accounts.recordView.edit();
		
		new VoodooControl("input", "name", "property_id_c").set("1234");
		new VoodooControl("input", "name", "property_name_c").set("ABC");
		new VoodooControl("input", "css", "span.fld_property_owner_occupied_c.edit input").set("true");
		new VoodooControl("input", "name", "property_cost_c").set("1234");
		new VoodooControl("input", "css", "span.fld_date_property_c.edit input").waitForVisible();
		new VoodooControl("input", "css", "span.fld_date_property_c.edit input").set(currDate);

		new VoodooControl("span", "css", "span.fld_property_resources_c.edit").click();
		new VoodooControl("li", "css", "ul.select2-results li:nth-of-type(2)[role='presentation']").click();
		
		new VoodooControl("input", "name", "property_phone_c").set("(091)12345");
		
		new VoodooControl("span", "css", "span.fld_property_ms_c.edit").click();
		new VoodooControl("li", "css", "ul.select2-results > li:nth-child(3)").click();		
		new VoodooControl("span", "css", "span.fld_property_ms_c.edit").click();
		new VoodooControl("li", "css", "ul.select2-results > li:nth-child(2)").click();
		
		new VoodooControl("input", "name", "property_currency_c").set("1234");
		
		new VoodooControl("input", "css", "input[name='property_radio_c'][value='Analyst']").click();
		
		new VoodooControl("input", "name", "property_address_street_c").set("My Street");
		new VoodooControl("input", "name", "property_address_city_c").set("My City");
		new VoodooControl("input", "name", "property_address_state_c").set("My State");
		new VoodooControl("input", "name", "property_address_country_c").set("USA");
		new VoodooControl("input", "name", "property_address_postalcode_c").set("12345");
		
		sugar().accounts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();

		// Now, asserts to verify that custom fields contain correct data in Record view
		new VoodooControl("span", "css", ".fld_property_name_c.detail").assertContains("ABC", true);
		new VoodooControl("span", "css", ".fld_property_id_c.detail").assertContains("1,234", true);
		new VoodooControl("span", "css", ".fld_property_owner_occupied_c.detail").assertExists(true);
		new VoodooControl("span", "css", ".fld_property_cost_c.detail").assertContains("1,234.00000000", true);
		// TODO: Investigate why below line is not working on Jenkins
		// new VoodooControl("span", "css", ".fld_date_property_c.detail").assertContains(currDate, true);
		new VoodooControl("span", "css", ".fld_property_resources_c.detail").assertContains("Analyst", true);
		new VoodooControl("span", "css", ".fld_property_phone_c.detail").assertContains("(091)12345", true);
		new VoodooControl("span", "css", ".fld_property_ms_c.detail").assertContains("Competitor, Customer", true);
		new VoodooControl("span", "css", ".fld_property_currency_c.detail").assertContains("$1,234.00", true);
		new VoodooControl("span", "css", ".fld_property_radio_c.detail").assertContains("Analyst", true);
		
		new VoodooControl("span", "css", ".fld_property_address_street_c.detail").assertContains("My Street", true);
		new VoodooControl("span", "css", ".fld_property_address_city_c.detail").assertContains("My City", true);
		new VoodooControl("span", "css", ".fld_property_address_state_c.detail").assertContains("My State", true);
		new VoodooControl("span", "css", ".fld_property_address_country_c.detail").assertContains("USA", true);
		new VoodooControl("span", "css", ".fld_property_address_postalcode_c.detail").assertContains("12345", true);

		sugar().accounts.navToListView();
		
		// Now, first asserts to verify that custom fields contain correct data in List view
		new VoodooControl("span", "css", ".fld_property_name_c.list").assertContains("ABC", true);
		new VoodooControl("span", "css", ".fld_property_id_c.list").assertContains("1,234", true);
		new VoodooControl("span", "css", ".fld_property_owner_occupied_c.list").assertExists(true);
		new VoodooControl("span", "css", ".fld_property_cost_c.list").assertContains("1,234.00000000", true);
		// TODO: Investigate why below line is not working on Jenkins
		// new VoodooControl("span", "css", ".fld_date_property_c.list").assertContains(currDate, true);
		new VoodooControl("span", "css", ".fld_property_resources_c.list").assertContains("Analyst", true);
		new VoodooControl("span", "css", ".fld_property_phone_c.list").assertContains("(091)12345", true);
		new VoodooControl("span", "css", ".fld_property_ms_c.list").assertContains("Competitor, Customer", true);
		new VoodooControl("span", "css", ".fld_property_currency_c.list").assertContains("$1,234.00", true);
		new VoodooControl("span", "css", ".fld_property_radio_c.list").assertContains("Analyst", true);
		
		new VoodooControl("span", "css", ".fld_property_address_street_c.list").assertContains("My Street", true);
		new VoodooControl("span", "css", ".fld_property_address_city_c.list").assertContains("My City", true);
		new VoodooControl("span", "css", ".fld_property_address_state_c.list").assertContains("My State", true);
		new VoodooControl("span", "css", ".fld_property_address_country_c.list").assertContains("USA", true);
		new VoodooControl("span", "css", ".fld_property_address_postalcode_c.list").assertContains("12345", true);
	}

	/**
	 * Verify that editing of Studio custom fields works correctly
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_25679_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		accountsButtonCtrl.click();
		VoodooUtils.waitForReady();
		fieldCtrl.click();
		VoodooUtils.waitForReady();

		// edit multiple custom fields
		String customFieldName;		
		for (int i=0; i<=edit_ds.size()-1; i++) {
			sugar().alerts.waitForLoadingExpiration();
			// Select Custom Field to delete
			customFieldName = String.format("%s_c", edit_ds.get(i).get("Field Name"));					
			new VoodooControl("tr", "xpath", "//*[@id='field_table']/div[3]/table/tbody[2]/tr[contains(.,'"+customFieldName+"')]").click();
		
			if (edit_ds.get(i).get("Data Type").contentEquals("enum")) { // DropDown
				if (!edit_ds.get(i).get("Drop Down List").isEmpty() && !edit_ds.get(i).get("Drop Down List").contentEquals("Leave as default")) {
					// Select DropDown
					new VoodooControl("option", "css", "select[id='options'] option[value='"+edit_ds.get(i).get("Drop Down List")+"']").click();
				}
			}
			if (!edit_ds.get(i).get("Field Label").isEmpty()) {
				displayLabelCtrl.set(edit_ds.get(i).get("Field Label"));
			}
			if (!edit_ds.get(i).get("Help Text").isEmpty()) {
				helpCtrl.set(edit_ds.get(i).get("Help Text"));
			}
			
			// Max, Min and Size for Int is different for Integer type
			if (edit_ds.get(i).get("Data Type").contentEquals("int")) {
				if (!edit_ds.get(i).get("Min Value").isEmpty()) {
					minIntLenCtrl.set(edit_ds.get(i).get("Min Value"));
				}
				if (!edit_ds.get(i).get("Max Value").isEmpty()) {
					maxIntLenCtrl.set(edit_ds.get(i).get("Max Value"));
				}
				if (!edit_ds.get(i).get("Max Size").isEmpty()) {
					intLenCtrl.set(edit_ds.get(i).get("Max Size"));
				}
			}
			else {
				if (maxLenCtrl.queryVisible() && !edit_ds.get(i).get("Max Size").isEmpty() && !edit_ds.get(i).get("Max Size").contentEquals("Leave as default")) {
					maxLenCtrl.set(edit_ds.get(i).get("Max Size"));
				}	
			}
			
			// precision is applicable for decimal fields only. Max len is also different for decimal fields
			if (edit_ds.get(i).get("Data Type").contentEquals("decimal")) {
				if (!edit_ds.get(i).get("Precision").isEmpty()) {
					precisionCtrl.set(edit_ds.get(i).get("Precision"));
				}
				if (!edit_ds.get(i).get("Max Size").isEmpty()) {
					maxDecimalLenCtrl.set(edit_ds.get(i).get("Max Size"));
				}				
			}
			
			if (defaultCtrl.queryVisible() && !edit_ds.get(i).get("Default Value").isEmpty() && !edit_ds.get(i).get("Default Value").contentEquals("Leave as default")) {
				defaultCtrl.set(edit_ds.get(i).get("Default Value"));
			}

			if (auditedCtrl.queryVisible()) {
				if (edit_ds.get(i).get("Audit").contentEquals("Checked")) {
					auditedCtrl.set("true");
				}
			}
			
			// Mass Update is visible only for date, datetime, dropdown and radio fields
			if (massUpdateCtrl.queryVisible() && edit_ds.get(i).get("Mass Update").contentEquals("Checked")) {
				massUpdateCtrl.set("true");
			}
			
			if (edit_ds.get(i).get("Data Type").contentEquals("html")) {
				if (!edit_ds.get(i).get("HTML").isEmpty()) {
					VoodooUtils.focusFrame("htmlarea_ifr");
					htmlAreaInputCtrl.set(edit_ds.get(i).get("HTML"));
					VoodooUtils.focusDefault();
					VoodooUtils.focusFrame("bwc-frame");
				}
			}
						
			if (reportableCtrl.queryVisible() && edit_ds.get(i).get("Reportable").contentEquals("Checked")) {
				reportableCtrl.set("true");
			}
			
			if (ds.get(i).get("Duplicate Merge").contentEquals("Disabled")) {
				duplicateMergeCtrl.set("Disabled");
			}
			else if (ds.get(i).get("Duplicate Merge").contentEquals("Enabled")) {
				duplicateMergeCtrl.set("Enabled");
			}
			
			// Save button
			fieldSaveButtonCtrl.click();
			VoodooUtils.waitForReady(30000);
		}
				
		VoodooUtils.focusDefault();
		
		// Jenkins hack
		sugar().logout();
		sugar().login();

		sugar().accounts.navToListView();
		
		// Now, first asserts to verify that custom fields contain correct data in List view
		new VoodooControl("span", "css", ".fld_property_name_c.list").assertContains("ABC", true);
		new VoodooControl("span", "css", ".fld_property_id_c.list").assertContains("1,234", true);
		new VoodooControl("span", "css", ".fld_property_owner_occupied_c.list").assertExists(true);
		// Property cost def changed
		new VoodooControl("span", "css", ".fld_property_cost_c.list").assertContains("1,234.00", true);
		// TODO: Investigate why below line is not working on Jenkins
		// new VoodooControl("span", "css", ".fld_date_property_c.list").assertContains(currDate, true);
		new VoodooControl("span", "css", ".fld_property_resources_c.list").assertContains("Analyst", true);
		new VoodooControl("span", "css", ".fld_property_phone_c.list").assertContains("(091)12345", true);
		new VoodooControl("span", "css", ".fld_property_ms_c.list").assertContains("Competitor, Customer", true);
		new VoodooControl("span", "css", ".fld_property_currency_c.list").assertContains("$1,234.00", true);
		new VoodooControl("span", "css", ".fld_property_radio_c.list").assertContains("Analyst", true);
		
		new VoodooControl("span", "css", ".fld_property_address_street_c.list").assertContains("My Street", true);
		new VoodooControl("span", "css", ".fld_property_address_city_c.list").assertContains("My City", true);
		new VoodooControl("span", "css", ".fld_property_address_state_c.list").assertContains("My State", true);
		new VoodooControl("span", "css", ".fld_property_address_country_c.list").assertContains("USA", true);
		new VoodooControl("span", "css", ".fld_property_address_postalcode_c.list").assertContains("12345", true);

		sugar().accounts.listView.clickRecord(1);

		// Now, asserts to verify that custom fields contain correct data as after edit of field definitions
		new VoodooControl("span", "css", ".fld_property_name_c.detail").assertContains("ABC", true);
		new VoodooControl("span", "css", ".fld_property_id_c.detail").assertContains("1,234", true);
		new VoodooControl("span", "css", ".fld_property_owner_occupied_c.detail").assertExists(true);

		// Property cost def changed
		new VoodooControl("span", "css", ".fld_property_cost_c.detail").assertContains("1,234.00", true);
		
		// TODO: Investigate why below line is not working on Jenkins
		// new VoodooControl("span", "css", ".fld_date_property_c.detail").assertContains(currDate, true);
		new VoodooControl("span", "css", ".fld_property_resources_c.detail").assertContains("Analyst", true);
		new VoodooControl("span", "css", ".fld_property_phone_c.detail").assertContains("(091)12345", true);
		new VoodooControl("span", "css", ".fld_property_ms_c.detail").assertContains("Competitor, Customer", true);
		new VoodooControl("span", "css", ".fld_property_currency_c.detail").assertContains("$1,234.00", true);
		new VoodooControl("span", "css", ".fld_property_radio_c.detail").assertContains("Analyst", true);
		
		new VoodooControl("span", "css", ".fld_property_address_street_c.detail").assertContains("My Street", true);
		new VoodooControl("span", "css", ".fld_property_address_city_c.detail").assertContains("My City", true);
		new VoodooControl("span", "css", ".fld_property_address_state_c.detail").assertContains("My State", true);
		new VoodooControl("span", "css", ".fld_property_address_country_c.detail").assertContains("USA", true);
		new VoodooControl("span", "css", ".fld_property_address_postalcode_c.detail").assertContains("12345", true);
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}