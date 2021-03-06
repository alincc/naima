package nalanda.com.naima.flow;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import nalanda.com.naima.R;
import nalanda.com.naima.fragment.BaseDataFragment;
import nalanda.com.naima.models.BaseModel;
import nalanda.com.naima.models.CaseClinical;
import nalanda.com.naima.models.CaseData;
import nalanda.com.naima.models.CaseDataModel;
import nalanda.com.naima.models.DoctorDetails;
import nalanda.com.naima.models.Patient;
import nalanda.com.naima.models.Provider;
import nalanda.com.naima.network.VolleyUtil;
import nalanda.com.naima.viewmodel.FooterButtonModel;
import nalanda.com.naima.viewmodel.LandingTableViewModel;
import nalanda.com.naima.viewmodel.SymptomItemModel;
import nalanda.com.naima.widgets.BaseView;
import nalanda.com.naima.widgets.ComboboxView;
import nalanda.com.naima.widgets.DoctorDetailsView;
import nalanda.com.naima.widgets.LandingTableView;
import nalanda.com.naima.widgets.FooterButton;
import nalanda.com.naima.widgets.NextFooterButton;
import nalanda.com.naima.widgets.TwoFooterButton;
import nalanda.com.naima.widgets.WidgetFactory;

/**
 * Created by ps1 on 9/10/16.
 */
public class SymptomFlowManager {
    // Diagnostic service endpoint.
    // Updated by Tarun on 1st Oct with latest EC2 instance URL
    public static final String symptomStandardUrl = "http://ec2-52-91-112-26.compute-1.amazonaws.com:8080/vitalstats";
    public static final String symptomClinicalUrl = "http://ec2-52-91-112-26.compute-1.amazonaws.com:8080/symptoms";
    public static final String symptomDiagnosticUrl = "http://ec2-52-91-112-26.compute-1.amazonaws.com:8080/symptoms/tests";
    public static final String associatedSymptomsUrl = "http://ec2-52-91-112-26.compute-1.amazonaws.com:8080/associatedsymptoms";

    // Cases Service endpoing
    public static final String caseNewUrl = "http://ec2-52-91-112-26.compute-1.amazonaws.com:8082/api/v1/cases/new";

    public static final String JSON_CONTENT_TYPE = "application/json";

    private BaseDataFragment mDataFragment;
    Gson gson;
    private WidgetFactory widgetFactory;
    public static final int SYMPTOM_STATE_LANDING_STANDARD = 1;
    public static final int SYMPTOM_STATE_STANDARD = 2;
    public static final int SYMPTOM_STATE_LANDING_COMBO = 3;
    public static final int SYMPTOM_STATE_COMBO = 4;
    public static final int SYMPTOM_STATE_SINGLE_CLINICAL = 5;
    public static final int SYMPTOM_STATE_LANDING_CLINICAL = 6;
    public static final int SYMPTOM_STATE_CLINICAL = 7;
    public static final int SYMPTOM_STATE_DIAGNOSTIC = 8;
    public static final int SYMPTOM_STATE_LANDING_SUBMIT = 9;
    public static final int SYMPTOM_STATE_CASE_SUBMIT = 10;
    private int mCurrentState;

    private SymptomItemModel[] symptomModel;

    private CaseDataModel caseDataModel;

    private List<BaseView> mViews;

    public SymptomFlowManager(BaseDataFragment dataFragment) {
        mDataFragment = dataFragment;

        gson = new Gson();

        widgetFactory = new WidgetFactory();
    }

    public void startFlow() {
        caseDataModel = new CaseDataModel();
        caseDataModel.setData(new CaseData());
        caseDataModel.getData().setClinical(new ArrayList<CaseClinical>());
        populatePatientData();
        populateProviderData();
//        goToSymptomStandardState();
        goToLandingState(SYMPTOM_STATE_LANDING_STANDARD);
    }

    private void continueStartFlow() {
        caseDataModel = new CaseDataModel();
        caseDataModel.setData(new CaseData());
        caseDataModel.getData().setClinical(new ArrayList<CaseClinical>());
        populatePatientData();
        populateProviderData();
        goToSymptomStandardState();
    }

    private void populatePatientData() {
        Patient patient = new Patient();
        patient.setId("PT-01");
        patient.setName("Patient-01");
        caseDataModel.setPatient(patient);
    }

    private void populateProviderData() {
        Provider provider = new Provider();
        provider.setId("PR-01");
        provider.setName("Provider-01");
        caseDataModel.setProvider(provider);
    }

