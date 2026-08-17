import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

public class ClienteMulticast {
    private static final int PORTA_BASE = 4446;
    private static final String GRUPO_MULTICAST = "230.0.0.1";

    public static void main(String[] args) throws Exception {
        int offset = lerOffset(args);
        int porta = PORTA_BASE + offset;
        int maxMensagens = lerMaxMensagens(args);

        InetAddress grupo = InetAddress.getByName(GRUPO_MULTICAST);
        InetSocketAddress endpointGrupo = new InetSocketAddress(grupo, porta);
        NetworkInterface loopback = NetworkInterface.getByInetAddress(InetAddress.getLoopbackAddress());

        try (MulticastSocket socket = new MulticastSocket(null)) {
            socket.setReuseAddress(true);
            socket.bind(new InetSocketAddress(porta));
            socket.joinGroup(endpointGrupo, loopback);

            System.out.println("[Multicast] Inscrito no grupo " + GRUPO_MULTICAST + ":" + porta + ".");
            byte[] buffer = new byte[1024];
            int recebidas = 0;

            while (maxMensagens == 0 || recebidas < maxMensagens) {
                DatagramPacket pacote = new DatagramPacket(buffer, buffer.length);
                socket.receive(pacote);
                String mensagem = new String(pacote.getData(), 0, pacote.getLength());
                recebidas++;
                System.out.println("[Multicast] Recebido: " + mensagem);
            }

            socket.leaveGroup(endpointGrupo, loopback);
        }
    }

    private static int lerOffset(String[] args) {
        if (args.length > 0 && args[0].matches("\\d+")) {
            return Integer.parseInt(args[0]);
        }

        String valor = System.getenv("LAB_REDES_OFFSET");
        return valor == null || valor.isBlank() ? 0 : Integer.parseInt(valor);
    }

    private static int lerMaxMensagens(String[] args) {
        if (args.length > 1 && args[1].matches("\\d+")) {
            return Integer.parseInt(args[1]);
        }

        String valor = System.getenv("LAB_REDES_MAX_MENSAGENS");
        return valor == null || valor.isBlank() ? 0 : Integer.parseInt(valor);
    }
}
