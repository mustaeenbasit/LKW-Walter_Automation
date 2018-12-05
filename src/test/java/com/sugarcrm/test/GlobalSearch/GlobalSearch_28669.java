package com.sugarcrm.test.GlobalSearch;

import java.util.ArrayList;

import org.junit.Test;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class GlobalSearch_28669 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify global search bar is expanded.
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28669_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		ArrayList<Module> modules = new ArrayList<Module>();
		modules.add(sugar().accounts);
		modules.add(sugar().contacts);
		modules.add(sugar().opportunities);
		modules.add(sugar().leads);
		modules.add(sugar().calendar);
		modules.add(sugar().reports);
		modules.add(sugar().quotes);
		modules.add(sugar().documents);
		modules.add(sugar().emails);

		// Verify multiple modules and more module caret icon on navigation bar
		for (Module module: modules) {
			// TODO: VOOD-784
			new VoodooControl("li", "css", ".nav.megamenu[data-container=module-list] li[data-module="+module.moduleNamePlural+"] span.btn-group").assertVisible(true);
		}
		VoodooControl moreModuleCaretCtrl = sugar().navbar.getControl("showAllModules");
		moreModuleCaretCtrl.assertVisible(true);

		try {
			// Verify global search bar, it will expand over the mega menu when focussed.
			sugar().navbar.getControl("globalSearch").click();

			// TODO: VOOD-1849
			new VoodooControl("div", "css", ".search.expanded").assertVisible(true);
			moreModuleCaretCtrl.assertVisible(true);

			// Verify user can view the first module in the mega menu, including the "more module" list carat when bar is expanded,.
			for (Module module: modules) {
				// TODO: VOOD-784
				VoodooControl navModuleCtrl = new VoodooControl("li", "css", ".nav.megamenu[data-container=module-list] li[data-module="+module.moduleNamePlural+"] span.btn-group");
				if (module.equals(sugar().accounts))
					navModuleCtrl.assertVisible(true);
				else
					navModuleCtrl.assertVisible(false);
			}
		}
		finally {
			// Better to cancel global search
			sugar().navbar.search.getControl("cancelSearch").click();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}