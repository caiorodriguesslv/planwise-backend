# 📋 Guia das Collections do Postman - Planwise

## 🚀 Como Importar as Collections

### 1. **Importar no Postman**
1. Abra o Postman
2. Clique em "Import" (botão no canto superior esquerdo)
3. Selecione os arquivos JSON das collections:
   - `POSTMAN-AUTH-COLLECTION.json`
   - `POSTMAN-CATEGORIES-COLLECTION.json`
   - `POSTMAN-INCOMES-COLLECTION.json`
   - `POSTMAN-EXPENSES-COLLECTION.json`
   - `POSTMAN-GOALS-COLLECTION.json`
   - `POSTMAN-REPORTS-COLLECTION.json`
   - `POSTMAN-USERS-COLLECTION.json`

### 2. **Configurar Variáveis de Ambiente**
1. Crie um novo Environment no Postman
2. Adicione as seguintes variáveis:
   - `baseUrl`: `http://localhost:8080`
   - `token`: (deixe vazio inicialmente)

## 📚 Collections Disponíveis

### 🔐 **1. Autenticação** (`POSTMAN-AUTH-COLLECTION.json`)
- **Registrar Usuário**: Cria um novo usuário
- **Login**: Autentica e obtém token JWT
- **Health Check**: Verifica se o serviço está funcionando

### 📂 **2. Categorias** (`POSTMAN-CATEGORIES-COLLECTION.json`)
- **Criar Categoria**: Cria categorias de receita ou despesa
- **Listar Categorias**: Lista todas as categorias do usuário
- **Filtrar por Tipo**: Lista categorias por tipo (RECEITA/DESPESA)
- **Buscar por ID**: Busca categoria específica
- **Atualizar**: Atualiza dados da categoria
- **Buscar por Texto**: Pesquisa categorias por nome
- **Deletar**: Remove categoria (soft delete)

### 💰 **3. Receitas** (`POSTMAN-INCOMES-COLLECTION.json`)
- **Criar Receita**: Registra nova receita
- **Listar Receitas**: Lista todas as receitas
- **Filtrar por Categoria**: Receitas de uma categoria específica
- **Filtrar por Período**: Receitas em um período específico
- **Buscar por ID**: Receita específica
- **Atualizar**: Atualiza dados da receita
- **Buscar por Texto**: Pesquisa receitas por descrição
- **Total de Receitas**: Soma total de todas as receitas
- **Total por Período**: Soma total em um período
- **Deletar**: Remove receita (soft delete)

### 💸 **4. Despesas** (`POSTMAN-EXPENSES-COLLECTION.json`)
- **Criar Despesa**: Registra nova despesa
- **Listar Despesas**: Lista todas as despesas
- **Filtrar por Categoria**: Despesas de uma categoria específica
- **Filtrar por Período**: Despesas em um período específico
- **Buscar por ID**: Despesa específica
- **Atualizar**: Atualiza dados da despesa
- **Buscar por Texto**: Pesquisa despesas por descrição
- **Total de Despesas**: Soma total de todas as despesas
- **Total por Período**: Soma total em um período
- **Deletar**: Remove despesa (soft delete)

### 🎯 **5. Metas** (`POSTMAN-GOALS-COLLECTION.json`)
- **Criar Meta**: Define nova meta financeira
- **Listar Metas**: Lista todas as metas
- **Filtrar por Status**: Metas por status (EM_ANDAMENTO, ATINGIDA, VENCIDA)
- **Metas Vencidas**: Lista metas que passaram do prazo
- **Buscar por ID**: Meta específica
- **Atualizar Meta**: Atualiza dados da meta
- **Atualizar Progresso**: Define valor atual da meta
- **Adicionar Progresso**: Adiciona valor ao progresso atual
- **Buscar por Texto**: Pesquisa metas por descrição
- **Contar por Status**: Quantidade de metas por status
- **Atualizar Vencidas**: Marca metas vencidas automaticamente
- **Deletar**: Remove meta (soft delete)

### 📊 **6. Relatórios** (`POSTMAN-REPORTS-COLLECTION.json`)
- **Resumo Financeiro**: Resumo geral de receitas e despesas
- **Resumo por Período**: Resumo financeiro em um período
- **Resumo de Metas**: Estatísticas das metas
- **Relatório Mensal**: Resumo de um mês específico
- **Relatório Anual**: Resumo de um ano específico

### 👤 **7. Usuários** (`POSTMAN-USERS-COLLECTION.json`)
- **Listar Usuários**: Lista todos os usuários (admin)
- **Buscar por ID**: Usuário específico
- **Usuário Atual**: Dados do usuário logado
- **Atualizar Usuário**: Atualiza dados do usuário
- **Alterar Senha**: Modifica senha do usuário
- **Ativar/Desativar**: Controla status do usuário
- **Alterar Role**: Modifica permissões do usuário
- **Deletar**: Remove usuário (soft delete)

## 🔧 **Configuração das Variáveis**

### **Variáveis Globais**
- `baseUrl`: URL base da API (http://localhost:8080)
- `token`: Token JWT para autenticação

### **Variáveis Específicas**
- `categoryId`: ID da categoria para testes (definido após criar categorias)

## 📝 **Sequência de Testes Recomendada**

### **1. Primeiro, teste a autenticação:**
1. Execute "Registrar Usuário"
2. Execute "Login" (o token será salvo automaticamente)

### **2. Configure categorias:**
1. Execute "Criar Categoria - Receita"
2. Execute "Criar Categoria - Despesa"
3. Anote os IDs retornados para usar nas variáveis

### **3. Teste receitas e despesas:**
1. Atualize a variável `categoryId` com o ID da categoria de receita
2. Execute "Criar Receita"
3. Atualize a variável `categoryId` com o ID da categoria de despesa
4. Execute "Criar Despesa"

### **4. Teste metas:**
1. Execute "Criar Meta"
2. Teste as funcionalidades de progresso

### **5. Teste relatórios:**
1. Execute "Resumo Financeiro Geral"
2. Teste os outros relatórios

## ⚠️ **Observações Importantes**

1. **Token JWT**: O token é salvo automaticamente após login/registro
2. **Soft Delete**: Dados não são removidos fisicamente, apenas marcados como inativos
3. **Isolamento**: Cada usuário só vê seus próprios dados
4. **Validações**: Categorias devem ser do tipo correto (RECEITA para receitas, DESPESA para despesas)
5. **Datas**: Use formato ISO (YYYY-MM-DD) para datas

## 🚀 **Iniciando os Testes**

1. Certifique-se de que a aplicação está rodando na porta 8080
2. Importe todas as collections
3. Configure o environment com as variáveis
4. Execute primeiro a autenticação
5. Siga a sequência recomendada de testes

Boa sorte com os testes! 🎉
