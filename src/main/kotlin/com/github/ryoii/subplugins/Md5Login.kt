package com.github.ryoii.subplugins

import com.github.ryoii.ConsoleAdditionBase
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.console.utils.checkManager
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.utils.FileBasedDeviceInfo
import net.mamoe.mirai.utils.SimpleLogger
import net.mamoe.mirai.utils.io.chunkedHexToBytes

object Md5Login : SubPlugin {

    override val name = "md5-login"
    override var on = false

    override fun onLoad() {
        ConsoleAdditionBase.registerCommand {
            name = "login-md5"
            description = "[console addition]使用md5作为密码登录"
            usage = """
                /login-md5 qq md5
            """.trimIndent()

            onCommand {
                if (this !is ConsoleCommandSender) {
                    sendMessage("请在后台使用该指令")
                    return@onCommand false
                }
                if (it.size < 2) {
                    ConsoleAdditionBase.logger.info("\"/login-md5 qq md5 \" to login a bot")
                    ConsoleAdditionBase.logger.info("\"/login-md5 qq号 qq密码md5 \" 来登录一个BOT")
                    return@onCommand false
                }
                val qqNumber = it[0].toLong()
                val md5 = it[1]
                ConsoleAdditionBase.logger.info("[Bot md5 Login] login...")
                try {
                    MiraiConsole.frontEnd.prePushBot(qqNumber)
                    val bot = Bot(qqNumber, md5.chunkedHexToBytes()) {
                        +FileBasedDeviceInfo
                        this.loginSolver = MiraiConsole.frontEnd.createLoginSolver()
                        this.botLoggerSupplier = {
                            SimpleLogger("[BOT $qqNumber]") { _, message, e ->
                                ConsoleAdditionBase.logger.info("[BOT $qqNumber] $message")
                                if (e != null) {
                                    ConsoleAdditionBase.logger.info("[BOT ERROR $qqNumber] $e")
                                    e.printStackTrace()
                                }
                            }
                        }
                        this.networkLoggerSupplier = {
                            SimpleLogger("BOT $qqNumber") { _, message, e ->
                                ConsoleAdditionBase.logger.info("[NETWORK] $message")
                                if (e != null) {
                                    ConsoleAdditionBase.logger.info("[NETWORK ERROR] $e")
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                    bot.login()
                    bot.subscribeMessages {
                        startsWith("/") { message ->
                            if (bot.checkManager(this.sender.id)) {
                                val sender = ContactCommandSender(this.subject)
                                CommandManager.runCommand(
                                    sender, message
                                )
                            }
                        }
                    }
                    sendMessage("$qqNumber login successes")
                    MiraiConsole.frontEnd.pushBot(bot)
                } catch (e: Exception) {
                    sendMessage("$qqNumber login failed -> " + e.message)
                }
                true
            }
        }
    }

    override fun onEnable() {}
    override fun onDisable() {}
    override fun onCommand(command: Command, sender: CommandSender, args: List<String>) {}
}
