package com.sugarcrm.sugar.views;

public class StudioEditView extends View {
	protected static StudioEditView view;
	
	private StudioEditView() throws Exception {}
	
	public static StudioEditView getInstance() throws Exception {
		if (view == null)
			view = new StudioEditView();
		return view;
	}
}