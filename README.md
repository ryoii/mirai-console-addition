<div align="center">
   <img width="160" src="http://img.mamoe.net/2020/02/16/a759783b42f72.png" alt="logo"></br>

   <img width="95" src="http://img.mamoe.net/2020/02/16/c4aece361224d.png" alt="title">

----

[![Gitter](https://badges.gitter.im/mamoe/mirai.svg)](https://gitter.im/mamoe/mirai?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

Mirai 是一个在全平台下运行，提供 QQ Android 和 TIM PC 协议支持的高效率机器人框架

这个项目的名字来源于
     <p><a href = "http://www.kyotoanimation.co.jp/">京都动画</a>作品<a href = "https://zh.moegirl.org/zh-hans/%E5%A2%83%E7%95%8C%E7%9A%84%E5%BD%BC%E6%96%B9">《境界的彼方》</a>的<a href = "https://zh.moegirl.org/zh-hans/%E6%A0%97%E5%B1%B1%E6%9C%AA%E6%9D%A5">栗山未来(Kuriyama <b>Mirai</b>)</a></p>
     <p><a href = "https://www.crypton.co.jp/">CRYPTON</a>以<a href = "https://www.crypton.co.jp/miku_eng">初音未来</a>为代表的创作与活动<a href = "https://magicalmirai.com/2019/index_en.html">(Magical <b>Mirai</b>)</a></p>
图标以及形象由画师<a href = "">DazeCake</a>绘制
</div>

# mirai-console-addition
mirai-console的扩展插件，提供对console功能的增强

### 其他插件开发与获取
[插件中心](https://github.com/mamoe/mirai-plugins) 
[mirai-console插件开发快速上手](PluginDocs/ToStart.MD) 

### 使用

将该插件放入`plugins`目录下，并修改`plugins/ConsoleAddition`目录下的配置文件

### 功能一览

+ [x] [MD5密码登录](#md5密码登录)
+ [x] [保存MD5密码，并自动登录](#自动登录)
+ [ ] [多账号登录时管理员共享](#管理员共享) 兼容性问题，暂时关闭该功能
+ [ ] 简易定时任务
+ [ ] 更好的验证码输入方式
+ [ ] Bot智能重启
+ [ ] [欢迎讨论和贡献代码][Issue]



### 全局配置
```yaml
## plugin/ConsoleAddition/main.yml
auto-login: true
md5-login: true
share-manager: true

```

> 设置为false关闭指定子功能


### md5密码登录

`Console Addition`提供了新的Command进行md5登录

```
/login-md5 qq md5
```

> md5密码为32位md5。
> md5密码是QQ的登录方式，相对于明文密码较安全。
> 但md5密码的丢失，依旧会导致QQ被他人登录。

### 自动登录

`Console Addition`提供了新的Command进行自动保存密码，并在下次启动时，对于保存密码的账号进行自动登录

```
/auto-login qq password
```

> 为保留正常登录（不保存密码）的模式，自动登录采用了新的命令作为入口。
> 自动登录保存的是用户的md5密码，保存在plugin/ConsoleAddition/auto-login.yml内

```yaml
## plugin/ConsoleAddition/auto-login.yml

bots:
  '123456789':
    md5: 41D2821CBFC5C789DAC7D18B28EF87BD

  '987654321':
    md5: B6BFAFDA4BA9CCED6846839C6D7B2AD6

```

> 该文件保存自动登录的信息，不建议手动修改

### 管理员共享

管理员是bot的可执行账户。处理可以在console的终端输入命令外，console还支持在群聊、私聊中监听命令。但命令的发送者账号必须具有bot的管理员权限。
通过`manager add [botId] [manageId]`为bot添加管理员，详情查看`/manager`命令。
大多数插件可通过`manager`进行鉴权，但管理员账户时根据单个bot进行配置的。
`管理员共享`则将所有登录的bot进行管理员同步。

```yaml
## plugins/ConsoleAddition/share-manager.yml

managers:
- 142857
- 428571
- 285714

```

> 可在plugins/ConsoleAddition/share-manager.yml中手动添加管理员。
> 也可以在运行过程中通过/manager命令添加管理员。
> 管理员将会被保存在share-manager.yml文件中，下次启动时自动同步



### FAQ.

#### 开发该插件的目的

[Console-Addition][Console-Addition]是对[Mirai-Console][Mirai-Console]功能的扩展，方便插件开发者和使用者。
同时该项目可以作为一个插件开发的例子，供想要对`Mirai`贡献插件的开发者参考。


#### 为什么没有某某某功能

这里，并不实现复杂的逻辑功能。只针对日常开发测试和使用中，能够方便开发者和使用者的功能，进行补充。
如您需要高定制化的功能，可以寻找其他插件，或在[Issue][Issue]中进行讨论。

#### 某某功能会被移除吗

随着[Mirai-core][Mirai-core]和[Mirai-Console][Mirai-Console]的完善，[Console-Addition][Console-Addition]的功能会逐步被取代。



[Console-Addition]: https://github.com/ryoii/mirai-console-addition
[Mirai-core]: https://github.com/mamoe/mirai
[Mirai-Console]: https://github.com/mamoe/mirai-console
[Issue]: https://github.com/ryoii/mirai-console-addition/issues
