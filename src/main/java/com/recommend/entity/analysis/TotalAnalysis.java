package com.recommend.entity.analysis;


import lombok.Data;

import java.io.Serializable;

/**
* 
* @TableName total_analysis
*/
@Data
public class TotalAnalysis implements Serializable {

    /**
    * 
    */
    private Long schoolCnt;
    /**
    * 
    */
    private Long nameCnt;
    /**
    * 
    */
    private Long instructorCnt;
    /**
    * 
    */
    private Long studentCnt;


}
