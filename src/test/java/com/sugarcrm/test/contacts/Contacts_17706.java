package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Contacts_17706 extends SugarTest {
	ContactRecord contactRecord;
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		ds = testData.get(testName);
		contactRecord = (ContactRecord) sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Verify that Saved Filter name will appear as the second Tag and the result set will apply that filter on Related Records.
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_17706_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		contactRecord.navToRecord();

		// Select a module in "Related", such as Cases.
		sugar().contacts.recordView.setRelatedSubpanelFilter(sugar().cases.moduleNamePlural);
		sugar().alerts.waitForLoadingExpiration();

		// TODO: VOOD-486
		VoodooControl filterLabel = new VoodooControl("span", "css", ".choice-filter-label");
		filterLabel.click();
		new VoodooSelect("input", "css", ".filter-definition-container div div:nth-child(1) > span").set(ds.get(0).get("field_name"));
		new VoodooSelect("span", "css", ".filter-definition-container div:nth-child(2) > span").set(ds.get(0).get("operator"));
		new VoodooSelect("span", "css", ".filter-definition-container div:nth-child(3) span").set(ds.get(0).get("value"));
		new VoodooControl("input", "css", ".filter-header .controls.span6 input").set(ds.get(0).get("filter_name"));

		// Save custom filter
		new VoodooControl("a", "css", ".btn.btn-primary.save_button").waitForVisible();
		new VoodooControl("a", "css", ".btn.btn-primary.save_button").click();
		sugar().alerts.waitForLoadingExpiration();

		// TODO: VOOD-486
		// Verify the saved filter name becomes the 2nd tag after Filter.
		new VoodooControl("span", "css", "span:nth-child(2) div.search-filter").click();
		new VoodooControl("div", "css", ".search-filter-dropdown li:nth-child(2)").waitForVisible();
		new VoodooControl("div", "css", ".search-filter-dropdown li:nth-child(2)").assertContains(ds.get(0).get("filter_name"), true);
		new VoodooControl("div", "css", ".search-filter-dropdown li:nth-child(2)").click();
		filterLabel.waitForVisible();
		filterLabel.assertEquals(ds.get(0).get("filter_name"), true);

		// Remove custom filter
		filterLabel.click();
		new VoodooControl("a", "css", ".btn-invisible.delete_button").click();
		sugar().alerts.getAlert().confirmAlert(); // should also set filter state to it's default
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info(testName + " complete.");

	}

	public void cleanup() throws Exception {}
}
