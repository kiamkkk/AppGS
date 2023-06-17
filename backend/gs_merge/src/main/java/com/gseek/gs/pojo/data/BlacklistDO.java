package com.gseek.gs.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * blacklist实体类
 *
 * @author Isabella
 * @since 2023/5/2-19:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistDO {
    private int claimerId;
    private int respondentId;
    private String appealReason;
    private boolean appealResult;
    private boolean checked;
    private int adminId;
    private int blackId;
    private String disapproveReason;
    private String provePic;

    public BlacklistDO(int claimerId, int respondentId, String appealReason, String provePic) {
        this.claimerId = claimerId;
        this.respondentId = respondentId;
        this.appealReason = appealReason;
        this.provePic=provePic;
    }

    public BlacklistDO(String appealReason, int blackId, String provePic) {
        this.appealReason = appealReason;
        this.blackId = blackId;
        this.provePic = provePic;
    }

    public BlacklistDO(boolean appealResult, int adminId, String disapproveReason) {
        this.appealResult = appealResult;
        this.adminId = adminId;
        this.disapproveReason = disapproveReason;
    }

    public BlacklistDO(int claimerId, int respondentId, String appealReason, boolean appealResult, boolean checked, int adminId, String disapproveReason, String provePic) {
        this.claimerId = claimerId;
        this.respondentId = respondentId;
        this.appealReason = appealReason;
        this.appealResult = appealResult;
        this.checked = checked;
        this.adminId = adminId;
        this.disapproveReason = disapproveReason;
        this.provePic = provePic;
    }

    public BlacklistDO(int adminId, boolean appealResult, int blackId, String disapproveReason) {
        this.appealResult = appealResult;
        this.adminId = adminId;
        this.disapproveReason = disapproveReason;
        this.blackId=blackId;

    }


}
