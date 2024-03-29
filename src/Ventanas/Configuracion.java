package Ventanas;

import Conexiones.Arduino;
import Conexiones.FileManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

public class Configuracion extends JDialog {

    private boolean estadoComandos;

    private JPanel panel;

    private JLabel JLbluetooth, JLbluetoothCOM, JLcomandos, JLrutaGuardado;
    private JLabel fondo;

    private JComboBox puertosCOM;

    private Button btnSalir;
    private Button btnAceptar;
    private Button btnCambiarRuta;
    private Button btnAplicar;

    private JToggleButton btnComandos;
    private JToggleButton btnBluetooth;

    private JTextField ruta;

    private FileManager manager;
    private Arduino ino;
    private boolean comandos;
    private Inicio inicio;

    private String[] config;

    /*
    * config[0] = Ruta de guardado predeterminada
    * config[1] = puerto COM predeterminado
    * config[2] = estado del bluetooth
    * config[3] = comandos de voz (activado/desactivado)
     */
    public Configuracion(Inicio frame, FileManager manager, Arduino ino) {
        super(frame, true);
        this.inicio = frame;
        setTitle("Configuración");
        //Manejador de Archivos.
        this.manager = manager;
        //Arduino
        this.ino = ino;
        //Arreglo de la configuracion
        config = new String[4];

        //Icono
        //setIconImage(new ImageIcon(getClass().getResource("../Imagenes/Icono_VoB.png")).getImage());
        try {
            BufferedImage imglogo;
            imglogo = ImageIO.read(getClass().getClassLoader().getResource("Imagenes/Icono_VoB.png"));
            setIconImage(imglogo);
        } catch (IOException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Tamaño de la pantalla
        Dimension pantalla;
        pantalla = Toolkit.getDefaultToolkit().getScreenSize();

        //Tamaño de la ventana
        setSize((pantalla.width * 65) / 100, (pantalla.height * 80) / 100);
        setResizable(false);
        Dimension ventana = getSize();
        //Inicializar los componentes internos de la ventana       
        inicializarComponentes(ventana);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void inicializarComponentes(Dimension ventana) {

        panel = new JPanel();
        panel.setBounds(10, 10, (ventana.width * 85) / 100, (ventana.height * 75) / 100);
        panel.setLocation((ventana.width - panel.getWidth()) * 50 / 100, (ventana.height - panel.getHeight()) * 10 / 100);
        panel.setBackground(new Color(0, 0, 0, 0));
        panel.setLayout(null);

        JLabel titulo = new JLabel("Configuración");
        titulo.setBounds(10, 10, ventana.width, ventana.height * 10 / 100);
        titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titulo.setFont(new Font("Times New Roman", Font.BOLD, 38));
        titulo.setLocation((ventana.width - titulo.getWidth()) / 2, (ventana.height - titulo.getHeight()) * 5 / 100);
        getContentPane().add(titulo);

        JLrutaGuardado = new JLabel("Ruta de Guardado de archivos");
        JLrutaGuardado.setBounds(10, 10, panel.getWidth(), panel.getHeight() * 10 / 100);
        JLrutaGuardado.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        JLrutaGuardado.setLocation((panel.getWidth() - JLrutaGuardado.getWidth()) * 10 / 100, (panel.getHeight() - JLrutaGuardado.getHeight()) * 25 / 100);
        JLrutaGuardado.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        panel.add(JLrutaGuardado);

        ruta = new JTextField();
        ruta.setBounds(10, 10, panel.getWidth() * 50 / 100, panel.getHeight() * 8 / 100);
        ruta.setLocation((panel.getWidth() - ruta.getWidth()) * 80 / 100, (panel.getHeight() - ruta.getHeight()) * 25 / 100);
        ruta.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        ruta.setEditable(false);
        ruta.setText(FileManager.leerConfiguracion(0));
        config[0] = manager.getDirectorioPorDefecto().getAbsolutePath();
        panel.add(ruta);

        btnCambiarRuta = new Button();
        btnCambiarRuta.setText("...");
        btnCambiarRuta.setBounds(10, 10, panel.getWidth() * 6 / 100, ruta.getHeight());
        btnCambiarRuta.setLocation(ruta.getLocation().x + ruta.getWidth(), (panel.getHeight() - btnCambiarRuta.getHeight()) * 25 / 100);
        btnCambiarRuta.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        btnCambiarRuta.setColor1(Color.WHITE); //Color superior
        btnCambiarRuta.setColor2(Color.BLACK); //Color inferior
        btnCambiarRuta.setColor3(Color.WHITE); //Color de borde
        btnCambiarRuta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                File directorio = manager.elegirDirectorio();
                if (directorio != null) {
                    manager.setDirectorioPorDefecto(directorio);
                    ruta.setText(directorio.getAbsolutePath());
                    config[0] = ruta.getText();
                    btnAplicar.setEnabled(true);
                }
            }
        });
        panel.add(btnCambiarRuta);

