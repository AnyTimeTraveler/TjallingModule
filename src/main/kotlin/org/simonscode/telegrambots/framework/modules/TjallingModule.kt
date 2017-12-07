package org.simonscode.telegrambots.framework.modules

import org.simonscode.telegrambots.framework.Bot
import org.simonscode.telegrambots.framework.Module
import org.simonscode.telegrambots.framework.Utils
import org.telegram.telegrambots.api.objects.Update
import java.util.*

class TjallingModule : Module {

    override val name: String
        get() {
            return "TjallingModule"
        }

    override val version: String
        get() {
            return "1.0"
        }

    /**
     * This method will be called when your module has been loaded.
     * The parameter state will contain whatever the method saveState returned last time the bot was shut down.
     *
     * @param state Previously saved state of the module
     */
    override fun setup(state: Any?) {
    }

    /**
     * This method is called every time Telegram has an update and is sent to every module in parallel
     *
     * @param sender    The bot that sent the update
     * @param update The update containing information about what happened
     */
    override fun processUpdate(sender: Bot, update: Update) {
        Utils.checkForCommand(update, "/tjalling", true)?.let {
            val list = AHAPIClient.getInstance().get()
            val first = list[Random().nextInt(list.lastIndex)]
            val second = list[Random().nextInt(list.lastIndex)]
            val third = list[Random().nextInt(list.lastIndex)]
            Utils.send(sender, it, "Tjalling would make dinner from:\n${first.unitSize} of ${first.name()},\n" +
                    "${second.unitSize} of ${second.name()},\n" +
                    "${third.unitSize} of ${third.name()}.")
        }
    }

    /**
     * This method will be called when your module is about to be unloaded.
     * Either it was disabled or the bot is about to shut down.
     *
     * @return Object that contains the state of this module
     */
    override fun saveState(): Any? {
        return null
    }

    override fun getHelpText(args: Array<String>?): String? {
        return "No help text, yet."
    }
}