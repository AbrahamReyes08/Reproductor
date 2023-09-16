/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package reproductor;

import com.sun.media.MediaPlayer;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


/**
 *
 * @author dell
 */
public class Reproductor implements ActionListener {
    static ArrayList<Cancion> listaCanciones = new ArrayList<>();
    private String posicionActual;
    ReproductorVista vista = new ReproductorVista();
    private static String carpetaCanciones = "Audios";
    static String cancionSeleccionada;
    static Player reproductor;
    private static boolean detener=false;
    static Clip clip;
    
    public Reproductor() {
        vista.setVisible(true);
        añadirActionEvents();
    }

    private void añadirActionEvents() {
        ReproductorVista.add.addActionListener(this);
        ReproductorVista.stop.addActionListener(this);
        ReproductorVista.play.addActionListener(this);
        ReproductorVista.select.addActionListener(this);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        posicionActual = getBotonPosicionString(ae.getSource());
        cargarCanciones();
        if (posicionActual!=null && posicionActual.equals("add")) {
            add();
        }
        if (posicionActual!=null && posicionActual.equals("play")) {
            try {
                play();
            } catch (FileNotFoundException ex) {
                System.out.println("error");
            } catch (JavaLayerException ex) {
                System.out.println("Error");
            } catch (UnsupportedAudioFileException ex) {
                Logger.getLogger(Reproductor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Reproductor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LineUnavailableException ex) {
                Logger.getLogger(Reproductor.class.getName()).log(Level.SEVERE, null, ex);
            }
            posicionActual=null;
        }
        if (posicionActual!=null && posicionActual.equals("stop")) {
            stop();
            
        }
        if (posicionActual!=null && posicionActual.equals("select")) {
            select();
        }
    }

    public static void add() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            String nombreCancion = fileChooser.getSelectedFile().getName();
            
            if (nombreCancion.toLowerCase().endsWith(".mp3") || nombreCancion.toLowerCase().endsWith(".wav") ) {
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
            } else {
            JOptionPane.showMessageDialog(null, "No se guardo el archivo");                
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se guardo ningun archivo");
        }
            
    }

   public static void play() throws FileNotFoundException, JavaLayerException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (cancionSeleccionada != null) {
            System.out.println(cancionSeleccionada);
            for (Cancion cancion : listaCanciones) {
                if (cancionSeleccionada.equals(cancion.getNombre())) {
                    String rutaCancion = cancion.getPath();
                    String song=cancion.toString();
                    if (song.endsWith(".mp3")) {
                      reproductor = new Player(new FileInputStream(rutaCancion));
                       reproductor.play();                    
                    } else {
                        File archivoAudio = new File(rutaCancion);
                        AudioInputStream audioInput = AudioSystem.getAudioInputStream(archivoAudio);

                        clip = AudioSystem.getClip();

                        clip.open(audioInput);
                        clip.start();  
                    }                    
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay canción seleccionada");
        }
    }

    public static void stop() {
        if (cancionSeleccionada.endsWith(".mp3")) {
            if (reproductor != null) {
            reproductor.close();
            reproductor = null;
            } else {
                JOptionPane.showMessageDialog(null, "No hay canción seleccionada");
            }
        } else {
             clip.stop();
        }
        
    } 


    public static void select() {
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
    
   public static void cargarCanciones() {
        listaCanciones.clear();
        File carpeta = new File(carpetaCanciones);

        if (carpeta.isDirectory()) {
            File[] archivos = carpeta.listFiles((dir, nombre) -> {
                String nombreLowerCase = nombre.toLowerCase();
                return nombreLowerCase.endsWith(".mp3") || nombreLowerCase.endsWith(".wav");
            });

            if (archivos != null) {
            for (File archivo : archivos) {
                String nombreArchivo = archivo.getName();
                int puntoIndex = nombreArchivo.lastIndexOf('.');
                if (puntoIndex > 0) {
                    String nombreCancion = nombreArchivo.substring(0, puntoIndex);
                    String ext = nombreArchivo.substring(puntoIndex);
                    String rutaCancion = archivo.getAbsolutePath();
                    String nombreArchivoSalida = nombreCancion + ext;
                    Cancion cancion = new Cancion(nombreArchivoSalida, rutaCancion);
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
        } else if (posicion.equals("select")) {
            return ReproductorVista.select;
        }
        return null;
    }
}
