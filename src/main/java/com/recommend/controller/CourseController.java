package com.recommend.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.recommend.common.Result;
import com.recommend.controller.dto.PageQuery;
import com.recommend.entity.Course;
import com.recommend.entity.StudentCourse;
import com.recommend.service.ICourseService;
import com.recommend.service.IStudentCourseService;
import com.recommend.service.IUserService;
import com.recommend.utils.TokenUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    private ICourseService courseService;

    @Resource
    private IUserService userService;
    @Resource
    private IStudentCourseService studentCourseService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Course course) {
        courseService.saveOrUpdate(course);
        return Result.success();
    }

    @ApiOperation("点击选课")
    @PostMapping("/studentCourse/{courseId}/{studentId}")
    public Result studentCourse(@PathVariable Integer courseId, @PathVariable Integer studentId) {
        courseService.setStudentCourse(courseId, studentId);
        return Result.success();
    }

    @ApiOperation("取消选课")
    @PostMapping("/cancelCourseSelection/{courseId}/{studentId}")
    public Result cancelCourseSelection(@PathVariable Integer courseId, @PathVariable Integer studentId) {
        courseService.cancelCourseSelection(courseId, studentId);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        courseService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        courseService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(courseService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(courseService.getById(id));
    }

    @ApiOperation("全部课程列表")
    @GetMapping("/page")
    public Result findPage(Course course, PageQuery pageQuery) {
        Page<Course> page = courseService.findPage(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()), course);
        //选课状态：【1=未选，2=已选】
        List<Course> records = page.getRecords();
        for (Course record : records) {
            //获取课程id，用户id
            Integer courseId = record.getId();
            Integer userId = Objects.requireNonNull(TokenUtils.getCurrentUser()).getId();
            StudentCourse studentCourseServiceOne = studentCourseService.getOne(new LambdaQueryWrapper<StudentCourse>()
                    .eq(StudentCourse::getStudentId, userId)
                    .eq(StudentCourse::getCourseId, courseId)
                    .last("limit 1")
            );
            if (ObjectUtil.isNotNull(studentCourseServiceOne)) {
                record.setCourseStatus(2);
            } else {
                record.setCourseStatus(1);
            }
        }
        return Result.success(page);
    }

    @ApiOperation("我的课程列表")
    @GetMapping("/myCourseList")
    public Result myCourseList(Course course, PageQuery pageQuery) {
        Page<Course> page = courseService.myCourseList(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()), course);
        return Result.success(page);
    }

    @ApiOperation("首页课程推荐")
    @GetMapping("/indexCourse")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "typeList", value = "类型数组（新用户必传，老用户传空）", dataType = "List"),
    })
    public Result indexCourse(@RequestParam(required = false) List<String> typeList) {
        List<Course> courseList = courseService.indexCourse(typeList);
        return Result.success(courseList);
    }

    @ApiOperation("课程类型列表")
    @GetMapping("/courseTypeList")
    public Result courseTypeList() {
        List<Course> courseList = courseService.courseTypeList();
        return Result.success(courseList);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Course course) {
        courseService.saveOrUpdate(course);
        return Result.success();
    }

}

