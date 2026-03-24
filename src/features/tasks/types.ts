export type TaskStatus = "pending" | "completed";

export type Task = {
  id: string;
  title: string;
  notes?: string;
  dueDate?: string;
  status: TaskStatus;
  order: number;
  completedAt?: number | null;
  createdAt?: number;
  updatedAt?: number;
};

export type CreateTaskInput = {
  title: string;
  notes?: string;
  dueDate?: string;
};
