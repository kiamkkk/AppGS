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
    private int claimer_id;
    private int respondent_id;
    private String appeal_reason;
    private boolean appeal_result;
    private boolean checked;
    private int admin_id;
    private int black_id;
    private String disapprove_reason;
    private String provePic;

    public BlacklistDO(int claimer_id, int respondent_id, String appeal_reason, String provePic) {
        this.claimer_id = claimer_id;
        this.respondent_id = respondent_id;
        this.appeal_reason = appeal_reason;
        this.provePic=provePic;
    }

    public BlacklistDO(String appeal_reason, int black_id, String provePic) {
        this.appeal_reason = appeal_reason;
        this.black_id = black_id;
        this.provePic = provePic;
    }

    public BlacklistDO(boolean appeal_result, int admin_id, String disapprove_reason) {
        this.appeal_result = appeal_result;
        this.admin_id = admin_id;
        this.disapprove_reason = disapprove_reason;
    }

    public BlacklistDO(int claimer_id, int respondent_id, String appeal_reason, boolean appeal_result, boolean checked, int admin_id, String disapprove_reason, String provePic) {
        this.claimer_id = claimer_id;
        this.respondent_id = respondent_id;
        this.appeal_reason = appeal_reason;
        this.appeal_result = appeal_result;
        this.checked = checked;
        this.admin_id = admin_id;
        this.disapprove_reason = disapprove_reason;
        this.provePic = provePic;
    }

    public BlacklistDO(int admin_id, boolean appeal_result, int black_id, String disapprove_reason) {
        this.appeal_result = appeal_result;
        this.admin_id = admin_id;
        this.disapprove_reason = disapprove_reason;
        this.black_id=black_id;

    }


}
