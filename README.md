# Sistema de Gestión de Reservas — Nelumbo Consultores

API REST para la gestión de reservas y control de ocupación de salones de eventos, con soporte para múltiples sucursales, roles de usuario, histórico de reservas e integración con microservicio de notificaciones.

---

## Tecnologías utilizadas

- Java 21
- Spring Boot 3
- Spring Security + JWT
- PostgreSQL 16
- Docker + Docker Compose
- Lombok
- Maven

---

## Estructura del proyecto

```
├── demo-reservas/                  ← API principal
│   ├── src/main/java/com/reservas/demo_reservas/
│   │   ├── controllers/            ← Endpoints REST
│   │   ├── services/               ← Lógica de negocio
│   │   ├── repositories/           ← Acceso a datos (JPA)
│   │   ├── entities/               ← Entidades JPA
│   │   ├── dto/
│   │   │   ├── request/            ← DTOs de entrada
│   │   │   └── response/           ← DTOs de salida
│   │   ├── enums/                  ← EstadoReserva, Rol
│   │   ├── exceptions/             ← Excepciones personalizadas
│   │   └── security/               ← JWT, filtros, configuración
│   └── Dockerfile
│
├── MICRO-NOTIFICACIONES/           ← Microservicio simulado
│   ├── src/main/java/
│   │   └── controllers/
│   └── Dockerfile
│
└── docker-compose.yml
```

---

## Requisitos previos

- Docker y Docker Compose instalados
- (Opcional para ejecución local) Java 21 y PostgreSQL

---

## Ejecución con Docker

**1. Clona el repositorio:**
```bash
git clone https://github.com/tu-usuario/tu-repo.git
cd tu-repo
```

**2. Levanta todos los servicios:**
```bash
docker-compose up --build
```

Esto levanta automáticamente:
- PostgreSQL en el puerto `5432`
- Microservicio de notificaciones en el puerto `8081`
- API principal en el puerto `8080`

**3. Para detener:**
```bash
docker-compose down
```

**4. Para detener y eliminar la base de datos:**
```bash
docker-compose down -v
```

---

## Ejecución en local (sin Docker)

**Requisitos:**
- PostgreSQL corriendo en `localhost:5432`
- Base de datos creada con nombre `reservas`

