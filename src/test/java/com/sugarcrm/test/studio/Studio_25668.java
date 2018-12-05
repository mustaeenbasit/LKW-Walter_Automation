package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Studio_25668 extends SugarTest {
	VoodooControl accountsButtonCtrl, defaultPanelListViewCtrl, subpanelsButtonCtrl, leadsSubpanelButtonCtrl, saveDeployButtonCtrl;
	StandardSubpanel leadsSubpanel;
	String leadSourceDraggableLI="", leadSourceDescriptionDraggableLI="";
	DataSource customData = new DataSource();

	public void setup() throws Exception {
		customData = testData.get(testName);

		// TODO: VOOD-1511
		accountsButtonCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		subpanelsButtonCtrl = new VoodooControl("td", "id", "subpanelsBtn");
		leadsSubpanelButtonCtrl = new VoodooControl("a","xpath", "//table[@class='wizardButton']/tbody/tr[2]/td/a[contains(.,'Leads')]");
		defaultPanelListViewCtrl = new VoodooControl("ul", "css", "td#Default ul");
		saveDeployButtonCtrl = new VoodooControl("input", "id", "savebtn");	
		leadSourceDraggableLI = String.format("li[data-name=%s]",customData.get(0).get("internal_voodoo_name"));
		leadSourceDescriptionDraggableLI = String.format("li[data-name=%s]",customData.get(1).get("internal_voodoo_name"));

		// Account and lead record created via API
		AccountRecord myAccount = (AccountRecord) sugar().accounts.api.create();
		LeadRecord myLead = (LeadRecord) sugar().leads.api.create();
		sugar().login();

		// Link lead record to Account record
		myAccount.navToRecord();
		leadsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubpanel.linkExistingRecord(myLead);
		sugar().alerts.getSuccess().closeAlert();
	}

	/**
	 * Verify that subpanel column made hidden in studio is removed from actual subpanel layout. 
	 * @throws Exception
	 */
	@Test
	public void Studio_25668_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Before making fields hidden, first verify that "Lead source" and "Lead Source Description" are visible
		VoodooControl columnsHeaderListCtrl = new VoodooControl("tr", "xpath", "//div[@data-subpanel-link='leads']/ul/li/div[2]/div[1]/table/thead/tr[1]");
		columnsHeaderListCtrl.assertElementContains(customData.get(0).get("column_name"), true);
		columnsHeaderListCtrl.assertElementContains(customData.get(1).get("column_name"), true);

		// Now make these fields hidden in List view
		// Studio => Accounts => Subpanels => Leads
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		accountsButtonCtrl.click();
		VoodooUtils.waitForReady();
		subpanelsButtonCtrl.click();
		VoodooUtils.waitForReady();
		leadsSubpanelButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Move fields from Default column to Hidden column
		VoodooControl hiddenPanelListViewCtrl = new VoodooControl("ul", "css", "td#Hidden ul");
		new VoodooControl("li", "css", leadSourceDraggableLI).dragNDrop(hiddenPanelListViewCtrl);
		new VoodooControl("li", "css", leadSourceDescriptionDraggableLI).dragNDrop(hiddenPanelListViewCtrl);

		// Save and Deploy
		saveDeployButtonCtrl.click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();

		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		leadsSubpanel.expandSubpanel();

		// Now verify that "user assigned" and "data created" are not visible
		columnsHeaderListCtrl.assertElementContains(customData.get(0).get("column_name"), false);
		columnsHeaderListCtrl.assertElementContains(customData.get(1).get("column_name"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}