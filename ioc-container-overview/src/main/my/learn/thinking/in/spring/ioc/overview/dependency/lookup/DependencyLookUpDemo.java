package my.learn.thinking.in.spring.ioc.overview.dependency.lookup;

import my.learn.thinking.in.spring.ioc.overview.annotation.SuperU;
import my.learn.thinking.in.spring.ioc.overview.domain.Super;
import my.learn.thinking.in.spring.ioc.overview.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * 依赖查找实例
 * 1. 通过名称的方式来查找
 *
 * 能讲讲factoryBean与beanFactory的区别吗？是只在于factoryBean在通过getObject()获取对象时都是新创建的吗？
 * FactoryBean 是一种特殊的 Bean，需要注册到 IoC 容器，通过容器 getBean 获取 FactoryBean#getObject() 方法的内容，而 BeanFactory#getBean 则是依赖查找，如果 Bean 没有初始化，那么将从底层查找或构建。
 *
 * FactoryBean默认创建的bean不是也是单例吗，没太懂文中说的，这就是BeanFactory和FactoryBean的重大区别是指啥
 * -
 * FactoryBean 从5.0开始默认值是单例，它有一个方法 isSingleton 来判断是否为单例对象。BeanFactory 是Bean 容器，FactoryBean 是工厂式的Bean 生成器
 *
 *
 */
public class DependencyLookUpDemo {
    public static void main(String[] args) {
        // 配置 XML 配置文件
        // 启动 Spring 应用上下文
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:META-INF/dependency-lookup-context.xml");
        // 按照类型查找
        lookupByType(beanFactory);
        // 按照类型查找结合对象
        lookupCollectionByType(beanFactory);
        lookupByAnnotationType(beanFactory);
        lookupInRealTime(beanFactory);
        lookupInLazy(beanFactory);
    }

    /**
     * DefaultListableBeanFactory$DependencyObjectProvider 即使 ObjectProvider 实现，而 ObjectProvider 则是 ObjectFactory 子接口。
     * @param beanFactory
     */
    private static void lookupByAnnotationType(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            Map<String, User> users = (Map) listableBeanFactory.getBeansWithAnnotation(SuperU.class);
            Map<String, Object> users2 = listableBeanFactory.getBeansWithAnnotation(SuperU.class);
            System.out.println("查找标注 @Super 所有的User集合对象"+users);
            System.out.println("查找标注 @Super 所有的User集合对象"+users2);
        }
    }

    private static void lookupCollectionByType(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            Map<String, User> users = listableBeanFactory.getBeansOfType(User.class);
            System.out.println("查找到的所有的User集合对象"+users);
        }
    }

    /**
     * Exception in thread "main" org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type 'my.learn.thinking.in.spring.ioc.overview.domain.User' available: expected single matching bean but found 2: user,superUser
     * 	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveNamedBean(DefaultListableBeanFactory.java:1180)
     * 	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveBean(DefaultListableBeanFactory.java:416)
     * 	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBean(DefaultListableBeanFactory.java:349)
     * 	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBean(DefaultListableBeanFactory.java:342)
     * 	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1126)
     * 	at my.learn.thinking.in.spring.ioc.overview.dependency.lookup.DependencyLookUpDemo.lookupByType(DependencyLookUpDemo.java:37)
     * 	at my.learn.thinking.in.spring.ioc.overview.dependency.lookup.DependencyLookUpDemo.main(DependencyLookUpDemo.java:21)
     *
     * 	加一个 primary 属性
     * @param beanFactory
     */
    private static void lookupByType(BeanFactory beanFactory) {
        User user = beanFactory.getBean(User.class);
        System.out.println("根据类型实时查找：" + user);
    }

    /**
     * ObjectFactory 对象并不是直接返回了实际的 Bean，而是一个 Bean 的查找代理。当得到 ObjectFactory 对象时，相当于 Bean 没有被创建，只有当 getObject() 方法时，才会触发 Bean 实例化等生命周期。
     * beanFactory.getBean("objectFactory")的是ObjectFactoryCreatingFactoryBean的内部类，这个内部类继承了ObjectFactory
     *
     * 实时查找就是马上查找到bean, 延时查找是 查找到目标 bean 的引用, 然后通过这个引用再来查找目标 bean
     *
     * ObjectFactory 仅是一个操作对象，当依赖注入或依赖查找该 ObjectFactory 对象时，实际内嵌的 Bean 需要 getObject() 方法来获取。
     *
     * ObjectFactoryCreatingFactoryBean 与ObjectFactory 没有父子关系，不过 ObjectFactoryCreatingFactoryBean  属于 FactoryBean 来创建 ObjectFactory，当依赖查找或依赖注入时，将返回 ObjectFactory 实例
     *
     * 所谓的延迟是指在注入时，不会马上注入目标对象，而是先弄一个句柄，当需要时，再次获取。
     *
     * 依赖注入的第一步是通过依赖查找去符合条件的Bean，延迟依赖查找相当于依赖注入的对象是一个代理对像，当调用该对象getObject 方法是才实际依赖查找
     *
     * @param beanFactory
     */
    private static void lookupInLazy(BeanFactory beanFactory) {
        ObjectFactory<User> objectFactory = (ObjectFactory<User>) beanFactory.getBean("objectFactory");
        User user = objectFactory.getObject();
        System.out.println("延迟查找：" + user);
    }

    private static void lookupInRealTime(BeanFactory beanFactory) {
        User user = (User) beanFactory.getBean("user");
        System.out.println("实时查找：" + user);
    }
}
