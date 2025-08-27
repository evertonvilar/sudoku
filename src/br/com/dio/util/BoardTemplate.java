package br.com.dio.util;

import br.com.dio.model.Board;

public final class BoardTemplate {

    private BoardTemplate() {}

    public static String render(Board board) {
        StringBuilder sb = new StringBuilder();
        String hSep = "+=======+=======+=======+";
        String hSepThin = "+-------+-------+-------+";

        // Cabeçalho de colunas
        sb.append("    ");
        for (int x = 0; x < 9; x++) sb.append(x).append(" ");
        sb.append('\n');

        for (int y = 0; y < 9; y++) {
            if (y % 3 == 0) {
                sb.append("  ").append(hSep).append('\n');
            } else {
                sb.append("  ").append(hSepThin).append('\n');
            }
            sb.append(y).append(" | ");
            for (int x = 0; x < 9; x++) {
                Integer v = board.getSpace(x, y).getActual();
                sb.append(v == null ? "." : v.toString());
                if (x % 3 == 2) {
                    sb.append(" | ");
                } else {
                    sb.append(" ");
                }
            }
            sb.append(y).append('\n');
        }
        sb.append("  ").append(hSep).append('\n');

        // Rodapé de colunas
        sb.append("    ");
        for (int x = 0; x < 9; x++) sb.append(x).append(" ");
        sb.append('\n');
        return sb.toString();
    }
}