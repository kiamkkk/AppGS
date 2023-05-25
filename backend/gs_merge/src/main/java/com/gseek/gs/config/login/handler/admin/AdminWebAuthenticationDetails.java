package com.gseek.gs.config.login.handler.admin;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Objects;

/**
 * @author Isabella
 * @since 2023/5/17-16:18
 */
@Slf4j
public class AdminWebAuthenticationDetails extends WebAuthenticationDetails {

    private Integer adminId;

    public AdminWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
    }

    public AdminWebAuthenticationDetails(String remoteAddress, String sessionId) {
        super(remoteAddress, sessionId);
    }

    public AdminWebAuthenticationDetails setAdminId(int adminId) {
        this.adminId = adminId;
        return this;
    }

    public int getAdminId() {
        return adminId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdminWebAuthenticationDetails that = (AdminWebAuthenticationDetails) o;
        return Objects.equals(getRemoteAddress(), that.getRemoteAddress())
                && Objects.equals(getSessionId(), that.getSessionId())
                && Objects.equals(getAdminId(), that.getAdminId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRemoteAddress(), getSessionId(),this.adminId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append(" [");
        sb.append("RemoteIpAddress=").append(this.getRemoteAddress()).append(", ");
        sb.append("SessionId=").append(this.getSessionId()).append("]");
        sb.append("AdminId=").append(this.getAdminId()).append("]");
        return sb.toString();
    }
}
