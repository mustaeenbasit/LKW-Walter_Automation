package com.sugarcrm.test.grimoire;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class RecordViewTests extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().login();
	}

	@Test
	public void toggleFavorite() throws Exception {
		VoodooUtils.voodoo.log.info("Running toggleFavorite test on recordView.");

		myAccount.navToRecord();
		VoodooControl favoriteButton = sugar().accounts.recordView.getControl("favoriteButton");

		// Verify favorite button is not active
		favoriteButton.assertAttribute("class", "fa fa-favorite");
		// Toggle favorite button
		sugar().accounts.recordView.toggleFavorite();

		// Verify favorite button is active
		favoriteButton.assertAttribute("class", "fa fa-favorite active");

		// Toggle favorite button
		sugar().accounts.recordView.toggleFavorite();

		// Verify favorite button is not active
		favoriteButton.assertAttribute("class", "fa fa-favorite");

		VoodooUtils.voodoo.log.info("toggleFavorite test on recordView completed.");
	}

	@Test
	public void relatedSubpanel() throws Exception {
		VoodooUtils.voodoo.log.info("Running relatedSubpanel test on recordView.");

		myAccount.navToRecord();
		StandardSubpanel conSub = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		FieldSet newContact = new FieldSet();
		newContact.put("firstName", "Test");
		newContact.put("lastName", "Contact");

		FieldSet newConVer = new FieldSet();
		newConVer.put("fullName", newContact.get("firstName") + " " + newContact.get("lastName"));

		conSub.addRecord();
		sugar().contacts.createDrawer.setFields(newContact);
		sugar().contacts.createDrawer.save();
		sugar().alerts.closeAllAlerts();
		sugar().alerts.waitForLoadingExpiration();

		sugar().accounts.recordView.setRelatedSubpanelFilter(sugar().contacts.moduleNamePlural);
		StandardSubpanel callsSub = sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callsSub.assertVisible(false); // Test

		// TODO: VOOD-1424 - Once resolved should use verify method
		//conSub.verify(1, newConVer, true); // Test
		conSub.getDetailField(1, "fullName").assertEquals(newConVer.get("fullName"), true);

		sugar().accounts.recordView.setRelatedSubpanelFilter("All");
		callsSub.assertVisible(true); // Test

		// TODO: VOOD-1424 - Once resolved should use verify method
		//conSub.verify(1, newConVer, true); // Test
		conSub.getDetailField(1, "fullName").assertEquals(newConVer.get("fullName"), true);

		VoodooUtils.voodoo.log.info("relatedSubpanel test on recordView completed.");
	}

	@Test
	public void toggleSidebar() throws Exception {
		VoodooUtils.voodoo.log.info("Running toggleSidebar test on recordView.");

		myAccount.navToRecord();

		sugar().accounts.recordView.toggleSidebar(); // Close the sidebar

		// TODO: VOOD-976
		VoodooControl rhsDashboard = new VoodooControl("div","css",".dashboard");
		rhsDashboard.assertVisible(false);

		sugar().accounts.recordView.toggleSidebar(); // Re-open the sidebar
		rhsDashboard.assertVisible(true);

		VoodooUtils.voodoo.log.info("toggleSidebar test on recordView completed.");
	}

	@Test
	public void gotoNextRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running gotoNextRecord test on recordView.");

		FieldSet fs = new FieldSet();
		// Create first account
		fs.put("name","Account1");
		sugar().accounts.api.create(fs);

		// Create second acount
		fs.put("name","Account2");
		sugar().accounts.api.create(fs);

		// Go to record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Verify that correct account is displayed 
		sugar().accounts.recordView.getDetailField("name").assertContains("Account2", true);

		// Go to next account in the record view 
		sugar().accounts.recordView.gotoNextRecord();

		// Verify that correct account is displayed
		sugar().accounts.recordView.getDetailField("name").assertContains("Account1", true);

		VoodooUtils.voodoo.log.info("gotoNextRecord test on recordView completed.");
	}

	@Test
	public void gotoPreviousRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running gotoNextRecord test on recordView.");

		FieldSet fs = new FieldSet();
		// Create first account
		fs.put("name","Account1");
		sugar().accounts.api.create(fs);

		// Create second acount
		fs.put("name","Account2");
		sugar().accounts.api.create(fs);

		// Go to record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(2);

		VoodooControl name = sugar().accounts.recordView.getDetailField("name");
		// Verify that correct account is displayed 
		name.assertContains("Account1", true);

		// Go to next account in the record view 
		sugar().accounts.recordView.gotoPreviousRecord();

		// Verify that correct account is displayed
		name.assertContains("Account2", true);

		VoodooUtils.voodoo.log.info("gotoNextRecord test on recordView completed.");
	}

	public void cleanup() throws Exception {}
}