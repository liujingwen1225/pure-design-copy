package com.recommend.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author author
 * @since 2023-02-28
 */
@Getter
@Setter
@TableName("student_course")
@ApiModel(value = "StudentCourse对象", description = "")
public class StudentCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private Integer studentId;

    @ApiModelProperty("课程id")
    private Integer courseId;


}
