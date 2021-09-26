package vn.com.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import vn.com.model.User;

@Dao
public interface UserDAO {
    @Insert
    void insertUser(User user);

    @Query("SELECT *FROM user")
    List<User> mListUser();

    @Query("SELECT *FROM user where userName=:userName")
    List<User> checkUser(String userName);
    @Update
    void updateUser(User user);
    @Delete
    void deleteUser(User user);
    @Query("DELETE FROM user")
    void deleteAllUser();
    @Query("SELECT *FROM user WHERE userName LIKE '%'|| :name ||'%'")
    List<User> mListUser(String name);
}
