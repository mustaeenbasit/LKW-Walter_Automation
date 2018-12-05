package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.QuoteRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24434 extends SugarTest {
	QuoteRecord myQuote;
	OpportunityRecord myOpp;
	StandardSubpanel quotesSubpanel;
	FieldSet quotesFieldSet;
	DataSource ds;

	public void setup() throws Exception {
		sugar().login();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		sugar().accounts.api.create();
		
		ds = testData.get(testName);
		quotesFieldSet = new FieldSet();
		quotesFieldSet.put("name",ds.get(0).get("name"));
		
		myQuote = (QuoteRecord) sugar().quotes.api.create(quotesFieldSet);
		
		// Link Quote with existing Opportunity Record.
		myOpp.navToRecord();
		quotesSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().quotes.moduleNamePlural);
		
		// TODO: VOOD-1207 
		// uncommnet line 42 and remove code line 43-50 after fixing the VOOD-1207 to link the record 
		//quotesSubpanel.linkExistingRecord(myQuote);
		
		quotesSubpanel.getControl("expandSubpanelActions").click();
		VoodooUtils.pause(300);
		quotesSubpanel.getControl("linkExistingRecord").click();
		sugar().alerts.waitForLoadingExpiration(50000);
		new VoodooControl("div", "css", ".fld_name.list div").click();
		VoodooUtils.pause(1000);
		new VoodooControl("a", "css", "[name='link_button']").click();
		sugar().alerts.getProcess();
		VoodooUtils.pause(5000);
		
	}

	/**
	 * Unlink Quote_Verify that quote can be unlinked from opportunity by using "Unlink" function on quotes subpanel.
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24434_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Unlink Quote
		// TODO: VOOD-1214
		// uncomment line 65 and remove line 66-67 after fixing the VOOD-1214 to unlink the record 
		// quotesSubpanel.unlinkRecord(1);
		new VoodooControl("span", "css", ".filtered.layout_Quotes tbody tr:nth-of-type(1) .fa.fa-caret-down").click();
		new VoodooControl("a", "css", "div.filtered.tabbable.tabs-left.layout_Quotes div.flex-list-view-content  li a").click();
		sugar().alerts.getWarning().confirmAlert();
		
		
		// Verify that unlinked Quote no longer exists in subpanel
		quotesSubpanel.expandSubpanel();
		quotesSubpanel.assertContains(myQuote.getRecordIdentifier(), false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}