        JLbluetoothCOM = new JLabel("Puerto Bluetooth Predeterminado");
        JLbluetoothCOM.setBounds(10, 10, panel.getWidth(), panel.getHeight() * 10 / 100);
        JLbluetoothCOM.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        JLbluetoothCOM.setLocation((panel.getWidth() - JLbluetoothCOM.getWidth()) * 10 / 100, (panel.getHeight() - JLbluetoothCOM.getHeight()) * 45 / 100);
        JLbluetoothCOM.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        panel.add(JLbluetoothCOM);

        puertosCOM = new JComboBox();
        puertosCOM.setBounds(10, 10, panel.getWidth() * 30 / 100, (panel.getHeight() * 10) / 100);
        puertosCOM.setLocation((panel.getWidth() - puertosCOM.getWidth()) * 60 / 100, (panel.getHeight() - puertosCOM.getHeight()) * 45 / 100);
        for (int x = 0; x < ino.getPortsAvailable(); x++) {
            puertosCOM.addItem(ino.getSerialPorts().get(x));
        }
        puertosCOM.setSelectedItem(FileManager.leerConfiguracion(1));
        puertosCOM.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        puertosCOM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                btnAplicar.setEnabled(true);
            }
        });
        panel.add(puertosCOM);
        /*
        JLbluetooth = new JLabel("Bluetooth");
        JLbluetooth.setBounds(10, 10, panel.getWidth(), panel.getHeight() * 10 / 100);
        JLbluetooth.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        JLbluetooth.setLocation((panel.getWidth() - JLbluetooth.getWidth()) * 10 / 100, (panel.getHeight() - JLbluetooth.getHeight()) * 65 / 100);
        JLbluetooth.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        panel.add(JLbluetooth);

        btnBluetooth = new JToggleButton();
        btnBluetooth.setBounds(10, 10, panel.getWidth() * 30 / 100, panel.getHeight() * 10 / 100);
        btnBluetooth.setLocation((panel.getWidth() - btnBluetooth.getWidth()) * 60 / 100, (panel.getHeight() - btnBluetooth.getHeight()) * 65 / 100);
        //Seleccion de la informacion de acuerdo a un archivo de configuraciones
        if (FileManager.leerConfiguracion(2).equals("1")) {
            btnBluetooth.setSelected(true);
            btnBluetooth.setText("ACTIVADO");
            config[2] = "1";
        }
        if (FileManager.leerConfiguracion(2).equals("0")) {
            btnBluetooth.setSelected(false);
            btnBluetooth.setText("DESACTIVADO");
            config[2] = "0";
        }
        btnBluetooth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (btnBluetooth.isSelected()) {
                    btnBluetooth.setText("ACTIVADO");
                    config[2] = "1";
                    btnAplicar.setEnabled(true);
                } else {
                    btnBluetooth.setText("DESACTIVADO");
                    config[2] = "0";
                    btnAplicar.setEnabled(true);
                }
            }
        });
        btnBluetooth.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        panel.add(btnBluetooth);
         */
        JLcomandos = new JLabel("Comandos de Voz");
        JLcomandos.setBounds(10, 10, panel.getWidth(), panel.getHeight() * 10 / 100);
        JLcomandos.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        JLcomandos.setLocation((panel.getWidth() - JLcomandos.getWidth()) * 10 / 100, (panel.getHeight() - JLcomandos.getHeight()) * 85 / 100);
        JLcomandos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        panel.add(JLcomandos);

        btnComandos = new JToggleButton();
        btnComandos.setBounds(10, 10, panel.getWidth() * 30 / 100, panel.getHeight() * 10 / 100);
        btnComandos.setLocation((panel.getWidth() - btnComandos.getWidth()) * 60 / 100, (panel.getHeight() - btnComandos.getHeight()) * 85 / 100);
        //Seleccion de la informacion de acuerdo a un archivo de configuraciones
        if (FileManager.leerConfiguracion(3).equals("1")) {
            btnComandos.setSelected(true);
            btnComandos.setText("ACTIVADO");
            config[3] = "1";
            comandos = true;
        }
        if (FileManager.leerConfiguracion(3).equals("0")) {
            btnComandos.setSelected(false);
            btnComandos.setText("DESACTIVADO");
            config[3] = "0";
            comandos = false;
        }
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        btnComandos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (btnComandos.isSelected()) {
                    btnComandos.setText("ACTIVADO");
                    config[3] = "1";
                    btnAplicar.setEnabled(true);
                    comandos = true;
                } else {
                    btnComandos.setText("DESACTIVADO");
                    config[3] = "0";
                    btnAplicar.setEnabled(true);
                    comandos = false;
                }
            }
        });
        btnComandos.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        panel.add(btnComandos);

        btnAplicar = new Button();
        btnAplicar.setText("Aplicar");
        btnAplicar.setBounds(10, 10, panel.getWidth() * 20 / 100, panel.getHeight() * 10 / 100);
        btnAplicar.setLocation((ventana.width - btnAplicar.getWidth()) * 80 / 100, (ventana.height - btnAplicar.getHeight()) * 90 / 100);
        btnAplicar.setColor1(Color.WHITE); //Color superior
        btnAplicar.setColor2(Color.BLACK); //Color inferior
        btnAplicar.setColor3(Color.WHITE); //Color de borde
        btnAplicar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (puertosCOM.getItemCount() > 0) {
                    config[1] = puertosCOM.getSelectedItem().toString();
                } else {
                    config[1] = FileManager.leerConfiguracion(1);
                }
                FileManager.escribirConfiguracion(config);
                btnAplicar.setEnabled(false);
                btnAceptar.setEnabled(true);
                if (comandos) {
                    inicio.reaunudarComandos();
                    comandos = false;
                    //inicio.hablar("comandos Activados");
                } else {
                    inicio.suspenderComandos();
                    comandos = false;
                    //inicio.hablar("comandos desactivados");
                }
            }
        });
        btnAplicar.setEnabled(false);
        getContentPane().add(btnAplicar);

        btnSalir = new Button();
        btnSalir.setText("Salir");
        btnSalir.setBounds(10, 10, panel.getWidth() * 20 / 100, panel.getHeight() * 10 / 100);
        btnSalir.setLocation((ventana.width - btnSalir.getWidth()) * 50 / 100, (ventana.height - btnSalir.getHeight()) * 90 / 100);
        btnSalir.setColor1(Color.WHITE); //Color superior
        btnSalir.setColor2(Color.BLACK); //Color inferior
        btnSalir.setColor3(Color.WHITE); //Color de borde
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (btnAplicar.isEnabled()) {
                    int resp = JOptionPane.showConfirmDialog(null, "¿Desea Guardar los cambios realizados?");
                    if (resp == JOptionPane.YES_OPTION) {
                        FileManager.escribirConfiguracion(config);
                        if (comandos) {
                            inicio.reaunudarComandos();
                            comandos = false;
                            //inicio.hablar("comandos Activados");
                        } else {
                            inicio.suspenderComandos();
                            comandos = false;
                            //inicio.hablar("comandos desactivados");
                        }
                        dispose();
                    }
                    if (resp == JOptionPane.NO_OPTION) {
                        dispose();
                    }
                } else {
                    dispose();
                }
            }
        });
        getContentPane().add(btnSalir);

        btnAceptar = new Button();
        btnAceptar.setText("Aceptar");
        btnAceptar.setBounds(10, 10, panel.getWidth() * 20 / 100, panel.getHeight() * 10 / 100);
        btnAceptar.setLocation((ventana.width - btnAceptar.getWidth()) * 20 / 100, (ventana.height - btnAceptar.getHeight()) * 90 / 100);
        btnAceptar.setColor1(Color.WHITE); //Color superior
        btnAceptar.setColor2(Color.BLACK); //Color inferior
        btnAceptar.setColor3(Color.WHITE); //Color de borde
        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
        });
        btnAceptar.setEnabled(false);
        getContentPane().add(btnAceptar);

        //Se agrega el panel al contenedor
        getContentPane().add(panel);

