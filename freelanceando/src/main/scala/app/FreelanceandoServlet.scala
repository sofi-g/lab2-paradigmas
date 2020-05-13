package app
import org.json4s.{DefaultFormats, Formats, JValue, JInt, JString, JNothing}

import org.scalatra._
import org.scalatra.json._
import models._
import models.database.Database

class FreelanceandoServlet(db : Database) extends ScalatraServlet
  with JacksonJsonSupport{
  
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
      (db.freelancers.all.map(c => c.toMap - "total_earnings")) 
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
          BadRequest("Id doesn't exists \n") 
      } 
    } catch {
      case e: java.lang.NumberFormatException => 
        BadRequest("ID should be a number \n") 
    }
  }

  post("/api/freelancers") { 
    parsedBody match {
      case JNothing => BadRequest("Bad \n")
      case parsedResponse => {
        val validParameters = List("username", "country_code", 
          "hourly_price","category_ids","reputation") 
        if (!checkParameters(validParameters, parsedResponse)){
          BadRequest("Unexpected parameters \n")
        } else { 
          val freelancer = new Freelancer()
          try{ 
            freelancer.fromJson(parsedResponse.extract[JValue])
            val i = (freelancer.getCategoryIds).
                      forall(y => {db.categories.exists("id",y)})
            if (i) {
              db.freelancers.save(freelancer)
              Ok(freelancer.getId)
            } else {
              BadRequest("Category doesn't exist \n")
            }
          } catch {
          case e: Exception => BadRequest("Falló Freelancer.fromJson \n") 
          }
        }
      }
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
          BadRequest("Bad Json \n") 
      } 
    } catch {
      case e: java.lang.NumberFormatException => 
        BadRequest("Id should be a number \n") 
    }
  }
  
  private def checkParameters(
    validParameters:List[String],jsonValue: JValue):Boolean = { 
      val inputData= jsonValue.extract[Map[String,Any]]
      val result = inputData == 
        inputData.filterKeys(validParameters.contains(_))
      result
  } 

  post("/api/clients") {
    parsedBody match {
      case JNothing => BadRequest("Bad \n") 
      case parsedResponse => { 
        val validParameters= List("username","country_code")
        if (!checkParameters(validParameters, parsedResponse)){
          BadRequest("Expected parameters: username,country_code \n")
        } else {
          val client = new Client()
          try {
            client.fromJson(parsedResponse.extract[JValue]) 
            db.clients.save(client)
            Ok(client.getId)
          } catch { 
            case e: Exception => BadRequest("Falló Clients.fromJson \n") 
          }
        }
      }
    }
  }
 
  get("/api/jobs") {
    Ok (
      db.jobs.all.map(c => c.toMap))
  }

  post("/api/jobs") {
    parsedBody match {
      case JNothing => BadRequest("Bad \n") 
      case parsedResponse => { 
        val validParameters= List("title", "category_id", "client_id",
          "preferred_expertise", "preferred_country", "hourly_price")
        if (!checkParameters(validParameters, parsedResponse)) {
          BadRequest("Unexpected parameters \n")
        } else {
          val job = new Job()
          val client_id  = (parsedResponse \ "client_id").extract[Int] 
          val category_id  = (parsedResponse \ "category_id").extract[Int]   
          if (!(db.clients.exists("id", client_id))) {
            BadRequest("Client doesn't exist \n") 
          } else if (!(db.categories.exists("id", category_id))) { 
            BadRequest("Category doesn't exist \n")
          } else {
            try { 
              job.fromJson(parsedResponse.extract[JValue]) 
              db.jobs.save(job)
              Ok(job.getId)
            } catch { 
              case e: Exception => BadRequest("Falló Jobs.fromJson \n") 
            }
          }
        }
      }
    }
  } 
 
  get("/api/freelancers") {
    Ok (
    db.freelancers.filter(params.toMap).
      map(c => c.toMap - "total_earnings")) 
  }

  get("/api/jobs") {
    Ok (
    db.jobs.filter(params.toMap).map(c => c.toMap))
  }
  
}
