package com.sugarcrm.test.tags;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TagRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Tags_create extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	@Test
	public void Tags_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		TagRecord tag1 = (TagRecord)sugar.tags.create();
		tag1.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}