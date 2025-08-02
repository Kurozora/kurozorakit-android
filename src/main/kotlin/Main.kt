package kurozora.kit

import kurozora.kit.core.KurozoraApi
import kurozora.kit.core.KurozoraKit

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
suspend fun main() {
    val name = "Kotlin"
    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    println("Hello, $name!")

    for (i in 1..5) {
        //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
        // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
        println("i = $i")
    }

    val kurozoraKit = KurozoraKit.Builder().apiEndpoint(KurozoraApi.V1.baseUrl).apiKey("key_goes_here").build()

    val show1 = kurozoraKit.show().getShow("1")
    show1.onSuccess { show ->
        println(show)
    }

    kurozoraKit.show().getShows().onSuccess { shows ->
        println(shows.size)
    }
}
