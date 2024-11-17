package my.learn.thinking.in.spring.ioc.overview.dependency.injection;

import my.learn.thinking.in.spring.ioc.overview.annotation.SuperU;
import my.learn.thinking.in.spring.ioc.overview.domain.User;
import my.learn.thinking.in.spring.ioc.overview.repository.UserRepository;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * 依赖注入实例
 *
 * 依赖的对象是否为主动获取，是的话，就是依赖查找，否则就是依赖注入，由框架绑定完成
 *
 * ObjectFactory 通常是针对单类 Bean 做延迟获取的，BeanFactory 则是全局 Bean 管理的容器
 *
 * ObjectFactory<ApplicationContext> 和 ObjectFactory<BeanFactory>  分别获取的 ApplicationContext 和 ApplicationContext 所关联的 BeanFactory，所以不会相等。
 *
 * @Autowired 在 Bean 生命周期时发生注入，是通用的，与XML 或注解的注册 Spring Bean 的方式无关
 */
public class DependencyInjectionDemo {
    public static void main(String[] args) {
        // 配置 XML 配置文件
        // 启动 Spring 应用上下文
        // 导入实例，因为包含上下文的信息，一个是injection的上下文，一个是lookup的上下文
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:META-INF/dependency-injection-context.xml");
        UserRepository userRepository = beanFactory.getBean("userRepository", UserRepository.class);
        System.out.println(userRepository.getUsers());

        /**
         * 注入进来的BeanFactory不是一个普通的bean对象，是一个内建的对象
         */
        //依赖注入
        System.out.println(userRepository.getBeanFactory());
        System.out.println(userRepository.getBeanFactory() == beanFactory);

        //private ObjectFactory<User> userObjectFactory;下返回superUser
        //依赖注入注入进来的，这里得到的Super，因为定义了primary
        //private ObjectFactory<ApplicationContext> objectFactory;
        //改成ApplicationContext，返回ApplicationContext
        ObjectFactory userFactory = userRepository.getUserObjectFactory();
        System.out.println(userFactory.getObject());
        //这里相等，说明刚才 ObjectFactory auto-wire时候，注入了一个ApplicationContext，ApplicationContext又是一个BeanFactory
        System.out.println(userFactory.getObject() == beanFactory);

        //依赖查找（错误）
//        System.out.println(beanFactory.getBean(BeanFactory.class));
    }

}
