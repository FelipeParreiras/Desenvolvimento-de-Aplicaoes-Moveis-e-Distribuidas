# Respostas - Roteiro 2

## Parte A - TCP

1. Se o cliente TCP iniciar antes do servidor, a conexao falha porque nao existe nenhum processo escutando na porta de destino. No Java e no Python, isso aparece como erro de conexao recusada. Isso ocorre porque o TCP precisa estabelecer uma conexao antes de enviar dados; sem servidor aceitando o handshake, o cliente nao tem para onde conectar.

2. O TCP usa numeros de sequencia e confirmacoes (`ACKs`) para controlar a entrega dos bytes. Como a comunicacao TCP e vista pela aplicacao como um fluxo ordenado, o sistema operacional reorganiza os segmentos quando necessario antes de entregar os dados ao programa.

3. A implementacao atual aceita um cliente por execucao do servidor TCP. No Java, o servidor chama `accept()` uma vez e atende aquele socket ate a mensagem `sair`. No Python acontece o mesmo com `accept()`. Se dois clientes tentarem conectar ao mesmo tempo, um pode ficar aguardando na fila do sistema operacional ou receber falha dependendo do momento, mas o codigo nao cria threads nem loop de atendimento para varios clientes simultaneos.

## Parte B - UDP

1. Com o servidor UDP desligado, o cliente consegue enviar o datagrama porque UDP nao estabelece conexao antes do envio. Na implementacao, o cliente espera uma resposta por pouco tempo e depois mostra timeout. Em TCP isso seria diferente: o cliente falharia ja na tentativa de conectar, porque o handshake nao seria concluido.

2. Dois exemplos reais de UDP sao chamadas de voz/video e jogos online. Em chamadas, e melhor perder um pacote atrasado do que pausar toda a conversa esperando retransmissao. Em jogos online, posicoes antigas perdem valor rapidamente; retransmitir tudo como TCP poderia aumentar latencia e prejudicar a experiencia.

3. Seria possivel manter uma lista de enderecos que enviaram datagramas recentemente, mas isso seria uma decisao da aplicacao, nao uma conexao UDP real. A arquitetura teria que incluir controle de estado, expiracao de clientes inativos e talvez mensagens periodicas para indicar presenca.

## Parte C - Multicast

1. No unicast repetido, o remetente envia uma copia da mesma mensagem para cada cliente. Com tres clientes, sao tres envios. No multicast, o remetente envia uma unica mensagem para o grupo, e a rede entrega aos membros inscritos. Isso reduz o trabalho do emissor e o trafego duplicado em parte do caminho.

2. TTL significa `time-to-live`. No multicast, ele limita quantos saltos o pacote pode atravessar na rede. Isso e importante para impedir que avisos locais se espalhem alem do escopo desejado, como para outras redes ou segmentos onde nao fazem sentido.

3. O cliente offline nao recebe os avisos perdidos. Multicast, neste laboratorio, nao guarda historico nem possui confirmacao de entrega. O cliente so recebe as mensagens enviadas enquanto esta inscrito no grupo e com o socket ativo.

## Parte D - WebSocket

1. Depois do handshake HTTP com `Upgrade: websocket`, a conexao deixa de seguir o modelo HTTP tradicional de requisicao e resposta. O mesmo TCP permanece aberto, mas passa a transportar frames WebSocket, permitindo mensagens nos dois sentidos a qualquer momento.

2. No multicast, os destinatarios sao alcancados pela inscricao em um grupo IP; o servidor nao conhece individualmente cada cliente. No WebSocket, o servidor aceita conexoes TCP/WebSocket individuais e mantem um conjunto de clientes conectados para reenviar as mensagens a todos.

3. WebSocket e mais adequado que TCP cru para o mural porque ja define handshake, framing de mensagens, integracao natural com navegadores e um modelo padrao para comunicacao full-duplex. TCP cru tambem manteria conexao aberta, mas exigiria criar um protocolo proprio para separar mensagens, controlar clientes e integrar com aplicacoes web.
