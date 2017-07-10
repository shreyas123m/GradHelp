package com.uta.gradhelp.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.blunderer.materialdesignlibrary.activities.Activity;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.uta.gradhelp.Application.AdvisorInfo;
import com.uta.gradhelp.Application.GradHelp;
import com.uta.gradhelp.Application.Server;
import com.uta.gradhelp.AsyncTask.ConnectionHelper;
import com.uta.gradhelp.Fragments.HomeFragment;
import com.uta.gradhelp.Interfaces.NetworkCallbackListener;
import com.uta.gradhelp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class BookAppointmentActivity extends Activity {

    HashMap<String, AdvisorInfo> advisorInfoHashMap;
    String advisor, reason;
    Boolean customReason = false;
    LinearLayout advisorList, reasonList;
    String advisorName;
    String todayDate;
    public static String sendJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        todayDate = (dateFormat.format(date)); //2014/08/06 15:59:48
        //setContentView(R.layout.activity_book_appointment);

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        advisorList = (LinearLayout) findViewById(R.id.advisorListContainer);
        reasonList = (LinearLayout) findViewById(R.id.reasonListContainer);
        advisorInfoHashMap = new HashMap<>();
        //get advisor names according to netID
        new ConnectionHelper(this, "getAdvisorNames", new NetworkCallbackListener() {
            @Override
            public void onResponse(String response) {
                //response = "[{\"net_id\" : \"brk4321\",\"first_name\" : \"Bahram\",\"last_name\" : \"Khalili\",\"advising_start_time\" : \"14\",\"advising_end_time\" : \"16\",\"week_days\" : \"Thursday\",\"room_no\" : \"502\",\"building\" : \"ERB\"},{\"net_id\" : \"sjd4321\",\"first_name\" : \"Sajib\",\"last_name\" : \"Dutta\",\"advising_start_time\" : \"14\",\"advising_end_time\" : \"16\",\"week_days\" : \"Wednesday\",\"room_no\" : \"502\",\"building\" : \"ERB\"},{\"net_id\" : \"elm4321\",\"first_name\" : \"Ramez\",\"last_name\" : \"Elmasri\",\"advising_start_time\" : \"14\",\"advising_end_time\" : \"16\",\"week_days\" : \"Friday\",\"room_no\" : \"502\",\"building\" : \"ERB\"}]";

                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //System.out.println(jsonArray.length());
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String key = jsonObject.getString("net_id");
                        if (advisorInfoHashMap.containsKey(key)) {
                            if (!advisorInfoHashMap.get(key).getWeek_days().equalsIgnoreCase(jsonObject.getString("week_days")) &&
                                    jsonObject.getString("week_days").equalsIgnoreCase(new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime()))) {
                                advisorInfoHashMap.remove(key);
                                advisorInfoHashMap.put(key, new AdvisorInfo(jsonObject.getString("net_id"), jsonObject.getString("first_name"), jsonObject.getString("last_name"), jsonObject.getString("advising_start_time"), jsonObject.getString("advising_end_time"), jsonObject.getString("week_days"), jsonObject.getString("room_no"), jsonObject.getString("building")));

                            }
                        } else {
                            advisorInfoHashMap.put(key, new AdvisorInfo(jsonObject.getString("net_id"), jsonObject.getString("first_name"), jsonObject.getString("last_name"), jsonObject.getString("advising_start_time"), jsonObject.getString("advising_end_time"), jsonObject.getString("week_days"), jsonObject.getString("room_no"), jsonObject.getString("building")));
                        }


                    }

                    for (AdvisorInfo advisorInfo : advisorInfoHashMap.values()) {
                        System.out.println(advisorInfo.getNetID() + ": " + advisorInfo.getWeek_days());
                    }
                    int i = 0;
                    for (final AdvisorInfo advisorInfo : advisorInfoHashMap.values()) {


                        //advisorInfoArrayList.add(new AdvisorInfo(jsonObject.getString("net_id"), jsonObject.getString("first_name"), jsonObject.getString("last_name"), jsonObject.getString("advising_start_time"), jsonObject.getString("advising_end_time"), jsonObject.getString("week_days"), jsonObject.getString("room_no"), jsonObject.getString("building")));


                        MaterialRippleLayout materialRippleLayout = new MaterialRippleLayout(BookAppointmentActivity.this);
                        final Button button = new Button(BookAppointmentActivity.this);
                        button.setText(advisorInfo.getFirstName() + " " + advisorInfo.getLastName());
                        button.setBackgroundColor(Color.rgb(255, 255, 255));
                        materialRippleLayout.addView(button);
                        materialRippleLayout.setRippleOverlay(true);
                        if (new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime()).equalsIgnoreCase(advisorInfo.getWeek_days())) {
                            button.setEnabled(true);
                        } else {
                            button.setEnabled(false);
                        }
                        advisorList.addView(materialRippleLayout);


                        //now reasons
                        final int finalI = i;
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                advisor = advisorInfo.getNetID();
                                advisorName = advisorInfo.getFirstName() + " " + advisorInfo.getLastName();


                                new AlertDialog.Builder(BookAppointmentActivity.this).setTitle(advisorInfo.getFirstName() + " " + advisorInfo.getLastName()).setMessage(
                                        "Start Time: " + advisorInfo.getStartTime() + "\n" +
                                                "End Time: " + advisorInfo.getEndTime() + "\n" +
                                                "Building: " + advisorInfo.getBuilding() + "\n" +
                                                "Room number: " + advisorInfo.getRoom_no()

                                ).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int ii) {
                                        //button.setTextColor(Color.BLUE);

                                        TextView textView = new TextView(BookAppointmentActivity.this);
                                        textView.setText("Choose Reason");
                                        textView.setTextColor(Color.BLACK);
                                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f);
                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                        params.setMargins(GradHelp.dpToPixel(16), GradHelp.dpToPixel(16), GradHelp.dpToPixel(16), GradHelp.dpToPixel(16));
                                        params.gravity = Gravity.CENTER;
                                        textView.setLayoutParams(params);
                                        final ArrayList<String> reasons = new ArrayList<String>();
                                        reasons.clear();
                                        reasons.add("General Advising");
                                        reasons.add("Course Selection");
                                        reasons.add("Other");

                                        reasonList.removeAllViews();
                                        reasonList.invalidate();
                                        reasonList.addView(textView);
                                        for (int i = 0; i < reasons.size(); i++) {
                                            MaterialRippleLayout materialRippleLayout = new MaterialRippleLayout(BookAppointmentActivity.this);
                                            final Button button = new Button(BookAppointmentActivity.this);
                                            button.setText(reasons.get(i));
                                            button.setBackgroundColor(Color.rgb(255, 255, 255));
                                            materialRippleLayout.addView(button);
                                            materialRippleLayout.setRippleOverlay(true);

                                            reasonList.addView(materialRippleLayout);
                                            final int finalI1 = i;
                                            button.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    //findViewById(R.id.reasonContainer).setVisibility(View.VISIBLE);
                                                    System.out.println("Reason clicked");
                                                    // button.setTextColor(Color.BLUE);
                                                    reason = reasons.get(finalI1);
                                                    if (finalI1 == reasonList.getChildCount() - 2) {
                                                        findViewById(R.id.otherContainer).setVisibility(View.VISIBLE);
                                                        reason = ((TextInputEditText) findViewById(R.id.otherReason)).getText().toString().trim();
                                                        customReason = true;
                                                    } else {
                                                        findViewById(R.id.otherContainer).setVisibility(View.GONE);
                                                        customReason = false;
                                                    }

                                                    findViewById(R.id.confirmContainer).setVisibility(View.VISIBLE);
                                                }
                                            });
                                        }
                                        System.out.println("After " + reasonList.getChildCount());
                                        findViewById(R.id.reasonContainer).setVisibility(View.VISIBLE);
                                        findViewById(R.id.confirmButton).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                //System.out.println(advisor);
                                                if (customReason)
                                                    reason = ((TextInputEditText) findViewById(R.id.otherReason)).getText().toString().trim();
                                                //System.out.println(reason);

                                                sendJSON = "{\"adv_netid\":\"" + advisor + "\",\"reason\":\"" + reason + "\",\"stud_netid\":\"" + GradHelp.getInstance().getLoginResponse().getNet_id() + "\",\"stud_mavid\":\"" + GradHelp.getInstance().getLoginResponse().getMaverick_Id() + "\",\"stud_name\":\"" + GradHelp.getInstance().getLoginResponse().getFirst_name() + " " + GradHelp.getInstance().getLoginResponse().getLast_name() + "\",\"adv_name\":\"" + advisorName + "\",\"session_date\":\"" + todayDate + "\",\"adv_complete\":\"false\",\"unqident\":\"" + GradHelp.getInstance().getLoginResponse().getNet_id() + "_" + advisor + "_" + todayDate + "\"}";
                                                // System.out.println(sendJSON);
                                                final ProgressDialog progressDialog = new ProgressDialog(BookAppointmentActivity.this);
                                                progressDialog.setIndeterminate(true);
                                                progressDialog.setMessage("Making an appointment...");
                                                progressDialog.setCancelable(false);
                                                progressDialog.show();

                                                new ConnectionHelper(BookAppointmentActivity.this, "checkSlots", advisor, new NetworkCallbackListener() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        if (!response.equalsIgnoreCase(Server.ERROR_OCCURRED)) {
                                                            try {
                                                                JSONObject jsonObject = new JSONObject(response);
                                                                if (jsonObject.getBoolean("available")) {
                                                                    new ConnectionHelper(BookAppointmentActivity.this, "makeAppointment", sendJSON, new NetworkCallbackListener() {
                                                                        @Override
                                                                        public void onResponse(String response) {
                                                                            progressDialog.dismiss();
                                                                            if (response.equalsIgnoreCase(Server.ERROR_OCCURRED)) {
                                                                                Toast.makeText(BookAppointmentActivity.this, "Connection problem", Toast.LENGTH_SHORT).show();
                                                                            } else {
                                                                                try {
                                                                                    JSONObject jsonObject1 = new JSONObject(response);
                                                                                    if (jsonObject1.getString("result").equalsIgnoreCase("success")) {
                                                                                        HomeFragment.fetchStatusFromServer = false;
                                                                                        BookAppointmentActivity.this.onBackPressed();
                                                                                        startActivity(new Intent(BookAppointmentActivity.this, HomeActivity.class));
                                                                                    } else {
                                                                                        Toast.makeText(BookAppointmentActivity.this, jsonObject1.getString("result"), Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                } catch (JSONException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        }
                                                                    }).execute();
                                                                } else {
                                                                    Toast.makeText(BookAppointmentActivity.this, "Slots are full", Toast.LENGTH_SHORT).show();
                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        } else {
                                                            Toast.makeText(BookAppointmentActivity.this, "Problem Connecting to server", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }).execute();


                                            }
                                        });
                                    }
                                }).setNegativeButton("Cancel", null).setCancelable(false).create().show();


                            }
                        });
                        i++;
                    }


                } catch (
                        JSONException e
                        )

                {
                    e.printStackTrace();
                }
            }

        }

        ).

                execute();

    }


    @Override
    protected int getContentView() {
        return R.layout.activity_book_appointment;
    }

    @Override
    protected boolean enableActionBarShadow() {
        return false;
    }

    @Override
    protected ActionBarHandler getActionBarHandler() {
        return null;
    }
}
