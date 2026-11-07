// sparky-182 · MOCK del backend (plan B, solo Node, sin dependencias)
// Úsalo SOLO si el backend Spring no te arranca y quieres seguir practicando el frontend.
//   node mock-backend/server.mjs      ->  http://localhost:8080/api
// Imita los mismos endpoints, códigos HTTP y mensajes { error } que el backend Java.
// Todo vive en memoria: al reiniciarlo se borra (igual que H2).
import http from "node:http";

const SEED = {"albums": [{"title": "Cheshire Cat", "year": 1995, "lineup": "Hoppus · DeLonge · Raynor", "songs": [["Carousel", "2:52"], ["M+M's", "2:38"], ["Wasting Time", "2:21"], ["Strings", "2:33"], ["Does My Breath Smell?", "2:36"]]}, {"title": "Dude Ranch", "year": 1997, "lineup": "Hoppus · DeLonge · Raynor", "songs": [["Pathetic", "2:28"], ["Voyeur", "2:43"], ["Dammit", "2:45"], ["Josie", "3:20"], ["Apple Shampoo", "2:52"]]}, {"title": "Enema of the State", "year": 1999, "lineup": "Hoppus · DeLonge · Barker", "songs": [["Dumpweed", "2:23"], ["Aliens Exist", "3:12"], ["What's My Age Again?", "2:28"], ["Adam's Song", "4:09"], ["All the Small Things", "2:48"]]}, {"title": "Take Off Your Pants and Jacket", "year": 2001, "lineup": "Hoppus · DeLonge · Barker", "songs": [["Anthem Part Two", "3:48"], ["First Date", "2:51"], ["The Rock Show", "2:51"], ["Stay Together for the Kids", "3:59"], ["Reckless Abandon", "3:06"]]}, {"title": "blink-182", "year": 2003, "lineup": "Hoppus · DeLonge · Barker", "songs": [["Feeling This", "2:53"], ["I Miss You", "3:47"], ["Down", "3:03"], ["Always", "4:11"], ["Stockholm Syndrome", "2:42"]]}, {"title": "Neighborhoods", "year": 2011, "lineup": "Hoppus · DeLonge · Barker", "songs": [["Ghost on the Dance Floor", "4:12"], ["Natives", "3:52"], ["Up All Night", "4:04"], ["After Midnight", "3:22"], ["Heart's All Gone", "3:05"]]}, {"title": "California", "year": 2016, "lineup": "Hoppus · Skiba · Barker", "songs": [["Cynical", "1:56"], ["Bored to Death", "3:55"], ["She's Out of Her Mind", "2:43"], ["Kings of the Weekend", "2:58"], ["California", "3:09"]]}, {"title": "Nine", "year": 2019, "lineup": "Hoppus · Skiba · Barker", "songs": [["The First Time", "3:02"], ["Happy Days", "3:07"], ["Darkside", "2:59"], ["Blame It on My Youth", "3:01"], ["I Really Wish I Hated You", "3:24"]]}, {"title": "One More Time...", "year": 2023, "lineup": "Hoppus · DeLonge · Barker", "songs": [["Anthem Part 3", "2:42"], ["Dance with Me", "2:48"], ["One More Time", "3:20"], ["Edging", "2:44"], ["Fell in Love", "2:44"]]}], "users": [{"email": "admin@sparky182.com", "password": "admin123", "name": "Admin Sparky", "role": "ADMIN"}, {"email": "fan@sparky182.com", "password": "fan123", "name": "Fan Demo", "role": "USER"}, {"email": "ana@sparky182.com", "password": "ana123", "name": "Ana Pop-Punk", "role": "USER"}], "reviews": [{"author": "fan@sparky182.com", "album": "Enema of the State", "rating": 5, "comment": "El disco con el que empecé. Cero canciones de relleno."}, {"author": "ana@sparky182.com", "album": "Enema of the State", "rating": 4, "comment": "Clásico total, aunque prefiero el sonido de Dude Ranch."}, {"author": "ana@sparky182.com", "album": "Dude Ranch", "rating": 5, "comment": "Crudo y rápido. Dammit en bucle."}, {"author": "fan@sparky182.com", "album": "blink-182", "rating": 5, "comment": "El más maduro. I Miss You no envejece."}, {"author": "ana@sparky182.com", "album": "California", "rating": 3, "comment": "Tiene buenos momentos, pero se siente largo."}], "tours": [{"city": "Lima", "country": "Perú", "venue": "Estadio Nacional", "date": "2027-03-06", "ticketsAvailable": 1200, "price": 280.0}, {"city": "Santiago", "country": "Chile", "venue": "Movistar Arena", "date": "2027-03-09", "ticketsAvailable": 0, "price": 310.0}, {"city": "Buenos Aires", "country": "Argentina", "venue": "Estadio Obras", "date": "2027-03-12", "ticketsAvailable": 3, "price": 295.0}, {"city": "São Paulo", "country": "Brasil", "venue": "Allianz Parque", "date": "2027-03-15", "ticketsAvailable": 900, "price": 330.0}, {"city": "Bogotá", "country": "Colombia", "venue": "Movistar Arena", "date": "2027-03-19", "ticketsAvailable": 650, "price": 260.0}, {"city": "Ciudad de México", "country": "México", "venue": "Palacio de los Deportes", "date": "2027-03-23", "ticketsAvailable": 1500, "price": 300.0}, {"city": "San Diego", "country": "EE. UU.", "venue": "Pechanga Arena", "date": "2027-04-02", "ticketsAvailable": 400, "price": 350.0}, {"city": "Los Ángeles", "country": "EE. UU.", "venue": "Kia Forum", "date": "2027-04-04", "ticketsAvailable": 20, "price": 390.0}, {"city": "Toronto", "country": "Canadá", "venue": "Scotiabank Arena", "date": "2027-04-10", "ticketsAvailable": 800, "price": 340.0}, {"city": "Londres", "country": "Reino Unido", "venue": "The O2", "date": "2027-05-01", "ticketsAvailable": 1100, "price": 360.0}, {"city": "Madrid", "country": "España", "venue": "WiZink Center", "date": "2027-05-05", "ticketsAvailable": 0, "price": 320.0}, {"city": "Tokio", "country": "Japón", "venue": "Makuhari Messe", "date": "2027-05-20", "ticketsAvailable": 700, "price": 410.0}]};

