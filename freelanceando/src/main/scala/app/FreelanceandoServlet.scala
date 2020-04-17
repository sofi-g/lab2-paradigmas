package app

import org.json4s.{DefaultFormats, Formats, JValue, JNothing}

import org.scalatra._
import org.scalatra.json._
import models._
import models.database.Database


class FreelanceandoServlet(db : Database) extends ScalatraServlet {

  // Here you have to complete all the API endopoints.

  get("/api/categories") { db.categories.all }

}

