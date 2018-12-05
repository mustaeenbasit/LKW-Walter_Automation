package com.sugarcrm.test.roles;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21194_RecordAndEditView extends SugarTest {
	public void setup() throws Exception {
		// Create Account record because it is required while creating Case
		sugar().accounts.api.create();

		// Login as Admin Users
		sugar().login();
	}

	/**
	 * field_permissions_multiple_roles_R/W_None (RecordView and EditView)
	 * @throws Exception
	 */
	@Test
	@Ignore("BR-3098 - 'Required' is getting Shown in 'Account Name' field instead of selected Account in Edit View of Cases Module.")
	public void Roles_21194_RecordAndEditView_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Define DataSource and FieldSet
		DataSource	roles = testData.get(testName);
		FieldSet roleNone = roles.get(0);
		FieldSet roleReadWrite = roles.get(1);
		FieldSet casesDefaultData = sugar().cases.getDefaultData();
		FieldSet accountsDefaultData = sugar().accounts.getDefaultData();

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

				if(oddCell.queryContains(roleNone.get("requiredField"), false)){
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

		// Assert that only required fields are visible to qaUser in record view
		sugar().cases.listView.clickRecord(1);
		sugar().cases.recordView.showMore();
		sugar().cases.recordView.getDetailField("name").assertEquals(casesDefaultData.get("name"), true);
		sugar().cases.recordView.getDetailField("caseNumber").assertVisible(true);
		sugar().cases.recordView.getDetailField("relAccountName").assertEquals(accountsDefaultData.get("name"), true);

		// TODO: VOOD-1161
		new VoodooControl("div", "css", ".detail.fld_team_name div").assertContains(roleNone.get("caseTeam"), true);

		// TODO: VOOD-1445
		// Define Controls
		VoodooControl priorityAccessCtrl = new VoodooControl("span", "css", ".fld_priority.noaccess span");
		VoodooControl statusAccessCtrl = new VoodooControl("span", "css", ".fld_status.noaccess span");
		VoodooControl assignedUserAccessCtrl = new VoodooControl("span", "css", ".fld_assigned_user_name.noaccess span");
		VoodooControl typeAccessCtrl = new VoodooControl("span", "css", ".fld_type.noaccess span");
		VoodooControl portalAccessCtrl = new VoodooControl("span", "css", ".fld_portal_viewable.noaccess span");
		VoodooControl sourceAccessCtrl = new VoodooControl("span", "css", ".fld_source.noaccess span");
		VoodooControl descriptionAccessCtrl = new VoodooControl("span", "css", ".fld_description.noaccess span");
		VoodooControl resolutionAccessCtrl = new VoodooControl("span", "css", ".fld_resolution.noaccess span");

		priorityAccessCtrl.assertEquals(roleNone.get("noAccess"), true);
		statusAccessCtrl.assertEquals(roleNone.get("noAccess"), true);
		assignedUserAccessCtrl.assertEquals(roleNone.get("noAccess"), true);
		typeAccessCtrl.assertEquals(roleNone.get("noAccess"), true);
		portalAccessCtrl.assertEquals(roleNone.get("noAccess"), true);
		sourceAccessCtrl.assertEquals(roleNone.get("noAccess"), true);
		descriptionAccessCtrl.assertEquals(roleNone.get("noAccess"), true);
		resolutionAccessCtrl.assertEquals(roleNone.get("noAccess"), true);

		// Assert that only required fields are visible to qaUser in edit view
		sugar().cases.recordView.edit();
		sugar().cases.recordView.getEditField("name").assertEquals(casesDefaultData.get("name"), true);
		sugar().cases.recordView.getEditField("caseNumber").assertVisible(true);
		sugar().cases.recordView.getEditField("relAccountName").assertEquals(accountsDefaultData.get("name"), true);

		// TODO: VOOD-1161
		new VoodooControl("span", "css", ".edit.fld_team_name .select2-chosen").assertContains(roleNone.get("caseTeam"), true);

		priorityAccessCtrl.assertEquals(roleNone.get("noAccess"), true);
		statusAccessCtrl.assertEquals(roleNone.get("noAccess"), true);
		assignedUserAccessCtrl.assertEquals(roleNone.get("noAccess"), true);
		typeAccessCtrl.assertEquals(roleNone.get("noAccess"), true);
		portalAccessCtrl.assertEquals(roleNone.get("noAccess"), true);
		sourceAccessCtrl.assertEquals(roleNone.get("noAccess"), true);
		descriptionAccessCtrl.assertEquals(roleNone.get("noAccess"), true);
		resolutionAccessCtrl.assertEquals(roleNone.get("noAccess"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}