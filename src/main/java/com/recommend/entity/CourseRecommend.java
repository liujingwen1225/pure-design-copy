package com.recommend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 课程推荐表
 * </p>
 *
 * @author author
 * @since 2023-03-02
 */
@Getter
@Setter
@TableName("course_recommend")
@ApiModel(value = "CourseRecommend对象", description = "课程推荐表")
public class CourseRecommend implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("课程id")
    private Integer courseId;

    @ApiModelProperty("推荐排序key")
    private Integer ranking;


}
