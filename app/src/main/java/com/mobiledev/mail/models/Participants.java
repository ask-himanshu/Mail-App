
package com.mobiledev.mail.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Participants {

    private String subject;
    private List<Participant> participants = new ArrayList<Participant>();
    private String preview;
    private Boolean isRead;
    private Boolean isStarred;
    private Integer id;
    private String body;
    private Integer ts;
    public ArrayList <String> name = new ArrayList();
    public ArrayList <String> email = new ArrayList();


    /**
     * 
     * @return
     *     The subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * 
     * @param subject
     *     The subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * 
     * @return
     *     The participants
     * @param string
     */
    public List<Participant> getParticipants(String string) {
        return participants;
    }

    /**
     * 
     * @param participants
     *     The participants
     */
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    /**
     * 
     * @return
     *     The preview
     */
    public String getPreview() {
        return preview;
    }

    /**
     * 
     * @param preview
     *     The preview
     */
    public void setPreview(String preview) {
        this.preview = preview;
    }

    /**
     * 
     * @return
     *     The isRead
     */
    public Boolean getIsRead() {
        return isRead;
    }

    /**
     * 
     * @param isRead
     *     The isRead
     */
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    /**
     * 
     * @return
     *     The isStarred
     */
    public Boolean getIsStarred() {
        return isStarred;
    }

    /**
     * 
     * @param isStarred
     *     The isStarred
     */
    public void setIsStarred(Boolean isStarred) {
        this.isStarred = isStarred;
    }

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The body
     */
    public String getBody() {
        return body;
    }

    /**
     * 
     * @param body
     *     The body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * 
     * @return
     *     The ts
     */
    public Integer getTs() {
        return ts;
    }

    /**
     * 
     * @param ts
     *     The ts
     */
    public void setTs(Integer ts) {
        this.ts = ts;
    }

    public ArrayList<String> getName() {
        return name;
    }
}
