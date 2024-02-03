package com.ufund.api.ufundapi.model;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthUser{
    static final String STRING_FORMAT = "AuthUser [username='%s',password='%s']";
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;

    public AuthUser(@JsonProperty("username") String username,
                @JsonProperty("password") String password){
                this.username = username;
                this.password = password;
    }

    public String getUsername(){ return this.username; }
    public String getPassword(){ return this.password; }

    public String toString(){
        return String.format(STRING_FORMAT,this.username,this.password);
    }
}
