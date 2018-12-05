package com.sugarcrm.test.subpanels;
import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.test.SugarTest;

public class Subpanels_28203 extends SugarTest {

	ArrayList<Module> modules;
	VoodooControl accountsStudioCtrl;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * To verify subpanel should be display if it is available on a module where subpanel is hidden
	 * @throws Exception
	 */
	@Test
	public void Subpanels_28203_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-938
		accountsStudioCtrl = new VoodooControl("a", "id", "studiolink_Accounts");

		// Click on Accounts module
		accountsStudioCtrl.click(); 
		VoodooUtils.waitForReady();

		// Create a one-to-many relationship between Accounts and Campaigns
		new VoodooControl("td", "id", "relationshipsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='addrelbtn']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("option", "css", "select#relationship_type_field option[value='one-to-many']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("option", "css", "select#rhs_mod_field option[value='Campaigns']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[name=saverelbtn]").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Disabling accounts and target list subpanels
		modules = new ArrayList<Module>();
		modules.add(sugar().accounts);
		modules.add(sugar().targetlists);
		sugar().admin.disableSubpanelDisplayViaJs(modules);
		VoodooUtils.focusFrame("bwc-frame");

		// Campaigns subpanel should be in the available subpanel list
		// TODO: VOOD-1373
		new VoodooControl("div", "css", "#enabled_subpanels_div").assertContains(sugar().campaigns.moduleNamePlural, true);
		VoodooUtils.voodoo.log.info(testName + " complete.");
	} 

	public void cleanup() throws Exception {}
}