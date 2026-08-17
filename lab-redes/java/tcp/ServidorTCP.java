import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServidorTCP {
    private static final int PORTA_BASE = 5000;
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) throws IOException {
        int porta = PORTA_BASE + lerOffset(args);

        try (ServerSocket servidor = new ServerSocket(porta)) {
            System.out.println("[TCP] Servidor aguardando conexoes na porta " + porta + "...");

            try (Socket cliente = servidor.accept();
                 BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                 PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true)) {

                System.out.println("[TCP] Cliente conectado: " + cliente.getRemoteSocketAddress());
                String mensagem;

                while ((mensagem = entrada.readLine()) != null) {
                    System.out.println("[TCP] Recebido: " + mensagem);

                    if (mensagem.equalsIgnoreCase("sair")) {
                        saida.println("Encerrando conexao. Ate mais!");
                        break;
                    }

                    saida.println(responder(mensagem));
                }
            }
        }

        System.out.println("[TCP] Servidor encerrado.");
    }

    private static String responder(String mensagem) {
        if (mensagem.equalsIgnoreCase("hora")) {
            return "Horario atual do servidor: " + LocalDateTime.now().format(FORMATO_HORA);
        }

        return "Monitor responde: recebi sua mensagem -> \"" + mensagem + "\"";
    }

    private static int lerOffset(String[] args) {
        if (args.length > 0 && args[0].matches("\\d+")) {
            return Integer.parseInt(args[0]);
        }

        String valor = System.getenv("LAB_REDES_OFFSET");
        return valor == null || valor.isBlank() ? 0 : Integer.parseInt(valor);
    }
}
