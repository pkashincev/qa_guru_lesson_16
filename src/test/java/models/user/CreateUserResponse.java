package models.user;

import lombok.Data;

@Data
public class CreateUserResponse extends User {
    private String createdAt;
}
