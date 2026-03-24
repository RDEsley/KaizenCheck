import {
  addDoc,
  collection,
  doc,
  getDocs,
  orderBy,
  query,
  serverTimestamp,
  updateDoc
} from "firebase/firestore";
import { db } from "@/services/firebase/config";
import { CreateTaskInput, Task } from "@/features/tasks/types";

const tasksPath = (uid: string, listId: string) => collection(db, "users", uid, "lists", listId, "tasks");

export async function fetchTasks(uid: string, listId: string): Promise<Task[]> {
  const snapshot = await getDocs(query(tasksPath(uid, listId), orderBy("order", "asc")));
  const tasks = snapshot.docs.map((item) => {
    const value = item.data();
    return {
      id: item.id,
      title: value.title,
      notes: value.notes,
      dueDate: value.dueDate,
      status: value.status ?? "pending",
      order: value.order ?? 0,
      completedAt: value.completedAt?.toMillis?.() ?? null,
      createdAt: value.createdAt?.toMillis?.(),
      updatedAt: value.updatedAt?.toMillis?.()
    } satisfies Task;
  });

  return tasks.sort((a, b) => {
    if (a.status !== b.status) {
      return a.status === "pending" ? -1 : 1;
    }
    return a.order - b.order;
  });
}

export async function createTask(
  uid: string,
  listId: string,
  input: CreateTaskInput,
  order: number
) {
  const title = input.title.trim();
  if (!title) {
    throw new Error("Task title is required.");
  }

  await addDoc(tasksPath(uid, listId), {
    title,
    notes: input.notes?.trim() || null,
    dueDate: input.dueDate || null,
    status: "pending",
    order,
    completedAt: null,
    createdAt: serverTimestamp(),
    updatedAt: serverTimestamp()
  });
}

export async function toggleTaskStatus(
  uid: string,
  listId: string,
  taskId: string,
  status: "pending" | "completed"
) {
  await updateDoc(doc(db, "users", uid, "lists", listId, "tasks", taskId), {
    status,
    completedAt: status === "completed" ? serverTimestamp() : null,
    updatedAt: serverTimestamp()
  });
}

export async function updateTaskTitle(uid: string, listId: string, taskId: string, title: string) {
  const value = title.trim();
  if (!value) {
    throw new Error("Task title is required.");
  }

  await updateDoc(doc(db, "users", uid, "lists", listId, "tasks", taskId), {
    title: value,
    updatedAt: serverTimestamp()
  });
}
