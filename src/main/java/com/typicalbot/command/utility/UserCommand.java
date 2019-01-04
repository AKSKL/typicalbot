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
package com.typicalbot.command.utility;

import com.typicalbot.command.Command;
import com.typicalbot.command.CommandArgument;
import com.typicalbot.command.CommandConfiguration;
import com.typicalbot.command.CommandContext;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

import java.util.List;
import java.util.stream.Collectors;

@CommandConfiguration(triggers = {"user", "userinfo", "uinfo"}, description = "Displays a user's information.", embed = true)
public class UserCommand implements Command {
  @Override
  public void execute (CommandContext context, CommandArgument argument) {

    Member mentionedMember = getMember(context.getMessage());
    User mentionedUser = mentionedMember.getUser();

    String header = "**__User information for:__** " + mentionedUser.getName() + "\n";
    String name = "Name                : " + mentionedUser.getName() + "#" + mentionedUser.getDiscriminator() + " (" + mentionedUser.getId() + ")\n";
    //TODO(AKSKL): Make status prettier
    String status = "Status              : " + mentionedMember.getOnlineStatus().toString() + "\n";
    String presence = (mentionedMember.getGame() == null ? "" : "Playing             : " + mentionedMember.getGame().getName() + "\n");
    String nickname = (mentionedMember.getNickname() == null ? "" : "Nickname            : " + mentionedMember.getNickname() + "\n");
    //TODO(AKSKL): Make join times prettier
    String joinDate = "Joined Discord      : " + mentionedUser.getCreationTime() + "\n";
    String serverJoinDate = "Joined Server       : " + mentionedMember.getJoinDate() + "\n";
    String icon = "Avatar URL          : " + mentionedUser.getAvatarUrl() + "\n";
    context.sendMessage(header + "```" + name + status + presence + nickname + joinDate + serverJoinDate + icon + "```");
  }

  @Override
  public void embed (CommandContext context, CommandArgument argument) {

    //TODO(nsylke): Format embed such that "Joined Discord" and "Joined Server" are always adjacent.

    Member mentionedMember = getMember(context.getMessage());
    User mentionedUser = mentionedMember.getUser();

    String header = "User information for: " + mentionedUser.getName();
    String tag = mentionedUser.getName() + "#" + mentionedUser.getDiscriminator();
    String id = mentionedUser.getId();
    String status = mentionedMember.getOnlineStatus().toString();
    String joinDate = mentionedUser.getCreationTime().toString();
    String serverJoinDate = mentionedMember.getJoinDate().toString();

    List<Role> rolesList = mentionedMember.getRoles();
    String roles = rolesList.stream().map(Role::getName).collect(Collectors.joining(", "));

    EmbedBuilder embed = new EmbedBuilder()
            .setTitle(header)
            .addField("» Tag", tag, true)
            .addField("» ID", id, true)
            //TODO(AKSKL): Make status prettier
            .addField("» Status", status, true);
    if (mentionedMember.getGame() != null) {
      String presence = mentionedMember.getGame().getName();
      embed.addField("» Playing", presence, true);
    }
    if (mentionedMember.getNickname() != null) {
      String nickname = mentionedMember.getNickname();
      embed.addField("» Nickname", nickname, true);
    }
    embed
            .addField("» Joined Discord", joinDate, true)
            .addField("» Joined Server", serverJoinDate, true)
            .addField("» Roles", roles, false)
            .setThumbnail(mentionedUser.getAvatarUrl())
            //TODO(nsylke): make the TB icon a constant
            .setFooter("TypicalBot", "https://images-ext-2.discordapp.net/external/qYPuNcjM4PjaKvsmlc-lcHhtJ8RZ-txaxYMDQmWL0g8/https/www.typicalbot.com/x/images/icon.png");

    context.sendEmbed(embed.build());
  }

  public Member getMember (Message message) {
    if (message.getMentionedMembers().isEmpty()) {
      return message.getMember();
    } else {
      return message.getMentionedMembers().get(0);
    }
  }
}
