##  Endpoints API

### Usuarios (`/api/v1/usuario`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/registro` | Registrar nuevo usuario |
| POST | `/login` | Iniciar sesión |
| GET | `/perfil/{id}` | Obtener perfil de usuario |

### Productos (`/api/v1/productos`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Listar todos los productos |
| GET | `/{id}` | Obtener producto por ID |
| GET | `/buscar/{titulo}` | Buscar producto por título |

### Pagos (`/api/v1/pagos`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/procesar/{pedidoId}` | Procesar pago de un pedido |
| GET | `/` | Listar todos los pagos |
| GET | `/pedido/{pedidoId}` | Obtener pago por ID de pedido |
