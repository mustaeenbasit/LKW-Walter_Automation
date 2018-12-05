package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ActivityStreamVsListView extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void ActivityStreamVsListView_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();
		// Go to Activity Stream View
		sugar().accounts.listView.showActivityStream();

		// TODO: VOOD-474
		// verify activityStream view
		new VoodooControl("button", "css", ".inputactions button").assertVisible(true);

		// Go to ListView View
		sugar().accounts.listView.showListView();

		// verify listView view
		// TODO: VOOD-1887
		new VoodooControl("div", "css", "div[data-voodoo-name=Accounts]").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
