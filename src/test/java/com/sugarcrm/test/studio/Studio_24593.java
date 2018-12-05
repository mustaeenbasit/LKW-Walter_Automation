package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Studio_24593 extends SugarTest {
	VoodooControl contactsButtonCtrl;
	FieldSet myData = new FieldSet();
	OpportunityRecord myOpportunity;

	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().accounts.api.create();
		sugar().login();

		// Create a opportunity without RLI
		myOpportunity = (OpportunityRecord) sugar().opportunities.create();
	}

	/**
	 * Auto updating calculated field that contain rollupSum function when related record changes.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_24593_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-542
		// Go to Contacts SubPanel > Fields
		contactsButtonCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		contactsButtonCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		myData = testData.get(testName).get(0);
		
		// Create a custom calculated field in contacts module
		new VoodooControl("input", "css", "input[value='Add Field']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(myData.get("field_name"));
		new VoodooControl("input", "id", "calculated").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
		VoodooUtils.waitForReady();

		// Set formula contain rollupSum function, such as rollupSum(opportunities,"amount")
		new VoodooControl("a", "css", ".rollup.button a").click();
		new VoodooControl("option", "css", "select#rollwiz_type option[value='Max']").click();
		new VoodooControl("option", "css", "select#rollwiz_rmodule option[value='opportunities']").click();
		new VoodooControl("button", "css", "[name='selrf_insertbtn']").click();
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']").click();
		VoodooUtils.waitForReady();
		contactsButtonCtrl.click();
		new VoodooControl("td", "id", "layoutsBtn").click();

		// Add custom field to List view
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooControl defaultSubPanelCtrl = new VoodooControl("td", "id", "Default");
		defaultSubPanelCtrl.waitForVisible();
		String dataNameDraggableLi = String.format("li[data-name=%s]",myData.get("display_name")); 
		new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultSubPanelCtrl);
		VoodooControl saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "div.bodywrapper div a:nth-of-type(5)").click();

		// Add custom field to Record view
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row");
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]",myData.get("display_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Create a contact record and select related opportunities for it.
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		StandardSubpanel opportunitySubpanel = sugar().contacts.recordView.subpanels.get("Opportunities");
		opportunitySubpanel.linkExistingRecord(myOpportunity);

		// Verify the sum value of related opportunities' amount will show in the calculated field 
		sugar().contacts.navToListView();
		VoodooControl displayName = new VoodooControl("div", "css", ".fld_"+myData.get("display_name")+" div");
		displayName.assertContains(myData.get("value"), true);

		// Go to Opportunity recordView  and update Amount
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.edit();
		sugar().opportunities.recordView.getEditField("likelyCase").set(myData.get("new_value"));
		sugar().opportunities.recordView.save();

		// Go to the detail view and edit view page of the contact record
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// Verify the calculated field value auto updated to the new value
		displayName.assertContains(myData.get("new_value"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}