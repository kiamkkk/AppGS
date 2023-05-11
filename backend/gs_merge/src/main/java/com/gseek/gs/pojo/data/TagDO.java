package com.gseek.gs.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Tag实体类
 *
 * @author Phak
 * @since 2023/5/9-13:14
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagDO {

    private Integer tagId;

    private String tagText;

    private Integer click;

    public TagDO(String tagText) {
        this.tagText = tagText;
    }
}
