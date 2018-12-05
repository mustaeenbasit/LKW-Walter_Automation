package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_30247 extends SugarTest {
	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that KB doesn't have export or import functions 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30247_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		VoodooControl kbModuleMenuItemsCtrl = new VoodooControl("div", "css", "[data-module='KBContents'] .dropdown-menu.scroll");
		VoodooControl kbListViewActionDropdownCtrl = new VoodooControl("ul", "css", ".actionmenu.list.btn-group.open .dropdown-menu");
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// Navigate to import Wizard page
		sugar().admin.adminTools.getControl("importWizard").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1172
		// Verifying KnowledgeBase is not listed in import wizard module list
		new VoodooControl("select", "id", "admin_import_module").assertContains(customData.get("moduleName"), false);
		VoodooUtils.focusDefault();
		
		sugar().knowledgeBase.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().knowledgeBase);

		// TODO: VOOD-771
		// Verifying Import is not exist in KB module dropdown
		kbModuleMenuItemsCtrl.assertContains(customData.get("import"), false);

		sugar().knowledgeBase.listView.checkRecord(1);
		sugar().knowledgeBase.listView.openActionDropdown();

		// TODO: VOOD-689
		// Verifying Export does not exist in Action dropdown in listview of KB
		kbListViewActionDropdownCtrl.assertContains(customData.get("export"), false);
		sugar().logout();
		
		// Login as qauser
		sugar().login(sugar().users.getQAUser());
		sugar().knowledgeBase.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().knowledgeBase);

		// TODO: VOOD-771
		// Verifying Import is not exist in KB module dropdown
		kbModuleMenuItemsCtrl.assertContains(customData.get("import"), false);

		sugar().knowledgeBase.listView.checkRecord(1);
		sugar().knowledgeBase.listView.openActionDropdown();

		// TODO: VOOD-689
		// Verifying Export does not exist in Action dropdown in listview of KB
		kbListViewActionDropdownCtrl.assertContains(customData.get("export"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}