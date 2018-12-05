package com.sugarcrm.test.documents;

import org.junit.Ignore;
import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.DocumentRecord;

public class Documents_update extends SugarTest {
	DocumentRecord myDocument;

	public void setup() throws Exception {
		sugar().login();
		myDocument = (DocumentRecord)sugar().documents.api.create();
	}

	@Test
	@Ignore("VOOD-2032 Fix Document CRUD's")
	public void Documents_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("documentName", "A New Doc Name");
		myDocument.edit(newData);
		myDocument.verify(newData);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}