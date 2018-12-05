package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29948 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();

		// Login
		sugar().login();

		// Enable Bugs sub-panel
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);

		// Import a Process with: 
		// Target Module - Cases (applies to New Records Only),
		// Receive Message element criteria: Bugs -> Status = Closed,
		// Action type Change Field -> set the Status value as Closed and Add End Event
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");

		// Enable Imported Process Definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();
	}

	/**
	 * Verify that a change in a field of a related module does change the value of a field of the target module
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29948_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet statusData = testData.get(testName).get(0);

		// Create a new Case with Status = New
		sugar().cases.navToListView();
		sugar().cases.listView.create();
		sugar().cases.createDrawer.showMore();
		sugar().cases.createDrawer.getEditField("name").set(testName);
		sugar().cases.createDrawer.getEditField("status").set(statusData.get("newStatus"));
		sugar().cases.createDrawer.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().cases.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		// Create a new Bug with Status = New. Link this bug to the Case
		sugar().cases.listView.clickRecord(1);
		StandardSubpanel bugsSubpanelCtrl = sugar().cases.recordView.subpanels.get(sugar().bugs.moduleNamePlural);
		bugsSubpanelCtrl.scrollIntoViewIfNeeded(false);
		bugsSubpanelCtrl.addRecord();
		sugar().bugs.createDrawer.getEditField("name").set(testName + "_" + sugar().bugs.moduleNamePlural);
		sugar().bugs.createDrawer.getEditField("status").set(statusData.get("newStatus"));
		sugar().bugs.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		// Change the status of the Bug to Closed
		bugsSubpanelCtrl.scrollIntoViewIfNeeded(false);
		bugsSubpanelCtrl.editRecord(1);
		// TODO: VOOD-1425 
		new VoodooSelect("a", "css", ".fld_status.edit a").set(statusData.get("closedStatus"));
		bugsSubpanelCtrl.saveAction(1);

		// Verify that the Status of the Case changes to Closed
		sugar().cases.recordView.getDetailField("status").assertEquals(statusData.get("closedStatus"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	public void cleanup() throws Exception {}
}