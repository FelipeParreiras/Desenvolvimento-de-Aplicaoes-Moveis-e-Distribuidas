from datetime import datetime
import os
import socket
import sys

HOST = "0.0.0.0"
PORTA_BASE = 5000


def ler_offset() -> int:
    if len(sys.argv) > 1 and sys.argv[1].isdigit():
        return int(sys.argv[1])
    return int(os.getenv("LAB_REDES_OFFSET", "0"))


def responder(mensagem: str) -> str:
    if mensagem.lower() == "hora":
        return f"Horario atual do servidor: {datetime.now():%H:%M:%S}"
    return f'Monitor responde: recebi sua mensagem -> "{mensagem}"'


porta = PORTA_BASE + ler_offset()

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as servidor:
    servidor.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    servidor.bind((HOST, porta))
    servidor.listen(1)
    print(f"[TCP] Servidor aguardando conexoes na porta {porta}...", flush=True)

    conexao, endereco = servidor.accept()
    with conexao:
        print(f"[TCP] Cliente conectado: {endereco}", flush=True)
        while True:
            dados = conexao.recv(1024).decode("utf-8").strip()
            if not dados:
                break

            print(f"[TCP] Recebido: {dados}", flush=True)
            if dados.lower() == "sair":
                conexao.sendall("Encerrando conexao. Ate mais!\n".encode("utf-8"))
                break

            conexao.sendall((responder(dados) + "\n").encode("utf-8"))

print("[TCP] Servidor encerrado.", flush=True)