**Variables necesarias en `application.properties`** (ya configuradas con valores por defecto):
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/reservas
spring.datasource.username=postgres
spring.datasource.password=1234
notificaciones.url=http://localhost:8081/notificaciones
jwt.secret=dGVzdFNlY3JldEtleVBhcmFKV1RGaXJtYWRvQ29uSFMyNTY=
jwt.expiration=21600000
```

Levanta primero el microservicio de notificaciones (puerto 8081) y luego la API principal (puerto 8080).

---

## Usuario administrador precargado

Al iniciar la aplicación se crea automáticamente un usuario ADMIN:

```
email:    admin@mail.com
password: admin
```

---

## Autenticación

Todos los endpoints (excepto `/auth/login`) requieren token JWT.

**1. Obtener token:**
```
POST /auth/login
{
  "email": "admin@mail.com",
  "password": "admin"
}
```

**2. Usar el token en cada request:**
```
Header: Authorization: Bearer <token>
```

El token tiene una expiración de **6 horas**.

---

## Endpoints principales

### Autenticación
| Método | Endpoint | Acceso |
|--------|----------|--------|
| POST | `/auth/login` | Público |
| POST | `/auth/register` | ADMIN |
| POST | `/auth/logout` | Autenticado |

### Usuarios
| Método | Endpoint | Acceso |
|--------|----------|--------|
| POST | `/usuarios/crear` | ADMIN |
| GET | `/usuarios/listar` | ADMIN |
| GET | `/usuarios/consultar/{id}` | ADMIN |
| PUT | `/usuarios/editar/{id}` | ADMIN |
| DELETE | `/usuarios/eliminar/{id}` | ADMIN |

### Sucursales
| Método | Endpoint | Acceso |
|--------|----------|--------|
| POST | `/sucursales/crear` | ADMIN |
| GET | `/sucursales/listar` | ADMIN, GESTOR |
| GET | `/sucursales/consultar/{id}` | ADMIN, GESTOR |
| PUT | `/sucursales/editar/{id}` | ADMIN |
| DELETE | `/sucursales/eliminar/{id}` | ADMIN |

### Salones
| Método | Endpoint | Acceso |
|--------|----------|--------|
| POST | `/salones/crear` | ADMIN |
| GET | `/salones/listar` | ADMIN, GESTOR |
| GET | `/salones/consultar/{id}` | ADMIN, GESTOR |
| PUT | `/salones/editar/{id}` | ADMIN |
| DELETE | `/salones/eliminar/{id}` | ADMIN |

### Reservas
| Método | Endpoint | Acceso |
|--------|----------|--------|
| POST | `/reservas/crear` | ADMIN, GESTOR |
| GET | `/reservas/listar` | ADMIN, GESTOR |
| GET | `/reservas/consultar/{id}` | ADMIN, GESTOR |
| PUT | `/reservas/editar/{id}` | ADMIN, GESTOR |
| DELETE | `/reservas/eliminar/{id}` | ADMIN |
| POST | `/reservas/finalizar` | ADMIN, GESTOR |
| POST | `/reservas/{id}/aprobar` | ADMIN |
| POST | `/reservas/{id}/rechazar` | ADMIN |
| GET | `/reservas/listar/{salonId}/activas` | ADMIN, GESTOR |
| GET | `/reservas/buscar-documento?documentoCliente=` | ADMIN, GESTOR |

### Indicadores
| Método | Endpoint | Acceso |
|--------|----------|--------|
| GET | `/indicadores/top-clientes` | ADMIN, GESTOR |
| GET | `/indicadores/top-clientes/salon/{salonId}` | ADMIN, GESTOR |
| GET | `/indicadores/clientes-primera-vez` | ADMIN, GESTOR |
| GET | `/indicadores/ganancias/{salonId}?periodo=dia\|semana\|mes\|anio` | GESTOR |
| GET | `/indicadores/top-sucursales` | ADMIN |
| GET | `/indicadores/reservas-activas/{salonId}` | ADMIN, GESTOR |

### Notificaciones
| Método | Endpoint | Acceso |
|--------|----------|--------|
| POST | `/notificaciones` | ADMIN |

---

## Reglas de negocio destacadas

- **Reserva premium:** si el costo estimado supera $500.000 (horas × tarifa), la reserva queda en estado `PENDIENTE_APROBACION` y requiere aprobación manual del ADMIN.
- **Expiración automática:** las reservas en `PENDIENTE_APROBACION` con más de 48 horas se marcan como `EXPIRADAS`.
- **Documento del cliente:** entre 6 y 12 caracteres, solo numéricos.
- **Cálculo de horas:** se redondea hacia arriba (fracción = hora completa).
- **Histórico:** al finalizar una reserva se mueve a la tabla `reserva_historico` con la fecha real y el cobro total.

---

## Microservicio de Notificaciones

Servicio independiente que simula el envío de correos. Recibe:

```json
{
  "email": "",
  "documento": "",
  "mensaje": "",
  "salonNombre": ""
}
```

Imprime la solicitud en log y retorna:
```json
{
  "mensaje": "Notificación Enviada"
}
```

La API principal transforma el `salonId` a `salonNombre` antes de llamarlo, y valida que el cliente tenga una reserva activa en el salón indicado.

---

## Modelo entidad-relación

```
USUARIO
  id, nombre, email, password, rol_id → ROLES

ROLES
  id, nombre (ADMIN | GESTOR)


SUCURSAL
  id, nombre
  └── gestor_id → USUARIO

SALON
  id, nombre, capacidad_maxima, costo_hora
  └── sucursal_id → SUCURSAL

RESERVAS
  id, documento_cliente, nombre_cliente,
  fecha_inicio, fecha_fin_estimada, fecha_creacion,
  asistentes, estado, motivo_rechazo
  └── salon_id → SALON

RESERVA_HISTORICO
  id, reserva_id, documento_cliente, nombre_cliente,
  fecha_inicio, fecha_fin_estimada, fecha_fin_real,
  fecha_creacion, asistentes, costo_estimado,
  total_cobrado, motivo_rechazo, estado
  └── salon_id → SALON
```

---

## Colección Postman

Importa el archivo `Nelumbo_Reservas.postman_collection.json` incluido en el repositorio. Contiene todos los endpoints documentados con headers, body y ejemplos de respuesta.

Configura una variable de entorno:
- `base_url` → `http://localhost:8080`
- `jwt_token` → se llena automáticamente al hacer login



## Comandos para levantar el api

# Construir y levantar todos los servicios
docker-compose up --build

# Solo levantar (si ya está construido)


# Levantar en segundo plano
docker-compose up -d

# Ver logs de un servicio específico
docker-compose logs -f api-reservas

# Detener todo
docker-compose down

# Detener y borrar volúmenes (borra la BD también)
docker-compose down -v