UserRepository中的BeanFactory beanFactory属性为什么通过autowired=“byType”，注入的是DefaultListableBeanFactory，而不是ClassPathXmlApplicationContext？既然他们两者属于BeanFactory接口，按type注入，为什么不是ClassPathXmlApplicationContext？是框架哪里设置的吗？
-
答案在 org.springframework.context.support.AbstractApplicationContext#prepareBeanFactory 方法中，其中
代码明确地指定了 BeanFactory 类型的对象是 ApplicationContext#getBeanFactory() 方法的内容，而非它自生

一些想说的：这课应该是跟对了没跑了，吊不吊打面试官不知道，只要我不被吊打就行（只看到这一课的期盼哈哈）
背景：有意等课程完结、有一定留言和点赞之后再来听；不知所以然的做过一些 Spring Boot 项目；硬着头皮痛苦熬完《Spring Boot 编程思想》+《Spring 源码深度解析》的核心实现部分，总觉得距离领悟还差一层，很多关键的接口、实现类、方法及概念都十分熟悉，但仍不得其要领。隔壁丁老师全家桶的课也都听了，但应对面试还是觉得心里虚。故有缘相见于此。
-
个人自学 Spring 经验吐血小总结（除学习官网外）：
1. 零基础：推荐先别深入纠结太多，走起 hello world、curd 找找感觉。此阶段可跟着隔壁丁老师的全家桶课程来，用法：快速扫盲、走 demo 体会
2. 深入Spring：先跟小马哥这课：真的是手把手看着从零撸代码（而不是为了速度直接上准备好的案例），能更真实完整地还原读者遇到的许多情况。可考虑辅以《Spring 源码深度解析》（虽然书中前言写了是基于 5.0.x，但有少量代码是 3.2.x 或 4.0.x 版本的，瑕不掩瑜，在逐行比对书本与官方源码的过程中，学到了许多「为什么这么升级」）
3. Spring Boot：上述提到的两本书都可走起
4. Spring Cloud：推《Spring Cloud 微服务实战》
-
一些心态与方法：
1. 类名普遍特别长长长，还有很深的继承关系，记不住没关系，沉下心、看仔细、读出来、打出来，即便看老师视频中的跳转你都心中有图
2. 关键方法的调用链可自行总结，体会更深，可辅以 Idea 书签功能（F11，Shift + F11）跟源码
3. 跟源码之前，先别进入，想/猜这方法是干什么的，如果是你实现，你大体会做什么（勾勒地图），再跟入代码，验证猜想。非常重要，这是能力培养，不只是学会一套框架。从哪学的？从看小马哥书里的最大体会...源码和特性一个没怎么记住，小马哥想要带着大家一起深入、推导、甚至再回过头纠正的感觉倒是深刻了。
-
也许这就是小马哥的风格吧，有一说一，一开始我是不适应的，总希望来个结论干脆一点，可一想到「Spring 版本也会升级啊，那以后新东西来了怎么办」，还是投「授人以渔」一票吧！

BeanFactory和ApplicationContext的关系，从设计模式上讲，应该是代理模式吧？
-
是的，ApplicationContext 是使用了代理模式，也使用了面门模式。

BeanFactory和ApplicationContext有何区别？
● ApplicationContext是BeanFactory的子接口
● BeanFactory是一个底层的IOC容器，提供了IOC容器的基本实现，而ApplicationContext则是BeanFactory的超集提供了丰富的企业级特性。
● ApplicationContext是委托DefaultListableBeanFactory来实现Bean的依赖查找和依赖注入。

1.文中xml中自动注入的BeanFactory为DefaultListableBeanFactory,是因为在AbstractApplicationContext#prepareBeanFactory方法中beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory)中指定了接口的实现为自己bean对象也就是DefaultListableBeanFactory(最后是放在一个map中)
2.我们通过classPathXmlApplicationContext创建的bean对象为ApplicationContext类型,虽然继承了接口BeanFactory但是依赖注入的并不是它所以不相等
3.ApplicationContext中每次getBean()都是通过DefaultListableBeanFactory来查找bean对象的

继承 BeanFactory 就是为了 方便用个 getBean 这些方法，也就是代理模式了

有点像委派，直接调用底层BeanFactory实现类的方法

