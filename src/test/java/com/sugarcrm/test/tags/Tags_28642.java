package com.sugarcrm.test.tags;
import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.test.SugarTest;

public class Tags_28642 extends SugarTest {
	DataSource customDS;
	
	public void setup() throws Exception {
		sugar().login();		
	}

	/**
	 * Verify the tags field is a default field on the business card for new custom modules
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tags_28574_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a Custom Module
		FieldSet moduleData = testData.get(testName + "_module").get(0);				
		sugar().admin.navToAdminPanelLink("moduleBuilder");
		VoodooUtils.focusFrame("bwc-frame");
		
		//TODO: VOOD-933
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
		
		// Click on View Layout
		new VoodooControl("input", "css" ,"input[name='viewlayoutsbtn']").click();
		VoodooUtils.waitForReady();

		// Click on Record View
		new VoodooControl("tc", "id" ,"viewBtnRecordView").click();
		VoodooUtils.waitForReady();

		// Verify the Tag field should be shown with the label "Tag"
		new VoodooControl("div", "css", "div.le_field[data-name='tag']").assertVisible(true);
		
		// Goto package menu
		new VoodooControl("a", "xpath", "//a[@class='crumbLink'][contains(.,'"+moduleData.get("mod_name")+"')]").click();
		VoodooUtils.waitForReady();
		
		// Deploy the custom module
		new VoodooControl("a", "css" ,"div.bodywrapper div a:nth-of-type(4)").click();
		new VoodooControl("input", "css" ,"table.mbTable input[name='deploybtn']").waitForVisible();
		new VoodooControl("input", "css" ,"table.mbTable input[name='deploybtn']").click();
		VoodooUtils.pause(2000); // Wait for deploy image to appear
		// TODO: VOOD-1010
		new VoodooControl("img", "css", ".bodywrapper img[align='absmiddle']").waitForInvisible(120000);
		VoodooUtils.waitForAlertExpiration();
		
		// Goto Studio
		new VoodooControl("input", "css", "div#footerHTML input.button[value='Studio']").click();
		VoodooUtils.waitForReady();

		// Goto Custom Module
		new VoodooControl("a", "css", "a#studiolink_"+moduleData.get("key")+"_"+moduleData.get("mod_name")).click();
		VoodooUtils.waitForReady();
		
		// Goto Layouts
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();

		// Goto recordView
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooUtils.waitForReady();

		// Verify the Tag field should be shown with the label "Tag"
		new VoodooControl("div", "css", "div.le_field[data-name='tag']").assertVisible(true);
		
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}