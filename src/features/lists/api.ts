import {
  addDoc,
  collection,
  deleteDoc,
  doc,
  getDocs,
  orderBy,
  query,
  serverTimestamp,
  updateDoc
} from "firebase/firestore";
import { db } from "@/services/firebase/config";
import { CreateListInput, TodoList } from "@/features/lists/types";

const listsPath = (uid: string) => collection(db, "users", uid, "lists");

export async function fetchLists(uid: string): Promise<TodoList[]> {
  const snapshot = await getDocs(query(listsPath(uid), orderBy("order", "asc")));
  return snapshot.docs.map((item) => {
    const value = item.data();
    return {
      id: item.id,
      title: value.title,
      color: value.color,
      order: value.order ?? 0,
      createdAt: value.createdAt?.toMillis?.(),
      updatedAt: value.updatedAt?.toMillis?.()
    } satisfies TodoList;
  });
}

export async function createList(uid: string, input: CreateListInput, order: number) {
  const title = input.title.trim();
  if (!title) {
    throw new Error("List title is required.");
  }

  await addDoc(listsPath(uid), {
    title,
    color: input.color ?? null,
    order,
    createdAt: serverTimestamp(),
    updatedAt: serverTimestamp()
  });
}

export async function renameList(uid: string, listId: string, title: string) {
  const value = title.trim();
  if (!value) {
    throw new Error("List title is required.");
  }

  await updateDoc(doc(db, "users", uid, "lists", listId), {
    title: value,
    updatedAt: serverTimestamp()
  });
}

export async function removeList(uid: string, listId: string) {
  await deleteDoc(doc(db, "users", uid, "lists", listId));
}
