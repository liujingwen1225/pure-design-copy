package com.recommend.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.recommend.entity.Course;
import com.recommend.entity.StudentCourse;
import com.recommend.mapper.CourseMapper;
import com.recommend.mapper.StudentCourseMapper;
import com.recommend.service.ICourseService;
import com.recommend.utils.TokenUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Resource
    private CourseMapper courseMapper;
    @Resource
    private StudentCourseMapper studentCourseMapper;

    /**
     * 找到页面
     *
     * @param page   页面
     * @param course 课程
     * @return {@link Page}<{@link Course}>
     */
    @Override
    public Page<Course> findPage(Page<Course> page, Course course) {
        //老师，课程，学校，类别
        LambdaQueryWrapper<Course> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(course.getInstructor()), Course::getInstructor, course.getInstructor());
        lqw.like(StringUtils.isNotBlank(course.getName()), Course::getName, course.getName());
        lqw.like(StringUtils.isNotBlank(course.getSchool()), Course::getSchool, course.getSchool());
        lqw.like(StringUtils.isNotBlank(course.getType()), Course::getType, course.getType());
        return courseMapper.selectPage(page, lqw);
    }

    @Transactional
    @Override
    public void setStudentCourse(Integer courseId, Integer studentId) {
        courseMapper.deleteStudentCourse(courseId, studentId);
        courseMapper.setStudentCourse(courseId, studentId);
    }

    /**
     * 取消选课
     *
     * @param courseId  进程id
     * @param studentId 学生证
     */
    @Override
    public void cancelCourseSelection(Integer courseId, Integer studentId) {
        courseMapper.deleteStudentCourse(courseId, studentId);
    }

    /**
     * 指数课程
     *
     * @param typeList 类型列表
     * @return {@link List}<{@link Course}>
     */
    @Override
    public List<Course> indexCourse(List<String> typeList) {
        //不为空是新用户，否则是老用户
        List<Course> courseList = new ArrayList<>();
        if (CollUtil.isNotEmpty(typeList)) {
            courseList = courseMapper.indexCourse(typeList);
        } else {
            // TODO: 2023/3/1 0001 推荐数据
        }
        return courseList;
    }

    /**
     * 课程类型列表
     *
     * @return {@link List}<{@link Course}>
     */
    @Override
    public List<Course> courseTypeList() {
        return courseMapper.selectList(new LambdaQueryWrapper<Course>().select(Course::getType).ne(Course::getType, "").groupBy(Course::getType));
    }

    /**
     * 我课程列表
     *
     * @param page   页面
     * @param course 课程
     * @return {@link Page}<{@link Course}>
     */
    @Override
    public Page<Course> myCourseList(Page<Course> page, Course course) {
        //
        Page<Course> coursePage;
        //获取用户id
        Integer userId = Objects.requireNonNull(TokenUtils.getCurrentUser()).getId();
        //我的课程数据
        List<StudentCourse> selectList = studentCourseMapper.selectList(new LambdaQueryWrapper<StudentCourse>().eq(StudentCourse::getStudentId, userId));
        if (CollUtil.isNotEmpty(selectList)) {
            List<Integer> collect = selectList.stream().map(StudentCourse::getCourseId).collect(Collectors.toList());
            //老师，课程，学校，类别，课程ids
            LambdaQueryWrapper<Course> lqw = Wrappers.lambdaQuery();
            lqw.in(Course::getId, collect);
            lqw.like(StringUtils.isNotBlank(course.getInstructor()), Course::getInstructor, course.getInstructor());
            lqw.like(StringUtils.isNotBlank(course.getName()), Course::getName, course.getName());
            lqw.like(StringUtils.isNotBlank(course.getSchool()), Course::getSchool, course.getSchool());
            lqw.like(StringUtils.isNotBlank(course.getType()), Course::getType, course.getType());
            coursePage = courseMapper.selectPage(page, lqw);
        } else {
            coursePage = new Page<>();
        }
        return coursePage;
    }

}
