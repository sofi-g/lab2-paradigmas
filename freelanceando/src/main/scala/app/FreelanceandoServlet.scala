package app
import org.json4s.{DefaultFormats, Formats, JValue, JInt, JString, JNothing}

import org.scalatra._
import org.scalatra.json._
import models._
import models.database.Database


class FreelanceandoServlet(db : Database) extends ScalatraServlet with JacksonJsonSupport{
  protected implicit val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }
  
  get("/api/categories") {
    Ok (
      db.categories.all.map(c => c.toMap)
    )
  }
  //no tiene que devolver total_earnings
  get("/api/freelancers") {
    Ok (
      db.freelancers.all.map(c => c.toMap)
    )
  }

  get("/api/freelancers/:id") {
    try {
      val id  = (params("id")).toInt
      if (db.freelancers.exists("id", id)){
        Ok (
          db.freelancers.get(id).map(c => c.toMap)
        )
      } else {
          BadRequest("Bad Json\n") // no se si el error es ese
    } 
    } catch {
      case e: java.lang.NumberFormatException => BadRequest("Bad Json\n") // acá tampoco
    }
  }
  get("/api/clients") {
    Ok (
      db.clients.all.map(c => c.toMap))
  }

  get("/api/clients/:id") {
    try {
      val id  = (params("id")).toInt
      if (db.clients.exists("id", id)){
        Ok (
          db.clients.get(id).map(c => c.toMap)
        )
      } else {
          BadRequest("Bad Json\n") // no se si el error es ese
    } 
    } catch {
      case e: java.lang.NumberFormatException => BadRequest("Bad Json\n") // acá tampoco
    }
  }

  get("/api/jobs") {
    Ok (
      db.jobs.all.map(c => c.toMap))
  }

  post("/api/freelancers") {
  parsedBody match {
    case JNothing => BadRequest("Bad \n") //  si no es json
    case parsedResponse => {
      /*
        400 - Solicitud incorrecta si: 
        * No se puede analizar json 
        * Los nombres o tipos de campos Json son incorrectos 
        * No hay categorías con esos identificadores 
        * Los parámetros recibidos tienen un ID de clave. 
        Si` reputación` no está presente como parámetro, cree uno con el valor "junior". // Ya está
        Las ganancias totales comienzan en 0. | */ //Ya está
        val freelancer = new Freelancer() 
        freelancer.fromJson(parsedResponse)
        db.freelancers.save(freelancer)
        Ok(freelancer.getId)
     }
   }
 }

  /*
  post("/api/freelancers") {
    Ok (
      db.freelancers.all.map(c => c.toMap)
    )
  }
  
  POST   | /api/freelancers/    | 
  {username: str, country_code: str, category_ids: [int], reputation?: str, hourly_price: int} 
  | 200 - id \ 400 - 
  Bad Request if: * Can’t parse json * Json fields names or types are incorrect 
  * There are no categories with those ids * The received parameters have a key id.                         
  | If `reputation` is not present as a parameter, create freelancer with value “junior”. Total earnings start on 0.
  */
}
