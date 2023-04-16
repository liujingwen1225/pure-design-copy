package com.recommend.controller.dto;

import com.recommend.entity.analysis.ChartModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * 图表数据签证官
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
    @ApiModelProperty("进行中课程")
    private Long startCourse;
    @ApiModelProperty("未开始课程")
    private Long notStartCourse;
    @ApiModelProperty("已结束课程")
    private Long endCourse;
    @ApiModelProperty("精品课程数")
    private Long boutiqueCourses;
    @ApiModelProperty("先修课程数")
    private Long purerCourses;

    @ApiModelProperty("热门课程名称数组")
    private List<String> popularCourseNameList;
    @ApiModelProperty("词云数组")
    private List<Map<String,Object>> wordCould;

    @ApiModelProperty("热门学校占比数组")
    private List<ChartModel> hotSchoolZb;

    @ApiModelProperty("热门类型占比数组")
    private List<ChartModel> hotTypeZb;

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
