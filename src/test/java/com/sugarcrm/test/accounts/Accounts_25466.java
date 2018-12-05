package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_25466 extends SugarTest {

	public void setup() throws Exception {		
		sugar().accounts.api.create();
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify the recalculation record list item
	 * @throws Exception
	 */
	@Test
	public void Accounts_25466_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Studio Accounts Fileds
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-1504 : Support Studio Module Fields View 
		new VoodooControl("a", "css" ,"#studiolink_" + sugar().accounts.moduleNamePlural).click();
		
		// Change the Description field to become a calculated field with logic 
		// e.g. (related($assigned_user_link,"first_name”)) - Assigned user Last Name.
		new VoodooControl("a", "css" ,"td#fieldsBtn a").click();
		new VoodooControl("a", "id" ,"description").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css" ,"input#calculated").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css" ,"[name='editFormula']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css" ,"[title='Related Field']").click();
		
		// Select and create releted formula
		new VoodooControl("select", "css", "#selrf_rmodule option:nth-child(29)").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "#selrf_rfield option:nth-child(2)").click(); 
		new VoodooControl("button", "css", "[name='selrf_insertbtn']").click();
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		
		// Click on Save button
		new VoodooControl("input", "css" ,"[name='fsavebtn']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Go to account list view
		sugar().accounts.navToListView();
		sugar().accounts.listView.checkRecord(1);
		sugar().accounts.listView.openActionDropdown();
		
		// For the Admin user the Recalculate Values menu item is visible 
		// TODO: 689 : Need Lib support for Accounts > listView > clickMassallCheckbox > Action Dropdown next to the MassallCheckbox
		VoodooControl recalculateValueBtn = new VoodooControl("a", "css", "[name='calc_field_button']");
		recalculateValueBtn.assertVisible(true);
		recalculateValueBtn.click();
		
		// Go to Accounts > RecordView
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.showMore();
		VoodooUtils.waitForReady();
		String assertValue = "Administrator";
		
		// For the Admin user - the Account Description field should contain the Assigned user's First Name
		sugar().accounts.recordView.getDetailField("description").assertContains(assertValue, true);
		
		// go to next record for Inspect an Account record not selected for Recalculate Value
		sugar().accounts.recordView.gotoNextRecord();
		
		// The Account Description field should not contain the Assigned user's First Name
		sugar().accounts.recordView.getDetailField("description").assertContains(assertValue, false);
		
		// Logout as admin & login as qauser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
		
		sugar().accounts.navToListView();
		sugar().accounts.listView.checkRecord(1);
		sugar().accounts.listView.openActionDropdown();
		
		// For the non-admin user the Recalculate Values menu item is not visible 
		recalculateValueBtn.assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}