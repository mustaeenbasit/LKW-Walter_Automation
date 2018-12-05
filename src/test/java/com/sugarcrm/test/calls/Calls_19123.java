package com.sugarcrm.test.calls;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Calls_19123 extends SugarTest {
	VoodooControl languageList;

	public void setup() throws Exception {
		// TODO: VOOD-999 & VOOD-1429
		// Log in with language Danish (Dansk)
		languageList = new VoodooControl("span", "id", "languageList");
		languageList.click();
		new VoodooControl("a", "css", "a[data-lang-key='da_DK']").click();
		VoodooUtils.waitForReady();
		sugar.login();
	}

	/**
	 * "Close and Create New" in non-English languages
	 * @throws Exception
	 */
	@Ignore("VOOD-1446, Fixed after VOOD-1429 is resolved.")
	@Test
	public void Calls_19123_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet callData = testData.get(testName).get(0);
		VoodooControl nameCtrl = sugar.calls.createDrawer.getEditField("name");
		sugar.calls.navToListView();
		sugar.calls.listView.create();
		nameCtrl.set(sugar.calls.getDefaultData().get("name"));

		sugar.calls.createDrawer.save();
		sugar.calls.listView.clickRecord(1);
		if(sugar.alerts.getSuccess().queryVisible())
			sugar.alerts.getSuccess().closeAlert();

		// Calls close and create
		sugar.calls.recordView.closeAndCreateNew();

		// another call drawer open and then, save call
		nameCtrl.set(callData.get("name"));
		sugar.calls.createDrawer.save();
		if(sugar.alerts.getSuccess().queryVisible())
			sugar.alerts.getSuccess().closeAlert();

		// previous call record status changed to "Held"
		sugar.calls.recordView.getDetailField("status").assertEquals(callData.get("status"), true);

		// TODO: 1416 - Once resolved below code is replaced by countRows
		// Verify 2 records only
		sugar.calls.navToListView();
		sugar.calls.listView.verifyField(1, "name", callData.get("name"));
		sugar.calls.listView.verifyField(2, "name", sugar.calls.getDefaultData().get("name"));
		sugar.calls.listView.getControl("checkbox03").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}