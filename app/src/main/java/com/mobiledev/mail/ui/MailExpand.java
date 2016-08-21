package com.mobiledev.mail.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mobiledev.mail.R;
import com.mobiledev.mail.models.GlobalVariable;
import com.mobiledev.mail.models.Participants;
import com.mobiledev.mail.rest.MailDBApiHelper;
import com.mobiledev.mail.utils.GlobalClass;

import java.util.ArrayList;

public class MailExpand extends AppCompatActivity {

    private static Intent intent;
    String previewText,Id,isStarred;
    Integer timeStamp;
    private static TextView mPreviewText, mTimeStamp,mBody, mParticipant, mName;
    private static CheckBox mChk;
    Context mCtx = this;
    ArrayList<Participants> mMailExpand = new ArrayList<>();
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_expand);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        mPreviewText = (TextView) findViewById(R.id.textView_preview);
        mTimeStamp = (TextView) findViewById(R.id.textView_timeStamp);
        mBody = (TextView) findViewById(R.id.textView_body);
        mParticipant = (TextView) findViewById(R.id.textView_participant);
        mName = (TextView) findViewById(R.id.textView_name);
        mChk = (CheckBox) findViewById(R.id.fav_check);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        intent = getIntent();
        Id = intent.getStringExtra("ID");
        mPreviewText.setText(intent.getStringExtra("subject"));
        isStarred = intent.getStringExtra("isStarred");
        mMailExpand = ((GlobalVariable) mCtx.getApplicationContext()).getMailBody();
        mBody.setText(mMailExpand.get(0).getBody());
        timeStamp = mMailExpand.get(0).getTs();
        String text = mMailExpand.get(0).getName().toString().replace("[", "").replace(",", " &").replace("]", "");
        mParticipant.setText(text);
        mName.setText( mMailExpand.get(0).getName().toString().replace("[", "").replace(",", " &").replace("]", ""));
        String result = GlobalClass.getTimeAgo(timeStamp,mCtx);
        mTimeStamp.setText(result);

        if(isStarred.equals("true")){
            mChk.setButtonDrawable(R.drawable.ic_fab_grade_fill);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home)
        {
            Intent main = new Intent(this,MainActivity.class);
            main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(main);
        }if( id == R.id.delete_mail){
            mProgressBar.setVisibility(View.VISIBLE);
            callDeleteApi();
        }

        return super.onOptionsItemSelected(item);
    }

    private void callDeleteApi(){

        android.os.Handler handler = new android.os.Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                if(inputMessage.what==1) {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(),"Mail deleted",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MailExpand.this, MainActivity.class);
                    startActivity(intent);

                }

                if(inputMessage.what==0) {
                    mProgressBar.setVisibility(View.GONE);
                    Bundle b = inputMessage.getData();
                    Toast.makeText(mCtx,b.get("message").toString(),Toast.LENGTH_LONG).show();

                }
            }

        };
        MailDBApiHelper.startAction(mCtx, "Delete Mail", Id, handler);

    }

}
