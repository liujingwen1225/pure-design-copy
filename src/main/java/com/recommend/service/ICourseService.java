package com.recommend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.recommend.entity.Course;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 *
 */
public interface ICourseService extends IService<Course> {

    Page<Course> findPage(Page<Course> page, Course course);

    void setStudentCourse(Integer courseId, Integer studentId);

    /**
     * 我课程列表
     *
     * @param course 课程
     * @param page   页面
     * @return {@link Page}<{@link Course}>
     */
    Page<Course> myCourseList(Page<Course> page, Course course);

    /**
     * 取消选课
     *
     * @param courseId  进程id
     * @param studentId 学生证
     */
    void cancelCourseSelection(Integer courseId, Integer studentId);

    /**
     * 指数课程
     *
     * @param typeList 类型列表
     * @return {@link List}<{@link Course}>
     */
    List<Course> indexCourse(List<String> typeList);

    /**
     * 课程类型列表
     *
     * @return {@link List}<{@link Course}>
     */
    List<Course> courseTypeList();
}
