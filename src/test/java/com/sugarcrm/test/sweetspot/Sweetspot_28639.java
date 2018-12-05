package com.sugarcrm.test.sweetspot;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

import org.junit.Test;

public class Sweetspot_28639 extends SugarTest {
	VoodooControl moduleBuilderCtrl;
	FieldSet customFS = new FieldSet();
	
	public void setup() throws Exception {
		customFS = testData.get(testName).get(0);
		sugar().login();
		
		// Navigate to admin
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		moduleBuilderCtrl = sugar().admin.adminTools.getControl("moduleBuilder");
		moduleBuilderCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-542
		new VoodooControl("a", "id" ,"newPackageLink").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css" ,"input[name='name']").set(customFS.get("package_name"));
		new VoodooControl("input", "css" ,"input[name='key']").set(customFS.get("key"));
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();	
		VoodooUtils.waitForReady(30000);

		// Create module
		new VoodooControl("a", "id" ,"new_module").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css" ,"input[name='name']").set(customFS.get("module_name"));
		new VoodooControl("input", "css" ,"input[name='label']").set(customFS.get("plural_label"));
		new VoodooControl("input", "css" ,"input[name='label_singular']").set(customFS.get("singular_label"));
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady(30000);
		
		// Deploy Created module
		new VoodooControl("input", "css", "#footerHTML input[value='Module Builder']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css" ,"#Buttons table tr td:nth-child(2) a.studiolink").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", ".mbTable input[name='deploybtn']").click();
		sugar().alerts.confirmAllAlerts();
		new VoodooControl("img", "css", "#mbtabs > div > div > div > img").waitForInvisible(160000);
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify that Sweet Spot works with custom module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Sweetspot_28639_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1437 - Need lib support to send "Key" to a control
		// Activate Sweetspot
		sugar().sweetspot.show();
		sugar().sweetspot.search(customFS.get("module_name"));
		
		// Verify search result
		sugar().sweetspot.getActionsResult().assertContains(customFS.get("module_name"), true);
		sugar().sweetspot.clickActionsResult();
		
		// TODO: VOOD-1036
		// Vefity that user is directed to the custom module
		new VoodooControl("div", "css", ".list-headerpane.fld_title div").assertContains(customFS.get("module_name"), true);
		
		// Create new record
		new VoodooControl("a", "css", ".fld_create_button a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", ".fld_name.edit input").set(customFS.get("new_custom_record"));
		new VoodooControl("a", "css", ".create.fld_save_button a").click();
		VoodooUtils.waitForReady();
		
		// Go to Sweetspot search
		sugar().sweetspot.show();
		sugar().sweetspot.search(customFS.get("new_custom_record"));
		
		// Verify that Sweet Spot search result shows the custom module's record
		sugar().sweetspot.getRecordsResult().assertContains(customFS.get("new_custom_record"), true);
		sugar().sweetspot.clickRecordsResult();
	
		// Verify that the User is directed to the correct custom module record
		new VoodooControl("div", "css", "span.detail.fld_name div").assertEquals(customFS.get("new_custom_record"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}