    private void populateDataForStandardState() {
    }

    private String getTitle(int state) {
        String title = null;
        switch (state) {
            case SYMPTOM_STATE_LANDING_STANDARD:
                title = mDataFragment.getActivity().getString(R.string.landing_standard_title);
                break;
            case SYMPTOM_STATE_LANDING_COMBO:
                title = mDataFragment.getActivity().getString(R.string.landing_combo_title);
                break;
            case SYMPTOM_STATE_LANDING_CLINICAL:
                title = mDataFragment.getActivity().getString(R.string.landing_clinical_title);
                break;
            case SYMPTOM_STATE_LANDING_SUBMIT:
                title = mDataFragment.getActivity().getString(R.string.landing_submit_title);
                break;
            default:
                break;
        }

        return title;
    }

    private String getContent(int state) {
        String content = null;
        switch (state) {
            case SYMPTOM_STATE_LANDING_STANDARD:
                content = mDataFragment.getActivity().getString(R.string.landing_standard_content);
                break;
            case SYMPTOM_STATE_LANDING_COMBO:
                content = mDataFragment.getActivity().getString(R.string.landing_combo_content);
                break;
            case SYMPTOM_STATE_LANDING_CLINICAL:
                content = mDataFragment.getActivity().getString(R.string.landing_clinical_content);
                break;
            case SYMPTOM_STATE_LANDING_SUBMIT:
                content = mDataFragment.getActivity().getString(R.string.landing_submit_content);
                break;
            default:
                break;
        }

        return content;
    }

    private void goToNextState(int state) {
        switch (state) {
            case SYMPTOM_STATE_LANDING_STANDARD:
                goToSymptomStandardState();
                break;
            case SYMPTOM_STATE_LANDING_COMBO:
                goToSymptomComboState(false);
                break;
            case SYMPTOM_STATE_LANDING_CLINICAL:
                goToSubmitState();
                break;
            case SYMPTOM_STATE_LANDING_SUBMIT:
                goToCaseSubmitState();
                break;
            default:
                break;
        }
    }

    private int getButtonLabel(int state) {
        int buttonLabel = 0;
        switch (state) {
            case SYMPTOM_STATE_LANDING_STANDARD:
                buttonLabel = R.string.landing_standard_buttonlabel;
                break;
            case SYMPTOM_STATE_LANDING_COMBO:
                buttonLabel = R.string.landing_combo_buttonlabel;
                break;
            case SYMPTOM_STATE_LANDING_CLINICAL:
                buttonLabel = R.string.landing_clinical_buttonlabel;
                break;
            case SYMPTOM_STATE_LANDING_SUBMIT:
                buttonLabel = R.string.landing_submit_buttonlabel;
                break;
            default:
                break;
        }

        return buttonLabel;
    }

