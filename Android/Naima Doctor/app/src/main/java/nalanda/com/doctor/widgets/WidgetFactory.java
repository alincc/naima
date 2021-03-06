package nalanda.com.doctor.widgets;

import nalanda.com.doctor.viewmodel.BaseViewModel;

/**
 * Created by ps1 on 9/10/16.
 */
public class WidgetFactory {
    public static final String WIDGET_NUMERIC = "Numeric";
    public static final String WIDGET_BOOLEAN = "Boolean";
    public static final String WIDGET_MULTIPLE_CHOICE = "List";
    public WidgetFactory() {
    }

    public BaseView getViewWidget(String viewName, BaseViewModel viewModel) {
        BaseView viewWidget = null;

        if(WIDGET_NUMERIC.equals(viewName)) {
            viewWidget = new NumericView(viewModel);
        } else if(WIDGET_BOOLEAN.equals(viewName)) {
            viewWidget = new ChoiceView(viewModel);
        } else if(WIDGET_MULTIPLE_CHOICE.equals(viewName)) {
            viewWidget = new MultipleChoiceView(viewModel);
        }

        return viewWidget;
    }
}
