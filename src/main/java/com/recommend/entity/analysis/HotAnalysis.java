package com.recommend.entity.analysis;

import lombok.Data;

import java.io.Serializable;

/**
 * @TableName hot_analysis
 */
@Data
public class HotAnalysis implements Serializable {

    /**
     *
     */
    private String type;
    /**
     *
     */
    private String name;
    /**
     *
     */
    private Long nameNum;
    /**
     *
     */
    private String school;
    /**
     *
     */
    private Long schoolNum;
    /**
     *
     */
    private String instructor;
    /**
     *
     */
    private Long instructorNum;
    /**
     *
     */
    private Integer rn;


}
