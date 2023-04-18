package com.recommend.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.recommend.controller.dto.ChartDataVo;
import com.recommend.entity.Course;
import com.recommend.entity.CourseRecommend;
import com.recommend.entity.StudentCourse;
import com.recommend.entity.User;
import com.recommend.entity.analysis.*;
import com.recommend.mapper.CourseMapper;
import com.recommend.mapper.CourseRecommendMapper;
import com.recommend.mapper.StudentCourseMapper;
import com.recommend.mapper.UserMapper;
import com.recommend.service.ICourseService;
import com.recommend.utils.TokenUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
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
    private UserMapper userMapper;
    @Resource
    private StudentCourseMapper studentCourseMapper;
    @Resource
    private CourseRecommendMapper recommendMapper;

    /**
     * 找到页面
     *
     * @param page   页面
     * @param course 课程
     * @return {@link Page}<{@link Course}>
     */
    @Override
    public Page<Course> findPage(Page<Course> page, Course course) {
        //条件查询
        LambdaQueryWrapper<Course> lqw = buildQueryWrapper(course);
        return courseMapper.selectPage(page, lqw);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
     * 首页课程推荐列表
     *
     * @return {@link List}<{@link Course}>
     */
    @Override
    public List<Course> indexCourse() {
        //不为空是新用户，否则是老用户
        Integer userId = Objects.requireNonNull(TokenUtils.getCurrentUser()).getId();
        List<Course> courseList = new ArrayList<>();
        //过滤已选课的数据
        List<Integer> courseIds = null;
        List<StudentCourse> studentCourseList = studentCourseMapper.selectList(new LambdaQueryWrapper<StudentCourse>().eq(StudentCourse::getStudentId, userId));
        if (CollUtil.isNotEmpty(studentCourseList)) {
            courseIds = studentCourseList.stream().map(StudentCourse::getCourseId).collect(Collectors.toList());
        }
        //查询推荐表是否有他数据
        CourseRecommend courseRecommend = recommendMapper.selectOne(new LambdaQueryWrapper<CourseRecommend>().eq(CourseRecommend::getUserId, userId).last("limit 1"));
        if (ObjectUtil.isNull(courseRecommend)) {
            //获取课程类型
            User user = userMapper.selectById(userId);
            String courseType = user.getCourseType();
            if (StrUtil.isNotBlank(courseType)) {
                //string转list
                String cleanBlank = StrUtil.cleanBlank(courseType);
                List<String> typeList = Arrays.asList(cleanBlank.split(","));
                courseList = courseMapper.indexCourse(typeList, courseIds);
            }
        } else {
            List<CourseRecommend> recommendList = recommendMapper.selectList(new LambdaQueryWrapper<CourseRecommend>().eq(CourseRecommend::getUserId, userId));
            if (CollUtil.isNotEmpty(recommendList)) {
                List<Integer> collect = recommendList.stream().map(CourseRecommend::getCourseId).collect(Collectors.toList());
                courseList = courseMapper.selectList(new LambdaQueryWrapper<Course>().in(Course::getId, collect).notIn(CollUtil.isNotEmpty(courseIds), Course::getId, courseIds));
            }
        }
        //选课状态：【1=未选，2=已选】
        if (CollUtil.isNotEmpty(courseList)) {
            for (Course course : courseList) {
                Integer courseStatus = courseStatus(userId, course.getId());
                course.setCourseStatus(courseStatus);
            }
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
     * 用户类型
     *
     * @return {@link Integer}
     */
    @Override
    public Integer userType() {
        //获取角色标识符，用户id
        String role = Objects.requireNonNull(TokenUtils.getCurrentUser()).getRole();
        Integer userId = Objects.requireNonNull(TokenUtils.getCurrentUser()).getId();
        //用户类型：【1=新用户，2=老用户】
        int userType = 2;
        if (!StrUtil.equals(role, "ROLE_ADMIN")) {
            User user = userMapper.selectById(userId);
            String courseType = user.getCourseType();
            if (StrUtil.isBlank(courseType)) {
                userType = 1;
            }
        }
        return userType;
    }

    /**
     * 选课状态：【1=未选，2=已选】
     *
     * @return {@link Integer}
     */
    @Override
    public Integer courseStatus(Integer userId, Integer courseId) {
        //选课状态：【1=未选，2=已选】
        int courseStatus = 1;
        StudentCourse studentCourseServiceOne = studentCourseMapper.selectOne(new LambdaQueryWrapper<StudentCourse>().eq(StudentCourse::getStudentId, userId).eq(StudentCourse::getCourseId, courseId).last("limit 1"));
        if (ObjectUtil.isNotNull(studentCourseServiceOne)) {
            courseStatus = 2;
        }
        return courseStatus;
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
        Page<Course> coursePage = new Page<>();
        //获取用户id
        Integer userId = Objects.requireNonNull(TokenUtils.getCurrentUser()).getId();
        //我的课程数据
        List<StudentCourse> selectList = studentCourseMapper.selectList(new LambdaQueryWrapper<StudentCourse>().eq(StudentCourse::getStudentId, userId).orderByDesc(StudentCourse::getCreateTime));
        if (CollUtil.isNotEmpty(selectList)) {
            List<Integer> collect = selectList.stream().map(StudentCourse::getCourseId).collect(Collectors.toList());
            List<Integer> collectid = new ArrayList<>();
            long max = (page.getCurrent() * page.getSize() - page.getSize()) + 12;
            if (max > collect.size()) {
                max = (page.getCurrent() * page.getSize() - page.getSize()) + collect.size() % 12;
            }
            for (int i = (int) (page.getCurrent() * page.getSize() - page.getSize()); i < max; i++) {
                collectid.add(collect.get(i));
            }
            course.setCourseIds(collectid);
            //条件查询
            LambdaQueryWrapper<Course> lqw = buildQueryWrapper(course);
            long current = page.getCurrent();
            page.setCurrent(1);
            coursePage = courseMapper.selectPage(page, lqw);
            setListOrder(collect, coursePage.getRecords());
            //
            coursePage.setCurrent(current);
            List<Course> records = coursePage.getRecords();
            for (Course record : records) {
                //
                Integer id = record.getId();
                //我的评分
                String rating = "";
                Integer myRatingType = 1;
                StudentCourse studentCourse = studentCourseMapper.selectOne(new LambdaQueryWrapper<StudentCourse>().eq(StudentCourse::getStudentId, userId).eq(StudentCourse::getCourseId, id).last("limit 1"));
                if (ObjectUtil.isNotNull(studentCourse)) {
                    String rating1 = studentCourse.getRating();
                    if (StrUtil.isNotBlank(rating1)) {
                        rating = rating1;
                        myRatingType = 2;
                    }
                }
                record.setRating(rating);
                record.setMyRatingType(myRatingType);
            }
        }
        coursePage.setTotal(selectList.size());
        return coursePage;
    }

    public static void setListOrder(List<Integer> orderRegulation, List<Course> targetList) {
        // 按照 list 里的 name 来排序 targetList
        Collections.sort(targetList, ((o1, o2) -> {
            int io1 = orderRegulation.indexOf(o1.getId());
            int io2 = orderRegulation.indexOf(o2.getId());

            if (io1 != -1) {
                io1 = targetList.size() - io1;
            }
            if (io2 != -1) {
                io2 = targetList.size() - io2;
            }
            return io2 - io1;
        }));
    }

    private LambdaQueryWrapper<Course> buildQueryWrapper(Course course) {
        LambdaQueryWrapper<Course> lqw = Wrappers.lambdaQuery();
        lqw.in(CollUtil.isNotEmpty(course.getCourseIds()), Course::getId, course.getCourseIds());
        lqw.like(StrUtil.isNotBlank(course.getInstructor()), Course::getInstructor, course.getInstructor());
        lqw.like(StrUtil.isNotBlank(course.getName()), Course::getName, course.getName());
        lqw.like(StrUtil.isNotBlank(course.getSchool()), Course::getSchool, course.getSchool());
        lqw.like(StrUtil.isNotBlank(course.getType()), Course::getType, course.getType());
        lqw.last("order by participants_number * 1 desc, grading * 1 desc");
        return lqw;
    }

    /**
     * 图表数据
     *
     * @return {@link ChartDataVo}
     */
    @Override
    public ChartDataVo chartData(Course course) {
        ChartDataVo chartDataVo = new ChartDataVo();
        //今年选课用户
        TotalAnalysis totalAnalysis = courseMapper.totalAnalysis();
        chartDataVo.setUserNumber(totalAnalysis.getStudentCnt());
        //课程数量
        chartDataVo.setCourseNumber(totalAnalysis.getNameCnt());
        //授课教师数量
        chartDataVo.setTeacherNumber(totalAnalysis.getInstructorCnt());
        //授课学校数量
        chartDataVo.setSchoolNumber(totalAnalysis.getSchoolCnt());
        List<TotalAnalysis2> totalAnalysis2s = courseMapper.totalAnalysis2();

        for (TotalAnalysis2 totalAnalysis2 : totalAnalysis2s) {
            switch (totalAnalysis2.getLabels()) {
                case "课程进行中":
                    chartDataVo.setStartCourse(totalAnalysis2.getCnt());
                    break;
                case "课程已结束":
                    chartDataVo.setEndCourse(totalAnalysis2.getCnt());
                    break;
                case "课程即将开始":
                    chartDataVo.setNotStartCourse(totalAnalysis2.getCnt());
                    break;
                case "国家精品":
                    chartDataVo.setBoutiqueCourses(totalAnalysis2.getCnt());
                    break;
                case "大学先修课":
                    chartDataVo.setPurerCourses(totalAnalysis2.getCnt());
                    break;
                default:
                    break;
            }
        }
        //
        String courseType = course.getTypeList();
        List<String> typeList = null;
        if (StrUtil.isNotBlank(courseType)) {
            //string转list
            String cleanBlank = StrUtil.cleanBlank(courseType);
            typeList = Arrays.asList(cleanBlank.split(","));
        }

        //热门课程
        List<HotAnalysis> hotAnalyses = courseMapper.topCourseList(typeList);
        List<String> popularCourseNameList = new ArrayList<>();
        List<Long> popularCourseNumberList = new ArrayList<>();
        //热门老师
        List<String> popularTeacherNameList = new ArrayList<>();
        List<Long> popularTeacherNumberList = new ArrayList<>();
        //热门学校
        List<String> popularSchoolNameList = new ArrayList<>();
        List<Long> popularSchoolNumberList = new ArrayList<>();
        if (CollUtil.isNotEmpty(hotAnalyses)) {
            popularCourseNameList = hotAnalyses.stream().map(HotAnalysis::getName).collect(Collectors.toList());
            popularCourseNumberList = hotAnalyses.stream().map(HotAnalysis::getNameNum).collect(Collectors.toList());
            popularSchoolNameList = hotAnalyses.stream().map(HotAnalysis::getSchool).collect(Collectors.toList());
            popularSchoolNumberList = hotAnalyses.stream().map(HotAnalysis::getSchoolNum).collect(Collectors.toList());
            popularTeacherNameList = hotAnalyses.stream().map(HotAnalysis::getInstructor).collect(Collectors.toList());
            popularTeacherNumberList = hotAnalyses.stream().map(HotAnalysis::getInstructorNum).collect(Collectors.toList());
        }
        List<HotAnalysis> hotAnalyses1 = courseMapper.topNameList();
        List<Map<String, Object>> words = new ArrayList<>();
        for (HotAnalysis hotAnalysis : hotAnalyses1) {
            Map<String, Object> word = new HashMap<>();
            word.put("name", hotAnalysis.getName());
            word.put("value", hotAnalysis.getNameNum());
            words.add(word);
        }
        chartDataVo.setWordCould(words);
        if (StrUtil.isBlank(courseType)) {
            List<HotAnalysis> hotAnalyses2 = courseMapper.topSchoolList(typeList);
            popularSchoolNameList = hotAnalyses2.stream().map(HotAnalysis::getSchool).collect(Collectors.toList());
            popularSchoolNumberList = hotAnalyses2.stream().map(HotAnalysis::getSchoolNum).collect(Collectors.toList());
        }
        chartDataVo.setPopularCourseNameList(popularCourseNameList);
        chartDataVo.setPopularCourseNumberList(popularCourseNumberList);
        chartDataVo.setPopularSchoolNameList(popularSchoolNameList);
        chartDataVo.setPopularSchoolNumberList(popularSchoolNumberList);
        chartDataVo.setPopularTeacherNameList(popularTeacherNameList);
        chartDataVo.setPopularTeacherNumberList(popularTeacherNumberList);
        // 热门课程发布课程占比
        List<ChartModel> chartModels = courseMapper.hotCourseZb(popularSchoolNameList);
        chartDataVo.setHotSchoolZb(chartModels);
        // 热门类型课程占比
        List<ChartModel> chartModels2 = courseMapper.hotTypeZb(typeList);
        chartDataVo.setHotTypeZb(chartModels2);

        return chartDataVo;
    }

    /**
     * 图表数据
     *
     * @return {@link ChartDataVo}
     */
    @Override
    public ChartDataVo chartData2(Integer course, Integer year, Integer month) {
        ChartDataVo chartDataVo = new ChartDataVo();
        List<YearMonthAnalysis> yearDatas = courseMapper.yearData(year, month);
        List<String> popularSchoolNameList = new ArrayList<>();
        List<Long> popularSchoolNumberList = new ArrayList<>();
        if (course == 1) {
            popularSchoolNameList = yearDatas.stream().map(YearMonthAnalysis::getInstructor).collect(Collectors.toList());
            popularSchoolNumberList = yearDatas.stream().map(YearMonthAnalysis::getInstructorPns).collect(Collectors.toList());
        } else if (course == 2) {
            popularSchoolNameList = yearDatas.stream().map(YearMonthAnalysis::getSchool).collect(Collectors.toList());
            popularSchoolNumberList = yearDatas.stream().map(YearMonthAnalysis::getSchoolPns).collect(Collectors.toList());
        } else if (course == 3) {
            popularSchoolNameList = yearDatas.stream().map(YearMonthAnalysis::getName).collect(Collectors.toList());
            popularSchoolNumberList = yearDatas.stream().map(YearMonthAnalysis::getNamePns).collect(Collectors.toList());
        }
        chartDataVo.setPopularSchoolNameList(popularSchoolNameList);
        chartDataVo.setPopularSchoolNumberList(popularSchoolNumberList);
        return chartDataVo;
    }
}
