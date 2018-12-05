package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21632 extends SugarTest {
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		// Create three contact records with different name
		ds = testData.get(testName+"_data");
		sugar().contacts.api.create(ds);
		sugar().login();
	}

	/**
	 * Cancel does not clear filters of dashlet
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21632_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Dashboard
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		FieldSet customFS = testData.get(testName).get(0);

		// TODO: VOOD-670
		// Click edit icon of a list view "Contacts" dashlet
		new VoodooControl("i", "css", ".dashlets.row-fluid li:nth-child(3) ul.dashlet-cell.rows.row-fluid .fa.fa-cog").click();
		new VoodooControl("a", "css", ".dashlets.row-fluid li:nth-child(3) [data-dashletaction='editClicked']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("i", "css", "[data-voodoo-name='filter-filter-dropdown'] .fa.fa-times-circle").click();
		VoodooUtils.waitForReady();

		// Verify that the search filters is cleared.
		new VoodooControl("span", "css", "[data-voodoo-name='filter-filter-dropdown'] .choice-filter-label").assertContains(customFS.get("notFoundFilterText"), false);

		// Click "X" to clear the filter 
		new VoodooControl("a", "css", "div.main-pane.span8 span.detail.fld_cancel_button a").click();

		// Sort the contacts in the dashlet by name to avoid inconsistency
		new VoodooControl("th", "css", ".dashlet-row .row-fluid.sortable:nth-child(3) .sorting.orderByfull_name").click();
		VoodooUtils.waitForReady();

		FieldSet contactData = sugar().contacts.getDefaultData();

		// Verify that the filter not changed, and the dashlet displays the same record as before
		int j = 2;
		for(int i = 1; i <= 3; i++) {
			String verifyText = contactData.get("salutation") + " " + contactData.get("firstName")+" "+ds.get(j).get("lastName");
			VoodooControl fullNameCtrl = new VoodooControl("td", "css", ".dashlets.row-fluid li:nth-child(3) tr:nth-child("+i+") .fld_full_name a");
			fullNameCtrl.waitForVisible();
			fullNameCtrl.assertEquals(verifyText, true);
			j--;
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}