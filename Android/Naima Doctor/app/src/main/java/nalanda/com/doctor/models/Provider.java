package nalanda.com.doctor.models;

/**
 * Created by ps1 on 9/11/16.
 */
public class Provider extends BaseModel {
    private String id;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
