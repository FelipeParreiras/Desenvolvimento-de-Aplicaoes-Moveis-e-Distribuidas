import os
import socket
import struct
import sys

GRUPO_MULTICAST = "230.0.0.1"
PORTA_BASE = 4446
INTERFACE_LOCAL = "127.0.0.1"


def ler_offset() -> int:
    if len(sys.argv) > 1 and sys.argv[1].isdigit():
        return int(sys.argv[1])
    return int(os.getenv("LAB_REDES_OFFSET", "0"))


def ler_max_mensagens() -> int:
    if len(sys.argv) > 2 and sys.argv[2].isdigit():
        return int(sys.argv[2])
    return int(os.getenv("LAB_REDES_MAX_MENSAGENS", "0"))


porta = PORTA_BASE + ler_offset()
max_mensagens = ler_max_mensagens()

with socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP) as sock:
    sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    sock.bind(("", porta))

    grupo = socket.inet_aton(GRUPO_MULTICAST)
    interface = socket.inet_aton(INTERFACE_LOCAL)
    solicitacao_membro = struct.pack("4s4s", grupo, interface)
    sock.setsockopt(socket.IPPROTO_IP, socket.IP_ADD_MEMBERSHIP, solicitacao_membro)

    print(f"[Multicast] Inscrito no grupo {GRUPO_MULTICAST}:{porta}.", flush=True)
    recebidas = 0

    while max_mensagens == 0 or recebidas < max_mensagens:
        dados, _ = sock.recvfrom(1024)
        recebidas += 1
        print(f"[Multicast] Recebido: {dados.decode('utf-8')}", flush=True)
