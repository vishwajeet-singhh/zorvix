# Orvix

**Local-first, privacy-first AI code review assistant.**

Orvix reviews your current Git branch **before** you open a Pull Request. It diffs your branch
against a base branch, understands the surrounding codebase, runs static analysis, applies
software-engineering principles, and produces actionable feedback — **entirely on your machine**.

- 🔒 **Private** — no source code ever leaves your machine. No cloud AI. All inference runs
  locally via [Ollama](https://ollama.com).
- 👀 **Read-only advisor** — Orvix never edits code, commits, branches, or PRs. The only file it
  writes is `.orvix/report.md`.
- 🧠 **Principled** — reviews for correctness, security, reliability, performance, concurrency,
  Spring/REST/DB conventions, and SOLID / DRY / KISS / YAGNI / architectural drift.

---

## Requirements

- **Java 21+**
- One of:
  - [Ollama](https://ollama.com/download) installed and running, **or**
  - [Docker](https://docs.docker.com/get-docker/) — Orvix will start Ollama for you on demand.

You do **not** need to pre-install the model. If `qwen2.5-coder:7b` is missing, Orvix pulls it
automatically on first use. If Ollama isn't running and Docker is available, Orvix starts it.

## Quick start

```bash
# Build
./gradlew build

# Run from the project you want to review (any git repo):
./gradlew bootRun --args="review"
./gradlew bootRun --args="review --base main"
./gradlew bootRun --args='ask "Is this implementation thread safe?"'
./gradlew bootRun --args="explain src/main/java/com/example/UserService.java"
./gradlew bootRun --args="health"
./gradlew bootRun --args="version"
```

> When packaged as an executable jar (`./gradlew bootJar`), invoke it as `java -jar orvix.jar <command>`.
> A thin `orvix` wrapper script can alias that for convenience.

## Commands

| Command | Description |
|---|---|
| `orvix review [--base <branch>]` | Review the current branch against a base branch. |
| `orvix ask "<question>"` | Ask a question grounded in your current changes. |
| `orvix chat [--base <branch>]` | **Interactive session**: loads context once, then answers follow-ups quickly. |
| `orvix explain <file>` | Explain a file: responsibility, dependencies, risks, design. |
| `orvix health` | Check git / Docker / Ollama / model status. |
| `orvix version` | Print the Orvix version. |

## How `review` works

1. Detect the repository and current branch.
2. Resolve the base branch (`--base`, else first of `origin/dev`, `origin/develop`,
   `origin/main`, `origin/master`, then local equivalents).
3. Compute the `merge-base` and diff `base...HEAD`.
4. Index the project and pull in **only the relevant** surrounding source (interfaces, parent
   classes, referenced project types) within a token budget — never the whole repo.
5. Run static analysis (PMD) on the changed Java files.
6. Send the diff + context + static findings to the local model and request structured findings.
7. Merge findings, compute a deterministic score, and produce:
   - a terminal summary, and
   - a Markdown report at `.orvix/report.md`.

## Performance

Most of a command's time is the local model generating its answer (~tens of seconds on the 7B
model); the rest is one-off setup. Orvix minimises the setup cost:

- **Interactive `orvix chat`** builds the diff + project context **once**, warms the model, then
  answers follow-up questions quickly — each turn pays only for generation, not re-indexing or a
  fresh JVM. The context is sent as a constant prefix so Ollama reuses its KV cache across turns.
- **Cached project index** — type/package extraction uses a fast scan and is cached in
  `.orvix/cache/index.json` keyed by file mtime+size, so unchanged files are never re-read on the
  next run.
- **`keep_alive`** keeps the model resident between commands, avoiding reload stalls.

To make a *single* answer faster, use a smaller model (e.g. `qwen2.5-coder:3b`) via
`orvix.ollama.model`, reduce `orvix.ollama.num-ctx`, or run Ollama on a GPU.

> `.orvix/` holds Orvix's own report and cache. Add it to your `.gitignore`. Orvix still never
> touches your source, build, config, or git history.

## Configuration

Configuration lives in `src/main/resources/application.yaml` under `orvix.*` and can be
overridden via environment variables or `--orvix.…` system properties.

```yaml
orvix:
  ollama:
    base-url: http://localhost:11434
    model: qwen2.5-coder:7b
    auto-start: true     # start Ollama via Docker if not reachable
    auto-pull: true      # pull the model if missing
  review:
    base-branch-priority: [origin/dev, origin/develop, origin/main, origin/master]
    max-context-chars: 24000
    max-related-files: 12
    report-dir: .orvix
```

## Running Ollama yourself (optional)

```bash
docker compose up -d
```

Orvix talks to `http://localhost:11434` regardless of how Ollama is started.

## Distribution (for end users) — planned

> Publishing/release automation is intentionally **not** implemented yet. This section documents
> the intended cross-platform (macOS / Windows / Linux) options and the end-user command each
> produces, so the channel can be chosen later. In every option, Orvix still auto-provisions
> Ollama (starts it via Docker and pulls the model on first use), so the install step only needs
> to deliver Orvix itself.

| Channel | Platforms | End-user install | Then run | Needs installed | Effort to publish |
|---|---|---|---|---|---|
| **GraalVM native binary** ⭐ | mac · win · linux | `curl -fsSL …/install.sh \| sh` (mac/Linux), `irm …/install.ps1 \| iex` (Windows) | `orvix review` | nothing (self-contained) | High (reflection config for PMD/JGit/Jackson) |
| **Jar + launcher scripts** | mac · win · linux | install script downloads `orvix.jar` + `orvix`/`orvix.bat` | `orvix review` | Java 21 | Low |
| **Docker image + compose** | mac · win · linux | `docker pull ghcr.io/<you>/orvix` | `docker compose run orvix review` | Docker only | Medium |
| **Homebrew** | mac · linux only | `brew install <you>/tap/orvix` | `orvix review` | Java 21 | Medium (needs a tap repo) |

**Recommended:** the **GraalVM native binary** gives the best experience on all three OSes — a
single `orvix` executable, no JVM, sub-second startup — with Docker needed only where it belongs
(running Ollama). The **jar + launcher** route is the low-risk fallback if you want to ship
quickly and don't mind requiring Java 21. All require a one-time publish to GitHub Releases (and,
for Docker, a container registry).

> Note: Homebrew does not support Windows, so it can't be the sole channel for a mac/win/linux goal.

## Tech stack

Java 21 · Spring Boot 3.5 · Picocli · JGit · JavaParser · PMD · Ollama (`qwen2.5-coder:7b`) · JUnit 5

## Privacy & safety guarantees

Orvix is an **advisor, not an editor**. It never modifies source, build files, configuration,
git history, branches, or pull requests. Source code is sent only to your **local** Ollama
instance. The single artifact it writes is `.orvix/report.md`.

## License

[MIT](LICENSE)
