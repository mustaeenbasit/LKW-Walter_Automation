package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_17162  extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		sugar().login();

		ds = testData.get(testName);
	}

	/**
	 * Available operators for Date, DateTime type field when defining filter
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_17162_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contacts.navToListView();
		sugar().contacts.listView.openFilterDropdown();
		sugar().contacts.listView.selectFilterCreateNew();

		// Select Date field
		// TODO: VOOD-1879 - Need Individual Controls for Filter Fields in List View of Sidecar modules.
		new VoodooControl("span", "css", ".filter-definition-container div div div:nth-child(1) span div .select2-arrow").click();
		new VoodooControl("div", "css", "#select2-drop .select2-results li:nth-of-type(15)").click();
		new VoodooControl("div", "css", "div[data-filter=operator]").click();

		// Verify operators
		new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-of-type(1)").assertEquals(ds.get(0).get("is_equal_to"), true);
		new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-of-type(2)").assertEquals(ds.get(0).get("before"), true);
		new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-of-type(3)").assertEquals(ds.get(0).get("after"), true);
		new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-of-type(4)").assertEquals(ds.get(0).get("yesterday"), true);
		new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-of-type(5)").assertEquals(ds.get(0).get("today"), true);
		new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-of-type(6)").assertEquals(ds.get(0).get("tomorrow"), true);
		new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-of-type(7)").assertEquals(ds.get(0).get("last_7_days"), true);
		new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-of-type(8)").assertEquals(ds.get(0).get("next_7_days"), true);
		new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-of-type(9)").assertEquals(ds.get(0).get("last_30days"), true);
		new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-of-type(10)").assertEquals(ds.get(0).get("next_30days"), true);
		new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-of-type(11)").assertEquals(ds.get(0).get("last_month"), true);
		new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-of-type(12)").assertEquals(ds.get(0).get("this_month"), true);
		new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-of-type(13)").assertEquals(ds.get(0).get("next_month"), true);
		new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-of-type(14)").assertEquals(ds.get(0).get("last_year"), true);
		new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-of-type(15)").assertEquals(ds.get(0).get("this_year"), true);
		new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-of-type(16)").assertEquals(ds.get(0).get("next_year"), true);
		new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-of-type(17)").assertEquals(ds.get(0).get("is_between"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
