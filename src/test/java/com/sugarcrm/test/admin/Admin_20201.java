package com.sugarcrm.test.admin;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

import java.util.ArrayList;

public class Admin_20201 extends SugarTest {
	ArrayList<Module> mods;
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * The Target Lists,Targets, Calls, Meetings, Notes, and Tasks tabs can be deactivated. 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20201_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		mods = new ArrayList<Module>();
		mods.add(sugar().tasks);
		mods.add(sugar().calls);
		mods.add(sugar().meetings);
		mods.add(sugar().notes);
		mods.add(sugar().targets);
		mods.add(sugar().targetlists);
			
		sugar().admin.disableModuleDisplayViaJs(mods);
		sugar().navbar.showAllModules();
		
		// TODO VOOD-784
		for(int i=0;i<mods.size();i++) {
			new VoodooControl("li", "css", "div.module-list li[data-module='"+mods.get(i).moduleNamePlural+"']").assertExists(false);	
		}
		new VoodooControl("li", "css", "div.module-list li[data-module='"+sugar().targetlists.moduleNamePlural+"']").assertExists(false);
		new VoodooControl("li", "css", "div.module-list li[data-module='"+sugar().targets.moduleNamePlural+"']").assertExists(false);
				
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
