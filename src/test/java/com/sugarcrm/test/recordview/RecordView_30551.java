package com.sugarcrm.test.recordview;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.sugar.modules.StandardModule;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class RecordView_30551 extends SugarTest {
	ArrayList<Module> enableDisablemodules = new ArrayList<Module>();

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().bugs.api.create();
		sugar().cases.api.create();		
		sugar().contacts.api.create();
		sugar().leads.api.create();
		sugar().opportunities.api.create();
		sugar().productCatalog.api.create();
		sugar().revLineItems.api.create();
		sugar().quotedLineItems.api.create();

		sugar().login();

		// Enable Bugs and QLI module
		enableDisablemodules.add(sugar().bugs);
		enableDisablemodules.add(sugar().quotedLineItems);
		sugar().admin.enableModuleDisplayViaJs(enableDisablemodules);
	}

	/**
	 * Verify that "View change log" button is available for the existing modules intentionally 
	 * @throws Exception
	 */
	@Test
	public void RecordView_30551_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		ArrayList<StandardModule> modules = new ArrayList<StandardModule>();
		modules.add(sugar().accounts);
		modules.add(sugar().bugs);
		modules.add(sugar().cases);
		modules.add(sugar().contacts);
		modules.add(sugar().leads);
		modules.add(sugar().opportunities);
		modules.add(sugar().productCatalog);
		modules.add(sugar().revLineItems);
		modules.add(sugar().quotedLineItems);

		// Verify view change Log option for modules
		for (StandardModule module: modules) {
			verifyViewChangeLog(module);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	private void verifyViewChangeLog(StandardModule module) throws Exception {
		module.navToListView();
		module.listView.clickRecord(1);
		module.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-695, VOOD-738
		VoodooControl viewChangeLog = new VoodooControl("a","css","ul.dropdown-menu span[data-voodoo-name='audit_button'] a"); 

		// View change log will not be displayed for Product Catalog and appears for others (given) module
		if (module.equals(sugar().productCatalog))
			viewChangeLog.assertVisible(false);
		else
			viewChangeLog.assertVisible(true);

		module.recordView.getControl("primaryButtonDropdown").click(); // Better to close primary button dropdown
	}

	public void cleanup() throws Exception {}
}