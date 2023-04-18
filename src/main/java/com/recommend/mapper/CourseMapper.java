package com.recommend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.recommend.entity.Course;
import com.recommend.entity.analysis.*;
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

    List<HotAnalysis> topNameList();

    List<HotAnalysis> topCourseList(List<String> typeList);

    TotalAnalysis totalAnalysis();

    List<TotalAnalysis2> totalAnalysis2();


    List<HotAnalysis> topSchoolList(List<String> typeList);


    List<ChartModel> hotCourseZb(List<String> typeList);

    List<ChartModel> hotTypeZb(List<String> typeList);

    List<YearMonthAnalysis> yearData(Integer year, Integer month);
}
