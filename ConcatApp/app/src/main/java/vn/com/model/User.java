package vn.com.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "user")
public class User implements Serializable,Comparable<User> {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String userName;
    private String sdt;
    private String email;

    public User(int id, String userName, String sdt, String email) {
        this.id = id;
        this.userName = userName;
        this.sdt = sdt;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int compareTo(User employee) {
        return this.userName.compareToIgnoreCase(employee.userName);
    }
}
