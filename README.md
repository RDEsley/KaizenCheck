# KaizenCheck

Aplicativo Android de checklist

## Funcionalidades

- **Múltiplas listas** – Crie, edite e organize diversas checklists
- **Itens dinâmicos** – Adicione e remova itens, marque como concluído
- **Persistência local** – Dados salvos no dispositivo com Room
- **UI Material 3** – Design moderno com tema claro/escuro automático

## Requisitos

- Android Studio Ladybug (2024.2.1) ou superior, com JDK 17 configurado
- minSdk 26
- targetSdk 34

## Como usar

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/seu-usuario/KaizenCheck.git
   cd KaizenCheck
   ```

2. **Abra no Android Studio:**
   - File → Open → Selecione a pasta do projeto
   - Aguarde o Gradle sincronizar

3. **Execute o app:**
   - Conecte um dispositivo ou inicie um emulador
   - Clique em Run (ou Shift+F10)

## Stack

- Kotlin 2.0
- Jetpack Compose + Material 3
- Room Database
- Navigation Compose
- MVVM

## Estrutura

```
app/src/main/java/com/kaizencheck/app/
├── MainActivity.kt          # Activity principal com navegação
├── data/                    # Camada de dados (Room)
├── ui/                      # Tema, componentes e telas
└── viewmodel/               # ViewModel
```

## Licença

MIT
