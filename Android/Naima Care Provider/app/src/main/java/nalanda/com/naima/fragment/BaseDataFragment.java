package nalanda.com.naima.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import nalanda.com.naima.R;
import nalanda.com.naima.activites.DataInputActivity;
import nalanda.com.naima.flow.CasesFlowManager;
import nalanda.com.naima.flow.SymptomFlowManager;
import nalanda.com.naima.widgets.BaseView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BaseDataFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BaseDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BaseDataFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private String mCurrentFlow;

    private View mRootView;

    private BaseView mFlowView;
    private View mParentView;

    public BaseDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BaseDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BaseDataFragment newInstance(String param1, String param2) {
        BaseDataFragment fragment = new BaseDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_base_data, container, false);
        mParentView = mRootView.findViewById(R.id.fragmentRoot);
        startFlow();
        // Inflate the layout for this fragment
        return mRootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void updateView(List<View> views) {
        ((LinearLayout) mParentView).removeAllViews();
        for (View view: views) {
            ((LinearLayout) mParentView).addView(view);
        }
    }

    public void updateFooterView(View view) {
        if(((LinearLayout) mRootView).getChildCount() > 1) {
            ((LinearLayout) mRootView).removeViewAt(1);
        }
        ((LinearLayout) mRootView).addView(view);
    }

    private void startFlow() {
        mCurrentFlow = getArguments().getString(DataInputActivity.INPUT_EXTRA_FLOW);
        if(DataInputActivity.FLOW_CREATE_CASE.equals(mCurrentFlow)) {
            startCreateFlow();
        } else if(DataInputActivity.FLOW_PENDING_CASE.equals(mCurrentFlow)) {
            startPendingFlow();
        } else if(DataInputActivity.FLOW_OPEN_CASE.equals(mCurrentFlow)) {
            startOpenFlow();
        } else if(DataInputActivity.FLOW_CLOSED_CASE.equals(mCurrentFlow)) {
            startClosedFlow();
        }
    }

    private void startCreateFlow() {
        SymptomFlowManager symptomFlowManager = new SymptomFlowManager(this);
        symptomFlowManager.startFlow();
    }

    private void startPendingFlow() {
        CasesFlowManager casesFlowManager = new CasesFlowManager(this, CasesFlowManager.PENDING_FLOW);
        casesFlowManager.startFlow();
    }

    private void startOpenFlow() {
        CasesFlowManager casesFlowManager = new CasesFlowManager(this, CasesFlowManager.OPEN_FLOW);
        casesFlowManager.startFlow();
    }

    private void startClosedFlow() {
        CasesFlowManager casesFlowManager = new CasesFlowManager(this, CasesFlowManager.CLOSED_FLOW);
        casesFlowManager.startFlow();
    }
}
