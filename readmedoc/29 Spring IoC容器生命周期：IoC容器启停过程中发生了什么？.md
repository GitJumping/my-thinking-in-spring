my.learn.thinking.in.spring.ioc.overview.container.AnnotationApplicationContextAsIoCContainerDemo 为例

**refresh()方法：重新刷新/启动**
```java
public static void main(String[] args) {
    // 创建 BeanFactory 容器
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    // 将当前类 AnnotationApplicationContextAsIoCContainerDemo 作为配置类 Configuration class
    applicationContext.register(AnnotationApplicationContextAsIoCContainerDemo.class);
    // 启动应用上下文
    applicationContext.refresh();
    // 依赖查找集合对象
    lookupCollectionByType(applicationContext);
}
```

org.springframework.context.support.AbstractApplicationContext.refresh() 方法
- 加锁
```java
synchronized (this.startupShutdownMonitor) {}
```
org.springframework.context.support.AbstractApplicationContext.prepareRefresh() 方法
计算启动时间
```java
this.startupDate = System.currentTimeMillis();
```
- 初始化 PropertySources
```java
initPropertySources();
```
- environment，与校验
```java
getEnvironment().validateRequiredProperties();
```
- spring事件
```java
if (this.earlyApplicationListeners == null) {
    this.earlyApplicationListeners = new LinkedHashSet<>(this.applicationListeners);
}
else {
    // Reset local application listeners to pre-refresh state.
    this.applicationListeners.clear();
    this.applicationListeners.addAll(this.earlyApplicationListeners);
}
```

5.2.2.RELEASE 版本
获取 BeanFactory
ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
org.springframework.context.support.AbstractApplicationContext.obtainFreshBeanFactory()
抽象实现
org.springframework.context.support.AbstractApplicationContext.refreshBeanFactory()
抽象实现
org.springframework.context.support.AbstractApplicationContext.getBeanFactory()

org.springframework.context.support.AbstractRefreshableApplicationContext.refreshBeanFactory()
```java
@Override
protected final void refreshBeanFactory() throws BeansException {
    if (hasBeanFactory()) {
        destroyBeans();
        closeBeanFactory();
    }
    try {
        //得到BeanFactory
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        beanFactory.setSerializationId(getId());
        customizeBeanFactory(beanFactory);
        //读取Bean的定义
        loadBeanDefinitions(beanFactory);
        //这里也可能会存在线程不安全，就不是在主流程调用的，而是在其他分支调用的
        synchronized (this.beanFactoryMonitor) {
            this.beanFactory = beanFactory;
        }
    }
    catch (IOException ex) {
        throw new ApplicationContextException("I/O error parsing bean definition source for " + getDisplayName(), ex);
    }
}
```


IoC容器启动（主要阶段）：
1.前期准备工作（记录IoC容器启动时间，校验必要的属性值...）
2.创建一个BeanFactory，注册一些内建的Bean对象或者Bean依赖和内建的非Bean的依赖
3.对BeanFactory进行扩展，通过BeanFactoryPostProcessors 进行操作
4.对Bean的进行扩展，通过BeanPostProcessors 进行操作
5.做一些国际化的资源设置
6.完成一个IoC容器启动事件的广播

IoC容器的停止（主要阶段）：
1.销毁容器里面的所有Bean对象
2.销毁BeanFactory


application相关的context需要调用refresh开启上下文，但是beanfactory是不需要调用的吧
-
作者回复: 你的理解是正确的，BeanFactory 实际上没有很细粒度的生命周期，通常是由 ApplicationContext 来驱动


小马哥，如果容器正在使用中，此时调用refresh会影响当前的容器的行为嘛？或者换句话说，类似zk的这种能够实现bean属性实时更新是不是也是触发的refresh方法
-
作者回复: 这个需要区分情况，如果使用了 Spring Cloud @RefreshScope 的话，Bootstrap 应用上下文会关闭并且重建。其他情况的话，通常不会触发 refresh 方法



BeanFactory，容器自身不属于Bean对象吗？
-
作者回复: 它并不是Bean 对象，但是可以被依赖注入



beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);  视频中提到 “BeanFactory” 可以作为一个非Bean的方式进行注入，小马哥，这里非Bean怎么理解呢？
-
作者回复: 是的，因为它并没有 Bean 名称

