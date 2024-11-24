package my.learn.thinking.in.spring.ioc.overview.dependency.injection;

import my.learn.thinking.in.spring.ioc.overview.annotation.SuperU;
import my.learn.thinking.in.spring.ioc.overview.domain.User;
import my.learn.thinking.in.spring.ioc.overview.repository.UserRepository;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.stream.StreamSupport;

/**
 * 依赖注入实例
 *
 * 依赖的对象是否为主动获取，是的话，就是依赖查找，否则就是依赖注入，由框架绑定完成
 *
 * ObjectFactory 通常是针对单类 Bean 做延迟获取的，BeanFactory 则是全局 Bean 管理的容器
 *
 * ObjectFactory<ApplicationContext> 和 ObjectFactory<BeanFactory>  分别获取的 ApplicationContext 和 ApplicationContext 所关联的 BeanFactory，所以不会相等。
 *
 * 1.内建依赖指的是DefaultListableBeanFactory属性resolvableDependencies这个map里面保存的bean，自定义bean指的是通过DefaultSingletonBeanRegistry#registerSingleton手动注册的bean。它们都在BeanFactory里面；
 * 2.依赖注入的时候比如@AutoWired(AutowiredAnnotationBeanPostProcessor处理)会调用DefaultListableBeanFactory#resolveDependency方法去resolvableDependencies里面找，而依赖查找BeanFactory.getBean(xxx)是不会去resolvableDependencies这个map里面找的。
 *
 * spring容器依赖的三个来源：
 * ①自定义Bean，可以通过getBean
 * ②容器内建的依赖Bean，是容器内非Bean。如BeanFactory。无法通过getBean获取。是通过AutowireCapableBeanFactory中的resolveDependency方法来注册
 * ③容器内建Bean，如Environment、BeanDefinitions 和 Singleton Objects。可以通过getBean获取
 *
 * 如果是 @Configuration 标准的 Class 所对应的 Bean 是会被 CGLib 提升的，而 @Bean 则则不会，这是由于技术限制所致。所以通常需要 org.springframework.aop.support.AopUtils#getTargetClass 方法获取真实的 Class
 *
 * ObjectFactory 包装的Bean 搜索范围更大，不限于BeanDefinition 中，还包括内部依赖对象，比如 BeanFactory，ApplicationContext 以及 Environment等
 *
 * @Autowired 在 Bean 生命周期时发生注入，是通用的，与XML 或注解的注册 Spring Bean 的方式无关
 */
public class DependencyInjectionDemo {
    public static void main(String[] args) {
        // 配置 XML 配置文件
        // 启动 Spring 应用上下文
        // 导入实例，因为包含上下文的信息，一个是injection的上下文，一个是lookup的上下文
//        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:META-INF/dependency-injection-context.xml");
        //因为ApplicationContext 就是一个 BeanFactory的子接口，享有BeanFactory原来定义有的方法签名
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:META-INF/dependency-injection-context.xml");
        /*
        依赖来源1:自定义地bean
         */
        UserRepository userRepository = applicationContext.getBean("userRepository", UserRepository.class);
        System.out.println(userRepository.getUsers());

        /**
         * 注入进来的BeanFactory不是一个普通的bean对象，是一个内建的对象
         */
        //依赖注入
        System.out.println(userRepository.getBeanFactory());
        System.out.println(userRepository.getBeanFactory() == applicationContext);

        //private ObjectFactory<User> userObjectFactory;下返回superUser
        //依赖注入注入进来的，这里得到的Super，因为定义了primary
        //private ObjectFactory<ApplicationContext> objectFactory;
        //改成ApplicationContext，返回ApplicationContext
        ObjectFactory userFactory = userRepository.getUserObjectFactory();
        System.out.println(userFactory.getObject());
        //这里相等，说明刚才 ObjectFactory auto-wire时候，注入了一个ApplicationContext，ApplicationContext又是一个BeanFactory
        System.out.println(userFactory.getObject() == applicationContext);

        //依赖查找（错误）
        /*
        依赖来源2:内建依赖

        内建的 Bean 是普通的 Spring Bean，包括 BeanDefinitions 和 Singleton Objects，而内建依赖则是通过 AutowireCapableBeanFactory 中的 resolveDependency 方法来注册，这并非是一个 Spring Bean，无法通过依赖查找获取
         */
//        System.out.println(beanFactory.getBean(BeanFactory.class));

        /*
        依赖来源3:容器内建的bean
        Spring IoC 底层容器就是指的 BeanFactory 的实现类，大多数情况是 DefaultListableBeanFactory 这个类，它来管理 Spring Beans，而 ApplicationContext 通常为开发人员接触到的 IoC 容器，它是一个 Facade，Wrap 了 BeanFactory 的实现
        自定义的bean和内建的bean能够通过依赖查找查询到，但是内建依赖是不能通过依赖查找查询到，因为他们的来源不同
         */
        Environment environment = applicationContext.getBean(Environment.class);
        System.out.println("获取 Environment 类型地 Bean： "+environment);
    }

