package com.sugarcrm.test.admin;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_25133 extends SugarTest {
	DataSource text;
	
	public void setup() throws Exception {
		text = testData.get(testName);
		sugar().login();
	}

	/**
	 * Quick create menu can display all modules
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_25133_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1150
		new VoodooControl("a", "id", "config_prod_bar").click();
		VoodooUtils.waitForReady();
		
		// Move all modules from right to left (i.e. enable all the modules). 
		ArrayList<String> moduleList= new ArrayList<>();
 		for(int i=1; i<6; i++){
 			String module = new VoodooControl("div", "css", "#disabled_div .yui-dt-data tr:nth-child(1) td:nth-child(1) div").getText();
			module = module.substring(0, module.length()-1);
			moduleList.add(module);
			new VoodooControl("tr", "css", "#disabled_div .yui-dt-data tr:nth-child(1)")
							.dragNDrop(new VoodooControl("tr", "css", "#enabled_div .yui-dt-data tr:nth-child(1)"));
		}
 		 
 		Collections.reverse(moduleList);
		new VoodooControl("input", "css", "[value='Save']").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.pause(15000); // pause need to save the changes.
		VoodooUtils.focusDefault();
		
		// Verify, In quick create menu, all modules are ordering as what we put in Admin-> Configure Navigation Bar Quick Create.
		sugar().navbar.openQuickCreateMenu();
		for(int i=1; i<6; i++){
			new VoodooControl("a", "css", "#createList div div ul li:nth-child("+i+") span a").assertContains(text.get(i-1).get("text")+" "+moduleList.get(i-1), true);
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
