import { Stack } from "expo-router";
import { colors } from "@/shared/theme/colors";

export default function PrivateLayout() {
  return (
    <Stack
      screenOptions={{
        headerStyle: { backgroundColor: colors.background },
        headerTintColor: colors.textPrimary,
        headerShadowVisible: false,
        contentStyle: { backgroundColor: colors.background }
      }}
    />
  );
}
