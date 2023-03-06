package com.recommend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author yx2
 */
@Data
@ApiModel("Course对象")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 课程类型
     */
    @ApiModelProperty("课程类型")
    private String type;

    /**
     * 课程链接
     */
    @ApiModelProperty("课程链接")
    private String link;

    /**
     * 参加人数
     */
    @ApiModelProperty("参加人数")
    private String participantsNumber;

    /**
     * 授课老师
     */
    @ApiModelProperty("授课老师")
    private String instructor;

    /**
     * 课程名称
     */
    @ApiModelProperty("课程名称")
    private String name;

    /**
     * 课程学校
     */
    @ApiModelProperty("课程学校")
    private String school;

    /**
     * 课程标签
     */
    @ApiModelProperty("课程标签")
    private String labels;

    /**
     * 开课开始时间
     */
    @ApiModelProperty("开课开始时间")
    private String startTime;

    /**
     * 开课结束时间
     */
    @ApiModelProperty("开课结束时间")
    private String endTime;

    /**
     * 课程状态
     */
    @ApiModelProperty("课程状态")
    private String status;

    /**
     * 课程评分
     */
    @ApiModelProperty("课程评分")
    private String grading;
    /**
     * 课程概述
     */
    @ApiModelProperty("课程概述")
    private String overview;
    /**
     * 封面图
     */
    @ApiModelProperty("封面图")
    private String coverImageUrl;

    @ApiModelProperty("选课状态：【1=未选，2=已选】")
    @TableField(exist = false)
    private Integer courseStatus;

    @ApiModelProperty("课程ids")
    @TableField(exist = false)
    private List<Integer> courseIds;

    @ApiModelProperty("课程类型数组")
    @TableField(exist = false)
    private String typeList;

    @ApiModelProperty("数量")
    @TableField(exist = false)
    private Long num;

    @ApiModelProperty("我的评分")
    @TableField(exist = false)
    private String rating;

    @ApiModelProperty("我的评分类型：【1=未评，2=已评】")
    @TableField(exist = false)
    private Integer myRatingType;

}
