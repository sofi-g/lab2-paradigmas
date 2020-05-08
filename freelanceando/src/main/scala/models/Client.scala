package models

import org.json4s.JValue
import org.json4s.JInt
import org.json4s.JArray
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JString

object Client extends ModelCompanion[Client] {

  implicit lazy val formats = DefaultFormats

  def apply: Client = new Client

}

class Client extends Model[Client] {

  protected[Client] var username: String = "" 
  protected[Client] var country_code: String = ""
  protected[Client] var total_spend: Int = 0 
  protected[Client] var job_ids: List[Int] = List()

  def getUserName: String = username

  override def toMap: Map[String, Any] = 
    super.toMap + (
      "username" -> username, 
      "country_code" -> country_code, 
      "total_spend" -> total_spend,
      "job_ids" -> job_ids
    )
      
  override def fromJson(jsonValue: JValue): Client = {
    super.fromJson(jsonValue)
    super.fromJson(jsonValue)
    username = (jsonValue \ "username").extract[String]
    country_code = (jsonValue \ "country_code").extract[String]
    total_spend = (jsonValue \ "total_spend").extract[Int]
    job_ids = (jsonValue \ "jobs_ids").extract[List[Int]]
    this 
  }
   
}