package com.example.ifetimerecorder;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FillFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FillFragment extends Fragment {


    ArrayList<String>   placesArr = new ArrayList<String>();
    ArrayList<String>   complaintArr = new ArrayList<String>();
    ArrayList<String>   surnameArr = new ArrayList<String>();
    ArrayList<String>   activityArr = new ArrayList<String>();
    ArrayList<String>   timeArr = new ArrayList<String>();
    ArrayList<String>   warrantyArr = new ArrayList<String>();
    ArrayList           emails = new ArrayList();

    EditText            dateTxt;
    Button              savaBtn;
    EditText            car;
    EditText            entr;
    EditText            description;
    EditText            comment;
    EditText            partNo;
    EditText            serialNo;
    EditText            driveNo;
    EditText            driveSNo;
    SwitchMaterial      inOperation;
    CheckBox            chBox;
    CheckBox            reportChBox;
    CheckBox            rmaChBox;
    TextView            saveStatus;

    AutoCompleteTextView place;
    ArrayAdapter<String> adapter_pl;
    AutoCompleteTextView complaint;
    ArrayAdapter<String> adapter_ct;
    AutoCompleteTextView surname;
    ArrayAdapter<String> adapter_su;
    AutoCompleteTextView activity;
    ArrayAdapter<String> adapter_ac;
    AutoCompleteTextView travelTime;
    AutoCompleteTextView arrTime;
    AutoCompleteTextView workTime;
    AutoCompleteTextView admTime;
    ArrayAdapter<String> adapter_time;
    AutoCompleteTextView war;
    ArrayAdapter<String> adapter_war;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FillFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FillFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FillFragment newInstance(String param1, String param2) {
        FillFragment fragment = new FillFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fill, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Create list of places
        place = view.findViewById(R.id.fill_place_etext);
        adapter_pl = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, placesArr);
        adapter_pl.add("Loading...");
        place.setAdapter(adapter_pl);
        place.setThreshold(0);

        //Getting list of places
        SendRequest requestPl = new SendRequest();
        JSONObject postDataParamsPl = new JSONObject();
        try {
            postDataParamsPl.put("getPlacesP", 1);
            postDataParamsPl.put("swVerP", getResources().getString(R.string.app_version));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestPl.execute(postDataParamsPl);

        //Create list of complaint types
        complaint = view.findViewById(R.id.fill_complaint_etext);
        adapter_ct = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, complaintArr);

        adapter_ct.add("Official");
        adapter_ct.add("Unofficial");
        adapter_ct.add("Customer unaware");
        adapter_ct.add("Non-complaint");
        complaint.setAdapter(adapter_ct);
        complaint.setThreshold(0);

        //Create list of surnames
        surname = view.findViewById(R.id.fill_surname_etext);
        adapter_su = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, surnameArr);
        //adapter_su.add("Loading...");
        adapter_su.add("Kirillov V.");
        adapter_su.add("Kravchenkov A.");
        adapter_su.add("Lavrinovich D.");
        adapter_su.add("Omelchenko S.");
        adapter_su.add("Pavlov I.");
        adapter_su.add("Ushakov A.");
        adapter_su.add("Zanosov M.");
        surname.setAdapter(adapter_su);
        surname.setThreshold(0);

        //Create list of emails
        emails.add("Viktor.Kirillov@knorr-bremse.com");
        emails.add("Anton.Kravchenkov@knorr-bremse.com");
        //emails.add("kravchenkovmiit@gmail.com");
        emails.add("Dmitrii.Lavrinovich@knorr-bremse.com");
        emails.add("Sergey.Omelchenko@knorr-bremse.com");
        emails.add("Ilia.Pavlov@knorr-bremse.com");
        emails.add("Alexey.Ushakov@knorr-bremse.com");
        emails.add("Mikhail.Zanosov@knorr-bremse.com");

        //Getting list of Surnames
        /*
        SendRequest requestSu = new SendRequest();
        JSONObject postDataParamsSu = new JSONObject();
        try {
            postDataParamsSu.put("getSurnameP", 1);
            postDataParamsSu.put("swVerP", getResources().getString(R.string.app_version));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestSu.execute(postDataParamsSu);
        */

        //Create list of activities
        activity = view.findViewById(R.id.fill_activity_etext);
        adapter_ac = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, activityArr);
        adapter_ac.add("Loading...");
        activity.setAdapter(adapter_ac);
        activity.setThreshold(0);

        //Getting list of Surnames
        SendRequest requestAc = new SendRequest();
        JSONObject postDataParamsAc = new JSONObject();
        try {
            postDataParamsAc.put("getActivityP", 1);
            postDataParamsAc.put("swVerP", getResources().getString(R.string.app_version));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestAc.execute(postDataParamsAc);

        //Create list of times
        travelTime = view.findViewById(R.id.fill_travel_time_etext);
        arrTime = view.findViewById(R.id.fill_arr_time_etext);
        workTime = view.findViewById(R.id.fill_working_time_etext);
        admTime = view.findViewById(R.id.fill_adm_time_etext);
        adapter_time = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, timeArr);

        int t = 3;
        adapter_time.add("0:00:00");
        adapter_time.add("0:15:00");
        adapter_time.add("0:30:00");
        adapter_time.add("0:45:00");
        adapter_time.add("1:00:00");
        adapter_time.add("1:15:00");
        adapter_time.add("1:30:00");
        adapter_time.add("1:45:00");
        adapter_time.add("2:00:00");
        adapter_time.add("2:15:00");
        adapter_time.add("2:30:00");
        adapter_time.add("2:45:00");
        while (t < 10) {
            adapter_time.add(t + ":00:00");
            adapter_time.add(t + ":30:00");
            t = t + 1;
        }

        travelTime.setAdapter(adapter_time);
        travelTime.setThreshold(0);
        arrTime.setAdapter(adapter_time);
        arrTime.setThreshold(0);
        workTime.setAdapter(adapter_time);
        workTime.setThreshold(0);
        admTime.setAdapter(adapter_time);
        admTime.setThreshold(0);

        //Create list of warranty types
        war = view.findViewById(R.id.fill_warranty_etext);
        adapter_war = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, warrantyArr);

        //Yes,No,Goodwill,In progress,N/A
        adapter_war.add("Yes");
        adapter_war.add("No");
        adapter_war.add("Goodwill");
        adapter_war.add("In progress");
        adapter_war.add("N/A");
        war.setAdapter(adapter_war);
        war.setThreshold(0);


        final Calendar myCalendar = Calendar.getInstance();
        dateTxt = (EditText) view.findViewById(R.id.fill_date_etext);
        savaBtn = view.findViewById(R.id.fill_save_btn);
        car = view.findViewById(R.id.fill_car_etext);
        entr = view.findViewById(R.id.fill_entrance_etext);
        description = view.findViewById(R.id.fill_description_etext);
        comment = view.findViewById(R.id.fill_comment_etext);
        inOperation = view.findViewById(R.id.fill_switch);
        Button scanBtn = view.findViewById(R.id.fill_scan_btn);
        partNo = view.findViewById(R.id.fill_part_no_etext);
        serialNo = view.findViewById(R.id.fill_serial_no_etext);
        Button scanDriveBtn = view.findViewById(R.id.fill_scan_drive_btn);
        driveNo = view.findViewById(R.id.fill_drive_no_etext);
        driveSNo = view.findViewById(R.id.fill_drive_serial_no_etext);
        chBox = view.findViewById(R.id.fill_chkbox);
        reportChBox = view.findViewById(R.id.report_chkbox);
        rmaChBox = view.findViewById(R.id.rma_chkbox);
        saveStatus = view.findViewById(R.id.fill_save_status);

        saveStatus.setText("Not saved yet ...");

        //Add part
        SwitchMaterial partSwitch = view.findViewById(R.id.fill_switch_add_part);
        LinearLayout partLayout = view.findViewById(R.id.fill_add_part_layout);

        if (!partSwitch.isChecked()) {
            partLayout.setVisibility(View.GONE);
        }

        partSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!partSwitch.isChecked()) {
                    partLayout.setVisibility(View.GONE);
                } else {
                    partLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        //Add drive
        SwitchMaterial driveSwitch = view.findViewById(R.id.fill_switch_add_drive);
        LinearLayout driveLayout = view.findViewById(R.id.fill_add_drive_layout);

        if (!driveSwitch.isChecked()) {
            driveLayout.setVisibility(View.GONE);
        }

        driveSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!driveSwitch.isChecked()) {
                    driveLayout.setVisibility(View.GONE);
                } else {
                    driveLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        //Add time
        SwitchMaterial timeSwitch = view.findViewById(R.id.fill_switch_add_time);
        LinearLayout timeLayout = view.findViewById(R.id.fill_add_time_layout);

        if (!timeSwitch.isChecked()) {
            timeLayout.setVisibility(View.GONE);
        }

        timeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timeSwitch.isChecked()) {
                    timeLayout.setVisibility(View.GONE);
                } else {
                    timeLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        //Date insert
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar);
            }
        };

        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateTxt.setText(sdf.format(new Date(System.currentTimeMillis())));

        dateTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        activity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter_ac.getItem(position).toString().contains("SW installation")) {
                    description.setText("Software uploaded - version: xx.xx");
                } else if (!adapter_ac.getItem(position).toString().contains("SW installation") &&
                        description.getText().toString().contains("Software uploaded - version:")) {
                    description.setText("");
                }
            }
        });

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(FillFragment.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan Part");
                integrator.setBeepEnabled(false);
                integrator.setCameraId(0);

                integrator.setBarcodeImageEnabled(false);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });

        scanDriveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(FillFragment.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan Drive");
                integrator.setBeepEnabled(false);
                integrator.setCameraId(0);
                integrator.setBarcodeImageEnabled(false);
                integrator.setOrientationLocked(false);
                integrator.setRequestCode(22);
                integrator.initiateScan();
            }
        });

        savaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dateTxt.getText().toString().length() < 1) {
                    Toast.makeText(getContext(), "Enter date",
                            Toast.LENGTH_LONG).show();
                } else if (surname.getText().toString().contains("Loading") || place.getText().toString().contains("Loading") ||
                        activity.getText().toString().contains("Loading")) {
                    Toast.makeText(getContext(), "Choose correct items from the list. Check internet connection.",
                            Toast.LENGTH_LONG).show();
                } else {
                    SendRequest request = new SendRequest();
                    JSONObject postDataParams = new JSONObject();

                    //Passing data as parameter

                    try {
                        postDataParams.put("addDataP", 1);
                        postDataParams.put("dateP", dateTxt.getText().toString());
                        postDataParams.put("surnameP", surname.getText().toString());
                        postDataParams.put("placeP", place.getText().toString());
                        postDataParams.put("complaintP", complaint.getText().toString());
                        postDataParams.put("inOperationP", inOperation.isChecked() ? 1 : 0);
                        postDataParams.put("carP", car.getText().toString());
                        postDataParams.put("entrP", entr.getText().toString());
                        postDataParams.put("activityP", activity.getText().toString());
                        postDataParams.put("descriptionP", description.getText().toString());
                        postDataParams.put("commentP", comment.getText().toString());
                        postDataParams.put("partnoP", partNo.getText().toString());
                        postDataParams.put("serialnoP", serialNo.getText().toString());
                        postDataParams.put("drivenoP", driveNo.getText().toString());
                        postDataParams.put("driveSnoP", driveSNo.getText().toString());
                        postDataParams.put("travelTimeP", travelTime.getText().toString());
                        postDataParams.put("arrTimeP", arrTime.getText().toString());
                        postDataParams.put("workTimeP", workTime.getText().toString());
                        postDataParams.put("admTimeP", admTime.getText().toString());
                        postDataParams.put("warP", war.getText().toString());
                        postDataParams.put("reportP", reportChBox.isChecked() ? 1 : 0);
                        postDataParams.put("rmaP", rmaChBox.isChecked() ? 1 : 0);
                        postDataParams.put("emailP", emails.get(adapter_su.getPosition(surname.getText().toString())));
                        postDataParams.put("swVerP", getResources().getString(R.string.app_version));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    request.execute(postDataParams);
                    savaBtn.setEnabled(false);
                }

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String  rawData;
        IntentResult result;

        if(requestCode == 22) {
            result = IntentIntegrator.parseActivityResult(resultCode,data);
        } else {
            result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        }

        if(result!=null && requestCode != 22) {
            rawData = result.getContents();
            partNo.setText(rawData);
        } else if(result!=null && requestCode == 22) {
            rawData = result.getContents();
            driveNo.setText(rawData);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateLabel(Calendar myCalendar) {

        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateTxt.setText(sdf.format(myCalendar.getTime()));
    }

    public class SendRequest extends AsyncTask<JSONObject, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(JSONObject... arg0) {

            try{

                String ssUrl = getResources().getString(R.string.app_ss_url);
                URL url = new URL(ssUrl);

                JSONObject postDataParams = arg0[0];

                Log.e("Send request:",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(35000 /* milliseconds */);
                conn.setConnectTimeout(35000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, StandardCharsets.UTF_8));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return "false : " + responseCode;
                }
            }
            catch(Exception e){
                return "Exception: " + e.getMessage();
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            if (result.contains("false") || result.contains("Exception")){
                Toast.makeText(getContext(), result,
                        Toast.LENGTH_LONG).show();

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                saveStatus.setText(result + " - " + df.format(c));

            }
            else {
                if (result.contains("Success")) {
                    Toast.makeText(getContext(), result,
                            Toast.LENGTH_LONG).show();

                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                    saveStatus.setText("Successful upload - " + df.format(c));

                    //TODO check of checkBox
                    if(!chBox.isChecked()) {
                        MainFragment mainFragment = new MainFragment();

                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, mainFragment, "Main_Fragment")
                                .commit();
                    }

                }
                else if(result.contains("Places#") && result.length() > 7) {
                    splitResult(result, adapter_pl);
                }
                else if(result.contains("Surname#") && result.length() > 8) {
                    splitResult(result, adapter_su);
                }
                else if(result.contains("Activity#") && result.length() > 9) {
                    splitResult(result, adapter_ac);
                } else {
                    Toast.makeText(getContext(), result,
                            Toast.LENGTH_LONG).show();
                }

            }
            savaBtn.setEnabled(true);
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    public void splitResult(String res, ArrayAdapter<String> adapter) {

        String  strTmp;
        int     tmp;

        tmp = res.indexOf('#');
        adapter.remove("Loading...");
        //add numbers
        while (res.indexOf('#', tmp + 1) != -1) {
            strTmp = res.substring(tmp + 1, res.indexOf('#', tmp + 1));
            adapter.add(strTmp);
            adapter.notifyDataSetChanged();
            tmp = res.indexOf('#', tmp + 1);
        }
    }
}