package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Opportunities_24379 extends SugarTest {
	
	public void setup() throws Exception {
		// Create two opportunity records with different name
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		fs.put("type", "New Business");
		sugar().opportunities.api.create(fs);
		sugar().opportunities.api.create();
		AccountRecord myAccRecord = (AccountRecord) sugar().accounts.api.create();
		sugar().login();
		
		// Update one record "type" as "New Business" so that filter can able to Find Duplicate
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(2);
		sugar().opportunities.recordView.edit();
		sugar().opportunities.recordView.getEditField("relAccountName").set(myAccRecord.getRecordIdentifier());
		sugar().opportunities.recordView.showMore();
		sugar().opportunities.recordView.getEditField("type").set("New Business");
		sugar().opportunities.recordView.save();
	}

	/**
	 * Merge Duplicate_Verify that the merging record can be set as primary record.
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24379_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Find Duplicate" button in the opportunity detail view.
		sugar().opportunities.recordView.gotoPreviousRecord();
		sugar().opportunities.recordView.openPrimaryButtonDropdown();
		
		// TODO: VOOD-695
		new VoodooControl("a", "css", "[data-voodoo-name='find_duplicates_button']").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-568
		VoodooControl settingCtrl = new VoodooControl("i", "css", "[data-action='fields-toggle'] .fa.fa-cog");
		settingCtrl.click();
		new VoodooControl("li", "css", "[data-field-toggle='opportunity_type']").click();
		settingCtrl.click();
		
		// For filter
		VoodooControl filterCtrl = new VoodooControl("i", "css", "[data-voodoo-name='dupecheck-filter-dropdown'] .select2-choice-type .fa.fa-caret-down");
		filterCtrl.waitForVisible();
		filterCtrl.click();
		new VoodooControl("li", "css", ".search-filter-dropdown .select2-results li:nth-child(1)").click(); // click create
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").waitForVisible();
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set("Type");
		new VoodooSelect("span", "css", ".detail.fld_filter_row_operator").waitForVisible();
		new VoodooSelect("span", "css", ".detail.fld_filter_row_operator").set("is any of");
		VoodooUtils.waitForReady();
		new VoodooSelect("span", "css", ".detail.fld_opportunity_type div:nth-child(1)").set("New Business");
		VoodooUtils.waitForReady();
		
		// Select duplicate Record
		new VoodooControl("input", "css", "[data-voodoo-name='dupecheck-list-multiselect'] table tr:nth-child(1) td:nth-of-type(1) input").click();
		new VoodooControl("input", "css", "[name='merge_duplicates_button']").click();
		VoodooUtils.waitForReady();
		
		// Click on all radio buttons for the right record which is merging record.
		new VoodooControl("input", "css", ".row-div-cnt > div:nth-child(2) [name='copy_name']").click();
		new VoodooControl("input", "css", ".row-div-cnt > div:nth-child(2) [name='copy_account_name']").click();
		new VoodooControl("input", "css", ".row-div-cnt > div:nth-child(2) [name='copy_opportunity_type']").click();
		new VoodooControl("input", "css", ".row-div-cnt > div:nth-child(2) [name='copy_date_closed']").click();
		new VoodooControl("input", "css", ".row-div-cnt > div:nth-child(2) [name='copy_amount']").click();
		new VoodooControl("input", "css", ".row-div-cnt > div:nth-child(2) [name='copy_best_case']").click();
		new VoodooControl("input", "css", ".row-div-cnt > div:nth-child(2) [name='copy_worst_case']").click();
		new VoodooControl("input", "css", ".row-div-cnt > div:nth-child(2) [name='copy_next_step']").click();
		new VoodooControl("input", "css", ".row-div-cnt > div:nth-child(2) [name='copy_opportunity_type']").click();
		new VoodooControl("input", "css", ".row-div-cnt > div:nth-child(2) [name='copy_lead_source']").click();
		new VoodooControl("input", "css", ".row-div-cnt > div:nth-child(2) [name='copy_campaign_name']").click();
		new VoodooControl("input", "css", ".row-div-cnt > div:nth-child(2) [name='copy_description']").click();
		
		// Click on save button for merge two records
		new VoodooControl("a", "css", "span.merge-duplicates-headerpane.fld_save_button a").click();
		sugar().alerts.confirmAllWarning();
		VoodooUtils.waitForReady();

		sugar().opportunities.navToListView();
		
		// Merge Duplicate_Verify that the merging record can be set as primary record
		// Verify that opportunity mentioned in step3 is deleted
		sugar().opportunities.listView.verifyField(1, "name", testName);
		sugar().opportunities.listView.assertContains(sugar().opportunities.getDefaultData().get("name"), false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}