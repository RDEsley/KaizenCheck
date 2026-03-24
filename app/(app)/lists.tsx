import { useMemo, useState } from "react";
import { FlatList, Pressable, StyleSheet, View } from "react-native";
import { router } from "expo-router";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createList, fetchLists, removeList, renameList } from "@/features/lists/api";
import { useAuthSession } from "@/features/auth/useAuthSession";
import { signOutSession } from "@/services/firebase/auth";
import { Screen } from "@/shared/ui/Screen";
import { AppText } from "@/shared/ui/AppText";
import { AppInput } from "@/shared/ui/AppInput";
import { AppButton } from "@/shared/ui/AppButton";
import { Card } from "@/shared/ui/Card";
import { colors } from "@/shared/theme/colors";

export default function ListsScreen() {
  const { user, loading } = useAuthSession();
  const [newListTitle, setNewListTitle] = useState("");
  const [editingListId, setEditingListId] = useState<string | null>(null);
  const [editingTitle, setEditingTitle] = useState("");
  const queryClient = useQueryClient();

  const uid = user?.uid ?? null;
  const key = useMemo(() => ["lists", uid], [uid]);

  const listsQuery = useQuery({
    queryKey: key,
    queryFn: () => fetchLists(uid!),
    enabled: Boolean(uid)
  });

  const createMutation = useMutation({
    mutationFn: async () => {
      const order = listsQuery.data?.length ?? 0;
      await createList(uid!, { title: newListTitle }, order);
    },
    onSuccess: () => {
      setNewListTitle("");
      queryClient.invalidateQueries({ queryKey: key });
    }
  });

  const renameMutation = useMutation({
    mutationFn: async () => renameList(uid!, editingListId!, editingTitle),
    onSuccess: () => {
      setEditingListId(null);
      setEditingTitle("");
      queryClient.invalidateQueries({ queryKey: key });
    }
  });

  const deleteMutation = useMutation({
    mutationFn: async (listId: string) => removeList(uid!, listId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: key });
    }
  });

  if (loading) {
    return (
      <Screen>
        <View style={styles.center}>
          <AppText>Carregando...</AppText>
        </View>
      </Screen>
    );
  }

  if (!user) {
    router.replace("/(auth)/sign-in");
    return null;
  }

  return (
    <Screen scrollable>
      <View style={styles.container}>
        <AppText style={styles.title}>Minhas listas</AppText>
        <View style={styles.row}>
          <AppInput
            style={styles.flex}
            placeholder="Nova lista"
            value={newListTitle}
            onChangeText={setNewListTitle}
          />
          <AppButton
            label="+"
            onPress={() => createMutation.mutate()}
            disabled={!newListTitle.trim() || createMutation.isPending}
          />
        </View>

        {listsQuery.data?.length ? (
          <FlatList
            data={listsQuery.data}
            keyExtractor={(item) => item.id}
            scrollEnabled={false}
            ItemSeparatorComponent={() => <View style={{ height: 10 }} />}
            renderItem={({ item }) => (
              <Card>
                {editingListId === item.id ? (
                  <>
                    <AppInput value={editingTitle} onChangeText={setEditingTitle} />
                    <View style={styles.row}>
                      <AppButton label="Salvar" onPress={() => renameMutation.mutate()} />
                      <AppButton label="Cancelar" variant="ghost" onPress={() => setEditingListId(null)} />
                    </View>
                  </>
                ) : (
                  <>
                    <Pressable onPress={() => router.push(`/(app)/lists/${item.id}`)}>
                      <AppText style={styles.listTitle}>{item.title}</AppText>
                    </Pressable>
                    <View style={styles.row}>
                      <AppButton
                        label="Editar"
                        variant="ghost"
                        onPress={() => {
                          setEditingListId(item.id);
                          setEditingTitle(item.title);
                        }}
                      />
                      <AppButton
                        label="Excluir"
                        variant="danger"
                        onPress={() => deleteMutation.mutate(item.id)}
                        disabled={deleteMutation.isPending}
                      />
                    </View>
                  </>
                )}
              </Card>
            )}
          />
        ) : (
          <Card>
            <AppText style={styles.empty}>Nenhuma lista criada ainda.</AppText>
          </Card>
        )}

        <AppButton label="Sair" variant="ghost" onPress={signOutSession} />
      </View>
    </Screen>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    gap: 12
  },
  center: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center"
  },
  title: {
    fontSize: 24,
    fontWeight: "700"
  },
  row: {
    flexDirection: "row",
    alignItems: "center",
    gap: 8
  },
  flex: {
    flex: 1
  },
  listTitle: {
    fontSize: 18,
    fontWeight: "600"
  },
  empty: {
    color: colors.textSecondary
  }
});
