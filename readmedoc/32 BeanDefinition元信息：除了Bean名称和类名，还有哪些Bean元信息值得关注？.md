究竟应该怎么理解bean这个概念呢？所有被容器托管的对象实例就叫bean?而java bean与spring bean的区别又是什么呢？
-
作者回复: 广义地来看，Java Bean 是一个表现形式，而 Spring Bean 是狭义地托管在 Spring 容器中的 Java Bean

springIOC 是管理对象的。一个普通的java对象由用户创建，被GC销毁。Spring接管之后，给这个类加了元信息，生成一个Beandifinition，创建销毁都由springIOC控制，在spring内部使用的其实是Beandifinition。就像一个人进了一家公司之后，会给这个人安排工号，工位，岗位，上下班也由这家公司决定。人还是那个人，在公司内部多了些属性，离了这家公司，这些属性就没用了。可以这样理解吗
-
作者回复: 属性还是存在的，而是从属关系不存在了

在mybatis的源码中 是通过spring中的那个扫描器，根据@mapper去扫描有这个注解的接口，并且返回了BeanDefinition 为什么这里可以为接口生成BeanDefinition呢？
-
作者回复: 你可以忽视了一个细节，@Mapper 标注的接口实际的对象是动态代理，BeanDefinition 也可以关联具体实例，也就是动态代理。

这个 BeanDefinition 除了是可以设置bean的元信息之外，设置完所存在的作用和意义是什么？
-
作者回复: BeanDefinition 是 Bean 实例化和初始化的依据，在未来的 Bean 生命周期中会给出两者之间的关系。简单地说，BeanDefinition  提供了 Bean 所属的类型，作用范围以及关联的属性等。

简单给大家提供个参考：
```java
一、IoC 容器启动时，关于 BeanDifinition 有三个过程
1. BeanDifinition 的 Resource 定位
2. BeanDifinition 的载入与解析
3. BeanDifinition 在 Ioc 容器中的注册

二、下面的代码结合老师的示例代码，即可生成 bean 实例，看起来会直观一些
//实例化一个容器
DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
//注册 beanDefinition
beanFactory.registerBeanDefinition("user", beanDefinition);
//从容器里获取或创建 bean 实例
User user = beanFactory.getBean("user", User.class);
System.out.println(user);
```

为啥build不返回GenericBeanDefinition，返回抽象类好处在哪里？
-
作者回复: 主要是不要绑定在特定的类上，除非自己特别清楚