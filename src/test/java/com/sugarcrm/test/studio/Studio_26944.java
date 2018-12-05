package com.sugarcrm.test.studio;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_26944 extends SugarTest {
	VoodooControl contactSubPanelCtrl, fieldCtrl, studioFooterCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl;
	FieldSet customData, fs;
	
	public void setup() throws Exception {
		sugar().login();
		customData = testData.get(testName).get(0);
	}

	/**
	 * Verify that calculated field function "today()" works correctly
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_26944_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-938
		// studio
		contactSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		contactSubPanelCtrl.click();		
		VoodooUtils.waitForReady();
		fieldCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "#type").set(customData.get("data_type"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "calculated").click();		
		new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("textarea", "css", "#formulaInput").set(customData.get("formula"));
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady(30000);

		// TODO: VOOD-999
		studioFooterCtrl.click();
		VoodooUtils.waitForReady();
		contactSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		// Record view		
		recordViewSubPanelCtrl.click();	
		VoodooUtils.waitForReady();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		VoodooUtils.waitForReady();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get("module_field_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		
		// Create contact
		sugar().contacts.create();
		sugar().contacts.listView.getControl(String.format("link%02d", 1)).click();
		
		// Get current date
		Date dt = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		dt = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyy");
		String date = sdf.format(dt);
		
		// Verify that custom filed is exist
		new VoodooControl("div", "css", "span.fld_"+customData.get("module_field_name")+"_c div").assertContains(date, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete."+date);
	}

	public void cleanup() throws Exception {}
}
