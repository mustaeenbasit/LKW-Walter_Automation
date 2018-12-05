package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_25607 extends SugarTest {
	FieldSet  customData;
	DataSource contactsData;
	VoodooControl contactsSubPanelCtrl; 

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		contactsData = testData.get(testName+"_contacts");
		sugar().login();
		
		// Go to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-542
		contactsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		contactsSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		// Adding dropdown custom field
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "id", "type").set(customData.get("data_type"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		sugar().admin.studio.clickStudio();
		contactsSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-542
		// layout subpanel
		VoodooControl layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		// List view
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();
		VoodooControl defaultSubPanelCtrl = new VoodooControl("td", "css", "#Default ul");
		defaultSubPanelCtrl.waitForVisible();
		new VoodooControl("li", "css", "li[data-name="+customData.get("module_field_name")+"_c]").dragNDrop(defaultSubPanelCtrl);
		VoodooControl saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		saveBtnCtrl.click();
		sugar().admin.studio.clickStudio();
		contactsSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-938
		// Record view
		new VoodooControl("td", "id", "viewBtnrecordview").click(); 
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		new VoodooControl("div", "css", "div[data-name="+customData.get("module_field_name")+"_c]").dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		sugar().admin.studio.clickStudio();
		contactsSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();

		// Search view
		new VoodooControl("td", "id", "searchBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "FilterSearchBtn").click();
		VoodooUtils.waitForReady();
		VoodooControl defaultSearchCtrl = new VoodooControl("ul", "id", "ul0");
		defaultSearchCtrl.waitForVisible();
		new VoodooControl("li", "css", "li[data-name="+customData.get("module_field_name")+"_c]").dragNDrop(defaultSearchCtrl);
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();

		// Contact records
		sugar().contacts.navToListView();
		for(int i=0; i< contactsData.size(); i++) {
			sugar().contacts.listView.create();
			sugar().contacts.createDrawer.getEditField("lastName").set(contactsData.get(i).get("last_name"));

			// TODO: VOOD-1036
			new VoodooSelect("div", "css", ".fld_"+customData.get("module_field_name")+"_c.edit div").set(contactsData.get(i).get("dropdown_value"));
			sugar().contacts.createDrawer.save();
		}
	}

	/**
	 * Search Custom field_Verify that custom field can be searched in "Advanced Search" form.
	 * @throws Exception
	 */
	@Test
	public void Studio_25607_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contacts.listView.openFilterDropdown();
		sugar().contacts.listView.selectFilterCreateNew();
		String searchValStr = customData.get("module_field_name").replace('_', ' ');
		
		// TODO: VOOD-1036
		// filter condition
		VoodooSelect dropdownCtrl = new VoodooSelect("a", "css", ".select2-choice.select2-default");
		dropdownCtrl.set(searchValStr);
		dropdownCtrl.set(customData.get("filter"));
		VoodooSelect searchInputCtrl = new VoodooSelect("input", "css", ".select2-search-field input");
		searchInputCtrl.set(customData.get("dsearch1"));
		sugar().alerts.waitForLoadingExpiration();

		// Verifying 2 records (search with Customer)
		sugar().contacts.listView.verifyField(1, "fullName", contactsData.get(4).get("last_name"));
		sugar().contacts.listView.verifyField(2, "fullName", contactsData.get(2).get("last_name"));
		searchInputCtrl.set(customData.get("dsearch2"));
		sugar().alerts.waitForLoadingExpiration();

		// Verifying 3 records (search with Customer + Competitor)
		sugar().contacts.listView.verifyField(3, "fullName", contactsData.get(1).get("last_name"));

		// Cancel filter
		new VoodooControl("a", "css", ".filter-close").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify that  all contact records are displayed in the list view after filter is cancelled
		for(int i = 1, j = 4; i <= contactsData.size(); i++, j--)
			sugar().contacts.listView.verifyField(i, "fullName", contactsData.get(j).get("last_name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}