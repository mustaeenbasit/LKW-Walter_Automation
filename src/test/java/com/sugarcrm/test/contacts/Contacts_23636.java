package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_23636 extends SugarTest {
	DataSource myContacts;

	public void setup() throws Exception {
		myContacts = testData.get(testName);
		sugar().contacts.api.create(myContacts);

		sugar().login();
	}

	/**
	 * Contact Merge_Verify that the clicked record is set as primary after clicking "Set as primary" link of a record when merging records. 
	 * @throws Exception
	 */
	@Test
	public void Contacts_23636_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Select all contact records
		sugar().contacts.navToListView();
		sugar().contacts.listView.toggleSelectAll();
		sugar().contacts.listView.openActionDropdown();

		// TODO: VOOD-681 Create a Lib for Merge
		new VoodooControl("a", "css", ".fld_merge_button.list a").click();
		
		// Store "to" contact before merging
		String toFirstName = "";
		String toLastName = "";
		if (new VoodooControl("input", "css", ".row-div-cnt div:nth-child(2) div.box span.fld_first_name input").queryExists()) {
			toFirstName = (String) new VoodooControl("input", "css", ".row-div-cnt div:nth-child(2) div.box span.fld_first_name input").waitForElement().
					getAttribute("data-original-title");
			toLastName = (String) new VoodooControl("input", "css", ".row-div-cnt div:nth-child(2) div.box span.fld_last_name input").waitForElement().		
					getAttribute("data-original-title");
		}
		else if (new VoodooControl("div", "css", ".row-div-cnt div:nth-child(2) div.box span.fld_first_name div").queryExists()) {
			toFirstName = (String) new VoodooControl("div", "css", ".row-div-cnt div:nth-child(2) div.box span.fld_first_name div").waitForElement().
					getAttribute("data-original-title");	 	
			toLastName = (String) new VoodooControl("div", "css", ".row-div-cnt div:nth-child(2) div.box span.fld_last_name div").waitForElement().		
					getAttribute("data-original-title");
		}
   
		new VoodooControl("div", "css", ".row-div-cnt div:nth-child(1) .primary-lbl").dragNDrop(new VoodooControl("div", "css", ".row-div-cnt div:nth-child(2) .primary-lbl"));
		sugar().alerts.confirmAllWarning();		

		// Verify the record which labeled, is set as primary.
		new VoodooControl("button", "css", ".row-div-cnt div:nth-child(2) .btn.third.active").assertVisible(true);
		sugar().contacts.createDrawer.getControl("saveButton").click();
		sugar().alerts.confirmAllWarning();
		sugar().alerts.waitForLoadingExpiration();

		// Verify that records merged successfully.
		sugar().contacts.navToListView();
		sugar().contacts.listView.verifyField(1, "fullName", sugar().contacts.getDefaultData().get("salutation")+" " +toFirstName+" "+toLastName);		

		// Verify that only 1 record is visible, after merge
		// TODO: VOOD-697 Create a View class method to return the number of records displayed
		new VoodooControl("input", "css", "div[data-voodoo-name='recordlist'] tbody tr:nth-of-type(1) input").assertVisible(true);
		new VoodooControl("input", "css", "div[data-voodoo-name='recordlist'] tbody tr:nth-of-type(2) input").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
