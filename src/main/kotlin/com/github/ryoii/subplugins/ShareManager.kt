package com.github.ryoii.subplugins

import com.github.ryoii.ConsolePlusBase
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.plugins.Config
import net.mamoe.mirai.console.utils.addManager
import net.mamoe.mirai.console.utils.checkManager
import net.mamoe.mirai.console.utils.removeManager

object ShareManager : SubPlugin {

    private lateinit var config: Config
    private lateinit var managers: MutableList<Long>

    override fun onLoad() {
        config = ConsolePlusBase.shareManagerConfig

        managers = if (config.exist("managers")) {
            config.getStringList("managers").mapNotNull { it.toLongOrNull() } as MutableList<Long>
        } else { mutableListOf() }
    }

    override fun onEnable() {
        Bot.forEachInstance {
            for (m in managers) {
                it.addManager(m)
            }
        }
    }

    override fun onDisable() {
        config["managers"] = managers
        config.save()
    }

    override fun onCommand(command: Command, sender: CommandSender, args: List<String>) {
        if (command.name == "manager") {
            when (args[0]) {
                "add" -> {
                    val managerId = args[2].toLong()
                    Bot.forEachInstance {
                        if (!it.checkManager(managerId)) {
                            it.addManager(managerId)
                        }
                    }
                    managers.add(managerId)
                }
                "remove" -> {
                    val managerId = args[2].toLong()
                    Bot.forEachInstance {
                        if (it.checkManager(managerId)) {
                            it.removeManager(managerId)
                        }
                    }
                    managers.remove(managerId)
                }
            }
        }
    }
}