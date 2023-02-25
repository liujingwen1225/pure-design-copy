package com.recommend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 */
@Getter
@Setter
@ApiModel(value = "Course对象", description = "")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 课程类型
     */
    @ApiModelProperty(name = "课程类型")
    private String type;

    /**
     * 课程链接
     */
    @ApiModelProperty(name = "课程链接")
    private String link;

    /**
     * 参加人数
     */
    @ApiModelProperty(name = "参加人数")
    private String participantsNumber;

    /**
     * 授课老师
     */
    @ApiModelProperty(name = "授课老师")
    private String instructor;

    /**
     * 课程名称
     */
    @ApiModelProperty(name = "课程名称")
    private String name;

    /**
     * 课程学校
     */
    @ApiModelProperty(name = "课程学校")
    private String school;

    /**
     * 课程标签
     */
    @ApiModelProperty(name = "课程标签")
    private String labels;

    /**
     * 开课开始时间
     */
    @ApiModelProperty(name = "开课开始时间")
    private String startTime;

    /**
     * 开课结束时间
     */
    @ApiModelProperty(name = "开课结束时间")
    private String endTime;

    /**
     * 课程状态
     */
    @ApiModelProperty(name = "课程状态")
    private String status;

    /**
     * 课程评分
     */
    @ApiModelProperty(name = "课程评分")
    private String grading;

    /**
     * 封面图
     */
    @ApiModelProperty(name = "封面图")
    private String coverImageUrl;

}