// ---------- "base de datos" ----------
let ids = { album: 0, song: 0, user: 0, review: 0, tour: 0 };
const next = (k) => ++ids[k];
const albums = [], songs = [], users = [], reviews = [], tours = [];
const favorites = new Map(); // userId -> Set(songId)
const history = new Map();   // "userId:albumId" -> status

for (const a of SEED.albums) {
  const album = {
    id: next("album"), title: a.title, year: a.year, lineup: a.lineup,
    coverUrl: "https://placehold.co/400x400/111827/f472b6/png?text=" + encodeURIComponent(a.title).replace(/%20/g, "+"),
  };
  albums.push(album);
  a.songs.forEach(([title, duration], i) =>
    songs.push({ id: next("song"), title, duration, trackNumber: i + 1, albumId: album.id }));
}
for (const u of SEED.users) users.push({ id: next("user"), ...u });
let clock = Date.now() - 86400000 * 10;
for (const r of SEED.reviews) {
  const user = users.find((u) => u.email === r.author);
  const album = albums.find((a) => a.title === r.album);
  reviews.push({ id: next("review"), userId: user.id, albumId: album.id, rating: r.rating, comment: r.comment, createdAt: iso(clock += 3600000) });
}
for (const t of SEED.tours) tours.push({ id: next("tour"), ...t });

function iso(ms) { return new Date(ms).toISOString().slice(0, 19); }

// ---------- DTOs (mismos campos que los records de Java) ----------
const albumSummary = (a) => ({ id: a.id, title: a.title, year: a.year, coverUrl: a.coverUrl, lineup: a.lineup });
const songDto = (s) => ({ id: s.id, title: s.title, duration: s.duration, trackNumber: s.trackNumber, albumId: s.albumId, albumTitle: albums.find((a) => a.id === s.albumId).title });
const reviewDto = (r) => { const u = users.find((x) => x.id === r.userId); return { id: r.id, albumId: r.albumId, rating: r.rating, comment: r.comment, authorName: u.name, authorEmail: u.email, createdAt: r.createdAt }; };
const tourDto = (t) => ({ id: t.id, city: t.city, country: t.country, venue: t.venue, date: t.date, ticketsAvailable: t.ticketsAvailable, price: t.price, soldOut: t.ticketsAvailable === 0 });
const meDto = (u) => ({ id: u.id, name: u.name, email: u.email, role: u.role });
const page = (list, number, size) => {
  const totalPages = Math.ceil(list.length / size);
  return { content: list.slice(number * size, number * size + size), number, size, totalElements: list.length, totalPages, first: number === 0, last: number >= totalPages - 1 };
};

