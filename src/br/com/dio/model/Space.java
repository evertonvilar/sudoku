package br.com.dio.model;

public class Space {

    private final int expected;   // valor da pista (se fixed) ou 0
    private final boolean fixed;  // true se é pista fixa
    private Integer actual;       // valor atual colocado (null = vazio)

    public Space(int expected, boolean fixed) {
        this.expected = expected;
        this.fixed = fixed;
        if (fixed) {
            this.actual = expected == 0 ? null : expected; // pista visível
        } else {
            this.actual = null;
        }
    }

    public boolean isFixed() {
        return fixed;
    }

    public Integer getActual() {
        return actual;
    }

    public void setActual(Integer value) {
        if (fixed) return;
        this.actual = value;
    }

    public void clearSpace() {
        if (fixed) return;
        this.actual = null;
    }

    public int getExpected() {
        return expected;
    }
}