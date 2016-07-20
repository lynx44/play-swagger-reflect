package xyz.mattclifton.play.swagger.stringent

import com.iheart.playSwagger.{JsonHelper, DomainModelQualifier}
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._
import play.api.mvc.AnyContent
import play.routes.compiler.Route

import scala.reflect.runtime.universe._

object EndPointSpecBuilderFactory {
  def apply(modelQualifier: DomainModelQualifier, defaultPostBodyFormat: String, route: Route, tag: Option[String], cl: ClassLoader): EndPointSpecBuilder =
    new EndPointSpecBuilder(modelQualifier, defaultPostBodyFormat, route, tag, cl)
}

class EndPointSpecBuilder(modelQualifier: DomainModelQualifier, defaultPostBodyFormat: String, route: Route, tag: Option[String], cl: ClassLoader) extends xyz.mattclifton.play.swagger.reflect.EndPointSpecBuilder(modelQualifier, defaultPostBodyFormat, route, tag, cl) {
  override protected def transformSwaggerDoc(userDefinedDoc: Option[JsObject]): Option[JsObject] = {
    controllerMethod.map(method => {
      println(s"controller method: $method")
      if(method.returnType.typeSymbol.name.decodedName.toString == "StringentReturn") {
        val typeArgs = getGenericTypeArgs(method.returnType)
        println(s"type args: $typeArgs")
        typeArgs.map(args => {
          val responseTypes = args.filter(arg => arg.baseClasses.exists(b => b.name.decodedName.toString == "StringentResult"))
          println(s"response types: $responseTypes")
          val newDoc = userDefinedDoc.map(doc => {
            val responses = (doc \ "responses").asOpt[JsObject].getOrElse(JsObject(Seq()))
            val updatedResponses = responseTypes.foldLeft(responses)((response, arg) => arg.typeSymbol.name.decodedName.toString match {
              case "OkResult" => mergeByResponseCode(play.api.http.Status.OK, buildSwaggerResponseItem(play.api.http.Status.OK), response)
              case "OkWithContent" => mergeByResponseCode(play.api.http.Status.OK, buildSwaggerResponseItem(play.api.http.Status.OK, Some(getGenericTypeArgs(arg).get.head)), response)
              case "BadRequestResult" => mergeByResponseCode(play.api.http.Status.BAD_REQUEST, buildSwaggerResponseItem(play.api.http.Status.BAD_REQUEST), response)
            })

            println(updatedResponses)

            JsObject(("responses" -> updatedResponses) +: doc.fieldSet.filterNot(_._1 == "responses").toSeq)
          })
          newDoc
        }).getOrElse(userDefinedDoc)
      } else {
        userDefinedDoc
      }
    }).getOrElse(userDefinedDoc)
  }

  private def mergeByResponseCode(statusCode: Int, status: JsObject, responses: JsObject): JsObject = {
    val statusCodeValue = (responses \ statusCode.toString).asOpt[JsObject].fold(status)(value => value)
    JsObject(((statusCode.toString -> statusCodeValue) +: responses.fields.filterNot(_._1 == statusCode.toString)))
  }

  private def buildSwaggerResponseItem(statusCode: Int, schemaType: Option[Type] = None): JsObject = {
//    val schema = schemaType.map(t => Seq("schema" -> JsObject(Seq("$ref" -> JsString(s"#/definitions/${packageName(t.typeSymbol)}.${t.typeSymbol.name.decodedName}")))))

//    val toSeq = schema.flatMap(a => a).toSeq
    if(schemaType.nonEmpty) {
      JsObject(Seq("schema" -> JsObject(Seq("$ref" -> JsString(s"#/definitions/${packageName(schemaType.get.typeSymbol)}.${schemaType.get.typeSymbol.name.decodedName}")))))
    } else {
      JsObject(Seq())
    }

  }

  private def packageName(sym: Symbol) = {
    def enclosingPackage(sym: Symbol): Symbol = {
      if (sym == NoSymbol) NoSymbol
      else if (sym.isPackage) sym
      else enclosingPackage(sym.owner)
    }
    val pkg = enclosingPackage(sym)
    if (pkg == scala.reflect.runtime.currentMirror.EmptyPackageClass) ""
    else pkg.fullName
  }
}
