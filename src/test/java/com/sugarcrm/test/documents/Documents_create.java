package com.sugarcrm.test.documents;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.DocumentRecord;
import com.sugarcrm.test.SugarTest;

public class Documents_create extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	@Ignore("VOOD-2032 Fix Document CRUD's")
	public void Documents_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
	
		DocumentRecord myDocument = (DocumentRecord)sugar().documents.create();
		myDocument.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}