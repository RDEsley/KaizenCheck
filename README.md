# KaizenCheck Mobile

KaizenCheck foi migrado de um app Android nativo para um app mobile com React Native + TypeScript, inspirado no Microsoft To Do.

## Objetivo do produto

Criar um app de produtividade simples e confiavel para listas e tarefas com:
- autenticacao de usuario;
- sincronizacao em nuvem em tempo real;
- experiencia clara para criar, concluir e organizar tarefas.

## Stack escolhida

- **App:** React Native + TypeScript com Expo
- **Navegacao:** Expo Router
- **Backend/Banco:** Firebase (Auth + Firestore)
- **Dados no app:** TanStack Query (cache, revalidacao, mutacoes)
- **Estado de UI pontual:** Zustand

## Por que Firebase

Entre Appwrite, PocketBase, Convex e Firebase, o Firebase foi escolhido por:
- setup rapido para MVP com Auth e banco sem servidor proprio;
- SDK maduro para mobile;
- Firestore com sincronizacao eficiente e evolucao facil para escala;
- regras de seguranca robustas no proprio banco.

## Requisitos

- Node.js 20+
- npm 10+
- Conta Firebase
- Expo Go no celular (ou emulador Android/iOS)

## Tutorial completo: conectar no Firebase e rodar

### 1) Instalar requisitos

- Instale `Node.js 20+`
- Confirme no terminal:

```bash
node -v
npm -v
```

### 2) Instalar dependencias do projeto

```bash
npm install
```

### 3) Criar e preencher o `.env`

No Windows PowerShell:

```powershell
Copy-Item .env.example .env
```

No macOS/Linux:

```bash
cp .env.example .env
```

Depois abra o `.env` e preencha os valores do Firebase.

### 4) Criar projeto Firebase

1. Acesse [Firebase Console](https://console.firebase.google.com/)
2. Clique em **Create a project**
3. Dê o nome `kaizencheck` (ou outro)
4. Ao finalizar, abra **Project settings**
5. Em **Your apps**, clique no icone `</>` para criar um **Web App**
6. Copie os dados de configuração (`apiKey`, `authDomain`, etc.)

### 5) Habilitar Authentication

1. Menu **Authentication**
2. Aba **Sign-in method**
3. Ative **Email/Password**

### 6) Habilitar Firestore

1. Menu **Firestore Database**
2. Clique em **Create database**
3. Selecione a regiao (de preferencia a mais proxima dos usuarios)
4. Pode iniciar em modo de teste e depois aplicar regras restritivas

### 7) Aplicar regras de seguranca

1. Abra o menu **Firestore Database -> Rules**
2. Copie e cole o conteudo do arquivo `firestore.rules`
3. Clique em **Publish**

### 8) Configurar variaveis no `.env`

Use os dados do seu projeto:

```env
EXPO_PUBLIC_FIREBASE_API_KEY=...
EXPO_PUBLIC_FIREBASE_AUTH_DOMAIN=...
EXPO_PUBLIC_FIREBASE_PROJECT_ID=...
EXPO_PUBLIC_FIREBASE_STORAGE_BUCKET=...
EXPO_PUBLIC_FIREBASE_MESSAGING_SENDER_ID=...
EXPO_PUBLIC_FIREBASE_APP_ID=...
```

Mapeamento rapido:
- `EXPO_PUBLIC_FIREBASE_API_KEY` -> `apiKey`
- `EXPO_PUBLIC_FIREBASE_AUTH_DOMAIN` -> `authDomain`
- `EXPO_PUBLIC_FIREBASE_PROJECT_ID` -> `projectId`
- `EXPO_PUBLIC_FIREBASE_STORAGE_BUCKET` -> `storageBucket`
- `EXPO_PUBLIC_FIREBASE_MESSAGING_SENDER_ID` -> `messagingSenderId`
- `EXPO_PUBLIC_FIREBASE_APP_ID` -> `appId`

### 9) Rodar o app

```bash
npm run start
```

Depois:
- Android: `npm run android` ou ler QR no Expo Go
- iOS: `npm run ios`
- Web (teste rapido): `npm run web`

### 10) Validar se conectou certo

Checklist:
- abre tela de login sem erro
- consegue criar conta
- consegue criar lista
- consegue criar tarefa
- fecha e abre o app e os dados continuam (sync por conta)

Se tudo isso funciona, Firebase esta integrado corretamente.

## Erros comuns e como resolver

- **Erro de credencial Firebase**
  - revise `.env` e reinicie o app (`Ctrl + C` e `npm run start`)
- **Tela em branco ou erro de bundle**
  - execute `npm install` novamente
  - limpe cache: `npx expo start -c`
- **Permissao negada no Firestore**
  - confirme publicacao das regras em `firestore.rules`
  - valide se esta logado (usuario autenticado)

## Modelo de dados (Firestore)

Colecoes:

- `users/{uid}/lists/{listId}`
  - `title: string`
  - `color?: string`
  - `order: number`
  - `createdAt`, `updatedAt`
- `users/{uid}/lists/{listId}/tasks/{taskId}`
  - `title: string`
  - `notes?: string`
  - `dueDate?: string`
  - `status: "pending" | "completed"`
  - `order: number`
  - `completedAt?: timestamp | null`
  - `createdAt`, `updatedAt`

## Regras de negocio (MVP)

- Usuario autenticado acessa apenas os proprios dados.
- Nao criar lista/tarefa sem titulo valido.
- Ordenacao padrao de tarefas:
  - pendentes primeiro;
  - concluidas depois;
  - ordem manual dentro de cada grupo.
- Concluir tarefa registra `completedAt`; reabrir limpa `completedAt`.
- Exclusao de lista no MVP eh definitiva (com confirmacao no fluxo de UI recomendado).

## Requisitos de UX e engenharia de requisitos

### Requisitos funcionais

- Cadastro e login com email/senha
- CRUD de listas
- CRUD de tarefas dentro da lista
- Concluir e reabrir tarefas
- Sincronizacao de dados por conta

### Requisitos nao funcionais

- Interface responsiva e consistente
- Feedback visual para loading, erro e vazio
- Estrutura de codigo por feature para facilitar evolucao
- Base pronta para offline-first incremental

### Boas praticas de UX

- foco em tarefas de alta frequencia (criar/concluir em poucos toques);
- estados vazios com orientacao clara;
- textos de acao objetivos;
- contrastes e tamanhos adequados para acessibilidade.

## Estrutura do projeto

```txt
app/
  _layout.tsx
  index.tsx
  (auth)/
    sign-in.tsx
    sign-up.tsx
  (app)/
    lists.tsx
    lists/[listId].tsx
src/
  features/
    auth/
    lists/
    tasks/
  providers/
  services/firebase/
  shared/
firestore.rules
.env.example
```

## Scripts uteis

- `npm run start` - inicia Metro/Expo
- `npm run android` - abre no Android
- `npm run ios` - abre no iOS
- `npm run web` - roda em modo web
- `npm run typecheck` - valida TypeScript
- `npm run lint` - roda ESLint

## Roadmap sugerido

1. Filtros "Hoje", "Importante" e "Concluidas"
2. Reordenacao drag-and-drop
3. Notificacoes locais para prazos
4. Recuperacao de senha
5. Login Google e Apple
6. Modo offline mais robusto (fila de mutacoes)

## Observacoes de seguranca

- Nunca commitar `.env` real
- Regras Firestore devem estar restritivas antes de producao
- Em producao, revisar limites e validacoes de campos nas regras

## Licenca

MIT
