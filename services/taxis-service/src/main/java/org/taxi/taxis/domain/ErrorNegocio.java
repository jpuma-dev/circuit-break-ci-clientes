package org.taxi.taxis.domain;

public class ErrorNegocio extends RuntimeException {
    public enum Tipo { NO_EXISTE, CONFLICTO, INVALIDO, NO_DISPONIBLE }
    private final Tipo tipo;
    public ErrorNegocio(Tipo tipo, String mensaje) { super(mensaje); this.tipo = tipo; }
    public Tipo tipo() { return tipo; }
}
