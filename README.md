# TjallingModule
This is a Module for my [Telegram Bot Framework](https://github.com/Simon70/telegrambots.framework).

## Description
This module gets a list of products from Albert Hijn and selects three random products and produces an injoke.

## Setup
1. In your src-folder, remove the folder in which language you **don't** want to write.
2. Write your module
3. Put your full package- and class-name in the file under `resources/META-INF/services/`
4. Make sure it's correctly formatted, like the template classes
5. Run `gradle shadowJar`
6. Run the test-class with your IDE or run the framework manually


Run `gradle shadowJar` to create a jar from your module.

If you get an error during the intial run, please follow the Setup guide [here](https://github.com/Simon70/telegrambots.framework)

The jar will be put into a new directory called `_working_directory` to easily distinguish files that belong to the project from files that have been created by the bot.

You cannot run the module without the BotController, so there is a test-class that basically just proxies the BotController to make it easier to debug with IntelliJ.
