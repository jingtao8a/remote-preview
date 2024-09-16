### 后端部署

1. 安装 jdk1.8
2. 安装 mysql 8
3. 安装 redis
4. 安装ffmpeg
5. 安装Another Redis Desktop Manager

```shell
用 init.sql初始化数据库
```
修改application.properties中的属性
```
#项目目录
project.folder=D:/remote-preview

#资源路径
remote.preview.material=D:/video-materials
```
依次运行RemotePreviewApplicationTests三个test载入数据后，启动

***
### 使用技术
> SpringBoot, mybatis, redis, mysql, ffmpeg

