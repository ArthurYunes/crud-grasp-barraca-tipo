# crud-grasp-barraca-tipo

Sistema de gestão de feira livre — Tema 2: **Barraca / TipoBarraca**  
Implementado em Java puro (JDK 11+), com menu textual no terminal e persistência em arquivo JSON local.

---

## Integrantes do Grupo

| Nome | Matrícula |
|------|-----------|
| Arthur Yunes | 2512843 |

---

## Como Compilar e Executar

### Pré-requisitos
- JDK 11 ou superior instalado
- Terminal (Linux/macOS) ou Prompt de Comando/PowerShell (Windows)

### Passo a Passo

**1. Clone o repositório**
```bash
git clone https://github.com/ArthurYunes/crud-grasp-barraca-tipo.git
cd crud-grasp-barraca-tipo
```

**2. Compile todos os arquivos Java**

Linux/macOS:
```bash
mkdir -p out
find src -name "*.java" > sources.txt
javac -d out @sources.txt
```

**Windows (PowerShell):**
```powershell
mkdir out
$sources = Get-ChildItem -Recurse -Filter "*.java" src | ForEach-Object { $_.FullName }
javac --release 11 -d out $sources
```

**3. Execute a aplicação**

Linux/macOS:
```bash
java -cp out feira.graspcrud.Main
```

**3. Execute a aplicação**
```powershell
java -cp out feira.graspcrud.Main
```

> **Atenção:** Se tiver mais de uma versão do Java instalada, certifique-se de que o `java` e o `javac` usados são da mesma versão (JDK 11 ou superior).

> Os arquivos JSON serão criados automaticamente na pasta `data/` no diretório onde o comando for executado.

---

## Estrutura do Projeto

```
crud-grasp-barraca-tipo/
├── src/
│   └── feira/
│       └── graspcrud/
│           ├── Main.java
│           ├── controller/
│           │   └── BarracaController.java
│           ├── domain/
│           │   ├── Barraca.java
│           │   └── TipoBarraca.java
│           ├── dto/
│           │   ├── BarracaRequest.java
│           │   └── TipoBarracaRequest.java
│           ├── exception/
│           │   └── RegraNegocioException.java
│           ├── repository/
│           │   ├── BarracaRepository.java       ← interface
│           │   ├── TipoBarracaRepository.java   ← interface
│           │   └── json/
│           │       ├── BarracaRepositoryJson.java
│           │       └── TipoBarracaRepositoryJson.java
│           ├── service/
│           │   ├── BarracaService.java
│           │   └── TipoBarracaService.java
│           └── util/
│               └── JsonMini.java
├── data/                    ← criado automaticamente na primeira execução
│   ├── barracas.json
│   └── tipos-barraca.json
└── README.md
```

---

## Padrões GRASP Aplicados

| Padrão GRASP | Classe(s) | Como foi aplicado |
|---|---|---|
| **Information Expert** | `Barraca`, `TipoBarraca` | Validações de nome (obrigatório, mín. 3 chars), de `tipoBarracaId` (obrigatório) e `ativo` ficam nas próprias entidades, que são as especialistas em seus dados. |
| **Creator** | `Main` | Instancia e conecta manualmente todos os repositórios, serviços e o controller, pois é quem tem o contexto global necessário para criar essas dependências. |
| **Controller** | `BarracaController` | Único ponto de entrada das interações do usuário via menu textual. Lê a entrada, delega aos serviços e exibe o resultado — sem implementar regras de negócio. |
| **Low Coupling** | `BarracaService`, `TipoBarracaService` | Dependem das interfaces `BarracaRepository` e `TipoBarracaRepository`, nunca das implementações concretas JSON. As entidades de domínio não importam repositórios nem serviços. |
| **High Cohesion** | Todas as classes | Cada classe tem uma única responsabilidade: `BarracaService` trata casos de uso de Barraca; `JsonMini` cuida da serialização; `BarracaController` cuida da interação com o usuário. |
| **Pure Fabrication** | `JsonMini`, `BarracaRepositoryJson`, `TipoBarracaRepositoryJson` | Classes fabricadas sem correspondência no domínio, criadas exclusivamente para isolar os detalhes de persistência JSON e manter as entidades de domínio livres de infraestrutura. |
| **Indirection** | `BarracaRepositoryJson`, `TipoBarracaRepositoryJson` | Intermediárias entre os serviços e o arquivo JSON. Os serviços nunca conhecem o arquivo — dependem apenas das interfaces. |
| **Protected Variations** | `BarracaRepository`, `TipoBarracaRepository` | Interfaces que protegem serviços e domínio de variações na implementação de persistência. Trocar JSON por banco de dados exige apenas criar nova implementação das interfaces, sem alterar domínio ou serviços. |

---

## Regras de Negócio Implementadas

### Regras Comuns (todos os temas)
- O campo `nome` da Barraca é obrigatório e deve ter no mínimo 3 caracteres.
- O `TipoBarraca` associado à Barraca é obrigatório no cadastro.
- O campo `nome` do `TipoBarraca` é obrigatório e **único** (não permite duplicatas).
- Não é permitido remover um `TipoBarraca` que esteja em uso por alguma Barraca.
- Operações inválidas exibem mensagem de erro clara via `RegraNegocioException`.

### Regras Específicas — Tema 2 (Barraca / TipoBarraca)
- `Barraca.ativo` indica se a barraca está em operação.
- A listagem padrão exibe apenas barracas ativas; existe opção separada no menu para listar todas (incluindo inativas).
- Não é permitido cadastrar duas barracas com o mesmo nome.

---

## Persistência JSON

Os dados são persistidos na pasta `data/` no diretório de execução:

- `data/tipos-barraca.json` — lista de TipoBarraca
- `data/barracas.json` — lista de Barraca

Ao iniciar, a aplicação carrega automaticamente os dados existentes.  
Após cada operação de escrita (criar, atualizar, remover), o arquivo JSON é atualizado imediatamente.

Exemplo de conteúdo gerado:

**`data/tipos-barraca.json`**
```json
[
  {"id": 1, "nome": "Hortifruti"},
  {"id": 2, "nome": "Laticínios"}
]
```

**`data/barracas.json`**
```json
[
  {"id": 1, "nome": "Barraca do João", "ativo": true, "tipoBarracaId": 1},
  {"id": 2, "nome": "Queijaria Minas", "ativo": false, "tipoBarracaId": 2}
]
```

---

## Operações Disponíveis no Menu

| # | Operação |
|---|---|
| 1 | Cadastrar TipoBarraca |
| 2 | Listar TipoBarracas |
| 3 | Cadastrar Barraca (associada a um TipoBarraca) |
| 4 | Listar Barracas Ativas |
| 5 | Listar Todas as Barracas (incluindo inativas) |
| 6 | Buscar Barraca por ID |
| 7 | Atualizar Barraca |
| 8 | Remover Barraca |
