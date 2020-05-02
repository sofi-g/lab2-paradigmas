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
  
  get("/api/categories") {
    Ok (
      db.categories.all.map(c => c.toMap))
  }
  
  get("/api/freelancers") {
    Ok (
      db.freelancers.all.map(c => c.toMap))
  }

  get("/api/clients") {
    Ok (
      db.clients.all.map(c => c.toMap))
  }

   get("/api/jobs") {
    Ok (
      db.jobs.all.map(c => c.toMap))
  }
  
}


