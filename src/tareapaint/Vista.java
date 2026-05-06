package tareapaint;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Vista extends javax.swing.JFrame {

    private JPanel panelHerramientas;
    private JPanel canvasPintura;
    private JSlider sliderVertices;
    private JComboBox<String> comboFiguras;
    private JButton btnColorLinea;
    private JButton btnColorRelleno;
    private JButton btnGuardarBD;
    private JButton btnCargarBD;
    private JButton btnLimpiar;
    private JButton btnExportarSVG;
    private List<Figura> figuras;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Vista.class.getName());
    
    public Vista() {
        initComponents();
        figuras = new ArrayList<>();
        
        setTitle("Aplicación Paint MVC");
        setSize(1200, 700);
        setLayout(new BorderLayout());

        panelHerramientas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        String[] nombresFiguras = {"Punto", "Recta", "Circunferencia", "Polígono Regular", "Polígono Irregular"};
        comboFiguras = new JComboBox<>(nombresFiguras);
        panelHerramientas.add(comboFiguras);

        sliderVertices = new JSlider(3, 12, 5); 
        sliderVertices.setMajorTickSpacing(1);
        sliderVertices.setPaintTicks(true);
        sliderVertices.setPaintLabels(true);
        panelHerramientas.add(sliderVertices);

        btnColorLinea = new JButton("Color Línea");
        panelHerramientas.add(btnColorLinea);

        btnColorRelleno = new JButton("Color Relleno");
        panelHerramientas.add(btnColorRelleno);

        btnGuardarBD = new JButton("Guardar BD");
        panelHerramientas.add(btnGuardarBD);

        btnCargarBD = new JButton("Cargar BD");
        panelHerramientas.add(btnCargarBD);

        btnLimpiar = new JButton("Limpiar Lienzo");
        panelHerramientas.add(btnLimpiar);

        btnExportarSVG = new JButton("Exportar SVG");
        panelHerramientas.add(btnExportarSVG);

        canvasPintura = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Figura f : figuras) {
                    g.setColor(f.getColorLinea());
                    
                    if (f.getTipo().equals("Punto")) {
                        g.fillOval(f.getX1(), f.getY1(), 4, 4);
                        
                    } else if (f.getTipo().equals("Recta")) {
                        g.drawLine(f.getX1(), f.getY1(), f.getX2(), f.getY2());
                        
                    } else if (f.getTipo().equals("Circunferencia")) {
                        g.setColor(f.getColorRelleno());
                        g.fillOval(f.getX1() - f.getR(), f.getY1() - f.getR(), f.getR() * 2, f.getR() * 2);
                        g.setColor(f.getColorLinea());
                        g.drawOval(f.getX1() - f.getR(), f.getY1() - f.getR(), f.getR() * 2, f.getR() * 2);
                        
                    } else if (f.getTipo().equals("Polígono Regular") || f.getTipo().equals("Polígono Irregular")) {
                        g.setColor(f.getColorRelleno());
                        g.fillPolygon(f.getXPoints(), f.getYPoints(), f.getLados());
                        g.setColor(f.getColorLinea());
                        g.drawPolygon(f.getXPoints(), f.getYPoints(), f.getLados());
                    }
                }
            }
        };
        canvasPintura.setBackground(Color.WHITE);

        add(panelHerramientas, BorderLayout.NORTH);
        add(canvasPintura, BorderLayout.CENTER);
    }

    public void setFiguras(List<Figura> figuras) {
        this.figuras = figuras;
        canvasPintura.repaint();
    }

    public JPanel getCanvasPintura() { return canvasPintura; }
    public JComboBox<String> getComboFiguras() { return comboFiguras; }
    public JSlider getSliderVertices() { return sliderVertices; }
    public JButton getBtnColorLinea() { return btnColorLinea; }
    public JButton getBtnColorRelleno() { return btnColorRelleno; }
    public JButton getBtnGuardarBD() { return btnGuardarBD; }
    public JButton getBtnCargarBD() { return btnCargarBD; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JButton getBtnExportarSVG() { return btnExportarSVG; }
    
    @SuppressWarnings("unchecked")
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            Vista vista = new Vista();
            Modelo modelo = new Modelo();
            Controlador controlador = new Controlador(vista, modelo);
            vista.setVisible(true);
        });
    }
}
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

