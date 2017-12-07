package org.simonscode.telegrambots.framework.modules

import java.util.*

object Test {
    @JvmStatic
    fun main(args: Array<String>) {
        val list = AHAPIClient.getInstance().get()
        val first = list[Random().nextInt(list.lastIndex)]
        val second = list[Random().nextInt(list.lastIndex)]
        val third = list[Random().nextInt(list.lastIndex)]
        println("Tjalling would make food from:\n${first.unitSize} of ${first.name()},\n" +
                "${second.unitSize} of ${second.name()},\n" +
                "${third.unitSize} of ${third.name()}.")
    }
}
