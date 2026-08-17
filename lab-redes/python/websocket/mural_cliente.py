import asyncio
import os
import sys

import websockets

PORTA_BASE = 8888


def ler_offset() -> int:
    if len(sys.argv) > 1 and sys.argv[1].isdigit():
        return int(sys.argv[1])
    return int(os.getenv("LAB_REDES_OFFSET", "0"))


def mensagens_script() -> list[str]:
    inicio = 2 if len(sys.argv) > 1 and sys.argv[1].isdigit() else 1
    return sys.argv[inicio:]


async def escutar(websocket):
    async for mensagem in websocket:
        print(f"\n{mensagem}", flush=True)
        print("> ", end="", flush=True)


async def main():
    porta = PORTA_BASE + ler_offset()
    uri = f"ws://localhost:{porta}"
    mensagens = mensagens_script()

    async with websockets.connect(uri) as websocket:
        print(f"[WebSocket] Conectado ao mural em {uri}.", flush=True)
        tarefa_escuta = asyncio.create_task(escutar(websocket))

        if mensagens:
            await asyncio.sleep(0.4)
            for mensagem in mensagens:
                print(f"> {mensagem}", flush=True)
                await websocket.send(mensagem)
                await asyncio.sleep(0.4)
            await asyncio.sleep(1.5)
        else:
            print("[WebSocket] Digite 'sair' para encerrar.", flush=True)
            while True:
                mensagem = await asyncio.to_thread(input, "> ")
                if mensagem.lower() == "sair":
                    break
                await websocket.send(mensagem)

        tarefa_escuta.cancel()
        try:
            await tarefa_escuta
        except asyncio.CancelledError:
            pass


asyncio.run(main())
