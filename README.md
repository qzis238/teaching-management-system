# 教学数字平台管理系统

基于 **Spring Boot + Vue 3** 的前后端分离教学管理平台，覆盖学生、班级、课程、排课、考勤、成绩、作业等核心教务场景。

## 技术栈

| 层 | 技术 |
|------|------|
| 后端 | Java 8 · Spring Boot 2.7 · MyBatis · MySQL 8 · JWT · BCrypt |
| 前端 | Vue 3 · Element Plus · ECharts（CDN 引入） |
| 构建 | Maven |

## 功能模块

- **登录鉴权**：JWT + BCrypt，连续 5 次登录失败自动锁定 15 分钟
- **仪表盘**：学生/班级/出勤/作业 4 项统计卡片 + ECharts 柱状图、折线图
- **学生管理**：增删改查、姓名模糊搜索、班级筛选、分页、学号唯一校验
- **班级管理**：增删改查、删除前校验学生与排课关联
- **课程管理**：增删改查、满分值配置
- **排课管理**：5×8 周课表，班级 + 教师双重时间冲突校验，课表一键复制
- **考勤打卡**：批量标记（出勤/迟到/请假/缺勤）、历史记录查询
- **成绩管理**：批量录入、分数范围校验、按学生/班级查询
- **作业管理**：发布 → 提交 → 批改，迟交自动判定，级联删除

## 项目结构

```
mumululu/
├── backend/     # Spring Boot 后端
│   └── src/main/
│       ├── java/com/mumululu/   # controller / service / mapper / entity / common
│       └── resources/           # application.yml / mapper XML / db/init.sql
├── front/       # Vue 3 前端（login.html / index.html）
└── doc/         # 需求、数据库、接口文档
```

## 快速开始

### 1. 初始化数据库

执行 `backend/src/main/resources/db/init.sql` 建库建表（含测试数据）。

### 2. 配置数据库连接

修改 `backend/src/main/resources/application.yml` 中的数据库账号密码。

### 3. 启动后端

```bash
cd backend
mvn spring-boot:run
```

### 4. 打开前端

用浏览器打开 `front/login.html`，使用 `admin / admin123` 登录。

## 界面预览

（截图待补充）
