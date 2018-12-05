package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Meetings_26987 extends SugarTest {
	VoodooControl moreColCtrl;
	DataSource headerDS;

	public void setup() throws Exception {
		headerDS = testData.get(testName);
		sugar().login();
	}

	/**
	 * Verify that default presentable columns and hidden columns in Meetings list view
	 * @throws Exception
	 */
	@Test
	public void Meetings_26987_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();

		// TODO: VOOD-1251 - Need logic changed with commented lines, after this VOOD# fix
		/*for(String header : sugar().meetings.listView.getHeaders()) {
			sugar().meetings.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
			VoodooControl columnHeader = sugar().meetings.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header)));
			String headerClass = columnHeader.getAttribute("class");
			VoodooUtils.voodoo.log.info(header + " has class value of: " + headerClass);
		}*/

		// Verify header columns in listview
		for(int i=0; i<=4; i++){ // Initially 5 headers (i.e subject, status, date start, user, date created) are active
			String internalHeaderStr = headerDS.get(i).get("internal_column_name");
			VoodooControl headerColumnCtrl = new VoodooControl("th", "css", "th[data-fieldname='"+internalHeaderStr+"']");
			headerColumnCtrl.assertAttribute("class", "orderBy"+internalHeaderStr+"", true);
			headerColumnCtrl.assertContains(headerDS.get(i).get("header_name"), true);
		}

		// Verify Related to column header (i.e no sort option available)
		new VoodooControl("th", "css", "th[data-fieldname='"+headerDS.get(7).get("internal_column_name")+"']").assertContains(headerDS.get(7).get("header_name"), true);

		// More column
		moreColCtrl = new VoodooControl("div", "css", "th.morecol div");
		moreColCtrl.click();

		// Verify date end and team  is inactive and make it as active
		for(int j=5; j<=6; j++){ // check for 2 fields
			VoodooControl headerColumnCtrl = new VoodooControl("button", "css", "button[data-field-toggle='"+headerDS.get(j).get("internal_column_name")+"']");
			headerColumnCtrl.assertAttribute("class", "active", false);
			headerColumnCtrl.click();
		}
		moreColCtrl.click(); // to close dropdown

		// Goto contacts module
		sugar().contacts.navToListView();

		// Back to Meetings module
		sugar().meetings.navToListView();

		// Verify default header columns including dateEnd, team fields also
		for(int j=0; j<=6; j++){ // check for 7 fields
			String internalHeaderStr = headerDS.get(j).get("internal_column_name");
			new VoodooControl("button", "css", "button[data-field-toggle='"+internalHeaderStr+"']").assertAttribute("class", "active", true);
			VoodooControl restHeaderCtrl = new VoodooControl("th", "css", "th[data-fieldname='"+internalHeaderStr+"']");
			restHeaderCtrl.assertAttribute("class", "orderBy"+internalHeaderStr+"", true);
			restHeaderCtrl.assertContains(headerDS.get(j).get("header_name"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}