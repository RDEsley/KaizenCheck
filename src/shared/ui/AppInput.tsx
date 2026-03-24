import { TextInput, TextInputProps, StyleSheet } from "react-native";
import { colors } from "@/shared/theme/colors";

export function AppInput(props: TextInputProps) {
  return (
    <TextInput
      placeholderTextColor={colors.textSecondary}
      style={[styles.input, props.style]}
      {...props}
    />
  );
}

const styles = StyleSheet.create({
  input: {
    backgroundColor: colors.surface,
    borderWidth: 1,
    borderColor: colors.border,
    borderRadius: 10,
    color: colors.textPrimary,
    paddingHorizontal: 14,
    paddingVertical: 12
  }
});
