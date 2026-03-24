import { create } from "zustand";

type ListsUiState = {
  selectedListId: string | null;
  setSelectedListId: (listId: string | null) => void;
};

export const useListsUiStore = create<ListsUiState>((set) => ({
  selectedListId: null,
  setSelectedListId: (selectedListId) => set({ selectedListId })
}));
