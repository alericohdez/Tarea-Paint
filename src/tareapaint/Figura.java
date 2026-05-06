package tareapaint;

import java.awt.Color;

public class Figura {

    private String tipo;
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private int r;
    private int lados;
    private int[] xPoints;
    private int[] yPoints;
    private Color colorLinea;
    private Color colorRelleno;

    public Figura(String tipo, int x1, int y1, Color colorLinea, Color colorRelleno) {
        this.tipo = tipo;
        this.x1 = x1;
        this.y1 = y1;
        this.colorLinea = colorLinea;
        this.colorRelleno = colorRelleno;
    }

    public String getTipo() { return tipo; }
    public int getX1() { return x1; }
    public int getY1() { return y1; }
    public Color getColorLinea() { return colorLinea; }
    public Color getColorRelleno() { return colorRelleno; }

    public int getX2() { return x2; }
    public void setX2(int x2) { this.x2 = x2; }

    public int getY2() { return y2; }
    public void setY2(int y2) { this.y2 = y2; }

    public int getR() { return r; }
    public void setR(int r) { this.r = r; }

    public int getLados() { return lados; }
    public void setLados(int lados) { this.lados = lados; }

    public int[] getXPoints() { return xPoints; }
    public void setXPoints(int[] xPoints) { this.xPoints = xPoints; }

    public int[] getYPoints() { return yPoints; }
    public void setYPoints(int[] yPoints) { this.yPoints = yPoints; }
}
