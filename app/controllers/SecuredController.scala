package controllers

import play.api.mvc._
import play.api.mvc.BodyParsers.parse
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

trait SecuredController extends Controller {

  private val corsHeaders = {
    Seq("Access-Control-Allow-Origin" -> "*",
      "Access-Control-Allow-Headers" -> "Origin, X-Requested-With, Content-Type, Accept, Authorization, api_key",
      // cache access control response for one day
      "Access-Control-Max-Age" -> (60 * 60 * 24).toString)
  }

  def CrossOriginResource[A](bp: BodyParser[A] = parse.empty)(f: Request[A] => Future[Result]): Action[A] = {
    Action.async(bp) { request =>
      f(request).map(_.withHeaders(corsHeaders: _*))
    }
  }

}
