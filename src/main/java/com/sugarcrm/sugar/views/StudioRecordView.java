package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.VoodooUtils;

public class StudioRecordView extends View {
	protected static StudioRecordView view;
	
	private StudioRecordView() throws Exception {}
	
	public static StudioRecordView getInstance() throws Exception {
		if (view == null)
			view = new StudioRecordView();
		return view;
	}
}
