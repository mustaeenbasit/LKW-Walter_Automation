package com.sugarcrm.test.tags;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_28643 extends SugarTest {
	VoodooControl moduleBuilderCtrl;

	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * [Tags] Verify Tag language strings aren't rendered in ModuleBuilder for custom modules
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tags_28643_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to admin
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		moduleBuilderCtrl = sugar.admin.adminTools.getControl("moduleBuilder");
		moduleBuilderCtrl.click();
		VoodooUtils.waitForReady();
		FieldSet customFS = new FieldSet();
		customFS = testData.get(testName).get(0);

		// TODO: VOOD-933
		new VoodooControl("a", "id" ,"newPackageLink").click();
		new VoodooControl("input", "css" ,"input[name='name']").set(customFS.get("package_name"));
		new VoodooControl("input", "css" ,"input[name='key']").set(customFS.get("key"));
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();	
		VoodooUtils.waitForReady();

		// create module
		new VoodooControl("a", "id" ,"new_module").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css" ,"input[name='name']").set(customFS.get("module_name"));
		new VoodooControl("input", "css" ,"input[name='label']").set(customFS.get("plural_label"));
		new VoodooControl("input", "css" ,"input[name='label_singular']").set(customFS.get("singular_label"));
		new VoodooControl("table", "id" ,"type_file").click();		
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady();

		// Click on View Layout
		new VoodooControl("input", "css" ,"input[name='viewlayoutsbtn']").click();

		// Click on Record View
		new VoodooControl("tc", "id" ,"viewBtnRecordView").click();

		// Verify the Tag field should be shown with the label "Tag" but not the label "LBL_TAGS".
		new VoodooControl("span", "css", ".le_panel div:nth-child(8) span").assertEquals("Tags", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}