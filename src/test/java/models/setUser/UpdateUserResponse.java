package models.setUser;

import lombok.Data;

@Data
public class UpdateUserResponse extends User {
    private String updatedAt;
}
