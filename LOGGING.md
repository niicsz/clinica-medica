# Sistema de Logs - Clínica Médica

## Visão Geral

O sistema de logs foi implementado para melhorar a rastreabilidade e facilitar o diagnóstico de problemas na aplicação Clínica Médica.

## Configuração de Logs

### Níveis de Log Configurados

Os níveis de log estão configurados no arquivo `application.yml`:

- **Root**: INFO
- **com.example.clinica_medica**: DEBUG
- **com.example.clinica_medica.controller**: INFO
- **com.example.clinica_medica.services**: INFO
- **com.example.clinica_medica.security**: DEBUG
- **com.example.clinica_medica.exceptions**: WARN
- **org.springframework.web**: INFO
- **org.springframework.security**: DEBUG
- **org.mongodb.driver**: WARN

### Arquivos de Log

- **Console**: Logs são exibidos no console durante a execução
- **Arquivo**: `logs/clinica-medica.log`
  - Tamanho máximo por arquivo: 10MB
  - Histórico mantido: 30 dias
  - Rotação automática quando atingir o tamanho máximo

### Formato do Log

```
yyyy-MM-dd HH:mm:ss.SSS [thread] LEVEL logger - mensagem
```

Exemplo:
```
2025-10-25 22:00:00.123 [http-nio-8080-exec-1] INFO  c.e.c.controller.api.AuthController - Requisição de login recebida para email: usuario@example.com
```

## Logs por Componente

### 1. AuthService (Autenticação e Registro)

**Logs de Autenticação:**
- INFO: Tentativas de autenticação e sucessos
- ERROR: Falhas na autenticação com detalhes do erro

**Logs de Registro:**
- INFO: Tentativas de registro e sucessos
- ERROR: Falhas no registro com detalhes do erro

### 2. AuthController

**Logs de Login:**
- INFO: Requisições de login recebidas
- WARN: Credenciais inválidas
- ERROR: Erros inesperados

**Logs de Registro:**
- INFO: Requisições de registro recebidas
- WARN: Dados duplicados ou inválidos
- ERROR: Erros inesperados

### 3. Serviços (UsuarioService, PacienteService, MedicoService, ConsultaService)

**Operações CRUD:**
- INFO: Inclusão, atualização e exclusão de registros
- DEBUG: Listagem e buscas de registros
- WARN: Registros não encontrados
- ERROR: Erros durante operações com detalhes

### 4. ClinicaMedicaController (API REST)

**Endpoints da API:**
- INFO: Requisições de criação, atualização e exclusão
- DEBUG: Requisições de listagem e busca
- ERROR: Validações falhas ou dados inválidos

### 5. JwtAuthenticationFilter

**Processamento de Tokens:**
- DEBUG: Processamento de requisições e validação de tokens
- WARN: Tokens inválidos
- ERROR: Erros no processamento de tokens
- TRACE: Requisições sem token

### 6. CustomUserDetailsService

**Carregamento de Usuários:**
- DEBUG: Carregamento e localização de usuários
- WARN: Usuários não encontrados

### 7. GlobalExceptionHandler

**Tratamento de Exceções:**
- WARN: IllegalArgumentException capturadas
- ERROR: DataIntegrityViolationException e RuntimeException

## Casos de Uso para Rastreamento

### Autenticação Falha
```
2025-10-25 22:00:00.123 INFO  AuthController - Requisição de login recebida para email: usuario@example.com
2025-10-25 22:00:00.124 INFO  AuthService - Tentativa de autenticação para o email: usuario@example.com
2025-10-25 22:00:00.125 ERROR AuthService - Erro durante autenticação para email: usuario@example.com - Erro: Bad credentials
2025-10-25 22:00:00.126 WARN  AuthController - Falha no login - Credenciais inválidas para email: usuario@example.com
```

### Registro de Novo Usuário
```
2025-10-25 22:00:00.123 INFO  AuthController - Requisição de registro recebida para email: novo@example.com (Nome: João Silva)
2025-10-25 22:00:00.124 INFO  AuthService - Tentativa de registro para o email: novo@example.com com nome: João Silva
2025-10-25 22:00:00.125 INFO  UsuarioService - Incluindo novo usuário: João Silva (Email: novo@example.com)
2025-10-25 22:00:00.130 INFO  UsuarioService - Usuário incluído com sucesso: ID 507f1f77bcf86cd799439011
2025-10-25 22:00:00.131 INFO  AuthService - Usuário registrado com sucesso: novo@example.com (CPF: 12345678901)
2025-10-25 22:00:00.132 INFO  AuthController - Registro bem-sucedido para email: novo@example.com
```

### Agendamento de Consulta
```
2025-10-25 22:00:00.123 INFO  ClinicaMedicaController - API - Requisição para agendar consulta
2025-10-25 22:00:00.124 INFO  ConsultaService - Agendando nova consulta para paciente ID: 123 com médico ID: 456
2025-10-25 22:00:00.130 INFO  ConsultaService - Consulta agendada com sucesso: ID 789 para data/hora 2025-10-26T10:00:00
```

### Token JWT Inválido
```
2025-10-25 22:00:00.123 DEBUG JwtAuthenticationFilter - Processando requisição: GET /api/pacientes
2025-10-25 22:00:00.124 DEBUG JwtAuthenticationFilter - Token JWT encontrado para usuário: usuario@example.com
2025-10-25 22:00:00.125 WARN  JwtAuthenticationFilter - Token JWT inválido para usuário: usuario@example.com
```

### Erro ao Excluir Registro
```
2025-10-25 22:00:00.123 INFO  PacienteService - Excluindo paciente com ID: 123
2025-10-25 22:00:00.124 ERROR PacienteService - Erro ao excluir paciente ID 123 - Erro: ...
2025-10-25 22:00:00.125 ERROR GlobalExceptionHandler - DataIntegrityViolationException capturada - URI: /api/pacientes/123 - Erro: ...
```

## Monitoramento e Análise

### Visualizar Logs em Tempo Real

```bash
# Windows
type logs\clinica-medica.log

# Linux/Mac
tail -f logs/clinica-medica.log
```

### Filtrar Logs por Nível

```bash
# Apenas erros
findstr /C:"ERROR" logs\clinica-medica.log

# Apenas warnings e erros
findstr /C:"WARN ERROR" logs\clinica-medica.log
```

### Buscar por Email/ID de Usuário

```bash
findstr "usuario@example.com" logs\clinica-medica.log
```

## Boas Práticas

1. **Não logar informações sensíveis**: Senhas, tokens completos, dados de cartão
2. **Usar níveis apropriados**:
   - ERROR: Erros que precisam atenção imediata
   - WARN: Situações anormais mas tratadas
   - INFO: Eventos importantes de negócio
   - DEBUG: Informações detalhadas para diagnóstico
   - TRACE: Informações muito detalhadas
3. **Incluir contexto relevante**: IDs, emails, timestamps
4. **Mensagens descritivas**: Facilitar entendimento sem ver código

## Troubleshooting

### Logs não aparecem
1. Verifique o nível de log em `application.yml`
2. Verifique se a pasta `logs/` tem permissão de escrita
3. Reinicie a aplicação

### Arquivo de log muito grande
- O sistema faz rotação automática a cada 10MB
- Mantém histórico de 30 dias
- Arquivos antigos são automaticamente removidos

### Logs duplicados
- Normal em ambientes de desenvolvimento com hot-reload
- Em produção, verifique se há múltiplas instâncias rodando

