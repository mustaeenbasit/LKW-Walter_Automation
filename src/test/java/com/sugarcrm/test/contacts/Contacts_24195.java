package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_24195 extends SugarTest {
	DataSource ds = new DataSource();
	ContactRecord myContact;

	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);

		FieldSet newData = new FieldSet();
		newData.put("firstName", ds.get(0).get("firstName"));
		newData.put("lastName", ds.get(0).get("lastName"));
		myContact = (ContactRecord) sugar().contacts.api.create(newData);

		sugar().campaigns.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().campaigns);
		sugar().campaigns.menu.getControl("createCampaignClassic").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "css", "#name").set(ds.get(1).get("name"));
		new VoodooControl("select", "css", "#status").click();
		new VoodooControl("select", "css", "#status option:nth-child(2)").click();
		new VoodooControl("select", "css", "#campaign_type").click();
		new VoodooControl("select", "css", "#campaign_type option:nth-child(9)").click();
		new VoodooControl("input", "css", ".dateTime #end_date").set(ds.get(1).get("date_end"));
		new VoodooControl("input", "css", ".action_buttons #SAVE_HEADER").click();

		new VoodooControl("span", "css", "span[sugar='slot1b']").click();
		sugar().alerts.waitForLoadingExpiration();
		StandardSubpanel contactsSubpanel = sugar().targetlists.recordView.subpanels.get("Contacts");
		contactsSubpanel.clickLinkExisting();
		sugar().alerts.waitForLoadingExpiration();
		// TODO: VOOD-942
		VoodooControl selectCheckbox = new VoodooControl("input", "css", ".single .list input");
		selectCheckbox.waitForVisible();
		selectCheckbox.click();
		new VoodooControl("a", "name", "link_button").click();
		sugar().alerts.getSuccess().closeAlert();
	}

	/**
	 * Manage Subscriptions_Verify that the selected newsletter for the contact can be unsubscribed.
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_24195_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myContact.navToRecord();
		sugar().contacts.recordView.openPrimaryButtonDropdown();
		new VoodooControl("ul", "css", ".fld_manage_subscription_button.detail").click();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl unsubscribedCtrl = new VoodooControl("ul", "css", "#disabled_ul");
		new VoodooControl("ul", "css", "#enabled_ul .noBullet2").dragNDrop(unsubscribedCtrl);

		// Verify selected contact is unsubscribed and displayed in "Available/Newsletters Unsubscribed To" column.
		// TODO: Should be removed when VOOD-984 is fixed
		VoodooUtils.pause(2000);
		unsubscribedCtrl.assertContains("Camp1", true);
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
