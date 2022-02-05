package model;

import lombok.Getter;
import lombok.Setter;

public class UserModel {
    @Getter @Setter
    private int id;

    @Getter @Setter
    private String name;

    public boolean isNew(){
        return id <= 0;
    }

    public UserModel(){
    }

    // constructor for fast creation of new users - used in tests
    public UserModel(String name) {
        this(0, name);
    }

    public UserModel(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
