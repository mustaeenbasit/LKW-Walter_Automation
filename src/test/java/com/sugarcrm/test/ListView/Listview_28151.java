package com.sugarcrm.test.ListView;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Listview_28151 extends SugarTest {
	VoodooControl moduleCtrl, layoutCtrl, listviewBtnCtrl, saveBtnCtrl;

	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().accounts.api.create();
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 * To verify the opportunity name should appears in the list view for the lead.
	 * @throws Exception
	 */
	@Test
	public void Listview_28151_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to admin > Studio > Leads > Layout > List View > Add "Opportunity Name" to the Leads module listview
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-542
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Leads");
		layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		listviewBtnCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		saveBtnCtrl = new VoodooControl("td", "id", "savebtn");

		moduleCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		listviewBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Drag "Opportunity Name" field from Hidden panel to Default panel
		VoodooControl dropCtrl = new VoodooControl("td", "id", "Default");
		new VoodooControl("li", "css", "#Hidden [data-name='opportunity_name']").dragNDrop(dropCtrl);

		// Save & Deploy
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Go to Lead Record
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-585
		// Click on "Convert Lead"
		new VoodooControl("a", "css", ".fld_lead_convert_button a").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooControl accountCtrl = new VoodooControl("input", "css", "#collapseAccounts .fld_name input");
		VoodooControl opportunityCtrl = new VoodooControl("input", "css", "#collapseOpportunities .fld_name input");

		// Fill in Account name and click Associate Account
		accountCtrl.waitForVisible();
		accountCtrl.set(sugar().accounts.getDefaultData().get("name"));
		new VoodooControl("a", "css", ".active [data-module='Accounts'] .fld_associate_button a").click();

		// Fill in Opportunity name and click Associate Opportunity
		opportunityCtrl.waitForVisible();
		opportunityCtrl.set(sugar().opportunities.getDefaultData().get("name"));
		new VoodooControl("a", "css", ".active [data-module='Opportunities'] .fld_associate_button a").click();

		// Click Save and Convert.
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		sugar().alerts.waitForLoadingExpiration();

		// Locate the lead in the Leads module list view
		sugar().leads.navToListView();

		// Verify that the opportunity name should appears in the list view of the lead
		new VoodooControl("a", "css", ".fld_opportunity_name.list").assertContains(sugar().opportunities.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}