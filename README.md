
# Microservicio de Reposición de Stock

### Reglas de negocio consideradas
1. Solo se puede tener 1 orden pendiente

## Casos de uso

#### 1. Controlar stock

* *Precondición*
    - Se genera una nueva orden de venta.
* *Camino normal*
    - Al confirmar una orden, el servicio de reposición recibe un mensaje por cada articulo vendido. 
    - Realiza la validación del stock, nota que luego de la orden actual, el stock quedará en el mínimo o menor, sino _CA1_
    - Solicita reposición de stock para el _articleId_ en cuestión, con _quantity rasultante_.
* *Camino alternativo 1*
    - Si el stock resultante es mayor al mínimo, no se realiza ninguna acción.


#### 2. Generar reposición del stock

* *Precondición*
  - El stock llego a la cantidad mínima configurada para un articulo y se solicitó reposición.

* *Camino normal*
  - Verificar si existe una orden de compra con estado _Pending_, sino _CA1_
  - Verificar si existe una _PurchaseOrder_ con el _articleId_, sino _CA2_
  - Calcular el _requiredQuantity_
  - Se actualiza _quantity_ con el valor de _requiredQuantity_
  - Se crea una notificación con tipo _UPDATE-ORDER_

* *Camino alternativo 1*
  - Se crea una orden de compra y se establece el estado en _Pending_
  - Se calcula el required _requiredQuantity_
  - Se crea un _PurchaseOrderItem_ del articulo y el _quantity_ se establece con el _requiredQuantity_
  - Se crea una notificacion con tipo _CREATE-ORDER_

* *Camino alternativo 2*
  - Se calcula el required _requiredQuantity_
  - Se crea un _PurchaseOrderItem_ del articulo y el _quantity_ se establece con el _requiredQuantity_
  - Se crea una notificación con tipo _ADD-ITEM_

#### 3. Cancelar orden de compra

* *Precondición*
  - Debe existir una orden de compra con estado _Pending_

* *Camino normal*
  - Se modifica el estado de la orden de compra a estado _Cancelled_
  - Se crea una notificación con tipo _CANCEL-ORDER_

#### 4. Confirmar una orden de compra

* *Precondición*
  - Debe existir una orden de compra con estado _Pending_
  
* *Camino normal*
  - Se modifica el estado de la orden de compra a estado _Sent_
  - Se crea una notificación con tipo _SENT-ORDER_

#### 5. Recibir una orden de compra

* *Precondición*
  - Debe existir una orden de compra con estado _Sent_
  
* *Camino normal*
  - Se modifica el estado de la orden de compra a estado _Received_
  - Por cada _PurchaseOrderItem_ se actualiza el stock del articulo con el _articleId_ y se incrementa su stock con _quantity_
  - Se crea una notificacion con tipo _RECEIVED-ORDER_

## Diagrama de modelo de datos

> **ArticleStockConfig**
> - id
> - articleId
> - stockMin
> - minimumOrderQuantity
> - create
> - update
> - enable

> **PurchaseOrder**
> - id
> - state  _(Pending | Cancelled | Sent | Received )_
> - create
> - update

> **PurchaseOrderItem**
> - articleId
> - purchaseOrderId
> - quantity


## Dependencias

### Auth

El catálogo sólo puede usarse por usuario autenticados, algunas operaciones como agregar un artículo nuevo requieren que el usuario sea "admin", ver la arquitectura de microservicios de [ecommerce](https://github.com/nmarsollier/ecommerce).

### MongoDb

Ver tutorial de instalación en [ecommerce](https://github.com/nmarsollier/ecommerce) en la raíz.

### RabbitMQ

La comunicación con Catalog y Auth es a través de rabbit.

Ver tutorial de instalación en [ecommerce](https://github.com/nmarsollier/ecommerce) en la raíz.

### Kotlin

Java JDK [oracle.com](http://www.oracle.com/technetwork/es/java/javase/downloads/index.html)

Gradle [gradle.org](https://gradle.org/install/)

Establecer las variables de entorno sujeridas en las instalaciones.
Tanto los ejecutables de java, como gradle deben poder encontrarse en el path.

## Ejecución del servidor

Se clona el repositorio en el directorio deseado.

Nos paramos en la carpeta donde se encuentra el archivo build.gradle y ejecutamos :
```bash
gradle run
```

La primera vez que ejecute descarga las dependencias, puede tardar un momento.

## Apidoc

Apidoc es una herramienta que genera documentación de apis para proyectos node (ver [Apidoc](http://apidocjs.com/)).

El microservicio muestra la documentación como archivos estáticos si se abre en un browser la raíz del servidor [localhost:3005](http://localhost:3005/)

Ademas se genera la documentación en formato markdown.

Para que funcione correctamente hay que instalarla globalmente con

```bash
npm install apidoc -g
npm install -g apidoc-markdown2
```

La documentación necesita ser generada manualmente ejecutando la siguiente linea en la carpeta raíz :

```bash
apidoc -o www
apidoc-markdown2 -p www -o README-API.md
```

Esto nos genera una carpeta www con la documentación, esta carpeta debe estar presente desde donde se ejecute el proyecto, aunque se puede configurar desde el archivo de properties.

## Configuración del servidor

Este servidor se configura con variables de entorno

- SERVER_PORT = Puerto del servidor (3005)
- AUTH_SERVICE_URL = Servidor Auth (http://localhost:3000)
- RABBIT_URL = Rabbit (localhost)
- MONGO_URL = Url de mongo (localhost)
- WWW_PATH = Path documentación (www)

Este archivo permite configurar parámetros del servidor, ver ejemplos en config-example.json.
