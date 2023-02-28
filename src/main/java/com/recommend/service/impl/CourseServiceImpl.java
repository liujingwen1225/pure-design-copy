package com.recommend.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

    @Override
    public Page<Course> findPage(Page<Course> page, Course course) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        //老师，课程，学校，类别
        if (StringUtils.isNotBlank(course.getInstructor())) {
            queryWrapper.lambda().like(Course::getInstructor, course.getInstructor());
        }
        if (StringUtils.isNotBlank(course.getName())) {
            queryWrapper.lambda().like(Course::getName, course.getName());
        }
        if (StringUtils.isNotBlank(course.getSchool())) {
            queryWrapper.lambda().like(Course::getSchool, course.getSchool());
        }
        if (StringUtils.isNotBlank(course.getSchool())) {
            queryWrapper.lambda().like(Course::getSchool, course.getSchool());
        }
        if (StringUtils.isNotBlank(course.getType())) {
            queryWrapper.lambda().like(Course::getSchool, course.getType());
        }
        return courseMapper.selectPage(page, queryWrapper);
    }

    @Transactional
    @Override
    public void setStudentCourse(Integer courseId, Integer studentId) {
        courseMapper.deleteStudentCourse(courseId, studentId);
        courseMapper.setStudentCourse(courseId, studentId);
    }

    @Override
    public Page<Course> myCourseList(Page<Course> page, Course course) {
        //
        Page<Course> coursePage = new Page<>();
        //获取用户id
        Integer userId = Objects.requireNonNull(TokenUtils.getCurrentUser()).getId();
        //我的课程数据
        List<StudentCourse> selectList = studentCourseMapper.selectList(new LambdaQueryWrapper<StudentCourse>().eq(StudentCourse::getStudentId, userId));
        if (CollUtil.isNotEmpty(selectList)) {
            List<Integer> collect = selectList.stream().map(StudentCourse::getCourseId).collect(Collectors.toList());
            QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
            //我的课程
            queryWrapper.lambda().in(Course::getId, collect);
            //老师，课程，学校，类别
            if (StringUtils.isNotBlank(course.getInstructor())) {
                queryWrapper.lambda().like(Course::getInstructor, course.getInstructor());
            }
            if (StringUtils.isNotBlank(course.getName())) {
                queryWrapper.lambda().like(Course::getName, course.getName());
            }
            if (StringUtils.isNotBlank(course.getSchool())) {
                queryWrapper.lambda().like(Course::getSchool, course.getSchool());
            }
            if (StringUtils.isNotBlank(course.getSchool())) {
                queryWrapper.lambda().like(Course::getSchool, course.getSchool());
            }
            if (StringUtils.isNotBlank(course.getType())) {
                queryWrapper.lambda().like(Course::getSchool, course.getType());
            }
            courseMapper.selectPage(page, queryWrapper);
        } else {
            coursePage = new Page<>();
        }
        return coursePage;
    }


}