// ---------- errores ----------
class HttpError extends Error { constructor(status, message, extra = {}) { super(message); this.status = status; this.extra = extra; } }
const fail = (status, message, extra) => { throw new HttpError(status, message, extra); };
function validate(rules) { // rules: [field, ok, message][]
  const fields = {};
  for (const [field, ok, msg] of rules) if (!ok && !(field in fields)) fields[field] = msg;
  const keys = Object.keys(fields);
  if (keys.length) fail(400, keys[0] + ": " + fields[keys[0]], { fields });
}
const blank = (v) => typeof v !== "string" || v.trim() === "";
const isInt = (v) => Number.isInteger(v);
const toId = (raw, name) => { if (!/^\d+$/.test(raw)) fail(400, `Invalid value for '${name}'`); return Number(raw); };
const intParam = (q, name, def) => { const v = q.get(name); if (v === null) return def; if (!/^-?\d+$/.test(v)) fail(400, `Invalid value for '${name}'`); return Number(v); };
const checkPage = (p, s) => { if (p < 0) fail(400, "page must be >= 0"); if (s < 1 || s > 50) fail(400, "size must be between 1 and 50"); };

// ---------- auth ----------
const tokenFor = (email) => "mock." + Buffer.from(JSON.stringify({ sub: email, exp: Date.now() + 86400000 })).toString("base64url");
function userFromToken(header) {
  if (!header?.startsWith("Bearer ")) return null;
  try {
    const { sub, exp } = JSON.parse(Buffer.from(header.slice(7).replace(/^mock\./, ""), "base64url").toString());
    if (exp < Date.now()) return null;
    return users.find((u) => u.email === sub) ?? null; // usuario borrado => anónimo => 401
  } catch { return null; }
}

// ---------- rutas ----------
const routes = [];
const route = (method, pattern, opts, handler) => routes.push({ method, re: new RegExp("^" + pattern.replace(/:(\w+)/g, "(?<$1>[^/]+)") + "$"), opts, handler });
const PUBLIC = { auth: false }, AUTH = { auth: true }, ADMIN = { auth: true, admin: true };

route("POST", "/api/auth/register", PUBLIC, ({ body }) => {
  validate([["email", !blank(body.email), "email is required"], ["email", blank(body.email) || /^[^@\s]+@[^@\s]+$/.test(body.email), "email is not valid"],
    ["password", !blank(body.password), "password is required"], ["password", blank(body.password) || body.password.length >= 6, "password must have at least 6 characters"],
    ["name", !blank(body.name), "name is required"]]);
  const email = body.email.trim().toLowerCase();
  if (users.some((u) => u.email === email)) fail(409, "Email already in use");
  users.push({ id: next("user"), email, password: body.password, name: body.name.trim(), role: "USER" });
  return [200, { token: tokenFor(email) }];
});
route("POST", "/api/auth/login", PUBLIC, ({ body }) => {
  validate([["email", !blank(body.email), "email is required"], ["email", blank(body.email) || /^[^@\s]+@[^@\s]+$/.test(body.email), "email is not valid"],
    ["password", !blank(body.password), "password is required"]]);
  const user = users.find((u) => u.email === body.email.trim().toLowerCase() && u.password === body.password);
  if (!user) fail(401, "Invalid credentials");
  return [200, { token: tokenFor(user.email) }];
});

const albumOr404 = (id) => albums.find((a) => a.id === id) ?? fail(404, "Album not found: " + id);
route("GET", "/api/albums", PUBLIC, () => [200, [...albums].sort((a, b) => a.year - b.year).map(albumSummary)]);
route("GET", "/api/albums/:albumId", PUBLIC, ({ p }) => {
  const id = toId(p.albumId, "albumId"); const a = albumOr404(id);
  const rs = reviews.filter((r) => r.albumId === id);
  const avg = rs.length ? Math.round((rs.reduce((s, r) => s + r.rating, 0) / rs.length) * 10) / 10 : 0;
  return [200, { ...albumSummary(a), songCount: songs.filter((s) => s.albumId === id).length, averageRating: avg, reviewCount: rs.length }];
});
route("GET", "/api/albums/:albumId/songs", PUBLIC, ({ p }) => {
  const id = toId(p.albumId, "albumId"); albumOr404(id);
  return [200, songs.filter((s) => s.albumId === id).sort((a, b) => a.trackNumber - b.trackNumber).map(songDto)];
});
route("GET", "/api/albums/:albumId/reviews", PUBLIC, ({ p }) => {
  const id = toId(p.albumId, "albumId"); albumOr404(id);
  return [200, reviews.filter((r) => r.albumId === id).sort((a, b) => b.createdAt.localeCompare(a.createdAt)).map(reviewDto)];
});
route("POST", "/api/albums/:albumId/reviews", AUTH, ({ p, body, user }) => {
  const id = toId(p.albumId, "albumId");
  validate([["rating", body.rating != null, "rating is required"], ["rating", body.rating == null || (isInt(body.rating) && body.rating >= 1 && body.rating <= 5), "rating must be between 1 and 5"],
    ["comment", !blank(body.comment), "comment is required"], ["comment", typeof body.comment !== "string" || body.comment.length <= 280, "comment must be at most 280 characters"]]);
  albumOr404(id);
  if (reviews.some((r) => r.userId === user.id && r.albumId === id)) fail(409, "You already reviewed this album");
  const r = { id: next("review"), userId: user.id, albumId: id, rating: body.rating, comment: body.comment.trim(), createdAt: iso(Date.now()) };
  reviews.push(r);
  return [201, reviewDto(r)];
});
route("DELETE", "/api/reviews/:reviewId", AUTH, ({ p, user }) => {
  const id = toId(p.reviewId, "reviewId");
  const i = reviews.findIndex((r) => r.id === id);
  if (i < 0) fail(404, "Review not found: " + id);
  if (reviews[i].userId !== user.id && user.role !== "ADMIN") fail(403, "You can only delete your own reviews");
  reviews.splice(i, 1);
  return [204];
});

