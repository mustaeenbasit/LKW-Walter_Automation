package com.sugarcrm.test.grimoire;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

/**
 * Contains self tests for enable disable modules and subpanels
 * 
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class EnableDisableModuleSubpanelViaJS extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void verifyEnableModule() throws Exception {
		VoodooUtils.voodoo.log.info("Running  verifyEnableModule()...");

		VoodooControl showAllModules = sugar().navbar.getControl("showAllModules");
		sugar().admin.enableModuleDisplayViaJs(sugar().contracts);
		showAllModules.click();

		// TODO: VOOD-784
		new VoodooControl("li","css","ul.nav.megamenu div[role='menu'].dropdown-menu li[data-module='Contracts']").assertVisible(true);
		showAllModules.click();

		VoodooUtils.voodoo.log.info("verifyEnableModule() complete.");
	}

	@Test
	public void verifyDisableModule() throws Exception {
		VoodooUtils.voodoo.log.info("Running  verifyDisableModule()...");

		VoodooControl showAllModules = sugar().navbar.getControl("showAllModules");
		sugar().admin.disableModuleDisplayViaJs(sugar().leads);

		// TODO: VOOD-784
		new VoodooControl("li", "css", "ul.nav.megamenu li[data-module='Leads'] a").assertVisible(false);
		showAllModules.click();
		new VoodooControl("li", "css", "ul.nav.megamenu div[role='menu'].dropdown-menu li[data-module='Leads']").assertVisible(false);
		showAllModules.click();

		VoodooUtils.voodoo.log.info("verifyDisableModule() complete.");
	}

	@Test
	public void verifyEnableMultipleModules() throws Exception {
		VoodooUtils.voodoo.log.info("Running  verifyEnableMultipleModules()...");

		VoodooControl showAllModules = sugar().navbar.getControl("showAllModules");
		ArrayList<Module> modules = new ArrayList<Module>();
		modules.add(sugar().bugs);
		modules.add(sugar().contracts);
		sugar().admin.enableModuleDisplayViaJs(modules);
		showAllModules.click();

		// TODO: VOOD-784
		new VoodooControl("li","css","ul.nav.megamenu div[role='menu'].dropdown-menu li[data-module='Bugs']").assertVisible(true);
		new VoodooControl("li","css","ul.nav.megamenu div[role='menu'].dropdown-menu li[data-module='Contracts']").assertVisible(true);
		showAllModules.click();

		VoodooUtils.voodoo.log.info("verifyEnableMultipleModules() complete.");
	}

	@Test
	public void verifyDisableMultipleModules() throws Exception {
		VoodooUtils.voodoo.log.info("Running  verifyDisableMultipleModules()...");

		VoodooControl showAllModules = sugar().navbar.getControl("showAllModules");
		ArrayList<Module> modules = new ArrayList<Module>();
		modules.add(sugar().contacts);
		modules.add(sugar().opportunities);

		sugar().admin.disableModuleDisplayViaJs(modules);
		// TODO: VOOD-784
		new VoodooControl("li","css","ul.nav.megamenu li[data-module='Contacts'] a").assertVisible(false);
		new VoodooControl("li","css","ul.nav.megamenu li[data-module='Opportunities'] a").assertVisible(false);
		showAllModules.click();
		new VoodooControl("li","css","ul.nav.megamenu div[role='menu'].dropdown-menu li[data-module='Contacts']").assertVisible(false);
		new VoodooControl("li","css","ul.nav.megamenu div[role='menu'].dropdown-menu li[data-module='Opportunities']").assertVisible(false);
		showAllModules.click();

		VoodooUtils.voodoo.log.info("verifyDisableMultipleModules() complete.");
	}

	@Test
	public void verifyEnableSubpanel() throws Exception {
		VoodooUtils.voodoo.log.info("Running  verifyEnableSubpanel()...");

		AccountRecord myAcc = (AccountRecord)sugar().accounts.api.create();
		sugar().admin.enableSubpanelDisplayViaJs(sugar().contracts);
		myAcc.navToRecord();
		sugar().accounts.recordView.subpanels.get(sugar().contracts.moduleNamePlural).assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyEnableSubpanel() complete.");
	}

	@Test
	public void verifyDisableSubpanel() throws Exception {
		VoodooUtils.voodoo.log.info("Running  verifyDisableSubpanel()...");

		AccountRecord myAcc = (AccountRecord)sugar().accounts.api.create();
		sugar().admin.disableSubpanelDisplayViaJs(sugar().contacts);
		myAcc.navToRecord();
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).assertVisible(false);

		VoodooUtils.voodoo.log.info("verifyDisableSubpanel() complete.");
	}

	@Test
	public void verifyEnableMultipleSubpanels() throws Exception {
		VoodooUtils.voodoo.log.info("Running  verifyEnableMultipleSubpanels()...");

		AccountRecord myAcc = (AccountRecord)sugar().accounts.api.create();
		ArrayList<Module> modules = new ArrayList<Module>();
		modules.add(sugar().contracts);
		modules.add(sugar().bugs);
		sugar().admin.enableSubpanelDisplayViaJs(modules);
		myAcc.navToRecord();
		sugar().accounts.recordView.subpanels.get(sugar().contracts.moduleNamePlural).assertVisible(true);
		sugar().accounts.recordView.subpanels.get(sugar().bugs.moduleNamePlural).assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyEnableMultipleSubpanels() complete.");
	}

	@Test
	public void verifyDisableMultipleSubpanels() throws Exception {
		VoodooUtils.voodoo.log.info("Running  verifyDisableMultipleSubpanels()...");

		AccountRecord myAcc = (AccountRecord)sugar().accounts.api.create();
		ArrayList<Module> modules = new ArrayList<Module>();
		modules.add(sugar().contacts);
		modules.add(sugar().opportunities);
		sugar().admin.disableSubpanelDisplayViaJs(modules);
		myAcc.navToRecord();
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).assertVisible(false);
		sugar().accounts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural).assertVisible(false);

		VoodooUtils.voodoo.log.info("verifyDisableMultipleSubpanels() complete.");
	}

	public void cleanup() throws Exception {}
}