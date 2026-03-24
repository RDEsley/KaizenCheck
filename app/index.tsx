import { ActivityIndicator, View } from "react-native";
import { Redirect } from "expo-router";
import { useAuthSession } from "@/features/auth/useAuthSession";
import { colors } from "@/shared/theme/colors";

export default function IndexPage() {
  const { user, loading } = useAuthSession();

  if (loading) {
    return (
      <View style={{ flex: 1, alignItems: "center", justifyContent: "center", backgroundColor: colors.background }}>
        <ActivityIndicator color={colors.brand} />
      </View>
    );
  }

  return <Redirect href={user ? "/(app)/lists" : "/(auth)/sign-in"} />;
}
