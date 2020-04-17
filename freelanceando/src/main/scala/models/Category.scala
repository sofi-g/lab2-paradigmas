package models

import org.json4s.JValue
import org.json4s.DefaultFormats


/* Object companion of class Category */
object Category extends ModelCompanion[Category] {

  implicit lazy val formats = DefaultFormats

  /* This class constructor needs to be overwritten in every subclass of
   * ModelCompanion, because it needs the direct reference to each subclass,
   * in this case, Category.
   */
  def apply: Category = new Category
}

class Category extends Model[Category] {

  // TODO complete here with the methods for your model

  /*
  override def fromJson(jsonValue: JValue): Category = {
    // TODO Parse jsonValue here and assign the values to
    // the instance attributes.

    this  // Return a reference to this object.
  }
  */
}
