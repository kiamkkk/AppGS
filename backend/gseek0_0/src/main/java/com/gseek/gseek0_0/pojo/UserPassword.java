package com.gseek.gseek0_0.pojo;


public class UserPassword {
    private int user_id;
    private String password;
    private String salt;

    public UserPassword(int userId, String password, String salt) {
        this.user_id = userId;
        this.password = password;
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + user_id +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                '}';
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int userId) {
        this.user_id = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
