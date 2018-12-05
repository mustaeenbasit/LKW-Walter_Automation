package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_17649 extends SugarTest {
	DataSource ds;
	
	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().accounts.api.create(ds.get(0));
		sugar().login();
	}

	/**
	 * reset to original create pane from edit duplicate record when found duplicate records during create account.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17649_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// navigate to accounts module
		sugar().accounts.navToListView();
		// click create button
		sugar().accounts.listView.create();
		// set name as Acc, input data in other fields 
		sugar().accounts.createDrawer.getEditField("name").set(ds.get(1).get("name"));
		sugar().accounts.createDrawer.getEditField("workPhone").set(ds.get(1).get("workPhone"));
		sugar().accounts.createDrawer.showMore();
		sugar().accounts.createDrawer.getEditField("description").set(ds.get(1).get("description"));
		
		// click save button
		sugar().accounts.createDrawer.save();
		
		// Assert that duplicate check panel opened
		sugar().accounts.createDrawer.getControl("duplicateCount").assertEquals(testData.get("env_dupe_assertion").get(0).get("dupe_check"), true);
		sugar().accounts.createDrawer.getControl("duplicateHeaderRow").assertExists(true);
		
		// Click the Select button on the specified row of the dup check panel.
		sugar().accounts.createDrawer.selectAndEditDuplicate(1);
		
		// Verify that all the fields that Acc1 has data should show Acc1's data
		sugar().accounts.createDrawer.getEditField("name").assertEquals(ds.get(0).get("name"), true);
		sugar().accounts.createDrawer.getEditField("website").assertContains(ds.get(0).get("website"), true);
		
		// Assert taht all fields that Acc1 has no data but the user has entered data in the new create pane
		// then that data should be show in account edit pane
		sugar().accounts.createDrawer.getEditField("workPhone").assertEquals(ds.get(1).get("workPhone"), true);
		sugar().accounts.createDrawer.getEditField("description").assertEquals(ds.get(1).get("description"), true);
		
		// click Reset to Original link
		sugar().accounts.createDrawer.resetToOriginal();
		
		// Assert that Acc create pane back to original, reset the data in the pane to how it was when the user entered it in the first place
		sugar().accounts.createDrawer.getEditField("name").assertEquals(ds.get(1).get("name"), true);
		sugar().accounts.createDrawer.getEditField("workPhone").assertEquals(ds.get(1).get("workPhone"), true);
		sugar().accounts.createDrawer.getEditField("description").assertEquals(ds.get(1).get("description"), true);
		sugar().accounts.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}