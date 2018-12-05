package com.sugarcrm.test.integration.googledocs;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Googledocs_10758 extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		sugar().login();
		myAccount = (AccountRecord) sugar().accounts.api.create();
		AdminModule.enableGoogledocsConnector();
	}

	/**
	 * Verify a user can link a Google Doc file to Accounts by Documents
	 * subpanel
	 *
	 * @throws Exception
	 */
	@Test
	public void Googledocs_10758_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myAccount.navToRecord();
		sugar().accounts.recordView.showDataView();
		// TODO - These VoodooControls and pauses will become part of Subpanels and Documents when
		// VOOD-419 is implemented
		new VoodooControl("a", "css",
				"div[data-voodoo-name='Documents'] .fld_create_button.panel-top a")
				.click();
		VoodooUtils.pause(2000);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("select", "id", "doc_type").set("Google Docs");
		new VoodooControl("h4", "id", "filename_externalApiLabel").click();
		new VoodooControl("input", "id", "filename_remoteName")
				.assertVisible(true);
		// TODO - When VOOD-419 - Documents Module - is implemented a test for
		// linking a Document will be created
		new VoodooControl("input", "id", "CANCEL_HEADER").click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
