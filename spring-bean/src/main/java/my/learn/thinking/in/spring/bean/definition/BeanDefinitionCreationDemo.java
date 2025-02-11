package my.learn.thinking.in.spring.bean.definition;

import com.sun.org.apache.xml.internal.utils.MutableAttrListImpl;
import my.learn.thinking.in.spring.ioc.overview.domain.User;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.Bean;

/**
 * {@link org.springframework.beans.factory.config.BeanDefinition} 构建示例
 */
public class BeanDefinitionCreationDemo {
    public static void main(String[] args) {
        // 1.通过 BeanDefinitionBuilder 构建
        User user = new User();
        //一般的 BeanDefinition 不是根的，或者最底层的一个Definition
        //如果是底层，就不能有Parent
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
        // 通过属性设置。和XML配置就是一样的
//        beanDefinitionBuilder.addPropertyValue("name", "nc-1");
//        beanDefinitionBuilder.addPropertyValue("id", 1);
        // builder模式链式操作
        beanDefinitionBuilder
                .addPropertyValue("name", "nc-1")
                .addPropertyValue("id", 1);
        // 获取 BeanDefinition 实例
        /*
        这里并没有决定Bean的完整的生命周期，这里只是一个定义
        后续还能去修改
         */
        BeanDefinition beanDefinitiopn = beanDefinitionBuilder.getBeanDefinition();
        // BeanDefinition 并非Bean的最终状态，所以可以自定义修改

        // 2。通过 AbstractBeanDefinition 以及派生类
        /*
        org.springframework.beans.factory.support.BeanDefinitionBuilder.getBeanDefinition
        返回的是 AbstractBeanDefinition 是一个抽象类，不是一个接口

        BeanDefinition，以往的版本，无法进行set操作，后面补充了set操作
        有一些属性的修改，在后续版本做了补充。过去要用到 AbstractBeanDefinition 操作的方法，放到了 BeanDefinition 操作
         */
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        // 设置Bean类型
        genericBeanDefinition.setBeanClass(User.class);
        // 通过 MutablePropertyValues 批量操作属性
        MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
        // 单独设置
//        mutablePropertyValues.addPropertyValue("name", "nc-2");
//        mutablePropertyValues.addPropertyValue("id", 2);
        // builder模式链式操作
        mutablePropertyValues.add("name", "nc-2").add("id", 2);
        // 通过 MutablePropertyValues 批量操作属性
        genericBeanDefinition.setPropertyValues(mutablePropertyValues);
    }
}
