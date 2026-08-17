# Relatorio - Roteiro 1: Threads em Java

## Objetivo

O laboratorio compara processos e threads e implementa cinco formas de observar concorrencia em Java:

- Parte A: `extends Thread`
- Parte B: `implements Runnable`
- Parte C: muitas threads nativas do sistema operacional
- Parte D: `ExecutorService` com pool fixo
- Parte E: Virtual Threads, disponiveis a partir do Java 21

## Como executar

Compile o projeto:

```bash
mvn compile
```

Execute as partes A, B, D e E pelo Java:

```bash
java -cp target/classes com.labdamd.Main
```

Ou pelo Maven:

```bash
mvn exec:java
```

Execute uma parte especifica:

```bash
java -cp target/classes com.labdamd.Main A
java -cp target/classes com.labdamd.Main B
java -cp target/classes com.labdamd.Main C
java -cp target/classes com.labdamd.Main D
java -cp target/classes com.labdamd.Main DC
java -cp target/classes com.labdamd.Main E
```

Para reduzir ou aumentar os experimentos:

```bash
java "-Dlabdamd.native.total=1000" -cp target/classes com.labdamd.Main C
java "-Dlabdamd.virtual.total=100000" -cp target/classes com.labdamd.Main E
```

## Revisao conceitual

### Processo x thread

Um processo e uma instancia de um programa em execucao, com memoria propria e recursos controlados pelo sistema operacional. No laboratorio, a JVM e o processo. Uma thread e uma linha de execucao dentro desse processo. Threads compartilham a memoria do processo, mas possuem sua propria pilha de execucao e sao escalonadas pela CPU.

Na analogia do guiche de atendimento, o processo e a agencia inteira: possui espaco, recursos, regras e fila de clientes. As threads sao os atendentes trabalhando dentro da mesma agencia. Eles compartilham informacoes da agencia, mas cada um executa um atendimento por vez.

### Ciclo de vida da thread

Uma thread nasce em `NEW`, quando o objeto foi criado mas `start()` ainda nao foi chamado. Depois passa para `RUNNABLE`, estado em que esta pronta para executar ou executando. Durante a execucao pode ficar `BLOCKED`, esperando um lock, ou `WAITING`/`TIMED_WAITING`, esperando uma chamada como `join()` ou `sleep()`. Ao final do metodo `run()`, entra em `TERMINATED`.

## Respostas das partes

### Parte A

Com 5 atendimentos de 1 segundo iniciados com `start()`, o tempo total fica perto de 1 segundo, nao de 5 segundos. Isso acontece porque os atendimentos rodam de forma concorrente em threads diferentes. O `join()` apenas faz a thread principal esperar todas terminarem.

### Parte B

A classe da Parte B, que implementa `Runnable`, ainda poderia herdar de outra classe. A classe da Parte A ja usa sua unica heranca para estender `Thread`.

### Parte C

Criar uma thread de sistema operacional e mais caro que criar um objeto comum porque a JVM precisa pedir recursos ao SO, incluindo pilha, estruturas do kernel e participacao no escalonamento. Esse limite mostra que usar uma thread nativa por requisicao em um servidor com milhares de conexoes pode esgotar memoria e degradar o desempenho.

### Parte D

Com 4 threads atendendo 10 clientes e cada atendimento durando 1 segundo, o tempo total fica perto de 3 segundos. O pool executa 4 tarefas no primeiro segundo, 4 no segundo e 2 no terceiro, reaproveitando as mesmas threads.

### Parte E

Uma Virtual Thread nao e uma thread de sistema operacional. Ela e uma thread leve gerenciada pela JVM e executada, quando necessario, sobre poucas threads nativas chamadas carrier threads. Por isso e possivel criar muito mais tarefas concorrentes sem o mesmo custo de memoria das threads nativas.

## Comparacao final

| Abordagem | Vantagem | Limitacao |
| --- | --- | --- |
| `extends Thread` | Simples para demonstrar o mecanismo basico | Prende a heranca da classe |
| `implements Runnable` | Separa tarefa de executor | Ainda usa threads nativas quando combinada com `new Thread` |
| Muitas threads nativas | Demonstra o limite real do SO | Alto custo de memoria e escalonamento |
| `ExecutorService` | Reaproveita poucas threads reais | Tarefas bloqueantes ainda ocupam threads do pool |
| Virtual Threads | Escala melhor para muitas tarefas bloqueantes | Exige Java 21+ |

Para um servidor com milhares de conexoes, a melhor abordagem moderna e usar Virtual Threads quando o codigo e majoritariamente bloqueante, ou um `ExecutorService` bem dimensionado quando se quer limitar explicitamente o paralelismo sobre threads nativas.

## Exercicios de fixacao

### Trocar o pool fixo por `newCachedThreadPool()`

O comportamento muda. No pool fixo com 4 threads, 10 tarefas de 1 segundo terminam perto de 3 segundos, pois apenas 4 tarefas rodam ao mesmo tempo. Com `newCachedThreadPool()`, o executor pode criar mais threads conforme a demanda; neste experimento simples, tende a executar os 10 atendimentos quase juntos e terminar perto de 1 segundo. A desvantagem e que ele pode criar muitas threads nativas se receber muitas tarefas bloqueantes.

### Imprimir `Thread.currentThread()` na Parte E

A Parte E imprime as 10 primeiras threads. A saida mostra algo como `VirtualThread[#...]`, indicando que a tarefa esta rodando em uma virtual thread.

### Escolha para servidor com milhares de conexoes

Para um servidor com milhares de conexoes bloqueantes, como chamadas a banco, arquivos ou APIs externas, Virtual Threads sao a melhor escolha moderna porque reduzem o custo de manter muitas tarefas aguardando I/O. Se a aplicacao precisa controlar rigidamente o numero de tarefas rodando em paralelo, um `ExecutorService` com pool fixo ainda pode ser adequado.

## Checklist de entrega

- As partes A, B, C, D e E estao implementadas.
- A variacao `DC` implementa o exercicio com `newCachedThreadPool()`.
- O projeto compila com Java 21+.
- As perguntas do roteiro estao respondidas neste relatorio.
- O codigo imprime os nomes das threads e os tempos totais para comparar os modelos.
