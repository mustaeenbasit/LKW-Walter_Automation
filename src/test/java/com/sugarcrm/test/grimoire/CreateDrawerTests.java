package com.sugarcrm.test.grimoire;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class CreateDrawerTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void saveTests() throws Exception {
		VoodooUtils.voodoo.log.info("Running saveTests()...");

		FieldSet accountData1 = new FieldSet();
		accountData1.put("name", "Test Account 1");

		AccountRecord myAcc1 = (AccountRecord)sugar().accounts.create(accountData1);
		assertTrue("The guid for the create with SAVE was empty!", !(myAcc1.getGuid().isEmpty()));

		VoodooUtils.voodoo.log.info("saveTests() complete.");
	}

	@Test
	public void ignoreDuplicateAndSaveTest() throws Exception {
		VoodooUtils.voodoo.log.info("Running ignoreDuplicateAndSaveTest()...");

		FieldSet accountData1 = new FieldSet();
		accountData1.put("name", "Test Account 1");

		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(accountData1.get("name"));
		sugar().accounts.createDrawer.save();

		sugar().accounts.listView.create();
		// create duplicate account with accountData1
		sugar().accounts.createDrawer.getEditField("name").set(accountData1.get("name"));
		sugar().accounts.createDrawer.save();

		// Verify ignore and duplicate save button appears 
		sugar().accounts.createDrawer.getControl("ignoreDuplicateAndSaveButton").assertVisible(true);
		sugar().accounts.createDrawer.ignoreDuplicateAndSave();

		VoodooUtils.voodoo.log.info("ignoreDuplicateAndSaveTest() complete.");
	}

	@Test
	public void cancelTest() throws Exception {
		VoodooUtils.voodoo.log.info("Running cancelTest()...");

		FieldSet accountData1 = new FieldSet();
		accountData1.put("name", "Test Account 1");
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(accountData1.get("name"));
		sugar().accounts.createDrawer.cancel();

		VoodooUtils.voodoo.log.info("cancelTest() complete.");
	}

	@Test
	public void toggleSidebarTest() throws Exception {
		VoodooUtils.voodoo.log.info("Running toggleSidebarTest()...");

		FieldSet accountData1 = new FieldSet();
		accountData1.put("name", "Test Account 1");
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getControl("toggleSidebar").assertVisible(true);

		VoodooUtils.voodoo.log.info("toggleSidebarTest() complete.");
	}

	public void cleanup() throws Exception {}
}