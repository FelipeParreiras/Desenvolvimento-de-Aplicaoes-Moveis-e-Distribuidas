import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServidorUDP {
    private static final int PORTA_BASE = 5001;
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) throws Exception {
        int porta = PORTA_BASE + lerOffset(args);
        byte[] buffer = new byte[1024];

        try (DatagramSocket socket = new DatagramSocket(porta)) {
            System.out.println("[UDP] Servidor aguardando datagramas na porta " + porta + "...");

            while (true) {
                DatagramPacket pacoteRecebido = new DatagramPacket(buffer, buffer.length);
                socket.receive(pacoteRecebido);

                String mensagem = new String(
                        pacoteRecebido.getData(),
                        0,
                        pacoteRecebido.getLength()
                );

                InetAddress enderecoCliente = pacoteRecebido.getAddress();
                int portaCliente = pacoteRecebido.getPort();

                System.out.println("[UDP] Recebido de " + enderecoCliente + ":" + portaCliente + " -> " + mensagem);

                String resposta = responder(mensagem);
                byte[] dadosResposta = resposta.getBytes();
                DatagramPacket pacoteResposta = new DatagramPacket(
                        dadosResposta,
                        dadosResposta.length,
                        enderecoCliente,
                        portaCliente
                );
                socket.send(pacoteResposta);
            }
        }
    }

    private static String responder(String mensagem) {
        if (mensagem.equalsIgnoreCase("hora")) {
            return "Horario atual do servidor: " + LocalDateTime.now().format(FORMATO_HORA);
        }

        return "Monitor responde: recebi seu datagrama -> \"" + mensagem + "\"";
    }

    private static int lerOffset(String[] args) {
        if (args.length > 0 && args[0].matches("\\d+")) {
            return Integer.parseInt(args[0]);
        }

        String valor = System.getenv("LAB_REDES_OFFSET");
        return valor == null || valor.isBlank() ? 0 : Integer.parseInt(valor);
    }
}
