import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ClienteUDP {
    private static final int PORTA_BASE = 5001;
    private static final int TIMEOUT_MS = 2000;

    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int offset = lerOffset(args);
        int porta = PORTA_BASE + offset;
        List<String> mensagensScript = mensagensScript(args);

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(TIMEOUT_MS);
            InetAddress enderecoServidor = InetAddress.getByName(host);

            System.out.println("[UDP] Pronto para enviar para " + host + ":" + porta + ".");

            if (!mensagensScript.isEmpty()) {
                for (String mensagem : mensagensScript) {
                    enviar(socket, enderecoServidor, porta, mensagem);
                    if (mensagem.equalsIgnoreCase("sair")) {
                        break;
                    }
                }
                return;
            }

            System.out.println("[UDP] Digite 'sair' para encerrar.");
            Scanner teclado = new Scanner(System.in);
            while (true) {
                System.out.print("> ");
                String mensagem = teclado.nextLine();
                enviar(socket, enderecoServidor, porta, mensagem);
                if (mensagem.equalsIgnoreCase("sair")) {
                    break;
                }
            }
        }
    }

    private static void enviar(DatagramSocket socket, InetAddress endereco, int porta, String mensagem) throws Exception {
        System.out.println("> " + mensagem);
        byte[] dados = mensagem.getBytes();
        DatagramPacket pacote = new DatagramPacket(dados, dados.length, endereco, porta);
        socket.send(pacote);

        try {
            byte[] buffer = new byte[1024];
            DatagramPacket resposta = new DatagramPacket(buffer, buffer.length);
            socket.receive(resposta);
            String texto = new String(resposta.getData(), 0, resposta.getLength());
            System.out.println(texto);
        } catch (SocketTimeoutException e) {
            System.out.println("[UDP] Nenhuma resposta recebida em " + TIMEOUT_MS + "ms.");
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
