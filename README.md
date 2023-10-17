# netty 集成 spring mvc

将netty的HttpRequest和HttpResponse对象与servlet的对象进行相互转换。我采用了springtest提供的用来模拟HttpServletRequest和HttpServletResponse的类，这两个类也是它们的子类。
org.springframework.mock.web.MockHttpServletRequest;
org.springframework.mock.web.MockHttpServletResponse;

#### 项目功能

- 支持spring mvc的controller中，注解方式获取参数
- 支持返回json
- 支持post请求

#### 如何测试

- 下载项目，启动org.springframework.sandbox.netty.MyServer
- 浏览器访问 http://localhost:8080/hello/foo, 如果返回结果，表示启动成功

#### 如何打包并发布

- 使用maven打包：mvn package
- 启动jar：java -jar target/Netty-SpringMVC-1.0.0-SNAPSHOT.jar
- 浏览器访问 http://localhost:8080/hello/foo, 如果返回结果，表示启动成功

#### 如何添加自己的业务

- 在org.springframework.sandbox.mvc.TestController中，你可以添加自己的方法

#### 待添加的功能

- 热部署(Hot deployment)
- 集群(cluster)

#### 版本信息

- 0.0.2 : 集成mybatis
- 0.0.1 : 实现netty+springMVC的集成