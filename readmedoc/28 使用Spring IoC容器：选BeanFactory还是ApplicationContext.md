小马哥  这种beanFactory  运用的什么设计模式  我看到了模板  适配器  其他看不出来  可以说下嘛
-
作者回复: DefaultListableBeanFactory 实现的设计模式有：
抽象工厂（BeanFactory 接口实现）
组合模式（组合 AutowireCandidateResolver 等实例）
单例模式（Bean Scope）
原型模式（Bean Scope）
模板模式（模板方法定义：AbstractBeanFactory）
适配器模式（适配 BeanDefinitionRegistry 接口）
策略模式（Bean 实例化）
代理模式（ObjectProvider 代理依赖查找）

当前类不加@Configuration也能获取到bean，并且当前类也会作为一个bean
-
作者回复: @Configuration 注册并不是必须的，不过标注它之后 Bean 的类被 CGLib 提升。

小马哥，那我可以理解成beanfacotry 是用于xml依赖查找，applicationContext 是用于注解依赖注入吗？
-
作者回复: 不是，ApplicationContext 才区分 XML 和注解，BeanFactory 通常只管理 BeanDefinition 和其 Bean 实例。

小马哥，听了你的课，有个疑问，到底什么是容器，我理解的容器，就是一个盒子，所有的东西都在盒子里面，这样理解对吗
-
作者回复: 是的，就是一个装对象的“盒子”。

refresh() 这段代码什么意思！springboot编程思想启动原理,那节有提到过,一直没有懂
-
作者回复: refresh 是刷新上下文，其实是驱动的意思

小马哥 我看视频里用了ApplicationContext实现方式，是用继承的BeneFactory去获取Bean的。那么用集成的DefaultListableBeanFactory是不是也可以？平时应用的时候是用继承的还是用集成的呢？
-
作者回复: Java 类是单集成体系，要实现也是可以的，不过集成的层次会比较深

老师，IoC底层容器是BeanFactory， 这里的BeanFactory指的是所有以BeanFactory结尾的类构成了容器，不是特指 org.springframework.beans.factory.BeanFactory这个接口吧？
-
作者回复: 是指 BeanFactory 的实现。

refresh不加也可以的，我自己用了个demo试了下，也能get到bean.
-
作者回复: 还是有些不一样的，getBean 能够让 Bean 初始化，然而，ApplicationContext 所关联的特性无法使用。

beanfactory充当ioc容器，将不能使用企业级特性如aop等，需要applicationcontext才行