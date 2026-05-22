# US-001 — Registrar treino

## História

**Como** corredor que usa o Running Tracker  
**Quero** registrar meus treinos com distância, tempo e data  
**Para** acompanhar minha evolução ao longo do tempo

---

## Escopo

Criação de treino e consulta por identificador. Listagem: [US-002](US-002-list-workouts.md). Edição e exclusão fora do MVP.

---

## Decisões de produto (validadas)

| Tema | Decisão |
|------|---------|
| Pace | **min/km** = (duração em minutos) ÷ distância em km |
| Data | **Dia civil do usuário** (`YYYY-MM-DD`), sem converter fuso na data armazenada |
| Duração (entrada) | String **`mm:ss`** (ex.: `53:00`) |
| Limites km/tempo | **Sem teto**; apenas valores estritamente maiores que zero |
| Strava | Opcional; host permitido: `strava.com` ou `www.strava.com` |
| Listagem | Incluída no MVP via [US-002](US-002-list-workouts.md) |

---

## Critérios de aceitação

### Campos obrigatórios

- [ ] **Título** obrigatório (trim; não vazio; máx. 120 caracteres).
- [ ] **Data de realização** obrigatória em `YYYY-MM-DD` (dia civil informado pelo cliente).
- [ ] **Distância (km)** obrigatória, numérica, **> 0** (sem limite máximo).
- [ ] **Duração** obrigatória no formato **`mm:ss`** (minutos:segundos, segundos `00–59`).

### Campo opcional

- [ ] **Link Strava** opcional; se informado: URL `http`/`https` com host `strava.com` ou `www.strava.com`.

### Cálculo de pace

- [ ] Pace em **min/km**, calculado na criação:

  `paceMinPerKm = (durationSeconds / 60) / distanceKm`

- [ ] Arredondar para exibição/persistência com **2 casas decimais** (half-up).
- [ ] Pace **não editável** no request; sempre derivado de km + duração.
- [ ] Retornar campo `paceMinPerKm` na resposta.

**Exemplo:** 10,5 km em `53:00` → 3180 s → 53 min → pace = 53 / 10,5 ≈ **5,05 min/km**.

### Validação de data (dia civil)

- [ ] Persistir `workoutDate` como data local (tipo data, sem horário).
- [ ] Rejeitar data **futura** em relação ao “hoje” do usuário:
  - Se o request enviar header `X-User-Local-Date: YYYY-MM-DD`, usar essa data como referência.
  - Caso contrário, usar `LocalDate.now()` do servidor (**limitação v1** documentada para clientes sem header).
- [ ] `workoutDate` no passado ou igual a hoje: permitido.

### API

- [ ] `POST /v1/workouts` → `201 Created`.
- [ ] `GET /v1/workouts/{id}` → `200` ou `404`.
- [ ] Validação → `400` com erros por campo.
- [ ] Contrato sob prefixo `/v1`.

### Formato `mm:ss`

- [ ] Padrão: `M:SS` ou `MM:SS` (1–3 dígitos nos minutos aceitos na v1, ex.: `5:30`, `90:00`).
- [ ] Regex sugerida: `^(\d{1,3}):([0-5]\d)$`.
- [ ] Converter para `durationSeconds` internamente (ex.: `53:00` → 3180).
- [ ] Rejeitar `0:00`, `5:60`, formato sem `:`, ou apenas segundos.

---

## Regras de negócio

| ID | Regra |
|----|--------|
| RN-001 | Título, data, km e duração (`mm:ss`) são obrigatórios na criação. |
| RN-002 | `distanceKm` > 0 (sem limite superior). |
| RN-003 | Duração convertida para segundos > 0. |
| RN-004 | `workoutDate` não pode ser posterior ao dia civil de referência (header ou fallback servidor). |
| RN-005 | `stravaUrl`, se presente: URL válida e host `strava.com` / `www.strava.com`. |
| RN-006 | `paceMinPerKm` é calculado; não aceitar override no body. |
| RN-007 | *(Processo)* Tarefas concluídas não voltam para *Open*. |
| RN-008 | *(Processo)* Falha na `main` → abrir bug antes de novo merge. |

---

## Fluxos alternativos

| Cenário | Comportamento |
|---------|----------------|
| FA-001 | Campo obrigatório ausente → `400` |
| FA-002 | Data inválida → `400` (`YYYY-MM-DD`) |
| FA-003 | Data futura → `400` |
| FA-004 | Km ≤ 0 ou inválido → `400` |
| FA-005 | `mm:ss` inválido ou duração zero → `400` |
| FA-006 | Strava com host não permitido → `400` |
| FA-007 | Strava omitido → `stravaUrl: null` |
| FA-008 | Header `X-User-Local-Date` inválido → ignorar e usar fallback servidor (ou `400` — Tech Lead define comportamento único) |

---

## Edge cases

| Caso | Tratamento |
|------|------------|
| Título só espaços | `400` |
| Km `0.001` ou valores muito pequenos | Permitir se > 0 |
| Ultramaratona (`24:00:00` em mm:ss = `1440:00`?) | Formato mm:ss limita minutos a 3 dígitos (max 999:59) — suficiente sem teto de produto |
| Duplicata título + mesma data | Permitido na v1 |
| Strava `http://strava.com/...` | Aceitar se host válido |
| Strava `https://www.google.com` | `400` |
| Pace | Sempre finito se km > 0 |

---

## Contrato API (v1)

**Request** `POST /v1/workouts`

```json
{
  "title": "Long run",
  "workoutDate": "2026-05-20",
  "distanceKm": 10.5,
  "duration": "53:00",
  "stravaUrl": "https://www.strava.com/activities/123456"
}
```

**Response** `201 Created`

```json
{
  "id": 1,
  "title": "Long run",
  "workoutDate": "2026-05-20",
  "distanceKm": 10.5,
  "duration": "53:00",
  "durationSeconds": 3180,
  "stravaUrl": "https://www.strava.com/activities/123456",
  "paceMinPerKm": 5.05
}
```

---

## Definition of Ready

- [x] Pace, data, duração, limites, Strava e escopo definidos
- [x] Fórmula e campos da API acordados
- [x] Tech Lead: [mvp-workouts-v1.md](../architecture/mvp-workouts-v1.md)
