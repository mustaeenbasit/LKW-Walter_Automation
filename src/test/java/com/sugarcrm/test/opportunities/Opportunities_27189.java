package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Opportunities_27189 extends SugarTest {
	FieldSet massUpdateRecord;
	OpportunityRecord myOpp;
	AccountRecord myAccount;

	// Common VoodooControl and VoodooSelect references
	VoodooControl studioLink;
	VoodooControl studioLinkOpp;
	VoodooControl fieldsButton;
	VoodooControl accountLabel;
	VoodooControl publishButton;
	VoodooControl dragToRow;
	VoodooControl dragToFiller;

	public void setup() throws Exception {
		// TODO VOOD-517 Create Studio Module (BWC)
		studioLink = new VoodooControl("a", "id", "studio");
		studioLinkOpp = new VoodooControl("a", "id", "studiolink_Opportunities");
		fieldsButton = new VoodooControl("a", "css", "td#fieldsBtn a");
		accountLabel = new VoodooControl("input", "id", "input_LBL_ACCOUNT_NAME");
		publishButton = new VoodooControl("button", "id", "publishBtn");
		dragToRow = new VoodooControl("div", "css", ".le_panel:nth-of-type(1) .le_row:nth-of-type(2)");
		dragToFiller = new VoodooControl("span", "css", ".le_panel:nth-of-type(1) .le_row:nth-of-type(3) span");
		sugar().login();
		myAccount = (AccountRecord)sugar().accounts.api.create();

		// Create a Custom related field
		// TODO VOOD-517 Create Studio Module (BWC)
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioLink.click();
		studioLinkOpp.click();
		fieldsButton.click();
		new VoodooControl("input", "css", "input[name='addfieldbtn']").click();
		new VoodooControl("select", "id", "type").set("Relate");
		VoodooUtils.waitForReady();
		// TODO: Investigate why setting of this select is initializing field_name_id,
		//       hence relocated to this position, before field_name_id
		new VoodooControl("select", "id", "ext2").set("Accounts");
		new VoodooControl("input", "id", "field_name_id").set("testrelate");
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[name='fsavebtn']").click();
		VoodooUtils.waitForReady();

		// Add Custom related field and add it to Opportunity record view
		VoodooUtils.focusDefault();
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioLink.click();
		studioLinkOpp.click();
		new VoodooControl("a", "css", "td#layoutsBtn a").click();
		new VoodooControl("a", "css", "td#viewBtnrecordview a").click();
		new VoodooControl("div", "css", ".le_row.special").dragNDrop(dragToRow);
		new VoodooControl("div", "css", "div[data-name='testrelate_c']").dragNDrop(dragToFiller);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();

		// Add Custom related field and add it to Opportunity list view
		VoodooUtils.focusDefault();
		// Add custom relate field into list view
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioLink.click();
		studioLinkOpp.click();
		new VoodooControl("a", "css", "td#layoutsBtn a").click();
		new VoodooControl("a", "css", "td#viewBtnlistview a").click();
		VoodooUtils.waitForReady();
		VoodooControl subject = new VoodooControl("li", "css", "td#Default li:nth-of-type(2)");
		VoodooControl related = new VoodooControl("li", "css", "td#Hidden li[data-name='testrelate_c']");
		related.dragNDrop(subject);
		new VoodooControl("input", "css", "#savebtn").waitForVisible();
		new VoodooControl("input", "css", "#savebtn").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify custom related field is showing up in list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_27189_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooUtils.focusDefault();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		myOpp.navToRecord();
		sugar().opportunities.recordView.edit();

		// fill out required field
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		
		// TODO VOOD-964: Need lib support for "Related to" custom dropdown field on Opportunity record view
		// Under relate field, select "Search for more..." and select related account record
		new VoodooControl("a", "css", "span[data-voodoo-name='testrelate_c'] div.select2-container a.select2-choice.select2-default").click();
		new VoodooControl("div", "css", "div#select2-drop div.select2-result-label").click();
		new VoodooControl("input", "css", "#drawers .layout_Accounts .search-and-select tbody input").click();
		sugar().opportunities.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		sugar().opportunities.navToListView();
		new VoodooControl("span", "css", "div[data-voodoo-name='recordlist'] tbody tr:nth-of-type(1) span[data-voodoo-name='testrelate_c']").assertContains("Aperture Laboratories", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
