import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.StandardSocketOptions;

public class ServidorMulticast {
    private static final int PORTA_BASE = 4446;
    private static final String GRUPO_MULTICAST = "230.0.0.1";

    public static void main(String[] args) throws Exception {
        int porta = PORTA_BASE + lerOffset(args);
        InetAddress grupo = InetAddress.getByName(GRUPO_MULTICAST);
        NetworkInterface loopback = NetworkInterface.getByInetAddress(InetAddress.getLoopbackAddress());

        try (MulticastSocket socket = new MulticastSocket()) {
            socket.setTimeToLive(2);
            socket.setOption(StandardSocketOptions.IP_MULTICAST_LOOP, true);
            socket.setNetworkInterface(loopback);

            System.out.println("[Multicast] Enviando avisos para " + GRUPO_MULTICAST + ":" + porta);
            for (int contador = 1; contador <= 5; contador++) {
                String mensagem = "Aviso #" + contador + ": a aula comeca em " + (5 - contador) + " minuto(s)!";
                byte[] dados = mensagem.getBytes();
                DatagramPacket pacote = new DatagramPacket(dados, dados.length, grupo, porta);

                socket.send(pacote);
                System.out.println("[Multicast] Enviado: " + mensagem);
                Thread.sleep(1000);
            }
        }
    }

    private static int lerOffset(String[] args) {
        if (args.length > 0 && args[0].matches("\\d+")) {
            return Integer.parseInt(args[0]);
        }

        String valor = System.getenv("LAB_REDES_OFFSET");
        return valor == null || valor.isBlank() ? 0 : Integer.parseInt(valor);
    }
}
