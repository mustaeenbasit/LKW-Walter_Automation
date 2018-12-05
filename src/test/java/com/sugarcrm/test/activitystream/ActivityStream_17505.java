package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_17505 extends SugarTest {
	AccountRecord myAcc;

	public void setup() throws Exception {
		sugar.login();
		myAcc = (AccountRecord) sugar.accounts.api.create();
	}

	/**
	 * no Create link in activity stream filters list.
	 * 
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_17505_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);

		// check filter on home activity stream page
		sugar.navbar.selectMenuItem(sugar.home, "activityStream");

		// TODO VOOD-955
		new VoodooControl("div", "css", "div.layout_Activities span:nth-of-type(2) div").click();
		new VoodooControl("li", "css", "div.select2-drop-active ul.select2-results li").assertEquals(ds.get(0).get("filter"), false);
		new VoodooControl("i", "css", "div.select2-drop-active ul.select2-results i.fa-plus").assertExists(false);
		// click on the default filter to close the dropdown list, otherwise can't navigate away 
		new VoodooControl("li", "css", "div.select2-drop-active ul.select2-results li").click();

		// TODO VOOD-954 the clean up will fail if not change back to dashboard page
		sugar.navbar.clickModuleDropdown(sugar.home);
		// TODO VOOD-953
		new VoodooControl("a", "css", "li[data-module='Home'] ul[role='menu'] li:nth-of-type(5) a").click();	

		// check filter on module list activity stream
		sugar.accounts.navToListView();
		sugar.accounts.listView.showActivityStream();
		sugar.alerts.waitForLoadingExpiration();
		// TODO VOOD-955
		new VoodooControl("div", "css", "div.layout_Accounts span:nth-of-type(2) div").click();
		new VoodooControl("li", "css", "div.select2-drop-active ul.select2-results li").assertEquals(ds.get(0).get("filter"), false);
		new VoodooControl("i", "css", "div.select2-drop-active ul.select2-results i.fa-plus").assertExists(false);
		// click on the default filter to close the dropdown list, otherwise can't navigate away 
		new VoodooControl("li", "css", "div.select2-drop-active ul.select2-results li").click();
		sugar.accounts.listView.showListView();

		// check filter on record view activity stream
		myAcc.navToRecord();
		sugar.accounts.recordView.showActivityStream();
		sugar.alerts.waitForLoadingExpiration();
		// TODO VOOD-955
		new VoodooControl("i", "css", "div.layout_Accounts span:nth-of-type(2) div a span i").click();
		new VoodooControl("li", "css", "div.select2-drop-active ul.select2-results li").assertEquals(ds.get(0).get("filter"), false);
		new VoodooControl("i", "css", "div.select2-drop-active ul.select2-results i.fa-plus").assertExists(false);
		// click on the default filter to close the dropdown list, otherwise can't navigate away 
		new VoodooControl("li", "css", "div.select2-drop-active ul.select2-results li").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}