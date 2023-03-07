# 序

通过Nginx和Nginx-Rtmp-Module，实现媒体服务器



# 前期准备

## Nginx

##### 1、下载Nginx

```bash
wget https://nginx.org/download/nginx-1.21.6.tar.gz
```

##### 2、将压缩包移到需要的安装目录下

```bash
mv  nginx-1.21.6.tar.gz  /usr/local
```

##### 3、下载Nginx-Rtmp-Module

```bash
git clone https://github.com/arut/nginx-rtmp-module.git
```

##### 4、将文件移到需要安装目录下

```bash
mv nginx-rtmp-module  /usr/local
```

##### 5、进入目录

```bash
cd /usr/local
```

##### 6、解压Nginx压缩包

```undefined
tar -zxvf nginx-1.21.6.tar.gz
```

##### 7、进入Nginx目录

```bash
cd nginx-1.21.6
```

##### 8、配置

```javascript
./configure --prefix=/usr/local/nginx --add-module=../nginx-rtmp-module --with-http_ssl_module
```

这一步会提示，缺少库，需要安装四个库，具体看报错信息



###### C编译器

在大多数Linux发行版中，C编译器包含在`build-essential`软件包中。你可以尝试在终端中使用以下命令来安装`build-essential`软件包：

```
sudo apt-get update
sudo apt-get install build-essential
```

这将更新你的包索引并安装编译器及其他相关工具。如果你使用的是其他Linux发行版，可以查阅相关文档或在终端中搜索相应的软件包名来安装C编译器。



###### PCRE库

这个错误信息指出，nginx需要PCRE库才能启用HTTP rewrite模块。你可以选择使用`--without-http_rewrite_module`选项来禁用rewrite模块，或者安装PCRE库到系统中，或者使用`--with-pcre=<path>`选项来静态构建PCRE库。

如果你想安装PCRE库，可以使用以下命令在Ubuntu系统上安装：

```
sudo apt-get update
sudo apt-get install libpcre3-dev
```

如果你使用的是其他Linux发行版，请根据其文档或使用相应的软件包管理器来安装libpcre3-dev库。

如果你已经安装了PCRE库但仍然遇到此错误，请检查PCRE库的路径是否正确，并使用`--with-pcre=<path>`选项重新运行`configure`脚本。



###### OpenSSL

这个错误信息指出，nginx需要OpenSSL库才能启用SSL模块。你可以选择不启用SSL模块，或者将OpenSSL库安装到系统中，或者使用`--with-openssl=<path>`选项从源代码中静态构建OpenSSL库。

如果你想安装OpenSSL库，可以使用以下命令在Ubuntu系统上安装：

```
sudo apt-get update
sudo apt-get install libssl-dev
```

如果你使用的是其他Linux发行版，请根据其文档或使用相应的软件包管理器来安装libssl-dev库。

如果你已经安装了OpenSSL库但仍然遇到此错误，请检查OpenSSL库的路径是否正确，并使用`--with-openssl=<path>`选项重新运行`configure`脚本。



###### zlib

这个错误信息指出，nginx需要zlib库才能启用HTTP gzip模块。你可以选择使用`--without-http_gzip_module`选项来禁用gzip模块，或者将zlib库安装到系统中，或者使用`--with-zlib=<path>`选项从源代码中静态构建zlib库。

如果你想安装zlib库，可以使用以下命令在Ubuntu系统上安装：

```
sudo apt-get update
sudo apt-get install zlib1g-dev
```

如果你使用的是其他Linux发行版，请根据其文档或使用相应的软件包管理器来安装zlib1g-dev库。

如果你已经安装了zlib库但仍然遇到此错误，请检查zlib库的路径是否正确，并使用`--with-zlib=<path>`选项重新运行`configure`脚本。

##### 9、安装

```go
make && make install
```

##### 10、配置 nginx.conf 文件（/usr/local/nginx/conf下）

注意nginx.conf默认是只读文件

使用chmod 777或者sudo nano等

```python
#user  nobody;
# multiple workers works !
worker_processes  2;
 
#pid        logs/nginx.pid;
events {
    worker_connections  8192;
}
 
rtmp {
    server {
        listen 1935;
        chunk_size 4000;
        application live {
             live on;
 
             record all;
             record_path /tmp/av;
             record_max_size 1K;
 
             record_unique on;
             allow publish all;
             deny publish all;
 
             allow play all;
        }
    }
}
 
http {
    include       mime.types;
    default_type  application/octet-stream;
  
    sendfile        off;
 
    server_names_hash_bucket_size 128;
 
    client_body_timeout   10;
    client_header_timeout 10;
    keepalive_timeout     30;
    send_timeout          10;
    keepalive_requests    10;
    #gzip  on; 
    server {
        listen       8080;
        server_name  localhost;
 
 
        location /stat {
            rtmp_stat all;
            rtmp_stat_stylesheet stat.xsl;
        }
        location /stat.xsl {
            root nginx-rtmp-module/;
        }
        location /control {
            rtmp_control all;
        }
 
        location / {
            root   html;
            index  index.html index.htm;
        }
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }
}
```



