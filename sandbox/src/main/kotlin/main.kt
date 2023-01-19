package local.sandbox

import dev.jeffnyauke.mpesa.kmp.library.core.CoreLib
import kotlinx.coroutines.runBlocking

fun main() {
  val core = CoreLib()
  println(core.sampleApi())
  runBlocking {
    suspendingMain()
  }
}

suspend fun suspendingMain() {
  val core = CoreLib()
  println(core.sampleSuspendApi())
}
