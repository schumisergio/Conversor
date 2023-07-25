/** 
 * Conversor de unidades de moneda y temperatura en un solo unico codigo funcional
 * 
 * @version 1.0
 * @author Schumisergio@gmail.com
 *
 */

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import javax.swing.*;

public class MiConversor {

    // Variables para los componentes de la interfaz gráfica
    private JFrame frmConversorDeUnidades;
    private JTextField txtInput;
    private JButton btnConvert;
    private JComboBox<Moneda> cmbCurrency;
    private JLabel lblResult;
    private JComboBox<Temperatura> cmbTemperature;
    private JLabel lblTemperatureResult;
    private JRadioButton rbtnCurrency;
    private JRadioButton rbtnTemperature;
    private ButtonGroup optionGroup;

    // Enumerado que representa las diferentes conversiones de monedas
    public enum Moneda {
        PESOS_DOLAR,
        PESOS_EURO,
        PESOS_LIBRA,
        PESOS_YEN,
        PESOS_WON,
        WON_PESOS,
        YEN_PESOS,
        DOLAR_PESOS,
        EURO_PESOS,
        LIBRA_PESOS;

        @Override
        public String toString() {
            switch (this) {
                case PESOS_DOLAR:
                    return "Pesos AR a Dólar";
                case PESOS_EURO:
                    return "Pesos AR a Euro";
                case PESOS_YEN:
                    return "Pesos AR a Yen";
                case PESOS_WON:
                    return "Pesos AR a Won";
                case PESOS_LIBRA:
                    return "Pesos AR a Libra";
                case YEN_PESOS:
                    return "Yen a Pesos AR";
                case WON_PESOS:
                    return "Won a Pesos AR";
                case DOLAR_PESOS:
                    return "Dólar a Pesos AR";
                case EURO_PESOS:
                    return "Euro a Pesos AR";
                case LIBRA_PESOS:
                    return "Libra a Pesos AR";
                default:
                    throw new IllegalArgumentException("Valor Inesperado: " + this);
            }
        }
    }

    // Enumerado que representa las diferentes conversiones de temperaturas
    public enum Temperatura {
        CELCIUS_FAHRENHEIT,
        CELCIUS_KELVIN,
        FAHRENHEIT,
        KELVIN,
        KELVIN_FAHRENHEIT,
        FAHRENHEIT_KELVIN;

        @Override
        public String toString() {
            switch (this) {
                case CELCIUS_FAHRENHEIT:
                    return "Celcius a Fahrenheit";
                case CELCIUS_KELVIN:
                    return "Celcius a Kelvin";
                case FAHRENHEIT:
                    return "Fahrenheit a Celcius";
                case KELVIN:
                    return "Kelvin a Celcius";
                case KELVIN_FAHRENHEIT:
                    return "Kelvin a Fahrenheit";
                case FAHRENHEIT_KELVIN:
                    return "Fahrenheit a Kelvin";
                default:
                    throw new IllegalArgumentException("Valor Inesperado: " + this);
            }
        }
    }

    // Tipos de cambio para las monedas actualizado al 24/07/23
    public double yen = 1.91;
    public double won =  0.21098;
    public double dolar = 560.15;
    public double euro = 299.90;
    public double libra = 347.19;

    public double valorInput = 0.00;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MiConversor window = new MiConversor();
                window.frmConversorDeUnidades.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public MiConversor() {
        initialize();
    }

