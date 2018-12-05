package com.sugarcrm.test.emails;

import org.junit.Test;
import org.junit.Ignore;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.QuoteRecord;

public class Emails_26512 extends SugarTest {
	QuoteRecord myQuote;
	FieldSet myArchiveEmail;
	FieldSet emailSettings = new FieldSet();
	
	public void setup() throws Exception {
		sugar.quotes.api.create();
		myArchiveEmail = testData.get("Emails_26512").get(0);	
		sugar.login();
	}

	/**
	 * TC-26512: Email Address is auto-populated in Quote record view 
	 * @throws Exception
	 */ 
	@Test
	public void Emails_26512_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.quotes.navToListView();
		sugar.quotes.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		
		//TODO: VOOD-826 -Lib support for History sub panel in quotes record view
		new VoodooControl("span", "css", "div#list_subpanel_history span.ab").click();
		new VoodooControl("a", "css", "ul.subnav.ddopen a[id='ArchiveEmail_button']").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
		
		// TODO: VOOD-797. Lib support to handle compose archive email in new email composer
		new VoodooControl("i", "css", "div.input-append i.fa.fa-calendar").click();
		new VoodooControl("td", "css", ".datepicker-days td[class='day active']").click();
		new VoodooControl("i", "css", "div.input-append i.fa.fa-clock-o").click();
		new VoodooControl("li", "css", "ul.ui-timepicker-list li").click();
		new VoodooControl("input", "css", ".fld_from_address input").set(myArchiveEmail.get("from"));
		new VoodooControl("input", "css", ".fld_to_addresses input").set(myArchiveEmail.get("to"));
		new VoodooControl("div", "css", "div.select2-result-label").click();
		VoodooUtils.pause(1000); // Required
		new VoodooControl("input", "css", ".fld_subject input").set(myArchiveEmail.get("subject"));

		// TODO: VOOD-808 - Can not input text in iframe (blank) in Email Composer's body
		// VoodooUtils.focusFrame(0);
		// new VoodooControl("body", "id", "body#tinymce.mceContentBody").set(myArchiveEmail.get("message"));
		// VoodooUtils.focusDefault();

		new VoodooControl("a", "css", ".fld_archive_button a").click();
		VoodooUtils.pause(1000); // Required
		
		//TODO: VOOD-826 - Lib support for History sub panel in quotes record view
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "div#list_subpanel_history").assertContains(myArchiveEmail.get("subject"), true);
		new VoodooControl("span", "css", "div#list_subpanel_history tr span.pageNumbers").assertEquals("(1 - 1 of 1)", true);
		VoodooUtils.focusDefault();
				
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
