# 🏠 智能宿舍管理系统

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3.x-brightgreen.svg)](https://vuejs.org/)
[![Element Plus](https://img.shields.io/badge/Element%20Plus-latest-blue.svg)](https://element-plus.org/)

> 一个基于 Spring Boot + Vue 3 的现代化高校宿舍管理系统，提供完整的宿舍分配、入住管理、维修报修等功能。

## 📋 项目简介

智能宿舍管理系统是一个面向高校的综合性宿舍管理平台，旨在简化宿舍管理流程，提升管理效率。系统采用前后端分离架构，提供了直观的用户界面和强大的后台管理功能。

### ✨ 核心功能

- **👥 用户管理**：支持学生、管理员等多角色管理，完善的权限控制
- **🏢 楼栋管理**：楼栋信息维护，自动生成房间，支持男女宿舍区分
- **🚪 房间管理**：房间信息管理，床位状态跟踪，实时显示入住情况
- **📝 入住管理**：学生入住登记，宿舍分配，押金管理
- **🔧 维修报修**：在线报修，维修进度跟踪，维修记录管理
- **💰 费用管理**：水电费记录，缴费管理，费用统计
- **📊 数据统计**：多维度数据分析，可视化图表展示

## 🚀 创新点

### 1. 智能房间生成
- **自动化生成**：创建楼栋时自动生成所有房间，无需手动逐个添加
- **规范化命名**：房间号采用"楼层(1位) + 房间号(2位)"格式，如305表示3楼5号房间
- **批量操作**：支持批量创建、批量分配，大幅提升管理效率

### 2. 灵活的宿舍分配机制
- **管理员分配**：管理员可直接为学生分配宿舍，无需学生性别等前置条件
- **学生自选**：学生可在开放的宿舍中自主选择房间和床位
- **智能验证**：自动检查房间容量、床位占用、重复分配等问题
- **实时更新**：分配后自动更新房间入住人数和床位状态

### 3. 完善的数据关联
- **VO模式**：使用Value Object模式，一次查询返回完整的关联数据
- **懒加载优化**：使用@Lazy注解解决循环依赖，提升启动速度
- **级联查询**：用户信息自动关联角色、宿舍、入住记录等信息

### 4. 现代化UI/UX设计
- **响应式布局**：适配各种屏幕尺寸，支持移动端访问
- **卡片式设计**：美观的卡片布局，清晰的信息层次
- **智能提示**：操作按钮配备Tooltip，提升用户体验
- **表格优化**：支持固定列、滚动加载、自适应高度

### 5. 安全可靠的事务处理
- **事务一致性**：关键操作使用@Transactional确保数据一致性
- **异常回滚**：操作失败自动回滚，防止数据不一致
- **并发控制**：防止床位重复分配、房间超员等问题

## 🛠️ 技术栈

### 后端技术
- **核心框架**：Spring Boot 3.x
- **持久层**：MyBatis-Plus
- **数据库**：MySQL 8.0
- **安全认证**：JWT Token
- **API文档**：Swagger 3 (OpenAPI)
- **工具库**：Lombok、Hutool

### 前端技术
- **框架**：Vue 3 + Vite
- **UI组件**：Element Plus
- **状态管理**：Pinia
- **路由**：Vue Router 4
- **HTTP客户端**：Axios
- **图表**：ECharts

## 📦 项目结构

```
BOOK/
├── backend/                 # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/
│   │   │   │       ├── controller/    # 控制器层
│   │   │   │       ├── service/       # 服务层
│   │   │   │       ├── mapper/        # 数据访问层
│   │   │   │       ├── entity/        # 实体类
│   │   │   │       ├── vo/            # 视图对象
│   │   │   │       ├── common/        # 公共类
│   │   │   │       ├── config/        # 配置类
│   │   │   │       └── exception/     # 异常处理
│   │   │   └── resources/
│   │   │       ├── mapper/            # MyBatis XML
│   │   │       └── application.yml    # 配置文件
│   │   └── test/                      # 测试代码
│   └── pom.xml                        # Maven配置
│
└── frontend/                # 前端项目
    ├── src/
    │   ├── api/             # API接口
    │   ├── components/      # 公共组件
    │   ├── views/           # 页面视图
    │   │   ├── system/      # 系统管理
    │   │   └── front/       # 前台页面
    │   ├── router/          # 路由配置
    │   ├── stores/          # 状态管理
    │   ├── utils/           # 工具函数
    │   └── assets/          # 静态资源
    ├── package.json         # 依赖配置
    └── vite.config.js       # Vite配置
```

## 🚀 快速开始

### 环境要求

- **JDK**：17 或更高版本
- **Node.js**：16.x 或更高版本
- **MySQL**：8.0 或更高版本
- **Maven**：3.6 或更高版本

### 后端部署

1. **克隆项目**
```bash
git clone https://github.com/qingyu6688/dormitory
cd dormitory-management-system/backend
```

2. **创建数据库**
```sql
CREATE DATABASE dormitory_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. **导入数据库脚本**
```bash
# 在MySQL中执行项目提供的SQL脚本
mysql -u root -p dormitory_db < database/init.sql
```

4. **修改配置文件**

编辑 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/dormitory_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

5. **启动后端服务**
```bash
# 使用Maven
mvn clean install
mvn spring-boot:run

# 或使用IDE直接运行主类
# com.example.DormitoryApplication
```

后端服务将在 `http://localhost:8080` 启动

### 前端部署

1. **进入前端目录**
```bash
cd ../frontend
```

2. **安装依赖**
```bash
npm install
# 或使用 yarn
yarn install
```

3. **修改API地址**

编辑 `src/utils/request.js`，确保baseURL指向后端地址：
```javascript
const request = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 5000
})
```

4. **启动开发服务器**
```bash
npm run dev
# 或使用 yarn
yarn dev
```

前端服务将在 `http://localhost:5173` 启动

5. **构建生产版本**
```bash
npm run build
# 构建产物在 dist/ 目录
```

### 默认账号

- **管理员账号**：admin / 123456
- **学生账号**：student / 123456

## 📸 系统截图

### 用户管理
![用户管理](docs/images/user-management.png)

### 宿舍分配
![宿舍分配](docs/images/room-assignment.png)

### 数据统计
![数据统计](docs/images/dashboard.png)

## 🔧 API文档

启动后端服务后，访问 Swagger API 文档：
```
http://localhost:8080/swagger-ui/index.html
```

## 📝 开发计划

- [ ] 移动端适配优化
- [ ] 访客登记功能
- [ ] 宿舍评分系统
- [ ] 智能推荐算法
- [ ] 数据导出功能
- [ ] 消息通知系统
- [ ] 微信小程序版本

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 📄 开源协议

本项目采用 [MIT](LICENSE) 协议开源

## 👨‍💻 作者

- **qingyu** - [GitHub](https://github.com/yourusername)

## 🙏 致谢

感谢以下开源项目：
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Vue.js](https://vuejs.org/)
- [Element Plus](https://element-plus.org/)
- [MyBatis-Plus](https://baomidou.com/)

## 📞 联系方式

如有问题或建议，欢迎通过以下方式联系：
- 📧 Email: 2661462026@qq.com
- 💬 Issue: [GitHub Issues](https://github.com/yourusername/dormitory-management-system/issues)

---

⭐ 如果这个项目对你有帮助，请给个 Star 支持一下！
