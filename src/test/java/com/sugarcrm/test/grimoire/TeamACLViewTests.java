package com.sugarcrm.test.grimoire;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.test.SugarTest;

import org.junit.Test;

import java.util.HashMap;

/**
 * Contains self tests different TeamACLView class methods in SugarCRM.
 *
 * @author Mazen Louis <mlouis@sugarcrm.com></mlouis@sugarcrm.com>
 */
public class TeamACLViewTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void setCheckboxByModuleTest() throws Exception {
		VoodooUtils.voodoo.log.info("Running setCheckboxByModuleTest()...");

		sugar().admin.navToAdminPanelLink("teamsPermissions");
		sugar().teams.teamsAcl.setTeamsAcl(true);
		sugar().teams.teamsAcl.setModuleCheckbox(sugar().accounts, false);

		VoodooUtils.focusFrame("bwc-frame");
		sugar().teams.teamsAcl.getModuleCheckbox(sugar().accounts).assertChecked(false);

		VoodooUtils.voodoo.log.info("setCheckboxByModuleTest() complete.");
	}

	@Test
	public void setCheckboxByStringTest() throws Exception {
		VoodooUtils.voodoo.log.info("Running setCheckboxByStringTest()...");

		sugar().admin.navToAdminPanelLink("teamsPermissions");
		sugar().teams.teamsAcl.setTeamsAcl(true);
		sugar().teams.teamsAcl.setModuleCheckbox(sugar().accounts.moduleNamePlural, false);

		VoodooUtils.focusFrame("bwc-frame");
		sugar().teams.teamsAcl.getModuleCheckbox(sugar().accounts.moduleNamePlural).assertChecked(false);

		VoodooUtils.voodoo.log.info("setCheckboxByStringTest() complete.");
	}

	@Test
	public void setTeamsAclTest() throws Exception {
		VoodooUtils.voodoo.log.info("Running setTeamsAclTest()...");

		sugar().admin.navToAdminPanelLink("teamsPermissions");
		sugar().teams.teamsAcl.setTeamsAcl(true);

		VoodooUtils.focusFrame("bwc-frame");
		sugar().teams.teamsAcl.getControl("enableDisableButton").assertChecked(true);

		VoodooUtils.voodoo.log.info("setTeamsAclTest() complete.");
	}

	@Test
	public void setModulePermissionsTest() throws Exception {
		VoodooUtils.voodoo.log.info("Running setModulePermissionsTest()...");

		sugar().admin.navToAdminPanelLink("teamsPermissions");
		sugar().teams.teamsAcl.setTeamsAcl(true);

		HashMap<Module, Boolean> modules = new HashMap<>();
		modules.put(sugar().contacts, false);
		modules.put(sugar().leads, false);
		modules.put(sugar().bugs, true);
		sugar().teams.teamsAcl.setModulePermissions(modules);

		VoodooUtils.focusFrame("bwc-frame");
		for(Module tbACLModule : modules.keySet()) {
			sugar().teams.teamsAcl.getModuleCheckbox(tbACLModule.moduleNamePlural).assertChecked(modules.get(tbACLModule));
		}

		VoodooUtils.voodoo.log.info("setModulePermissionsTest() complete.");
	}

	@Test
	public void enableDisableTeamsAclTest() throws Exception {
		VoodooUtils.voodoo.log.info("Running enableDisableTeamsAclTest()...");

		// Enable Team Based Permissions
		sugar().teams.enableTeamAcl();

		// Verify teams based permissions are enabled
		sugar().admin.navToAdminPanelLink("teamsPermissions");
		VoodooUtils.focusFrame("bwc-frame");
		sugar().teams.teamsAcl.getControl("enableDisableButton").assertChecked(true);
		VoodooUtils.focusDefault();

		// Disable Team Based Permissions
		sugar().teams.disableTeamAcl();

		// Verify teams based permissions are disbaled
		sugar().admin.navToAdminPanelLink("teamsPermissions");
		VoodooUtils.focusFrame("bwc-frame");
		sugar().teams.teamsAcl.getControl("enableDisableButton").assertChecked(false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("enableDisableTeamsAclTest() complete.");
	}

	@Test
	public void setupTeamAclTest() throws Exception {
		VoodooUtils.voodoo.log.info("Running setupTeamAclTest()...");

		HashMap<Module, Boolean> tbACL = new HashMap<>();
		tbACL.put(sugar.accounts, false);
		tbACL.put(sugar.contacts, false);
		tbACL.put(sugar.calls, true);
		tbACL.put(sugar.leads, false);

		sugar().teams.setupTeamAcl(tbACL);

		sugar().admin.navToAdminPanelLink("teamsPermissions");
		VoodooUtils.focusFrame("bwc-frame");
		for(Module currentModule : tbACL.keySet()){
			sugar().teams.teamsAcl.getModuleCheckbox(currentModule).assertChecked(tbACL.get(currentModule));
		}

		VoodooUtils.voodoo.log.info("setupTeamAclTest() complete.");
	}

	public void cleanup() throws Exception {}
}