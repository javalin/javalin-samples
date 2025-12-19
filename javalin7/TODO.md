# Javalin 7 Migration TODO

## Completed Modules ✅
- javalin-auth-example
- javalin-cors-example
- javalin-email-example
- javalin-heroku-example
- javalin-html-forms-example
- javalin-jetty-sessions-example
- javalin-kotlin-example
- javalin-modern-java-example
- javalin-prometheus-example
- javalin-realtime-collaboration-example
- javalin-testing-example
- javalin-vuejs-example
- javalin-websocket-example
- javalinstagram
- javalinvue-example/javalinvue2-example
- javalinvue-example/javalinvue3-example

## Modules Staying on Javalin 6 (Not Migrating)
- javalin-async-example (fixed SSL plugin import, moved routes to config)
- javalin-http2-example (fixed SSL plugin import, moved routes to config)

## Modules Needing Migration

### javalin-openapi-example
**Status:** Skipped for now
**Reason:** User requested to skip this module

## Notes

### Common Migration Patterns
1. **Routing API:** `config.router.mount` → `config.routes` or `config.routes.apiBuilder`
2. **SSL Plugin:** `SSLPlugin` → `SslPlugin` (lowercase 'sl')
3. **Jetty Session Handler:** Package changes from `org.eclipse.jetty.server.session.*` to `org.eclipse.jetty.ee10.servlet.SessionHandler` and `org.eclipse.jetty.session.*`
4. **Kotlin Scope Functions:** Use `also` instead of `apply` for nested blocks to access both receiver and outer context
5. **Java/Kotlin Versions:** Java 17+, Kotlin 2.2.20
6. **Maven Compiler Plugin:** 3.13.0
7. **Compression API:** `config.http.brotliAndGzipCompression()` → `config.http.compressionStrategy = CompressionStrategy.GZIP` (import `io.javalin.compression.CompressionStrategy`)
8. **Vue Plugin:** `config.vue.apply { ... }` → `config.registerPlugin(JavalinVuePlugin { vue -> ... })` (import `io.javalin.plugin.bundled.JavalinVuePlugin`)

### Jetty 12 Breaking Changes

#### StatisticsHandler API Changes (RESOLVED in prometheus example)
- **Removed**: `getRequests()` → **Use**: `getRequestTotal()` (old method deprecated)
- **Removed**: `getDispatched*()`, `getAsync*()`, `getExpires()` → **No direct replacement** (removed metrics)
- **Removed**: `getStatsOnMs()` → **Use**: `getStatisticsDuration().toMillis()`
- **Removed**: `getResponsesBytesTotal()` → **Use**: `getBytesWritten()` (similar metric)
- **New in Jetty 12**: `getHandle*()` methods for handler execution metrics
- **New in Jetty 12**: `getHandlingFailures()`, `getBytesRead()`, `getBytesWritten()`
- **Note**: Time values changed from milliseconds to nanoseconds - divide by 1_000_000_000.0 for seconds

#### Session Management
- Session management uses EE10 packages
- Property names changed (e.g., `httpOnly` → `isHttpOnly`)

