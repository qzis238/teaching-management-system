-- =============================================
-- 教学数字平台管理系统 — 数据库初始化脚本
-- =============================================

CREATE DATABASE IF NOT EXISTS `mumululu`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE `mumululu`;

-- =============================================
-- 1. 用户表 sys_user
-- =============================================
CREATE TABLE IF NOT EXISTS `sys_user` (
  `id`            BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
  `username`      VARCHAR(50)  NOT NULL                 COMMENT '用户名，用于登录',
  `password`      VARCHAR(255) NOT NULL                 COMMENT '密码，BCrypt加密存储',
  `real_name`     VARCHAR(50)  NOT NULL                 COMMENT '真实姓名',
  `role`          TINYINT      NOT NULL DEFAULT 1       COMMENT '角色：1=管理员，2=教师',
  `phone`         VARCHAR(20)  DEFAULT NULL             COMMENT '手机号',
  `status`        TINYINT      NOT NULL DEFAULT 1       COMMENT '状态：1=正常，0=锁定',
  `locked_until`  DATETIME     DEFAULT NULL             COMMENT '锁定到期时间',
  `login_fail`    INT          NOT NULL DEFAULT 0       COMMENT '连续登录失败次数',
  `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`    TINYINT      NOT NULL DEFAULT 0       COMMENT '逻辑删除：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_username` (`username`),
  INDEX `idx_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- =============================================
-- 2. 班级表 class
-- =============================================
CREATE TABLE IF NOT EXISTS `class` (
  `id`            BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
  `class_name`    VARCHAR(100) NOT NULL                 COMMENT '班级名称',
  `teacher_id`    BIGINT       NOT NULL                 COMMENT '负责老师ID，关联 sys_user.id',
  `description`   VARCHAR(500) DEFAULT NULL             COMMENT '班级描述',
  `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`    TINYINT      NOT NULL DEFAULT 0       COMMENT '逻辑删除：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_class_name` (`class_name`),
  INDEX `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

-- =============================================
-- 3. 学生表 student
-- =============================================
CREATE TABLE IF NOT EXISTS `student` (
  `id`            BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
  `student_no`    VARCHAR(50)  NOT NULL                 COMMENT '学号，唯一',
  `name`          VARCHAR(50)  NOT NULL                 COMMENT '学生姓名',
  `gender`        TINYINT      NOT NULL                 COMMENT '性别：1=男，2=女',
  `class_id`      BIGINT       NOT NULL                 COMMENT '所属班级ID，关联 class.id',
  `phone`         VARCHAR(20)  DEFAULT NULL             COMMENT '手机号',
  `address`       VARCHAR(200) DEFAULT NULL             COMMENT '家庭住址',
  `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`    TINYINT      NOT NULL DEFAULT 0       COMMENT '逻辑删除：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_student_no` (`student_no`),
  INDEX `idx_class_id` (`class_id`),
  INDEX `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表';

-- =============================================
-- 4. 课程表 course
-- =============================================
CREATE TABLE IF NOT EXISTS `course` (
  `id`            BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
  `course_name`   VARCHAR(100) NOT NULL                 COMMENT '课程名称',
  `description`   VARCHAR(500) DEFAULT NULL             COMMENT '课程描述',
  `teacher_id`    BIGINT       DEFAULT NULL             COMMENT '默认授课老师ID，关联 sys_user.id',
  `max_score`     INT          NOT NULL DEFAULT 100     COMMENT '满分值，用于成绩范围校验',
  `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`    TINYINT      NOT NULL DEFAULT 0       COMMENT '逻辑删除：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  INDEX `idx_course_name` (`course_name`),
  INDEX `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- =============================================
-- 5. 排课表 schedule
-- =============================================
CREATE TABLE IF NOT EXISTS `schedule` (
  `id`            BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
  `class_id`      BIGINT       NOT NULL                 COMMENT '班级ID，关联 class.id',
  `course_id`     BIGINT       NOT NULL                 COMMENT '课程ID，关联 course.id',
  `teacher_id`    BIGINT       NOT NULL                 COMMENT '授课老师ID，关联 sys_user.id',
  `classroom`     VARCHAR(100) DEFAULT NULL             COMMENT '上课教室',
  `day_of_week`   TINYINT      NOT NULL                 COMMENT '星期几：1=周一，2=周二，3=周三，4=周四，5=周五',
  `period`        TINYINT      NOT NULL                 COMMENT '第几节课：1~8',
  `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`    TINYINT      NOT NULL DEFAULT 0       COMMENT '逻辑删除：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_class_day_period` (`class_id`, `day_of_week`, `period`),
  INDEX `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='排课表';

-- =============================================
-- 6. 考勤表 attendance
-- =============================================
CREATE TABLE IF NOT EXISTS `attendance` (
  `id`               BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
  `student_id`       BIGINT       NOT NULL                 COMMENT '学生ID，关联 student.id',
  `class_id`         BIGINT       NOT NULL                 COMMENT '班级ID，关联 class.id（冗余，保留学生当时所在班级）',
  `attendance_date`  DATE         NOT NULL                 COMMENT '考勤日期',
  `status`           TINYINT      NOT NULL                 COMMENT '出勤状态：1=出勤，2=迟到，3=请假，4=缺勤',
  `create_time`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`       TINYINT      NOT NULL DEFAULT 0       COMMENT '逻辑删除：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_student_date` (`student_id`, `attendance_date`),
  INDEX `idx_class_date` (`class_id`, `attendance_date`),
  INDEX `idx_attendance_date` (`attendance_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考勤表';

-- =============================================
-- 7. 成绩表 score
-- =============================================
CREATE TABLE IF NOT EXISTS `score` (
  `id`            BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
  `student_id`    BIGINT       NOT NULL                 COMMENT '学生ID，关联 student.id',
  `course_id`     BIGINT       NOT NULL                 COMMENT '课程ID，关联 course.id',
  `exam_type`     VARCHAR(50)  NOT NULL                 COMMENT '考试类型，如：期中考试、期末考试、月考',
  `score`         DECIMAL(5,1) NOT NULL                 COMMENT '考试分数',
  `exam_date`     DATE         NOT NULL                 COMMENT '考试日期',
  `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`    TINYINT      NOT NULL DEFAULT 0       COMMENT '逻辑删除：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_student_course_exam` (`student_id`, `course_id`, `exam_type`),
  INDEX `idx_course_id` (`course_id`),
  INDEX `idx_exam_date` (`exam_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成绩表';

-- =============================================
-- 8. 作业表 homework
-- =============================================
CREATE TABLE IF NOT EXISTS `homework` (
  `id`            BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
  `title`         VARCHAR(200) NOT NULL                 COMMENT '作业标题',
  `description`   TEXT         DEFAULT NULL             COMMENT '作业描述/要求',
  `class_id`      BIGINT       NOT NULL                 COMMENT '发布班级ID，关联 class.id',
  `course_id`     BIGINT       NOT NULL                 COMMENT '所属课程ID，关联 course.id',
  `deadline`      DATETIME     NOT NULL                 COMMENT '截止提交时间',
  `max_score`     INT          NOT NULL DEFAULT 100     COMMENT '作业满分值',
  `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`    TINYINT      NOT NULL DEFAULT 0       COMMENT '逻辑删除：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  INDEX `idx_class_id` (`class_id`),
  INDEX `idx_course_id` (`course_id`),
  INDEX `idx_deadline` (`deadline`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业表';

-- =============================================
-- 9. 作业提交表 homework_submission
-- =============================================
CREATE TABLE IF NOT EXISTS `homework_submission` (
  `id`             BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
  `homework_id`    BIGINT       NOT NULL                 COMMENT '作业ID，关联 homework.id',
  `student_id`     BIGINT       NOT NULL                 COMMENT '学生ID，关联 student.id',
  `submit_status`  TINYINT      NOT NULL DEFAULT 0       COMMENT '提交状态：0=未提交，1=已提交，2=迟交',
  `submit_time`    DATETIME     DEFAULT NULL             COMMENT '实际提交时间',
  `score`          DECIMAL(5,1) DEFAULT NULL             COMMENT '老师批改得分',
  `comment`        VARCHAR(500) DEFAULT NULL             COMMENT '老师评语',
  `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`     TINYINT      NOT NULL DEFAULT 0       COMMENT '逻辑删除：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_homework_student` (`homework_id`, `student_id`),
  INDEX `idx_student_id` (`student_id`),
  INDEX `idx_submit_status` (`submit_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业提交表';

-- =============================================
-- 初始数据：管理员账号 (admin / admin123)
-- =============================================
INSERT IGNORE INTO `sys_user` (`username`, `password`, `real_name`, `role`)
VALUES ('admin', '$2a$10$m0mPVm5lCEhOanNlqtaNBeduKTx5kHaKH0vvFHSgMq8xsHrTMVABu', '系统管理员', 1);

-- =============================================
-- 测试数据
-- =============================================

-- 教师用户（role=2，密码均为 admin123）
INSERT IGNORE INTO `sys_user` (`id`, `username`, `password`, `real_name`, `role`)
VALUES
(2, 'teacher_li',  '$2a$10$m0mPVm5lCEhOanNlqtaNBeduKTx5kHaKH0vvFHSgMq8xsHrTMVABu', '李老师', 2),
(3, 'teacher_wang', '$2a$10$m0mPVm5lCEhOanNlqtaNBeduKTx5kHaKH0vvFHSgMq8xsHrTMVABu', '王老师', 2),
(4, 'teacher_zhao', '$2a$10$m0mPVm5lCEhOanNlqtaNBeduKTx5kHaKH0vvFHSgMq8xsHrTMVABu', '赵老师', 2);

-- 班级
INSERT IGNORE INTO `class` (`id`, `class_name`, `teacher_id`, `description`) VALUES
(1, '2024级软件工程1班', 2, '软件工程专业核心班级'),
(2, '2024级软件工程2班', 3, '软件工程专业核心班级'),
(3, '2024级计算机科学1班', 4, '计算机科学专业方向');

-- 学生
INSERT IGNORE INTO `student` (`id`, `student_no`, `name`, `gender`, `class_id`, `phone`, `address`) VALUES
(1,  '2024001', '张三',   1, 1, '13800001111', '北京市海淀区'),
(2,  '2024002', '李四',   1, 1, '13800002222', '北京市朝阳区'),
(3,  '2024003', '王五',   2, 1, '13800003333', '上海市浦东新区'),
(4,  '2024004', '赵六',   2, 1, '13800004444', '广州市天河区'),
(5,  '2024005', '钱七',   1, 1, '13800005555', '深圳市南山区'),
(6,  '2024006', '孙八',   1, 2, '13800006666', '杭州市西湖区'),
(7,  '2024007', '周九',   2, 2, '13800007777', '成都市武侯区'),
(8,  '2024008', '吴十',   1, 2, '13800008888', '武汉市洪山区'),
(9,  '2024009', '郑十一', 1, 3, '13800009999', '南京市鼓楼区'),
(10, '2024010', '冯十二', 2, 3, '13800010000', '西安市雁塔区'),
(11, '2024011', '陈十三', 1, 3, '13800011111', '重庆市渝中区'),
(12, '2024012', '褚十四', 2, 3, '13800012222', '长沙市岳麓区');

-- 课程
INSERT IGNORE INTO `course` (`id`, `course_name`, `description`, `teacher_id`, `max_score`) VALUES
(1, '高等数学',   '大一公共必修课', 2, 100),
(2, '大学英语',   '大一公共必修课', 3, 100),
(3, '数据结构',   '专业核心课',     4, 100),
(4, '计算机基础', '专业选修课',     2, 150);

-- 排课（班级1的课表）
INSERT IGNORE INTO `schedule` (`id`, `class_id`, `course_id`, `teacher_id`, `classroom`, `day_of_week`, `period`) VALUES
(1, 1, 1, 2, 'A201', 1, 1),
(2, 1, 1, 2, 'A201', 1, 2),
(3, 1, 2, 3, 'B102', 2, 1),
(4, 1, 2, 3, 'B102', 2, 2),
(5, 1, 3, 4, 'C305', 3, 3),
(6, 1, 3, 4, 'C305', 3, 4),
(7, 1, 4, 2, 'D408', 4, 1),
(8, 1, 4, 2, 'D408', 4, 2),
(9, 1, 1, 2, 'A201', 5, 5),
(10, 1, 1, 2, 'A201', 5, 6);

-- 排课（班级2的课表）
INSERT IGNORE INTO `schedule` (`id`, `class_id`, `course_id`, `teacher_id`, `classroom`, `day_of_week`, `period`) VALUES
(11, 2, 3, 4, 'C306', 1, 3),
(12, 2, 3, 4, 'C306', 1, 4),
(13, 2, 1, 2, 'A203', 2, 1),
(14, 2, 1, 2, 'A203', 2, 2);

-- 考勤记录（近3天）
INSERT IGNORE INTO `attendance` (`id`, `student_id`, `class_id`, `attendance_date`, `status`) VALUES
(1,  1,  1, '2026-05-23', 1),
(2,  2,  1, '2026-05-23', 1),
(3,  3,  1, '2026-05-23', 2),
(4,  4,  1, '2026-05-23', 1),
(5,  5,  1, '2026-05-23', 4),
(6,  6,  2, '2026-05-23', 1),
(7,  7,  2, '2026-05-23', 1),
(8,  8,  2, '2026-05-23', 3),
(9,  1,  1, '2026-05-24', 1),
(10, 2,  1, '2026-05-24', 1),
(11, 3,  1, '2026-05-24', 1),
(12, 4,  1, '2026-05-24', 2),
(13, 5,  1, '2026-05-24', 1),
(14, 6,  2, '2026-05-24', 1),
(15, 7,  2, '2026-05-24', 4),
(16, 8,  2, '2026-05-24', 1),
(17, 1,  1, '2026-05-25', 1),
(18, 2,  1, '2026-05-25', 1),
(19, 3,  1, '2026-05-25', 1),
(20, 4,  1, '2026-05-25', 1),
(21, 5,  1, '2026-05-25', 3),
(22, 6,  2, '2026-05-25', 2),
(23, 7,  2, '2026-05-25', 1),
(24, 8,  2, '2026-05-25', 1);

-- 成绩
INSERT IGNORE INTO `score` (`id`, `student_id`, `course_id`, `exam_type`, `score`, `exam_date`) VALUES
(1, 1, 1, '期中考试', 85.5, '2026-05-10'),
(2, 2, 1, '期中考试', 92.0, '2026-05-10'),
(3, 3, 1, '期中考试', 78.0, '2026-05-10'),
(4, 4, 1, '期中考试', 88.0, '2026-05-10'),
(5, 5, 1, '期中考试', 73.5, '2026-05-10');

-- 作业
INSERT IGNORE INTO `homework` (`id`, `title`, `description`, `class_id`, `course_id`, `deadline`, `max_score`) VALUES
(1, '高数第一章练习题', '完成课本第20-25页习题1-10', 1, 1, '2026-05-28 23:59:00', 100),
(2, '英语作文练习',   '写一篇200词英语短文',          2, 2, '2026-05-30 18:00:00', 100);

-- 作业提交
INSERT IGNORE INTO `homework_submission` (`id`, `homework_id`, `student_id`, `submit_status`, `submit_time`, `score`, `comment`) VALUES
(1, 1, 1, 1, '2026-05-16 14:30:00', 85.0, '完成得不错'),
(2, 1, 2, 1, '2026-05-17 09:15:00', NULL, NULL),
(3, 1, 3, 1, '2026-05-18 22:00:00', NULL, NULL);