    private void initialize() {
        frmConversorDeUnidades = new JFrame();
        frmConversorDeUnidades.getContentPane().setBackground(Color.LIGHT_GRAY);
        frmConversorDeUnidades.setBackground(Color.GRAY);
        frmConversorDeUnidades.setForeground(Color.BLACK);
        frmConversorDeUnidades.setTitle("Conversor by schumisergio@gmail.com");
        frmConversorDeUnidades.setBounds(100, 100, 450, 300);
        frmConversorDeUnidades.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmConversorDeUnidades.getContentPane().setLayout(null);

        // Campo de entrada
        txtInput = new JTextField();
        txtInput.setHorizontalAlignment(SwingConstants.CENTER);
        txtInput.setFont(new Font("Tahoma", Font.PLAIN, 16));
        txtInput.setBounds(52, 162, 146, 42);
        frmConversorDeUnidades.getContentPane().add(txtInput);
        txtInput.setColumns(10);

        // ComboBox para las conversiones de moneda
        cmbCurrency = new JComboBox<Moneda>();
        cmbCurrency.setFont(new Font("Tahoma", Font.PLAIN, 14));
        cmbCurrency.setModel(new DefaultComboBoxModel<Moneda>(Moneda.values()));
        int indexPesosEuro = getIndexForMoneda(Moneda.PESOS_EURO);
        cmbCurrency.setSelectedIndex(indexPesosEuro);
        cmbCurrency.setBounds(52, 94, 148, 30);
        frmConversorDeUnidades.getContentPane().add(cmbCurrency);

        // ComboBox para las conversiones de temperatura
        cmbTemperature = new JComboBox<Temperatura>();
        cmbTemperature.setFont(new Font("Tahoma", Font.PLAIN, 14));
        cmbTemperature.setModel(new DefaultComboBoxModel<Temperatura>(Temperatura.values()));
        cmbTemperature.setBounds(52, 93, 148, 32);
        frmConversorDeUnidades.getContentPane().add(cmbTemperature);
        cmbTemperature.setVisible(false);

        // Botón de conversión
        btnConvert = new JButton("Convertir");
        btnConvert.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnConvert.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                convertir();
            }
        });
        btnConvert.setBounds(242, 162, 137, 42);
        frmConversorDeUnidades.getContentPane().add(btnConvert);

        // Etiqueta para mostrar el resultado de la conversión de moneda
        lblResult = new JLabel("00.00");
        lblResult.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblResult.setBounds(242, 93, 181, 30);
        frmConversorDeUnidades.getContentPane().add(lblResult);

        // Etiqueta para mostrar el resultado de la conversión de temperatura
        lblTemperatureResult = new JLabel("00.00");
        lblTemperatureResult.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblTemperatureResult.setBounds(242, 93, 89, 31);
        frmConversorDeUnidades.getContentPane().add(lblTemperatureResult);
        lblTemperatureResult.setVisible(false);

        // Radio Button para conversiones de moneda
        rbtnCurrency = new JRadioButton("Moneda");
        rbtnCurrency.setBackground(Color.LIGHT_GRAY);
        rbtnCurrency.setFont(new Font("Tahoma", Font.PLAIN, 14));
        rbtnCurrency.setBounds(96, 25, 100, 20);
        frmConversorDeUnidades.getContentPane().add(rbtnCurrency);

        // Radio Button para conversiones de temperatura
        rbtnTemperature = new JRadioButton("Temperatura");
        rbtnTemperature.setBackground(Color.LIGHT_GRAY);
        rbtnTemperature.setFont(new Font("Tahoma", Font.PLAIN, 14));
        rbtnTemperature.setBounds(212, 25, 123, 20);
        frmConversorDeUnidades.getContentPane().add(rbtnTemperature);

        // Grupo de botones para los Radio Buttons
        optionGroup = new ButtonGroup();
        optionGroup.add(rbtnCurrency);
        optionGroup.add(rbtnTemperature);

        rbtnCurrency.setSelected(true);

        rbtnCurrency.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cmbCurrency.setVisible(true);
                lblResult.setVisible(true);
                cmbTemperature.setVisible(false);
                lblTemperatureResult.setVisible(false);
            }
        });

        rbtnTemperature.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cmbCurrency.setVisible(false);
                lblResult.setVisible(false);
                cmbTemperature.setVisible(true);
                lblTemperatureResult.setVisible(true);
            }
        });
    }

    private int getIndexForMoneda(Moneda moneda) {
        Moneda[] monedas = Moneda.values();
        for (int i = 0; i < monedas.length; i++) {
            if (monedas[i] == moneda) {
                return i;
            }
        }
        return -1;
    }

    public void convertir() {
        if (validarInput(txtInput.getText())) {
            if (rbtnCurrency.isSelected()) {
                if (cmbCurrency.getSelectedItem() instanceof Moneda) {
                    Moneda moneda = (Moneda) cmbCurrency.getSelectedItem();
                    switch (moneda) {
                        case PESOS_DOLAR:
                            convertirMoneda(dolar);
                            break;
                        case PESOS_EURO:
                            convertirMoneda(euro);
                            break;
                        case PESOS_LIBRA:
                            convertirMoneda(libra);
                            break;
                        case PESOS_YEN:
                            convertirMoneda(yen);
                            break;
                        case PESOS_WON:
                            convertirMoneda(won);
                            break;
                        case WON_PESOS:
                            convertirMonedaAPesos(won);
                            break;
                        case YEN_PESOS:
                            convertirMonedaAPesos(yen);
                            break;
                        case DOLAR_PESOS:
                            convertirMonedaAPesos(dolar);
                            break;
                        case EURO_PESOS:
                            convertirMonedaAPesos(euro);
                            break;
                        case LIBRA_PESOS:
                            convertirMonedaAPesos(libra);
                            break;
                        default:
                            throw new IllegalArgumentException("Valor Inesperado: " + moneda);
                    }
                }
            } else if (rbtnTemperature.isSelected()) {
                if (cmbTemperature.getSelectedItem() instanceof Temperatura) {
                    Temperatura temperatura = (Temperatura) cmbTemperature.getSelectedItem();
                    switch (temperatura) {
                        case CELCIUS_KELVIN:
                            convertirCelsiusAKelvin();
                            break;
                        case CELCIUS_FAHRENHEIT:
                            convertirCelsiusAFahrenheit();
                            break;
                        case FAHRENHEIT:
                            convertirTemperaturaAFahrenheit();
                            break;
                        case KELVIN:
                            convertirTemperaturaAKelvin();
                            break;
                        case KELVIN_FAHRENHEIT:
                            convertirKelvinAFahrenheit();
                            break;
                        case FAHRENHEIT_KELVIN:
                            convertirFahrenheitAKelvin();
                            break;
                        default:
                            throw new IllegalArgumentException("Valor Inesperado: " + temperatura);
                    }
                }
            }
        }
    }

    public void convertirMoneda(double valorMoneda) {
        double resultado = valorInput / valorMoneda;
        lblResult.setText(redondear(resultado));
    }

    public void convertirMonedaAPesos(double valorMoneda) {
        double resultado = valorInput * valorMoneda;
        lblResult.setText(redondear(resultado));
    }

    private void convertirCelsiusAKelvin() {
        double celsius = Double.parseDouble(txtInput.getText());
        double kelvin = celsius + 273.15;
        lblTemperatureResult.setText(redondear(kelvin));
    }

    private void convertirCelsiusAFahrenheit() {
        double celsius = Double.parseDouble(txtInput.getText());
        double fahrenheit = (celsius * 9 / 5) + 32;
        lblTemperatureResult.setText(redondear(fahrenheit));
    }

    private void convertirTemperaturaAFahrenheit() {
        double celsius = Double.parseDouble(txtInput.getText());
        double fahrenheit = (celsius * 9 / 5) + 32;
        lblTemperatureResult.setText(redondear(fahrenheit));
    }

    private void convertirTemperaturaAKelvin() {
        double celsius = Double.parseDouble(txtInput.getText());
        double kelvin = celsius + 273.15;
        lblTemperatureResult.setText(redondear(kelvin));
    }

    private void convertirKelvinAFahrenheit() {
        double kelvin = Double.parseDouble(txtInput.getText());
        double fahrenheit = (kelvin - 273.15) * 9 / 5 + 32;
        lblTemperatureResult.setText(redondear(fahrenheit));
    }

    private void convertirFahrenheitAKelvin() {
        double fahrenheit = Double.parseDouble(txtInput.getText());
        double kelvin = (fahrenheit - 32) * 5 / 9 + 273.15;
        lblTemperatureResult.setText(redondear(kelvin));
    }

    public String redondear(double valor) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(valor);
    }

    public boolean validarInput(String texto) {
        try {
            double x = Double.parseDouble(texto);
            if (rbtnTemperature.isSelected()) {
                valorInput = x;
                return true;
            } else if (rbtnCurrency.isSelected()) {
                if (x >= 0) {
                    valorInput = x;
                    return true;
                } else {
                    lblResult.setText("Ingrese un número positivo para la moneda");
                    return false;
                }
            } else {
                lblResult.setText("Seleccione Moneda o Temperatura");
                return false;
            }
        } catch (NumberFormatException e) {
            lblResult.setText("Solamente números!!");
            return false;
        }
    }
}