//Inicializar el fondo
        fondo = new JLabel();
        fondo.setSize(ventana.width, ventana.height);
        //ImageIcon imagenFondo = new ImageIcon(getClass().getResource("../Imagenes/Fondo.jpg"));
        URL urlDelFondo = Splash.class.getClassLoader().getResource("Imagenes/fondo.jpg");
        ImageIcon imagenFondo = new ImageIcon(urlDelFondo);
        Icon iconoFondo = new ImageIcon(imagenFondo.getImage().getScaledInstance(fondo.getWidth(), fondo.getHeight(), Image.SCALE_DEFAULT));
        fondo.setIcon(iconoFondo);
        getContentPane().add(fondo);
    }

    public Configuracion getMe() {
        return this;
    }

    public FileManager getFileManager() {
        return manager;
    }

    public int cerrarVentana() {
        // Dispose 2
        // no salir 0
        if (btnAplicar.isEnabled()) {
            int resp = JOptionPane.showConfirmDialog(null, "¿Desea Guardar los cambios realizados?");
            if (resp == JOptionPane.YES_OPTION) {
                FileManager.escribirConfiguracion(config);
                return 2;
            }
            if (resp == JOptionPane.NO_OPTION) {
                return 0;
            }
        } else {
            return 2;
        }
        return 2;
    }
}
