import asyncio
import os
import sys

import websockets

PORTA_BASE = 8888
clientes_conectados = set()


def ler_offset() -> int:
    if len(sys.argv) > 1 and sys.argv[1].isdigit():
        return int(sys.argv[1])
    return int(os.getenv("LAB_REDES_OFFSET", "0"))


async def tratar_conexao(websocket):
    clientes_conectados.add(websocket)
    print(f"[WebSocket] Novo aluno conectado. Total: {len(clientes_conectados)}", flush=True)
    await websocket.send("Bem-vindo(a) ao mural de avisos da turma!")

    try:
        async for mensagem in websocket:
            print(f"[WebSocket] Recebido: {mensagem}", flush=True)
            aviso_formatado = f"Aviso da turma: {mensagem}"
            websockets.broadcast(clientes_conectados, aviso_formatado)
    finally:
        clientes_conectados.discard(websocket)
        print(f"[WebSocket] Aluno desconectado. Total: {len(clientes_conectados)}", flush=True)


async def main():
    porta = PORTA_BASE + ler_offset()
    print(f"[WebSocket] Servidor do mural iniciado na porta {porta}.", flush=True)
    async with websockets.serve(tratar_conexao, "0.0.0.0", porta):
        await asyncio.Future()


asyncio.run(main())
