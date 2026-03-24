export type TodoList = {
  id: string;
  title: string;
  color?: string;
  order: number;
  createdAt?: number;
  updatedAt?: number;
};

export type CreateListInput = {
  title: string;
  color?: string;
};
