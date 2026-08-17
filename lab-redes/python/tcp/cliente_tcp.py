import os
import socket
import sys

HOST = "localhost"
PORTA_BASE = 5000


def ler_offset() -> int:
    if len(sys.argv) > 1 and sys.argv[1].isdigit():
        return int(sys.argv[1])
    return int(os.getenv("LAB_REDES_OFFSET", "0"))


def mensagens_script() -> list[str]:
    inicio = 2 if len(sys.argv) > 1 and sys.argv[1].isdigit() else 1
    return sys.argv[inicio:]


def enviar(cliente: socket.socket, arquivo, mensagem: str) -> None:
    print(f"> {mensagem}", flush=True)
    cliente.sendall((mensagem + "\n").encode("utf-8"))
    print(arquivo.readline().strip(), flush=True)


porta = PORTA_BASE + ler_offset()
mensagens = mensagens_script()

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as cliente:
    cliente.connect((HOST, porta))
    print(f"[TCP] Conectado ao servidor em {HOST}:{porta}.", flush=True)
    arquivo = cliente.makefile("r", encoding="utf-8")

    if mensagens:
        for mensagem in mensagens:
            enviar(cliente, arquivo, mensagem)
            if mensagem.lower() == "sair":
                break
    else:
        print("[TCP] Digite 'sair' para encerrar.", flush=True)
        while True:
            mensagem = input("> ")
            enviar(cliente, arquivo, mensagem)
            if mensagem.lower() == "sair":
                break
