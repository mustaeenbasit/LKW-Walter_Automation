package com.sugarcrm.test.contacts;

import junit.framework.Assert;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Contacts_17467 extends SugarTest {
	DataSource moduleDS;

	public void setup() throws Exception {
		moduleDS = testData.get(testName);
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Each Sub panel header will be combined from an Icon for the module name, module name and an action drop down in Contact sub panel
	 *  @throws Exception
	 */
	@Test
	public void Contacts_17467_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// Verify that selected ALL Related drop down
		sugar().contacts.recordView.getControl("relatedSubpanelChoice").assertEquals("All", true);

		for(int i=0; i<moduleDS.size()-3; i++){ // Special case to handle for Emails, CampaignLog, Quotes subpanels outside the loop until VOOD into resolved state
			StandardSubpanel subPanel = sugar().contacts.recordView.subpanels.get(moduleDS.get(i).get("module_plural_name"));

			// Verify module icon name and actions (create + link)
			// TODO: VOOD-1419
			String moduleIconHookValue = String.format("%s div.label-%s", subPanel.getHookString(),moduleDS.get(i).get("module_plural_name"));
			new VoodooControl("div", "css", moduleIconHookValue).assertEquals(moduleDS.get(i).get("module_icon_label"), true);
			subPanel.scrollIntoViewIfNeeded(false);
			subPanel.getControl("addRecord").assertVisible(true);
			subPanel.getControl("expandSubpanelActions").click();
			VoodooUtils.waitForReady();
			subPanel.getControl("linkExistingRecord").assertVisible(true);
			subPanel.getControl("expandSubpanelActions").click();
		}

		// TODO: VOOD-1418 and VOOD-1419
		// Verify emails module icon name and actions
		StandardSubpanel emailSubPanel = sugar().contacts.recordView.subpanels.get(moduleDS.get(9).get("module_plural_name"));
		emailSubPanel.scrollIntoViewIfNeeded(false);
		String emailIconHookValue = String.format("%s div.label-%s", emailSubPanel.getHookString(),moduleDS.get(9).get("module_plural_name"));
		new VoodooControl("div", "css", emailIconHookValue).assertEquals(moduleDS.get(9).get("module_icon_label"), true);
		new VoodooControl("a", "css", ".filtered.layout_Emails .fld_email_compose_button a").assertVisible(true);
		Assert.assertTrue("Expand subpanel is not in disabled state.", emailSubPanel.getControl("expandSubpanelActions").isDisabled());

		// Verify campaign log icon name and actions
		new VoodooControl("div", "css", ".layout_CampaignLog").scrollIntoViewIfNeeded(false);
		new VoodooControl("div", "css", ".filtered.layout_CampaignLog div.label-CampaignLog").assertEquals(moduleDS.get(10).get("module_icon_label"), true);
		VoodooControl addCampaign = new VoodooControl("a", "css", ".layout_CampaignLog .fld_panel_dropdown .fld_create_button a");
		VoodooControl linkCampaign = new VoodooControl("a", "css", ".layout_CampaignLog .fld_panel_dropdown a.dropdown-toggle");
		Assert.assertTrue("+ button is not in disabled state.", addCampaign.isDisabled());
		Assert.assertTrue("Expand subpanel is not in disabled state.", linkCampaign.isDisabled());

		// Verify quotes module icon name and actions
		new VoodooControl("div", "css", ".layout_Quotes").scrollIntoViewIfNeeded(false);
		new VoodooControl("div", "css", ".filtered.layout_Quotes div.label-Quotes").assertEquals(moduleDS.get(11).get("module_icon_label"), true);
		new VoodooControl("a", "css", ".layout_Quotes .fld_panel_dropdown .fld_create_button a").assertVisible(true);
		VoodooControl expandQuoteAction = new VoodooControl("a", "css", ".layout_Quotes .fld_panel_dropdown a.dropdown-toggle");
		expandQuoteAction.click();
		new VoodooControl("span", "css", ".layout_Quotes .fld_select_button").assertVisible(true);
		expandQuoteAction.click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
