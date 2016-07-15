package controllers

import models.TestContent
import play.api.libs.json.Reads
import play.api.mvc.{Controller, Action}
import xyz.mattclifton.play.stringent.StringentActions

class TestController(implicit reader: Reads[TestContent]) extends Controller with StringentActions {
  def okAction = Action.stringent[OkResult] {
    Ok
  }

  def noBodyAction = Action {
    Ok
  }
}
