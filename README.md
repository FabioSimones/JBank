## Projeto JBank
#### Autores
- [Fábio Simones](https://github.com/FabioSimones)

### Descrição do projeto:
<p>Este projeto consiste na criação de um sistema de banking usando Spring Boot. O sistema permitirá a criação e gestão de carteiras bancárias, 
  incluindo a realização de depósitos, transferências e consultas de extratos. Adicionalmente, o projeto deverá incluir funcionalidades de 
  logging para auditoria de todas as transações e acesso ao sistema.</p>

### Funcionalidades do sistema:

<ul>
  <li>Criar uma Carteira: </li>
  <ul>
    <li>Permitir a criação de uma carteira bancária com informações como CPF, email e nome do titular.</li>
  </ul>
</ul>

  <ul>
    <li>Encerrar uma Carteira: </li>
    <ul>
      <li>Permitir o fechamento de uma carteira bancária existente, desde que o saldo esteja zerado.</li>
    </ul>
  </ul>

  <ul>
    <li>Depositar Dinheiro: </li>
    <ul>
      <li>Realizar depósitos de dinheiro em uma carteira existente. Este serviço deve atualizar o saldo da carteira correspondente e registrar 
        os dados na tabela de histórico de depósitos.
      </li>
    </ul>
  </ul>

  <ul>
    <li>Realizar Transferências: </li>
    <ul>
      <li>Permitir a transferência de fundos de uma carteira para outra. Deve verificar a disponibilidade de saldo suficiente antes de completar a transação.
      </li>
    </ul>
  </ul>

  <ul>
    <li>Consultar Extrato: </li>
    <ul>
      <li>Gerar e fornecer um extrato detalhado das transações realizadas em uma carteira, incluindo depósitos, transferências recebidas e enviadas, com data e hora.
      </li>
    </ul>
  </ul>
</ul>

### Entidades e Relacionamentos:

<ul>
  <li>Carteira:</li>
  <ul>
    <li>Código da Conta(uuid)</li>
    <li>CPF (unique)</li>
    <li>E-mail (unique)</li>
    <li>Nome do Titular</li>
    <li>Saldo Atual</li>
  </ul>
</ul>

<ul>
  <li>Transferência:</li>
  <ul>
    <li>Código da Transferência (uuid)</li>
    <li>Carteira de Origem (Many-to-One): Carteira de onde o dinheiro será debitado.</li>
    <li>Carteira de Destino (Many-to-One): Carteira para onde o dinheiro será creditado.</li>
    <li>Valor da Transferência</li>
    <li>Data e Hora da Transferência</li>
  </ul>
</ul>

<ul>
  <li>Depósitos:</li>
  <ul>
    <li>Código do deposito (uuid)</li>
    <li>Carteira de destino do depósito (Many-to-One).</li>
    <li>Valor depositado.</li>
    <li>Data e hora do depósito.</li>
    <li>Endereço IP do usuário que fez o depósito.</li>
  </ul>
</ul>

### Regras do Projeto:
<ul>
  <li>Logging e Auditoria:</li>
  <ul>
    <li>Filtro de IP: Implementar um filtro para capturar e logar o endereço IP de cada usuário que faz uma solicitação ao sistema. Esse IP deve ser 
      incluído no header de resposta.</li>
    <li>Interceptor de Auditoria: Criar um interceptor para logar todas as requisições e respostas do sistema. Este interceptor deve registrar detalhes 
      como método HTTP, URL, código de status da resposta e endereço ip.</li>
  </ul>
</ul>

<ul>
  <li>Validações e Regras de Negócio:</li>
  <ul>
    <li>Saldo Suficiente: Antes de realizar uma transferência, o sistema deve verificar se a conta de origem possui saldo suficiente.</li>
    <li>Encerramento de Conta: Só permitir o encerramento de contas que possuem saldo igual a zero.</li>
    <li>Transações Simultâneas: Garantir a consistência das operações de depósito e transferência, utilizando mecanismos apropriados de controle 
      de transações (transações ACID).</li>
  </ul>
</ul>

<p>
  Ao termino deste projeto pude adquirir: experiência prática na configuração de projetos Spring Boot, manipulação de transações bancárias, 
  implementação de filtros e interceptores para auditoria e logging, validação de dados da requisição com e na criação de APIs RESTful para 
  operações CRUD e gerenciamento de transações bancárias.
</p>
  
<p>
  Este projeto me capacitou com o conhecimento e habilidades necessárias para construir e gerenciar sistemas backend robustos e 
  seguros usando Spring Boot, Spring Data JPA, Hibernate Validator e boas práticas de logging e auditoria.
</p>
