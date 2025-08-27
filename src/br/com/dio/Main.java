package br.com.dio;

import br.com.dio.model.Board;
import br.com.dio.model.Space;
import br.com.dio.util.BoardTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;   // <- IMPORT CERTO (com ;)
import java.util.stream.Stream;      // <- IMPORT CERTO (com ;)

// Remova QUALQUER static import que você tenha aqui (isNull, nonNull, toMap)

public class Main {

    private static final Scanner SC = new Scanner(System.in);

    public static void main(String[] args) {
        // Mapa: "x,y" -> "valor,isFixed"
        final Map<String, String> positions = Stream.of(args).collect(
                Collectors.toMap(
                        k -> k.split(";")[0].trim(),
                        v -> v.split(";")[1].trim()
                )
        );

        // Constrói grid 9x9 a partir dos argumentos
        List<List<Space>> spaces = buildSpacesFromArgs(positions);

        // Cria o tabuleiro
        Board board = new Board(spaces);

        // Loop do menu
        while (true) {
            System.out.println();
            System.out.println("=======================================");
            System.out.println(" Sudoku — Jogo no Terminal");
            System.out.println(" Status: " + board.getStatus() + (board.hasErrors() ? " (com erros)" : ""));
            System.out.println("=======================================");
            System.out.println();
            System.out.println(BoardTemplate.render(board)); // OK
            System.out.println();
            System.out.println("Menu:");
            System.out.println("1) Colocar novo número");
            System.out.println("2) Remover número");
            System.out.println("3) Visualizar tabuleiro");
            System.out.println("4) Sair");
            System.out.print("Escolha: ");

            int opt = runUntilGetValidNumber(1, 4);

            if (opt == 1) {
                putNumber(board);
            } else if (opt == 2) {
                removeNumber(board);
            } else if (opt == 3) {
                // reexibe no próximo laço
            } else {
                System.out.println("Saindo...");
                break;
            }

            if (board.gameIsFinished()) {
                System.out.println();
                System.out.println(BoardTemplate.render(board));
                System.out.println("Parabéns! Você concluiu o Sudoku sem erros. 🎉");
                break;
            }
        }
    }

    private static void putNumber(Board board) {
        System.out.print("Informe x (0..8): ");
        int x = runUntilGetValidNumber(0, 8);
        System.out.print("Informe y (0..8): ");
        int y = runUntilGetValidNumber(0, 8);

        Space space = board.getSpace(x, y);
        if (space.isFixed()) {
            System.out.println("Não é possível editar uma pista fixa!");
            return;
        }

        System.out.print("Informe o valor (1..9): ");
        int value = runUntilGetValidNumber(1, 9);

        boolean ok = board.set(x, y, value);
        System.out.println(ok ? "Valor inserido!" : "Movimento inválido (conflito em linha/coluna/subgrade).");
    }

    private static void removeNumber(Board board) {
        System.out.print("Informe x (0..8): ");
        int x = runUntilGetValidNumber(0, 8);
        System.out.print("Informe y (0..8): ");
        int y = runUntilGetValidNumber(0, 8);

        Space space = board.getSpace(x, y);
        if (space.isFixed()) {
            System.out.println("Não é possível remover um valor fixo!");
            return;
        }

        boolean ok = board.remove(x, y);
        System.out.println(ok ? "Valor removido!" : "Nada para remover.");
    }

    private static int runUntilGetValidNumber(int minInclusive, int maxInclusive) {
        while (true) {
            String line = SC.nextLine();
            try {
                int n = Integer.parseInt(line.trim());
                if (n < minInclusive || n > maxInclusive) {
                    System.out.print("Valor fora do intervalo. Tente novamente (" + minInclusive + ".." + maxInclusive + "): ");
                    continue;
                }
                return n;
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Tente novamente (" + minInclusive + ".." + maxInclusive + "): ");
            }
        }
    }

    private static List<List<Space>> buildSpacesFromArgs(Map<String, String> positions) {
        List<List<Space>> grid = new ArrayList<>(9);
        for (int y = 0; y < 9; y++) {
            List<Space> row = new ArrayList<>(9);
            for (int x = 0; x < 9; x++) {
                String key = x + "," + y;
                if (positions.containsKey(key)) {
                    String[] rf = positions.get(key).split(",");
                    int expected = safeParseInt(rf[0], 0);
                    boolean fixed = rf.length > 1 && Boolean.parseBoolean(rf[1]);
                    Space s = new Space(expected, fixed);
                    if (!fixed) s.clearSpace(); // não-fixa começa vazia
                    row.add(s);
                } else {
                    Space s = new Space(0, false);
                    s.clearSpace();
                    row.add(s);
                }
            }
            row = List.copyOf(row); // opcional, imutável
            grid.add(row);
        }
        return grid;
    }

    private static int safeParseInt(String s, int def) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return def;
        }
    }
}
