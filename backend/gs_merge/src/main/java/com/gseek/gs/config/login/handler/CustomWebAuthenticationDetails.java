package com.gseek.gs.config.login.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Objects;

/**
 * 用户id存在这里.
 * 替换原有的WebAuthenticationDetails.
 *
 * @author Phak
 * @since 2023/5/5-11:35
 */
@Slf4j
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    private int userId = -1;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
    }

    public CustomWebAuthenticationDetails(String remoteAddress, String sessionId) {
        super(remoteAddress, sessionId);
    }

    public CustomWebAuthenticationDetails setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomWebAuthenticationDetails that = (CustomWebAuthenticationDetails) o;
        return Objects.equals(getRemoteAddress(), that.getRemoteAddress())
                && Objects.equals(getSessionId(), that.getSessionId())
                && Objects.equals(getUserId(), that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRemoteAddress(), getSessionId(),this.userId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append(" [");
        sb.append("RemoteIpAddress=").append(this.getRemoteAddress()).append(", ");
        sb.append("SessionId=").append(this.getSessionId()).append("]");
        sb.append("UserId=").append(this.getUserId()).append("]");
        return sb.toString();
    }
}
