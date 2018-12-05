package com.sugarcrm.test.tags;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TagRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Tags_delete extends SugarTest {
	TagRecord tag1;

	public void setup() throws Exception {
		sugar.login();
		tag1 = (TagRecord)sugar.tags.api.create();
	}

	@Test
	public void Tags_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the account using the UI.
		tag1.delete();

		// Verify the account was deleted.
		sugar.tags.navToListView();
		assertEquals(VoodooUtils.contains(tag1.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}