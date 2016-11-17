package ng.iceetech2016.teamui.ixtech.model;

/**
 * Created by unorthodox on 17/11/16.
 * A POJO model class for the feedback
 */
public class FeedbackPOJO {
    private String id, institution, message, cname, email,date_added;

    public FeedbackPOJO(){

    }

    public FeedbackPOJO(String id, String institution, String message, String cname,
                        String email,
                        String date_added){

        this.institution =institution;
        this.cname=cname;
        this.message=message;
        this.date_added=date_added;
        this.email=email;
        this.id=id;
    }

    public String getCname() {
        return cname;
    }
    public void setCname(String cname) {
        this.cname = cname;
    }
    public String getDate_added() {
        return date_added;
    }
    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getInstitution() {
        return institution;
    }
    public void setInstitution(String institution) {
        this.institution = institution;
    }
}
