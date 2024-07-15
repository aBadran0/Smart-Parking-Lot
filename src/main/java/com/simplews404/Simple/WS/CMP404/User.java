package com.simplews404.Simple.WS.CMP404;

import org.springframework.data.annotation.Id;
import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Container(containerName = "Users")
public class User {

    @Id
    @PartitionKey
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private boolean type;

    public User() {
    }

    @JsonCreator
    public User(@JsonProperty("username") String username, 
                @JsonProperty("password") String password, 
                @JsonProperty("firstName") String firstName, 
                @JsonProperty("lastName") String lastName,
                @JsonProperty("type") boolean type) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
    }

    // Standard getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

	public boolean getType() {
		return type;
	}

	public void setType(boolean type) {
		this.type = type;
	}
}
