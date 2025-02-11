package models.user;

import lombok.Data;

@Data
public class SingleUserResponse {
    private UserInfo data;
    private SupportInfo support;
}
