# CLAUDE.md

## 项目概述

教学数字平台的管理系统，用于管理教学数字平台的日常运营。

## 技术选型

### 前端
- **技术栈**：HTML + CSS + JavaScript + Vue 3 + Element Plus + ECharts
- **引入方式**：全部通过 CDN 引入，禁止使用 npm / webpack / vite 等构建工具
- **代码目录**：[front/](front/)

### 后端
- **技术栈**：Java + Spring Boot + MyBatis + Maven
- **代码目录**：[backend/](backend/)

### 数据库
- **数据库**：MySQL

## 项目结构

```
mumululu/
├── doc/          # 项目文档
├── front/        # 前端页面（HTML + JS + CSS，CDN 引入）
├── backend/      # 后端代码（Spring Boot + MyBatis + Maven）
└── CLAUDE.md     # 本文件
```

## 编码规范

### Java 规范
- 严格遵循《阿里巴巴 Java 开发手册》规范
- 类名使用 UpperCamelCase，方法名和变量名使用 lowerCamelCase
- 常量使用 CONSTANT_CASE
- 数据库表名使用 lower_snake_case

### 前端规范
- 所有表单必须做输入校验（前端校验 + 后端校验双保险）
- 使用 Element Plus 的 el-form 组件进行表单校验
- HTML 文件使用语义化标签，CSS 命名使用 kebab-case

### 接口规范
- 所有接口地址统一以 `/api/` 开头
- 统一返回格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

- code 为 200 表示成功，其他值表示错误
- 后端每个接口必须做参数校验

### 数据库规范
- 每张表必须包含以下通用字段：
  - `id` — 主键，BIGINT，自增
  - `create_time` — 创建时间，DATETIME
  - `update_time` — 更新时间，DATETIME
  - `is_deleted` — 逻辑删除标记，TINYINT，0=未删除，1=已删除
- 查询操作必须过滤 `is_deleted = 0`
- **禁止字符串拼接 SQL**，必须使用 MyBatis 的参数化方式（`#{}`），防止 SQL 注入

## 开发约定

1. **一次只改一个文件** — 每次修改只聚焦一个文件，避免大范围改动
2. 新增接口前先在 [doc/](doc/) 目录补充对应的接口文档
3. 提交代码前确保前后端都能正常运行
4. 禁止提交硬编码的敏感信息（数据库密码、API 密钥等），统一使用配置文件管理
5. 开发计划中的任务完成后，验收点用 `[✓]`（对号）标注，不使用 `[x]`

## 开发流程

1. 明确需求 → 确认需要改动的文件
2. 每次改动限定一个文件
3. 改动完成后向开发者汇报变更内容
4. 开发者确认后，再进入下一个文件的修改
