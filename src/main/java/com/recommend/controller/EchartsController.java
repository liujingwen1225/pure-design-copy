package com.recommend.controller;

import cn.hutool.core.util.ObjectUtil;
import com.recommend.common.Result;
import com.recommend.controller.dto.ChartDataVo;
import com.recommend.entity.Course;
import com.recommend.service.ICourseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Resource
    private ICourseService courseService;

    @ApiOperation("图表")
    @GetMapping("/chartData")
    public Result chartData(Course course) {
        ChartDataVo chartDataVo = courseService.chartData(course);
        return Result.success(chartDataVo);
    }

    @ApiOperation("图表")
    @GetMapping("/chartYearData")
    public Result chartYearData(Integer course) {
        if (ObjectUtil.isNull(course)){
            course = 1;
        }
        ChartDataVo chartDataVo = courseService.chartData2(course);
        return Result.success(chartDataVo);
    }

}
