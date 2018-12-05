package com.sugarcrm.sugar.views;

public class StudioDetailView extends View {
	protected static StudioDetailView view;
	
	private StudioDetailView() throws Exception {}
	
	public static StudioDetailView getInstance() throws Exception {
		if (view == null)
			view = new StudioDetailView();
		return view;
	}
}