route("GET", "/api/songs", PUBLIC, ({ q }) => {
  const search = (q.get("search") ?? "").trim().toLowerCase(), pg = intParam(q, "page", 0), size = intParam(q, "size", 10);
  checkPage(pg, size);
  return [200, page(songs.filter((s) => s.title.toLowerCase().includes(search)).sort((a, b) => a.id - b.id).map(songDto), pg, size)];
});

const tourOr404 = (id) => tours.find((t) => t.id === id) ?? fail(404, "Tour date not found: " + id);
route("GET", "/api/tours", PUBLIC, ({ q }) => {
  const city = (q.get("city") ?? "").trim().toLowerCase(), pg = intParam(q, "page", 0), size = intParam(q, "size", 5);
  checkPage(pg, size);
  return [200, page(tours.filter((t) => t.city.toLowerCase().includes(city)).sort((a, b) => a.date.localeCompare(b.date)).map(tourDto), pg, size)];
});
route("GET", "/api/tours/:tourId", PUBLIC, ({ p }) => [200, tourDto(tourOr404(toId(p.tourId, "tourId")))]);
route("POST", "/api/tours/:tourId/tickets", AUTH, ({ p, body }) => {
  const id = toId(p.tourId, "tourId");
  validate([["quantity", body.quantity != null, "quantity is required"], ["quantity", body.quantity == null || (isInt(body.quantity) && body.quantity >= 1 && body.quantity <= 6), "quantity must be between 1 and 6"]]);
  const t = tourOr404(id);
  if (t.ticketsAvailable < body.quantity) fail(400, `Not enough tickets: only ${t.ticketsAvailable} left`);
  t.ticketsAvailable -= body.quantity;
  return [200, { tourId: t.id, city: t.city, quantity: body.quantity, remaining: t.ticketsAvailable }];
});
route("POST", "/api/admin/tours", ADMIN, ({ body }) => {
  const date = body.date === "" ? null : body.date;
  if (date != null && !/^\d{4}-\d{2}-\d{2}$/.test(date)) fail(400, "Malformed or missing JSON body");
  const today = new Date().toISOString().slice(0, 10);
  validate([["city", !blank(body.city), "city is required"], ["country", !blank(body.country), "country is required"], ["venue", !blank(body.venue), "venue is required"],
    ["date", date != null, "date is required"], ["date", date == null || date > today, "date must be in the future"],
    ["ticketsAvailable", body.ticketsAvailable != null, "ticketsAvailable is required"], ["ticketsAvailable", body.ticketsAvailable == null || body.ticketsAvailable >= 0, "ticketsAvailable must be >= 0"],
    ["price", body.price != null, "price is required"], ["price", body.price == null || body.price > 0, "price must be > 0"]]);
  const t = { id: next("tour"), city: body.city.trim(), country: body.country.trim(), venue: body.venue.trim(), date, ticketsAvailable: body.ticketsAvailable, price: body.price };
  tours.push(t);
  return [201, tourDto(t)];
});
route("DELETE", "/api/admin/tours/:tourId", ADMIN, ({ p }) => {
  const t = tourOr404(toId(p.tourId, "tourId"));
  tours.splice(tours.indexOf(t), 1);
  return [204];
});

