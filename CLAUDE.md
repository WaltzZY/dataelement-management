# CLAUDE.md

**重要说明：在处理此项目时，请始终使用中文进行回复和交流。**



## 项目概述

这是一个使用 Spring Boot 2.6.7 和 Java 8 构建的数据元素管理系统。该项目采用多模块 Maven 架构来管理数据定源标准化流程。

### 模块结构

- **dataelement-management-common**: 通用工具类、枚举、注解和共享领域对象
- **dataelement-management-console**: Web 层，包含 REST 控制器和主应用程序入口点
- **dataelement-management-service**: 业务逻辑层，包含服务实现、DAO 和数据库实体

### 核心技术栈

- Spring Boot 2.6.7 with Spring Cloud
- MyBatis Plus 用于数据库操作
- Apache Dubbo 用于服务通信
- Nacos 用于服务发现和配置管理
- Flyway 用于数据库迁移
- Jasypt 用于配置加密
- EasyExcel 用于 Excel 文件处理
- Apache Kafka 用于消息传递


## 架构模式

### 包结构约定
- 控制器: `com.inspur.dsp.direct.console.controller.{业务领域}`
- 服务: `com.inspur.dsp.direct.service` 和 `com.inspur.dsp.direct.service.Impl`
- DAO: `com.inspur.dsp.direct.dao`
- 实体: `com.inspur.dsp.direct.dbentity`
- DTO: `com.inspur.dsp.direct.entity.dto`
- VO: `com.inspur.dsp.direct.entity.vo`
- BO: `com.inspur.dsp.direct.entity.bo`


### 项目中的枚举
#### confirmation_task表tasktype字段（任务类型)枚举：

- confirmation_task - 确认任务
- claim_task - 认领任务

后端代码中的枚举类:

TaskTypeEnums.java

##### confirmation_task表根据tasktype字段（任务类型）判断status字段（数据元状态）

认领任务(tasktype=claim_task )状态(status):

* pending_claimed - 待认领
* claimed - 认领
* not_claimed - 不认领

确认任务(tasktype=confirmation_task )状态(status)

* 待确认 pending
* 确认 confirmed
* 拒绝 rejected

后端代码中的枚举类:

ConfirmationTaskEnums.java

#### 定源事件记录表(source_event_record)中定源类型(source_type)枚举:

* 确认型核定 - confirmation_approval
* 认领型核定 - claim_approval
* 协商结果录入 - negotiation_result_entry
* 手动定源 - manual_source
* 导入定源 - import_source

后端代码中的枚举类:

RecordSourceTypeEnums.java

#### 列表查询条件DTO

所有当涉及到列表查询时，前端传来的DTO字段都从下面取，不要擅自命名

| 属性名                                    | 类型       | 说明                                                         |
| ----------------------------------------- | ---------- | ------------------------------------------------------------ |
| List<collectUnitCode> collectUnitCodeList | 字符串List | 采集单位统一社会信用代码列表                                 |
| List<status> statusList                   | 字符串list | base_dataelement表status状态，主状态，具体内容见枚举值       |
| keyword                                   | 字符串     | 输入的查询条件                                               |
| sendDateBegin                             | 日期       | 发起时间查询起始时间<br />注意，采集方页面上的接收时间就是组织方的发起时间，所以，此处用sendDate属性即可，不会同时出现包含发起时间，接收时间的查询情况。 |
| sendDateEnd                               | 日期       | 发起时间查询截止时间                                         |
| List<taskStatus> taskStatusList           | 字符串list | confirmation_task表status状态，任务状态，具体见枚举值        |
| List<sourceUnitCode> sourceUnitCodeList   | 字符串list | 数源单位统一社会信用代码列表                                 |
| confirmDateBegin                          | 日期       | 定源时间查询起始时间                                         |
| confirmDateEnd                            | 日期       | 定源时间查询截止时间                                         |
| processDateBegin                          | 日期       | 任务处理时间起始时间，表confirmation_task的processing_date   |
| processDateEnd                            | 日期       | 任务处理时间截止时间，                                       |

#### base_data_element(基准数据元表)status字段(数据元状态)枚举:

待定源 pending_source
确认中 confirming
认领中 claimed
待核定 pending_approval
待协商 pend negotiating
已定源 confirmed


#### 后端代码中的枚举类:

StatusEnums.java


### 控制器约定
所有控制器应该:
- 使用注解: `@Slf4j`, `@RestController`, `@RequestMapping`
- 使用 `@RespAdvice` 进行自动响应包装
- 使用 `@SysLog` 进行操作日志记录
- 遵循 RESTful 模式

### 服务层
- 服务接口定义在 `service` 包中
- 实现类在 `service.Impl` 包中，使用 `@Service` 注解
- 使用依赖注入获取 DAO 和其他服务

### 数据访问
- 使用 MyBatis Plus 作为 ORM
- Mapper 接口在 `dao` 包中
- XML 映射文件在 `src/main/resources/mapper` 中
- 实体类在 `dbentity` 包中，使用适当的注解

## 配置

### 数据库
- 使用 Jasypt 加密密码
- 配置在 `application.yml` 和 `application-local.properties` 中
- 通过 Flyway 进行数据库迁移，脚本在 `src/main/resources/db/migration`

### 认证
- 使用 `UserInfoFilter` 进行登录验证
- 基于会话的身份验证，使用 cookies
- API 测试时需要添加 `SESSION` cookie

### 开发环境
- 本地配置在 `config/application-local.properties` 中
- 密码加密测试方法: `com.inspur.dsp.direct.console.test.AppTest.encryption()`

## 代码规范和标准

### 实体和 DTO 设计
- 避免使用 `Map` 或 `JSONObject` 进行数据传输
- 使用特定的 DTO/VO/BO 类来确保类型安全
- 遵循既定的命名约定

### 异常处理
- 使用结构化的异常处理模式
- 返回适当的 HTTP 状态码
- 包含有意义的错误消息

### 日志记录和审计
- 使用 `@SysLog` 注解进行操作跟踪
- 在服务方法中包含适当的日志记录语句
- 遵循既定的日志记录模式

## 依赖和外部系统

### 主要依赖
- Spring Boot 生态系统 (Web, Data, Cloud)
- MyBatis Plus 用于数据库操作
- Apache Dubbo 用于服务网格
- Hutool 用于工具函数
- EasyExcel 用于 Excel 处理
- Apache POI 用于文档处理

### 外部集成
- Nacos 服务注册和配置中心
- Apache Kafka 用于消息传递
- Zookeeper 用于协调 (通过 Dubbo)
- GBase 数据库驱动支持

---
