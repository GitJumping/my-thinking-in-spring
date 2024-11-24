ApplicationContext除了IoC容器，还有提供
- 面向切面（AOP）
- 配置元信息（Configuration Metadata）
- 资源管理（Resources）
- 事件（Events）
- 国际化（i18n）
- 注解（Annotations）
- Environment抽象（Environment Abstraction）

BeanFactory和ApplicationContext才是真正的IOC容器，ApplicationContext又提供了AOP的功能
-
并不矛盾，BeanFactory 是 Bean 容器，它不提供企业特性，比如 AOP、事务以及 事件等，这些都被 ApplicationContext 支持。

这个意思是不是说spring中的容器不光是Bean容器，ApplicationContext提供除Bean容器外的其他容器功能？
-
是的

BeanFactory 是 Bean 容器，ApplicationContext通过组合BeanFactory提供依赖查找的能力
-
bingo