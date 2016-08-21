package com.mobiledev.mail.models;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by hp on 17-Aug-16.
 */
public class GlobalVariable extends Application {


    private ArrayList<ResponseMail> allMail;
    private ArrayList<Participants> mailBody;

    public void setAllMail(ArrayList<ResponseMail> allMail) {
        this.allMail = allMail;
    }

    public ArrayList<ResponseMail> getAllMail() {
        return allMail;
    }

    public void setMailBody(ArrayList<Participants> mailBody) {
        this.mailBody = mailBody;
    }

    public ArrayList<Participants> getMailBody() {
        return mailBody;
    }

}
