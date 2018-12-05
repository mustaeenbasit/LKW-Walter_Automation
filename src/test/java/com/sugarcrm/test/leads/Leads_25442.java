package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Leads_25442 extends SugarTest{
	LeadRecord leadRecord;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify Website and Twitter ID are copied in Account and Contact record when Lead convert completes
	 * @throws Exception
	 */
	@Test
	public void Leads_25442_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running" + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		FieldSet leadsData = new FieldSet();
		leadsData.put("lastName", customData.get("lastName"));
		leadsData.put("website", customData.get("website"));
		leadsData.put("accountName", customData.get("accountName"));

		sugar().leads.navToListView();
		sugar().leads.listView.create();
		sugar().leads.createDrawer.setFields(leadsData);
		sugar().leads.createDrawer.save();

		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.edit();
		sugar().leads.recordView.showMore();

		// TODO: VOOD-578, VOOD-710
		new VoodooControl("input", "css", ".fld_twitter.edit input").set(customData.get("twitterName"));
		sugar().leads.recordView.save();

		// Convert Lead
		sugar().leads.navToListView();
		sugar().leads.listView.openRowActionDropdown(1);

		// TODO: VOOD-585
		new VoodooControl("a", "css", ".fld_lead_convert_button.list a").click();
		sugar().alerts.waitForLoadingExpiration();

		// Save the conversion
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		sugar().alerts.waitForLoadingExpiration();

		// Preview of Contact record
		new VoodooControl("a", "css", "table.converted-results tbody tr:nth-of-type(1) .preview-list-cell a").click();
		sugar().alerts.waitForLoadingExpiration();

		// show more
		new VoodooControl("button", "css", ".preview-data .more").click();

		// Verify, Twitter account is copied 
		new VoodooControl("div", "css", ".preview-data .fld_twitter.detail div").assertEquals(customData.get("twitterName"), true);

		// Preview of Account record
		new VoodooControl("a", "css", "table.converted-results tbody tr:nth-of-type(2) .preview-list-cell a").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify, Website & twitter account is copied
		new VoodooControl("a", "css", ".preview-data .fld_website.detail a").assertEquals(customData.get("website"), true);
		new VoodooControl("div", "css", ".preview-data .fld_twitter.detail div").assertEquals(customData.get("twitterName"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}