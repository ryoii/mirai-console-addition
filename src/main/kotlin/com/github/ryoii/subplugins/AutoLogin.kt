@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.github.ryoii.subplugins

import com.github.ryoii.ConsoleAdditionBase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.ConsoleCommandSender
import net.mamoe.mirai.console.command.registerCommand
import net.mamoe.mirai.console.plugins.ConfigSectionImpl
import java.security.MessageDigest

object AutoLogin : SubPlugin {

    override val name = "auto-login"
    override var on = false

    private val bots = mutableMapOf<Long, String>()

    override fun onLoad() {
        ConsoleAdditionBase.registerCommand {
            name = "auto-login"
            description = "[console addition]自动保存本次登录的qq号和密码md5值，下次启动时自动登录"
            usage = """
                自动保存本次登录的qq号和密码md5值，下次启动时自动登录
                /auto-login qq password
            """.trimIndent()

            onCommand {
                if (this !is ConsoleCommandSender) {
                    sendMessage("请在后台使用该指令")
                    return@onCommand false
                }
                if (it.size < 2) {
                    ConsoleAdditionBase.logger.info("/auto-login qqnumber qqpassword  to login a bot")
                    ConsoleAdditionBase.logger.info("/auto-login qq号 qq密码  来登录一个BOT")
                    return@onCommand false
                }
                try {

                    val qq = it[0].toLong()
                    val pwd = it[1]
                    withContext(ConsoleAdditionBase.coroutineContext) {
                        bots[qq] = ""
                        ConsoleAdditionBase.runCommand("/login $qq $pwd").await()
                    }

                } catch (e: Exception) {
                    ConsoleAdditionBase.logger.error("请输入正确格式的QQ号和密码")
                    false
                }
            }
        }

        val setting = ConsoleAdditionBase.autoLoginConfig
        val conf = if (setting.exist("bots")) {
            setting.getConfigSection("bots")
        } else {
            ConfigSectionImpl().also { setting["bots"] = it }
        }

        conf.keys.forEach {
            ConsoleAdditionBase.logger.info(it)
            bots[it.toLong()] = conf.getConfigSection(it).getString("md5")
        }

        ConsoleAdditionBase.logger.info("读取了${bots.size}个bot信息")
    }

    override fun onEnable() {
        bots.forEach { (qq, md5) ->
            ConsoleAdditionBase.logger.info("正在自动登录$qq")
            ConsoleAdditionBase.launch {
                val success = ConsoleAdditionBase.runCommand("/login-md5 $qq $md5")
                if (success.await()) {
                    ConsoleAdditionBase.logger.info("$qq 自动登录成功")
                } else {
                    ConsoleAdditionBase.logger.info("$qq 自动登录失败")
                }
            }
        }
    }

    override fun onDisable() {
        val botsSection = ConsoleAdditionBase.autoLoginConfig.getConfigSection("bots")

        bots.forEach { (qq, md5) ->
            if (md5.isNotEmpty()) {
                botsSection[qq.toString()] = ConfigSectionImpl().also {
                    it["md5"] = md5
                }
            }
        }

        ConsoleAdditionBase.autoLoginConfig.save()
    }

    override fun onCommand(command: Command, sender: CommandSender, args: List<String>) {
        if (command.name == "login" && bots.containsKey(args[0].toLong())) {
            bots[args[0].toLong()] = MessageDigest.getInstance("MD5").apply { update(args[1].toByteArray()) }.digest().toUHexString("")
        }
    }
}

@JvmOverloads
@Suppress("DuplicatedCode") // false positive. foreach is not common to UByteArray and ByteArray
internal fun ByteArray.toUHexString(separator: String = " ", offset: Int = 0, length: Int = this.size - offset): String {
    if (length == 0) {
        return ""
    }
    val lastIndex = offset + length
    return buildString(length * 2) {
        this@toUHexString.forEachIndexed { index, it ->
            if (index in offset until lastIndex) {
                var ret = it.toUByte().toString(16).toUpperCase()
                if (ret.length == 1) ret = "0$ret"
                append(ret)
                if (index < lastIndex - 1) append(separator)
            }
        }
    }
}
