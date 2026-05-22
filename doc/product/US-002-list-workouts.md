# US-002 — Listar treinos

## História

**Como** corredor que usa o Running Tracker  
**Quero** listar meus treinos cadastrados  
**Para** visualizar histórico e acompanhar evolução

---

## Critérios de aceitação

### Listagem

- [ ] `GET /v1/workouts` retorna treinos cadastrados.
- [ ] Resposta paginada (Spring `Page` ou estrutura equivalente padronizada no projeto).
- [ ] Parâmetros opcionais:
  - `page` (default `0`)
  - `size` (default `20`, máx. `100`)
  - `sort` default: `workoutDate,desc` (mais recente primeiro)
- [ ] Cada item expõe os mesmos campos do detalhe: `id`, `title`, `workoutDate`, `distanceKm`, `duration`, `durationSeconds`, `stravaUrl`, `paceMinPerKm`.
- [ ] Lista vazia → `200` com conteúdo vazio (não `404`).

### Consulta por id (complemento ao MVP)

- [ ] `GET /v1/workouts/{id}` documentado em [US-001](US-001-register-workout.md); listagem e detalhe compartilham representação do recurso.

### Filtros (v1 mínimo)

- [ ] Sem filtros obrigatórios na v1 (retorna todos os treinos paginados).
- [ ] *(Opcional futuro)* filtro por intervalo de `workoutDate` — fora do MVP salvo pedido do Tech Lead.

---

## Regras de negócio

| ID | Regra |
|----|--------|
| RN-101 | Ordenação padrão: data do treino decrescente. |
| RN-102 | `size` acima do máximo → `400` ou cap silencioso no máximo (**Tech Lead**: preferir `400` com mensagem clara). |
| RN-103 | `page` negativa → `400`. |

---

## Fluxos alternativos

| Cenário | Comportamento |
|---------|----------------|
| FA-101 | `page`/`size` inválidos (não numéricos) → `400` |
| FA-102 | `size` = 0 → `400` |
| FA-103 | Página além do total → `200` com lista vazia |

---

## Edge cases

| Caso | Tratamento |
|------|------------|
| Muitos registros | Paginação obrigatória; não retornar lista ilimitada |
| Empate na mesma `workoutDate` | Ordenação secundária por `id` desc para ordem estável |

---

## Contrato API (v1)

**Request** `GET /v1/workouts?page=0&size=20&sort=workoutDate,desc`

**Response** `200 OK`

```json
{
  "content": [
    {
      "id": 2,
      "title": "Easy run",
      "workoutDate": "2026-05-22",
      "distanceKm": 5.0,
      "duration": "28:00",
      "durationSeconds": 1680,
      "stravaUrl": null,
      "paceMinPerKm": 5.6
    },
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
  ],
  "page": 0,
  "size": 20,
  "totalElements": 2,
  "totalPages": 1
}
```

*(Formato exato do envelope de paginação a padronizar no Tech Lead alinhado ao restante da API.)*

---

## Definition of Ready

- [x] Escopo e ordenação definidos
- [x] Paginação e campos alinhados à US-001
- [x] Tech Lead: [mvp-workouts-v1.md](../architecture/mvp-workouts-v1.md)
