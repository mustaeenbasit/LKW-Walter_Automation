package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Accounts_22898 extends SugarTest {
	FieldSet massUpdateRecord;

	public void setup() throws Exception {
		massUpdateRecord = testData.get("Accounts_22898").get(0);
		sugar().login();
		sugar().accounts.api.create();
		sugar().accounts.api.create();
		sugar().accounts.api.create();
	}

	/**
	 * Mass Update Account_Verify that the selected accounts information is mass
	 * updated
	 * @throws Exception
	 */
	@Test
	public void Accounts_22898_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();

		FieldSet massUpdateData = new FieldSet();
		massUpdateData.put("Industry", massUpdateRecord.get("industry"));
		massUpdateData.put("Type", massUpdateRecord.get("type"));

		sugar().accounts.listView.checkRecord(1);
		sugar().accounts.listView.checkRecord(2);
		sugar().accounts.massUpdate.performMassUpdate(massUpdateData);
		sugar().alerts.waitForLoadingExpiration();

		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getDetailField("industry").assertEquals(
				massUpdateRecord.get("industry"), true);
		sugar().accounts.recordView.getDetailField("type").assertEquals(
				massUpdateRecord.get("type"), true);

		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(2);
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getDetailField("industry").assertEquals(
				massUpdateRecord.get("industry"), true);
		sugar().accounts.recordView.getDetailField("type").assertEquals(
				massUpdateRecord.get("type"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}