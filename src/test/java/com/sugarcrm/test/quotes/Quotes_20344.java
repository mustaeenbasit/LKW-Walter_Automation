package com.sugarcrm.test.quotes;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Quotes_20344 extends SugarTest {

	public void setup() throws Exception {
		sugar().quotes.api.create();
		sugar().login();
	}

	/**
	 * Verify that newly created saved view is displayed in drop down lists both in the navigation shortcuts and the "Saved Search & Layout" tab
	 * 
	 * @throws Exception
	 */
	@Test
	public void Quotes_20344_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);
		sugar().quotes.navToListView();

		// Click on Advanced Search
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.listView.getControl("advancedSearchLink").click();
		VoodooUtils.waitForReady();

		// Conditions for the saved search
		// TODO: VOOD-975
		new VoodooControl("input", "id", "name_advanced").set(testName);
		new VoodooControl("option", "css", "#assigned_user_id_advanced option:nth-child(2)").click();
		new VoodooControl("option", "css", "#quote_stage_advanced option:nth-of-type(3)").click();
		new VoodooControl("input", "css", "input[name=saved_search_name]").set(customFS.get("searchName"));
		new VoodooControl("input", "css", "input[name=saved_search_submit]").click();
		VoodooUtils.waitForReady();

		// Verify saved search in dropdown and records are filtered accordingly
		VoodooControl savedSearchDropdown = new VoodooControl("select", "id", "saved_search_select");
		savedSearchDropdown.assertContains(customFS.get("searchName"), true);
		VoodooUtils.focusDefault();
		Assert.assertTrue("Quote record is not equals zero", sugar().quotes.listView.countRows() == 0);

		// Verify records with default "none" are filtered accordingly
		savedSearchDropdown.set(customFS.get("noneFilter"));
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		Assert.assertTrue("Quote record is not equals one", sugar().quotes.listView.countRows() == 1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}