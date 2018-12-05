package com.sugarcrm.test.employees;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Employees_24703 extends SugarTest {
	FieldSet userDataSet;
	UserRecord user;
	String fullName;
	
	public void setup() throws Exception {
		userDataSet = testData.get(testName+"_user").get(0);
		fullName = userDataSet.get("firstName")+" "+userDataSet.get("lastName");
		sugar.login();
		
		// Create user
		user = (UserRecord)sugar.users.create(userDataSet);
		
		// Navigate to Admin -> System Setting
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("systemSettings").click();

		// In Admin > System Settings > Set "List View Items" to be 2.
		new VoodooControl("input", "id",
				"ConfigureSettings_list_max_entries_per_page").set("2");
		new VoodooControl("input", "name", "save").click();		
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.pause(6000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Employee_Verify that admin user can export employee list by "Current Page".
	 * @throws Exception
	 */
	@Test
	public void Employees_24703_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
		
		// TODO VOOD-1041
		sugar.navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "css", "#selectLinkTop > li > span").click();
		
		// Verify that "select this page" appear
		new VoodooControl("a", "id", "button_select_this_page_top").waitForVisible();
		new VoodooControl("a", "id", "button_select_this_page_top").assertContains("Select This Page", true);
		new VoodooControl("a", "id", "button_select_this_page_top").click(); // click on select this page li
		
		// Verify that two records exist
		new VoodooControl("tr", "xpath", "//*[@id='MassUpdate']/table/tbody/tr[contains(.,'"+fullName+"')]").assertExists(true);
		new VoodooControl("tr", "xpath", "//*[@id='MassUpdate']/table/tbody/tr[contains(.,'qauser')]").assertExists(true);
		
		// Verify that select all have attribute checked
		VoodooControl massAll = new VoodooControl("input", "css", "#selectLinkTop input[checked='checked']");
		massAll.waitForVisible(3000);
		massAll.assertExists(true);
		
		new VoodooControl("span", "css", "#actionLinkTop > li > span").click();
		new VoodooControl("a", "id", "massupdate_listview_top").click();
		
		// Click on next button(pagination) for next page
		new VoodooControl("button", "id", "listViewNextButton_top").click();
		sugar.alerts.waitForLoadingExpiration();
		
		// Verify that select all checkbox not checked
		massAll.assertExists(false);
		
		// Verify that one records exist
		new VoodooControl("tr", "xpath", "//*[@id='MassUpdate']/table/tbody/tr[contains(.,'Administrator')]").assertExists(true);		

		/** 
		 * A prompt window pop up to Download does not appear in chrome (as per its design). 
		 * So we have no verification for "A prompt window pops up for user to select open or save the exported file to local machine."
		 * We will need this for firefox and other such supported drivers
		 */  
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}