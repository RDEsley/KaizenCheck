import { ReactNode } from "react";
import { SafeAreaView, ScrollView, StyleSheet, View } from "react-native";
import { colors } from "@/shared/theme/colors";

type ScreenProps = {
  children: ReactNode;
  padded?: boolean;
  scrollable?: boolean;
};

export function Screen({ children, padded = true, scrollable = false }: ScreenProps) {
  const content = <View style={[styles.content, padded ? styles.padded : null]}>{children}</View>;

  return (
    <SafeAreaView style={styles.container}>
      {scrollable ? <ScrollView>{content}</ScrollView> : content}
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.background
  },
  content: {
    flex: 1
  },
  padded: {
    padding: 16
  }
});
