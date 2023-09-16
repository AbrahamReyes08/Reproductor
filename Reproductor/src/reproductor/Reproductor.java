/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package reproductor;

import com.sun.media.MediaPlayer;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.*;

/**
 *
 * @author dell
 */
public class Reproductor implements ActionListener {
    MediaPlayer mediaPlayer = new MediaPlayer();
    ArrayList<Cancion> listaCanciones = new ArrayList<>();
    private String posicionActual;
    ReproductorVista vista = new ReproductorVista();
    private String carpetaCanciones = "AudiosMP3";
    String cancionSeleccionada;
    AdvancedPlayer reproductor;
    
    public Reproductor() {
        vista.setVisible(true);
        añadirActionEvents();
    }

    private void añadirActionEvents() {
        ReproductorVista.add.addActionListener(this);
        ReproductorVista.stop.addActionListener(this);
        ReproductorVista.play.addActionListener(this);
        ReproductorVista.siguiente.addActionListener(this);
        ReproductorVista.select.addActionListener(this);
        ReproductorVista.atras.addActionListener(this);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        posicionActual = getBotonPosicionString(ae.getSource());
        cargarCanciones();
        if (posicionActual.equals("add")) {
            add();
        }
        if (posicionActual.equals("play")) {
            try {
                play();
            } catch (FileNotFoundException ex) {
                System.out.println("error");
            } catch (JavaLayerException ex) {
                System.out.println("Errir");
            }
        }
        if (posicionActual.equals("stop")) {
            stop();
        }
        if (posicionActual.equals("siguiente")) {
            siguiente();
        }
        if (posicionActual.equals("select")) {
            select();
        }
        if (posicionActual.equals("atras")) {
            atras();
        }
    }

    private void add() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            String nombreCancion = fileChooser.getSelectedFile().getName();
            
            if (nombreCancion.toLowerCase().endsWith(".mp3")) {
                File carpeta = new File(carpetaCanciones);
                if (!carpeta.exists()) {
                    carpeta.mkdirs();
                }

                File archivoDestino = new File(carpetaCanciones, nombreCancion);
                try {
                    Files.copy(Paths.get(filePath), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    Cancion cancion = new Cancion(nombreCancion, archivoDestino.getAbsolutePath());
                    listaCanciones.add(cancion);
                    JOptionPane.showMessageDialog(null, "El audio ha sido agregado");
                } catch (IOException e) {
                    System.out.println("Error");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Este archivo no se puede guardar");
        }
            
    }

    private void play() throws FileNotFoundException, JavaLayerException {
        if (cancionSeleccionada != null) {
            for (Cancion cancion : listaCanciones) {
                if (cancionSeleccionada.equals(cancion.getNombre())) {
                    String rutaCancion = cancion.getRuta();
                    
                    reproductor = new AdvancedPlayer(new FileInputStream(rutaCancion));
                    reproductor.play();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay canción seleccionada");
        }
    }

    private void stop() {
        
    }

    private void siguiente() {
        
    }

    private void select() {
        Object[] opciones = listaCanciones.toArray();

        if (opciones.length > 0) {
            Object seleccion = JOptionPane.showInputDialog(
                null,
                "Seleccione el audio que desea: ",
                "Audios Disponibles",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
            );

            if (seleccion != null) {
                cancionSeleccionada = ((Cancion) seleccion).getNombre();
            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay canciones disponibles.");
        }
        
    }

    private void atras() {

    }
    
    private void cargarCanciones() {
        listaCanciones.clear();
        File carpeta = new File(carpetaCanciones);

        if (carpeta.isDirectory()) {
            File[] archivos = carpeta.listFiles((dir, nombre) -> nombre.toLowerCase().endsWith(".mp3"));

            if (archivos != null) {
                for (File archivo : archivos) {
                    String nombreArchivo = archivo.getName();
                    int puntoIndex = nombreArchivo.lastIndexOf('.');
                    if (puntoIndex > 0) {
                        String nombreCancion = nombreArchivo.substring(0, puntoIndex);
                        String rutaCancion = archivo.getAbsolutePath();
                        Cancion cancion = new Cancion(nombreCancion, rutaCancion);
                        listaCanciones.add(cancion);
                    }
                }
            }
        }
    }

    public String getBotonPosicionString(Object boton) {
        if (boton == ReproductorVista.play) {
            return "play";
        } else if (boton == ReproductorVista.stop) {
            return "stop";
        } else if (boton == ReproductorVista.add) {
            return "add";
        } else if (boton == ReproductorVista.select) {
            return "select";
        } else if (boton == ReproductorVista.siguiente) {
            return "siguiente";
        }else if (boton == ReproductorVista.atras) {
            return "atras";
        }
        return null;
    }

    private JButton boton(String posicion) {
        if (posicion.equals("add")) {
            return ReproductorVista.add;
        }else if(posicion.equals("play")) {
            return ReproductorVista.play;
        }else if (posicion.equals("stop")) {
            return ReproductorVista.stop;
        } else if (posicion.equals("siguiente")) {
            return ReproductorVista.siguiente;
        } else if (posicion.equals("atras")) {
            return ReproductorVista.atras;
        } else if (posicion.equals("select")) {
            return ReproductorVista.select;
        }
        return null;
    }
}
    


/*
C:\Users\dell\Documents\NetBeansProjects\Reproductor\Reproductor\AudiosMP3\audioCR7.mp3
*/
