package be.technobel.westpole_visitor_journal.model.dto;

public class UserDto {

    private String userName;
    private String password;

    public UserDto(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public UserDto() {

    }

    public String getUserName() {
        return userName;
    }

    public UserDto setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserDto setPassword(String password) {
        this.password = password;
        return this;
    }
}
