# Desenvolvimento de Aplicacoes Moveis e Distribuidas

Repositorio incremental da disciplina **Desenvolvimento de Aplicacoes Moveis e Distribuidas**.

Este repositorio sera atualizado continuamente ao longo da materia, conforme novos roteiros, laboratorios, exercicios, evidencias e relatorios forem sendo desenvolvidos. A ideia e manter um historico organizado da evolucao tecnica, com commits pequenos e uma estrutura facil de navegar.

## Objetivo

Centralizar as atividades praticas da disciplina, registrando:

- implementacoes em Java, Python e outras tecnologias usadas nos roteiros;
- respostas teoricas e relatorios de laboratorio;
- evidencias de execucao quando solicitadas;
- evolucao incremental por commits;
- decisoes tecnicas relevantes para revisao futura.

## Estrutura Atual

```text
.
|-- labdamd/
|   |-- src/
|   |-- pom.xml
|   `-- RELATORIO_ROTEIRO_1.md
|
|-- lab-redes/
|   |-- java/
|   |-- python/
|   |-- evidencias/
|   |-- README.md
|   `-- RESPOSTAS.md
|
|-- .gitignore
|-- LICENSE
`-- README.md
```

## Atividades

| Pasta | Tema | Tecnologias | Situacao |
| --- | --- | --- | --- |
| `labdamd/` | Roteiro 1 - Threads em Java | Java 21, Maven | Implementado |
| `lab-redes/` | Roteiro 2 - Revisao de Redes de Computadores | Java, Python, Maven, WebSocket | Implementado com evidencias |

## `labdamd/` - Roteiro 1

Laboratorio de revisao de sistemas operacionais e concorrencia, com foco em threads.

Conteudos implementados:

- criacao de threads com `extends Thread`;
- criacao de tarefas com `implements Runnable`;
- experimento com muitas threads nativas;
- uso de `ExecutorService`;
- uso de Virtual Threads;
- relatorio com respostas e comparacao entre abordagens.

Comandos principais:

```powershell
cd labdamd
mvn compile
java -cp target/classes com.labdamd.Main
```

Para executar uma parte especifica:

```powershell
java -cp target/classes com.labdamd.Main A
java -cp target/classes com.labdamd.Main B
java -cp target/classes com.labdamd.Main C
java -cp target/classes com.labdamd.Main D
java -cp target/classes com.labdamd.Main E
```

## `lab-redes/` - Roteiro 2

Laboratorio de revisao de redes de computadores, usando o cenario de uma central de avisos da turma.

Protocolos implementados:

- TCP em Java e Python;
- UDP em Java e Python;
- Multicast em Java e Python;
- WebSocket em Java e Python.

Tambem foram incluidos:

- `RESPOSTAS.md` com as respostas das questoes do roteiro;
- `README.md` especifico com comandos de execucao;
- pasta `evidencias/` com prints de execucao real dos 8 exemplos exigidos.

Comandos de entrada:

```powershell
cd lab-redes
```

Para detalhes de execucao de cada protocolo, consulte:

```text
lab-redes/README.md
```

## Evidencias

Alguns roteiros exigem prints de tela comprovando a execucao real dos programas. Quando isso acontecer, as evidencias devem ficar dentro da pasta da atividade correspondente.

No Roteiro 2, por exemplo, os prints oficiais estao em:

```text
lab-redes/evidencias/
|-- tcp/
|-- udp/
|-- multicast/
`-- websocket/
```

Os prints devem mostrar comandos executados, saidas dos servidores/clientes e, quando solicitado, a saida de `Get-Date`.

## Convencoes do Repositorio

Este repositorio deve continuar crescendo de forma incremental. Para manter a organizacao:

- cada roteiro ou laboratorio deve ter sua propria pasta;
- cada atividade deve ter um `README.md` quando houver comandos especificos;
- respostas teoricas devem ficar em arquivos claros, como `RESPOSTAS.md` ou `RELATORIO_*.md`;
- evidencias devem ficar em uma pasta chamada `evidencias/`;
- arquivos gerados por build nao devem ser versionados;
- commits devem ser pequenos, objetivos e semanticamente nomeados.

## Padrao de Commits

Sempre que possivel, usar mensagens curtas em ingles no formato semantico:

```text
feat: add TCP clients and servers for network lab
docs: add network lab execution guide
test: add network lab execution evidence
```

Tipos comuns:

| Tipo | Uso |
| --- | --- |
| `feat` | nova implementacao ou funcionalidade |
| `fix` | correcao de comportamento |
| `docs` | documentacao, respostas ou relatorios |
| `test` | testes, evidencias ou validacoes |
| `chore` | configuracao, estrutura ou manutencao |
| `refactor` | reorganizacao sem mudar comportamento |

## Requisitos Gerais

Os requisitos podem mudar conforme a atividade, mas atualmente o repositorio usa:

- Java JDK 17+;
- Java 21+ para atividades com Virtual Threads;
- Maven 3.8+;
- Python 3.10+;
- pacote Python `websockets` para o laboratorio de WebSocket.

Verificacoes uteis:

```powershell
java -version
mvn -version
python --version
git status
```

## Como Navegar

Para revisar uma atividade:

1. Abra a pasta do roteiro.
2. Leia o `README.md` ou relatorio da atividade.
3. Compile ou execute os exemplos conforme indicado.
4. Confira as respostas teoricas.
5. Verifique as evidencias, quando existirem.

## Proximas Atualizacoes

Conforme a disciplina avancar, novas pastas poderao ser adicionadas para:

- aplicacoes moveis;
- APIs e servicos distribuidos;
- comunicacao cliente-servidor;
- persistencia e integracao com banco de dados;
- autenticacao, mensageria ou sincronizacao;
- trabalhos avaliativos e projetos finais.

Este README deve ser atualizado sempre que uma nova atividade relevante entrar no repositorio.
