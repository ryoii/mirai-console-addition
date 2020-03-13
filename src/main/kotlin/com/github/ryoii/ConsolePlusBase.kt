package com.github.ryoii



import com.github.ryoii.subplugins.AutoLogin
import com.github.ryoii.subplugins.Md5Login
import com.github.ryoii.subplugins.ShareManager
import net.mamoe.mirai.LowLevelAPI
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.plugins.PluginBase

object ConsolePlusBase : PluginBase() {

    private val subPlugins = listOf(
        Md5Login,
        AutoLogin,
        ShareManager
    )

    val mainConfig by lazy { loadConfig("main.yml") }
    val autoLoginConfig by lazy { loadConfig("auto-login.yml") }
    val shareManagerConfig by lazy { loadConfig("share-manager.yml") }

    override fun onLoad() {
        subPlugins.forEach {
            if (!mainConfig.exist(it.name) || mainConfig.getBoolean(it.name))  {
                it.on = true
                it.onLoad()
            }
        }
    }

    @LowLevelAPI
    override fun onEnable() {
        subPlugins.forEach {
            if (it.on) {
                it.onEnable()
            }
        }
    }

    override fun onDisable() {
        subPlugins.forEach {
            if (it.on) {
                it.onDisable()
            }
            mainConfig[it.name] = it.on
        }
        mainConfig.save()
    }

    override fun onCommand(command: Command, sender: CommandSender, args: List<String>) =
        subPlugins.forEach {
            if (it.on) {
                it.onCommand(command, sender, args)
            }
        }

}
