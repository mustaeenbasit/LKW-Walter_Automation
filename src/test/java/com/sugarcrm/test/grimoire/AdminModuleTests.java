package com.sugarcrm.test.grimoire;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.test.SugarTest;

/**
 * Test class for Admin Module Opportunity View switch
 */
public class AdminModuleTests extends SugarTest {
	VoodooControl displayModule, hiddenModule, displaySubpanel, hiddenSubpanel;
	String bugsModuleDisplayName = "";
	String projectsModuleDisplayName = "";

	public void setup() throws Exception {
		displayModule = sugar().admin.configureTabs.getControl("displayedModules");
		hiddenModule = sugar().admin.configureTabs.getControl("hiddenModules");
		displaySubpanel = sugar().admin.configureTabs.getControl("displayedSubpanels");
		hiddenSubpanel = sugar().admin.configureTabs.getControl("hiddenSubpanels");
		bugsModuleDisplayName = sugar().bugs.moduleNamePlural;
		projectsModuleDisplayName = sugar().projects.moduleNamePlural;
		sugar().login();
	}

	@Test
	public void enableDisableModuleDisplayViaJs() throws Exception {
		VoodooUtils.voodoo.log.info("Running enableDisableModuleDisplayViaJs()...");

		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
		VoodooUtils.focusFrame("bwc-frame");
		displayModule.assertContains(bugsModuleDisplayName, true);
		hiddenModule.assertContains(bugsModuleDisplayName, false);
		displaySubpanel.assertContains(bugsModuleDisplayName, false);
		hiddenSubpanel.assertContains(bugsModuleDisplayName, true);
		VoodooUtils.focusDefault();

		sugar().admin.disableModuleDisplayViaJs(sugar().bugs);
		VoodooUtils.focusFrame("bwc-frame");
		displayModule.assertContains(bugsModuleDisplayName, false);
		hiddenModule.assertContains(bugsModuleDisplayName, true);
		displaySubpanel.assertContains(bugsModuleDisplayName, false);
		hiddenSubpanel.assertContains(bugsModuleDisplayName, true);
		VoodooUtils.focusDefault();

		ArrayList<Module> toTest = new ArrayList<Module>();
		toTest.add(sugar().projects);
		toTest.add(sugar().contracts);
		String contractsModuleName = sugar().contracts.moduleNamePlural;

		sugar().admin.enableModuleDisplayViaJs(toTest);
		VoodooUtils.focusFrame("bwc-frame");
		displayModule.assertContains(projectsModuleDisplayName, true);
		hiddenModule.assertContains(projectsModuleDisplayName, false);
		displaySubpanel.assertContains(projectsModuleDisplayName, false);
		hiddenSubpanel.assertContains(projectsModuleDisplayName, true);
		displayModule.assertContains(contractsModuleName, true);
		hiddenModule.assertContains(contractsModuleName, false);
		displaySubpanel.assertContains(contractsModuleName, false);
		hiddenSubpanel.assertContains(contractsModuleName, true);
		VoodooUtils.focusDefault();

		sugar().admin.disableModuleDisplayViaJs(toTest);
		VoodooUtils.focusFrame("bwc-frame");
		displayModule.assertContains(projectsModuleDisplayName, false);
		hiddenModule.assertContains(projectsModuleDisplayName, true);
		displaySubpanel.assertContains(projectsModuleDisplayName, false);
		hiddenSubpanel.assertContains(projectsModuleDisplayName, true);
		displayModule.assertContains(contractsModuleName, false);
		hiddenModule.assertContains(contractsModuleName, true);
		displaySubpanel.assertContains(contractsModuleName, false);
		hiddenSubpanel.assertContains(contractsModuleName, true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("enableDisableModuleDisplayViaJs() complete.");
	}

	@Test
	public void enableDisableSubpanelDisplayViaJs() throws Exception {
		VoodooUtils.voodoo.log.info("Running enableDisableSubpanelDisplayViaJs()...");

		sugar().admin.enableSubpanelDisplayViaJs(sugar().projects);
		VoodooUtils.focusFrame("bwc-frame");
		displayModule.assertContains(projectsModuleDisplayName, false);
		hiddenModule.assertContains(projectsModuleDisplayName, true);
		displaySubpanel.assertContains(projectsModuleDisplayName, true);
		hiddenSubpanel.assertContains(projectsModuleDisplayName, false);
		VoodooUtils.focusDefault();

		sugar().admin.disableSubpanelDisplayViaJs(sugar().projects);
		VoodooUtils.focusFrame("bwc-frame");
		displayModule.assertContains(projectsModuleDisplayName, false);
		hiddenModule.assertContains(projectsModuleDisplayName, true);
		displaySubpanel.assertContains(projectsModuleDisplayName, false);
		hiddenSubpanel.assertContains(projectsModuleDisplayName, true);
		VoodooUtils.focusDefault();

		ArrayList<Module> toTest = new ArrayList<Module>();
		toTest.add(sugar().quotedLineItems);
		toTest.add(sugar().bugs);

		sugar().admin.enableSubpanelDisplayViaJs(toTest);
		VoodooUtils.focusFrame("bwc-frame");
		displayModule.assertContains("Quoted Line Items", false);
		hiddenModule.assertContains("Quoted Line Items", true);
		displaySubpanel.assertContains("Quoted Line Items", true);
		hiddenSubpanel.assertContains("Quoted Line Items", false);
		displayModule.assertContains(bugsModuleDisplayName, false);
		hiddenModule.assertContains(bugsModuleDisplayName, true);
		displaySubpanel.assertContains(bugsModuleDisplayName, true);
		hiddenSubpanel.assertContains(bugsModuleDisplayName, false);
		VoodooUtils.focusDefault();

		sugar().admin.disableSubpanelDisplayViaJs(toTest);
		VoodooUtils.focusFrame("bwc-frame");
		displayModule.assertContains("Quoted Line Items", false);
		hiddenModule.assertContains("Quoted Line Items", true);
		displaySubpanel.assertContains("Quoted Line Items", false);
		hiddenSubpanel.assertContains("Quoted Line Items", true);
		displayModule.assertContains(bugsModuleDisplayName, false);
		hiddenModule.assertContains(bugsModuleDisplayName, true);
		displaySubpanel.assertContains(bugsModuleDisplayName, false);
		hiddenSubpanel.assertContains(bugsModuleDisplayName, true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("enableDisableSubpanelDisplayViaJs() complete.");
	}

	public void cleanup() throws Exception {}
}

