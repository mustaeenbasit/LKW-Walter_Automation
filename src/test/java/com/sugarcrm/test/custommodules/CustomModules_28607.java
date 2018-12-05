package com.sugarcrm.test.custommodules;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class CustomModules_28607 extends SugarTest {
	FieldSet moduleData = new FieldSet();
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that the mega menu is displayed properly after deploying a custom module
	 * 
	 * @throws Exception
	 */
	@Test
	public void CustomModules_28607_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");			
		
		moduleData = testData.get(testName).get(0);		
		
		sugar().admin.navToAdminPanelLink("moduleBuilder");
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-933 - Library support needed for controls on Admin Module Builder and Module Loader
		new VoodooControl("a", "css" ,"td#newPackage a").click();
		new VoodooControl("input", "css" ,"table.mbTable input[name='name']").set(moduleData.get("pack_name"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='key']").set(moduleData.get("key"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='savebtn']").click();		
		VoodooUtils.waitForReady(30000);
		
		new VoodooControl("a", "css" ,"table#new_module a").click();	
		VoodooUtils.waitForReady();
		
		new VoodooControl("input", "css" ,"table.mbTable input[name='name']").set(moduleData.get("mod_name"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='label']").set(moduleData.get("label"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='label_singular']").set(moduleData.get("singular_label"));
		new VoodooControl("table", "css" ,"tr#factory_modules table#type_person").click();		
		new VoodooControl("input", "css" ,"table.mbTable input[name='savebtn']").click();
		VoodooUtils.waitForReady(30000);
		
		new VoodooControl("a", "css" ,"div.bodywrapper div a:nth-of-type(4)").click();
		new VoodooControl("input", "css" ,"table.mbTable input[name='deploybtn']").waitForVisible();
		new VoodooControl("input", "css" ,"table.mbTable input[name='deploybtn']").click();
		VoodooUtils.waitForReady(30000);
		new VoodooControl("img", "css", "div.bodywrapper img[align='absmiddle']").waitForInvisible(60000);
		VoodooUtils.waitForReady(30000);

		VoodooUtils.focusDefault();

		// Verify that Custom Module appears either in Navbar or in overflow menu
		VoodooControl topNavLink = new VoodooControl("li", "css", "ul.nav.megamenu li[data-module='" + moduleData.get("data_module") + "'] a");
		if(!(topNavLink.queryVisible())) {
			// If the link in the top nav is hidden, use the overflow menu instead.
			new VoodooControl("li", "css", "ul.nav.megamenu div[role='menu'].dropdown-menu li[data-module='" + moduleData.get("data_module") + "']").assertExists(true);
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
