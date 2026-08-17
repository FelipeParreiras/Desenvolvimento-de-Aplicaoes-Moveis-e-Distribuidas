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

## Execucao - TCP

Java:

```powershell
cd lab-redes/java/tcp
javac ServidorTCP.java ClienteTCP.java
java ServidorTCP
java ClienteTCP
```

Python:

```powershell
cd lab-redes/python/tcp
python servidor_tcp.py
python cliente_tcp.py
```

Teste a mensagem `hora` em ambas as linguagens.

## Execucao - UDP

Java:

```powershell
cd lab-redes/java/udp
javac ServidorUDP.java ClienteUDP.java
java ServidorUDP
java ClienteUDP
```

Python:

```powershell
cd lab-redes/python/udp
python servidor_udp.py
python cliente_udp.py
```

Depois da troca normal, pare o servidor com `Ctrl+C` e envie uma mensagem pelo cliente. O cliente deve mostrar que nao recebeu resposta.

## Execucao - Multicast

Java:

```powershell
cd lab-redes/java/multicast
javac ServidorMulticast.java ClienteMulticast.java
java ClienteMulticast
java ClienteMulticast
java ServidorMulticast
```

Python:

```powershell
cd lab-redes/python/multicast
python cliente_multicast.py
python cliente_multicast.py
python servidor_multicast.py
```

Abra os clientes antes do servidor. Para teste local, a implementacao usa a interface de loopback.

## Execucao - WebSocket

Java:

```powershell
cd lab-redes/java/websocket
mvn compile
mvn exec:java "-Dexec.mainClass=MuralServidor"
mvn exec:java "-Dexec.mainClass=MuralCliente"
mvn exec:java "-Dexec.mainClass=MuralCliente"
```

Python:

```powershell
cd lab-redes/python/websocket
python -m pip install -r requirements.txt
python mural_servidor.py
python mural_cliente.py
python mural_cliente.py
```

Abra dois clientes e envie uma mensagem por um deles. O outro deve receber o aviso imediatamente.

## Modo de teste scriptado

Alguns clientes aceitam mensagens por argumento para testes rapidos:

```powershell
java ClienteTCP 0 "oi" hora sair
python cliente_tcp.py 0 "oi" hora sair
java ClienteUDP 0 "oi" hora
python cliente_udp.py 0 "oi" hora
java ClienteMulticast 0 5
python cliente_multicast.py 0 5
java -cp target/classes MuralCliente 0 "aviso de teste"
python mural_cliente.py 0 "aviso de teste"
```
