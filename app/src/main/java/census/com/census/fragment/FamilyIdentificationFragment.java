package census.com.census.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import java.util.ArrayList;
import census.com.census.DbUtils;
import census.com.census.R;

public class FamilyIdentificationFragment extends Fragment{

    private View view;

    public static EditText editTextFName;
    public static EditText editTextMName;
    public static EditText editTextLName;
    public static EditText editTextHouseNo;
    public static EditText editTextStreetNo;
    public static RadioGroup radioGroupResidency;
    public static RadioGroup radioGroupOwnership;
    public static RadioGroup radioGroupStatus;
    public static Spinner spinnerRegions;
    public static Spinner spinnerProvinces;
    public static Spinner spinnerMunicipal;
    public static Spinner spinnerBarangay;

    private DbUtils dbUtils;
    private ArrayAdapter spinnerArrayAdapter;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_family_identification, container, false);

        //init views
        onInitViews();

        //to load regions from sql
        onLoadRegions();

        //select region
        onSpinnerRegionEvent();

        //select province
        onSpinnerProvinceEvent();

        //select municipal
        onSpinnerMunicipalEvent();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        onLoadData();
    }

    private void onInitViews(){
        editTextFName = (EditText) view.findViewById(R.id.editTextFname);
        editTextMName = (EditText) view.findViewById(R.id.editTextMname);
        editTextLName = (EditText) view.findViewById(R.id.editTextLname);
        editTextHouseNo = (EditText) view.findViewById(R.id.editTextHouseNo);
        editTextStreetNo = (EditText) view.findViewById(R.id.editTextStreetNo);
        /*radioButtonResident = (RadioButton) view.findViewById(R.id.radioButtonResident);
        radioButtonNonResident = (RadioButton) view.findViewById(R.id.radioButtonNonResident);
        radioButtonOwner = (RadioButton) view.findViewById(R.id.radioButtonOwner);
        radioButtonExtended = (RadioButton) view.findViewById(R.id.radioButtonExtended);
        radioButtonActive = (RadioButton) view.findViewById(R.id.radioButtonActive);
        radioButtonInactive = (RadioButton) view.findViewById(R.id.radioButtonInactive);*/
        spinnerRegions = (Spinner) view.findViewById(R.id.spinnerRegions);
        spinnerProvinces = (Spinner) view.findViewById(R.id.spinnerProvince);
        spinnerMunicipal = (Spinner) view.findViewById(R.id.spinnerMunicipal);
        spinnerBarangay = (Spinner) view.findViewById(R.id.spinnerBarangay);

        radioGroupResidency = (RadioGroup) view.findViewById(R.id.radioGroupResidency);
        radioGroupOwnership = (RadioGroup) view.findViewById(R.id.radioGroupOwnership);
        radioGroupStatus = (RadioGroup) view.findViewById(R.id.radioGroupStatus);
    }

    private void onLoadRegions(){
        ArrayList listRegion;
        String query = "SELECT DISTINCT(region) FROM locations";
        dbUtils = new DbUtils(getActivity());
        listRegion = dbUtils.queryLocation(query,"region");

        if(!listRegion.isEmpty()) {
            useArrayAdapter(listRegion);
            spinnerRegions.setAdapter(spinnerArrayAdapter);
        }
        else{
            Log.i("regions:","empty");
        }
    }

    private void onSpinnerRegionEvent(){
        spinnerRegions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getProvinces(spinnerRegions.getSelectedItem().toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
    }

    private void getProvinces(String region){
        String query = "SELECT province FROM locations where region='"+region+"'";
        ArrayList listProvince;
        dbUtils = new DbUtils(getActivity());
        listProvince = dbUtils.queryLocation(query,"province");
        if(!listProvince.isEmpty()) {
            useArrayAdapter(listProvince);
            spinnerProvinces.setAdapter(spinnerArrayAdapter);
        }
        else{
            Log.i("provinces:","empty");
        }
    }

    private void onSpinnerProvinceEvent(){
        spinnerProvinces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getMunicipal(spinnerProvinces.getSelectedItem().toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
    }

    private void getMunicipal(String province){
        String query = "SELECT municipality FROM locations where province='"+province+"'";
        ArrayList listMunicipal;
        dbUtils = new DbUtils(getActivity());
        listMunicipal = dbUtils.queryLocation(query,"municipality");
        if(!listMunicipal.isEmpty()) {
            useArrayAdapter(listMunicipal);
            spinnerMunicipal.setAdapter(spinnerArrayAdapter);
        }
        else{
            Log.i("municipals:","empty");
        }
    }

    private void onSpinnerMunicipalEvent(){
        spinnerMunicipal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getBarangay(spinnerMunicipal.getSelectedItem().toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
    }

    private void getBarangay(String municipal){
        String query = "SELECT barangay FROM locations where municipality='"+municipal+"'";
        ArrayList listBarangay;
        dbUtils = new DbUtils(getActivity());
        listBarangay = dbUtils.queryLocation(query,"barangay");
        if(!listBarangay.isEmpty()) {
            useArrayAdapter(listBarangay);
            spinnerBarangay.setAdapter(spinnerArrayAdapter);
        }
        else{
            Log.i("barangays:","empty");
        }
    }

    private void useArrayAdapter(ArrayList arrayList){
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, arrayList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    }

    private void onLoadData(){
        sharedPreferences = getActivity().getSharedPreferences("census.com.census",Context.MODE_PRIVATE);

        editTextFName.setText(sharedPreferences.getString("fname",""));
        editTextMName.setText(sharedPreferences.getString("mname",""));
        editTextLName.setText(sharedPreferences.getString("lname",""));
        editTextHouseNo.setText(sharedPreferences.getString("houseno",""));
        editTextStreetNo.setText(sharedPreferences.getString("streetno",""));

        //spinnerBarangay.setSelection(sharedPreferences.getInt("barangay",0));
        //spinnerRegions.setSelection(sharedPreferences.getInt("region",0));

        editTextHouseNo.setText(sharedPreferences.getString("houseno",""));
        editTextStreetNo.setText(sharedPreferences.getString("streetno",""));

        radioGroupResidency.check(sharedPreferences.getInt("residency",2131624160));
        radioGroupOwnership.check(sharedPreferences.getInt("ownership",2131624163));
        radioGroupStatus.check(sharedPreferences.getInt("status",2131624166));
    }

    private void onSaveReference(){
        sharedPreferences.edit().putString("fname",editTextFName.getText().toString().trim()).apply();
        sharedPreferences.edit().putString("mname",editTextMName.getText().toString()).apply();
        sharedPreferences.edit().putString("lname",editTextLName.getText().toString()).apply();
        sharedPreferences.edit().putString("houseno",editTextHouseNo.getText().toString()).apply();
        sharedPreferences.edit().putString("streetno",editTextStreetNo.getText().toString()).apply();
        sharedPreferences.edit().putInt("region",spinnerRegions.getSelectedItemPosition()).apply();
        sharedPreferences.edit().putString("houseno",editTextHouseNo.getText().toString().trim()).apply();
        sharedPreferences.edit().putString("streetno",editTextStreetNo.getText().toString().trim()).apply();

        //cause error in naugat version
        //sharedPreferences.edit().putString("barangay",spinnerBarangay.getSelectedItem().toString()).apply();
        //sharedPreferences.edit().putString("province",spinnerProvinces.getSelectedItem().toString()).apply();
        //sharedPreferences.edit().putString("regionv",spinnerRegions.getSelectedItem().toString()).apply();

        sharedPreferences.edit().putInt("residency",radioGroupResidency.getCheckedRadioButtonId()).apply();
        sharedPreferences.edit().putInt("ownership",radioGroupOwnership.getCheckedRadioButtonId()).apply();
        sharedPreferences.edit().putInt("status",radioGroupStatus.getCheckedRadioButtonId()).apply();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSaveReference();
    }



}
