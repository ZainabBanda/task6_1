// app/src/main/java/com/mine/pl/User.java
package com.mine.pl;

/**
 * Represents an authenticated user.
 */
public class User {
    public String username;
    public String token;  // e.g. JWT

    public User(String username, String token) {
        this.username = username;
        this.token = token;
    }
}
