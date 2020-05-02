package app
import org.json4s.{DefaultFormats, Formats, JValue, JNothing}

import org.scalatra._
import org.scalatra.json._
import models._
import models.database.Database


class FreelanceandoServlet(db : Database) extends ScalatraServlet with JacksonJsonSupport{
  protected implicit val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }
  // Here you have to complete all the API endopoints.

  //get("/api/categories") { db.categories.all }
  get("/api/categories") {
    Ok (
      db.categories.all.map(c => c.toMap))
  }


  /*
    get("/api/freelancers") {
    Ok (
      db.freelancer.all.map(c => c.toMap))
  }

   post("/api/freelancers") {
   parsedBody match {
     case JNothing => BadRequest("Bad Json\n")
     case parsedResponse => {
       val freelancer = new Freelancer()
       // Do things to create client here
       Ok(freelancer.getId)
     }
   }
 }
*/


}


