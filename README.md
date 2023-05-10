# AdminWhitelist - Allows players who are not on the whitelist to join the server when an admin is online

![Version](https://img.shields.io/github/v/release/LakyLuc/AdminWhitelist?style=flat-square)
![License](https://img.shields.io/badge/license-AGPL%20v3-yellow?style=flat-square)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/iSCYGGiW?style=flat-square)](https://modrinth.com/plugin/adminwhitelist)

## Description
AdminWhitelist is a plugin that allows players who are not whitelisted to join the server, but only if an admin is online.
Players that are not whitelisted will be kicked if no admins are online.
This is useful for servers that want to be open to the public, but don't want to be overrun by griefers.

## Installation
- Put the plugin in the `plugins` folder of your server
- Start the server
- Add players to the admin list with `/adminwhitelist add <UUID>`
- Turn on the whitelist with `/whitelist on`
- Add players to the whitelist with `/whitelist add <player>`
- Reload the whitelist with `/whitelist reload` after making changes

## Commands
`/adminwhitelist add <UUID>` (`/awl add <UUID>`) - Adds a player to the admin list

`/adminwhitelist remove <UUID>` (`/awl remove <UUID>`) - Removes a player from the admin list

`/adminwhitelist list` (`/awl list`) - Lists all players on the admin list

The commands must be run in the server console.

[![Modrinth](modrinth.png)](https://modrinth.com/plugin/adminwhitelist)