1.xml中自定义bean中注入的BeanFactory是DefaultListableBeanFactory,是我们在创建ClassPathXmlApplicationContext时,构造函数再次创建的一个组合对象,即使他们的顶级接口都是BeanFactory,但是它们根本不是同一个对象
创建组合对象是在
org.springframework.context.support.AbstractRefreshableApplicationContext#refreshBeanFactory方法中的createBeanFactory()方法中明确创建了DefaultListableBeanFactory()
2.在我们操作Bean(getBean())时,其实调用的都是组合对象的getBean()方法org.springframework.context.support.AbstractApplicationContext#getBean(java.lang.String)

AbstractApplicationContext#prepareBeanFactory  里放入到依赖处理的map DefaultListableBeanFactory#registerResolvableDependency,
然后在创建UserRepository对象时，通过属性方法完成注入BeanFactory，是在populateBean中查找候选的bean完成注入。  DefaultListableBeanFactory#findAutowireCandidates

BeanFactory的子接口有两条路子，一条是ApplicationContext，一条是ConfigurableListableBeanFactory，虽然继承同一个接口，但是实际上是两个独立的对象，悟了

BeanFactory和ApplicationContext实例不同是因为采用组合代理的模式设计。

1. BeanFactory#getBean()方法有两个实现类：
1.1 AbstractBeanFactory#getBean(); 
1.2、AbstractApplicationContext#getBean();从实现上来看AbstractBeanFactory的getBean方法是执行doGetBean去查找Bean对象，而AbstractApplicationContext#getBean()是通过getBeanFactory().getBean(name);通过获取ConfigurableListableBeanFactory对象来进行调用getBean方法获取。
2. 从接口实现上来看ApplicationContext接口是继承自BeanFactory接口，所以，ApplicationContext是在BeanFactory的基础上进行企业级的功能扩展

24.1 userRepository.getBeanFactory对象和 BeanFactory beanFactory = new XmlClassPathApplicationContext()对象，不相等。
24.2 userRepository.getObjectFactory().getObject() 对象与声明的 BeanFactory对象，相等。
这一章节，对24.1的问题做了很好的解释，因为ApplicationContext是BeanFactory的一个子接口，但是是通过代理方式实现的，底层实际上作为ioc容器的是类中的一个组合实例，DefaultListableBeanFactory，所以自动注入的BeanFactory就是这个DefaultListableBeanFactory，和ApplicationContext不是同一个对象，和applicationContext.getBeanFactory()是一个对象。
-A
24.2 userRepository.getObjectFactory().getObject() 对象与声明的 BeanFactory对象，相等，
ObjectFactory 对象相当于 Bean 的 Supplier 包装，通过 getObject() 可以获取自身，类似于这样 ObjectFactory factory = () -> bean;

ApplicationContext是BeanFactory的子接口，说明ApplicationContext is BeanFactory。并且ApplicationContext 是BeanFactory的包装类，也就是内部组合了BeanFactory的实现-DefaultListableBeanFactory。为什么包装了DefaultListableBeanFactory，因为它需要简化且丰富功能来为企业开发提供更高的便捷性，也就是说ApplicationContext 是DefaultListableBeanFactory的超集。
至于为什么UserRepository注入的BeanFactory 不等于ClassPathXmlApplicationContext得到的BeanFactory ，是因为AbstractApplicationContext#prepareBeanFactory中 指明了 beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory); 也就是说当byType是BeanFactory.class的时候，获得是的ApplicationContext中的DefaultListableBeanFactory对象。
那真正的IOC的底层实现就是BeanFactory的实现类，因为ApplicationContext是委托DefaultListableBeanFactory来操作getBean等方法的。

AbstractRefreshableApplicationContext.getBeanFacory();获取的是组合的对象DefaultListableBeanFactory，那么着了类是在那里set进去的呢?

applicationContext.getAutowireCapableBeanFactory() == userRepository.getBeanFactory()
ApplicationContext里面getAutowireCapableBeanFactory()和userRepository.getBeanFactory()指向的BeanFactory是同一个。说明ApplicationContext只是聚合了BeanFactory，和userRepositoy里面指向的BeanFactory是同一个对象，而不是重新创建的？
-
是的，就是这样~

为什么spring需要做这么一层代理实现呢，直接用ApplicationContext继承某个BeanFactory，然后提供新的能力的方式不好吗，是因为太耦合了吗，
-
这样设计相对简单，将 ApplicationContext 作为  Facade 提供一个大而全的 API

反复观看第三章以后，发现ObjectFactory也是内建对象，不需要通过在xml中配置，可以直接通过依赖注入的方式获取得到。只要指定ObjectFactory<T>的泛型类，再设置setter方法注入，即可通过getObject方法获取到需要的bean。通过ObjectFactory.getObject方法获取到的对象，与依赖注入同源，是同个对象。