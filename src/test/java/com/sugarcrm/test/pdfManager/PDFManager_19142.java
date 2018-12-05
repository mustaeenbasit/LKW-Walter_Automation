package com.sugarcrm.test.pdfManager;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PDFManager_19142  extends SugarTest {
	FieldSet moduleData;
	VoodooControl modulebuilderCtrl, breadCrumbCtrl, pdfManagerCtrl, insertCtrl, saveButtonCtrl ;
	DataSource fieldsData;
	public void setup() throws Exception {
		moduleData = testData.get(testName).get(0);
		fieldsData = testData.get(testName + "_fields_data");
		sugar.login();
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// Module Builder
		// TODO: VOOD-933
		modulebuilderCtrl = new VoodooControl("a", "id" ,"moduleBuilder");
		modulebuilderCtrl.click();

		// create package
		// TODO: VOOD-933
		new VoodooControl("a", "id" ,"newPackageLink").click();
		new VoodooControl("input", "css" ,"input[name='name']").set(moduleData.get("package_name"));
		new VoodooControl("input", "css" ,"input[name='key']").set(moduleData.get("key"));
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();	
		VoodooUtils.waitForReady();

		new VoodooControl("a", "id" ,"new_module").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css" ,"input[name='name']").set(moduleData.get("module_name"));
		new VoodooControl("input", "css" ,"input[name='label']").set(moduleData.get("plural_label"));
		new VoodooControl("input", "css" ,"input[name='label_singular']").set(moduleData.get("singular_label"));
		new VoodooControl("table", "id" ,"type_company").click();	
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady();

		// Add custom fields
		new VoodooControl("input", "css" ,"input[name='viewfieldsbtn']").click();
		VoodooControl customFieldCtrl = new VoodooControl("input", "css" ,"input[name='addfieldbtn']");
		VoodooControl selectFieldTypeCtrl = new VoodooControl("select", "id", "type");
		VoodooControl saveBtnCtrl = new VoodooControl("input", "css" ,"input[name='fsavebtn']");
		VoodooControl fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");

		for (int i = 0; i < fieldsData.size(); i++) {
			customFieldCtrl.click();
			VoodooUtils.waitForReady();
			selectFieldTypeCtrl.set(fieldsData.get(i).get("field_type"));
			VoodooUtils.waitForReady();
			fieldNameCtrl.set(fieldsData.get(i).get("field_name"));
			saveBtnCtrl.click();
			VoodooUtils.waitForReady();
		}

		// TODO: VOOD-933
		breadCrumbCtrl = new VoodooControl("a", "css" ,"div.bodywrapper div a:nth-of-type(4)");
		breadCrumbCtrl.click();
		VoodooUtils.waitForReady();
		breadCrumbCtrl.waitForVisible();
		breadCrumbCtrl.click();
		VoodooUtils.waitForReady();

		// Deploy
		VoodooControl deployCtrl = new VoodooControl("input", "css" ,"input[name='deploybtn']");
		deployCtrl.waitForVisible();
		deployCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1010
		// This pause is required for deploy
		VoodooUtils.pause(30000);

		VoodooUtils.focusDefault();
	}

	/**
	 * 
	 * Verify the custom issue module and fields are appeared in drop down when creating pdf template
	 * @throws Exception
	 */
	@Test
	public void PDFManager_19142_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Admin -> PDF Manager
		sugar.navbar.navToAdminTools();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1158
		pdfManagerCtrl = new VoodooControl("a", "id", "pdfmanager");
		pdfManagerCtrl.click();
		VoodooUtils.focusDefault();

		// Create PDF Template
		new VoodooControl("i", "css", "li[data-module='PdfManager'] i.fa-caret-down").click();
		new VoodooControl("a", "css", "li[data-module='PdfManager'] a[data-navbar-menu-item='LNK_NEW_RECORD']").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");

		VoodooControl moduleCtrl = new VoodooControl("select", "id", "base_module");
		moduleCtrl.click();
		VoodooUtils.pause(200); // small delay required 

		// verify that new module Tests in the drop down.
		new VoodooControl("option", "css", "#base_module [value='test1_Test']").assertContains(moduleData.get("plural_label"), true);
		moduleCtrl.set(moduleData.get("plural_label"));

		VoodooControl fieldCtrl = new VoodooControl("select", "id", "field");
		fieldCtrl.click();

		// verify that custom fields like i.e. date1, datetime1 is in the drop down.
		new VoodooControl("option", "css", "[value='date1']").assertContains(fieldsData.get(0).get("field_name"), true);
		new VoodooControl("option", "css", "[value='datetime1']").assertContains(fieldsData.get(1).get("field_name"), true);

		// set template name
		new VoodooControl("input", "id", "name").set(moduleData.get("pdf_name"));

		// set field name
		fieldCtrl.set(fieldsData.get(0).get("field_name"));
		insertCtrl = new VoodooControl( "input", "id", "pdfManagerInsertField");
		insertCtrl.click();

		fieldCtrl.set(fieldsData.get(1).get("field_name"));
		insertCtrl.click();

		// save the template
		saveButtonCtrl = new VoodooControl("input", "id", "SAVE_HEADER");
		saveButtonCtrl.click();

		// assert that pdf saved successfully 
		new VoodooControl("td", "css", "#DEFAULT tr:nth-child(1) td:nth-child(2)").assertContains(moduleData.get("pdf_name"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}