package com.sugarcrm.test.grimoire;

import com.sugarcrm.test.SugarTest;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class DupCheckTest extends SugarTest {
	String websiteStr = "";

	public void setup() throws Exception {
		// Create a record to be the duplicate match.
		websiteStr = "http://www.sugarcrm.com/";
		FieldSet record = new FieldSet();
		record.put("website", websiteStr);
		sugar().accounts.api.create(record);
		sugar().login();
	}

	@Test
	public void verifyDuplicateRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyDuplicateRecord()...");

		// Create a new Account record using custom data via UI
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.showMore();
		sugar().accounts.createDrawer.setFields(sugar().accounts.getDefaultData());
		sugar().accounts.createDrawer.save();
		sugar().accounts.createDrawer.getControl("duplicateCount").assertEquals(testData.get("env_dupe_assertion").get(0).get("dupe_check"), true);
		sugar().accounts.createDrawer.getControl("duplicateHeaderRow").assertExists(true);
		VoodooControl websiteFieldCtrl = sugar().accounts.createDrawer.getEditField("website");
		websiteFieldCtrl.assertEquals(sugar().accounts.getDefaultData().get("website"), true);
		sugar().accounts.createDrawer.selectAndEditDuplicate(1);
		websiteFieldCtrl.assertEquals(websiteStr, true);
		sugar().accounts.createDrawer.resetToOriginal();
		websiteFieldCtrl.assertEquals(sugar().accounts.getDefaultData().get("website"), true);
		sugar().accounts.createDrawer.cancel();

		VoodooUtils.voodoo.log.info("verifyDuplicateRecord() complete.");
	}

	public void cleanup() throws Exception {}
}