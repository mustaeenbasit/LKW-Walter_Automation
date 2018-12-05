package com.sugarcrm.test.documents;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.DocumentRecord;
import com.sugarcrm.test.SugarTest;

public class Documents_19062 extends SugarTest {
	String documentName = "";
	VoodooControl globalSearchTextField, firstSearchResult;
	DocumentRecord myDocument;
		
	public void setup() throws Exception {
		sugar().login();
		
		myDocument = (DocumentRecord) sugar().documents.api.create();
		documentName = myDocument.getRecordIdentifier();
		
		// TODO VOOD-668
		globalSearchTextField = new VoodooControl("input", "css", ".search-query");
		firstSearchResult = new VoodooControl("h3", "css", ".search-result .ellipsis_inline.primary h3");
	}

	/**
	 * 19062 Verify Global search works for Document module
	 * @throws Exception
	 */
	@Test
	public void Documents_19062_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		globalSearchTextField.set(documentName);

		// TODO VOOD-669
		firstSearchResult.waitForVisible();
		firstSearchResult.assertContains(documentName, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}