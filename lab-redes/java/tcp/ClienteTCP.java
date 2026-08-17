import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class ClienteTCP {
    private static final int PORTA_BASE = 5000;

    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int offset = lerOffset(args);
        int porta = PORTA_BASE + offset;
        List<String> mensagensScript = mensagensScript(args);

        try (Socket socket = new Socket(host, porta);
             PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("[TCP] Conectado ao servidor em " + host + ":" + porta + ".");

            if (!mensagensScript.isEmpty()) {
                for (String mensagem : mensagensScript) {
                    enviarMensagem(saida, entrada, mensagem);
                    if (mensagem.equalsIgnoreCase("sair")) {
                        break;
                    }
                }
                return;
            }

            System.out.println("[TCP] Digite 'sair' para encerrar.");
            while (true) {
                System.out.print("> ");
                String linha = teclado.readLine();
                enviarMensagem(saida, entrada, linha);
                if (linha.equalsIgnoreCase("sair")) {
                    break;
                }
            }
        }
    }

    private static void enviarMensagem(PrintWriter saida, BufferedReader entrada, String mensagem) throws IOException {
        System.out.println("> " + mensagem);
        saida.println(mensagem);
        System.out.println(entrada.readLine());
    }

    private static int lerOffset(String[] args) {
        if (args.length > 0 && args[0].matches("\\d+")) {
            return Integer.parseInt(args[0]);
        }

        String valor = System.getenv("LAB_REDES_OFFSET");
        return valor == null || valor.isBlank() ? 0 : Integer.parseInt(valor);
    }

    private static List<String> mensagensScript(String[] args) {
        int inicio = args.length > 0 && args[0].matches("\\d+") ? 1 : 0;
        return Arrays.asList(args).subList(inicio, args.length);
    }
}
