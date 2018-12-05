package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Shagun Kaushik <skaushik@sugarcrm.com>
 */
public class Calls_30155 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Record IDs should not be displayed in "Related to" and "Contact Name" field while 
	 * creating record through quick create or subpanel.
	 * @throws Exception
	 */
	@Test
	public void Calls_30155_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Navigating to the Lead record created above
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		
		// Getting the ID of the Lead Record
		String url = VoodooUtils.getUrl();
		String splittedURL[] = url.split("/");
		String leadID = splittedURL[splittedURL.length - 1];
		
		// Clicking the '+' button on Calls Subpanel
		sugar().leads.recordView.subpanels.get(sugar().calls.moduleNamePlural).addRecord();
		VoodooUtils.waitForReady();
		
		// Verifying that the Lead name is displayed in related to field
		VoodooControl parentLead = sugar().calls.createDrawer.getEditField("relatedToParentName");
		parentLead.assertEquals(sugar().leads.getDefaultData().get("fullName"), true);
		
		// Verifying that Lead ID is not displayed in related to field
		parentLead.assertEquals(leadID, false);
		sugar().calls.createDrawer.cancel();
		
		// Navigating to the Contact record created above
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// Getting the ID of the Contact Record
		url = VoodooUtils.getUrl();
		String splittedURL2[] = url.split("/");
		String contactID = splittedURL2[splittedURL2.length - 1];
		
		// Open QuickCreate and click "Create Task"
		sugar().navbar.quickCreateAction(sugar().tasks.moduleNamePlural);

		// TODO: SC-4854: Warning message about unsaved changes comes up when it should not.
		if(sugar().alerts.getAlert().queryVisible())
		sugar().alerts.getAlert().confirmAlert();
		
		// Verifying that the Contact name is displayed in related to field
		VoodooControl parentContact = sugar().tasks.createDrawer.getEditField("relRelatedToParent");
		parentContact.assertEquals(sugar().contacts.getDefaultData().get("fullName"), true);

		// Verifying that Contact ID is not displayed in related to field
		parentContact.assertEquals(contactID, false);
		sugar().tasks.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
