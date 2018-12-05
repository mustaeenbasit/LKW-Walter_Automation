
package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_26663 extends SugarTest {
	DataSource customData;
	VoodooControl layoutsButtonCtrl, recordViewButtonCtrl, moveToLayoutPanelCtrl, moveToNewFilter, fieldsBtn;
	ContactRecord myContact;
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify custom dropdown list with a blank option.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_26663_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		customData = testData.get(testName);
		sugar().admin.navToAdminPanelLink("dropdownEditor");
		VoodooUtils.focusFrame("bwc-frame");
		
		// Add a dropdown list
		new VoodooControl("input", "css", "[name='adddropdownbtn']").click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-938
		new VoodooControl("input", "css", "input[name='dropdown_name']").set(customData.get(0).get("name"));
		new VoodooControl("input", "css", "input[name='drop_name']").set(customData.get(0).get("item_name"));
		new VoodooControl("input", "css", "input[name='drop_value']").set(customData.get(0).get("display_label"));
		new VoodooControl("input", "css", "#dropdownaddbtn").click();
		VoodooUtils.pause(2000); // TODO: PAT-1957 - Studio buttons remain disabled longer than intended
		new VoodooControl("input", "css", "input[name='drop_name']").set(customData.get(1).get("item_name"));
		new VoodooControl("input", "css", "input[name='drop_value']").set(customData.get(1).get("display_label"));
		new VoodooControl("input", "css", "#dropdownaddbtn").click();
		VoodooUtils.pause(2000); // TODO: PAT-1957 - Studio buttons remain disabled longer than intended
		new VoodooControl("input", "css", "input[name='drop_name']").set(customData.get(2).get("item_name"));
		new VoodooControl("input", "css", "input[name='drop_value']").set(customData.get(2).get("display_label"));
		new VoodooControl("input", "css", "#dropdownaddbtn").click();
		VoodooUtils.pause(2000); // TODO: PAT-1957 - Studio buttons remain disabled longer than intended
		new VoodooControl("input", "css", "#saveBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.pause(2000); // TODO: PAT-1957 - Studio buttons remain disabled longer than intended

		// TODO: Investigate Jenkins failure at the below line.
		// new VoodooControl("a", "css" ,"div.bodywrapper div a:nth-of-type(4)").click();
				
		// TODO: Remove when above issue fix. 
		new VoodooControl("input", "css", "#footerHTML input[value='Studio']").click();
		new VoodooControl("a", "id", "studiolink_Contacts").click();
		VoodooUtils.waitForReady();
		fieldsBtn = new VoodooControl("td", "id", "fieldsBtn");
		fieldsBtn.click();
		VoodooUtils.waitForReady();
		
		// Add field and save
		// TODO: VOOD-938
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("option", "css", "#type option[value='enum']").click();
		new VoodooControl("option", "css", "#options option[value='myCustom_dom']").click();
		new VoodooControl("input", "id", "field_name_id").set(customData.get(0).get("module_field_name"));
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		// TODO: Investigate Jenkins failure at the below line.
		// new VoodooControl("a", "css" ,"div.bodywrapper div a:nth-of-type(4)").click();
		
		// TODO: Remove when above issue fix. 
		new VoodooControl("input", "css", "#footerHTML input[value='Studio']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "id", "studiolink_Contacts").click();
		VoodooUtils.waitForReady();
		
		// Add new field to Contacts Record view
		// TODO: VOOD-938
		layoutsButtonCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewButtonCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		layoutsButtonCtrl.click();
		recordViewButtonCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get(0).get("module_field_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Create and edit Contact record, set the option field to blank, then Save 
		myContact = (ContactRecord) sugar().contacts.api.create();
		myContact.navToRecord();
		sugar().contacts.recordView.edit();
		new VoodooControl("span", "css", "[data-fieldname='my_field_c']").click();
		new VoodooControl("li", "css", ".select2-drop .select2-results li:nth-child(1)").click();
		sugar().contacts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify the new option field is set to blank and then set value to non-Blank.
		myContact.navToRecord();
		new VoodooControl("span", "css", ".fld_my_field_c.detail").assertContains(customData.get(0).get("item_name"), true);
		sugar().contacts.recordView.edit();
		new VoodooSelect("span", "css", "[data-voodoo-name='my_field_c']").set(customData.get(2).get("item_name"));
		sugar().contacts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify the value of new option field is set to non-Blank
		myContact.navToRecord();
		new VoodooControl("span", "css", ".fld_my_field_c.detail").assertContains(customData.get(2).get("item_name"), true);
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}