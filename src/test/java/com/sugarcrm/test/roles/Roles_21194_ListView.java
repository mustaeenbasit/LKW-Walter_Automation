package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21194_ListView extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		
		// Login as Admin User
		sugar().login();
	}

	/**
	 * field_permissions_multiple_roles_R/W_None
	 * @throws Exception
	 */
	@Test
	public void Roles_21194_ListView_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet assertRoles = testData.get(testName + "_AssertFields").get(0);
		FieldSet roleNone = testData.get(testName).get(0);
		FieldSet roleReadWrite = testData.get(testName).get(1);
		
		// TODO: VOOD-856
		VoodooControl oddCell, evenCell, optionNone, optionReadWrite, saveRole, casesLink;
		VoodooSelect selectDropDown;
		saveRole = new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON");
		casesLink = new VoodooControl("a", "css", ".edit tr:nth-child(5) a");

		// Create RoleNone with permission None for all the fields other than Required Fields in Cases Module
		AdminModule.createRole(roleNone);
		VoodooUtils.focusFrame("bwc-frame");
		casesLink.click();
		VoodooUtils.waitForReady();
		int columns, row = 0;

		// Count number of Rows in the fieldPermission table
		while (new VoodooControl("tr", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+(row+1)+")").queryExists()){
			row++;
		}

		// Count number of Columns in the fieldPermission table
		int columnsInRow[] = new int[row];
		for(int i=0;i<row;i++){
			columns=0;
			while(new VoodooControl("td", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+(i+1)+") "
					+ "td:nth-child("+(columns+1)+")").queryExists()){
				columns++;
			}
			columnsInRow[i] = columns;
		}

		// Set the permission for all the non-required fields to None
		for(int matrixRow=1; matrixRow<=row; matrixRow++ ){
			for(int matrixCol=1; matrixCol<=columnsInRow[(matrixRow-1)]; matrixCol+=2){
				oddCell = new VoodooControl("td", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+matrixRow+") "
						+ "td:nth-child("+matrixCol+")");
				evenCell = new VoodooControl("td", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+matrixRow+") "
						+ "td:nth-child("+(matrixCol+1)+") div:nth-child(2)");
				selectDropDown = new VoodooSelect("td", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+matrixRow+") "
						+ "td:nth-child("+(matrixCol+1)+") div select");
				optionNone = new VoodooControl("option", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+matrixRow+") "
						+ "td:nth-child("+(matrixCol+1)+") select option:nth-child(6)");

				if(oddCell.queryContains(assertRoles.get("requiredField"), false)){
				}
				else{
					evenCell.click();
					VoodooUtils.waitForReady();
					if(!(selectDropDown).queryVisible()){
						evenCell.click();
					}
					selectDropDown.click();
					optionNone.click();
				}
			}
		}

		// Click Save button to save the roleNone
		saveRole.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign roleNone to qaUser
		AdminModule.assignUserToRole(sugar().users.getQAUser());

		// Create roleReadWrite with permission read/write for all the fields in Cases Module
		AdminModule.createRole(roleReadWrite);
		VoodooUtils.focusFrame("bwc-frame");
		casesLink.click();
		VoodooUtils.waitForReady();

		// Set the field permission for all the fields to Read/Write
		for(int matrixRow=1; matrixRow<=row; matrixRow++ ){
			for(int matrixCol=1; matrixCol<=columnsInRow[(matrixRow-1)]; matrixCol+=2){
				evenCell = new VoodooControl("td", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+matrixRow+") "
						+ "td:nth-child("+(matrixCol+1)+") div:nth-child(2)");
				selectDropDown = new VoodooSelect("td", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+matrixRow+") "
						+ "td:nth-child("+(matrixCol+1)+") select");
				optionReadWrite = new VoodooControl("option", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+matrixRow+") "
						+ "td:nth-child("+(matrixCol+1)+") select option:nth-child(2)");

				evenCell.click();
				if(!(selectDropDown).queryVisible()){
					evenCell.click();
				}
				selectDropDown.click();
				optionReadWrite.click();
			}
		}

		// Click Save button to save the roleReadWrite 
		saveRole.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign roleReadWrite to qaUser
		AdminModule.assignUserToRole(sugar().users.getQAUser());

		// Create a Case record assigned to qaUser and Team is Global
		FieldSet caseData = new FieldSet();
		caseData.put("relAssignedTo", sugar().users.getQAUser().get("userName"));
		sugar().cases.create(caseData);

		// logout from Admin
		sugar().logout();

		// Login as qaUser
		sugar().login(sugar().users.getQAUser());
		sugar().cases.navToListView();
		sugar().cases.listView.getControl("moreColumn").click();

		// TODO: VOOD-1161
		VoodooControl teamsHeader = new VoodooControl("button", "css", "[data-field-toggle='team_name']");
		if (!teamsHeader.getChildElement("i", "class", "fa-check").queryVisible()) {
			teamsHeader.click();
		}
		sugar().cases.listView.toggleSidebar();

		// Assert that only required fields are visible by qaUser
		sugar().cases.listView.verifyField(1, "name", sugar().cases.getDefaultData().get("name"));
		sugar().cases.listView.getDetailField(1, "caseNumber").assertExists(true);

		// TODO: VOOD-1445
		new VoodooControl("div", "css", ".fld_account_name div").assertEquals(sugar().accounts.getDefaultData().get("name"), true);
		new VoodooControl("span", "css", ".fld_priority.noaccess span").assertEquals(assertRoles.get("noAccess"), true);
		new VoodooControl("span", "css", ".fld_status.noaccess span").assertEquals(assertRoles.get("noAccess"), true);
		new VoodooControl("span", "css", ".fld_assigned_user_name.noaccess span").assertEquals(assertRoles.get("noAccess"), true);
		new VoodooControl("span", "css", ".fld_date_modified.noaccess span").assertEquals(assertRoles.get("noAccess"), true);
		new VoodooControl("span", "css", ".fld_date_entered.noaccess span").assertEquals(assertRoles.get("noAccess"), true);

		// TODO: VOOD-1161
		new VoodooControl("span", "css", ".fld_team_name").assertContains(assertRoles.get("caseTeam"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}