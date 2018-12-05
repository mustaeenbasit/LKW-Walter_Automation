package com.sugarcrm.sugar.views;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.ActivityModule;
import com.sugarcrm.sugar.modules.RecordsModule;
import com.sugarcrm.sugar.records.Record;

/**
 * Models the ActivityRecordView for Activity type Modules in SugarCRM.
 * @author Mazen Louis <mlouis@sugarcrm.com>
 *
 */
public class ActivityRecordView extends RecordView {
	/**
	 * Initializes the ActivityRecordView and specifies its parent module so that it knows which fields are available.
	 * @param parentModule - the module that owns this ActivityRecordView, likely passed in using the module's this
	 *                        variable when constructing the RecordView.
	 * @throws Exception
	 */
	public ActivityRecordView(RecordsModule parentModule) throws Exception {
		super(parentModule);

		// Calls and Meetings specific controls
		addControl("share", "a", "css", ".fld_share.detail a");
		addControl("close", "a", "css", ".fld_record-close.detail a");
		addControl("closeAndCreateNew", "a", "css", ".fld_record-close-new.detail a");

		// editCaretIcon: Special case for save and send invitees on record view
		addControl("editCaretIcon", "a", "css", ".fld_save_dropdown.detail .btn.dropdown-toggle.btn-primary");
		addControl("saveAndSendInvite", "span", "css", ".fld_save_invite_button.detail");

		// Reocurrence
		addControl("deleteAllReocurrences", "a", "css", ".fld_delete_recurrence_button.detail a");
		addControl("editAllReocurrences", "a", "css", ".fld_edit_recurrence_button.detail a");

		// Scheduling Controls
		addControl("addInviteeButton", "button", "css", ".fld_invitees.edit [data-action='addRow']");
		addControl("invitees", "span", "css", ".panel_body .fld_invitees.detail");

		// Build out a few of the remove invitee "-" buttons
		for (int i = 1; i < 5; i++) {
			String removeInvitee = String.format("removeInvitee%02d", i);

			String currentRow = ".fld_invitees.edit > div > div:nth-child(" + ((i*2) + 1) + ")";

			addControl(removeInvitee, "button", "css", currentRow + " [data-action='removeRow']");
		}

		// Special Select2 search field for invitees
		addSelect("searchInvitee", "a", "css", ".participants-form a");
	}

	/**
	 * Click the Save and Send Invite button.
	 * <p>
	 * You must already be on the ActivityRecordView edit Mode to use this method.<br>
	 * Takes you to the module listView when used.<br>
	 *
	 * @throws Exception
	 */
	public void saveAndSendInvites() throws Exception {
		getControl("editCaretIcon").click();
		getControl("saveAndSendInvite").click();
		sugar().alerts.getSuccess().closeAlert();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Click Close and Create New.
	 * <p>
	 * You must already be on the ActivityRecordView, in the deatil mode, to use this method.<br>
	 * When used, you will be left on the ActivityCreateDrawer in Edit mode.
	 *
	 * @throws Exception
	 */
	public void closeAndCreateNew() throws Exception {
		openPrimaryButtonDropdown();
		getControl("closeAndCreateNew").click();
		sugar().alerts.getSuccess().closeAlert();
		((ActivityModule)parentModule).createDrawer.getControl("cancelButton").waitForVisible();
	}

	/**
	 * Click the close link.
	 * <p>
	 * You must already be on the ActivityRecordView, in the detail mode, to use this method.<br>
	 * When used, you will remain on the same ActivityRecordView with the record status set to "Held"
	 *
	 * @throws Exception
	 */
	public void close() throws Exception {
		openPrimaryButtonDropdown();
		getControl("close").click();
		// TODO: Possibly update timing to wait for status change in the Record to occur.
		sugar().alerts.getSuccess().closeAlert();
	}

	/**
	 * Click the share link.
	 * <p>
	 * You must already be on the ActivityRecordView, in the detail mode, to use this method.<br>
	 * When used, you will be taken to the Compose Email drawer.
	 *
	 * @throws Exception
	 */
	public void share() throws Exception {
		openPrimaryButtonDropdown();
		getControl("share").click();
		sugar().alerts.waitForLoadingExpiration();
		// TODO: Once VOOD-1076 is merged, use its view in the below waitForVisible()
		new VoodooControl("a", "css", ".layout_Emails .fld_cancel_button a").waitForVisible();
	}

	/**
	 * Click the Add Invitee button.
	 * <p>
	 * You must already be on ActivityRecordView to use this method.
	 * When clicked the default behavior will be a search select2 dropdown appears 
	 * to search for, and select an invitee and it remains on ActivityRecordView.
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
	 * You must already be on ActivityRecordView to use this method.
	 * To use, you must first have the invitee search available.
	 * When used, the desired search name will be added as an invitee to record
	 * and it remains on ActivityRecordView.
	 * 
	 * @param searchMe Record to select.
	 * @throws Exception
	 */
	public void selectInvitee(Record searchMe) throws Exception {
		// Logic to search and invite record
		new VoodooSelect("a", "css", ".participants-form a").selectWidget.getControl("searchBox").set(searchMe.getRecordIdentifier());
		new VoodooControl(".select2-drop-active", "css", ".select2-drop-active ul[role='listbox'] li").waitForVisible();
		new VoodooControl("span", "XPATH", "/html//div[@id='select2-drop']//*[text()[contains(.,'" + searchMe.getRecordIdentifier() + "')]]").click();
	}

	/**
	 * Select the desired search name to invitee.
	 * <p>
	 * You must already be on ActivityRecordView to use this method.
	 * To use, you must first have the invitee search available.
	 * When used, the desired search name will be added as an invitee to record
	 * and it remains on ActivityRecordView.
	 *
	 * @param searchMe String to select.
	 * @throws Exception
	 */
	public void selectInvitee(String searchMe) throws Exception {
		// Logic to search and invite as desired search string
		new VoodooSelect("a", "css", ".participants-form a").selectWidget.getControl("searchBox").set(searchMe);
		new VoodooControl(".select2-drop-active", "css", ".select2-drop-active ul[role='listbox'] li").waitForVisible();
		new VoodooControl("span", "XPATH", "/html//div[@id='select2-drop']//*[text()[contains(.,'" + searchMe + "')]]").click();
	}

	/**
	 * Assume you are in RecordView edit mode. It will loop through the fields to enter FieldSet data.
	 * @param editedData
	 * @throws Exception
	 */
	@Override
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

	/**
	 * Set repeat occurrence fields.
	 * <p>
	 * You must already be on ActivityRecordView to use this method.
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
	 * Verifies that the given Invitee cell, identified by row number, contains the given Invitee with the given status.
	 * 
	 * @param rowNumber the 1-based number of the row you want to verify 
	 * @param verifyThis the fieldSet containing Invitee name and status you want to verify.
	 * @throws Exception
	 */
	public void verifyInvitee(int rowNumber, FieldSet verifyThis) throws Exception {
		for(String key : verifyThis.keySet()) {
			new VoodooControl("span", "xpath" , "//*[contains(@class,'participants-schedule')]/div["+ (1 + (rowNumber * 2)) +"]//*[contains(@class,'cell profile')]").assertContains(verifyThis.get(key), true);
		}
	}
}