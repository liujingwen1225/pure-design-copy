package com.recommend.entity.analysis;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
* 
* @TableName year_month_analysis
*/
@Data
public class YearMonthAnalysis implements Serializable {

    /**
    * 
    */
    @ApiModelProperty("")
    private Integer year;
    /**
    * 
    */
    @ApiModelProperty("")
    private Integer ymonth;
    /**
    * 
    */
    @ApiModelProperty("")
    private String name;
    /**
    * 
    */
    @ApiModelProperty("")
    private Long namePns;
    /**
    * 
    */
    @ApiModelProperty("")
    private String instructor;
    /**
    * 
    */
    @ApiModelProperty("")
    private Long instructorPns;
    /**
    * 
    */
    @ApiModelProperty("")
    private String school;
    /**
    * 
    */
    @ApiModelProperty("")
    private Long schoolPns;


}
