# Replacement Service en Kotlin v0.1.0

Microservicio de Reposición de Stock

- [Configuración de stock de artículo](#configuracion)
	- [Buscar Configuraciones](#buscar-configuraciones)
    - [Buscar Configuracion](#buscar-configuracion)
    - [Crear Configuración](#crear-configuracion)
    - [Editar Stock_Minimo](#editar-stock-configuracion)
    - [Editar Pedido_Minimo](#editar-pedido-configuracion)
    - [Eliminar Configuración](#eliminar-configuracion)
  
- [Configuración de stock de artículo](#orden)
	- [Buscar ordenes de pedido](#buscar-ordenes)
    - [Buscar orden de pedido](#buscar-orden)
    - [Crear orden de pedido](#crear-orden)
    - [Cancelar orden de pedido](#cancelar-orden)
    - [Confirmar orden de pedido](#confirmar-orden)
    - [Recibir orden de pedido](#recibir-orden)
    - [Agregar articulo de orden](#agregar-articulo-orden)
	- [Eliminar articulo de orden](#eliminar-articulo-orden)

- [RabbitMQ](#rabbitmq)
	
- [RabbitMQ_GET](#rabbitmq_get)
	- [Control de stock de artículos](#control-stock)
	- [Logout](#logout)
	
- [RabbitMQ_POST](#rabbitmq_post)
	- [Notificar_orden_creada](#orden-creada)
    - [Notificar_orden_cancelada](#orden-cancelada)
    - [Notificar_orden_actualizada](#orden-actulizada)
    - [Notificar_orden_confirmada](#orden-confirmada)
    - [Notificar_orden_recibida](#orden-recibida)
	


# <a name='configuracion'></a> Configuraciones de Artículo


## <a name='buscar-configuraciones'></a> Buscar configuraciones
[Back to top](#top)



	GET /v1/replacement/articles-config/




### Success Response

Respuesta

```
HTTP/1.1 200 OK
[
  {
      "_id": "{id de la configuración}"
      "articleId": "{id del artículo asociado}",
      "minOrderQuantity": "{cantidad mínima que se puede pedir}",
      "minStock": "{cantidad mínima de stock}",
      "enabled": {si esta activo}
  },
  ...
 ]
```


### Error Response

400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "path" : "{Nombre de la propiedad}",
    "message" : "{Motivo del error}"
}
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "error" : "{Motivo del error}"
}
```
500 Server Error

```
HTTP/1.1 500 Server Error
{
    "error" : "{Motivo del error}"
}
```

# <a name='configuracion'></a> Configuraciones de artículo

## <a name='buscar-configuracion'></a> Buscar configuración de artículo
[Back to top](#top)



	GET /v1/replacement/articles-config/:id



### Success Response

Respuesta

```
HTTP/1.1 200 OK
{
    "_id": "{id de la configuración}"
    "articleId": "{id del artículo asociado}",
    "minOrderQuantity": "{cantidad mínima que se puede pedir}",
    "minStock": "{cantidad mínima de stock}",
    "enabled": {si esta activo}
},

```


### Error Response

400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "path" : "{Nombre de la propiedad}",
    "message" : "{Motivo del error}"
}
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "error" : "{Motivo del error}"
}
```
500 Server Error

```
HTTP/1.1 500 Server Error
{
    "error" : "{Motivo del error}"
}
```


# <a name='configuracion'></a> Configuraciones de artículos

## <a name='crear-configuracion'></a> Crear configuración de artículo
[Back to top](#top)



	POST /v1/replacement/articles-config




### Examples

Body

```
{
      "articleId": "{id del artículo}",
      "minOrderQuantity": "{cantidad mínima que se puede pedir}",
      "minStock": "{cantidad mínima de stock}",
}
```
Header Autorización

```
Authorization=bearer {token}
```


### Success Response

Respuesta

```
HTTP/1.1 200 OK
{
      "_id": "{id de la configuración}"
      "articleId": "{id del artículo asociado}",
      "minOrderQuantity": "{cantidad mínima que se puede pedir}",
      "minStock": "{cantidad mínima de stock}",
      "enabled": {si esta activo}
}
```

### Error Response

401 Unauthorized

```
HTTP/1.1 401 Unauthorized
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "path" : "{Nombre de la propiedad}",
    "message" : "{Motivo del error}"
}
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "error" : "{Motivo del error}"
}
```
500 Server Error

```
HTTP/1.1 500 Server Error
{
    "error" : "{Motivo del error}"
}
```

# <a name='configuracion'></a> Configuraciones de artículos

## <a name='editar-stock-configuracion'></a> Editar stock mínimo de configuración de artículo
[Back to top](#top)



	PUT /v1/replacement/articles-config/:id/stock-min/:value




### Examples

Header Autorización

```
Authorization=bearer {token}
```


### Success Response

Respuesta

```
HTTP/1.1 200 OK
{
      "_id": "{id de la configuración}"
      "articleId": "{id del artículo asociado}",
      "minOrderQuantity": "{cantidad mínima que se puede pedir}",
      "minStock": "{cantidad mínima de stock}",
      "enabled": {si esta activo}
}
```

### Error Response

401 Unauthorized

```
HTTP/1.1 401 Unauthorized
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "path" : "{Nombre de la propiedad}",
    "message" : "{Motivo del error}"
}
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "error" : "{Motivo del error}"
}
```
500 Server Error

```
HTTP/1.1 500 Server Error
{
    "error" : "{Motivo del error}"
}
```

# <a name='configuracion'></a> Configuraciones de artículos

## <a name='editar-pedido-configuracion'></a> Editar pedidido mínimo de configuración de artículo
[Back to top](#top)



	PUT /v1/replacement/articles-config/:id/min-order-quantity/:value




### Examples

Header Autorización

```
Authorization=bearer {token}
```


### Success Response

Respuesta

```
HTTP/1.1 200 OK
{
      "_id": "{id de la configuración}"
      "articleId": "{id del artículo asociado}",
      "minOrderQuantity": "{cantidad mínima que se puede pedir}",
      "minStock": "{cantidad mínima de stock}",
      "enabled": {si esta activo}
}
```

### Error Response

401 Unauthorized

```
HTTP/1.1 401 Unauthorized
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "path" : "{Nombre de la propiedad}",
    "message" : "{Motivo del error}"
}
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "error" : "{Motivo del error}"
}
```
500 Server Error

```
HTTP/1.1 500 Server Error
{
    "error" : "{Motivo del error}"
}
```


# <a name='configuracion'></a> Configuraciones de artículos
  
## <a name='eliminar-configuración'></a> Eliminar configuración de artículo
[Back to top](#top)



	DELETE v1/replacement/articles-config/:id



### Examples

Header Autorización

```
Authorization=bearer {token}
```


### Success Response

200 Respuesta

```
HTTP/1.1 200 OK
```


### Error Response

401 Unauthorized

```
HTTP/1.1 401 Unauthorized
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "path" : "{Nombre de la propiedad}",
    "message" : "{Motivo del error}"
}
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "error" : "{Motivo del error}"
}
```
500 Server Error

```
HTTP/1.1 500 Server Error
{
    "error" : "{Motivo del error}"
}
```



# <a name='orden'></a> Ordenes de pedido de reposición


## <a name='buscar-ordenes'></a> Buscar ordenes de reposición
[Back to top](#top)



	GET /v1/replacement/purchase-orders




### Success Response

Respuesta

```
HTTP/1.1 200 OK
[
      {
          "_id": "{id de la orden}"
          "created": "{fecha de creación}",
          "updated": "{fecha de actualización}",
          "state": "{estado de la orden}"
          "items": [
              {
                  "articleId": "{id del artículo}",
                  "quantity": "{cantidad a reponer}"
              },
              ...
          ]
      },
      ...
]
```


### Error Response

400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "path" : "{Nombre de la propiedad}",
    "message" : "{Motivo del error}"
}
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "error" : "{Motivo del error}"
}
```
500 Server Error

```
HTTP/1.1 500 Server Error
{
    "error" : "{Motivo del error}"
}
```


# <a name='orden'></a> Ordenes de reposición

## <a name='buscar-orden'></a> Buscar orden de pedido
[Back to top](#top)



	GET /v1/replacement/purchase-orders/{id}



### Success Response

Respuesta

```
HTTP/1.1 200 OK
{
    "_id": "{id de la orden}"
    "created": "{fecha de creación}",
    "updated": "{fecha de actualización}",
    "state": "{estado de la orden}"
    "items": [
        {
            "articleId": "{id del artículo}",
            "quantity": "{cantidad a reponer}"
        },
        ...
    ]
}
```


### Error Response

400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "path" : "{Nombre de la propiedad}",
    "message" : "{Motivo del error}"
}
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "error" : "{Motivo del error}"
}
```
500 Server Error

```
HTTP/1.1 500 Server Error
{
    "error" : "{Motivo del error}"
}
```


# <a name='orden'></a> Ordenes de reposición

## <a name='crear-configuracion'></a> Crear orden de pedido
[Back to top](#top)



	POST /v1/replacement/purchase-orders




### Examples

Header Autorización

```
Authorization=bearer {token}
```


### Success Response

Respuesta

```
HTTP/1.1 200 OK
{
    "_id": "{id de la orden}"
    "created": "{fecha de creación}",
    "updated": "{fecha de actualización}",
    "state": "{estado de la orden}"
    "items": [
        {
            "articleId": "{id del artículo}",
            "quantity": "{cantidad a reponer}"
        },
        ...
    ]
}
```

### Error Response

401 Unauthorized

```
HTTP/1.1 401 Unauthorized
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "path" : "{Nombre de la propiedad}",
    "message" : "{Motivo del error}"
}
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "error" : "{Motivo del error}"
}
```
500 Server Error

```
HTTP/1.1 500 Server Error
{
    "error" : "{Motivo del error}"
}
```

# <a name='orden'></a> Ordenes de reposición

## <a name='cancelar-orden'></a> Cancelar orden de reposición
[Back to top](#top)



	PUT /v1/replacement/purchase-orders/:id/cancel




### Examples

Header Autorización

```
Authorization=bearer {token}
```


### Success Response

Respuesta

```
HTTP/1.1 200 OK
{
    "_id": "{id de la orden}"
    "created": "{fecha de creación}",
    "updated": "{fecha de actualización}",
    "state": "{estado de la orden}"
    "items": [
        {
            "articleId": "{id del artículo}",
            "quantity": "{cantidad a reponer}"
        },
        ...
    ]
}
```

### Error Response

401 Unauthorized

```
HTTP/1.1 401 Unauthorized
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "path" : "{Nombre de la propiedad}",
    "message" : "{Motivo del error}"
}
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "error" : "{Motivo del error}"
}
```
500 Server Error

```
HTTP/1.1 500 Server Error
{
    "error" : "{Motivo del error}"
}
```


# <a name='orden'></a> Ordenes de reposición

## <a name='confirmar-orden'></a> Confirmar orden de reposición
[Back to top](#top)



	PUT /v1/replacement/purchase-orders/:id/confirm




### Examples

Header Autorización

```
Authorization=bearer {token}
```


### Success Response

Respuesta

```
HTTP/1.1 200 OK
{
    "_id": "{id de la orden}"
    "created": "{fecha de creación}",
    "updated": "{fecha de actualización}",
    "state": "{estado de la orden}"
    "items": [
        {
            "articleId": "{id del artículo}",
            "quantity": "{cantidad a reponer}"
        },
        ...
    ]
}
```

### Error Response

401 Unauthorized

```
HTTP/1.1 401 Unauthorized
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "path" : "{Nombre de la propiedad}",
    "message" : "{Motivo del error}"
}
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "error" : "{Motivo del error}"
}
```
500 Server Error

```
HTTP/1.1 500 Server Error
{
    "error" : "{Motivo del error}"
}
```


# <a name='orden'></a> Ordenes de reposición

## <a name='recibir-orden'></a> Recibir orden de reposición
[Back to top](#top)



	PUT /v1/replacement/purchase-orders/:id/receive




### Examples

Header Autorización

```
Authorization=bearer {token}
```


### Success Response

Respuesta

```
HTTP/1.1 200 OK
{
    "_id": "{id de la orden}"
    "created": "{fecha de creación}",
    "updated": "{fecha de actualización}",
    "state": "{estado de la orden}"
    "items": [
        {
            "articleId": "{id del artículo}",
            "quantity": "{cantidad a reponer}"
        },
        ...
    ]
}
```

### Error Response

401 Unauthorized

```
HTTP/1.1 401 Unauthorized
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "path" : "{Nombre de la propiedad}",
    "message" : "{Motivo del error}"
}
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "error" : "{Motivo del error}"
}
```
500 Server Error

```
HTTP/1.1 500 Server Error
{
    "error" : "{Motivo del error}"
}
```


# <a name='orden'></a> Ordenes de reposición

## <a name='agregar-articulo-orden'></a> Agregar artículo a la orden de reposición
[Back to top](#top)



	PUT /v1/replacement/purchase-orders/:id/item




### Examples

Body

```
{
    "articleId": "{id del articulo}",
    "quantity": "{cantidad del articulo}",
}
```
Header Autorización

```
Authorization=bearer {token}
```


### Success Response

Respuesta

```
HTTP/1.1 200 OK
{
    "_id": "{id de la orden}"
    "created": "{fecha de creación}",
    "updated": "{fecha de actualización}",
    "state": "{estado de la orden}"
    "items": [
        {
            "articleId": "{id del artículo}",
            "quantity": "{cantidad a reponer}"
        },
        ...
    ]
}
```

### Error Response

401 Unauthorized

```
HTTP/1.1 401 Unauthorized
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "path" : "{Nombre de la propiedad}",
    "message" : "{Motivo del error}"
}
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "error" : "{Motivo del error}"
}
```
500 Server Error

```
HTTP/1.1 500 Server Error
{
    "error" : "{Motivo del error}"
}
```


# <a name='orden'></a> Ordenes de reposición

## <a name='eliminar-articulo-orden'></a> Eliminar artículo a la orden de reposición
[Back to top](#top)



	DELETE /v1/replacement/purchase-orders/:id/item/:idItem




### Examples

Header Autorización

```
Authorization=bearer {token}
```


### Success Response

Respuesta

```
HTTP/1.1 200 OK
{
    "_id": "{id de la orden}"
    "created": "{fecha de creación}",
    "updated": "{fecha de actualización}",
    "state": "{estado de la orden}"
    "items": [
        {
            "articleId": "{id del artículo}",
            "quantity": "{cantidad a reponer}"
        },
        ...
    ]
}
```

### Error Response

401 Unauthorized

```
HTTP/1.1 401 Unauthorized
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "path" : "{Nombre de la propiedad}",
    "message" : "{Motivo del error}"
}
```
400 Bad Request

```
HTTP/1.1 400 Bad Request
{
    "error" : "{Motivo del error}"
}
```
500 Server Error

```
HTTP/1.1 500 Server Error
{
    "error" : "{Motivo del error}"
}
```



# <a name='rabbitmq'></a> RabbitMQ

# <a name='rabbitmq_get'></a> RabbitMQ_GET

## <a name='control-stock'></a> Control de stock de artículos
[Back to top](#top)

<p>Escucha de mensajes model.article-data desde article-replacement-control. Valida stock del artículo.</p>

	DIRECT article-replacement-control/model.article-data



### Examples

Mensaje

```
{
    articleId = "{id del artículo}",
    price = "{precio del artículo}",
    referenceId = "{referencia del carrito}",
    stock = "{stock del artículo}",
    valid = "{validación del artículo}"
}
```




## <a name='logout'></a> Logout
[Back to top](#top)

<p>Escucha de mensajes logout desde auth. Invalida sesiones en cache.</p>

	FANOUT auth/logout



### Examples

Mensaje

```
{
"type": "article-exist",
"message" : "tokenId"
}
```




# <a name='rabbitmq_post'></a> RabbitMQ_POST

## <a name='orden-creada'></a> Notificar orden creada
[Back to top](#top)

<p>Enviá de mensajes model.purchase-order-created desde replacement.</p>

	DIRECT replacement/model.purchase-order-created



### Success Response

Mensaje

```
{
        "type": "order-created",
        "message": {
        "orderId": "{orderId}",
        "creationDate": "{creationDate}",
        "purchaseOrderItems": [
            {
                "articleId": "{arcticleId}",
                "quantity": "{quantity}"
            },
            ...
        ]
    }
}
```

## <a name='orden-cancelada'></a> Notificar orden cancelada
[Back to top](#top)

<p>Enviá de mensajes model.purchase-order-canceled desde replacement.</p>

	DIRECT replacement/model.purchase-order-caceled



### Success Response

Mensaje

```
{
        "type": "order-canceled",
        "message": {
            "orderId": "{orderId}",
            "updateDate": "{updateDate}",
        }
}
```

## <a name='orden-confirmada'></a> Notificar orden confirmada
[Back to top](#top)

<p>Enviá de mensajes model.purchase-order-confirmed desde replacement.</p>

	DIRECT replacement/model.purchase-order-confirmed



### Success Response

Mensaje

```
{
        "type": "order-confirmed",
        "message": {
        "orderId": "{orderId}",
        "updateDate": "{updateDate}",
        "purchaseOrderItems": [
            {
                "articleId": "{arcticleId}",
                "quantity": "{quantity}"
            },
            ...
        ]
    }
}
```

## <a name='orden-recibida'></a> Notificar orden recibida
[Back to top](#top)

<p>Enviá de mensajes model.purchase-order-received desde replacement.</p>

	DIRECT replacement/model.purchase-order-received



### Success Response

Mensaje

```
{
        "type": "order-received",
        "message": {
        "orderId": "{orderId}",
        "purchaseOrderItems": [
            {
                "articleId": "{arcticleId}",
                "quantity": "{quantity}"
            },
            ...
        ]
    }
}
```

## <a name='orden-actualizada'></a> Notificar orden actualizada
[Back to top](#top)

<p>Enviá de mensajes model.purchase-order-updated desde replacement.</p>

	DIRECT replacement/model.purchase-order-updated



### Success Response

Mensaje

```
{
        "type": "order-received",
        "message": {
        "orderId": "{orderId}",
        "updateDate": "{updateDate}",
        "purchaseOrderItems": [
            {
                "articleId": "{arcticleId}",
                "quantity": "{quantity}"
            },
            ...
        ]
    }
}
```


