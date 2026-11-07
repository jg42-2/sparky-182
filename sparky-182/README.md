# sparky-182 🎸

Backend de práctica para el laboratorio de la **PC2 de CS2031 DBP** (React I y II).
Mezcla las dos PCs del curso con temática de **blink-182**:

- **Sparky The Horizon** → álbumes, canciones y favoritos (sin GET de favoritos).
- **SparkyRoll** → historial con status que se actualiza (sin GET de historial).
- **Extras** → detalle de álbum, reseñas (201 / 204 / 403 / 409), búsqueda paginada, tours con compra de entradas, perfil con `PATCH` y un panel solo para `ADMIN`.

Mismo stack que los backends del curso: **Spring Boot 3.4 · Java 21 · H2 en memoria · JWT · BCrypt**.

```
sparky-182/
├── backend/        ← Spring Boot (esto es lo que corres)
│   └── api.http    ← todas las peticiones listas para probar (extensión REST Client)
├── mock-backend/   ← plan B en Node, sin dependencias, mismos endpoints
└── frontend/       ← lo creas tú con Vite (ver abajo)
```

---

## 1. Requisitos

| Herramienta | Comprobar | Nota |
|---|---|---|
| JDK 21 | `java -version` | Si `mvnw` pide `JAVA_HOME`, crea la variable apuntando a la carpeta del JDK |
| Node.js LTS (20+) | `node -v` | |
| Git | `git --version` | |
| VS Code | | Al abrir el repo te sugerirá las extensiones de `.vscode/extensions.json` |

No necesitas instalar Maven: el proyecto trae el wrapper (`mvnw` / `mvnw.cmd`).

## 2. Subir a GitHub y clonar

```bash
# dentro de la carpeta sparky-182 descomprimida
git init
git add .
git commit -m "sparky-182: backend inicial"
git branch -M main
git remote add origin https://github.com/TU-USUARIO/sparky-182.git   # repo vacío creado en GitHub
git push -u origin main
```

Luego, como el día de la PC: en VS Code, `Ctrl+Shift+P` → **Git: Clone** → pega la URL → **Open**.

## 3. Levantar el backend

```powershell
cd backend
.\mvnw.cmd spring-boot:run      # Windows (PowerShell)
./mvnw spring-boot:run          # Mac / Linux  (si dice "Permission denied": chmod +x mvnw)
```

Listo cuando veas `Started Sparky182Application`. Prueba en el navegador:
<http://localhost:8080/api/albums>

- Consola de H2: <http://localhost:8080/h2-console> (JDBC URL `jdbc:h2:mem:sparky182`, usuario `sa`, sin contraseña).
- La base es **en memoria**: al reiniciar se borra todo lo que creaste y se vuelven a sembrar los datos.

### Usuarios de prueba

| Email | Contraseña | Rol |
|---|---|---|
| `fan@sparky182.com` | `fan123` | USER |
| `ana@sparky182.com` | `ana123` | USER |
| `admin@sparky182.com` | `admin123` | ADMIN |

### Plan B: mock en Node

Si Spring no arranca y quieres seguir practicando el frontend:

```bash
node mock-backend/server.mjs     # desde la raíz; usa el mismo puerto 8080
```

Imita los mismos endpoints, códigos y mensajes. No lo uses para aprender el backend: es solo un respaldo.

## 4. Crear el frontend

Desde la raíz `sparky-182` (no dentro de `backend`):

```bash
npm create vite@latest frontend -- --template react-ts     # si pregunta "start now?", responde No
cd frontend
npm install
npm install axios react-router-dom
npm install tailwindcss @tailwindcss/vite
npm run dev                                                # http://localhost:5173
```

`vite.config.ts`:

```ts
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";

export default defineConfig({
  plugins: [react(), tailwindcss()],
});
```

`src/index.css` → una sola línea: `@import "tailwindcss";`

El backend tiene CORS abierto, así que el frontend en `:5173` puede llamar a `:8080` sin configurar nada.

---

## 5. Endpoints

Base: `http://localhost:8080/api`. **token** = header `Authorization: Bearer <token>`.

### Auth (público)

| Método | Ruta | Body | Respuesta |
|---|---|---|---|
| POST | `/auth/register` | `{ email, password, name }` | `200 { token }` · `400` validación · `409 "Email already in use"` |
| POST | `/auth/login` | `{ email, password }` | `200 { token }` · `401 "Invalid credentials"` |

Validación: email con formato válido, `password` de 6 caracteres o más, `name` no vacío.

### Álbumes y canciones (público)

| Método | Ruta | Respuesta |
|---|---|---|
| GET | `/albums` | `[{ id, title, year, coverUrl, lineup }]` (ordenados por año) |
| GET | `/albums/{albumId}` | `{ id, title, year, coverUrl, lineup, songCount, averageRating, reviewCount }` · `404` |
| GET | `/albums/{albumId}/songs` | `[{ id, title, duration, trackNumber, albumId, albumTitle }]` · `404` |
| GET | `/songs?search=&page=0&size=10` | `Page<Song>` (ver formato abajo) · `size` entre 1 y 50 |

