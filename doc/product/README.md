# Product — Running Tracker

Documentação de requisitos (Product Agent).

| ID | Documento | Status |
|----|-----------|--------|
| US-001 | [Registrar treino](US-001-register-workout.md) | Pronto para Tech Lead |
| US-002 | [Listar treinos](US-002-list-workouts.md) | Pronto para Tech Lead |

## MVP — Treinos (v1)

| Endpoint | US |
|----------|-----|
| `POST /v1/workouts` | US-001 |
| `GET /v1/workouts/{id}` | US-001 |
| `GET /v1/workouts` | US-002 |

**Decisões globais:** pace em min/km (2 decimais); data como dia civil; duração `mm:ss` na API; sem limite de km/tempo; Strava apenas `strava.com` / `www.strava.com`.

Arquitetura: [doc/architecture/mvp-workouts-v1.md](../architecture/mvp-workouts-v1.md) (9 tasks).

Próximo passo: **Developer Agent** — TASK-001 em diante (TDD).
