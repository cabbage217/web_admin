# web_admin
此项目是一个通用的后台管理系统，使用Spring Boot + Mybatis实现，前端使用layui框架。功能包含基本的用户管理、权限管理等，业务相关逻辑不与管理后台有过多耦合，数据应存储于不同的数据表中。

## 配置
创建mysql数据库后，修改application-dev.yml中数据库配置，再执行init.sql文件，无需更多配置

## 管理员账号
执行init.sql会生成一个管理员账号，用户名为admin，密码是123456
