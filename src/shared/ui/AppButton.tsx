import { Pressable, PressableProps, StyleSheet } from "react-native";
import { colors } from "@/shared/theme/colors";
import { AppText } from "@/shared/ui/AppText";

type AppButtonProps = PressableProps & {
  label: string;
  variant?: "primary" | "danger" | "ghost";
};

export function AppButton({ label, variant = "primary", ...props }: AppButtonProps) {
  return (
    <Pressable style={[styles.base, styles[variant], props.disabled ? styles.disabled : null]} {...props}>
      <AppText style={styles.label}>{label}</AppText>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  base: {
    borderRadius: 10,
    paddingVertical: 12,
    paddingHorizontal: 14,
    alignItems: "center",
    justifyContent: "center"
  },
  primary: {
    backgroundColor: colors.brand
  },
  danger: {
    backgroundColor: colors.danger
  },
  ghost: {
    backgroundColor: colors.surfaceAlt
  },
  disabled: {
    opacity: 0.6
  },
  label: {
    fontWeight: "600"
  }
});
