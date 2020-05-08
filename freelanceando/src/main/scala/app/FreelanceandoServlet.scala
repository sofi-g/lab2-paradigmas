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
  
  get("/api/freelancers") {
    Ok (
      (db.freelancers.all.map(c => c.toMap - "total_earnings")) //-total_earnings
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
  
  post("/api/freelancers") { 
    parsedBody match { // parsed body es el json que recibe 
      case JNothing => BadRequest("Bad \n") //  si no es json 
      case parsedResponse => 
        
        val freelancer = new Freelancer() // crea freelancer
        freelancer.fromJson(parsedResponse) // le asigna los parametros de free
        val i = (freelancer.getCategoryIds).forall(y => {db.categories.exists("id",y)})
          if (i) {
            db.freelancers.save(freelancer)
            Ok(freelancer.getId)
          } else {
            BadRequest("category error \n")
          }
    }
  } 
     /* 
          400 - Solicitud incorrecta si: 
          * Los nombres o tipos de campos Json son incorrectos 
          * Los parámetros recibidos tienen un ID de clave. */ 
        
   get("/api/clients") {
    Ok (
      db.clients.all.map(c => c.toMap))
  }

  get("/api/clients/:id") {
    try {
      val id  = (params("id")).toInt // toma el parametro id y lo asigna a la variable id (da error cuando es de otro tipo, no int) por eso la excepcion 
      if (db.clients.exists("id", id)){ // si el cliente existe lo muestra
        Ok (
          db.clients.get(id).map(c => c.toMap)
        )
      } else { // sino da badRequest 
          BadRequest("Bad Json\n") // el error no es ese
    } 
    } catch {
      case e: java.lang.NumberFormatException => BadRequest("Bad Json\n") // el error no es bad json, copie y pegué
    }
  }

  post("/api/clients") {
    parsedBody match {
      case JNothing => BadRequest("Bad \n")
      case parsedResponse => {
        val client = new Client() 
        client.fromJson(parsedResponse)
        db.clients.save(client)
        Ok(client.getId)
      }
    }
  }

  get("/api/jobs") {
    Ok (
      db.jobs.all.map(c => c.toMap))
  } 
  

} 