route("GET", "/api/user/me", AUTH, ({ user }) => [200, meDto(user)]);
route("PATCH", "/api/user/me", AUTH, ({ user, body }) => {
  validate([["name", !blank(body.name), "name is required"], ["name", typeof body.name !== "string" || body.name.length <= 60, "name must be at most 60 characters"]]);
  user.name = body.name.trim();
  return [200, meDto(user)];
});
route("POST", "/api/user/favorites", AUTH, ({ user, body }) => {
  validate([["songId", body.songId != null, "songId is required"]]);
  if (!songs.some((s) => s.id === body.songId)) fail(404, "Song not found: " + body.songId);
  if (!favorites.has(user.id)) favorites.set(user.id, new Set());
  favorites.get(user.id).add(body.songId);
  return [200];
});
route("DELETE", "/api/user/favorites", AUTH, ({ user, body }) => {
  validate([["songId", body.songId != null, "songId is required"]]);
  favorites.get(user.id)?.delete(body.songId);
  return [200];
});
route("POST", "/api/user/history", AUTH, ({ user, body }) => {
  validate([["albumId", body.albumId != null, "albumId is required"], ["status", !blank(body.status), "status is required"],
    ["status", blank(body.status) || /^(escuchando|escuchado)$/.test(body.status), "status must be 'escuchando' or 'escuchado'"]]);
  albumOr404(body.albumId);
  history.set(`${user.id}:${body.albumId}`, body.status);
  return [200];
});
route("DELETE", "/api/user/history", AUTH, ({ user, body }) => {
  validate([["albumId", body.albumId != null, "albumId is required"]]);
  history.delete(`${user.id}:${body.albumId}`);
  return [200];
});

// ---------- servidor ----------
const PUBLIC_GET = ["/api/albums", "/api/songs", "/api/tours"];
const server = http.createServer(async (req, res) => {
  const origin = req.headers.origin;
  if (origin) {
    res.setHeader("Access-Control-Allow-Origin", origin);
    res.setHeader("Access-Control-Allow-Credentials", "true");
    res.setHeader("Vary", "Origin");
  }
  if (req.method === "OPTIONS") {
    res.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,PATCH,DELETE,OPTIONS");
    res.setHeader("Access-Control-Allow-Headers", req.headers["access-control-request-headers"] ?? "*");
    res.writeHead(200); return res.end();
  }
  const url = new URL(req.url, "http://localhost");
  const send = (status, body) => {
    if (body === undefined) { res.writeHead(status, { "Content-Length": 0 }); return res.end(); }
    res.writeHead(status, { "Content-Type": "application/json" });
    res.end(JSON.stringify(body));
  };
  try {
    const user = userFromToken(req.headers.authorization);
    const raw = await new Promise((ok) => { let d = ""; req.on("data", (c) => (d += c)); req.on("end", () => ok(d)); });
    const sameUrl = routes.filter((r) => r.re.test(url.pathname));
    const match = sameUrl.find((r) => r.method === req.method);
    const isPublic = url.pathname.startsWith("/api/auth/") || (req.method === "GET" && PUBLIC_GET.some((pre) => url.pathname === pre || url.pathname.startsWith(pre + "/")));
    if (!isPublic && !user) return send(401, { error: "Unauthorized: missing or invalid token" });
    if (url.pathname.startsWith("/api/admin/") && user?.role !== "ADMIN") return send(403, { error: "Forbidden: admin role required" });
    if (!match) return sameUrl.length ? send(405, { error: `Method ${req.method} not allowed here` }) : send(404, { error: "Endpoint not found" });
    let body = {};
    if (["POST", "PATCH", "PUT", "DELETE"].includes(req.method) && match.handler.length) {
      try { body = JSON.parse(raw); } catch { if (["POST", "PATCH"].includes(req.method) || url.pathname.startsWith("/api/user/")) return send(400, { error: "Malformed or missing JSON body" }); }
      if (body === null || typeof body !== "object") body = {};
    }
    const [status, payload] = match.handler({ p: url.pathname.match(match.re).groups ?? {}, q: url.searchParams, body, user });
    send(status, payload);
  } catch (e) {
    if (e instanceof HttpError) return send(e.status, { error: e.message, ...e.extra });
    console.error(e);
    send(500, { error: "Internal server error" });
  }
});
const PORT = Number(process.env.PORT ?? 8080);
server.listen(PORT, () => console.log(`sparky-182 MOCK escuchando en http://localhost:${PORT}/api  (usuarios demo: fan@sparky182.com / fan123, admin@sparky182.com / admin123)`));
