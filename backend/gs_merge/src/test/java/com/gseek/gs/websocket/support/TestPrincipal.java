package com.gseek.gs.websocket.support;

import java.security.Principal;

/**
 * @author Phak
 * @since 2023/6/29-0:32
 */
public class TestPrincipal implements Principal {

    String name;

    public TestPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return null;
    }
}
