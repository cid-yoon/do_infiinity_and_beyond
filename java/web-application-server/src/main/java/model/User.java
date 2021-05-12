package model;

import java.util.Map;

public class User {

    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public static User create(Map<String, String> paramMap) {

        return new User(paramMap.get("userId"),
                paramMap.get("password"),
                paramMap.get("name"),
                paramMap.get("email")
        );

    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
