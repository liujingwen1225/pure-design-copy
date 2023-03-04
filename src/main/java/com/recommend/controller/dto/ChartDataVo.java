package com.recommend.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 图表数据签证官
 *
 * @author calm
 * @date 2023/03/04
 */
@Data
@ApiModel("图表数据")
public class ChartDataVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("今年选课用户")
    private Long userNumber;

    @ApiModelProperty("课程数量")
    private Long courseNumber;

    @ApiModelProperty("授课教师数量")
    private Long teacherNumber;

    @ApiModelProperty("授课学校数量")
    private Long schoolNumber;

    @ApiModelProperty("热门课程名称数组")
    private List<String> popularCourseNameList;

    @ApiModelProperty("热门课程人数数组")
    private List<Long> popularCourseNumberList;

    @ApiModelProperty("热门学校名称数组")
    private List<String> popularSchoolNameList;

    @ApiModelProperty("热门学校人数数组")
    private List<Long> popularSchoolNumberList;

    @ApiModelProperty("热门老师名称数组")
    private List<String> popularTeacherNameList;

    @ApiModelProperty("热门老师人数数组")
    private List<Long> popularTeacherNumberList;


}
