package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21254 extends SugarTest {
	FieldSet testFS = new FieldSet();

	public void setup() throws Exception {
		sugar().accounts.api.create();
		FieldSet roleData = testData.get("env_role_setup").get(0);
		testFS = testData.get(testName).get(0);

		// Login as Admin User
		sugar().login();

		// Create a new role
		AdminModule.createRole(roleData);
		VoodooUtils.focusFrame("bwc-frame");

		// Click "Accounts" to view the "Field Permissions"
		// TODO: VOOD-856 - Lib is needed for Roles Management
		new VoodooControl("a", "css", ".edit tr:nth-child(2) a").click();
		VoodooUtils.waitForReady();

		// Set all field permissions to "None" * except for Name and Teams (set to "Read Only")
		int columns, row = 0;
		// Count number of Rows in the fieldPermission table
		while (new VoodooControl("tr", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child(" + (row + 1) + ")").queryExists()) {
			row++;
		}

		// Count number of Columns in the fieldPermission table
		int columnsInRow[] = new int[row];
		for(int i = 0; i < row; i++) {
			columns = 0;
			while(new VoodooControl("td", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child(" + (i + 1) + ") td:nth-child(" + (columns + 1) + ")").queryExists()) {
				columns++;
			}
			columnsInRow[i] = columns;
		}

		// Set all the permission for all the non-required fields to 'None'
		// TODO: VOOD-856 - Lib is needed for Roles Management
		VoodooControl oddCell, evenCell, optionNone, optionReadOnly;
		VoodooSelect selectDropDown;
		for(int matrixRow = 1; matrixRow <= row; matrixRow++) {
			for(int matrixCol = 1; matrixCol <= columnsInRow[(matrixRow-1)]; matrixCol += 2) {
				oddCell = new VoodooControl("td", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child(" + matrixRow + ") td:nth-child(" + matrixCol + ")");
				evenCell = new VoodooControl("td", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child(" + matrixRow + ") td:nth-child(" + (matrixCol + 1) +") div:nth-child(2)");
				selectDropDown = new VoodooSelect("td", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child(" + matrixRow + ") td:nth-child(" + (matrixCol + 1) +") div select");
				optionNone = new VoodooControl("option", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child(" + matrixRow + ") td:nth-child(" + (matrixCol + 1) +") select option:nth-child(6)");
				optionReadOnly = new VoodooControl("option", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child(" + matrixRow + ") td:nth-child(" + (matrixCol + 1) +") select option:nth-child(4)");

				// For Name and Teams (set to "Read Only")
				if(oddCell.queryContains(testFS.get("requiredField"), false)) {
					evenCell.click();
					VoodooUtils.waitForReady();
					if(!(selectDropDown).queryVisible()) {
						evenCell.click();
					}
					selectDropDown.click();				
					optionReadOnly.click();
				} else {
					evenCell.click();
					VoodooUtils.waitForReady();
					if(!(selectDropDown).queryVisible()) {
						evenCell.click();
					}
					selectDropDown.click();				
					optionNone.click();
				}
			}
		}

		// Save the role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign roleNone to QAUser
		AdminModule.assignUserToRole(roleData);

		// Logout and login as QAUser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Field permissions set to None on Accounts - List view, detail view, and edit view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_21254_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click on Accounts module to display the list view
		sugar().accounts.navToListView();

		// Define control for 'No Access' field on list view and on record view
		// TODO: VOOD-1445 - Need lib support for enhanced disabled check in parent controls of a child
		VoodooControl nameFieldCtrl = sugar().accounts.recordView.getDetailField("name");
		VoodooControl teamFieldCtrl = sugar().accounts.recordView.getDetailField("relTeam");
		VoodooControl billingCityCtrl = new VoodooControl("span", "css", ".fld_billing_address_city.noaccess span");
		VoodooControl billingCountryCtrl = new VoodooControl("span", "css", ".fld_billing_address_country.noaccess span");
		VoodooControl phoneCtrl = new VoodooControl("span", "css", ".fld_phone_office.noaccess span");
		VoodooControl assignedUserCtrl = new VoodooControl("span", "css", ".fld_assigned_user_name.noaccess span");
		VoodooControl emailCtrl = new VoodooControl("span", "css", ".fld_email.noaccess span");
		VoodooControl dateModifiedCtrl = new VoodooControl("span", "css", ".fld_date_modified.noaccess span");
		VoodooControl dateCreatedCtrl = new VoodooControl("span", "css", ".fld_date_entered.noaccess span");
		VoodooControl websiteCtrl = new VoodooControl("span", "css", ".fld_website.noaccess span");
		VoodooControl industryCtrl = new VoodooControl("span", "css", ".fld_industry.noaccess span");
		VoodooControl memberOfCtrl = new VoodooControl("span", "css", ".fld_parent_name.noaccess span");
		VoodooControl accountTypeCtrl = new VoodooControl("span", "css", ".fld_account_type.noaccess span");
		VoodooControl tagCtrl = new VoodooControl("span", "css", ".fld_tag.noaccess span");
		VoodooControl billingAddressCtrl = new VoodooControl("span", "css", ".fld_billing_address.noaccess span");
		VoodooControl shippingAddressCtrl = new VoodooControl("span", "css", ".fld_shipping_address.noaccess span");
		VoodooControl alternatePhoneCtrl = new VoodooControl("span", "css", ".fld_phone_alternate.noaccess span");
		VoodooControl faxPhoneCtrl = new VoodooControl("span", "css", ".fld_phone_fax.noaccess span");
		VoodooControl campaignCtrl = new VoodooControl("span", "css", ".fld_campaign_name.noaccess span");
		VoodooControl twitterCtrl = new VoodooControl("span", "css", ".fld_twitter.noaccess span");
		VoodooControl descriptionCtrl = new VoodooControl("span", "css", ".fld_description.noaccess span");
		VoodooControl sicCodeCtrl = new VoodooControl("span", "css", ".fld_sic_code.noaccess span");
		VoodooControl tickerSymbolCtrl = new VoodooControl("span", "css", ".fld_ticker_symbol.noaccess span");
		VoodooControl annualRevenueCtrl = new VoodooControl("span", "css", ".fld_annual_revenue.noaccess span");
		VoodooControl employeesCtrl = new VoodooControl("span", "css", ".fld_employees.noaccess span");
		VoodooControl ownershipCtrl = new VoodooControl("span", "css", ".fld_ownership.noaccess span");
		VoodooControl ratingCtrl = new VoodooControl("span", "css", ".fld_rating.noaccess span");
		VoodooControl dunsCtrl = new VoodooControl("span", "css", ".fld_duns_num.noaccess span");
		VoodooControl dateModifiedRecordViewCtrl = new VoodooControl("span", "css", ".fld_date_modified_by");
		VoodooControl dateCreatedRecordViewCtrl = new VoodooControl("span", "css", ".fld_date_entered_by");

		// Verify that the account "Name" is the only value displayed on the list view
		String accountName = sugar().accounts.getDefaultData().get("name");
		sugar().accounts.listView.getDetailField(1, "name").assertEquals(accountName, true);
		billingCityCtrl.assertEquals(testFS.get("noAccess"), true);
		billingCountryCtrl.assertEquals(testFS.get("noAccess"), true);
		phoneCtrl.assertEquals(testFS.get("noAccess"), true);
		assignedUserCtrl.assertEquals(testFS.get("noAccess"), true);
		emailCtrl.assertEquals(testFS.get("noAccess"), true);
		dateModifiedCtrl.assertEquals(testFS.get("noAccess"), true);
		dateCreatedCtrl.assertEquals(testFS.get("noAccess"), true);

		// Click on any Account to bring up the detailed view
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.showMore();

		// Only "Name" and "Teams" field should be displayed
		nameFieldCtrl.assertEquals(accountName, true);
		teamFieldCtrl.assertContains(testFS.get("global"), true);
		phoneCtrl.assertEquals(testFS.get("noAccess"), true);
		assignedUserCtrl.assertEquals(testFS.get("noAccess"), true);
		emailCtrl.assertEquals(testFS.get("noAccess"), true);
		websiteCtrl.assertEquals(testFS.get("noAccess"), true);
		industryCtrl.assertEquals(testFS.get("noAccess"), true);
		memberOfCtrl.assertEquals(testFS.get("noAccess"), true);
		accountTypeCtrl.assertEquals(testFS.get("noAccess"), true);
		tagCtrl.assertEquals(testFS.get("noAccess"), true);
		billingAddressCtrl.assertEquals(testFS.get("noAccess"), true);
		shippingAddressCtrl.assertEquals(testFS.get("noAccess"), true);
		alternatePhoneCtrl.assertEquals(testFS.get("noAccess"), true);
		faxPhoneCtrl.assertEquals(testFS.get("noAccess"), true);
		campaignCtrl.assertEquals(testFS.get("noAccess"), true);
		twitterCtrl.assertEquals(testFS.get("noAccess"), true);
		descriptionCtrl.assertEquals(testFS.get("noAccess"), true);
		sicCodeCtrl.assertEquals(testFS.get("noAccess"), true);
		tickerSymbolCtrl.assertEquals(testFS.get("noAccess"), true);
		annualRevenueCtrl.assertEquals(testFS.get("noAccess"), true);
		employeesCtrl.assertEquals(testFS.get("noAccess"), true);
		ownershipCtrl.assertEquals(testFS.get("noAccess"), true);
		ratingCtrl.assertEquals(testFS.get("noAccess"), true);
		dunsCtrl.assertEquals(testFS.get("noAccess"), true);
		dateModifiedRecordViewCtrl.assertEquals(testFS.get("noData"), true);
		dateCreatedRecordViewCtrl.assertEquals(testFS.get("noData"), true);

		// Click the "Edit" button
		sugar().accounts.recordView.edit();

		// None of the Account fields should be editable
		nameFieldCtrl.assertContains(accountName, true);
		teamFieldCtrl.assertContains(testFS.get("global"), true);
		phoneCtrl.assertEquals(testFS.get("noAccess"), true);
		assignedUserCtrl.assertEquals(testFS.get("noAccess"), true);
		emailCtrl.assertEquals(testFS.get("noAccess"), true);
		websiteCtrl.assertEquals(testFS.get("noAccess"), true);
		industryCtrl.assertEquals(testFS.get("noAccess"), true);
		memberOfCtrl.assertEquals(testFS.get("noAccess"), true);
		accountTypeCtrl.assertEquals(testFS.get("noAccess"), true);
		tagCtrl.assertEquals(testFS.get("noAccess"), true);
		billingAddressCtrl.assertEquals(testFS.get("noAccess"), true);
		shippingAddressCtrl.assertEquals(testFS.get("noAccess"), true);
		alternatePhoneCtrl.assertEquals(testFS.get("noAccess"), true);
		faxPhoneCtrl.assertEquals(testFS.get("noAccess"), true);
		campaignCtrl.assertEquals(testFS.get("noAccess"), true);
		twitterCtrl.assertEquals(testFS.get("noAccess"), true);
		descriptionCtrl.assertEquals(testFS.get("noAccess"), true);
		sicCodeCtrl.assertEquals(testFS.get("noAccess"), true);
		tickerSymbolCtrl.assertEquals(testFS.get("noAccess"), true);
		annualRevenueCtrl.assertEquals(testFS.get("noAccess"), true);
		employeesCtrl.assertEquals(testFS.get("noAccess"), true);
		ownershipCtrl.assertEquals(testFS.get("noAccess"), true);
		ratingCtrl.assertEquals(testFS.get("noAccess"), true);
		dunsCtrl.assertEquals(testFS.get("noAccess"), true);
		dateModifiedRecordViewCtrl.assertEquals(testFS.get("noData"), true);
		dateCreatedRecordViewCtrl.assertEquals(testFS.get("noData"), true);

		// Cancel the Accounts edit view
		sugar().accounts.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}