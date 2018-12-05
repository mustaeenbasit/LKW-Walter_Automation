
package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_24687 extends SugarTest {
	VoodooControl leadsSubPanelCtrl;
	FieldSet customData;
	LeadRecord myLead;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify that the Encrypt Field sort can be used in List View
	 * @throws Exception
	 */
	@Test
	public void Studio_24687_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-938
		// studio
		sugar().admin.adminTools.getControl("studio").click();
		leadsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Leads");
		leadsSubPanelCtrl.click();
		new VoodooControl("td", "id", "fieldsBtn").click();

		// TODO: VOOD-938
		// Add field [type=Encrypt] and save
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		new VoodooControl("select", "css", "#type").set(customData.get("data_type"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-999
		new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']").click();
		leadsSubPanelCtrl.click();

		// TODO: VOOD-938
		// layout subpanel
		new VoodooControl("td", "id", "layoutsBtn").click();

		// TODO: VOOD-938
		// List view
		new VoodooControl("a", "css", "#viewBtnlistview tr:nth-child(1) td a").click();     
		VoodooControl moveHere = new VoodooControl("td", "css", "#Default li:nth-of-type(5)");
		new VoodooControl("li", "css", ".draggable[data-name='"+customData.get("module_field_name")+"_c").dragNDrop(moveHere);
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Leads listview
		sugar().leads.navToListView();
		sugar().leads.listView.editRecord(1);

		// TODO: VOOD-1036
		// Verifying Encrypted field 
		new VoodooControl("span", "css", "th[data-fieldname='"+customData.get("module_field_name")+"_c'] div:nth-of-type(2) span").assertEquals(customData.get("module_field_name_lbl"), true);
		new VoodooControl("input", "css", ".fld_test_encrypt_c.edit input").set(customData.get("value"));
		sugar().leads.listView.saveRecord(1);
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("div", "css", ".fld_test_encrypt_c.list div").assertEquals(customData.get("value"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}