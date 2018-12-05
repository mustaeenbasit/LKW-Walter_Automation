package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_16998 extends SugarTest {	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that phone type field has default max width of 100 characters 
	 * @throws Exception
	 */
	@Test
	public void Accounts_16998_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		DataSource ds = testData.get(testName);
		sugar().accounts.createDrawer.showMore();
		sugar().accounts.createDrawer.getEditField("name").set(ds.get(0).get("name"));
		sugar().accounts.createDrawer.getEditField("workPhone").assertAttribute("maxlength", ds.get(0).get("maxlength"));
		sugar().accounts.createDrawer.getEditField("fax").assertAttribute("maxlength", ds.get(0).get("maxlength"));
		sugar().accounts.createDrawer.getEditField("workPhone").set(ds.get(0).get("officephoneinput"));
		sugar().accounts.createDrawer.getEditField("fax").set(ds.get(0).get("officephoneinput"));
		sugar().accounts.createDrawer.getEditField("website").click();
		sugar().accounts.createDrawer.getEditField("workPhone").assertEquals(ds.get(0).get("officephonesaved"), true);
		sugar().accounts.createDrawer.getEditField("fax").assertEquals(ds.get(0).get("officephonesaved"), true);
		sugar().accounts.createDrawer.cancel();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
