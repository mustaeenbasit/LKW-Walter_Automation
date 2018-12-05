package com.sugarcrm.test.subpanels;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Subpanels_26239 extends SugarTest {
	FieldSet moduleData = new FieldSet();
	UserRecord user;
	AccountRecord myAccount;

	public void setup() throws Exception {
		// Create account record from API
		myAccount= (AccountRecord) sugar().accounts.api.create();
		
		// Logging in as admin 
		sugar().login();
		moduleData = testData.get(testName+"_1").get(0);	
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// Module Builder
		// TODO: VOOD-933
		new VoodooControl("a", "id" ,"moduleBuilder").click();
		VoodooUtils.waitForReady();

		// create package
		new VoodooControl("a", "id" ,"newPackageLink").click();
		new VoodooControl("input", "css" ,"input[name='name']").set(moduleData.get("package_name"));
		new VoodooControl("input", "css" ,"input[name='key']").set(moduleData.get("key"));
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady(30000);

		// create module
		new VoodooControl("a", "id" ,"new_module").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css" ,"input[name='name']").set(moduleData.get("module_name"));
		new VoodooControl("input", "css" ,"input[name='label']").set(moduleData.get("plural_label"));
		new VoodooControl("input", "css" ,"input[name='label_singular']").set(moduleData.get("singular_label"));
		new VoodooControl("table", "id" ,"type_company").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady(30000);
		
		// Add a relationship 1-Many
		new VoodooControl("input", "css" ,"input[name='viewrelsbtn']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css" ,"input[name='addrelbtn']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css" ,"input[name='saverelbtn']").click();
		VoodooUtils.waitForReady(30000);

		VoodooControl breadCrumbCtrl = new VoodooControl("a", "css" ,"div.bodywrapper div a:nth-of-type(4)");
		breadCrumbCtrl.click();
		VoodooUtils.waitForReady();
		breadCrumbCtrl.waitForVisible();
		breadCrumbCtrl.click();
		VoodooUtils.waitForReady();
		
		new VoodooControl("input", "css" ,"input[name='deploybtn']").click();

		// TODO: VOOD-1010
		new VoodooControl("img", "css", ".bodywrapper img[align='absmiddle']").waitForInvisible(120000);
		new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']").click();

		// New module having a subpanel under Accounts module
		// TODO: VOOD-938
		VoodooUtils.waitForReady();
		new VoodooControl("a", "id", "studiolink_Accounts").click();
		VoodooUtils.waitForReady();
		new VoodooControl("table", "id", "subpanelsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "css", "#Buttons table tbody tr:nth-of-type(3) td:nth-of-type(4)").click();
		VoodooUtils.waitForReady();
		VoodooControl defaultSubPanelCtrl = new VoodooControl("li", "css" ,"#Default #subslot1");
		VoodooControl emailPanel = new VoodooControl("li", "id" ,"subslot40");
		emailPanel.scrollIntoViewIfNeeded(false);
		
		// Drag and drop Email field to Default Column 
		emailPanel.dragNDrop(defaultSubPanelCtrl);
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id" ,"savebtn").click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();

		// Creating new user
		FieldSet data = testData.get(testName).get(0);
		user = (UserRecord) sugar().users.create(data);
		sugar().logout();
	}

	/**
	 * Verify Email Address if visible on custom Company Type Module subpanels. 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_26239_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		// Logging in as new created user
		sugar().login(user);

		// Navigate to new module i.e Test
		// In CSV ->data_module field is the combination of "key" + "_" + "module_name"
		sugar().navbar.navToModule(moduleData.get("data_module"));

		// TODO: VOOD-939
		new VoodooControl("a", "css", ".btn.btn-primary").click();
		new VoodooControl("input", "css", "input[name=name]").set(moduleData.get("module_record_name"));
		new VoodooControl("input", "css", ".newEmail").set(moduleData.get("module_record_email_address"));
		new VoodooControl("a", "css", ".create.fld_save_button a").click();
		VoodooUtils.waitForReady();
		
		// Navigate to account record
		myAccount.navToRecord();

		String subpanel = String.format(".layout_test1_%s .dropdown-toggle", moduleData.get("module_name"));
		new VoodooControl("a", "css", subpanel).click();
		new VoodooControl("a", "css", "a[name=select_button]").click();

		new VoodooControl("input", "css", ".search-and-select tbody tr td:nth-of-type(1) input").click();
		new VoodooControl("a", "css", ".fld_link_button a").click();
		VoodooUtils.waitForReady();
		sugar().alerts.getSuccess().closeAlert();

		// Verifying Email Address if visible on custom Company Type Module subpanel
		new VoodooControl("a", "css", ".list.fld_email a").assertEquals(moduleData.get("module_record_email_address"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
