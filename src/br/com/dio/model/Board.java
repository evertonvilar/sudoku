package br.com.dio.model;


import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

import java.util.Set;

public class Board {

    private final List<List<Space>> spaces;

    public Board(List<List<Space>> spaces) {
        if (spaces == null || spaces.size() != 9) {
            throw new IllegalArgumentException("Board deve ter 9 linhas.");
        }
        for (List<Space> row : spaces) {
            if (row == null || row.size() != 9) {
                throw new IllegalArgumentException("Cada linha deve ter 9 colunas.");
            }
        }
        this.spaces = spaces;
    }

    public Space getSpace(int x, int y) {
        return spaces.get(y).get(x);
    }

    public boolean set(int x, int y, int value) {
        if (value < 1 || value > 9) return false;
        Space s = getSpace(x, y);
        if (s.isFixed()) return false;

        if (conflictsAt(x, y, value)) return false;

        s.setActual(value);
        return true;
    }

    public boolean remove(int x, int y) {
        Space s = getSpace(x, y);
        if (s.isFixed()) return false;
        if (s.getActual() == null) return false;
        s.clearSpace();
        return true;
    }

    public boolean hasErrors() {
        // linhas
        for (int y = 0; y < 9; y++) {
            if (hasDuplicates(getRowValues(y))) return true;
        }
        // colunas
        for (int x = 0; x < 9; x++) {
            if (hasDuplicates(getColValues(x))) return true;
        }
        // subgrades 3x3
        for (int gy = 0; gy < 3; gy++) {
            for (int gx = 0; gx < 3; gx++) {
                if (hasDuplicates(getBoxValues(gx, gy))) return true;
            }
        }
        return false;
    }

    public GameStatusEnum getStatus() {
        boolean allNull = true;
        boolean anyNull = false;
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                Integer a = getSpace(x, y).getActual();
                if (a != null) {
                    allNull = false;
                } else {
                    anyNull = true;
                }
            }
        }
        if (allNull) return GameStatusEnum.NON_STARTED;
        if (anyNull) return GameStatusEnum.INCOMPLETE;
        return GameStatusEnum.COMPLETE;
    }

    public boolean gameIsFinished() {
        return !hasErrors() && getStatus() == GameStatusEnum.COMPLETE;
    }

    private boolean conflictsAt(int x, int y, int value) {
        // linha
        for (int cx = 0; cx < 9; cx++) {
            if (cx == x) continue;
            Integer a = getSpace(cx, y).getActual();
            if (a != null && a == value) return true;
        }
        // coluna
        for (int cy = 0; cy < 9; cy++) {
            if (cy == y) continue;
            Integer a = getSpace(x, cy).getActual();
            if (a != null && a == value) return true;
        }
        // subgrade 3x3
        int boxX = (x / 3) * 3;
        int boxY = (y / 3) * 3;
        for (int cy = boxY; cy < boxY + 3; cy++) {
            for (int cx = boxX; cx < boxX + 3; cx++) {
                if (cx == x && cy == y) continue;
                Integer a = getSpace(cx, cy).getActual();
                if (a != null && a == value) return true;
            }
        }
        return false;
    }

    private List<Integer> getRowValues(int y) {
        List<Integer> list = new ArrayList<>(9);
        for (int x = 0; x < 9; x++) {
            Integer a = getSpace(x, y).getActual();
            if (a != null) list.add(a);
        }
        return list;
    }

    private List<Integer> getColValues(int x) {
        List<Integer> list = new ArrayList<>(9);
        for (int y = 0; y < 9; y++) {
            Integer a = getSpace(x, y).getActual();
            if (a != null) list.add(a);
        }
        return list;
    }

    private List<Integer> getBoxValues(int boxX, int boxY) {
        List<Integer> list = new ArrayList<>(9);
        int startX = boxX * 3;
        int startY = boxY * 3;
        for (int y = startY; y < startY + 3; y++) {
            for (int x = startX; x < startX + 3; x++) {
                Integer a = getSpace(x, y).getActual();
                if (a != null) list.add(a);
            }
        }
        return list;
    }

    private boolean hasDuplicates(List<Integer> values) {
        Set<Integer> set = new HashSet<>();
        for (Integer v : values) {
            if (v < 1 || v > 9) return true; // inválido
            if (!set.add(v)) return true;    // duplicado
        }
        return false;
    }
}