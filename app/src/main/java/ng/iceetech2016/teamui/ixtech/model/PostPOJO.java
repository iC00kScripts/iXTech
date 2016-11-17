package ng.iceetech2016.teamui.ixtech.model;

/**
 * Created by unorthodox on 17/11/16.
 * a generic pojo class for posts/ announcements
 */
public class PostPOJO {
    private String id, message, email,date_added;

    public PostPOJO(){

    }

    public PostPOJO(String id, String message, String email,
                    String date_added){
        this.message=message;
        this.date_added=date_added;
        this.email=email;
        this.id=id;
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
}
