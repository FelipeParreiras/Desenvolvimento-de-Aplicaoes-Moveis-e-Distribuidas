from datetime import datetime
import os
import socket
import sys

HOST = "0.0.0.0"
PORTA_BASE = 5001


def ler_offset() -> int:
    if len(sys.argv) > 1 and sys.argv[1].isdigit():
        return int(sys.argv[1])
    return int(os.getenv("LAB_REDES_OFFSET", "0"))


def responder(mensagem: str) -> str:
    if mensagem.lower() == "hora":
        return f"Horario atual do servidor: {datetime.now():%H:%M:%S}"
    return f'Monitor responde: recebi seu datagrama -> "{mensagem}"'


porta = PORTA_BASE + ler_offset()
buffer = 1024

with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as servidor:
    servidor.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    servidor.bind((HOST, porta))
    print(f"[UDP] Servidor aguardando datagramas na porta {porta}...", flush=True)

    while True:
        dados, endereco = servidor.recvfrom(buffer)
        mensagem = dados.decode("utf-8").strip()
        print(f"[UDP] Recebido de {endereco}: {mensagem}", flush=True)
        servidor.sendto(responder(mensagem).encode("utf-8"), endereco)
