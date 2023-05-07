package com.gseek.gseek0_0.pojo;

import lombok.*;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blacklist {
    private int claimer_id;
    private int respondent_id;
    private String appeal_reason;
    private boolean appeal_result;
    private boolean checked;
    private int admin_id;
    private int black_id;
    private String disapprove_reason;

    private String provePic;

    public Blacklist(int claimer_id, int respondent_id, String appeal_reason,String provePic) {
        this.claimer_id = claimer_id;
        this.respondent_id = respondent_id;
        this.appeal_reason = appeal_reason;
        this.provePic=provePic;
    }

    public Blacklist(String appeal_reason, int black_id, String provePic) {
        this.appeal_reason = appeal_reason;
        this.black_id = black_id;
        this.provePic = provePic;
    }

    public Blacklist(boolean appeal_result, int admin_id, String disapprove_reason) {
        this.appeal_result = appeal_result;
        this.admin_id = admin_id;
        this.disapprove_reason = disapprove_reason;
    }

    public Blacklist(int admin_id,boolean appeal_result,int black_id,String disapprove_reason) {
        this.appeal_result = appeal_result;
        this.admin_id = admin_id;
        this.disapprove_reason = disapprove_reason;
        this.black_id=black_id;

    }


}
