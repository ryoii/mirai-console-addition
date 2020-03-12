package com.github.ryoii.subplugins

import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.utils.addManager
import net.mamoe.mirai.console.utils.checkManager
import net.mamoe.mirai.console.utils.removeManager

object ShareManager : SubPlugin {

    override fun onLoad() {}

    override fun onEnable() {}

    override fun onDisable() {}

    override fun onCommand(command: Command, sender: CommandSender, args: List<String>) {
        if (command.name == "manager") {
            when(args[0]) {
                "add" -> {
                    val managerId = args[2].toLong()
                    Bot.forEachInstance {
                        if (!it.checkManager(managerId)) {
                            it.addManager(managerId)
                        }
                    }
                }
                "remove" -> {
                    val managerId = args[2].toLong()
                    Bot.forEachInstance {
                        if (it.checkManager(managerId)) {
                            it.removeManager(managerId)
                        }
                    }
                }
            }
        }
    }
}