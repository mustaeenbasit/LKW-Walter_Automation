package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_28094 extends SugarTest {
	VoodooControl moduleCtrl;

	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().documents.api.create();
		sugar().login();
	}

	/**
	 * To Verify that Many to many relationship between Leads and Document in studio.
	 * @throws Exception
	 */
	@Test
	public void Studio_28094_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Admin > Studio > Leads > Relationship
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-542
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Leads");
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "relationshipsBtn").click();
		VoodooUtils.waitForReady();

		// Create many to many relationship between Leads and Documents perform save and deploy.
		new VoodooControl("input", "css", "[name='addrelbtn']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("option", "css", "select#rhs_mod_field option[value='Documents']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='saverelbtn']").click();
		VoodooUtils.waitForReady(30000); // Extra wait needed as Loading... message is there 
		VoodooUtils.focusDefault();

		// Navigate to the leads module, create or open an existing lead.
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// Verify that in the leads module there is document subpanel
		// TODO: VOOD-1382
		new VoodooControl("div", "css", ".filtered.layout_Documents").assertContains(sugar().documents.moduleNamePlural, true);
		// sugar().leads.recordView.subpanels.get(sugar().documents.moduleNamePlural).assertExists(true);

		// Navigate to the documents module and open existing document. 
		sugar().documents.navToListView();
		sugar().documents.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that in the documents module there is leads subpanel
		// TODO: VOOD-1382
		new VoodooControl("div", "id", "subpanel_title_leads_documents_1").assertElementContains(sugar().leads.moduleNamePlural, true);
		// sugar().documents.detailView.subpanels.get(sugar().leads.moduleNamePlural).assertExists(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}