package tareapaint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

public class Controlador {

    private Vista vista;
    private Modelo modelo;
    
    private int x1, y1;
    private int numClics = 0;
    
    private List<Integer> polIrregX = new ArrayList<>();
    private List<Integer> polIrregY = new ArrayList<>();

    private Color colorLinea = Color.BLACK;
    private Color colorRelleno = Color.WHITE;

    public Controlador(Vista vista, Modelo modelo) {
        this.vista = vista;
        this.modelo = modelo;
        inicializarEventos();
    }

    private void inicializarEventos() {
        vista.getBtnColorLinea().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color nuevoColor = JColorChooser.showDialog(null, "Elige el color de la linea", colorLinea);
                if (nuevoColor != null) {
                    colorLinea = nuevoColor;
                }
            }
        });

        vista.getBtnColorRelleno().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color nuevoColor = JColorChooser.showDialog(null, "Elige el color de relleno", colorRelleno);
                if (nuevoColor != null) {
                    colorRelleno = nuevoColor;
                }
            }
        });

        vista.getBtnGuardarBD().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelo.guardarEnBD();
                System.out.println("Lienzo guardado en la base de datos.");
            }
        });

        vista.getBtnCargarBD().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelo.cargarDeBD();
                vista.setFiguras(modelo.getFiguras());
                System.out.println("Lienzo cargado desde la base de datos.");
            }
        });

        vista.getBtnLimpiar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelo.limpiarLienzo();
                vista.setFiguras(modelo.getFiguras());
                System.out.println("Lienzo limpiado.");
            }
        });

        vista.getBtnExportarSVG().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Guardar archivo SVG");
                int userSelection = fileChooser.showSaveDialog(vista);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    String ruta = fileChooser.getSelectedFile().getAbsolutePath();
                    if (!ruta.toLowerCase().endsWith(".svg")) {
                        ruta += ".svg";
                    }
                    modelo.exportarSVG(ruta);
                    System.out.println("SVG exportado en: " + ruta);
                }
            }
        });

        vista.getCanvasPintura().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                String figuraSeleccionada = (String) vista.getComboFiguras().getSelectedItem();
                
                Graphics g = vista.getCanvasPintura().getGraphics();
                g.setColor(colorLinea);

                if (!figuraSeleccionada.equals("Polígono Irregular")) {
                    polIrregX.clear();
                    polIrregY.clear();
                }

                if (figuraSeleccionada.equals("Punto")) {
                    Figura f = new Figura("Punto", x, y, colorLinea, colorRelleno);
                    modelo.agregarFigura(f);
                    vista.setFiguras(modelo.getFiguras());
                    numClics = 0;
                    
                } else if (figuraSeleccionada.equals("Recta")) {
                    if (numClics == 0) {
                        x1 = x;
                        y1 = y;
                        numClics = 1;
                    } else if (numClics == 1) {
                        Figura f = new Figura("Recta", x1, y1, colorLinea, colorRelleno);
                        f.setX2(x);
                        f.setY2(y);
                        modelo.agregarFigura(f);
                        vista.setFiguras(modelo.getFiguras());
                        numClics = 0;
                    }
                    
                } else if (figuraSeleccionada.equals("Circunferencia")) {
                    if (numClics == 0) {
                        x1 = x;
                        y1 = y;
                        numClics = 1;
                    } else if (numClics == 1) {
                        int r = (int) Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2));
                        Figura f = new Figura("Circunferencia", x1, y1, colorLinea, colorRelleno);
                        f.setR(r);
                        modelo.agregarFigura(f);
                        vista.setFiguras(modelo.getFiguras());
                        numClics = 0;
                    }
                    
                } else if (figuraSeleccionada.equals("Polígono Regular")) {
                    if (numClics == 0) {
                        x1 = x;
                        y1 = y;
                        numClics = 1;
                    } else if (numClics == 1) {
                        int lados = vista.getSliderVertices().getValue();
                        double r = Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2));
                        double anguloInicial = Math.atan2(y - y1, x - x1);
                        
                        int[] xPoints = new int[lados];
                        int[] yPoints = new int[lados];
                        
                        for (int i = 0; i < lados; i++) {
                            double anguloActual = anguloInicial + i * (2 * Math.PI / lados);
                            xPoints[i] = (int) Math.round(x1 + r * Math.cos(anguloActual));
                            yPoints[i] = (int) Math.round(y1 + r * Math.sin(anguloActual));
                        }
                        
                        Figura f = new Figura("Polígono Regular", x1, y1, colorLinea, colorRelleno);
                        f.setXPoints(xPoints);
                        f.setYPoints(yPoints);
                        f.setLados(lados);
                        modelo.agregarFigura(f);
                        vista.setFiguras(modelo.getFiguras());
                        numClics = 0;
                    }
                    
                } else if (figuraSeleccionada.equals("Polígono Irregular")) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        if (polIrregX.size() > 2) {
                            int lastX = polIrregX.get(polIrregX.size() - 1);
                            int lastY = polIrregY.get(polIrregY.size() - 1);
                            int firstX = polIrregX.get(0);
                            int firstY = polIrregY.get(0);
                            
                            boolean cruza = validarIntersecciones(lastX, lastY, firstX, firstY, polIrregX, polIrregY, true);
                            
                            if (!cruza) {
                                int n = polIrregX.size();
                                int[] xP = new int[n];
                                int[] yP = new int[n];
                                for(int i = 0; i < n; i++) {
                                    xP[i] = polIrregX.get(i);
                                    yP[i] = polIrregY.get(i);
                                }
                                
                                Figura f = new Figura("Polígono Irregular", 0, 0, colorLinea, colorRelleno);
                                f.setXPoints(xP);
                                f.setYPoints(yP);
                                f.setLados(n);
                                modelo.agregarFigura(f);
                                vista.setFiguras(modelo.getFiguras());
                            }
                        }
                        polIrregX.clear();
                        polIrregY.clear();
                    } else {
                        if (!polIrregX.isEmpty()) {
                            int lastX = polIrregX.get(polIrregX.size() - 1);
                            int lastY = polIrregY.get(polIrregY.size() - 1);
                            
                            boolean cruza = validarIntersecciones(lastX, lastY, x, y, polIrregX, polIrregY, false);
                            
                            if (!cruza) {
                                g.drawLine(lastX, lastY, x, y);
                                polIrregX.add(x);
                                polIrregY.add(y);
                            }
                        } else {
                            polIrregX.add(x);
                            polIrregY.add(y);
                            g.fillOval(x - 2, y - 2, 4, 4);
                        }
                    }
                }
            }
        });
    }

    private boolean validarIntersecciones(int rx1, int ry1, int rx2, int ry2, List<Integer> px, List<Integer> py, boolean cerrando) {
        int n = px.size();
        int limite = cerrando ? n - 1 : n - 2;
        
        for (int i = 0; i < limite; i++) {
            int rx3 = px.get(i);
            int ry3 = py.get(i);
            int rx4 = px.get(i + 1);
            int ry4 = py.get(i + 1);
            
            if (Line2D.linesIntersect(rx1, ry1, rx2, ry2, rx3, ry3, rx4, ry4)) {
                if (rx1 == rx4 && ry1 == ry4) continue;
                if (rx2 == rx3 && ry2 == ry3) continue;
                return true;
            }
        }
        return false;
    }
}