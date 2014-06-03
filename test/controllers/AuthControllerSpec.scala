package controllers

import org.scalatest._
import org.scalatest.Inside._
import org.scalatestplus.play._
import play.api.test.Helpers._
import play.api.test._
import play.api.libs.json._
import play.api.libs.Crypto

class AuthControllerSpec extends PlaySpec with OneAppPerSuite {

  "AuthController" must {

    "authenticate user successfully" in {
      val body: JsValue = Json.obj("username" -> "user-1", "password" -> "pass")

      inside(route(FakeRequest(POST, "/auth").withJsonBody(body))) {
        case Some(authRoute) =>
          status(authRoute) mustBe OK
          contentType(authRoute) mustBe Some("application/json")
          val jsonContent: JsObject = contentAsJson(authRoute).asInstanceOf[JsObject]
          jsonContent.fields.length mustBe 1

          inside(jsonContent.value.get("token")) {
            case Some(JsString(token)) =>
              inside(Crypto.extractSignedToken(token)) {
                case Some(tokenUsername) => tokenUsername mustBe "user-1"
              }
          }
      }
    }

    "not authenticate with invalid body" in {
      val body: JsValue = Json.obj("username" -> "user-10")

      inside(route(FakeRequest(POST, "/auth").withJsonBody(body))) {
        case Some(authRoute) =>
          status(authRoute) mustBe BAD_REQUEST
          contentType(authRoute) mustBe Some("application/json")
          contentAsJson(authRoute) mustBe Json.obj("error" -> "Username or password not supplied.")
      }
    }

    "not authenticate with invalid username" in {
      val body: JsValue = Json.obj("username" -> "test1", "password" -> "pass")
      inside(route(FakeRequest(POST, "/auth").withJsonBody(body))) {
        case Some(authRoute) =>
          status(authRoute) mustBe UNAUTHORIZED
          contentType(authRoute) mustBe Some("application/json")
          contentAsJson(authRoute) mustBe Json.obj("error" -> "Username doesn't exist.")
      }
    }

    "not authenticate with invalid password" in {
      val body: JsValue = Json.obj("username" -> "user-2", "password" -> "pass2")
      inside(route(FakeRequest(POST, "/auth").withJsonBody(body))) {
        case Some(authRoute) =>
          status(authRoute) mustBe UNAUTHORIZED
          contentType(authRoute) mustBe Some("application/json")
          contentAsJson(authRoute) mustBe Json.obj("error" -> "Password is incorrect.")
      }
    }

    "verify a valid token" in {
      val request = FakeRequest(POST, "/verify").withJsonBody(Json.obj("token" -> Crypto.signToken("user-3")))

      inside(route(request)) {
        case Some(result) =>
          status(result) mustBe OK
          contentType(result) mustBe Some("application/json")
          contentAsJson(result) mustBe Json.obj("username" -> "user-3")
      }
    }

    "not verify an invalid token" in {
      val request = FakeRequest(POST, "/verify").withJsonBody(Json.obj("token" -> "FAKETOKEN"))

      inside(route(request)) {
        case Some(result) =>
          status(result) mustBe UNAUTHORIZED
          contentType(result) mustBe Some("application/json")
          contentAsJson(result) mustBe Json.obj("error" -> "Token is not valid.")
      }
    }

    "not verify an invalid request" in {
      val request = FakeRequest(POST, "/verify").withJsonBody(Json.obj("hello" -> "world"))

      inside(route(request)) {
        case Some(result) =>
          status(result) mustBe BAD_REQUEST
          contentType(result) mustBe Some("application/json")
          contentAsJson(result) mustBe Json.obj("error" -> "Token not supplied.")
      }
    }
  }
}
