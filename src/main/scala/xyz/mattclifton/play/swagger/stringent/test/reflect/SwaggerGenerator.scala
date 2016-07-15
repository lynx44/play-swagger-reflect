package xyz.mattclifton.play.swagger.reflect

import com.iheart.playSwagger.{JsonHelper, DomainModelQualifier}
import play.api.libs.json.{Json, JsString, JsObject, JsArray}
import play.api.mvc.AnyContent
import play.routes.compiler.Route

import scala.reflect.runtime.universe._

object EndPointSpecBuilderFactory {
  def apply(modelQualifier: DomainModelQualifier, defaultPostBodyFormat: String, route: Route, tag: Option[String], cl: ClassLoader): EndPointSpecBuilder =
    new EndPointSpecBuilder(modelQualifier, defaultPostBodyFormat, route, tag, cl)
}

class EndPointSpecBuilder(modelQualifier: DomainModelQualifier, defaultPostBodyFormat: String, route: Route, tag: Option[String], cl: ClassLoader) extends com.iheart.playSwagger.EndPointSpecBuilder(modelQualifier, defaultPostBodyFormat, route, tag, cl) {
  override protected def readControllerParams: JsArray = {
    val params = super.readControllerParams

    controllerType.map(controller => {
      val typeArgs = getGenericTypeArgs(controllerMethod.get.returnType)

      val bodyType = typeArgs.map(_.head)

      if(bodyType.exists(contentType => !(contentType.weak_<:<(typeOf[AnyContent])))) {
        val bodyParam = JsonHelper.findByName(params, "body")
        bodyParam.fold(JsArray(Seq(JsObject(Map("name" → JsString("body"), "schema" -> Json.obj("$ref" → JsString(s"#/definitions/${bodyType.get}")), "in" → JsString("body")))) ++ params.value)) { param ⇒
          params
        }
      } else {
        params
      }
    }).getOrElse(params)
  }

  protected lazy val controllerType: Option[Type] = {
    val className = s"${route.call.packageName}.${route.call.controller}"

    val mirror = runtimeMirror(cl)
    Some(mirror.classSymbol(cl.loadClass(className)).toType)
  }

  protected lazy val controllerMethod: Option[MethodSymbol] = {
    controllerType.flatMap(_.members.collect({
      case m: MethodSymbol if m.name.toString == route.call.method => m
    }).headOption)
  }

  protected def getGenericTypeArgs(t: Type): Option[List[Type]] = {
    t match {
      case TypeRef(_, _, args) => if (args.nonEmpty) Option(args) else None
      case AnnotatedType(_, underlying) => underlying match {
        case TypeRef(_, _, args) => if (args.nonEmpty) Option(args) else None
      }
      case _ => None
    }
  }
}