### Favoritos (token) — igual que Sparky The Horizon

| Método | Ruta | Body | Respuesta |
|---|---|---|---|
| POST | `/user/favorites` | `{ songId }` | `200` sin body · `404 "Song not found: X"` |
| DELETE | `/user/favorites` | `{ songId }` | `200` sin body |

⚠️ **No existe `GET /user/favorites`**: el frontend tiene que guardar su propia lista. En Axios, el DELETE con body es `api.delete(url, { data: body })`.

### Historial (token) — igual que SparkyRoll, con álbumes

| Método | Ruta | Body | Respuesta |
|---|---|---|---|
| POST | `/user/history` | `{ albumId, status }` | `200` sin body. Si ya existe, **actualiza** el status |
| DELETE | `/user/history` | `{ albumId }` | `200` sin body |

`status` solo puede ser `"escuchando"` o `"escuchado"` (en minúsculas), si no `400`. ⚠️ **No existe GET de historial.**

### Perfil (token)

| Método | Ruta | Body | Respuesta |
|---|---|---|---|
| GET | `/user/me` | — | `{ id, name, email, role }` |
| PATCH | `/user/me` | `{ name }` | el usuario actualizado |

### Reseñas

| Método | Ruta | Auth | Body | Respuesta |
|---|---|---|---|---|
| GET | `/albums/{albumId}/reviews` | — | — | `[{ id, albumId, rating, comment, authorName, authorEmail, createdAt }]` |
| POST | `/albums/{albumId}/reviews` | token | `{ rating, comment }` | **`201`** con la reseña · `409` si ya reseñaste ese álbum |
| DELETE | `/reviews/{reviewId}` | token | — | **`204`** · `403` si no es tuya (el ADMIN puede borrar cualquiera) |

`rating` entre 1 y 5; `comment` no vacío y de máximo 280 caracteres.

### Tours (fechas **ficticias**)

| Método | Ruta | Auth | Body | Respuesta |
|---|---|---|---|---|
| GET | `/tours?city=&page=0&size=5` | — | — | `Page<Tour>`: `{ id, city, country, venue, date, ticketsAvailable, price, soldOut }` |
| GET | `/tours/{tourId}` | — | — | `Tour` · `404` |
| POST | `/tours/{tourId}/tickets` | token | `{ quantity }` (1 a 6) | `{ tourId, city, quantity, remaining }` · `400 "Not enough tickets: only N left"` |
| POST | `/admin/tours` | **ADMIN** | `{ city, country, venue, date: "2027-08-15", ticketsAvailable, price }` | **`201`** con la fecha · `400` con `fields` |
| DELETE | `/admin/tours/{tourId}` | **ADMIN** | — | **`204`** |

### Formato de página

```json
{
  "content": [ ... ],
  "number": 0,
  "size": 5,
  "totalElements": 12,
  "totalPages": 3,
  "first": true,
  "last": false
}
```

### Errores

Todos los errores llegan con la misma forma:

```json
{ "error": "Album not found: 99" }
```

Los de validación (`@Valid`) agregan el detalle por campo:

```json
{ "error": "city: city is required", "fields": { "city": "city is required", "date": "date is required" } }
```

| Código | Cuándo |
|---|---|
| 400 | Validación, JSON mal formado, parámetro inválido, sin stock |
| 401 | Sin token, token inválido o vencido, o credenciales malas en el login |
| 403 | Tienes token pero no permiso (no eres ADMIN, o la reseña no es tuya) |
| 404 | El recurso no existe |
| 405 | El método no existe en esa ruta (por ejemplo, `GET /user/favorites`) |
| 409 | Conflicto: email repetido o reseña repetida |

> **Diferencia con los backends del curso:** allí Spring responde **403** cuando falta el token (no configuran un *entry point*). sparky-182 separa **401** (no sé quién eres) de **403** (sé quién eres, pero no puedes). En la PC, tu interceptor debe tratar 401 **y** 403 como "sesión vencida".

---

## 6. Datos sembrados

- 9 álbumes de blink-182 (de *Cheshire Cat* a *One More Time...*) con 5 canciones cada uno. Títulos reales; **duraciones aproximadas**. Las portadas son imágenes de relleno (`placehold.co`).
- 5 reseñas de usuarios ficticios y 12 fechas de tour **inventadas** (no son conciertos reales).

## 7. Problemas comunes

| Síntoma | Solución |
|---|---|
| `mvnw` no se reconoce | Estás fuera de `backend`, o en PowerShell te faltó `.\` |
| `JAVA_HOME not defined` / versión incorrecta | Instala JDK 21 y define `JAVA_HOME`; abre una terminal nueva |
| `Port 8080 already in use` | Cierra el otro backend (Ctrl+C en su terminal) o cambia `server.port` en `application.properties` |
| El frontend dice "No se pudo conectar con el servidor" | El backend no está corriendo, o la `baseURL` está mal |
| Todo lo protegido da 401 después de reiniciar | Normal: H2 se borró y tu token apunta a un usuario que ya no existe. Vuelve a hacer login |
