package com.mobiledev.mail.rest;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.mobiledev.mail.models.GlobalVariable;
import com.mobiledev.mail.models.Participants;
import com.mobiledev.mail.models.ResponseMail;
import com.mobiledev.mail.utils.AppConstants;
import com.mobiledev.mail.utils.GlobalClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hp on 17-Aug-16.
 */
public class MailDBApiHelper extends IntentService {

    public static String mailApi = AppConstants.BASE_URL;
    public static String mailDetailsApi;
    static Message msg;
    String action;
    static Context mContext;
    ResponseMail mResponseMail = new ResponseMail();
    Participants mParticipants = new Participants();
    ArrayList<ResponseMail> arrayMail = new ArrayList<>();
    ArrayList<Participants> mailBody = new ArrayList<>();

    public MailDBApiHelper() {
        super("MailDBApiHelper");
    }

    public static void startAction(Context ctx, String action, String id, Handler handler){
        mContext = ctx;
        mailDetailsApi = AppConstants.BASE_URL+id;
        msg = handler.obtainMessage();
        Intent intent = new Intent(ctx, MailDBApiHelper.class);
        intent.setAction(action);
        ctx.startService(intent);

    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        if (intent != null) {
            action = intent.getAction();
            if (action.equals("Mail List")) {
                final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, mailApi, new Response.Listener<JSONArray>() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json = jsonArray.getJSONObject(i);
                                mResponseMail.setSubject(json.getString(AppConstants.SUBJECT));
                                mResponseMail.setPreview(json.getString(AppConstants.PREVIEW));
                                JSONArray array = json.getJSONArray(AppConstants.PARTICIPANTS);
                                mResponseMail.participant = new ArrayList<>();
                                for (int j=0; j <array.length(); j++){
                                    mResponseMail.participant.add(array.getString(j));
                                }
                                mResponseMail.setIsRead(json.getString(AppConstants.IS_READ));
                                mResponseMail.setIsStarred(json.getString(AppConstants.IS_STARRED));
                                mResponseMail.setId(json.getString(AppConstants.ID));
                                mResponseMail.setTimeStamp(json.getString(AppConstants.TIMESTAMP));

                                arrayMail.add(mResponseMail);
                                mResponseMail = new ResponseMail();
                            }

                            ((GlobalVariable) mContext.getApplicationContext()).setAllMail(arrayMail);
                            if (msg != null) {
                                msg.what = 1;
                                msg.sendToTarget();

                            }
                        } catch (JSONException e) {
                            Log.d("ResponseList", e.toString());
                            if (msg != null) {
                                Bundle b = new Bundle();
                                b.putString("message", e.toString());
                                msg.what = 0;
                                msg.setData(b);
                                msg.sendToTarget();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (msg != null) {
                            Bundle b = new Bundle();
                            b.putString("message", error.toString());
                            msg.what = 0;
                            msg.setData(b);
                            msg.sendToTarget();
                        }
                    }
                });
                request.setRetryPolicy(new DefaultRetryPolicy(30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                GlobalClass.getInstance(mContext).makeRequest(request);
            }

            if (action.equals("Mail Details")) {
                final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mailDetailsApi, new Response.Listener<JSONObject>() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());

                                mParticipants.setBody(jsonObject.getString(AppConstants.BODY));
                                mParticipants.setTs(jsonObject.getInt(AppConstants.TIMESTAMP));
                                mParticipants.setTs(jsonObject.getInt(AppConstants.TIMESTAMP));
                                mParticipants.getParticipants(jsonObject.getString(AppConstants.PARTICIPANTS));
                                JSONArray jsonRequest = jsonObject.getJSONArray(AppConstants.PARTICIPANTS);
                                for (int i=0; i<jsonRequest.length(); i++){
                                    JSONObject user = jsonRequest.getJSONObject(i);
                                    mParticipants.name.add(user.getString(AppConstants.NAME));
                                    mParticipants.email.add(user.getString(AppConstants.EMAIL));
                                }

                                mailBody.add(mParticipants);
                                mParticipants = new Participants();


                            ((GlobalVariable) mContext.getApplicationContext()).setMailBody(mailBody);
                            if (msg != null) {
                                msg.what = 1;
                                msg.sendToTarget();

                            }
                        } catch (JSONException e) {
                            Log.d("ResponseList", e.toString());
                            if (msg != null) {
                                Bundle b = new Bundle();
                                b.putString("message", e.toString());
                                msg.what = 0;
                                msg.setData(b);
                                msg.sendToTarget();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (msg != null) {
                            Bundle b = new Bundle();
                            b.putString("message", error.toString());
                            msg.what = 0;
                            msg.setData(b);
                            msg.sendToTarget();
                        }
                    }
                });
                request.setRetryPolicy(new DefaultRetryPolicy(30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                GlobalClass.getInstance(mContext).makeRequest(request);
            }

            if (action.equals("Delete Mail")) {
                final StringRequest request = new StringRequest(Request.Method.DELETE, mailDetailsApi, new Response.Listener<String>() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {

                        if (msg != null) {
                            msg.what = 1;
                            msg.sendToTarget();

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (msg != null) {
                            Bundle b = new Bundle();
                            b.putString("message", error.toString());
                            msg.what = 0;
                            msg.setData(b);
                            msg.sendToTarget();
                        }
                    }
                });
                request.setRetryPolicy(new DefaultRetryPolicy(30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                GlobalClass.getInstance(mContext).makeRequest(request);
            }
        }


    }
}
