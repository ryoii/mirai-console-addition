package com.github.ryoii.subplugins

import com.github.ryoii.ConsolePlusBase
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.ConsoleCommandSender
import net.mamoe.mirai.console.command.registerCommand
import net.mamoe.mirai.console.plugins.ConfigSectionImpl
import net.mamoe.mirai.utils.MiraiPlatformUtils
import net.mamoe.mirai.utils.io.toUHexString

object AutoLogin : SubPlugin {

    private val bots = mutableMapOf<Long, String>()

    override fun onLoad() {
        registerCommand {
            name = "auto-login"
            description = "自动保存本次登录的qq号和密码md5值，下次启动时自动登录"
            onCommand {
                if (this !is ConsoleCommandSender) {
                    sendMessage("请在后台使用该指令")
                    return@onCommand false
                }
                if (it.size < 2) {
                    MiraiConsole.logger("/auto-login qqnumber qqpassword  to login a bot")
                    MiraiConsole.logger("/auto-login qq号 qq密码  来登录一个BOT")
                    return@onCommand false
                }
                try {
                    val qq = it[0].toLong()
                    val pwd = it[1]
                    ConsolePlusBase.launch {
                        MiraiConsole.CommandProcessor.runConsoleCommand("/login $qq $pwd")
                        bots[qq] = ""
                    }
                    true
                } catch (e: Exception) {
                    ConsolePlusBase.logger.error("请输入正确格式的QQ号和密码")
                    false
                }
            }
        }

        val setting = ConsolePlusBase.autoLoginConfig
        val conf = if (setting.exist("bots")) {
            setting.getConfigSection("bots")
        } else {
            ConfigSectionImpl().also { setting["bots"] = it }
        }

        conf.keys.forEach {
            bots[it.toLong()] = conf.getConfigSection(it).getString("pwd")
        }

        ConsolePlusBase.logger.info("读取了${bots.size}个bot信息")
    }

    override fun onEnable() {
        bots.forEach { (qq, md5) ->
            ConsolePlusBase.logger.info("正在自动登录$qq")
            ConsolePlusBase.launch {
                MiraiConsole.CommandProcessor.runConsoleCommand("/login-md5 $qq $md5")
            }
        }
    }

    override fun onDisable() {
        val botsSection = ConsolePlusBase.autoLoginConfig.getConfigSection("bots")

        bots.forEach { (qq, md5) ->
            if (md5.isNotEmpty()) {
                botsSection[qq.toString()] = ConfigSectionImpl().also {
                    it["md5"] = md5
                }
            }
        }

        ConsolePlusBase.autoLoginConfig.save()
    }

    override fun onCommand(command: Command, sender: CommandSender, args: List<String>) {
        if (command.name == "login" && bots.containsKey(args[0].toLong())) {
            bots[args[0].toLong()] = MiraiPlatformUtils.md5(args[1]).toUHexString("")
            ConsolePlusBase.logger.info("${args[0]}自动登录成功")
        }
    }
}