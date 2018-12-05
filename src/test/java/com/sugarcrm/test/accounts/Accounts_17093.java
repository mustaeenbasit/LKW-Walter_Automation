package com.sugarcrm.test.accounts;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

import org.junit.Ignore;
import org.junit.Test;

public class Accounts_17093 extends SugarTest {

	AccountRecord account1;
	DataSource ds;

	public void setup() throws Exception {
		sugar().login();
		account1 = (AccountRecord) sugar().accounts.api.create();
		ds = testData.get(testName);
		FieldSet newData = new FieldSet();
		newData.put("description", ds.get(0).get("des"));
		account1.edit(newData);
	}

	/**
	 * Verify using "...more" and "...less" to hide and display the contents in the OOTB text area fields in the record view
	 * @throws Exception
	 */
	@Ignore("VOOD-594")
	@Test
	public void Accounts_17093_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.recordView.getDetailField("description").assertContains(ds.get(0).get("des1")+ds.get(0).get("suffix"), true);

		// TODO VOOD-594
		new VoodooControl("a", "css", ".record-edit-link-wrapper[data-name='description'] a").click();
		sugar().accounts.recordView.getDetailField("description").assertContains(ds.get(0).get("des")+ds.get(0).get("suffix"), true);

		new VoodooControl("a", "css", ".record-edit-link-wrapper[data-name='description'] a").click();
		sugar().accounts.recordView.getDetailField("description").assertContains(ds.get(0).get("des1")+ds.get(0).get("suffix"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}