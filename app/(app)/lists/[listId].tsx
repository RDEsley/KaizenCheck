import { useMemo, useState } from "react";
import { FlatList, StyleSheet, View } from "react-native";
import { useLocalSearchParams } from "expo-router";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useAuthSession } from "@/features/auth/useAuthSession";
import { createTask, fetchTasks, toggleTaskStatus, updateTaskTitle } from "@/features/tasks/api";
import { Screen } from "@/shared/ui/Screen";
import { AppText } from "@/shared/ui/AppText";
import { AppInput } from "@/shared/ui/AppInput";
import { AppButton } from "@/shared/ui/AppButton";
import { Card } from "@/shared/ui/Card";
import { colors } from "@/shared/theme/colors";

export default function TasksScreen() {
  const { listId } = useLocalSearchParams<{ listId: string }>();
  const { user } = useAuthSession();
  const [newTaskTitle, setNewTaskTitle] = useState("");
  const [editingTaskId, setEditingTaskId] = useState<string | null>(null);
  const [editingTitle, setEditingTitle] = useState("");
  const queryClient = useQueryClient();

  const uid = user?.uid ?? null;
  const key = useMemo(() => ["tasks", uid, listId], [uid, listId]);

  const tasksQuery = useQuery({
    queryKey: key,
    queryFn: () => fetchTasks(uid!, listId),
    enabled: Boolean(uid && listId)
  });

  const createMutation = useMutation({
    mutationFn: async () => {
      const order = tasksQuery.data?.length ?? 0;
      await createTask(uid!, listId, { title: newTaskTitle }, order);
    },
    onSuccess: () => {
      setNewTaskTitle("");
      queryClient.invalidateQueries({ queryKey: key });
    }
  });

  const toggleMutation = useMutation({
    mutationFn: async (params: { taskId: string; status: "pending" | "completed" }) =>
      toggleTaskStatus(uid!, listId, params.taskId, params.status),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: key })
  });

  const renameMutation = useMutation({
    mutationFn: async () => updateTaskTitle(uid!, listId, editingTaskId!, editingTitle),
    onSuccess: () => {
      setEditingTaskId(null);
      setEditingTitle("");
      queryClient.invalidateQueries({ queryKey: key });
    }
  });

  if (!uid) {
    return (
      <Screen>
        <View style={styles.center}>
          <AppText>Autenticacao necessaria.</AppText>
        </View>
      </Screen>
    );
  }

  return (
    <Screen scrollable>
      <View style={styles.container}>
        <AppText style={styles.title}>Tarefas</AppText>
        <View style={styles.row}>
          <AppInput
            style={styles.flex}
            placeholder="Nova tarefa"
            value={newTaskTitle}
            onChangeText={setNewTaskTitle}
          />
          <AppButton
            label="+"
            onPress={() => createMutation.mutate()}
            disabled={!newTaskTitle.trim() || createMutation.isPending}
          />
        </View>

        {tasksQuery.data?.length ? (
          <FlatList
            data={tasksQuery.data}
            keyExtractor={(item) => item.id}
            scrollEnabled={false}
            ItemSeparatorComponent={() => <View style={{ height: 10 }} />}
            renderItem={({ item }) => (
              <Card>
                {editingTaskId === item.id ? (
                  <>
                    <AppInput value={editingTitle} onChangeText={setEditingTitle} />
                    <View style={styles.row}>
                      <AppButton label="Salvar" onPress={() => renameMutation.mutate()} />
                      <AppButton label="Cancelar" variant="ghost" onPress={() => setEditingTaskId(null)} />
                    </View>
                  </>
                ) : (
                  <>
                    <AppText style={item.status === "completed" ? styles.completedTitle : styles.taskTitle}>
                      {item.title}
                    </AppText>
                    <View style={styles.row}>
                      <AppButton
                        label={item.status === "completed" ? "Reabrir" : "Concluir"}
                        variant="ghost"
                        onPress={() =>
                          toggleMutation.mutate({
                            taskId: item.id,
                            status: item.status === "completed" ? "pending" : "completed"
                          })
                        }
                      />
                      <AppButton
                        label="Editar"
                        variant="ghost"
                        onPress={() => {
                          setEditingTaskId(item.id);
                          setEditingTitle(item.title);
                        }}
                      />
                    </View>
                  </>
                )}
              </Card>
            )}
          />
        ) : (
          <Card>
            <AppText style={styles.empty}>Nenhuma tarefa nesta lista.</AppText>
          </Card>
        )}
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
  taskTitle: {
    fontSize: 16,
    fontWeight: "600"
  },
  completedTitle: {
    fontSize: 16,
    textDecorationLine: "line-through",
    color: colors.textSecondary
  },
  empty: {
    color: colors.textSecondary
  }
});
