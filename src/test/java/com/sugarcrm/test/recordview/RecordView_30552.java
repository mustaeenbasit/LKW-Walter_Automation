package com.sugarcrm.test.recordview;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.StandardModule;
import com.sugarcrm.test.SugarTest;

public class RecordView_30552 extends SugarTest {
	VoodooControl viewChangeLog;

	public void setup() throws Exception {
		// Creating modules
		sugar().targetlists.api.create();
		sugar().targets.api.create();
		sugar().notes.api.create();		
		sugar().calls.api.create();
		sugar().meetings.api.create();
		sugar().productCategories.api.create();
		sugar().processBusinessRules.api.create();
		sugar().processEmailTemplates.api.create();

		// Login 
		sugar().login();

		// Creating Process definition 
		// Import Process Definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + "_7700.bpm");
		sugar().processDefinitions.navToListView();

		// Enable Process Definition
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Creating Account record so that process can trigger
		sugar().accounts.create();

		// Setting Forecast setting so that navigate to forecast worksheet
		// Enable default Forecast settings
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();

		// Creating Web Logic
		// TODO: VOOD-816
		sugar().navbar.navToAdminTools();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("webLogicHook").click();
		VoodooUtils.focusDefault();
		new VoodooControl("span", "css", ".list-headerpane.fld_create_button").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", ".fld_name.edit input").set(testName);
		new VoodooControl("input", "css", ".fld_url.edit input").set(testName);
		new VoodooControl("span", "css", ".create.fld_save_button").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify that "View change log" button is unavailable for the existing modules intentonally
	 * @throws Exception
	 */
	@Test
	public void RecordView_30552_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		ArrayList<StandardModule> modules = new ArrayList<StandardModule>();
		modules.add(sugar().targetlists);
		modules.add(sugar().targets);
		modules.add(sugar().notes);
		modules.add(sugar().calls);
		modules.add(sugar().meetings);
		modules.add(sugar().productCategories);
		modules.add(sugar().processDefinitions);
		modules.add(sugar().processes);
		modules.add(sugar().processBusinessRules);
		modules.add(sugar().processEmailTemplates);

		// TODO: VOOD-695, VOOD-738
		// View Change Log control in record view action drop down
		viewChangeLog = new VoodooControl("a","css","ul.dropdown-menu span[data-voodoo-name='audit_button'] a");
		
		// Verify view change Log option is unavailable for modules
		for (StandardModule module: modules) {
			verifyViewChangeLog(module);
		}

		// Verifying View change Log is not available for forecast worksheet
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.worksheet.getControl("actionDropdown").click();
		viewChangeLog.assertVisible(false);

		// Verifying View change Log is not available for Web Logic
		sugar().navbar.navToAdminTools();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("webLogicHook").click();
		VoodooUtils.focusDefault();
		
		// TODO: VOOD-816
		new VoodooControl("a", "css", ".list.fld_name a").click();
		VoodooUtils.waitForReady();
		viewChangeLog.assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	private void verifyViewChangeLog(StandardModule module) throws Exception {
		module.navToListView();
		if (module.equals(sugar().processes)) {
			// Verifying View change Log is not available for Process management
			sugar().processes.myProcessesListView.showProcess(1);
			sugar().processes.recordView.openPrimaryButtonDropdown();
			viewChangeLog.assertVisible(false);
		}
		else {
			// Verifying View change Log is not available for modules
			module.listView.clickRecord(1);
			module.recordView.openPrimaryButtonDropdown();
			viewChangeLog.assertVisible(false);

			// Better to close primary button dropdown
			module.recordView.getControl("primaryButtonDropdown").click();
		}
	}

	public void cleanup() throws Exception {}
}