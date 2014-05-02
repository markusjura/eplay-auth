package utils

import play.api.Play.current
import scala.concurrent.{ Future, Promise }
import scala.concurrent.duration._
import play.api.libs.concurrent.Akka
import play.api.libs.concurrent.Execution.Implicits._
import scala.util.Try

object AsyncHelper {

  def delayedFuture[A](future: Future[A])(implicit duration: FiniteDuration): Future[A] = {
    val delayedPromise = Promise[A]()
    future.onComplete { completion: Try[A] =>
      Akka.system.scheduler.scheduleOnce(duration) {
        delayedPromise.complete(completion)
      }
    }
    delayedPromise.future
  }
}
