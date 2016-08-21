package com.mobiledev.mail.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mobiledev.mail.R;
import com.mobiledev.mail.adapter.MailListViewAdapter;
import com.mobiledev.mail.models.GlobalVariable;
import com.mobiledev.mail.models.ResponseMail;
import com.mobiledev.mail.rest.MailDBApiHelper;
import com.mobiledev.mail.utils.GlobalClass;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private RecyclerView mMailRecycler;
    private MailListViewAdapter mailListViewAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Context mCtx = this;
    ArrayList<ResponseMail> mails = new ArrayList<>();
    private ProgressBar mProgressBar;
    private LinearLayout mNoMailLayout,mErrorLayout;
    private String id,subject,isStarred;
    Button mRetryBtn;
    private ResponseMail select = new ResponseMail();
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mNoMailLayout = (LinearLayout) findViewById(R.id.nomail_layout);
        mErrorLayout = (LinearLayout) findViewById(R.id.error_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRetryBtn = (Button) findViewById(R.id.btn_retry);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        mMailRecycler = (RecyclerView) findViewById(R.id.mail_recyclerView);
        assert mMailRecycler != null;
        mMailRecycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mCtx);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mMailRecycler.setLayoutManager(llm);
        mProgressBar.setVisibility(View.VISIBLE);

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.mail_swipe);
        assert mSwipeRefreshLayout != null;
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                fetchMail();
            }
        });
    }

    private void fetchMail() {
        mSwipeRefreshLayout.setRefreshing(true);

        android.os.Handler handler = new android.os.Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                if(inputMessage.what==1)
                {
                    mails = ((GlobalVariable) mCtx.getApplicationContext()).getAllMail();
                    mProgressBar.setVisibility(View.GONE);
                    hideErrorLayout();
                    mSwipeRefreshLayout.setRefreshing(false);

                    if(mails.size() == 0){
                      showNoMailLayout();
                    }else {
                        hideNoMailLayout();
                    }
                    mailListViewAdapter = new MailListViewAdapter(mails, mCtx);
                    mailListViewAdapter.setOnClickListner(new MailListViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            select = mails.get(position);
                            id = select.getId();
                            subject = select.getSubject();
                            isStarred = select.getIsStarred();
                            Handler handler1 = new Handler(Looper.getMainLooper()){
                                @Override
                                public void handleMessage(Message message){
                                    if(message.what==1){
                                        Intent readMail = new Intent(MainActivity.this,MailExpand.class);
                                        readMail.putExtra("ID",id);
                                        readMail.putExtra("subject",subject);
                                        readMail.putExtra("isStarred",isStarred);
                                        startActivity(readMail);

                                    }if(message.what==0){
                                        Bundle b = message.getData();
                                        Toast.makeText(getApplicationContext(),""+b.toString(),Toast.LENGTH_SHORT).show();
                                    }

                                }
                            };MailDBApiHelper.startAction(mCtx, "Mail Details", id, handler1);
                        }
                    });
                    mMailRecycler.setAdapter(mailListViewAdapter);
                }

                if(inputMessage.what==0)
                {
                    mProgressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    Bundle b = inputMessage.getData();
                    Toast.makeText(mCtx,b.get("message").toString(),Toast.LENGTH_LONG).show();
                    showErrorLayout();
                }
            }

        };
        if(GlobalClass.getInstance(mCtx).isNetworking())
        {
            MailDBApiHelper.startAction(mCtx, "Mail List", null, handler);
        }else
        {
            mSwipeRefreshLayout.setRefreshing(false);
            showErrorLayout();
        }
    }



    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_inbox) {
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawers();

        } else if (id == R.id.nav_star) {
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawers();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefresh() {
        fetchMail();

    }

    private void showNoMailLayout(){
        mMailRecycler.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mNoMailLayout.setVisibility(View.VISIBLE);
    }

    private void hideNoMailLayout(){
        mMailRecycler.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mNoMailLayout.setVisibility(View.GONE);
    }

    private void showErrorLayout() {
        mMailRecycler.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.VISIBLE);
    }

    private void hideErrorLayout(){
        mMailRecycler.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        if(v == mRetryBtn){
            fetchMail();
        }
    }
}
