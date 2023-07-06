package com.gseek.gs.websocket.handel;

import java.security.Principal;

/**
 * @author Phak
 * @since 2023/5/15-17:16
 */
public class UserPrincipal implements Principal {

    private final String userId;

    public UserPrincipal(String userId) {
        this.userId=userId;
    }

    @Override
    public String getName() {
        return userId;
    }
}
