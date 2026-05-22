# Running Tracker

Backend Spring Boot para registrar e consultar atividades de corrida. Este arquivo orienta agentes de código; detalhes de produto ficam no README quando existir.

**Escopo:** repositório raiz (`com.runningTracker`). Em monorepos futuros, o `AGENTS.md` mais próximo do arquivo editado prevalece.

## Stack

- Java 21 (Gradle toolchain)
- Spring Boot 4.x — Web MVC, Docker Compose dev, H2 + PostgreSQL
- GraalVM Native Image (plugin configurado; build nativo opcional)
- Testes: JUnit 5 via `./gradlew test`

## Estrutura

```
src/main/java/com/runningTracker/     # aplicação e camadas futuras
src/main/resources/                   # application.properties, migrations
src/test/java/com/runningTracker/     # testes
compose.yaml                          # Postgres local (dev)
build.gradle / settings.gradle
```

## Comandos

```bash
./gradlew build
```

```bash
./gradlew test
```

```bash
./gradlew bootRun
```

```bash
docker compose -f compose.yaml up -d
```

Preferir testes focados (`--tests com.runningTracker...`) durante iteração.

## Princípios Arquiteturais 

- SOLID 
- APIs Rest
- Separação clara de responsabilidades
- Baixo acoplamento
- Alta coesão

## Convenções

- Pacote base: `com.runningTracker` (manter consistência com o código existente).
- Camadas sugeridas: `controller` → `service` → `repository` / domínio; DTOs separados de entidades JPA quando aplicável.
- Config sensível em `application-local.properties` (gitignored); nunca commitar senhas ou tokens.
- Mudanças pequenas e focadas; alinhar estilo ao código ao redor (indentação com tab no Java gerado pelo Spring Initializr).

### Código 

- Código em inglês 
- Classes com responsabilidade única 
- Evite métodos com mais de 20 linhas 
- Não usar comentários desnecessários 
- Utilizar código autoexplicativo

### API 

- Seguir padrão REST 
- Respostas padronizadas 
- HTTP com status correto 
- Versionamento via `/v1`

### Testes

- Todo caso de uso deve possuir testes 
- Testes unitários obrigatórios 
- Testes de integração para endpoints críticos 
- Projeto com pelo menos 90% de cobertura de testes

## Guardrails

- Não commitar `.env`, `application-local.*`, `build/`, `.gradle/`, artefatos de IDE.
- Não rodar `git push --force` em `main` sem pedido explícito.
- Validar com `./gradlew test` (ou build) após mudanças em lógica ou API.
- Postgres local usa credenciais de desenvolvimento em `compose.yaml` — não reutilizar em produção.

## Regras Cursor

Instruções persistentes em `.cursor/rules/`:

- `project-core.mdc` — sempre ativo (comandos e workflow)
- `java-spring-boot.mdc` — código Java e Gradle
- `spring-tests.mdc` — testes Spring Boot

---

# Fluxo entre agentes 

## Product Agent 

Responsável por: 

- Definir requisitos 
- Trazer os critérios de aceite 
- Criar e documentar as regras de negócio 
- Criar e documentar casos de uso 

Saída esperada: 

- arquivos markdown em `/doc/product` 

---

## Tech Lead Agent

Responsável por: 

- Quebrar requisitos 
- Definir arquitetura 
- Criar tasks técnicas 
- Validar impactos 

Saída esperada: 

- arquivos markdown em `/doc/architecture` 

---

## Developer Agent 

Responsável por: 

- Implementar código utilizando o TDD
- Criar testes
- Seguir padrões e convenções do projeto

Nunca: 

- Alterar arquitetura sem aprovação
- ignorar testes 
- Finalizar sem testes de todos os casos de uso

---

## QA Agent

Responsável por: 

- Validar regra de negócio 
- revisar código
- executar testes 
- identificar bugs e riscos

---

# Definition of Done

Um item só é finalizado quando: 

- Código estiver compilando 
- testes unitários passando com o percentual de cobertura definido
- revisão aprovada 
- documentação atualizada 



# Regras gerais 

- Nunca gerar código sem contexto suficente 
- Sempre validar impacto antes de alterar contratos 
- Evitar dependências desnecessárias
- Questionar caso precise adicionar dependências 
- Priorizar simplicidade

