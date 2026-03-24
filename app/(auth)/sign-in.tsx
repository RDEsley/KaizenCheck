import { useState } from "react";
import { StyleSheet, View } from "react-native";
import { Link, router } from "expo-router";
import { signInWithEmail } from "@/services/firebase/auth";
import { Screen } from "@/shared/ui/Screen";
import { AppText } from "@/shared/ui/AppText";
import { AppInput } from "@/shared/ui/AppInput";
import { AppButton } from "@/shared/ui/AppButton";
import { colors } from "@/shared/theme/colors";

export default function SignInScreen() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit() {
    setError(null);
    setSubmitting(true);
    try {
      await signInWithEmail(email.trim(), password);
      router.replace("/(app)/lists");
    } catch {
      setError("Nao foi possivel entrar. Verifique email e senha.");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <Screen>
      <View style={styles.container}>
        <AppText style={styles.title}>KaizenCheck</AppText>
        <AppText style={styles.subtitle}>Entre para sincronizar suas tarefas.</AppText>
        <AppInput
          placeholder="Email"
          keyboardType="email-address"
          autoCapitalize="none"
          value={email}
          onChangeText={setEmail}
        />
        <AppInput
          placeholder="Senha"
          secureTextEntry
          value={password}
          onChangeText={setPassword}
        />
        {error ? <AppText style={styles.error}>{error}</AppText> : null}
        <AppButton
          label={submitting ? "Entrando..." : "Entrar"}
          onPress={handleSubmit}
          disabled={submitting}
        />
        <Link href="/(auth)/sign-up" style={styles.link}>
          Criar conta
        </Link>
      </View>
    </Screen>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    gap: 12,
    justifyContent: "center"
  },
  title: {
    fontSize: 28,
    fontWeight: "700"
  },
  subtitle: {
    color: colors.textSecondary,
    marginBottom: 10
  },
  error: {
    color: colors.danger
  },
  link: {
    color: colors.brand,
    textAlign: "center",
    marginTop: 4
  }
});
