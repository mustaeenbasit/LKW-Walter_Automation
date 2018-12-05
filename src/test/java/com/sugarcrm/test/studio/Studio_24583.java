package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_24583  extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Workflow action should not occur if the field that to be updated through the workflow action is turned into calculated field.
	 * @throws Exception
	 */
	@Test
	public void Studio_24583_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);

		// TODO: VOOD-1042
		// Create Workflow Definition => leads module
		VoodooControl dropDownCtrl = new VoodooControl("i", "css", "li.active .fa-caret-down");
		sugar().admin.navToAdminPanelLink("workflowManagement");
		dropDownCtrl.click();
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_NEW_WORKFLOW']").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "name").set(testName);
		new VoodooControl("select", "css", "select[name=base_module]").set(sugar().leads.moduleNamePlural);
		new VoodooControl("input", "id", "save_workflow").click();

		// Create a condition "When a field in the target module changes to or from a specified value" for the workflow
		new VoodooControl("a", "id", "NewWorkFlowTriggerShells").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", "body table:nth-child(2) tr td input[type='radio']").click();

		// click to field link to select a target field
		new VoodooControl("a", "css", "#lang_compare_specific a").click();
		VoodooUtils.focusWindow(2);

		// select "Description" as a target field
		new VoodooControl("select", "css", "#selector").set(customData.get("field_name"));
		new VoodooControl("input", "css", "[name='Save']").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "next").click();
		new VoodooControl("a", "id", "href_future_trigger").click();
		VoodooUtils.focusWindow(2);

		// add description 
		new VoodooControl("input", "id", "future_trigger__field_value").set(customData.get("description"));
		new VoodooControl("input", "id", "save").click();
		VoodooUtils.focusWindow(1);

		// save the condition
		new VoodooControl("input", "id", "save").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");

		// Action "Update fields in the target module" for the workflow		
		new VoodooControl("a", "id", "NewWorkFlowActionShells").click();
		VoodooUtils.focusWindow(1);

		// check " Update fields in the target module " field
		new VoodooControl("input","css", "body table:nth-child(2)  tr td input[type='radio']").click();
		new VoodooControl("input", "id", "step1_next").click();
		VoodooUtils.focusFrame("selectiframe");
		new VoodooControl("input", "id", "mod_field_0").click(); // Tags field display 2 times
		VoodooUtils.focusDefault();
		new VoodooControl("a", "id", "href_0").click();
		VoodooUtils.focusWindow(2);
		new VoodooControl("input", "id", "field_0__field_value").set(customData.get("description"));
		new VoodooControl("input", "css", "[value='Save']").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", "form tbody tr:nth-child(5) input:nth-child(2)").click();
		VoodooUtils.focusWindow(0);

		// navigate to studio module
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "studiolink_Leads").click();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "id", "description").click();
		new VoodooControl("input", "id", "calculated").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("textarea", "css", "#formulaInput").set(customData.get("formula"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Leads
		sugar().leads.navToListView();
		sugar().leads.listView.create();
		sugar().leads.createDrawer.getEditField("firstName").set(sugar().leads.getDefaultData().get("firstName"));
		sugar().leads.createDrawer.getEditField("lastName").set(sugar().leads.getDefaultData().get("lastName"));
		sugar().leads.createDrawer.save();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.showMore();

		// Verify that calculated fields will be calculated according to the formula
		int firstNameLen = sugar().leads.getDefaultData().get("firstName").length();
		sugar().leads.recordView.getDetailField("description").assertContains(Integer.toString(firstNameLen), true);

		// Verify Workflow Definition containing the Workflow Action that mentions that the Action will not be performed.
		sugar().admin.navToAdminPanelLink("workflowManagement");
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.waitForReady();

		// TODO: VOOD-1042
		new VoodooControl("a", "css", "#MassUpdate table tbody tr.oddListRowS1 td:nth-child(3) a").click();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.waitForReady();
		new VoodooControl("span", "css" , ".listViewTdToolsS1 span img").click();
		new VoodooControl("span", "css", "table.list.view tr:nth-child(3) span").assertEquals(customData.get("error_message"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}