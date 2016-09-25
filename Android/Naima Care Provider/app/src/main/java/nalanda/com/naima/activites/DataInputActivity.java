package nalanda.com.naima.activites;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import nalanda.com.naima.R;
import nalanda.com.naima.fragment.BaseDataFragment;
import nalanda.com.naima.widgets.BaseView;

public class DataInputActivity extends AppCompatActivity implements BaseDataFragment.OnFragmentInteractionListener{
    private BaseDataFragment dataFragment;
    private String mCurrentFlow;
    private BaseView mView;

    public static final String INPUT_EXTRA_FLOW = "Flow";
    public static final String FLOW_CREATE_CASE = "CreateCase";
    public static final String FLOW_PENDING_CASE = "PendingCase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_input);

        initFragment();
    }

    private void initFragment() {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.data_input_parent);
        layout.removeAllViews();

        dataFragment = new BaseDataFragment();
        dataFragment.setArguments(getIntent().getExtras());

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.data_input_parent, dataFragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
