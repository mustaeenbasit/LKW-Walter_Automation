package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_30704 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that when auditing dependent dropdown field, change log on UI is shown correctly.
	 * @throws Exception
	 */
	@Test
	public void Studio_30704_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to studio > Accounts > Fields
		// TODO: VOOD-542
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl studioCtrl = sugar().admin.adminTools.getControl("studio");
		studioCtrl.click();
		VoodooUtils.waitForReady();
		VoodooControl accountCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1504 
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();

		// Setting Account type field to 'Auditable'
		new VoodooControl("a", "id", "account_type").click();
		VoodooUtils.waitForReady();
		VoodooControl auditCtrl = new VoodooControl("input", "css", "[name='audited']");
		auditCtrl.click();
		VoodooControl saveButton = new VoodooControl("input", "css", "[name='fsavebtn']");
		saveButton.click();
		VoodooUtils.waitForReady();

		// Setting Industry field to 'Auditable' and dependent field to 'Parent Dropdown'
		new VoodooControl("a", "id", "industry").click();
		VoodooUtils.waitForReady();
		auditCtrl.click();
		FieldSet customData = testData.get(testName).get(0);
		new VoodooControl("select", "id", "depTypeSelect").set(customData.get("parentDropdown"));
		VoodooUtils.waitForReady();

		// Configure visibility for parent dropdown
		new VoodooControl("button", "css", "#visGridRow td:nth-child(2) button").click();
		VoodooUtils.waitForReady();
		VoodooControl analystTableCtrl = new VoodooControl("ul", "id", "ddd_Analyst_list");
		new VoodooControl("li", "css", "#childTable [val='--blank--']").dragNDropViaJS(analystTableCtrl);
		new VoodooControl("li", "css", "#childTable [val='Apparel']").dragNDropViaJS(analystTableCtrl);
		new VoodooControl("li", "css", "#childTable [val='Banking']").dragNDropViaJS(analystTableCtrl);
		new VoodooControl("li", "css", "#childTable [val='Construction']").dragNDropViaJS(new VoodooControl("ul", "id", "ddd_Competitor_list"));
		new VoodooControl("button", "css", "#visGridWindow div.bd  div:nth-child(6) button:nth-child(2)").click();
		VoodooUtils.waitForReady();
		saveButton.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Updating account record with type 'Analyst' and industry 'Apparel'
		String defaultIndustry = sugar().accounts.getDefaultData().get("industry");
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		VoodooControl type = sugar().accounts.recordView.getEditField("type");
		type.set(customData.get("defaultType"));
		VoodooControl industry = sugar().accounts.recordView.getEditField("industry");
		industry.set(defaultIndustry);
		sugar().accounts.recordView.save();

		// Updating the type to 'Competitor' and industry to 'Construction'
		sugar().accounts.recordView.edit();
		type.set(customData.get("updatedType"));
		industry.set(customData.get("updatedIndustry"));
		sugar().accounts.recordView.save();

		// Navigating to 'View Change log' page
		// TODO: VOOD-738
		sugar().accounts.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", "[name='audit_button']").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1990
		// Asserting the Old value on industry field should be Apparel
		new VoodooControl("span", "css", ".dataTable th:nth-of-type(2)").assertContains(customData.get("oldValueLable"), true);
		new VoodooControl("a", "css", "span.fld_cancel_button.history-summary-headerpane a");
		new VoodooControl("span", "css", ".dataTable tr:nth-child(2)  td:nth-child(2)  span").assertContains(defaultIndustry, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}