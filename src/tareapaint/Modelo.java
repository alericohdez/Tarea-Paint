package tareapaint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;

public class Modelo {

    private List<Figura> figuras;
    private Connection conexion;

    public Modelo() {
        figuras = new ArrayList<>();
        inicializarBaseDatos();
    }

    public void inicializarBaseDatos() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conInicial = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");
            Statement stmt = conInicial.createStatement();
            
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS TareaPaint");
            stmt.close();
            conInicial.close();

            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TareaPaint", "root", "");
            Statement stmtPaint = conexion.createStatement();

            String sqlFigura = "CREATE TABLE IF NOT EXISTS figura (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "tipo VARCHAR(50), " +
                    "color_linea INT, " +
                    "color_relleno INT, " +
                    "x1 INT, " +
                    "y1 INT, " +
                    "x2 INT, " +
                    "y2 INT, " +
                    "r INT, " +
                    "lados INT" +
                    ")";
            stmtPaint.executeUpdate(sqlFigura);

            String sqlVertice = "CREATE TABLE IF NOT EXISTS vertice (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "id_figura INT, " +
                    "orden INT, " +
                    "x INT, " +
                    "y INT, " +
                    "FOREIGN KEY (id_figura) REFERENCES figura(id) ON DELETE CASCADE" +
                    ")";
            stmtPaint.executeUpdate(sqlVertice);

            stmtPaint.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void agregarFigura(Figura f) {
        figuras.add(f);
    }

    public List<Figura> getFiguras() {
        return figuras;
    }

    public void guardarEnBD() {
        try {
            Statement stmtBorrar = conexion.createStatement();
            stmtBorrar.executeUpdate("DELETE FROM figura");
            stmtBorrar.close();

            String sqlFig = "INSERT INTO figura (tipo, color_linea, color_relleno, x1, y1, x2, y2, r, lados) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmtFig = conexion.prepareStatement(sqlFig, Statement.RETURN_GENERATED_KEYS);

            String sqlVert = "INSERT INTO vertice (id_figura, orden, x, y) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmtVert = conexion.prepareStatement(sqlVert);

            for (Figura f : figuras) {
                pstmtFig.setString(1, f.getTipo());
                pstmtFig.setInt(2, f.getColorLinea().getRGB());
                pstmtFig.setInt(3, f.getColorRelleno().getRGB());
                pstmtFig.setInt(4, f.getX1());
                pstmtFig.setInt(5, f.getY1());
                pstmtFig.setInt(6, f.getX2());
                pstmtFig.setInt(7, f.getY2());
                pstmtFig.setInt(8, f.getR());
                pstmtFig.setInt(9, f.getLados());
                pstmtFig.executeUpdate();

                ResultSet rs = pstmtFig.getGeneratedKeys();
                if (rs.next()) {
                    int idFigura = rs.getInt(1);
                    if (f.getXPoints() != null && f.getYPoints() != null) {
                        for (int i = 0; i < f.getXPoints().length; i++) {
                            pstmtVert.setInt(1, idFigura);
                            pstmtVert.setInt(2, i);
                            pstmtVert.setInt(3, f.getXPoints()[i]);
                            pstmtVert.setInt(4, f.getYPoints()[i]);
                            pstmtVert.executeUpdate();
                        }
                    }
                }
            }
            pstmtFig.close();
            pstmtVert.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cargarDeBD() {
        figuras.clear();
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM figura ORDER BY id ASC");
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String tipo = rs.getString("tipo");
                Color cLinea = new Color(rs.getInt("color_linea"));
                Color cRelleno = new Color(rs.getInt("color_relleno"));
                
                Figura f = new Figura(tipo, rs.getInt("x1"), rs.getInt("y1"), cLinea, cRelleno);
                f.setX2(rs.getInt("x2"));
                f.setY2(rs.getInt("y2"));
                f.setR(rs.getInt("r"));
                f.setLados(rs.getInt("lados"));

                if (tipo.equals("Polígono Regular") || tipo.equals("Polígono Irregular")) {
                    PreparedStatement pstmtVert = conexion.prepareStatement("SELECT * FROM vertice WHERE id_figura = ? ORDER BY orden");
                    pstmtVert.setInt(1, id);
                    ResultSet rsVert = pstmtVert.executeQuery();
                    
                    List<Integer> tempX = new ArrayList<>();
                    List<Integer> tempY = new ArrayList<>();
                    while(rsVert.next()) {
                        tempX.add(rsVert.getInt("x"));
                        tempY.add(rsVert.getInt("y"));
                    }
                    
                    int[] xPoints = new int[tempX.size()];
                    int[] yPoints = new int[tempY.size()];
                    for(int i = 0; i < tempX.size(); i++) {
                        xPoints[i] = tempX.get(i);
                        yPoints[i] = tempY.get(i);
                    }
                    f.setXPoints(xPoints);
                    f.setYPoints(yPoints);
                    pstmtVert.close();
                }
                figuras.add(f);
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void limpiarLienzo() {
        figuras.clear();
    }

    public void exportarSVG(String rutaArchivo) {
        try {
            FileWriter writer = new FileWriter(rutaArchivo);
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            writer.write("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" width=\"2000\" height=\"2000\">\n");

            for (Figura f : figuras) {
                String stroke = String.format("#%06x", f.getColorLinea().getRGB() & 0x00FFFFFF);
                String fill = String.format("#%06x", f.getColorRelleno().getRGB() & 0x00FFFFFF);

                if (f.getTipo().equals("Punto")) {
                    writer.write("<circle cx=\"" + f.getX1() + "\" cy=\"" + f.getY1() + "\" r=\"2\" fill=\"" + stroke + "\" />\n");
                } else if (f.getTipo().equals("Recta")) {
                    writer.write("<line x1=\"" + f.getX1() + "\" y1=\"" + f.getY1() + "\" x2=\"" + f.getX2() + "\" y2=\"" + f.getY2() + "\" stroke=\"" + stroke + "\" stroke-width=\"1\" />\n");
                } else if (f.getTipo().equals("Circunferencia")) {
                    writer.write("<circle cx=\"" + f.getX1() + "\" cy=\"" + f.getY1() + "\" r=\"" + f.getR() + "\" fill=\"" + fill + "\" stroke=\"" + stroke + "\" stroke-width=\"1\" />\n");
                } else if (f.getTipo().equals("Polígono Regular") || f.getTipo().equals("Polígono Irregular")) {
                    StringBuilder points = new StringBuilder();
                    for (int i = 0; i < f.getLados(); i++) {
                        points.append(f.getXPoints()[i]).append(",").append(f.getYPoints()[i]).append(" ");
                    }
                    writer.write("<polygon points=\"" + points.toString().trim() + "\" fill=\"" + fill + "\" stroke=\"" + stroke + "\" stroke-width=\"1\" />\n");
                }
            }
            writer.write("</svg>");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}