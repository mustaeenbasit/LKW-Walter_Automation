package com.sugarcrm.sugar.views;

public class StudioListView extends View {
	protected static StudioListView view;
	
	private StudioListView() throws Exception {}
	
	public static StudioListView getInstance() throws Exception {
		if (view == null)
			view = new StudioListView();
		return view;
	}
}