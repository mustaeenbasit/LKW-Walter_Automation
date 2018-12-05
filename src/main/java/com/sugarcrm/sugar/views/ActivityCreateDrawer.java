package com.sugarcrm.sugar.views;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.StandardModule;
import com.sugarcrm.sugar.records.Record;

/**
 * Models the CreateDrawer for standard SugarCRM modules.  
 * @author Mazen Louis <mlouis@sugarcrm.com>
 *
 */
public class ActivityCreateDrawer extends MultiSaveCreateDrawer {
	/**
	 * Initializes the CreateDrawer and specifies its parent module so that it knows which fields are available.
	 * @param parentModule - the module that owns this CreateDrawer, likely passed in using the module's this variable when constructing the CreateDrawer.
	 * @throws Exception
	 */
	public ActivityCreateDrawer(StandardModule parentModule) throws Exception {
		super(parentModule);

		// Calls and Meetings specific controls
		addControl("saveAndSendInvites", "a", "css", "a[name='save_invite_button']");
		addControl("addInviteeButton", "button", "css", ".fld_invitees.edit [data-action='addRow']");
		addControl("invitees", "span", "css", ".fld_invitees.edit");
		addSelect("searchInvitee", "a", "css", ".participants-form a");

		// Build out a few of the remove invitee "-" buttons
		// TODO: delete limit upto 5 records only
		for (int i = 2; i < 7; i++) {
			String removeInvitee = String.format("removeInvitee%02d", i);
			String currentRow = ".fld_invitees.edit > div > div:nth-child(" + ((i*2) + 1) + ")";
			addControl(removeInvitee, "button", "css", currentRow + " [data-action='removeRow']");
		}
	}

	/**
	 * Click the Save and Send Invite button.
	 * You must already be on the ActivityCreateDrawer to use this method.
	 * Takes you to the module listView when used.
	 * 
	 * @throws Exception
	 */
	public void saveAndSendInvites() throws Exception {
		openPrimaryButtonDropdown();
		getControl("saveAndSendInvites").click();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Click the Add Invitee button.
	 * <p>
	 * You must already be on ActivityCreateDrawer to use this method.
	 * When clicked the default behavior will be a search select2 dropdown appears 
	 * to search for, and select an invitee and it remains on ActivityCreateDrawer.
	 *
	 * @throws Exception
	 */
	public void clickAddInvitee() throws Exception {
		getControl("addInviteeButton").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#select2-drop input").waitForVisible();
	}

	/**
	 * Select the desired record to invite.
	 * <p>
	 * You must already be on ActivityCreateDrawer to use this method.
	 * To use, you must first have the invitee search available.
	 * When used, the desired Record will be added as an invitee to record
	 * and it remains on ActivityCreateDrawer.
	 *
	 * @param searchMe Record to select.
	 * @throws Exception
	 */
	public void selectInvitee(Record searchMe) throws Exception {
		// Logic to search and invite record
		VoodooSelect inviteeSearchSelect = (VoodooSelect)getControl("searchInvitee");
		inviteeSearchSelect.selectWidget.getControl("searchBox").set(searchMe.getRecordIdentifier());
		new VoodooControl(".select2-drop-active", "css", ".select2-drop-active ul[role='listbox'] li").waitForVisible();
		new VoodooControl("span", "XPATH", "/html//div[@id='select2-drop']//*[text()[contains(.,'" + searchMe.getRecordIdentifier() + "')]]").click();
	}

	/**
	 * Select the desired search name to invitee.
	 * <p>
	 * You must already be on ActivityCreateDrawer to use this method.
	 * To use, you must first have the invitee search available.
	 * When used, the desired search name will be added as an invitee to record
	 * and it remains on ActivityCreateDrawer.
	 * 
	 * @param searchMe String to select.
	 * @throws Exception
	 */
	public void selectInvitee(String searchMe) throws Exception {
		// Logic to search and invite as desired search string
		VoodooSelect inviteeSearchSelect = (VoodooSelect)getControl("searchInvitee");
		inviteeSearchSelect.selectWidget.getControl("searchBox").set(searchMe);
		VoodooUtils.waitForReady();
		new VoodooControl(".select2-drop-active", "css", ".select2-drop-active ul[role='listbox'] li").waitForVisible();
		new VoodooControl("span", "XPATH", "/html//div[@id='select2-drop']//*[text()[contains(.,'" + searchMe + "')]]").click();
	}

	/**
	 * Set repeat occurrence fields.
	 * <p>
	 * You must already be on the ActivityCreateDrawer to use this method.
	 * This method will leave you on the ActivityCreateDrawer with the specified fields filled.
	 * 
	 * @param repeatData FieldSet of repeat occurrence data.
	 * @throws Exception
	 */
	public void repeatOccurrences(FieldSet repeatData) throws Exception {
		if(repeatData.get("repeatType") == null) {
			throw new Exception("Data for Repeat Occurrences was present but the Repeat Type was not provided.");
		}
		for (String controlName : repeatData.keySet()) {
			setField(controlName, repeatData.get(controlName));
		}
	}

	/**
	 * Fill the specified fields on the ActivityCreateDrawer.
	 * You must already be on the ActivityCreateDrawer to use this method.
	 * This method will leave you on the ActivityCreateDrawer with the specified fields filled.
	 * 
	 * @param editedData	A FieldSet of fields to fill on the CreateDrawer.
	 * @throws Exception
	 */
	public void setFields(FieldSet editedData) throws Exception {
		FieldSet repeatData = new FieldSet();
		for (String controlName : editedData.keySet()) {
			if (controlName.startsWith("repeat")) {
				repeatData.put(controlName, editedData.get(controlName));
				continue;
			}
			setField(controlName, editedData.get(controlName));
		}
		if (!(repeatData.isEmpty())) {
			repeatOccurrences(repeatData);
		}
	}
}