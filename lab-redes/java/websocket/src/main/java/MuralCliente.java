import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletionStage;

public class MuralCliente {
    private static final int PORTA_BASE = 8887;

    public static void main(String[] args) throws Exception {
        int offset = lerOffset(args);
        int porta = PORTA_BASE + offset;
        List<String> mensagensScript = mensagensScript(args);

        HttpClient client = HttpClient.newHttpClient();
        WebSocket.Listener listener = new WebSocket.Listener() {
            @Override
            public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                System.out.println("\n" + data);
                System.out.print("> ");
                webSocket.request(1);
                return null;
            }
        };

        WebSocket socket = client.newWebSocketBuilder()
                .buildAsync(URI.create("ws://localhost:" + porta), listener)
                .join();

        System.out.println("[WebSocket] Conectado ao mural em ws://localhost:" + porta + ".");

        if (!mensagensScript.isEmpty()) {
            Thread.sleep(400);
            for (String mensagem : mensagensScript) {
                System.out.println("> " + mensagem);
                socket.sendText(mensagem, true).join();
                Thread.sleep(400);
            }
            Thread.sleep(1500);
            socket.sendClose(WebSocket.NORMAL_CLOSURE, "Ate mais!").join();
            return;
        }

        Scanner teclado = new Scanner(System.in);
        System.out.println("[WebSocket] Digite 'sair' para encerrar.");
        while (true) {
            System.out.print("> ");
            String mensagem = teclado.nextLine();
            if (mensagem.equalsIgnoreCase("sair")) {
                socket.sendClose(WebSocket.NORMAL_CLOSURE, "Ate mais!").join();
                break;
            }
            socket.sendText(mensagem, true).join();
        }
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
