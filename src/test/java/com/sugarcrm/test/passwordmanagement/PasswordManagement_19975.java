package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_19975 extends SugarTest {
	DataSource ds, ds1;

	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);
		sugar().admin.passwordSettings(ds.get(0));
	}

	/**
	 * verify password rules message shows.
	 * 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_19975_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		ds1 = testData.get(testName + "_1");

		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("edit").waitForVisible();
		VoodooUtils.focusDefault();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("passwordTab").click();
		
		// verify password rules are listed with red X icon
		// TODO VOOD-947
		new VoodooControl("td", "css", "div#generate_password table.x-sqs-list").assertElementContains(ds1.get(0).get("assert1"), true);
		new VoodooControl("div", "css", "div#generate_password table.x-sqs-list div.bad[id='1upcase']").assertVisible(true);
		new VoodooControl("td", "css", "div#generate_password table.x-sqs-list").assertElementContains(ds1.get(0).get("assert2"), true);
		new VoodooControl("div", "css", "div#generate_password table.x-sqs-list div.bad[id='1lowcase']").assertVisible(true);
		new VoodooControl("td", "css", "div#generate_password table.x-sqs-list").assertElementContains(ds1.get(0).get("assert3"), true);
		new VoodooControl("div", "css", "div#generate_password table.x-sqs-list div.bad[id='1number']").assertVisible(true);
		new VoodooControl("td", "css", "div#generate_password table.x-sqs-list").assertElementContains(ds1.get(0).get("assert4"), true);
		new VoodooControl("div", "css", "div#generate_password table.x-sqs-list div.bad[id='lengths']").assertVisible(true);	
		
		VoodooUtils.focusDefault();
		sugar().users.editView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}