package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29563 extends SugarTest {

	public void setup() throws Exception {
		// There has at least one Process Definitions, Process Email Templates and Process Business Rules exist.
		sugar().processDefinitions.api.create();
		FieldSet fs = new FieldSet();
		fs.put("targetModule", sugar().contacts.moduleNamePlural);
		sugar().processBusinessRules.api.create(fs);
		fs.clear();
		fs.put("targetModule", sugar().accounts.moduleNamePlural);
		sugar().processEmailTemplates.api.create(fs);
		sugar().login();

		// Enable Process Definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();
	}

	/**
	 * Verify modules name should list in the Process Author dashlets
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29563_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet dashletData = testData.get(testName).get(0);

		// Create a Dashboard and Add Process Definitions, Process Email Templates and Process Business Rules dashlets.
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		sugar().home.dashboard.clickCreate();
		sugar().home.dashboard.getControl("title").set(testName);
		sugar().home.dashboard.addRow();
		sugar().home.dashboard.addDashlet(1, 1);

		// TODO: VOOD-960 - Dashlet Selection
		VoodooControl dashletName = new VoodooControl("input", "css", ".inline-drawer-header input");
		VoodooControl dashletSelect = new VoodooControl("a", "css", ".fld_title a");
		VoodooControl saveBtnCtrl = new VoodooControl("a", "css", ".active .fld_save_button a");
		dashletName.set(dashletData.get("processDefinitionDashlet"));
		VoodooUtils.waitForReady();
		dashletSelect.click();
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Process Email Templates
		sugar().home.dashboard.addRow();
		sugar().home.dashboard.addDashlet(2, 1);
		dashletName.set(dashletData.get("processEmailDashlet"));
		VoodooUtils.waitForReady();
		dashletSelect.click();
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Process Business Rules
		sugar().home.dashboard.addRow();
		sugar().home.dashboard.addDashlet(3, 1);
		dashletName.set(dashletData.get("businessRuleDashlet"));
		VoodooUtils.waitForReady();
		dashletSelect.click();
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Save the dashboard
		sugar().home.dashboard.save();

		// Verify that the Target Modules name should be displayed in the Process Author dashlets.
		new VoodooControl("div", "css", "[data-voodoo-name='dashlet-processes'] .tab-pane.active .details").assertContains(sugar().leads.moduleNamePlural, true);
		new VoodooControl("div", "css", "[data-voodoo-name='dashlet-email'] .tab-pane.active .details").assertContains(sugar().accounts.moduleNamePlural, true);
		new VoodooControl("div", "css", "[data-voodoo-name='dashlet-businessrules'] .tab-pane.active .details").assertContains(sugar().contacts.moduleNamePlural, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}