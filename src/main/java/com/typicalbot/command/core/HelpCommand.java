/**
 * Copyright 2016-2018 Bryan Pikaard & Nicholas Sylke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.typicalbot.command.core;

import com.typicalbot.command.Command;
import com.typicalbot.command.CommandArgument;
import com.typicalbot.command.CommandConfiguration;
import com.typicalbot.command.CommandContext;
import com.typicalbot.shard.Shard;

import java.util.Arrays;
import java.util.stream.Collectors;

@CommandConfiguration(triggers = {"help", "?", "info", "information"})
public class HelpCommand implements Command {
    @Override
    public void execute(CommandContext context, CommandArgument argument) {
        if (!argument.has()) {
            context.sendMessage("Hello there, I'm TypicalBot! I am developed by HyperCoder#2975 and Nick#4490 with the help of their fantastic staff team. If you would like to access my list of commands, try using `b$commands`. If you need any help with commands or settings, you can find documentation at <https://typicalbot.com/documentation>. If you cannot figure out how to use a command or setting, or would like to chat, you can join us in the TypicalBot Lounge at <https://www.typicalbot.com/join-us>.\n\nBuilt upon years of experience, TypicalBot is the ironically-named bot that is far from typical. Stable, lightning fast, and easy to use, TypicalBot is there for you and will seamlessly help you moderate your server and offer some entertaining features for your users, every step of the way.");
            return;
        }

        Command command = Shard.getSingleton().getCommandManager().findCommand(argument.get(0));

        if (command == null) {
            context.sendMessage("That command does not exist.");
            return;
        }

        StringBuilder builder = new StringBuilder();

        builder.append("Triggers » ").append(Arrays.stream(command.getConfiguration().triggers()).collect(Collectors.joining(", "))).append("\n");
        builder.append("Description » ").append(command.getConfiguration().description());

        context.sendMessage(builder.toString());
    }

    @Override
    public void embed(CommandContext context, CommandArgument argument) {
    }
}
