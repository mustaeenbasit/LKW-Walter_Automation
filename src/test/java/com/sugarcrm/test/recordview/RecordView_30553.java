package com.sugarcrm.test.recordview;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RecordView_30553 extends SugarTest {
	DataSource moduleData = new DataSource();				
	FieldSet packageData = new FieldSet();
	
	public void setup() throws Exception {
		sugar().login();

		moduleData = testData.get(testName + "_modules");				
		packageData = testData.get(testName).get(0);

		sugar().admin.navToAdminPanelLink("moduleBuilder");
		VoodooUtils.focusFrame("bwc-frame");
		
		//TODO: VOOD-933
		new VoodooControl("a", "css" ,"td#newPackage a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css" ,"table.mbTable input[name='name']").set(packageData.get("pack_name"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='key']").set(packageData.get("key"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='savebtn']").click();		
		VoodooUtils.waitForAlertExpiration();

		// Add modules
		for (int i = 0; i < moduleData.size(); i++) {
			new VoodooControl("a", "css" ,"table#new_module a").click();	
			VoodooUtils.waitForReady();
			
			new VoodooControl("input", "css" ,"table.mbTable input[name='name']").set(moduleData.get(i).get("mod_name"));
			new VoodooControl("input", "css" ,"table.mbTable input[name='label']").set(moduleData.get(i).get("label"));
			new VoodooControl("input", "css" ,"table.mbTable input[name='label_singular']").set(moduleData.get(i).get("singular_label"));
			new VoodooControl("table", "css" ,"tr#factory_modules table#type_"+moduleData.get(i).get("mod_name")).click();
			new VoodooControl("input", "css" ,"table.mbTable input[name='savebtn']").click();
			VoodooUtils.waitForAlertExpiration();

			// Goto package menu
			new VoodooControl("a", "xpath", "//a[@class='crumbLink'][contains(.,'"+packageData.get("pack_name")+"')]").click();
			VoodooUtils.waitForReady();
		}
		
		new VoodooControl("input", "css" ,"table.mbTable input[name='deploybtn']").waitForVisible();
		new VoodooControl("input", "css" ,"table.mbTable input[name='deploybtn']").click();
		VoodooUtils.pause(2000); // Wait for deploy image to appear
		// TODO: VOOD-1010
		new VoodooControl("img", "css", ".bodywrapper img[align='absmiddle']").waitForInvisible(120000);
		sugar().alerts.waitForLoadingExpiration();
			
		VoodooUtils.focusDefault();
		
		// Add records to customModules
		for (int i = 0; i < moduleData.size(); i++) {
			sugar().navbar.showAllModules();
			new VoodooControl("li", "css", "ul.nav.megamenu div[role='menu'].dropdown-menu li[data-module='" + packageData.get("key") + "_" + moduleData.get(i).get("mod_name") + "']").click();
			sugar().alerts.waitForLoadingExpiration();

			// Click create
			new VoodooControl("a", "css", ".layout_" + packageData.get("key") + "_" + moduleData.get(i).get("mod_name") + " .fld_create_button a").click();
			sugar().alerts.waitForLoadingExpiration();

			VoodooUtils.focusDefault();
			
			// Input name
			if (moduleData.get(i).get("mod_name").equals("person")) {
				new VoodooControl("input", "css", ".layout_" + packageData.get("key") + "_" + moduleData.get(i).get("mod_name") + 
						" .edit.fld_last_name input").set(moduleData.get(i).get("name"));
			}
			else if (moduleData.get(i).get("mod_name").equals("file")){
				new VoodooControl("input", "css", ".layout_" + packageData.get("key") + "_" + moduleData.get(i).get("mod_name") + 
						" .edit.fld_document_name input").set(moduleData.get(i).get("name"));
			}
			else {
				new VoodooControl("input", "css", ".layout_" + packageData.get("key") + "_" + moduleData.get(i).get("mod_name") + 
						" .edit.fld_name input").set(moduleData.get(i).get("name"));
			}
			
			// Input additional required fields in case of "Sale" module
			if (moduleData.get(i).get("mod_name").equals("sale")) {
				new VoodooControl("input", "css", ".layout_" + packageData.get("key") + "_" + moduleData.get(i).get("mod_name") + 
						" .edit.fld_amount > div > input").set(moduleData.get(i).get("amount"));
				new VoodooControl("input", "css", ".layout_" + packageData.get("key") + "_" + moduleData.get(i).get("mod_name") + 
						" .edit.fld_date_closed input").set(moduleData.get(i).get("expected_close_date"));
				VoodooUtils.waitForReady();
			}			

			VoodooUtils.focusDefault();

			// Click Save
			new VoodooControl("a", "css", ".layout_" + packageData.get("key") + "_" + moduleData.get(i).get("mod_name") + 
			" .fld_save_button a:not(.hide)").click();
			VoodooUtils.waitForReady();
			sugar().alerts.getAlert().closeAlert();
		}
	}

	/**
	 * Verify that "View change log" action is available for the custom modules 
	 * @throws Exception
	 */
	@Test
	public void RecordView_30553_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Just to bring back all Custom modules in drop down menu
		sugar().accounts.navToListView();
		
		// TODO: VOOD-695, VOOD-738
		VoodooControl viewChangeLog = new VoodooControl("a","css","ul.dropdown-menu span[data-voodoo-name='audit_button'] a"); 
		VoodooControl nameLink = new VoodooControl("a", "xpath", "//td[contains(@data-type, 'name')]//a");
		VoodooControl downArrow = new VoodooControl("a", "css", ".fld_main_dropdown a[data-toggle='dropdown']");

		// Verify view change Log option for modules
		for (int i = 0; i < moduleData.size(); i++) {
			sugar().navbar.showAllModules();
			new VoodooControl("li", "css", "ul.nav.megamenu div[role='menu'].dropdown-menu li[data-module='" + packageData.get("key") + "_" + moduleData.get(i).get("mod_name") + "']").click();
			sugar().alerts.waitForLoadingExpiration();

			// Click name and goto recordView
			nameLink.click();
			VoodooUtils.waitForReady();
			
			downArrow.click();
			
			viewChangeLog.assertVisible(true);

			downArrow.click(); // Better to close primary button dropdown
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}