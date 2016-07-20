package controllers

import models.TestContent
import play.api.http.Writeable
import play.api.libs.json.Reads
import play.api.mvc.{Controller, Action}
import xyz.mattclifton.play.stringent.StringentActions

class TestController(implicit reader: Reads[TestContent], writeable: Writeable[TestContent]) extends Controller with StringentActions {
  def okAction = Action.stringent[OkResult] {
    Ok
  }

  def multipleResults = Action.stringent[OkResult, BadRequestResult] {
    Ok
  }

  def okWithContent = Action.stringent[OkWithContent[TestContent]] {
    Ok.withContent(TestContent(1, "test"))
  }
}
