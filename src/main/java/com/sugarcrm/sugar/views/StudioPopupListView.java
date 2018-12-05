package com.sugarcrm.sugar.views;

public class StudioPopupListView extends View {
	protected static StudioPopupListView view;
	
	private StudioPopupListView() throws Exception {}
	
	public static StudioPopupListView getInstance() throws Exception {
		if (view == null)
			view = new StudioPopupListView();
		return view;
	}
}