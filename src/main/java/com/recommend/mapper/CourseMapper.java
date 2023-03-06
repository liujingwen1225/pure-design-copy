package com.recommend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.recommend.entity.Course;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 */
public interface CourseMapper extends BaseMapper<Course> {

    Page<Course> findPage(Page<Course> page, @Param("name") String name);

    void deleteStudentCourse(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);

    void setStudentCourse(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);

    List<Course> indexCourse(@Param("typeList") List<String> typeList, @Param("courseIds") List<Integer> courseIds);

    List<Course> topCourseList(List<String> typeList);

    Long userNumber();

    Long courseNumber();

    Long teacherNumber();

    Long schoolNumber();

    List<Course> topSchoolList(List<String> typeList);

    List<Course> topTeacherList(List<String> typeList);
}
