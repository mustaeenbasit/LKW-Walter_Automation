package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.modules.ActivityModule;

/**
 * Sub class from which the ActivityRecord objects for Calls and Meeting modules extends.
 * Data and methods that are common to ActivityRecords in all Activity Modules are
 * stored here.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public abstract class ActivityRecord extends StandardRecord {
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Pass-through constructor.
	 *
	 * @param	data	A FieldSet containing the data for the new record.
	 * @throws Exception
	 */
	public ActivityRecord(FieldSet data) throws Exception {
		super(data);
	}

	/**
	 * You must already be on the RecordView in edit mode to use this method.
	 * Click on '+' icon which is placed on the right-hand side for record in scheduling section (i.e guests)
	 * and select to invite new user or other person type record to this Activity Record.
	 * After Save it remains on the RecordView and switching it to Detail mode if successful, otherwise remains in Edit mode.
	 *
	 * @param inviteMe Person type record to invite (contact, user, lead, target)
	 * @throws Exception
	 */
	public void addInvitee(Record inviteMe) throws Exception {
		navToRecord();
		module.recordView.edit();
		((ActivityModule)module).recordView.clickAddInvitee();
		((ActivityModule)module).recordView.selectInvitee(inviteMe);
		module.recordView.save();
	}

	/**
	 * You must already be on the RecordView in edit mode to use this method.
	 * Click on '+' icon which is placed on the right-hand side for record in scheduling section (i.e guests)
	 * and invite new user with desired search to this Activity Record.
	 * After Save it remains on the RecordView and switching it to Detail mode if successful, otherwise remains in Edit mode.
	 *
	 * @param inviteMe Person type string to invite (contact, user, lead, target)
	 * @throws Exception
	 */
	public void addInvitee(String inviteMe) throws Exception {
		navToRecord();
		module.recordView.edit();
		((ActivityModule)module).recordView.clickAddInvitee();
		((ActivityModule)module).recordView.selectInvitee(inviteMe);
		module.recordView.save();
	}

	/**
	 * You must already be on the RecordView in edit mode to use this method.
	 * Click on '-' icon which is placed on the right-hand side for record in scheduling section (i.e guests)
	 * and remove record as an invitee of this Activity Record.
	 * After Save it remains on the RecordView and switching it to Detail mode if successful, otherwise remains in Edit mode.
	 *
	 * @param removeMe Record to remove
	 * @throws Exception
	 */
	public void removeInvitee(Record removeMe) throws Exception {
		navToRecord();
		module.recordView.edit();
		new VoodooControl("button", "css", ".fld_invitees.edit button[data-id='" + removeMe.getGuid() + "']").click();
		sugar().alerts.waitForLoadingExpiration();
		module.recordView.save();
	}

	/**
	 * You must already be on the RecordView in edit mode to use this method.
	 * Click on '-' icon which is placed on the right-hand side for record in scheduling section (i.e guests)
	 * and remove record as an invitee of this Activity Record.
	 * After Save it remains on the RecordView and switching it to Detail mode if successful, otherwise remains in Edit mode.
	 * <p>
	 * NOTE:<br>
	 *     This method will remove an invited person record by index. You can not remove the user who created this call/meeting
	 *     therefore one of the indices is invalid.
	 *     
	 * @param removeMe int index of the record to remove
	 * @throws Exception
	 */
	public void removeInvitee(int removeMe) throws Exception {
		navToRecord();
		module.recordView.edit();
		((ActivityModule)module).recordView.getControl(String.format("removeInvitee%02d", removeMe)).click();
		sugar().alerts.waitForLoadingExpiration();
		module.recordView.save();
	}

	/**
	 * Click the "delete all reocurrences" button.
	 * <p>
	 * You must already be on the ActivityRecordView, in the detail mode, to use this method.<br>
	 * When used, the alert warning will appear to ask to be confirmed or canceled.
	 *
	 * @throws Exception
	 */
	public void deleteAllReocurrences() throws Exception {
		((ActivityModule)module).recordView.openPrimaryButtonDropdown();
		((ActivityModule)module).recordView.getControl("deleteAllReocurrences").click();
	}

	/**
	 * Click the "edit all reocurrences" button.
	 * <p>
	 * You must already be on the ActivityRecordView, in the detail mode, to use this method.<br>
	 * When used, you will be left in the ActivityRecordView in edit mode.
	 *
	 * @throws Exception
	 */
	public void editAllReocurrenses() throws Exception {
		((ActivityModule)module).recordView.openPrimaryButtonDropdown();
		((ActivityModule)module).recordView.getControl("editAllReocurrences").click();
	}
} // ActivityRecord