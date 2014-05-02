package controllers

import org.scalatest._
import org.scalatest.Inside._
import play.api.test.Helpers._
import play.api.test._
import play.api.libs.Crypto
import play.api.libs.json._

class AuthControllerSpec extends WordSpec with Matchers {

  "AuthController" should {

    "authenticate user successfully" in new WithApplication {
      val body: JsValue = Json.obj("username" -> "user-1", "password" -> "pass")

      inside(route(FakeRequest(POST, "/auth").withJsonBody(body))) {
        case Some(authRoute) =>
          status(authRoute) shouldBe OK
          contentType(authRoute) shouldBe Some("application/json")
          val jsonContent: JsObject = contentAsJson(authRoute).asInstanceOf[JsObject]
          jsonContent.fields.length shouldBe 1

          inside(jsonContent.value.get("token")) {
            case Some(JsString(token)) =>
              inside(Crypto.extractSignedToken(token)) {
                case Some(tokenUsername) => tokenUsername shouldBe "user-1"
              }
          }
      }
    }

    "not authenticate with invalid body" in new WithApplication {
      val body: JsValue = Json.obj("username" -> "user-10")

      inside(route(FakeRequest(POST, "/auth").withJsonBody(body))) {
        case Some(authRoute) =>
          status(authRoute) shouldBe BAD_REQUEST
          contentType(authRoute) shouldBe Some("application/json")
          contentAsJson(authRoute) shouldBe Json.obj("error" -> "Username or password not supplied.")
      }
    }

    "not authenticate with invalid username" in new WithApplication {
      val body: JsValue = Json.obj("username" -> "test1", "password" -> "pass")
      inside(route(FakeRequest(POST, "/auth").withJsonBody(body))) {
        case Some(authRoute) =>
          status(authRoute) shouldBe UNAUTHORIZED
          contentType(authRoute) shouldBe Some("application/json")
          contentAsJson(authRoute) shouldBe Json.obj("error" -> "Username doesn't exist.")
      }
    }

    "not authenticate with invalid password" in new WithApplication {
      val body: JsValue = Json.obj("username" -> "user-2", "password" -> "pass2")
      inside(route(FakeRequest(POST, "/auth").withJsonBody(body))) {
        case Some(authRoute) =>
          status(authRoute) shouldBe UNAUTHORIZED
          contentType(authRoute) shouldBe Some("application/json")
          contentAsJson(authRoute) shouldBe Json.obj("error" -> "Password is incorrect.")
      }
    }

    "verify a valid token" in new WithApplication {
      val request = FakeRequest(POST, "/verify").withJsonBody(Json.obj("token" -> Crypto.signToken("user-3")))

      inside(route(request)) {
        case Some(result) =>
          status(result) shouldBe OK
          contentType(result) shouldBe Some("application/json")
          contentAsJson(result) shouldBe Json.obj("username" -> "user-3")
      }
    }

    "not verify an invalid token" in new WithApplication {
      val request = FakeRequest(POST, "/verify").withJsonBody(Json.obj("token" -> "FAKETOKEN"))

      inside(route(request)) {
        case Some(result) =>
          status(result) shouldBe UNAUTHORIZED
          contentType(result) shouldBe Some("application/json")
          contentAsJson(result) shouldBe Json.obj("error" -> "Token is not valid.")
      }
    }

    "not verify an invalid request" in new WithApplication {
      val request = FakeRequest(POST, "/verify").withJsonBody(Json.obj("hello" -> "world"))

      inside(route(request)) {
        case Some(result) =>
          status(result) shouldBe BAD_REQUEST
          contentType(result) shouldBe Some("application/json")
          contentAsJson(result) shouldBe Json.obj("error" -> "Token not supplied.")
      }
    }
  }
}
