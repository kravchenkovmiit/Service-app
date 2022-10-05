package com.example.ifetimerecorder;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckCarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckCarFragment extends Fragment {

    EditText            car;
    Button              chkBtn;
    TextInputLayout     checkCarLayout;
    MaterialTextView    chkResult;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CheckCarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckCarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckCarFragment newInstance(String param1, String param2) {
        CheckCarFragment fragment = new CheckCarFragment();
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
        return inflater.inflate(R.layout.fragment_check_car, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        car = view.findViewById(R.id.chk_car_etext);
        chkBtn = view.findViewById(R.id.fin_chk_btn);
        chkResult = view.findViewById(R.id.chk_result);
        checkCarLayout = view.findViewById(R.id.chk_car_layout);

        chkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (car.getText().toString().length() > 0) {
                    chkBtn.setEnabled(false);
                    //Getting list of places
                    SendRequest requestCheckCar = new SendRequest();
                    JSONObject postDataParamsCheck = new JSONObject();
                    try {
                        postDataParamsCheck.put("checkCarP", 1);
                        postDataParamsCheck.put("carP", car.getText().toString());
                        postDataParamsCheck.put("swVerP", getResources().getString(R.string.app_version));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    requestCheckCar.execute(postDataParamsCheck);
                } else {
                    Toast.makeText(getContext(), "Car is incorrect",
                            Toast.LENGTH_LONG).show();
                    chkBtn.setEnabled(true);
                }
            }
        });

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
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
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
                checkCarLayout.setVisibility(View.GONE);
                chkBtn.setVisibility(View.GONE);
                chkResult.setVisibility(View.VISIBLE);
                chkResult.setText(splitResult(result));
            }
            chkBtn.setEnabled(true);
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

    private String splitResult(String res) {

        String  strTmp;
        if (res.contains("Success#")) {
            strTmp = res.replace("Success#", "");
            //return strTmp;
            return strTmp.replace('#', '\n');
        }
        return res;
    }
}