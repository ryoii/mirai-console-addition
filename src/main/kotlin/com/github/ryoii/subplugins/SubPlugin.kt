package com.github.ryoii.subplugins

import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.command.CommandSender

interface SubPlugin {

    fun onLoad()

    fun onEnable()

    fun onDisable()

    fun onCommand(command: Command, sender: CommandSender, args: List<String>)
}