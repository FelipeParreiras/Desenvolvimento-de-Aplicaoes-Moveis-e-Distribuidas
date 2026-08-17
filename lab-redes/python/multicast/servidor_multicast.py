import os
import socket
import struct
import sys
import time

GRUPO_MULTICAST = "230.0.0.1"
PORTA_BASE = 4446
INTERFACE_LOCAL = "127.0.0.1"


def ler_offset() -> int:
    if len(sys.argv) > 1 and sys.argv[1].isdigit():
        return int(sys.argv[1])
    return int(os.getenv("LAB_REDES_OFFSET", "0"))


porta = PORTA_BASE + ler_offset()

with socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP) as sock:
    sock.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, 2)
    sock.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_LOOP, 1)
    sock.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_IF, socket.inet_aton(INTERFACE_LOCAL))

    print(f"[Multicast] Enviando avisos para {GRUPO_MULTICAST}:{porta}", flush=True)
    for contador in range(1, 6):
        mensagem = f"Aviso #{contador}: a aula comeca em {5 - contador} minuto(s)!"
        sock.sendto(mensagem.encode("utf-8"), (GRUPO_MULTICAST, porta))
        print(f"[Multicast] Enviado: {mensagem}", flush=True)
        time.sleep(1)
