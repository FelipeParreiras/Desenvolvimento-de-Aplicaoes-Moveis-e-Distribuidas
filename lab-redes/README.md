# Central de Avisos da Turma - Lab de Redes

Implementacao do Roteiro 2 da disciplina LabDAMD, com exemplos equivalentes em Java e Python para:

- TCP
- UDP
- Multicast
- WebSocket

## Requisitos

- Java JDK 17+
- Maven 3.8+
- Python 3.10+
- Biblioteca Python `websockets` para a parte WebSocket

Instale a dependencia Python:

```powershell
python -m pip install websockets
```

## Offset de portas

Todos os programas usam `OFFSET = 0` por padrao. Para usar os dois ultimos digitos da sua matricula/RA, informe uma das opcoes abaixo:

```powershell
$env:LAB_REDES_OFFSET = "67"
```

Ou passe por argumento:

```powershell
java ServidorTCP 67
python servidor_tcp.py 67
```

As portas finais seguem o roteiro:

| Protocolo | Porta-base |
| --- | --- |
| TCP | 5000 + OFFSET |
| UDP | 5001 + OFFSET |
| Multicast | 4446 + OFFSET |
| WebSocket Java | 8887 + OFFSET |
| WebSocket Python | 8888 + OFFSET |

## Evidencias

Os prints de tela devem ser salvos em:

- `evidencias/tcp/tcp-java.png`
- `evidencias/tcp/tcp-python.png`
- `evidencias/udp/udp-java.png`
- `evidencias/udp/udp-python.png`
- `evidencias/multicast/multicast-java.png`
- `evidencias/multicast/multicast-python.png`
- `evidencias/websocket/websocket-java.png`
- `evidencias/websocket/websocket-python.png`

Em cada print, deixe visivel um terminal com `Get-Date`, conforme solicitado no roteiro.
