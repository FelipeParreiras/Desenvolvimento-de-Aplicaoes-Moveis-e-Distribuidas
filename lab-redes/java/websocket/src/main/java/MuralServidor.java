import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class MuralServidor extends WebSocketServer {
    private static final int PORTA_BASE = 8887;

    public MuralServidor(int porta) {
        super(new InetSocketAddress(porta));
    }

    @Override
    public void onOpen(WebSocket conexao, ClientHandshake handshake) {
        System.out.println("[WebSocket] Novo aluno conectado: " + conexao.getRemoteSocketAddress());
        conexao.send("Bem-vindo(a) ao mural de avisos da turma!");
    }

    @Override
    public void onMessage(WebSocket conexao, String mensagem) {
        System.out.println("[WebSocket] Recebido: " + mensagem);
        String avisoFormatado = "Aviso da turma: " + mensagem;

        for (WebSocket cliente : getConnections()) {
            cliente.send(avisoFormatado);
        }
    }

    @Override
    public void onClose(WebSocket conexao, int codigo, String motivo, boolean remoto) {
        System.out.println("[WebSocket] Aluno desconectado: " + conexao.getRemoteSocketAddress());
    }

    @Override
    public void onError(WebSocket conexao, Exception ex) {
        System.out.println("[WebSocket] Erro: " + ex.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("[WebSocket] Servidor do mural iniciado.");
    }

    public static void main(String[] args) {
        int porta = PORTA_BASE + lerOffset(args);
        MuralServidor servidor = new MuralServidor(porta);
        servidor.start();
        System.out.println("[WebSocket] Escutando na porta " + porta + ".");
    }

    private static int lerOffset(String[] args) {
        if (args.length > 0 && args[0].matches("\\d+")) {
            return Integer.parseInt(args[0]);
        }

        String valor = System.getenv("LAB_REDES_OFFSET");
        return valor == null || valor.isBlank() ? 0 : Integer.parseInt(valor);
    }
}
