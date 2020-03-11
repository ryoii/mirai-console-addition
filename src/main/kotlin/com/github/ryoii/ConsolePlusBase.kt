package com.github.ryoii



import com.github.ryoii.subplugins.AutoLogin
import com.github.ryoii.subplugins.Md5Login
import net.mamoe.mirai.LowLevelAPI
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.plugins.PluginBase

object ConsolePlusBase : PluginBase() {

    private val subPlugins = listOf(
        Md5Login,
        AutoLogin
    )

    val mainConfig by lazy { loadConfig("main.yml") }
    val autoLoginConfig by lazy { loadConfig("auto-login.yml") }

    override fun onLoad() = subPlugins.forEach { it.onLoad() }

    @LowLevelAPI
    override fun onEnable() {
        subPlugins.forEach { it.onEnable() }
    }

    override fun onDisable() {
        subPlugins.forEach { it.onDisable() }
    }

    override fun onCommand(command: Command, sender: CommandSender, args: List<String>) =
        subPlugins.forEach { it.onCommand(command, sender, args) }

}
