package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Studio_18930 extends SugarTest {
	FieldSet studioRecord;
	ContactRecord myContact;
	// Common VoodooControl and VoodooSelect references
	VoodooControl studioLink;
	VoodooControl studioLinkContacts;
	VoodooControl labelsButton;
	VoodooControl accountLabel;
	VoodooControl publishButton;

	public void setup() throws Exception {
		// Initialize the common Control references
		// TODO VOOD-517 Create Studio Module (BWC) - will provide the reference
		// to replace these explicit VoodooControls
		studioLink = new VoodooControl("a", "id", "studio");
		studioLinkContacts = new VoodooControl("a", "id", "studiolink_Contacts");
		labelsButton = new VoodooControl("a", "css", "td#labelsBtn a");
		accountLabel = new VoodooControl("input", "id",
				"input_LBL_ACCOUNT_NAME");
		publishButton = new VoodooControl("button", "id", "publishBtn");

		studioRecord = testData.get("Studio_18930").get(0);

		sugar().login();

		myContact = (ContactRecord) sugar().contacts.api.create();
	}

	/**
	 * Verify the Label of an existing field can be changed in Studio
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_18930_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		studioLink.click();

		studioLinkContacts.click();

		labelsButton.click();

		accountLabel.set((studioRecord.get("newLabel")));

		new VoodooControl("button", "id", "publishBtn").click();

		// At this point the Browser refresh need to be invoked for the latest
		// Studio changes to display
		// This Refresh takes about 50 seconds
		VoodooUtils.refresh();
		VoodooUtils.pause(50000);

		// Navigate to the Contacts screen to verify the Account label change
		myContact.navToRecord();
		// TODO VOOD-517 Create Studio Module (BWC) - will provide the reference
		// to replace this explicit VoodooControl
		new VoodooControl("div", "css", "div[data-name=account_name]")
				.assertContains((studioRecord.get("newLabel")), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
