# KaizenCheck Mobile

Aplicativo de tarefas inspirado no Microsoft To Do, feito com React Native + TypeScript, autenticação por Firebase e sincronização em nuvem com Firestore.

## Visão geral

- Listas e tarefas por usuário
- Login e cadastro com email/senha
- Sincronização em tempo real
- Organização por status (pendente/concluída)
- Base preparada para evoluções (notificações, filtros, etc.)

## Stack

- `expo` + `react-native` + `typescript`
- `expo-router` para rotas
- `firebase` (Auth + Firestore)
- `@tanstack/react-query` para cache/mutações
- `zustand` para estado de UI

## Requisitos

- Node.js 20+
- npm 10+
- Conta Firebase
- Expo Go (Android/iOS) ou emulador

## Setup rápido

```bash
npm install
```

Crie o `.env`:

```powershell
Copy-Item .env.exemple .env
```

Depois rode:

```bash
npm run start
```

## Configuração Firebase (passo a passo)

### 1) Criar projeto

1. Acesse [Firebase Console](https://console.firebase.google.com/)
2. Crie um projeto
3. Vá em `Project settings`
4. Em `Your apps`, adicione um app Web (`</>`)
5. Copie o objeto `firebaseConfig`

### 2) Habilitar Authentication

1. `Authentication`
2. `Sign-in method`
3. Ative `Email/Password`

### 3) Habilitar Firestore

1. `Firestore Database`
2. `Create database`
3. Escolha a região
4. Pode iniciar em modo de produção

### 4) Aplicar regras

1. Abra `Firestore -> Segurança` (ou `Rules` no console em inglês)
2. Copie o conteúdo de `firestore.rules`
3. Clique em `Publicar`

### 5) Configurar variáveis de ambiente

Preencha o `.env` com os dados do seu projeto:

```env
EXPO_PUBLIC_FIREBASE_API_KEY=...
EXPO_PUBLIC_FIREBASE_AUTH_DOMAIN=...
EXPO_PUBLIC_FIREBASE_PROJECT_ID=...
EXPO_PUBLIC_FIREBASE_STORAGE_BUCKET=...
EXPO_PUBLIC_FIREBASE_MESSAGING_SENDER_ID=...
EXPO_PUBLIC_FIREBASE_APP_ID=...
```

Mapeamento:
- `EXPO_PUBLIC_FIREBASE_API_KEY` -> `apiKey`
- `EXPO_PUBLIC_FIREBASE_AUTH_DOMAIN` -> `authDomain`
- `EXPO_PUBLIC_FIREBASE_PROJECT_ID` -> `projectId`
- `EXPO_PUBLIC_FIREBASE_STORAGE_BUCKET` -> `storageBucket`
- `EXPO_PUBLIC_FIREBASE_MESSAGING_SENDER_ID` -> `messagingSenderId`
- `EXPO_PUBLIC_FIREBASE_APP_ID` -> `appId`

## Como rodar

- `npm run start` inicia o servidor Expo
- No Android com Expo Go, escaneie o QR code
- Para emulador Android, inicie o emulador antes e rode `npm run android`
- Para web: `npm run web`

## Comandos úteis

- `npm run typecheck`
- `npm run lint`
- `npx expo start -c` (limpar cache)

## Regras de negócio (MVP)

- Cada usuário acessa somente seus próprios dados
- Lista/tarefa sem título não deve ser salva
- Tarefa concluída registra `completedAt`
- Reabrir tarefa limpa `completedAt`
- Ordenação padrão: pendentes primeiro, depois concluídas

## Modelo de dados (Firestore)

- `users/{uid}/lists/{listId}`
  - `title`, `color`, `order`, `createdAt`, `updatedAt`
- `users/{uid}/lists/{listId}/tasks/{taskId}`
  - `title`, `notes`, `dueDate`, `status`, `order`, `completedAt`, `createdAt`, `updatedAt`

## Estrutura do projeto

```txt
app/
  (auth)/
  (app)/
  _layout.tsx
  index.tsx
src/
  features/
    auth/
    lists/
    tasks/
  services/firebase/
  providers/
  shared/
firestore.rules
.env.exemple
```

## Troubleshooting

- **Expo Go em SDK diferente**
  - atualize o projeto para o SDK suportado pelo app Expo Go
- **No Android connected device found**
  - conecte celular (USB debug) ou inicie emulador no Android Studio
- **Permission denied no Firestore**
  - publique as regras em `firestore.rules`
- **Variáveis não carregando**
  - revise `.env` e reinicie o Expo

## Roadmap

1. Filtros (`Hoje`, `Importante`, `Concluídas`)
2. Reordenação drag-and-drop
3. Notificações locais
4. Recuperação de senha
5. Login Google/Apple

## Licença

MIT
