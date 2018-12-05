package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

/**
 * @author Vaibhav Singhal <vsinghal@sugarcrm.com>
 */
@Features(revenueLineItem = false)
public class Contacts_17603 extends SugarTest {
	FieldSet customData;
	ContactRecord myContact;
	OpportunityRecord relatedOpportunity;
	StandardSubpanel opportunitiesSubpanel, contactSubpanel;
	VoodooControl contactsCtrl, subpanelCtrl, oppSubPanelCtrl;

	public void setup() throws Exception {
		myContact = (ContactRecord) sugar().contacts.api.create();
		relatedOpportunity = (OpportunityRecord)sugar().opportunities.api.create();
		sugar().accounts.api.create();
		customData = testData.get(testName).get(0);
		sugar().login();

		// Create an opportunity with default api data and link to created contact
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		opportunitiesSubpanel = sugar().contacts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);
		opportunitiesSubpanel.linkExistingRecord(relatedOpportunity);

		// Create an opportunity whose Probability is > 70 %
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		sugar().opportunities.createDrawer.getEditField("name").set(testName);
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.getEditField("date_closed").set(sugar().opportunities.getDefaultData().get("date_closed"));
		sugar().opportunities.createDrawer.getEditField("likelyCase").set(sugar().opportunities.getDefaultData().get("likelyCase"));

		// TODO: VOOD-1359
		new VoodooSelect("span", "css", ".fld_sales_stage.edit").set(customData.get("salesStage"));

		sugar().opportunities.createDrawer.save();
		sugar().opportunities.listView.clickRecord(1);
		contactSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.scrollIntoViewIfNeeded(false);
		contactSubpanel.linkExistingRecord(myContact);
	}

	/**
	 * Be able to access Saved Filters on the related module to see filtered views of related records.
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_17603_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-468, VOOD-486
		VoodooControl allRecords = new VoodooControl("div", "css", "[data-id='all_records']");

		// TODO: VOOD-486
		VoodooControl filterDropdown = new VoodooControl("a","css","span[data-voodoo-name='filter-filter-dropdown'] a");

		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// Verify the filter for all opportunities
		sugar().contacts.recordView.setRelatedSubpanelFilter(sugar().opportunities.moduleNamePlural);
		filterDropdown.click();
		allRecords.click();

		opportunitiesSubpanel.getDetailField(1, "name").assertEquals(testName, true);
		opportunitiesSubpanel.getDetailField(2, "name").assertEquals(sugar().opportunities.getDefaultData().get("name"), true);

		// Verify the filter for recently viewed opportunities
		filterDropdown.click();
		new VoodooControl("div", "css", "div[data-id='recently_viewed']").click();
		opportunitiesSubpanel.getDetailField(1, "name").assertEquals(testName, true);

		// TODO: VOOD-815, VOOD-486
		// Verify records for new filter
		filterDropdown.click();
		new VoodooControl("div", "css", "div[data-id='create']").click();
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set(customData.get("opportunityNameFltr"));
		new VoodooSelect("a", "css", ".detail.fld_filter_row_operator").set(customData.get("nameVerbFltr"));
		new VoodooControl("input", "css", ".fld_name input").set(customData.get("nameValFltr"));
		new VoodooControl("button", "css", "button[data-action='add']").click();
		new VoodooSelect("div", "css", "div[data-voodoo-name=filter-rows] div[data-filter=row]:nth-of-type(2) div[data-filter='field']").set(customData.get("probFldFltr"));
		new VoodooSelect("div", "css", "div[data-voodoo-name=filter-rows] div[data-filter=row]:nth-of-type(2) div[data-filter='operator']").set(customData.get("probVerbFltr"));
		new VoodooControl("input", "css", "div[data-voodoo-name=filter-rows] div[data-filter=row]:nth-of-type(2) div[data-filter='value'] input").set(customData.get("probValFltr"));
		new VoodooControl("input", "css", "div[data-voodoo-name='filter-actions'] input").set(customData.get("fltrName"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "div[data-voodoo-name='filter-actions'] a.save_button").click();
		VoodooUtils.waitForReady();
		opportunitiesSubpanel.getDetailField(1, "name").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
