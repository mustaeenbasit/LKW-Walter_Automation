package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Shagun Kaushik <skaushik@sugarcrm.com>
 */
public class Opportunities_29064 extends SugarTest {
	Record contact;
	FieldSet contactRole;
	
	public void setup() throws Exception {
		contactRole = testData.get(testName).get(0);
		contact = sugar().contacts.api.create();
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 * Verify that in sub panel of Opportunity Contact Role should be retain even after refresh
	 * the Opportunity record.
	 * @throws Exception
	 */
	@Test
	public void Opportunities_29064_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Navigating to Opportunities page
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		
		StandardSubpanel contactSubpanel = sugar().opportunities.recordView.subpanels.get(sugar
				.contacts.moduleNamePlural); 
		contactSubpanel.scrollIntoView();
		contactSubpanel.linkExistingRecord(contact);
		contactSubpanel.editRecord(1);
		contactSubpanel.getEditField(1, "role").set(contactRole.get("role"));
		contactSubpanel.saveAction(1);
		
		// Refreshing the page
		VoodooUtils.refresh();
		
		// Verifying that the contact role is retained in Opportunity's Contact sub-panel
		contactSubpanel.scrollIntoView();
		contactSubpanel.expandSubpanel();
		contactSubpanel.getDetailField(1, "role").assertEquals(contactRole.get("role"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}