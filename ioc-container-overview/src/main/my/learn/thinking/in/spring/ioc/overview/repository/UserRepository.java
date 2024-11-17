package my.learn.thinking.in.spring.ioc.overview.repository;

import my.learn.thinking.in.spring.ioc.overview.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;

import java.util.Collection;

/**
 * 用户信息仓库
 */
public class UserRepository {
    // 自定义bean
    private Collection<User> users;

    /**
     * 测试获取内部对象
     * 内建非bean对象，（依赖）
     */
    private BeanFactory beanFactory;

//    private ObjectFactory<User> userObjectFactory;
    //就改了一下泛型对象
    private ObjectFactory<ApplicationContext> objectFactory;

    public ObjectFactory<ApplicationContext> getUserObjectFactory() {
        return objectFactory;
    }

    public void setUserObjectFactory(ObjectFactory<ApplicationContext> userObjectFactory) {
        this.objectFactory = userObjectFactory;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public String toString() {
        return "UserRepository{" +
                "users=" + users +
                '}';
    }
}
