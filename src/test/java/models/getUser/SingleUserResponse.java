package models.getUser;

import lombok.Data;

@Data
public class SingleUserResponse {
    private UserInfo data;
    private SuppotInfo support;
}
