package com.sugarcrm.test.reports;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_18977 extends SugarTest {
	DataSource moduleData;
	VoodooControl modulebuilderCtrl, breadCrumbCtrl ;
	FieldSet fs;
	public void setup() throws Exception {
		moduleData = testData.get(testName);
		fs = testData.get(testName + "_1").get(0);
		sugar().login();
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// Module Builder
		// TODO: VOOD-933
		modulebuilderCtrl = new VoodooControl("a", "id" ,"moduleBuilder");
		modulebuilderCtrl.click();
		
		// create package
		// TODO: VOOD-933
		new VoodooControl("a", "id" ,"newPackageLink").click();
		new VoodooControl("input", "css" ,"input[name='name']").set(moduleData.get(0).get("package_name"));
		new VoodooControl("input", "css" ,"input[name='key']").set(moduleData.get(0).get("key"));
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();	
		VoodooUtils.waitForReady();

		VoodooControl newModuleCtrl = new VoodooControl("a", "id" ,"new_module");
		VoodooControl moduleNameCtrl = new VoodooControl("input", "css" ,"input[name='name']");
		VoodooControl moduleLabelCtrl = new VoodooControl("input", "css" ,"input[name='label']");
		VoodooControl moduleSingularLabelCtrl = new VoodooControl("input", "css" ,"input[name='label_singular']");
		VoodooControl moduleTypeCtrl = new VoodooControl("table", "id" ,"type_company");	
		VoodooControl saveCtrl = new VoodooControl("input", "css" ,"input[name='savebtn']");
		
		// create two custom modules
		for (int i = 0; i < moduleData.size(); i++) {
			if(i == 1){
				new VoodooControl("a", "css", "#mbtabs a:nth-child(4)").click();
				VoodooUtils.waitForReady();
			}
			VoodooUtils.waitForAlertExpiration();
			newModuleCtrl.click();
			VoodooUtils.waitForReady();
			moduleNameCtrl.set(moduleData.get(i).get("module_name"));
			moduleLabelCtrl.set(moduleData.get(i).get("plural_label"));
			moduleSingularLabelCtrl.set(moduleData.get(i).get("singular_label"));
			moduleTypeCtrl.click();	
			saveCtrl.click();
			VoodooUtils.waitForReady();
		}

		// Add relationship Many to One
		new VoodooControl("input", "css" ,"input[name='viewrelsbtn']").click();
		VoodooControl addRelationshipCtrl = new VoodooControl("input", "css" ,"input[name='addrelbtn']");
		VoodooControl relationshipTypeCtrl = new VoodooControl("select", "id", "relationship_type_field");
		VoodooControl saveBtnCtrl = new VoodooControl("input", "css" ,"input[name='saverelbtn']");
		
		for (int i = 0; i < moduleData.size(); i++) {
			addRelationshipCtrl.click();
			VoodooUtils.waitForReady();
			relationshipTypeCtrl.set(moduleData.get(i).get("relation_type"));
			VoodooUtils.waitForReady();
			new VoodooControl("select", "id", "rhs_mod_field").set(moduleData.get(i).get("relationship_module"));
			VoodooUtils.waitForReady();
			saveBtnCtrl.click();
			VoodooUtils.waitForReady();
		}

		// TODO: VOOD-933
		breadCrumbCtrl = new VoodooControl("a", "css" ,"div.bodywrapper div a:nth-of-type(4)");
		breadCrumbCtrl.click();
		VoodooUtils.waitForReady();

		// Deploy
		VoodooControl deployCtrl = new VoodooControl("input", "css" ,"input[name='deploybtn']");
		deployCtrl.waitForVisible(20000);
		deployCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();
		
	}

	/**
	 * Verify all labels are correct when creating report on custom "many-to-one" relationship module
	 * @throws Exception
	 */
	@Test
	public void Reports_18977_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// VOOD-643
		// navigate to report module 
		sugar().navbar.navToModule(fs.get("module_plural_name"));
		new VoodooControl("li", "css", ".dropdown.active .fa.fa-caret-down").click();
		sugar().alerts.waitForLoadingExpiration(45000);
		
		// click on Create Report
		VoodooControl creatReport = new VoodooControl("a", "css", "[data-navbar-menu-item='LBL_CREATE_REPORT']");
		creatReport.waitForVisible(20000);
		creatReport.click();
		sugar().alerts.waitForLoadingExpiration(30000);
		VoodooUtils.focusFrame("bwc-frame");
		
		// click onRows and Columns Report
		new VoodooControl("td", "css", "#report_type_div > table > tbody > tr:nth-child(2) > td:nth-child(1) > table > tbody > tr:nth-child(1) > td:nth-child(1)").click();
		
		// click on custom module i.e. Demos
		new VoodooControl("table", "id", "Demos").click();
		sugar().alerts.waitForLoadingExpiration(30000);
		
		// Verify custom module and accounts module labels are correct in related module folder.
		VoodooControl moduleTree = new VoodooControl("div", "id", "module_tree");
		moduleTree.waitForVisible(20000);
		moduleTree.assertContains(moduleData.get(0).get("relationship_module"), true);
		moduleTree.assertContains(moduleData.get(1).get("relationship_module"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}