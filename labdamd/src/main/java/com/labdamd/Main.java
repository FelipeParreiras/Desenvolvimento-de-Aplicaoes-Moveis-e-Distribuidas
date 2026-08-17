package com.labdamd;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int ATENDIMENTOS_CURTOS = 5;
    private static final int CLIENTES_POOL = 10;
    private static final Duration TEMPO_ATENDIMENTO = Duration.ofSeconds(1);

    public static void main(String[] args) {
        if (args.length == 0) {
            executarPartes("A", "B", "D", "E");
            System.out.println();
            System.out.println("Parte C e o experimento de estresse. Execute com argumento C para rodar separadamente.");
            return;
        }

        executarPartes(args);
    }

    private static void executarPartes(String... partes) {
        for (String parte : partes) {
            switch (parte.toUpperCase(Locale.ROOT)) {
                case "A" -> executarParteA();
                case "B" -> executarParteB();
                case "C" -> executarParteC();
                case "D" -> executarParteD();
                case "DC", "D-CACHED" -> executarParteDComPoolCached();
                case "E" -> executarParteE();
                case "ALL", "TODAS" -> executarPartes("A", "B", "C", "D", "DC", "E");
                default -> System.out.printf("Parte desconhecida: %s. Use A, B, C, D, DC, E ou ALL.%n", parte);
            }
        }
    }

    private static void executarParteA() {
        System.out.println();
        System.out.println("PARTE A - extends Thread");
        medirTempo(() -> {
            AtendimentoThread[] atendentes = new AtendimentoThread[ATENDIMENTOS_CURTOS];

            for (int i = 0; i < atendentes.length; i++) {
                atendentes[i] = new AtendimentoThread(i + 1);
                atendentes[i].setName("AtendenteThread-" + (i + 1));
                atendentes[i].start();
            }

            for (AtendimentoThread atendente : atendentes) {
                atendente.join();
            }
        });
    }

    private static void executarParteB() {
        System.out.println();
        System.out.println("PARTE B - implements Runnable");
        medirTempo(() -> {
            Thread[] atendentes = new Thread[ATENDIMENTOS_CURTOS];

            for (int i = 0; i < atendentes.length; i++) {
                Runnable tarefa = new AtendimentoRunnable(i + 1);
                atendentes[i] = new Thread(tarefa, "AtendenteRunnable-" + (i + 1));
                atendentes[i].start();
            }

            for (Thread atendente : atendentes) {
                atendente.join();
            }
        });
    }

    private static void executarParteC() {
        int total = Integer.getInteger("labdamd.native.total", 10_000);

        System.out.println();
        System.out.printf("PARTE C - criando %,d threads nativas%n", total);
        medirTempo(() -> {
            Thread[] threads = new Thread[total];

            for (int i = 0; i < total; i++) {
                threads[i] = new Thread(() -> dormir(TEMPO_ATENDIMENTO), "Nativa-" + i);
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }
        });
    }

    private static void executarParteD() {
        System.out.println();
        System.out.println("PARTE D - ExecutorService com pool fixo");
        executarComExecutor(Executors.newFixedThreadPool(4));
    }

    private static void executarParteDComPoolCached() {
        System.out.println();
        System.out.println("EXERCICIO - ExecutorService com pool cached");
        executarComExecutor(Executors.newCachedThreadPool());
    }

    private static void executarComExecutor(ExecutorService pool) {
        medirTempo(() -> {
            for (int i = 0; i < CLIENTES_POOL; i++) {
                int idCliente = i + 1;
                pool.submit(() -> atenderCliente(idCliente));
            }

            pool.shutdown();
            if (!pool.awaitTermination(1, TimeUnit.MINUTES)) {
                pool.shutdownNow();
            }
        });
    }

    private static void executarParteE() {
        int total = Integer.getInteger("labdamd.virtual.total", 100_000);

        System.out.println();
        System.out.printf("PARTE E - criando %,d virtual threads%n", total);
        medirTempo(() -> {
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int i = 0; i < total; i++) {
                    int idCliente = i + 1;
                    executor.submit(() -> {
                        if (idCliente <= 10) {
                            System.out.printf("%s atendendo cliente %d%n", Thread.currentThread(), idCliente);
                        }
                        dormir(TEMPO_ATENDIMENTO);
                    });
                }
            }
        });
    }

    private static void atenderCliente(int idCliente) {
        System.out.printf("%s atendendo cliente %d%n", Thread.currentThread().getName(), idCliente);
        dormir(TEMPO_ATENDIMENTO);
    }

    private static void medirTempo(Execucao execucao) {
        Instant inicio = Instant.now();

        try {
            execucao.executar();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Execucao interrompida.");
        }

        long segundos = Duration.between(inicio, Instant.now()).toSeconds();
        System.out.printf("Tempo total: %ds%n", segundos);
    }

    private static void dormir(Duration duracao) {
        try {
            Thread.sleep(duracao);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static final class AtendimentoThread extends Thread {
        private final int idCliente;

        private AtendimentoThread(int idCliente) {
            this.idCliente = idCliente;
        }

        @Override
        public void run() {
            atenderCliente(idCliente);
        }
    }

    private static final class AtendimentoRunnable implements Runnable {
        private final int idCliente;

        private AtendimentoRunnable(int idCliente) {
            this.idCliente = idCliente;
        }

        @Override
        public void run() {
            atenderCliente(idCliente);
        }
    }

    @FunctionalInterface
    private interface Execucao {
        void executar() throws InterruptedException;
    }
}
