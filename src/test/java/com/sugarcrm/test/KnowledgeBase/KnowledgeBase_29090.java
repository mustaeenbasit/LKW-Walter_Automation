package com.sugarcrm.test.KnowledgeBase;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.QuoteRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Shagun Kaushik  <skaushik@sugarcrm.com>
 */
public class KnowledgeBase_29090 extends SugarTest {
	VoodooControl studioLink, studioLinkKB;
	DataSource customData = new DataSource();
	QuoteRecord quote1;

	public void setup() throws Exception {
		FieldSet quoteRecords = new FieldSet();
		customData = testData.get(testName);

		// Creating two Quotes
		quoteRecords.put("name", customData.get(0).get("quote"));
		quote1 = (QuoteRecord) sugar().quotes.api.create(quoteRecords);
		quoteRecords.clear();
		quoteRecords.put("name", customData.get(1).get("quote"));
		sugar().quotes.api.create(quoteRecords);
		quoteRecords.clear();

		// Creating a KB Article
		sugar().knowledgeBase.api.create();

		// Logging in as admin
		sugar().login();

		// Enable KB module
		sugar().admin.enableSubpanelDisplayViaJs(sugar().knowledgeBase);

		// TODO: VOOD-1505
		studioLinkKB = new VoodooControl("a", "id", "studiolink_KBContents");
		studioLink = sugar().admin.adminTools.getControl("studio");

		// Creating a custom Many-to-Many relationship KB-Quotes
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioLink.click();
		VoodooUtils.waitForReady();
		studioLinkKB.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1505
		new VoodooControl("td", "id", "relationshipsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='addrelbtn']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "id", "rhs_mod_field").set(sugar().quotes.moduleNamePlural);
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[name=saverelbtn]").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that records in the sub panel appear correctly when add relation of KB
	 * and a BWC (here Quotes) module
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29090_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.waitForReady();

		// BWC Subpanel control VOOD
		// TODO: VOOD-1382
		VoodooControl kbSubpanel = new VoodooControl("div", "css", "#list_subpanel_kbcontents_quotes_1");
		VoodooControl kbDropDown = new VoodooControl("span", "css", "#list_subpanel_kbcontents_quotes_1 .ab");
		VoodooControl kbSelectButton = new VoodooControl("a", "id", "kbcontents_quotes_1_select_button");

		kbSubpanel.scrollIntoViewIfNeeded(false);
		kbDropDown.click();
		kbSelectButton.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);

		// Clicking the first element in KB list
		new VoodooControl("a", "css", "tr.oddListRowS1 td:nth-child(3) a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.waitForReady();

		VoodooControl firstKB = new VoodooControl("a", "css", "#list_subpanel_kbcontents_quotes_1"
				+ " tr.oddListRowS1 td:nth-child(1) a");
		firstKB.scrollIntoViewIfNeeded(false);

		// Verify that the KB is visible in subpanel
		firstKB.assertEquals(sugar().knowledgeBase.getDefaultData().get("name"), true);

		firstKB.click();
		VoodooUtils.focusDefault();

		// Verifying that the user is navigated to the record view of the above KB
		sugar().knowledgeBase.recordView.getDetailField("name")
			.assertEquals(sugar().knowledgeBase.getDefaultData().get("name"), true);

		// Linking the KB with Quote (from Subpanel)
		new VoodooControl("a", "css", ".layout_Quotes .dropdown-toggle").click();
		new VoodooControl("li", "css", ".layout_Quotes .actions li").click();
		VoodooUtils.waitForReady();
		sugar().quotes.searchSelect.selectRecord(quote1);
		sugar().quotes.searchSelect.link();
		VoodooUtils.waitForReady();

		// Verify that the second Quote is visible in KB Record View's Quote Subpanel
		new VoodooControl("a", "css", ".layout_Quotes tr:nth-child(2) .fld_name a")
			.assertEquals(customData.get(0).get("quote"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