###### 解释上述代码

这是一个 Nginx 配置文件，其中使用了 Nginx-RTMP 模块，该模块可以实现音视频流的传输。下面是代码各部分的解释：

```
#user  nobody;
# multiple workers works !
worker_processes  2;
```

这部分代码设置 Nginx 运行时所使用的用户和进程数量，这里设置为两个进程。

```
events {
    worker_connections  8192;
}
```

这部分代码设置 Nginx 的事件模块，指定了每个进程的最大连接数为 8192。

```
rtmp {
    server {
        listen 1935;
        chunk_size 4000;
        application live {
             live on;
 
             record all;
             record_path /tmp/av;
             record_max_size 1K;
 
             record_unique on;
             allow publish all;
             deny publish all;
 
             allow play all;
        }
    }
}
```

这部分代码定义了 Nginx-RTMP 模块的配置，指定监听端口为 1935，定义了名为 live 的应用程序，并开启直播流功能，以及录制功能，录制的视频将保存在 `/tmp/av` 目录下，单个录制文件的最大大小为 1K。此外，还允许所有用户发布流和播放流。

```
http {
    include       mime.types;
    default_type  application/octet-stream;
  
    sendfile        off;
 
    server_names_hash_bucket_size 128;
 
    client_body_timeout   10;
    client_header_timeout 10;
    keepalive_timeout     30;
    send_timeout          10;
    keepalive_requests    10;
    #gzip  on; 
    server {
        listen       8080;
        server_name  localhost;
 
 
        location /stat {
            rtmp_stat all;
            rtmp_stat_stylesheet stat.xsl;
        }
        location /stat.xsl {
            root nginx-rtmp-module/;
        }
        location /control {
            rtmp_control all;
        }
 
        location / {
            root   html;
            index  index.html index.htm;
        }
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }
}
```

这部分代码定义了 HTTP 模块的配置，指定了监听端口为 8080，定义了多个 URL 路径的访问方式。例如：

- `/stat`：获取 RTMP 相关的统计信息，需要使用 `stat.xsl` 样式表渲染；
- `/stat.xsl`：指定 `stat.xsl` 样式表所在的位置；
- `/control`：可以控制 RTMP 服务器的某些行为；
- `/`：定义了根目录，指定了访问 HTML 页面的方式。

此外，还设置了一些连接超时等参数，并定义了错误页面的访问方式。



另外**值得注意**的是，这段配置文件还使用了一些 Nginx 的指令，例如：

- `include mime.types`：包含了 Nginx 的 MIME 类型定义文件；
- `default_type application/octet-stream`：设置默认的 MIME 类型为 `application/octet-stream`，这是二进制流的通用 MIME 类型，用于处理未知的 MIME 类型；
- `sendfile off`：禁用 sendfile 机制，该机制可以提高文件传输效率，但在一些特殊情况下可能会导致问题；
- `server_names_hash_bucket_size 128`：设置 server name 的 hash 表桶的数量；
- `gzip on`：启用 gzip 压缩，可以减小 HTTP 响应的大小，提高传输效率。

总之，这段配置文件主要是为了实现 Nginx-RTMP 模块的功能，以及定义 HTTP 模块的一些配置参数。通过这些配置，可以搭建一个支持音视频直播的服务器。

11、启动Nginx

```bash
cd /usr/local/nginx/sbin
./nginx -t
./nginx -s reload
```

启动时可能会包错：nginx: [error] invalid PID number "" in "/usr/local/nginx/logs/nginx.pid"
解决：

```bash
/usr/local/nginx/sbin/nginx -c /usr/local/nginx/conf/nginx.conf
```

12、测试（注意打开服务器8080和1935端口安全组）
访问服务器外网 IP：8080





## pom依赖

        <!--直播相关依赖-->
        <!--javacv-->
        <dependency>
            <groupId>org.bytedeco</groupId>
            <artifactId>javacv-platform</artifactId>
            <version>1.5.5</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.bytedeco.javacpp-presets/opencv-platform -->
        <!--opencv-->
        <dependency>
            <groupId>org.bytedeco.javacpp-presets</groupId>
            <artifactId>opencv-platform</artifactId>
            <version>4.0.1-1.4.4</version>
        </dependency>



# 代码概述

直播推流实现类

直播推流启动类

直播拉流实现类（里面关于mat的import和格式转化有问题

直播拉流启动类



# 实现情景

## 情景1（推荐）

系统给直播者url和推流码

直播者通过obs等软件，自行推流



用户在网页点击进入直播房间

跳转页面，其中的网页播放器拉取对应的主播的url（url存在数据库或缓存）



## 情景2

系统在直播后台，实现点击直播

系统通过自身代码逻辑，完成推流的流程



用户同情景1