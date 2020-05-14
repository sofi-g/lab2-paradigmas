# Informe Lab2: Programación Orientada a Objetos

  
## Introducción
Para este laboratorio implementamos una API RESTful para un directorio de freelancers donde el objetivo es poner en práctica el paradigma de programación basado en el concepto de `objetos` y seguir desarrollando la programación de alto orden del paradigma funcional. 

## Comenzando

Al comenzar teníamos el archivo Model.scala donde se implementaban los modelos que nos ayudarían a representar las diferentes entidades de la aplicación: Category, Freelancers, Clients y Jobs.

### Clases

Para cada una de las entidades definimos su propia clase extendiéndola de la clase Model implementada en el archivo Models.scala. 


**Dos casos en los que utilizamos herencia para evitar duplicar código serían en:**

Los métodos *`toMap`* y *`fromJson`* para que nos mostraran los atributos correspondientes de cada entidad en formato JSON. 

Para ello nos ayudamos de la función *`super`* para llamar a los métodos de la superclase y así no perder la implementación anterior del `trait Model` de estos metodos.

```
override def toMap: Map[String, Any] = 
	super.toMap +
		("name" -> name)
		.
		.
		.
```
```
override  def  fromJson(jsonValue: JValue):  Category  = {
	super.fromJson(jsonValue)
	name = (jsonValue \ "name").extract[String]
	.
	.
	.
	this
}
```
#### Clase Category 

**atributos**

* *name*: indica el nombre de la categoría 

#### Clase Freelancer

**atributos**

* *username*: nombre de usuario del freelancer
* *country_code*: país del usuario
* *category_ids*: identificador de la categoría en la que trabaja el freelancer
* *reputation*: nivel de experiencia del freelancer
* *hourly_price*: muestra lo que cobra por hora
* *total_earnings*: indica cuánto gano en la plataforma


#### Clase Client

**atributos**

* *username*: nombre de usuario del cliente
* *country_code*: país del cliente
* *job_ids*: identificador de los distintos trabajos
* *total_spend*: muestra cuánto gastaron en la plataforma

#### Clase Job

**atributos**

* *title*: título del trabajo
* *category_id*: identificador de la categoría a la que pertenece el trabajo
* *client_id*: muestra el id del cliente 
* *preferred_expertise*: preferencia del nivel de experiencia del freelancer
* *preferred_country*: preferencia del país del que debe ser el freelancer
* *hourly_price*: monto que esta dispuesto a pagar el cliente por el trabajo

***
### Endpoints 

En el archivo FreelanceandoServlet.scala definimos los endpoints GET y POST para las entidades de nuestra aplicación.
Antes de comenzar con los endpoints, implementamos los métodos *def all*, *def get*, *def filter* en DatabaseTable.scala.


PREGUNTAS:

- ¿Qué concepto de la programación orientada a objetos es el que les permite que un mismo endpoint como api/freelancers tome distintos parámetros?

- ¿En qué clase se encargan de controlar que las categorías asignadas a un freelancer efectivamente existan? ¿Por qué es responsabilidad de esa clase y no de otra?


- ¿Dónde colocaron el código con la lógica de la acción pagar? ¿Lo dividieron entre varios objetos, o pusieron todo en un mismo lugar? ¿Dónde tendrían que agregar código si se decidiera llamar a una API externa como MercadoPago para realizar la transacción?

* * *

### DECISIONES DE DISEÑO 

Para los endpoints tuvimos que tomar algunas decisiones de diseño en particular:

En en el archivo DatabaseTable.scala definimos el metodo `exists`

```
def exists(attr: String, value: Any): Boolean = 
    all.exists(m => m.toMap(attr) == value)
```

Para tratar algunos de los errores de los endpoints como por ejemplo que el id del freelancer que tenemos como argumento NO existe o cuando necesitábamos  controlar que las categorías asignadas a un freelancer efectivamente existan, etc.


También definimos el metodo `private def checkParameters` 

```
  private def checkParameters(
    validParameters:List[String],jsonValue: JValue):Boolean = { 
      val inputData= jsonValue.extract[Map[String,Any]]
      val result = inputData == 
        inputData.filterKeys(validParameters.contains(_))
      result
  } 
```  
que toma una lista de parametros válidos y un json, y verifica que el json contenga esos parámetros.
