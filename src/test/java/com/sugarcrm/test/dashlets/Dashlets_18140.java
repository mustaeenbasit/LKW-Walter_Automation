package com.sugarcrm.test.dashlets;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_18140 extends SugarTest {

	public void setup() throws Exception {
		// Create test data and Log in as a valid user
		sugar.contacts.api.create(testData.get(testName));
		sugar.login();
	}

	/**
	 * Verify that in Dashlet, multi fields can be reselected again after all fields are de-selected
	 * @throws Exception
	 */
	@Test
	public void Dashlets_18140_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-670 - More Dashlet Support
		// Verify "My Contacts" Dashlet
		VoodooControl myContactsDashlet = new VoodooControl("ul", "css", "li:nth-of-type(3) .dashlet-cell");
		myContactsDashlet.assertVisible(true);
		
		// Confirm that My Contacts dashlet is not empty
		VoodooControl myContactsDashletTableHeader = new VoodooControl("span", "css", ".fieldset.list.fld_full_name");
		myContactsDashletTableHeader.assertExists(true);
		
		VoodooControl myContactsDashletTableHeaderFullname = new VoodooControl("th", "css", ".orderByfull_name span");
		VoodooControl myContactsDashletTableHeaderAccountname = new VoodooControl("th", "css", ".orderByaccount_name span");
		VoodooControl myContactsDashletTableHeaderPhonework = new VoodooControl("th", "css", ".orderByphone_work span");
		VoodooControl myContactsDashletTableHeaderTitle = new VoodooControl("th", "css", ".orderBytitle span");

		// Confirm that columns exist
		myContactsDashletTableHeaderFullname.assertExists(true);
		myContactsDashletTableHeaderAccountname.assertExists(true);
		myContactsDashletTableHeaderPhonework.assertExists(true);
		myContactsDashletTableHeaderTitle.assertExists(true);
		
		// Confirm that 10 data rows are present
		VoodooControl myContactsDashletTableData = new VoodooControl("tr", "css", ".single");
		assertTrue("Required no. of rows not present. Expected: 10, Actual: " + myContactsDashletTableData.count(), myContactsDashletTableData.count() == 10);
		
		myContactsDashlet.scrollIntoView();
		
		// Click Configure on My Contacts Dashlet
		VoodooControl myContactsConfigure = new VoodooControl("i", "css", "li:nth-of-type(3) .dashlet-cell .fa.fa-cog");
		myContactsConfigure.click();

		// Click Edit from Menu
		VoodooControl myContactsEdit = new VoodooControl("ul", "css", "li:nth-of-type(3) .dashlet-cell .dropdown-menu.left li");
		myContactsEdit.click();
		
		VoodooUtils.waitForReady();
		
		// On Configure Dashlet page, deselect 4 default fields - Name, Account Name, Office phone
		VoodooControl dashletFields = new VoodooControl("a", "css", ".fld_display_columns ul li a");
		while(dashletFields.queryExists()) {
			dashletFields.click();
		}

		VoodooControl saveButtonCtrl = new VoodooControl("a", "css", "a[name='save_button']:not(.hide)");
		saveButtonCtrl.click();
		sugar.alerts.waitForLoadingExpiration();
		
		myContactsDashlet.scrollIntoView();
		
		// On back to Home, confirm that My Contacts dashlet is empty
		myContactsDashletTableHeader.assertExists(false);

		// On Configure Dashlet page, restore all fields 
		myContactsConfigure.click();
		myContactsEdit.click();
		VoodooUtils.waitForReady();

		// Select field Control
		VoodooControl selectFieldCtrl = new VoodooControl("ul", "css", ".select2-choices.ui-sortable .select2-search-field.ui-sortable-handle");
		
		// Select all field names offered.
		for (int row = 1; row < 6; row++) {
			if (row != 4) {
				selectFieldCtrl.click();
				new VoodooControl("li", "css", ".select2-result:nth-of-type(" + row + ")").click();
			}
		}

		// Save the restored fields
		saveButtonCtrl.click();
		sugar.alerts.waitForLoadingExpiration();
		
		// On back to Home, confirm that My Contacts dashlet is not empty
		myContactsDashlet.scrollIntoView();
		myContactsDashletTableHeader.assertExists(true);

		// Confirm that columns exist
		myContactsDashletTableHeaderFullname.assertExists(true);
		myContactsDashletTableHeaderAccountname.assertExists(true);
		myContactsDashletTableHeaderPhonework.assertExists(true);
		myContactsDashletTableHeaderTitle.assertExists(true);
		
		// Confirm that 10 data rows are present
		assertTrue("Required no. of rows not present. Expected: 10, Actual: " + myContactsDashletTableData.count(), myContactsDashletTableData.count() == 10);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}