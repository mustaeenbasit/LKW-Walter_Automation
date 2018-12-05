package com.sugarcrm.test.custommodules;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class CustomModules_27190 extends SugarTest {
	FieldSet moduleData;
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * "Find Duplicate" action is in the record view of custom person type module
	 * @throws Exception
	 */
	@Test
	public void CustomModules_27190_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");			
		
		moduleData = testData.get(testName).get(0);				
		sugar().admin.navToAdminPanelLink("moduleBuilder");
		VoodooUtils.focusFrame("bwc-frame");
		
		//TODO: VOOD-933 requests for Library support for new VoodooControls defined from line 30-68 and 77-91
		new VoodooControl("a", "css" ,"td#newPackage a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css" ,"table.mbTable input[name='name']").set(moduleData.get("pack_name"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='key']").set(moduleData.get("key"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='savebtn']").click();		
		VoodooUtils.waitForAlertExpiration();
		
		new VoodooControl("a", "css" ,"table#new_module a").click();	
		VoodooUtils.waitForReady();
		
		new VoodooControl("input", "css" ,"table.mbTable input[name='name']").set(moduleData.get("mod_name"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='label']").set(moduleData.get("label"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='label_singular']").set(moduleData.get("singular_label"));
		new VoodooControl("table", "css" ,"tr#factory_modules table#type_person").click();		
		new VoodooControl("input", "css" ,"table.mbTable input[name='savebtn']").click();
		VoodooUtils.waitForAlertExpiration();
		
		new VoodooControl("a", "css" ,"div.bodywrapper div a:nth-of-type(4)").click();
		new VoodooControl("input", "css" ,"table.mbTable input[name='deploybtn']").waitForVisible();
		new VoodooControl("input", "css" ,"table.mbTable input[name='deploybtn']").click();
		VoodooUtils.pause(2000); // Wait for deploy image to appear
		// TODO: VOOD-1010
		new VoodooControl("img", "css", ".bodywrapper img[align='absmiddle']").waitForInvisible(120000);
		VoodooUtils.waitForAlertExpiration();
			
		VoodooUtils.focusDefault();
		
		sugar().navbar.navToModule(moduleData.get("data_module"));
		
		new VoodooControl("a", "css", ".btn.btn-primary[name='create_button']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[name=last_name]").set(moduleData.get("last_name"));
		new VoodooControl("a", "css", ".layout_sugar_crm .create.fld_save_button .btn.btn-primary[name='save_button']").click();
		VoodooUtils.waitForAlertExpiration();
		
		new VoodooControl("a", "css", "div.flex-list-view-content table > tbody > tr > td:nth-child(2) > span > div > a").waitForVisible();
		new VoodooControl("a", "css", "div.flex-list-view-content table > tbody > tr > td:nth-child(2) > span > div > a").click();
		new VoodooControl("a", "css", "span.actions.btn-group.detail a.btn.dropdown-toggle.btn-primary").waitForVisible();
		new VoodooControl("a", "css", "span.actions.btn-group.detail a.btn.dropdown-toggle.btn-primary").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "a[name='find_duplicates_button']").waitForVisible();
		new VoodooControl("a", "css", "a[name='find_duplicates_button']").assertEquals("Find Duplicates", true);	
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}