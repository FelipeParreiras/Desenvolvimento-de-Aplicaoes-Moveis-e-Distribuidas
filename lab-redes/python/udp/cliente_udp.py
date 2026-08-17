import os
import socket
import sys

HOST = "localhost"
PORTA_BASE = 5001
TIMEOUT_SEGUNDOS = 2


def ler_offset() -> int:
    if len(sys.argv) > 1 and sys.argv[1].isdigit():
        return int(sys.argv[1])
    return int(os.getenv("LAB_REDES_OFFSET", "0"))


def mensagens_script() -> list[str]:
    inicio = 2 if len(sys.argv) > 1 and sys.argv[1].isdigit() else 1
    return sys.argv[inicio:]


def enviar(cliente: socket.socket, destino: tuple[str, int], mensagem: str) -> None:
    print(f"> {mensagem}", flush=True)
    cliente.sendto(mensagem.encode("utf-8"), destino)
    try:
        dados, _ = cliente.recvfrom(1024)
        print(dados.decode("utf-8"), flush=True)
    except (TimeoutError, ConnectionResetError, OSError):
        print(f"[UDP] Nenhuma resposta recebida em {TIMEOUT_SEGUNDOS}s.", flush=True)


porta = PORTA_BASE + ler_offset()
destino = (HOST, porta)
mensagens = mensagens_script()

with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as cliente:
    cliente.settimeout(TIMEOUT_SEGUNDOS)
    print(f"[UDP] Pronto para enviar para {HOST}:{porta}.", flush=True)

    if mensagens:
        for mensagem in mensagens:
            enviar(cliente, destino, mensagem)
            if mensagem.lower() == "sair":
                break
    else:
        print("[UDP] Digite 'sair' para encerrar.", flush=True)
        while True:
            mensagem = input("> ")
            enviar(cliente, destino, mensagem)
            if mensagem.lower() == "sair":
                break
