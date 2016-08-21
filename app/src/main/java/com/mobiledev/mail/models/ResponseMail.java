
package com.mobiledev.mail.models;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResponseMail {

    public String subject;
    public String preview;
    public ArrayList<String> participant;
    public String isRead;
    public String isStarred;
    public String id;
    public String timeStamp;

    public String getSubject() {
        return subject;
    }



    public String getPreview() {
        return preview;
    }

    public ArrayList<String> getParticipant() {
        return participant;
    }


    public String getIsRead() {
        return isRead;
    }

    public String getIsStarred() {
        return isStarred;
    }

    public String getId() {
        return id;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public void setParticipant(ArrayList<String> participant) {
        this.participant = participant;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public void setIsStarred(String isStarred) {
        this.isStarred = isStarred;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
