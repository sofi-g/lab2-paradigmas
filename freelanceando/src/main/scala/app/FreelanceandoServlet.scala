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

  private def checkParameters(
    validParameters:List[String],jsonValue: JValue):Boolean = { 
      val inputData= jsonValue.extract[Map[String,Any]]
      val result = inputData == 
        inputData.filterKeys(validParameters.contains(_))
      result
  } 
  
  get("/api/categories") {
    Ok (
      db.categories.all.map(c => c.toMap)
    )
  }

  get("/api/freelancers") {
    try {
      val category_id = params("category_id").toInt
      val parameters = params.toMap - "category_id"
      (db.freelancers.filter(parameters)).filter(m => m.getCategoryIds.exists(
        category_id == _)).map(c => c.toMap - "total_earnings")
    } catch {
      case e: Exception => (db.freelancers.filter(params.toMap).
        map(c => c.toMap - "total_earnings"))
    }
  }
  
  get("/api/freelancers/:id") {
    try {
      val id  = (params("id")).toInt
      if (db.freelancers.exists("id", id)){
        Ok (
          db.freelancers.get(id).map(f => f.toMap)
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
      case JNothing => BadRequest("Bad Json \n")
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
      db.clients.all.map(c => c.toMap - "job_ids"))
  }

  get("/api/clients/:id") {
    try {
      val id  = (params("id")).toInt
      if (db.clients.exists("id", id)){ 
        Ok (
          db.clients.get(id).map(c => c.toMap)
        )
      } else {  
          BadRequest("Id doesn't exists \n") 
      } 
    } catch {
      case e: java.lang.NumberFormatException => 
        BadRequest("Id should be a number \n") 
    }
  }
  
  post("/api/clients") {
    parsedBody match {
      case JNothing => BadRequest("Bad Json \n") 
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
    var parameters: Map[String,Any]= params.toMap
    if (parameters.contains("category_id")){
      val category_id = params("category_id").toInt
      parameters = parameters-"category_id" + ("category_id"->category_id)
      }
    if (parameters.contains("client_id")){
      val client_id = params("client_id").toInt
      parameters = parameters-"client_id" + ("client_id"->client_id)
    }
    if (parameters.contains("hourly_price")){
      val hourly_price = params("hourly_price").toInt
      parameters = parameters-"hourly_price" + ("hourly_price"-> hourly_price)
    }
    db.jobs.filter(parameters).map(j => j.toMap)
  }
 
  post("/api/jobs") {
    parsedBody match {
      case JNothing => BadRequest("Bad Json \n") 
      case parsedResponse => { 
        val validParameters= List("title", "category_id", "client_id",
          "preferred_expertise", "preferred_country", "hourly_price")
        if (!checkParameters(validParameters, parsedResponse)) {
          BadRequest("Unexpected parameters \n")
        } else {
        val job = new Job()  
          try{
            val client_id  = (parsedResponse \ "client_id").extract[Int] 
            val category_id  = (parsedResponse \ "category_id").extract[Int]  
            if (!(db.clients.exists("id", client_id))) {
              BadRequest("Client doesn't exist \n") 
            } else if (!(db.categories.exists("id", category_id))) { 
              BadRequest("Category doesn't exist \n")
            } else {
              job.fromJson(parsedResponse.extract[JValue]) 
              db.jobs.save(job)
              Ok(job.getId)
            }
            } catch { 
              case e: Exception => BadRequest("Falló Jobs.fromJson \n") 
            }
          }
        }
      }
    }
  
  post("/api/jobs/pay"){
    parsedBody match {
      case JNothing => BadRequest("Bad \n") 
      case parsedResponse => { 
        val validParameters= List("freelancer_id", "job_id", "amount")
        if (!checkParameters(validParameters, parsedResponse)){
          BadRequest("Unexpected parameters \n")
        } else {
          try {
            val freelancer_id = (parsedResponse \ "freelancer_id").extract[Int]
            val job_id = (parsedResponse \ "job_id").extract[Int]
            val amount = (parsedResponse \ "amount").extract[Int]
            if (!(db.freelancers.exists("id", freelancer_id))){
              BadRequest("freelancer doesn't exist \n")
            } else if (!(db.jobs.exists("id", job_id))){ 
              BadRequest("Job doesn't exist \n")
            } else { 
              val job = db.jobs.filter(Map("id" -> job_id)).head
              val client = db.clients.filter(
                Map("id" -> job.getClientId)).head.increment(amount)
              val freelancer = db.freelancers.filter(
                Map("id" -> freelancer_id)).head.increment(amount)
              db.clients.save(client)
              db.freelancers.save(freelancer)
              Ok("Payment was completed successfully \n")
            }
          } catch {
            case e: Exception => BadRequest("Type error\n")
          }
        }
      }
    }
  }

}