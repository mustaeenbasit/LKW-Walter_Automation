package com.sugarcrm.test.ListView;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_17330 extends SugarTest {

	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify displayed columns can be toggled on or off
	 */
	@Test
	public void ListView_17330_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		ArrayList<String> headerNames = new ArrayList<String>();
		headerNames.addAll(sugar.accounts.listView.getHeaders());
		
		sugar.accounts.navToListView();
		sugar.accounts.listView.toggleSidebar();
		sugar.accounts.listView.getControl("moreColumn").click();

		// Verifying all the column labels are displayed on the drop down list and are in the order displayed from left to right
		// TODO: VOOD-1517, VOOD-1518
		for(int i = 0; i < headerNames.size(); i++) {
			if (i == 6) 
				new VoodooControl("li", "css", ".dropdown-menu.left li:nth-child("+(i+2)+") [data-field-toggle='date_entered']").assertExists(true);
			else if(i == 7 )
				new VoodooControl("li", "css", ".dropdown-menu.left li:nth-child("+(i)+") [data-field-toggle='date_modified']").assertExists(true);
			else{
				new VoodooControl("li", "css", ".dropdown-menu.left li:nth-child("+(i+1)+") [data-field-toggle='"+headerNames.get(i)+"']").assertExists(true);
			}
		}

		// Verifying that all column headers are checked on or "displayed" by default
		for(int i = 0; i < headerNames.size(); i++) {
			if (i == 6) 
				new VoodooControl("li", "css", ".dropdown-menu.left li:nth-child("+(i+2)+") [data-field-toggle='date_entered']").assertAttribute("class", "active");
			else if(i == headerNames.size()-1)
				new VoodooControl("li", "css", ".dropdown-menu.left li:nth-child("+(i)+") [data-field-toggle='date_modified']").assertAttribute("class", "active");
			else
				new VoodooControl("li", "css", ".dropdown-menu.left li:nth-child("+(i+1)+") [data-field-toggle='"+headerNames.get(i)+"']").assertAttribute("class", "active");
		}

		// Disable or un-check, Billing Country, Email Address, and Date Created.
		new VoodooControl("li", "css", ".dropdown-menu.left li:nth-child(3) [data-field-toggle='billing_address_country']").click();
		new VoodooControl("li", "css", ".dropdown-menu.left li:nth-child(6) [data-field-toggle='email']").click();
		new VoodooControl("li", "css", ".dropdown-menu.left li:nth-child(8) [data-field-toggle='date_entered']").click();

		// Verify that menu stays open
		sugar.accounts.listView.getControl("moreColumn").assertAttribute("class", "open", true);

		new VoodooControl("div", "css", ".dropdown-menu.left").assertVisible(true);

		// Verify that the unchecked items are no more shown in the listView
		sugar.accounts.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("billing_address_country"))).assertVisible(false);
		sugar.accounts.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("email"))).assertVisible(false);
		sugar.accounts.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("date_entered"))).assertVisible(false);

		// Verify that checked items are displayed properly
		sugar.accounts.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("name"))).assertVisible(true);
		sugar.accounts.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("billing_address_city"))).assertVisible(true);
		sugar.accounts.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("phone_office"))).assertVisible(true);
		sugar.accounts.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("assigned_user_name"))).assertVisible(true);
		new VoodooControl("th", "css", ".orderBydate_modified").assertVisible(true);

		// Closing the dropdown menu list
		sugar.accounts.listView.getControl("moreColumn").click();

		// Verify that checked items are displayed properly even after drop down menu is closed
		sugar.accounts.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("name"))).assertVisible(true);
		sugar.accounts.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("billing_address_city"))).assertVisible(true);
		sugar.accounts.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("phone_office"))).assertVisible(true);
		sugar.accounts.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("assigned_user_name"))).assertVisible(true);
		new VoodooControl("th", "css", ".orderBydate_modified").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}