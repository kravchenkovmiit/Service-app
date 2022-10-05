package com.example.ifetimerecorder;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;


public class AddCarFragment extends Fragment {

    ArrayList   prjNosArr = new ArrayList();
    ArrayList   manufacturerArr = new ArrayList();
    ArrayList   depotArr = new ArrayList();

    AutoCompleteTextView    prjNos;
    ArrayAdapter            adapter_prj;
    AutoCompleteTextView    manufacturer;
    ArrayAdapter            adapter_man;
    AutoCompleteTextView    depot;
    ArrayAdapter            adapter_dep;

    Button      addBtn;
    EditText    car1;
    EditText    car2;
    EditText    car3;
    EditText    car4;
    EditText    car5;
    EditText    car6;
    EditText    car7;
    EditText    car8;
    EditText    dateTxt;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddCarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCarFragment newInstance(String param1, String param2) {
        AddCarFragment fragment = new AddCarFragment();
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
        return inflater.inflate(R.layout.fragment_add_car, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText comm1;
        EditText comm2;

        //Create list of projects
        prjNos = view.findViewById(R.id.train_prj_no_etext);
        adapter_prj = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, prjNosArr);
        adapter_prj.add("Loading...");
        prjNos.setAdapter(adapter_prj);
        prjNos.setThreshold(0);

        //Getting list of places
        SendRequest requestPrj = new SendRequest();
        JSONObject postDataParamsPrj = new JSONObject();
        try {
            postDataParamsPrj.put("getPrjP", 1);
            postDataParamsPrj.put("swVerP", getResources().getString(R.string.app_version));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestPrj.execute(postDataParamsPrj);

        //Create list of manufacturers
        manufacturer = view.findViewById(R.id.train_man_etext);
        adapter_man = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, manufacturerArr);
        adapter_man.add("Loading...");
        manufacturer.setAdapter(adapter_man);
        manufacturer.setThreshold(0);

        //Getting list of manufacturers
        SendRequest requestMan = new SendRequest();
        JSONObject postDataParamsMan = new JSONObject();
        try {
            postDataParamsMan.put("getManP", 1);
            postDataParamsMan.put("swVerP", getResources().getString(R.string.app_version));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestMan.execute(postDataParamsMan);

        //Create list of depots
        depot = view.findViewById(R.id.train_depot_etext);
        adapter_dep = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, depotArr);
        adapter_dep.add("Loading...");
        depot.setAdapter(adapter_dep);
        depot.setThreshold(0);

        //Getting list of depots
        SendRequest requestDep = new SendRequest();
        JSONObject postDataParamsDep = new JSONObject();
        try {
            postDataParamsDep.put("getDepotP", 1);
            postDataParamsDep.put("swVerP", getResources().getString(R.string.app_version));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestDep.execute(postDataParamsDep);

        final Calendar myCalendar = Calendar.getInstance();
        addBtn = view.findViewById(R.id.train_add_btn);
        car1 = view.findViewById(R.id.train_car_1_etext);
        car2 = view.findViewById(R.id.train_car_2_etext);
        car3 = view.findViewById(R.id.train_car_3_etext);
        car4 = view.findViewById(R.id.train_car_4_etext);
        car5 = view.findViewById(R.id.train_car_5_etext);
        car6 = view.findViewById(R.id.train_car_6_etext);
        car7 = view.findViewById(R.id.train_car_7_etext);
        car8 = view.findViewById(R.id.train_car_8_etext);
        dateTxt = view.findViewById(R.id.train_date_etext);
        comm1 = view.findViewById(R.id.train_comment_1_etext);
        comm2 = view.findViewById(R.id.train_comment_2_etext);

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

        dateTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (prjNos.getText().toString().contains("Loading") || manufacturer.getText().toString().contains("Loading") ||
                        depot.getText().toString().contains("Loading")) {
                    Toast.makeText(getContext(), "Choose correct items from the list. Check internet connection.",
                            Toast.LENGTH_LONG).show();
                } else {
                    SendRequest request = new SendRequest();
                    JSONObject postDataParams = new JSONObject();

                    //Passing data as parameter

                    try {
                        postDataParams.put("addTrainP", 1);
                        postDataParams.put("trainP", car1.getText().toString() + " - " + car8.getText().toString());
                        postDataParams.put("prjNoP", prjNos.getText().toString().substring(0, prjNos.getText().toString().indexOf(" ")));
                        postDataParams.put("prjP", prjNos.getText().toString().substring(prjNos.getText().toString().indexOf(" ") + 1));
                        postDataParams.put("car1P", car1.getText().toString());
                        postDataParams.put("car2P", car2.getText().toString());
                        postDataParams.put("car3P", car3.getText().toString());
                        postDataParams.put("car4P", car4.getText().toString());
                        postDataParams.put("car5P", car5.getText().toString());
                        postDataParams.put("car6P", car6.getText().toString());
                        postDataParams.put("car7P", car7.getText().toString());
                        postDataParams.put("car8P", car8.getText().toString());
                        postDataParams.put("manP", manufacturer.getText().toString());
                        postDataParams.put("dateP", dateTxt.getText().toString());
                        postDataParams.put("depotP", depot.getText().toString());
                        postDataParams.put("descriptionP", comm1.getText().toString());
                        postDataParams.put("commentP", comm2.getText().toString());
                        postDataParams.put("swVerP", getResources().getString(R.string.app_version));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    request.execute(postDataParams);
                    addBtn.setEnabled(false);
                }

            }
        });
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
                conn.setReadTimeout(23000 /* milliseconds */);
                conn.setConnectTimeout(23000 /* milliseconds */);
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

        @Override
        protected void onPostExecute(String result) {
            if (result.contains("false") || result.contains("Exception")){
                Toast.makeText(getContext(), result,
                        Toast.LENGTH_LONG).show();
            }
            else {
                if (result.contains("Success")) {
                    Toast.makeText(getContext(), result,
                            Toast.LENGTH_LONG).show();

                    MainFragment mainFragment = new MainFragment();

                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, mainFragment, "Main_Fragment")
                            .commit();

                }
                else if(result.contains("Depot#") && result.length() > 6) {
                    splitResult(result, adapter_dep);
                }
                else if(result.contains("Projects#") && result.length() > 9) {
                    splitResult(result, adapter_prj);
                }
                else if(result.contains("Manufact#") && result.length() > 9) {
                    splitResult(result, adapter_man);
                } else {
                    Toast.makeText(getContext(), result,
                            Toast.LENGTH_LONG).show();
                }

            }
            addBtn.setEnabled(true);
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