    public void goToLandingState(final int state) {
        mCurrentState = state;

        LandingTableViewModel landingTableViewModel = new LandingTableViewModel();
        landingTableViewModel.setTitle(getTitle(state));
        landingTableViewModel.setContent(getContent(state));

        LandingTableView landingTableView =
                (LandingTableView) widgetFactory.getViewWidget(WidgetFactory.WIDGET_LANDING, landingTableViewModel);

        List<View> views = new ArrayList<View>();
        View view = landingTableView.getView(mDataFragment.getActivity());

        views.add(view);

        FooterButtonModel footerButtonModel = new FooterButtonModel(getButtonLabel(state));

        Button footerButton = (Button) (new FooterButton(footerButtonModel)).getView(mDataFragment.getActivity());
        footerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               goToNextState(state);
            }
        });

        mDataFragment.updateView(views);

        mDataFragment.updateFooterView(footerButton);
    }

    public void goToSymptomStandardState() {
        mCurrentState = SYMPTOM_STATE_STANDARD;
        VolleyUtil.getInstance().doGet(mDataFragment.getContext(), symptomStandardUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                symptomModel = gson.fromJson(response, SymptomItemModel[].class);
                List<View> views = new ArrayList<View>();
                for (SymptomItemModel itemModel :symptomModel) {
                    BaseView view = widgetFactory.getViewWidget(itemModel.getFormat(), itemModel);
                    views.add(view.getView(mDataFragment.getActivity()));
                }

                Button footerButton = (Button) (new NextFooterButton()).getView(mDataFragment.getActivity());
                footerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        populateDataForStandardState();
                        VolleyUtil.getInstance().doGet(mDataFragment.getContext(), symptomClinicalUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                symptomModel = gson.fromJson(response, SymptomItemModel[].class);
                                goToLandingState(SYMPTOM_STATE_LANDING_COMBO);
                            }
                        });
                    }
                });

                mDataFragment.updateView(views);

                mDataFragment.updateFooterView(footerButton);
            }
        });
    }

    private void populateDataForClinicalState() {
        for(BaseView view: mViews) {
            BaseModel model = view.getClinicalData();
            if(model != null) {
                caseDataModel.getData().getClinical().add((CaseClinical) model);
            }
        }
    }

    public void goToSymptomComboState(boolean reEnter) {
        String body = gson.toJson(caseDataModel, CaseDataModel.class);
        Log.i("goToSymptomComboState", "JSON: " + body);
        mCurrentState = SYMPTOM_STATE_COMBO;
        final ComboboxView comboboxView = new ComboboxView(symptomModel);

        View view = comboboxView.getView(mDataFragment.getActivity());

        if(reEnter) {
            ((TextView) view.findViewById(R.id.patient_label)).setText("Additional\nSymptoms: ");
        } else {
            ((TextView) view.findViewById(R.id.patient_label)).setText("Symptoms: ");
        }

        List<View> views = new ArrayList<View>();
        views.add(view);

        View footerView = (new TwoFooterButton()).getView(mDataFragment.getActivity());
        footerView.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = comboboxView.getSelectedIndex();
                if(index >= 0) {
                    goToSingleSymptomState(comboboxView.getSelectedIndex());
                } else {
                    Toast.makeText(mDataFragment.getActivity(), "Please select a sysmptom", Toast.LENGTH_LONG).show();
                }
            }
        });
        footerView.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLandingState(SYMPTOM_STATE_LANDING_CLINICAL);
            }
        });

        mDataFragment.updateView(views);

        mDataFragment.updateFooterView(footerView);
    }

    private void populateDataForSingleClinicalState(BaseView view) {
        BaseModel model = view.getClinicalData();
        if(model != null) {
            caseDataModel.getData().getClinical().add((CaseClinical) model);
        }
    }

    public void goToSingleSymptomState(int index) {
        mCurrentState = SYMPTOM_STATE_SINGLE_CLINICAL;

        List<View> views = new ArrayList<View>();
        SymptomItemModel itemModel = symptomModel[index];
        final BaseView view = widgetFactory.getViewWidget(itemModel.getFormat(), itemModel);
        if (view != null) {
            views.add(view.getView(mDataFragment.getActivity()));
        }

        Button footerButton = (Button) (new NextFooterButton()).getView(mDataFragment.getActivity());
        footerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateDataForSingleClinicalState(view);
                goToSymptomComboState(true);
            }
        });

        mDataFragment.updateView(views);

        mDataFragment.updateFooterView(footerButton);
    }

    public void goToSymptomClinicalState(String response) {
        mCurrentState = SYMPTOM_STATE_CLINICAL;
        symptomModel = gson.fromJson(response, SymptomItemModel[].class);
        if(symptomModel != null && symptomModel.length > 0) {
            List<View> views = new ArrayList<View>();
            mViews = new ArrayList<BaseView>();
            for (SymptomItemModel itemModel : symptomModel) {
                BaseView view = widgetFactory.getViewWidget(itemModel.getFormat(), itemModel);
                if (view != null) {
                    views.add(view.getView(mDataFragment.getActivity()));
                    mViews.add(view);
                }
            }

            Button footerButton = (Button) (new NextFooterButton()).getView(mDataFragment.getActivity());
            footerButton.setText(R.string.next);
            footerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    populateDataForClinicalState();
                    if (mViews != null && mViews.size() > 0) {
                        goToSubmitState();
                    } else {
                        // should never come here
                        goToLandingState(SYMPTOM_STATE_LANDING_SUBMIT);
                    }
                }
            });

            mDataFragment.updateView(views);

            mDataFragment.updateFooterView(footerButton);
        } else {
            goToLandingState(SYMPTOM_STATE_LANDING_SUBMIT);
        }
    }

    public void goToSymptomDiagnosticState() {
        mCurrentState = SYMPTOM_STATE_DIAGNOSTIC;
        VolleyUtil.getInstance().doGet(mDataFragment.getContext(), symptomDiagnosticUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//              response = "[{\"_id\":\"57d43de9aad0ed452f5d530f\",\"id\":\"CS-01\",\"format\":\"clinical\",\"info\":{\"title\":\"Where is the abodomen pain?\",\"name\":\"Abdomen Pain\",\"format\":\"List\",\"options\":[]}},{\"_id\":\"57d43de9aad0ed452f5d5310\",\"id\":\"CS-02\",\"format\":\"clinical\",\"info\":{\"title\":\"Did you have nausea?\",\"name\":\"Nausea\",\"format\":\"Boolean\",\"options\":[]}},{\"_id\":\"57d43de9aad0ed452f5d5311\",\"id\":\"CS-03\",\"format\":\"clinical\",\"info\":{\"title\":\"Did you vomit?\",\"name\":\"Vommiting\",\"format\":\"Boolean\",\"options\":[]}},{\"_id\":\"57d43de9aad0ed452f5d5312\",\"id\":\"CS-04\",\"format\":\"clinical\",\"info\":{\"title\":\"Nature of stool?\",\"name\":\"Stool\",\"format\":\"List\",\"options\":[\"Hard\",\"Soft\",\"Liquid\"]}}]";
                symptomModel = gson.fromJson(response, SymptomItemModel[].class);
                List<View> views = new ArrayList<View>();
                for (SymptomItemModel itemModel : symptomModel) {
                    BaseView view = widgetFactory.getViewWidget(itemModel.getFormat(), itemModel);
                    if (view != null)
                        views.add(view.getView(mDataFragment.getActivity()));
                }

                Button footerButton = (Button) (new NextFooterButton()).getView(mDataFragment.getActivity());
                footerButton.setText(R.string.submit);
                footerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToSubmitState();
                    }
                });

                mDataFragment.updateView(views);

                mDataFragment.updateFooterView(footerButton);
            }
        });
    }

    public void goToSubmitState() {
        mCurrentState = SYMPTOM_STATE_DIAGNOSTIC;

//        String body = "{ \"data\":  { \"clinical\": [ {  \"id\": \"ID-01\", \"name\": \"Abdomain  Pain\",  \"value\": [ \"UpperLeft\",  \"UpperRigth\" ] } ]  }, \"provider\": { \"id\":  \"PR-02\", \"name\": \"Test02\" },  \"patient\": { \"id\": \"PT-01\", \"name\": \"Ramu\" } }";
        String body = gson.toJson(caseDataModel.getData().getClinical());
        Log.i("goToSubmitState", "JSON: " + body);
        VolleyUtil.getInstance().doPost(mDataFragment.getContext(), associatedSymptomsUrl, body, JSON_CONTENT_TYPE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("goToSubmitState", "JSON response: " + response);
//                Toast.makeText(mDataFragment.getActivity(), response, Toast.LENGTH_SHORT).show();
                goToSymptomClinicalState(response);
            }
        });
    }

    public void goToCaseSubmitState() {
        mCurrentState = SYMPTOM_STATE_CASE_SUBMIT;

//        String body = "{ \"data\":  { \"clinical\": [ {  \"id\": \"ID-01\", \"name\": \"Abdomain  Pain\",  \"value\": [ \"UpperLeft\",  \"UpperRigth\" ] } ]  }, \"provider\": { \"id\":  \"PR-02\", \"name\": \"Test02\" },  \"patient\": { \"id\": \"PT-01\", \"name\": \"Ramu\" } }";
        String body = gson.toJson(caseDataModel, CaseDataModel.class);
        Log.i("goToSubmitState", "JSON: " + body);
        VolleyUtil.getInstance().doPost(mDataFragment.getContext(), caseNewUrl, body, JSON_CONTENT_TYPE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("goToSubmitState", "JSON response: " + response);
//                Toast.makeText(mDataFragment.getActivity(), response, Toast.LENGTH_SHORT).show();
                goToShowDoctorDetails(response);
            }
        });
    }

    public void goToShowDoctorDetails(String response) {
        DoctorDetails doctorDetails = gson.fromJson(response, DoctorDetails.class);

        List<View> views = new ArrayList<View>();

        DoctorDetailsView doctorDetailsView = new DoctorDetailsView(doctorDetails);

        views.add(doctorDetailsView.getView(mDataFragment.getActivity()));

        mDataFragment.updateView(views);

        Button footerButton = (Button) (new NextFooterButton()).getView(mDataFragment.getActivity());
        footerButton.setText(R.string.done);
        footerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateDataForStandardState();
                VolleyUtil.getInstance().doGet(mDataFragment.getContext(), symptomClinicalUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mDataFragment.getActivity().onBackPressed();
                    }
                });
            }
        });

        mDataFragment.updateFooterView(footerButton);
    }
}