    /**
     * https://docs.spring.io/spring-framework/reference/6.0/core/beans/introduction.html
     *
     * org.springframework.context.support.AbstractApplicationContext
     *
     * org.springframework.context.ConfigurableApplicationContext#getBeanFactory()
     *
     * org.springframework.context.ConfigurableApplicationContext <-
     * org.springframework.context.ApplicationContext <-
     * org.springframework.beans.factory.BeanFactory
     *
     * org.springframework.context.support.AbstractRefreshableApplicationContext#getBeanFactory()
     * 具体的实现上，beanFactory是一个组合的属性
     * org.springframework.context.support.AbstractRefreshableApplicationContext#beanFactory
     * 在上下文里面是一种组合：将BeanFactory组合成类的一个属性Field，并没有完全的靠extends来获取BeanFactory
     * 同时在接口里又是extends关系，见上面记录的层层extends或implements
     * 这种组合就有点像代理的方式
     *
     * org.springframework.context.support.AbstractApplicationContext#getBean(java.lang.Class)
     * 再来随便看一下getBean的实现，是getBeanFactory().getBean的方式获取BeanFactory
     * 用代理的方式去查找BeanFactory，而不是作为BeanFactory的继承或实现，类自身就具备这个能力，而是用的组合对象去做事情
     *
     * BeanFactory和ApplicationContext是同一种类，具体实现上，ApplicationContext组合了一个BeanFactory的实现
     * 这就是为什么ApplicationContext不等于BeanFactory，它们是复用了同一个接口，但是是两个对象
     *
     * ApplicationContext是BeanFactory的子接口，说明ApplicationContext is BeanFactory。并且ApplicationContext 是BeanFactory的包装类，也就是内部组合了BeanFactory的实现-DefaultListableBeanFactory。为什么包装了DefaultListableBeanFactory，因为它需要简化且丰富功能来为企业开发提供更高的便捷性，也就是说ApplicationContext 是DefaultListableBeanFactory的超集。
     * 至于为什么UserRepository注入的BeanFactory  不等于ClassPathXmlApplicationContext得到的BeanFactory ，是因为AbstractApplicationContext#prepareBeanFactory中 指明了 beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory); 也就是说当byType是BeanFactory.class的时候，获得是的ApplicationContext中的DefaultListableBeanFactory对象。
     * 那真正的IOC的底层实现就是BeanFactory的实现类，因为ApplicationContext是委托DefaultListableBeanFactory来操作getBean等方法的。
     *
     * @param userRepository
     * @param beanFactory
     */
//    private static void whoIsIocContainer(UserRepository userRepository, BeanFactory beanFactory) {
    private static void whoIsIocContainer(UserRepository userRepository, ApplicationContext applicationContext) {
        //这个表达式为什么不会成立？
        System.out.println(userRepository.getBeanFactory() == applicationContext);

        //Application is a BeanFactory
    }
}
