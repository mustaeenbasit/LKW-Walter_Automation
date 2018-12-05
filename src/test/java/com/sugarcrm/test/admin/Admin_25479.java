package com.sugarcrm.test.admin;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.test.SugarTest;

public class Admin_25479 extends SugarTest {
	ArrayList<Module> modulesName = new ArrayList<Module>();

	public void setup() throws Exception {
		// add elements to the array list
		modulesName.add(sugar().revLineItems);
		modulesName.add(sugar().notes);
		modulesName.add(sugar().tasks);
		modulesName.add(sugar().meetings);
		modulesName.add(sugar().calls);
		modulesName.add(sugar().emails);
		modulesName.add(sugar().documents);
		modulesName.add(sugar().leads);
		modulesName.add(sugar().opportunities);
		modulesName.add(sugar().contacts);
		modulesName.add(sugar().accounts);

		// Login as admin user
		sugar().login();
	}

	/**
	 * Verify no error when move all modules to Disabled Columns in "Configure Navigation Bar Quick Create"
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_25479_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to admin -> Configure Navigation Bar Quick Create
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("configurationShortcutBar").click();
		VoodooUtils.waitForReady();

		// Move all modules to "Disabled Columns"
		// TODO: VOOD-1805
		VoodooControl dropHereCtrl = new VoodooControl("tbody", "css", "#disabled_div tbody[tabindex='0']");
		for(int i = 0; i < modulesName.size(); i++) { 	
			new VoodooControl("tr", "xpath", "//*[@id='enabled_div']/div[3]/table/tbody[2]/tr[contains(.,'" + modulesName.get(i).moduleNamePlural + "')]").dragNDrop(dropHereCtrl);
		}

		// Verify, all modules are moved to Disabled Columns (i.e.  Enabled Columns is empty).
		new VoodooControl("div", "css", "#enabled_div .yui-dt-data tr td div").assertContains("", true);

		// Save it
		new VoodooControl("input", "css", "input[value='Save']").click();
		VoodooUtils.focusDefault();
		// Chained wait is needed because the product loads twice after Save the 'Configure Navigation Bar Quick Create' 
		VoodooUtils.waitForReady(40000);
		VoodooUtils.waitForReady(30000);

		// Log in as other valid user again
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Click on + sign to view all available modules in quick create module.
		// TODO: VOOD-1150
		sugar().navbar.openQuickCreateMenu();
		new VoodooControl("li", "css", "#createList > div > div > ul > li").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}