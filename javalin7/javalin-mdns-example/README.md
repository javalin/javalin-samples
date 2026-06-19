# javalin-mdns-example

A tiny Javalin server that advertises itself over **mDNS** (multicast DNS / Zeroconf), so it's
reachable at `http://javalin-demo.local` from other machines on the same LAN — no DNS config, no
hardcoded IPs. On startup it wires [JmDNS](https://github.com/jmdns/jmdns) directly: it publishes
the `javalin-demo.local` hostname and registers an `_http._tcp` service on port `80` (so the URL needs
no port suffix), and it unregisters/closes JmDNS when the server stops.

## Run it

Java or Kotlin `main` — both do the same thing. The server binds on port `80`, which needs elevated
privileges, so run it with `sudo` (or from an IDE/terminal that already has the rights):

```bash
sudo mvn -q compile exec:java -Dexec.mainClass=JavalinMdnsExampleApp
```

Then open `http://javalin-demo.local` (or `http://localhost` on this machine).

## The `.local` caveat

mDNS is **link-local only**: it works on the same LAN/subnet and does not route across networks or
the internet. Resolving `*.local` needs a local mDNS resolver — built in on macOS, Avahi on Linux,
Bonjour on Windows. On macOS you may also need to grant the app **Local Network** permission
(System Settings → Privacy & Security → Local Network) before discovery works.
