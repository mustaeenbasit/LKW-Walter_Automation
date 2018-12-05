package com.sugarcrm.test.tags;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TagRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Tags_update extends SugarTest {
	TagRecord tag1;

	public void setup() throws Exception {
		sugar.login();
		tag1 = (TagRecord)sugar.tags.api.create();
	}

	@Test
	public void Tags_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("name", "Edited Tag 1");

		// Delete the account using the UI.
		tag1.edit(newData);

		// Verify the account was edited.
		tag1.verify(newData);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}