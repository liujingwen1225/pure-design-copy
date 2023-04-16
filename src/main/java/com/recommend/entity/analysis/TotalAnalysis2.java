package com.recommend.entity.analysis;


import lombok.Data;

import java.io.Serializable;

/**
* 
* @TableName total_analysis
*/
@Data
public class TotalAnalysis2 implements Serializable {

    /**
    * 
    */
    private String labels;
    /**
    * 
    */
    private Long cnt;


}
